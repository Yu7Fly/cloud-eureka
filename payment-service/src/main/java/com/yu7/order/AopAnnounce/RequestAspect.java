package com.yu7.order.AopAnnounce;

import AllUtil.MapUtils.MapConvert;
import com.yu7.order.config.ClearRibbonCacheBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

/**
 * aop前置,跨服务调用前更新Ribbon缓存
 */
@Aspect  //切面，定义了通知和切点的关系
@Component
@Slf4j
public class RequestAspect {

    @Resource
    SpringClientFactory springClientFactory;
    @Resource
    ClearRibbonCacheBean clearRibbonCacheBean;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
//
//    @Before(value = "execution(* com.yu7.order.web.*.*(..))")
//    public void refreshBefore(JoinPoint joinPoint) {
//        Map<Object, Object> objectMap = stringRedisTemplate.opsForHash().entries("port-map");
//        Map<String, String> portMap = MapConvert.convertMap(objectMap);
//        Set<String> serviceNameSet = portMap.keySet();
//        String firstServiceName = serviceNameSet.stream().findFirst().orElse(null);
//        String ports = (String) stringRedisTemplate.opsForHash().get("port-map", firstServiceName);
//        log.debug("从Redis获取的端口为：{}", ports);
//        //下线了才会有值，没有值说明没下线不用更新
//        if (ObjectUtils.isNotEmpty(ports)) {
//            clearRibbonCacheBean.clearRibbonCache(springClientFactory, ports,firstServiceName);
//            log.debug("Redis更新Ribbon缓存完成");
//        }
//        log.debug("在切面中：现在的所有服务列表：{}",springClientFactory.getLoadBalancer("user-service").getAllServers());
//        log.debug("在切面中：现在的可用服务列表：{}",springClientFactory.getLoadBalancer("user-service").getReachableServers());
//    }
}
