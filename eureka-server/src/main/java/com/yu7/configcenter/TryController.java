package com.yu7.configcenter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : CSDN_懒羊羊.java
 * @Time : 2024/1/10 15:41
 * @Summarize :
 */
@RestController
@RefreshScope
public class TryController {
    @Value("${eureka.server.useReadOnlyResponseCache}")
    private String flag;

    @GetMapping("info")
    public String info(){
        return flag;
    }
}
