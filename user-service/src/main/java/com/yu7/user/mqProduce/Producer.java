package com.yu7.user.mqProduce;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
//        String queueName = "work.queue";
//        String message = "hello, MQ,i am yu7";
////        for (int i = 1; i <= 50; i++) {
////            rabbitTemplate.convertAndSend(queueName, message + i);
////            Thread.sleep(20);
////        }
//        rabbitTemplate.convertAndSend(queueName, message);
        HashMap<String, List<Integer>> portInfo = new HashMap<>();
        portInfo.put(appName, Arrays.asList(8083,8084));
        rabbitTemplate.convertAndSend(queueName,portInfo);
    }
}
