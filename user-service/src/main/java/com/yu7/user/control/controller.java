package com.yu7.user.control;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.DiscoveryManager;
import com.netflix.discovery.EurekaClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/control")
public class controller {


    @Value("${eureka-server.ipAddress}")
    private String ipAddress;

    @Value("${eureka-server.appName}")
    private String appName;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

//    @Resource
//    ClearRibbonCache clearRibbonCache;

    @Resource
    EurekaClient eurekaClient;
    @Resource
    private SpringClientFactory clientFactory;

//    /**
//     * DiscoveryManager下线当前服务
//     *
//     * @param portParams 端口列表
//     */
//    @GetMapping(value = "/service-down")
//    public String offLine(@RequestParam List<Integer> portParams) {
//        List<Integer> successList = new ArrayList<>();
//        //得到服务信息
//        List<InstanceInfo> instances = eurekaClient.getInstancesByVipAddress(appName, false);
//        //去服务列表里挨个下线
//        instances.forEach(temp -> {
//            //只要实例的端口在传递的端口范围内就关闭
//            if (portParams.contains(temp.getPort())) {
//                eurekaClient.evict(appName, temp.getInstanceId());
//                log.debug(temp.getPort() + "服务下线成功");
//                successList.add(temp.getPort());
//            }
//        });
//        //循环执行完去清除Ribbon缓存
//        log.debug("开始清除Ribbon缓存");
//        clearRibbonCache.clearRibbonCache(clientFactory);
//        return successList + "优雅下线成功";
//    }


    @GetMapping(value = "/service-down")
    public void offLine() {
        DiscoveryClient client = DiscoveryManager.getInstance().getDiscoveryClient();
        client.shutdown();
//        log.debug("可用服务列表：" + clientFactory.getLoadBalancer("user-service").getReachableServers().toString());
    }

    @GetMapping(value = "/service-down-list")
    public String offLine(@RequestParam List<Integer> portParams) {
        List<Integer> successList = new ArrayList<>();
        //得到服务信息
        List<InstanceInfo> instances = eurekaClient.getInstancesByVipAddress(appName, false);
        List<Integer> servicePorts = instances.stream().map(InstanceInfo::getPort).collect(Collectors.toList());

        //去服务列表里挨个下线
        OkHttpClient client = new OkHttpClient();
        log.error("开始时间：{}", System.currentTimeMillis());
        portParams.parallelStream().forEach(temp -> {
            if (servicePorts.contains(temp)) {
                String url = "http://" + ipAddress + ":" + temp + "/control/service-down";
                try {
                    Response response = client.newCall(new Request.Builder().url(url).build()).execute();
                    if (response.code() == 200) {
                        log.debug(temp + "服务下线成功");
                        successList.add(temp);
                    } else {
                        log.debug(temp + "服务下线失败");
                    }
                } catch (IOException e) {
                    log.error(e.toString());
                }
            }
        });
        //循环执行完去清除Ribbon缓存
//        log.debug("可用服务列表：" + clientFactory.getLoadBalancer("user-service").getReachableServers().toString());
//        log.debug("可用服务列表：" + clientFactory.getLoadBalancer("user-service").getServerList(true).toString());
//        log.debug("开始清除Ribbon缓存");
//        clearRibbonCache.clearRibbonCache(clientFactory,portParams);
//        log.debug("可用服务列表：{}",clientFactory.getLoadBalancer("user-service").getAllServers());
//        log.debug("可用服务列表：{}",clientFactory.getLoadBalancer("user-service").getReachableServers());
        // todo MQ通知
        stringRedisTemplate.opsForHash().putIfAbsent("port-map",appName,portParams.toString());
        stringRedisTemplate.expire("port-map", 30, TimeUnit.SECONDS);
        return successList + "优雅下线成功";
    }


    /**
     * actuator下线服务列表
     *
     * @param portParams 端口集合
     * @return 优雅
     */
    @GetMapping(value = "/service-down-ports")
    public String downServiceByPorts(@RequestParam List<Integer> portParams) {
        if (ObjectUtils.isEmpty(portParams)) {
            return "端口为空";
        }
        //成功下线列表
        List<Integer> successList = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        portParams.forEach(temp -> {
            Request request = new Request.Builder()
                    .url("http://" + ipAddress + ":" + temp + "/actuator/shutdown")
                    .post(RequestBody.create(null, new byte[0]))
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response.code() == 200) {
                    log.debug(temp + "服务下线成功");
                    successList.add(temp);
                } else {
                    log.debug(temp + "服务下线失败");
                }
            } catch (IOException e) {
                log.error(e.toString());
            }
        });
//        log.debug("可用服务列表：" + clientFactory.getLoadBalancer("user-service").getReachableServers().toString());
//        log.debug("可用服务列表：" + clientFactory.getLoadBalancer("user-service").getServerList(true).toString());
//        log.debug("开始清除Ribbon缓存");
//        clearRibbonCache.clearRibbonCache(clientFactory,portParams);
        return successList + "优雅下线成功";
    }


}
