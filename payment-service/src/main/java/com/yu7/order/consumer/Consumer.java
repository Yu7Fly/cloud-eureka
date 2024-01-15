package com.yu7.order.consumer;

import com.yu7.order.config.ClearRibbonCacheBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

/**
 * 消费者
 */
@Slf4j
@Component
public class Consumer {

    @Resource
    SpringClientFactory springClientFactory;
    @Resource
    ClearRibbonCacheBean clearRibbonCacheBean;

    //@RabbitListener(queues = "USER-QUEUE")//当使用work_queue，队列绑定了两个消费者，每一个消息只能被消费一次。一个队列绑定多个消费者可以加快处理消息的速度，这也是处理MQ消息堆积的一种方式
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "USER-QUEUE"),
            exchange = @Exchange(name = "USER-TOPIC", type = ExchangeTypes.TOPIC),
            key = "USER.SERVICE-DOWN")
    )
    public void listenWorkQueue1(HashMap<String, List<Integer>> message) {
        log.debug("消费者1接收到消息——" + message + "时间为：" + LocalTime.now());
        for (String key : message.keySet()) {
            List<Integer> value = message.get(key);
            log.debug("Key: " + key);
            log.debug("Value: " + value);
            if (ObjectUtils.isNotEmpty(value)) {
                clearRibbonCacheBean.clearRibbonCache(springClientFactory, value.toString(), key);
            }
            log.debug("现在的所有服务列表：{}", springClientFactory.getLoadBalancer(key).getAllServers());
        }
    }
}


