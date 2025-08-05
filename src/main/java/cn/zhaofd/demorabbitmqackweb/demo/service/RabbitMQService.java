package cn.zhaofd.demorabbitmqackweb.demo.service;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * RabbitMQ应用示例
 */
@Service
public class RabbitMQService {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQService.class);
    private final RabbitTemplate rabbitTemplate;
    private final Binding logBinding;

    @Autowired
    public RabbitMQService(RabbitTemplate rabbitTemplate, Binding logBinding) {
        this.rabbitTemplate = rabbitTemplate;
        this.logBinding = logBinding;
    }

    /**
     * 发送消息
     *
     * @param message 消息
     */
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(logBinding.getExchange(), logBinding.getRoutingKey(), message);
    }

    /**
     * 接收消息并ACK
     *
     * @param message 消息
     * @param channel 通道
     * @param tag     标记
     */
    @RabbitListener(queues = "log-queue")
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
