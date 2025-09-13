package cn.zhaofd.demorabbitmqackweb.modules.demo.service;

import com.rabbitmq.client.Channel;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 消息退回机制、消息发送确认机制RabbitMQ应用示例
 */
@Service
public class RabbitMqTopicService implements RabbitTemplate.ConfirmCallback {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqTopicService.class);
    private final RabbitTemplate rabbitTemplate;
    private final Queue topicQueue;

    public RabbitMqTopicService(@Autowired RabbitTemplate rabbitTemplate, @Autowired Queue topicQueue) {
        this.rabbitTemplate = rabbitTemplate;
        this.topicQueue = topicQueue;

        /* 当在构造函数中设置回调时，Service无需实现对应接口；也不需要设置回调为当前对象rabbitTemplate.setConfirmCallback(this);
        // 设置确认回调(确保消息到达RabbitMQ)
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                logger.error("消息发送失败: {}", cause);
            }
        });

        // 设置退回回调(确保消息被正确路由到队列)
        rabbitTemplate.setReturnsCallback(returned -> {
            logger.error("消息路由失败: 路由键={}, 退回原因={}", returned.getRoutingKey(), returned.getReplyText());
        });
        */
    }

    /**
     * 发送消息
     *
     * @param message 消息
     */
    public void sendMessage(String message) {
        // 设置回调为当前对象
        rabbitTemplate.setConfirmCallback(this);
        // 发送消息
        rabbitTemplate.convertAndSend(topicQueue.getName(), message);
    }

    /**
     * 发送消息回调确认
     *
     * @param correlationData 关联数据
     * @param ack             是否消息接收确认成功
     * @param cause           错误信息
     */
    @Override
    public void confirm(@Nullable CorrelationData correlationData, boolean ack, @Nullable String cause) {
        if (ack) {
            logger.info("消息发送成功");
        } else {
            logger.error("消息发送失败:{}", cause);
        }
    }

    /**
     * 接收消息并ACK
     *
     * @param message 消息
     * @param channel 通道
     * @param tag     标记
     */
    @RabbitListener(queues = "topic-queue")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            logger.info("Received <{}>", message);
            // 确认消息
            // 第2个参数：表示是否批量确认消息，true确认所有比deliveryTag更小或相等的未确认消息；false仅确认当前指定的deliveryTag消息
            channel.basicAck(tag, false);
        } catch (IOException e) {
            // 拒绝并重入队列
            // 第2个参数：表示是否批量拒绝消息，true拒绝所有比deliveryTag更小或相等的未确认消息；false仅拒绝当前指定的deliveryTag消息
            // 第3个参数：表示是否重新入队列
            channel.basicNack(tag, false, true);
            throw e;
        }
    }
}
