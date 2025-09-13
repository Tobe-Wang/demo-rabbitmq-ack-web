package cn.zhaofd.demorabbitmqackweb.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置
 */
@Configuration
public class RabbitMQConfig {
    /**
     * 创建消息转换器
     *
     * @return MessageConverter
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter(); // JSON序列化替代Java原生序列化
    }

    /**
     * 创建RabbitListenerContainerFactory
     *
     * @param configurer        配置
     * @param connectionFactory 连接工厂
     * @return RabbitListenerContainerFactory
     */
    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }

    /**
     * 创建队列(日志)
     *
     * @return Queue
     */
    @Bean
    public Queue logQueue() {
        return new Queue("log-queue"); // 默认持久化
    }

    /**
     * 创建交换机
     *
     * @return DirectExchange
     */
    @Bean
    public DirectExchange logExchange() {
        return new DirectExchange("log-exchange"); // 默认持久化
    }

    /**
     * 绑定队列和交换机
     *
     * @param logQueue    队列
     * @param logExchange 交换机
     * @return Binding
     */
    @Bean
    public Binding logBinding(Queue logQueue, DirectExchange logExchange) {
        return BindingBuilder.bind(logQueue).to(logExchange).with("log.key");
    }

    /**
     * 创建队列(用户)
     *
     * @return Queue
     */
    @Bean
    public Queue sysUserQueue() {
        return new Queue("sysUser-queue");
    }

    /**
     * 创建队列(主题)
     *
     * @return Queue
     */
    @Bean
    public Queue topicQueue() {
//        return new Queue("topic-queue");

        return QueueBuilder.durable("topic-queue") // 持久化
                .withArgument("x-max-length", 5) // 队列最多容纳消息数量
//                .withArgument("x-max-length-bytes", 102400) // 队列最大总容量100KB
//                .withArgument("x-overflow", "drop-head") // 丢弃最早的消息(x-overflow不配置时的默认值)
                .withArgument("x-overflow", "reject-publish") // 队列满时拒绝新消息，生产者会收到basic.return或basic.nack响应(取决于发布确认设置)；不配此参数则丢弃最早的消息
                .build();
    }
}
