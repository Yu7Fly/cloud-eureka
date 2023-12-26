package com.yu7.order.web;


import com.yu7.order.config.ClearRibbonCacheBean;
import com.yu7.order.pojo.Order;
import com.yu7.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Resource
    SpringClientFactory springClientFactory;
    @Resource
    ClearRibbonCacheBean clearRibbonCacheBean;
    @GetMapping("{orderId}")
    public Order queryOrderByUserId(@PathVariable("orderId") Long orderId) {
        // 根据id查询订单并返回
        return orderService.queryOrderById(orderId);
    }

    @GetMapping("/before")
    public String beforeClean(@RequestParam("info") String info) {
        clearRibbonCacheBean.clearRibbonCache(springClientFactory,info,"user-service");
        return "hello";
    }
}
