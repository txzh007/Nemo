package link.tanxin.common.exception;

import link.tanxin.common.request.CodeEnum;
import lombok.Getter;
import org.slf4j.helpers.MessageFormatter;

/**
 * 服务异常类
 *
 * @author tan
 */
@Getter
public class BusinessException extends RuntimeException {
    private final Integer code;

    private final String message;

    private final String finalMsg;

    public BusinessException(Integer code, String message, Object... args) {
        super();
        this.code = code;
        this.message = message;
        this.finalMsg = getFinalMsg(args);
    }

    public BusinessException(CodeEnum codeEnum, Object... args) {

        this(codeEnum.getCode(), codeEnum.getMessage(), args);
    }

    public String getFinalMsg(Object... args) {
        return MessageFormatter.arrayFormat(message, args)
                .getMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return "BusinessException:[code= " + code + " ,message= " + finalMsg + " ]";
    }

    @Override
    public String getMessage() {
        return finalMsg;
    }


}
