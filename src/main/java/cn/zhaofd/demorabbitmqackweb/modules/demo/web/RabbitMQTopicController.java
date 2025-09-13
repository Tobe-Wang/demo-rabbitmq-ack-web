package cn.zhaofd.demorabbitmqackweb.modules.demo.web;

import cn.zhaofd.demorabbitmqackweb.modules.demo.service.RabbitMqTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息退回机制、消息发送确认机制RabbitMQ应用示例
 */
@RestController
@RequestMapping("/demo/rabbitmq/topic")
public class RabbitMQTopicController {
    private final RabbitMqTopicService rabbitMqTopicService;

    public RabbitMQTopicController(@Autowired RabbitMqTopicService rabbitMqTopicService) {
        this.rabbitMqTopicService = rabbitMqTopicService;
    }

    /**
     * 发送消息
     *
     * @param message 消息
     */
    @GetMapping("/sendMessage")
    public void sendMessage(String message) {
        rabbitMqTopicService.sendMessage(message);
    }
}
