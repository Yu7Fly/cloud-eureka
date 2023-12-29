package com.yu7.order.consumer;

import com.yu7.order.config.ClearRibbonCacheBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
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

    @RabbitListener(queues = "SERVER_LIST")
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


