package com.yu7.order.configcenter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : CSDN_懒羊羊.java
 * @Time : 2024/1/9 19:37
 * @Summarize :
 */
@RestController
@RefreshScope
public class TryConfigController {
    @Value("${eureka.client.registry-fetch-interval-seconds}")
    private String seconds;

    @GetMapping("info")
    public String info(){
        return seconds;
    }
}
