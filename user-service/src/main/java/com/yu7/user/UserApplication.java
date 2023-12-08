package com.yu7.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@MapperScan("com.yu7.user.mapper")
@SpringBootApplication
public class UserApplication {

    @Resource
    StringRedisTemplate stringRedisTemplate;


    public void testString(){
        stringRedisTemplate.opsForValue().set("Yu7","hello java",10, TimeUnit.SECONDS);
        System.out.println(stringRedisTemplate.opsForValue().get("Yu7"));
    }
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
