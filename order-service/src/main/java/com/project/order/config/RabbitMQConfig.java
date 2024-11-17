package com.project.order.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue articleLikeQueue() {
        return new Queue("concertOrderQueue", true); // true 表示隊列是持久的
    }

    /**
     * 監聽器無限重試消息處理，因為消息在處理失敗後會被重新放回隊列，然後又會重新被消費
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMaxConcurrentConsumers(1);
        factory.setDefaultRequeueRejected(false); // 當消息處理失敗時不重新放回隊列
        return factory;
    }
}