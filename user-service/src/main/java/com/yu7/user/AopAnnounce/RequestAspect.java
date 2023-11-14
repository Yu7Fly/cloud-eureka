package com.yu7.user.AopAnnounce;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * aop前置，打印被调用方负载均衡到的实例信息
 */
@Aspect
@Component
@Slf4j
public class RequestAspect {
    @Resource
    EurekaClient eurekaClient;

    @Before(value = "execution(* com.yu7.user.web.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        log.debug("调用方法:{} ", className + "." + methodName);
        InstanceInfo instanceInfo = eurekaClient.getApplicationInfoManager().getInfo();
        log.debug("负载均衡到的实例信息为：{}", instanceInfo.getInstanceId());
    }
}
