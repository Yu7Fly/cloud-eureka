package com.yu7.user.mqProduce;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 生产者，生产消息到交换机，交换机路由到指定队列，消费者监听队列消费消息
 */
@RestController
@RequestMapping("mq")
public class Producer {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Value("${eureka-server.appName}")
    private String appName;

    @Value("${DIY_QUEUE.VALUE}")
    private String queueName;
    @GetMapping("/sendMsg")
    public void customInfo() {
        HashMap<String, List<Integer>> portInfo = new HashMap<>();
        portInfo.put(appName, Arrays.asList(8083,8084));
        rabbitTemplate.convertAndSend(queueName,portInfo);
    }
}
