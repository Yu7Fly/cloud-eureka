package com.yu7.user.Redis;

import org.jetbrains.annotations.TestOnly;
import org.junit.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


public class Demo {
    @Resource
    StringRedisTemplate stringRedisTemplate;



    public void testString(){
        stringRedisTemplate.opsForValue().set("Yu7","hello java",10, TimeUnit.SECONDS);
        System.out.println(stringRedisTemplate.opsForValue().get("Yu7"));
    }
}
