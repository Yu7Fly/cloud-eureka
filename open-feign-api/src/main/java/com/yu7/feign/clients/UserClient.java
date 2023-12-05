package com.yu7.feign.clients;


import com.yu7.feign.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * feign是一个声明式的http客户端
 */
@FeignClient(value = "user-service")
public interface UserClient {

    /**
     * 有接口无实现，这就是在做声明
     */
    @GetMapping("/user/{id}")
    User findById(@PathVariable("id") Long id);

}
