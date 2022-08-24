package link.tanxin.common.utils;

import link.tanxin.common.request.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * kafka 调用工具类
 *
 * @author tan
 * @date 2022年08月11日
 */
@Component
@Slf4j
public class KafkaUtil<K, V> {

    private final KafkaTemplate<K, V> kafkaTemplate;

    @Value("${spring.kafka.bootstrap-servers}")
    String[] servers;

    KafkaUtil(KafkaTemplate<K, V> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 获取连接
     */
    private Admin getAdmin() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", servers);
        // 正式环境需要添加账号密码
        return Admin.create(properties);
    }

    /**
     * 增加topic
     *
     * @param name      主题名字
     * @param partition 分区数量
     * @param replica   副本数量
     * @date 2022-06-23 chens
     */
    public Result<String> addTopic(String name, Integer partition, Integer replica) {

        try (Admin admin = getAdmin()) {
            if (replica > servers.length) {
                return Result.failed("副本数量不允许超过Broker数量");
            }
            NewTopic topic = new NewTopic(name, partition, Short.parseShort(replica.toString()));
            admin.createTopics(Collections.singleton(topic));
        }
        return Result.succeeded();
    }

    /**
     * 删除主题
     *
     * @param names 主题名字集合
     * @date 2022-06-23 chens
     */
    public void deleteTopic(List<String> names) {
        try (Admin admin = getAdmin()) {
            admin.deleteTopics(names);
        }
    }

    /**
     * 查询所有主题
     *
     * @date 2022-06-24 chens
     */
    public Set<String> queryTopic() {
        try (Admin admin = getAdmin()) {
            ListTopicsResult topics = admin.listTopics();
            return topics.names()
                    .get();
        } catch (Exception e) {
            log.error("查询主题错误！");
        }
        return null;
    }

    /**
     * 向所有分区发送消息
     *
     * @param topic 主题
     * @param data  数据
     */
    public void send(String topic, @Nullable V data) {
        kafkaTemplate.send(topic, data)
                .addCallback(new KafkaFutureCallback());
    }

    /**
     * 指定key发送消息，相同key保证消息在同一个分区
     *
     * @param topic 主题
     * @param key   key
     * @param data  数据
     */
    public void send(String topic, K key, @Nullable V data) {
        kafkaTemplate.send(topic, key, data)
                .addCallback(new KafkaFutureCallback());
    }

    /**
     * 指定分区和key发送
     *
     * @param topic     主题
     * @param partition 分区
     * @param key       key
     * @param data      数据
     */
    public void send(String topic, Integer partition, K key, @Nullable V data) {
        kafkaTemplate.send(topic, partition, key, data)
                .addCallback(new KafkaFutureCallback());
    }
}

@Slf4j
class KafkaFutureCallback implements ListenableFutureCallback<SendResult> {

    @Override
    public void onFailure(Throwable ex) {
        //todo 目前仅打印日志，后期可使用im推送报警
        log.error("kafka 发送失败:", ex);
    }
//    成功应该不需要回调吧
    @Override
    public void onSuccess(SendResult result) {
//        assert result != null;
//        log.info("send kafka success: {}", result.getProducerRecord());
    }
}

