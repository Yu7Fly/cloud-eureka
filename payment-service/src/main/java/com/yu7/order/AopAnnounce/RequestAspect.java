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
@Aspect
@Component
@Slf4j
public class RequestAspect {

    @Resource
    SpringClientFactory springClientFactory;
    @Resource
    ClearRibbonCacheBean clearRibbonCacheBean;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Before(value = "execution(* com.yu7.order.web.*.*(..))")
    public void refreshBefore(JoinPoint joinPoint) {
//        String ports = (String) stringRedisTemplate.opsForHash().get("port-map", "down-ports");
        Map<Object, Object> objectMap = stringRedisTemplate.opsForHash().entries("port-map");
        Map<String, String> portMap = MapConvert.convertMap(objectMap);
        Set<String> serviceNameSet = portMap.keySet();
        String firstServiceName = serviceNameSet.stream().findFirst().orElse(null);
        String ports = (String) stringRedisTemplate.opsForHash().get("port-map", firstServiceName);
        log.debug("从Redis获取的端口为：{}", ports);
        //下线了才会有值，没有值说明没下线不用更新
        if (ObjectUtils.isNotEmpty(ports)) {
            clearRibbonCacheBean.clearRibbonCache(springClientFactory, ports);
        }
    }
}
