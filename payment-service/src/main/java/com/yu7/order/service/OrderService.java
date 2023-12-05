package com.yu7.order.service;


import com.yu7.feign.clients.UserClient;
import com.yu7.feign.pojo.User;
import com.yu7.order.config.ClearRibbonCacheBean;
import com.yu7.order.mapper.OrderMapper;
import com.yu7.order.pojo.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserClient userClient;

    @Autowired
    private RestTemplate restTemplate;

    @Resource
    ClearRibbonCacheBean clearRibbonCache;

    public Order queryOrderById(Long orderId) {
        // 1.查询订单
        Order order = orderMapper.findById(orderId);
         // todo 接受MQ发来的端口信息
//        String ports = (String) stringRedisTemplate.opsForHash().get("port-map","down-ports");
//        log.debug("从Redis获取的端口为：{}",ports);
//        //下线了才会有值，没有值说明没下线不用更新
//        if (ObjectUtils.isNotEmpty(ports)){
//            clearRibbonCache.clearRibbonCache(springClientFactory,ports);
//        }
        // 2.用Feign远程调用
        User user = userClient.findById(order.getUserId());
        // 3.封装user到Order
        order.setUser(user);
        // 4.返回
        return order;
    }


    @Resource
    SpringClientFactory springClientFactory;


//    public Order queryOrderById(Long orderId) {
//        // 1.查询订单
//        Order order = orderMapper.findById(orderId);
//
//        // 2.利用RestTemplate发起http请求，调用user-service服务里的接口
//        // 2.1.请求路径
//        String url = "http://user-service/user/" + order.getUserId();
//        // todo 接受MQ发来的端口信息
////        String ports = (String) stringRedisTemplate.opsForHash().get("port-map","ports");
////        log.debug("从Redis获取的端口为：{}",ports);
////        clearRibbonCache.clearRibbonCache(springClientFactory,ports);
//        // 2.2.发送http请求 远程调用
//        User user = restTemplate.getForObject(url, User.class);
//        // 3.封装user到Order
//        order.setUser(user);
//        // 4.返回
//        return order;
//    }
}
