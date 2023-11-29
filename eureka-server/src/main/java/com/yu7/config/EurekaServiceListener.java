package com.yu7.config;

import com.netflix.appinfo.InstanceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.context.event.EventListener;

/**
 * Eureka服务上下线监听
 */
@Slf4j
public class EurekaServiceListener {
    @EventListener
    public void listen(EurekaInstanceCanceledEvent event){
        log.debug(event.getServerId()+"\t"+event.getAppName()+"服务下线");
    }

    @EventListener
    public void listen(EurekaInstanceRegisteredEvent event){
        InstanceInfo instanceInfo = event.getInstanceInfo();
        log.debug(instanceInfo.getAppName()+"进行注册");
    }
}
