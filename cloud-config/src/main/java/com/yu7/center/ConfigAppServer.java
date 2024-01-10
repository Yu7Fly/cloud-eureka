package com.yu7.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @Author : CSDN_懒羊羊.java
 * @Time : 2024/1/9 15:00
 * @Summarize : 配置中心启动类
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigAppServer {
    public static void main(String[] args) {
        SpringApplication.run(ConfigAppServer.class);
    }
}
