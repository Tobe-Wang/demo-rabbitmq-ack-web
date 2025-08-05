package cn.zhaofd.demorabbitmqackweb.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
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
     * @param configurer 配置
     * @param connectionFactory 连接工厂
     * @return RabbitListenerContainerFactory
     */
    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }

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

    /**
     * 创建队列(用户)
     *
     * @return Queue
     */
    @Bean
    public Queue sysUserQueue() {
        return new Queue("sysUser-queue");
    }
}
