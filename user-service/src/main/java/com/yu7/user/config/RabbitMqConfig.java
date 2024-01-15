package com.yu7.user.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMqConfig {
    //    @Bean
//    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
//        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
//        // 服务启动时候开启自动启动
//        rabbitAdmin.setAutoStartup(true);
//        return rabbitAdmin;
//    }
    @Bean
    public Queue theQueue() {
        return new Queue("USER-QUEUE");
    }

//    @Bean
//    public DirectExchange theDirectExchange(){ return new DirectExchange("USER-DIRECT");}
//
//    @Bean
//    public TopicExchange theTopicExchange(){ return new TopicExchange("USER-TOPIC");}
//
//    /**
//     * 队列绑定交换机 可以用注解替代
//     */
//    @Bean
//    public Binding bindingUserQueue(Queue userQueue, TopicExchange topicExchange){
//        return BindingBuilder.bind(userQueue).to(topicExchange).with("USER");
//    }
}
