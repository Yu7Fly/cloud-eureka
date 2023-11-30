//package com.yu7.user.config;
//
//import com.netflix.loadbalancer.BaseLoadBalancer;
//import com.netflix.loadbalancer.ILoadBalancer;
//import com.netflix.loadbalancer.Server;
//import com.netflix.loadbalancer.ServerList;
//import com.netflix.niws.client.http.RestClient;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.ObjectUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.Iterator;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * 手动清除Ribbon缓存
// */
//@Configuration
//@Slf4j
//public class ClearRibbonCache {
//
//
//    /**
//     * 削减
//     */
//    public static boolean cutDown(List<Integer> ports, Server index) {
//        return ports.contains(index.getPort());
//    }
//
//    public void clearRibbonCache(SpringClientFactory clientFactory, List<Integer> portParams) {
//        // 获取指定服务的负载均衡器
//        ILoadBalancer loadBalancer = clientFactory.getLoadBalancer("user-service");
//        //在主动拉取可用列表，而不是走拦截器被动的方式——这里
//        List<Server> reachableServers = loadBalancer.getReachableServers();//这里从客户端获取，会等待客户端同步三级缓存
//        //过滤掉已经下线的端口，符合条件端口的服务过滤出来
//        List<Server> ableServers = reachableServers.stream().filter(temp -> !cutDown(portParams, temp)).collect(Collectors.toList());
//        log.debug("可用服务列表：{}", ableServers);
//        // 在某个时机需要清除Ribbon缓存
//        ((BaseLoadBalancer) loadBalancer).setServersList(ableServers); // 清除Ribbon负载均衡器的缓存
//    }
//}
