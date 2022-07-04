package link.tanxin.gateway.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import link.tanxin.common.utils.EnumUtil;
import link.tanxin.common.request.HttpStatusCn;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;

import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;

import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


/**
 * 在filter中获取前置预言里面的请求body
 *
 * @author tan
 */
@Component
@Slf4j

public class WrapperRequestGlobalFilter implements GlobalFilter, Ordered {

    private final ObjectMapper jsonMapper;
    private final String SYSTEM_ERROR_CN = "系统异常";
    private final String ERROR_TEXT_CN = "系统开小差了";

    public WrapperRequestGlobalFilter(ObjectMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String scheme = request.getURI()
                .getScheme();
        log.info("请求地址:{}", request.getURI());
        if (StringUtils.equals("websocket", scheme)) {
            return chain.filter(exchange);
        }
        //获取response的 返回数据
        ServerHttpResponse response = exchange.getResponse();
        //获取状态码
        HttpStatus statusCode = response.getStatusCode();
        if (statusCode == HttpStatus.OK) {
            //当然要复制一份请求啦，不然消费完就没了

            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {
                @Override
                public boolean setStatusCode(HttpStatus status) {
                    return super.setStatusCode(HttpStatus.OK);
                }

                @Override
                @NonNull
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

                    ServerHttpResponse delegateResponse = this.getDelegate();
                    MediaType contentType = delegateResponse.getHeaders()
                            .getContentType();
                    log.debug("contentType:{}", contentType);

                    if (null == contentType) {
                        ObjectNode node = buildResult(true, 200, "成功");
                        String resp = node.asText();
                        byte[] newRs = resp.getBytes(StandardCharsets.UTF_8);
                        delegateResponse.setStatusCode(HttpStatus.OK);
                        delegateResponse.getHeaders()
                                .setContentType(MediaType.APPLICATION_JSON);
                        delegateResponse.getHeaders()
                                .setContentLength(newRs.length);
                        DataBuffer buffer = delegateResponse.bufferFactory()
                                .wrap(newRs);
                        return delegateResponse.writeWith(Flux.just(buffer));
                    }

                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                    return super.writeWith(fluxBody.buffer()
                                                   .map(dataBuffers -> {
                                                       DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                                                       DataBuffer join = dataBufferFactory.join(dataBuffers);
                                                       byte[] content = new byte[join.readableByteCount()];
                                                       join.read(content);
                                                       DataBufferUtils.release(join);
                                                       Charset contentCharset = contentType.getCharset();
                                                       contentCharset = (null == contentCharset) ? StandardCharsets.UTF_8 : contentCharset;
                                                       if (contentType.equalsTypeAndSubtype(MediaType.TEXT_HTML)) {
                                                           String responseData = new String(content, contentCharset);
                                                           log.debug("响应内容:{}", responseData);
                                                           final String redirectCommand = "redirect:";
                                                           int redirectCommandIndex = responseData.indexOf(
                                                                   redirectCommand);
                                                           if (redirectCommandIndex >= 0) {
                                                               String redirectUri = responseData.substring(
                                                                               redirectCommandIndex + redirectCommand.length())
                                                                       .trim();
                                                               delegateResponse.setStatusCode(HttpStatus.SEE_OTHER);
                                                               delegateResponse.getHeaders()
                                                                       .set(HttpHeaders.LOCATION, redirectUri);
                                                           }
                                                       }
                                                       if (contentType.includes(MediaType.APPLICATION_JSON)) {

                                                           String responseData = new String(content, contentCharset);
                                                           log.debug("响应内容:{}", responseData);
                                                           //初始化一个基本对象
                                                           ObjectNode objectNode = buildResult(true, 200, "成功");
                                                           try {
                                                               JsonNode respDataNode = jsonMapper.readTree(
                                                                       responseData);
                                                               if (respDataNode.isObject()) {
                                                                   JsonNode statusNode = respDataNode.path("status");

                                                                   String messageNode = respDataNode.path("message")
                                                                           .asText();
                                                                   String errorNode = respDataNode.path("error")
                                                                           .asText();
                                                                   String codeNode = respDataNode.path("code")
                                                                           .asText();
                                                                   String dataNode = respDataNode.path("data")
                                                                           .asText();

                                                                   // 非result 报错返回值
                                                                   if (statusNode.isNumber() && !StringUtils.isAllBlank(
                                                                           errorNode, messageNode)) {
                                                                       HttpStatusCn httpStatusCn = EnumUtil.getEnumByCode(
                                                                               statusNode.asInt(), HttpStatusCn.class);
                                                                       objectNode.put("success", false);

                                                                       JsonNode errorCodeNode = respDataNode.get(
                                                                               "errorCode");

                                                                       if (null != errorCodeNode && !errorCodeNode.isNull()) {
                                                                           objectNode.put("code",
                                                                                          errorCodeNode.asText());
                                                                       } else {
                                                                           String status = statusNode.asText("500");
                                                                           objectNode.put("code", status);
                                                                       }

                                                                       if (null != httpStatusCn) {
                                                                           objectNode.put("message",
                                                                                          httpStatusCn.getMessage());
                                                                       } else {
                                                                           objectNode.put("message", ERROR_TEXT_CN);
                                                                       }

                                                                       objectNode.set("data",
                                                                                      respDataNode.path("data"));
                                                                       objectNode.set("path",
                                                                                      respDataNode.path("path"));

                                                                   } else {
                                                                       if (!StringUtils.equalsAny(codeNode, "0",
                                                                                                  "200")) {
                                                                           objectNode.put("success", false);
                                                                       }
                                                                       objectNode.put("message", messageNode);
                                                                       objectNode.put("code", codeNode);
                                                                       objectNode.put("data", dataNode);
                                                                   }

                                                               }

                                                               byte[] newRs = jsonMapper.writeValueAsBytes(objectNode);
                                                               delegateResponse.getHeaders()
                                                                       .setContentLength(newRs.length);
                                                               return delegateResponse.bufferFactory()
                                                                       .wrap(newRs);
                                                           } catch (IOException e) {
                                                               log.error("网络IO异常,{ }", e);
                                                               ObjectNode node = buildResult(false, 500,
                                                                                             SYSTEM_ERROR_CN);
                                                               String errResp = node.asText();
                                                               byte[] newRs = errResp.getBytes(contentCharset);
                                                               delegateResponse.getHeaders()
                                                                       .setContentLength(newRs.length);
                                                               return delegateResponse.bufferFactory()
                                                                       .wrap(newRs);
                                                           }
                                                       }
                                                       return delegateResponse.bufferFactory()
                                                               .wrap(content);
                                                   }));
                }
            };
            return chain.filter(exchange.mutate()
                                        .response(decoratedResponse)
                                        .build());
        }
        return chain.filter(exchange);
    }

    private ObjectNode buildResult(Boolean success, Integer code, String message) {
        ObjectNode resultJson = jsonMapper.createObjectNode();
        resultJson.put("success", success);
        resultJson.put("code", code);
        resultJson.put("message", message);
        return resultJson;
    }

    @Override
    public int getOrder() {
        return -1;
    }
}


