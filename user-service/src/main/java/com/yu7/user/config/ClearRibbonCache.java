package com.yu7.user.config;

import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 手动清除Ribbon缓存
 */
@Configuration
public class ClearRibbonCache {


    public void clearRibbonCache(SpringClientFactory clientFactory) {
        // 获取指定服务的负载均衡器
        ILoadBalancer loadBalancer = clientFactory.getLoadBalancer("user-service");
        //在主动拉取可用列表，而不是走拦截器被动的方式——这里
        List<Server> reachableServers = loadBalancer.getReachableServers();//这里从客户端获取，会等待客户端同步三级缓存

        // 在某个时机需要清除Ribbon缓存
        ((BaseLoadBalancer) loadBalancer).setServersList(reachableServers); // 清除Ribbon负载均衡器的缓存
    }
}
