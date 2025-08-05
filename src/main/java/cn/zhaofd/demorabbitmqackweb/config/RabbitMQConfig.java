package cn.zhaofd.demorabbitmqackweb.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置
 */
@Configuration
public class RabbitMQConfig {
    /**
     * 创建队列
     *
     * @return Queue
     */
    @Bean
    public Queue logQueue() {
        return new Queue("log-queue");
    }

    /**
     * 创建交换机
     *
     * @return DirectExchange
     */
    @Bean
    public DirectExchange logExchange() {
        return new DirectExchange("log-exchange");
    }

    /**
     * 绑定队列和交换机
     *
     * @param logQueue 队列
     * @param logExchange 交换机
     * @return Binding
     */
    @Bean
    public Binding logBinding(Queue logQueue, DirectExchange logExchange) {
        return BindingBuilder.bind(logQueue).to(logExchange).with("log.key");
    }
}
