package com.yu7.order.config;

import AllUtil.StringUtils.StringChange;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 手动清除Ribbon缓存
 */
@Configuration
@Slf4j
public class ClearRibbonCacheBean {

    /**
     * 削减
     */
    public static boolean cutDown(List<Integer> ports, Server index) {
        return ports.contains(index.getPort());
    }

    public void clearRibbonCache(SpringClientFactory clientFactory, String portParams,String appName) {
        // 获取指定服务的负载均衡器
        ILoadBalancer loadBalancer = clientFactory.getLoadBalancer(appName);
        //在主动拉取可用列表，而不是走拦截器被动的方式——这里为什么获取可用的之后还要过滤，就是因为所谓的可用不是实时的可用而是缓存中的可用
        List<Server> reachableServers = loadBalancer.getReachableServers();//这里从客户端获取，会等待客户端同步三级缓存
        //过滤掉已经下线的端口，符合条件端口的服务过滤出来
        List<Integer> portList = StringChange.stringToList(portParams);
        List<Server> ableServers = reachableServers.stream().filter(temp -> !cutDown(portList, temp)).collect(Collectors.toList());
        log.debug("可用服务列表：{}", ableServers);
        // 在某个时机需要清除Ribbon缓存
        ((BaseLoadBalancer) loadBalancer).setServersList(ableServers); // 清除Ribbon负载均衡器的缓存
    }

    public void clearRibbonCacheByPort(SpringClientFactory clientFactory, Integer portParam,String appName) {
        // 获取指定服务的负载均衡器
        ILoadBalancer loadBalancer = clientFactory.getLoadBalancer(appName);
        //在主动拉取可用列表，而不是走拦截器被动的方式——这里
        List<Server> reachableServers = loadBalancer.getReachableServers();//这里从客户端获取，会等待客户端同步三级缓存
        //过滤掉已经下线的端口，符合条件端口的服务过滤出来
        List<Server> ableServers = reachableServers.stream().filter(temp -> temp.getPort()!=portParam).collect(Collectors.toList());
        log.debug("可用服务列表：{}", ableServers);
        // 在某个时机需要清除Ribbon缓存
        ((BaseLoadBalancer) loadBalancer).setServersList(ableServers); // 清除Ribbon负载均衡器的缓存
    }
}
