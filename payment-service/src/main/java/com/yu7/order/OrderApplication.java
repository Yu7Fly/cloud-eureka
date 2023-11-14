package com.yu7.order;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@MapperScan("com.yu7.order.mapper")
@SpringBootApplication
//当feignClient所在的包不在该项目启动类的扫描范围内，就可以通过指定@EnableFeignClients注解的clients属性来确定其想要的client
//@EnableFeignClients(clients = {UserClient.class},defaultConfiguration = DefaultFeignConfiguration.class) //开启feign的客户端
@EnableFeignClients
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    /**
     * 创建RestTemplate并注入Spring容器
     */
    @Bean
    @LoadBalanced //只是开启负载均衡的标志
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

//    /**
//     * 负载均衡
//     * @return
//     */
//    @Bean
//    public IRule randomRule() {
//        return new RandomRule();
//    }
}