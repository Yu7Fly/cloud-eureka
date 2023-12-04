package com.yu7.order.service;


import com.yu7.model.User;
import com.yu7.order.config.ClearRibbonCacheBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.yu7.order.mapper.OrderMapper;
import com.yu7.order.pojo.Order;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

//    @Autowired
//    private UserClient userClient;

//    public Order queryOrderById(Long orderId) {
//        // 1.查询订单
//        Order order = orderMapper.findById(orderId);
//        // 2.用Feign远程调用
//        User user = userClient.findById(order.getUserId());
//        // 3.封装user到Order
//        order.setUser(user);
//        // 4.返回
//        return order;
//    }

    @Autowired
    private RestTemplate restTemplate;

    @Resource
    ClearRibbonCacheBean clearRibbonCache;

    @Resource
    SpringClientFactory springClientFactory;

    public Order queryOrderById(Long orderId) {
        // 1.查询订单
        Order order = orderMapper.findById(orderId);

        // 2.利用RestTemplate发起http请求，调用user-service服务里的接口
        // 2.1.请求路径
        String url = "http://user-service/user/" + order.getUserId();
        // todo 接受MQ发来的端口信息
        String ports = (String) stringRedisTemplate.opsForHash().get("port-map","ports");
        log.debug("从Redis获取的端口为：{}",ports);
        clearRibbonCache.clearRibbonCache(springClientFactory,ports);
        // 2.2.发送http请求 远程调用
        User user = restTemplate.getForObject(url, User.class);
        // 3.封装user到Order
        order.setUser(user);
        // 4.返回
        return order;
    }
}
