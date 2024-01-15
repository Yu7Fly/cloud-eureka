package com.yu7.order.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMqConfig {
    @Bean
    public TopicExchange theTopicExchange() {
        return new TopicExchange("USER-TOPIC");
    }
}
