package com.kk.rediscluster.controller;

import com.kk.rediscluster.model.CacheBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @author zhouzijia
 * @version 1.0
 * @description rdis集群演示接口
 * @date 2020/4/27 10:55
 */

@RestController
@RequestMapping("/redis")
@Api("redis 操作接口")
@Slf4j
public class RedisController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @ApiOperation("根据key获取缓存")
    @GetMapping("get/{key}")
    public String getRedisKey(@PathVariable("key") String key){

        String result = redisTemplate.opsForValue().get(key);

        log.info("key == {}, value == {}", key, result);

        return result;

    }

    @ApiOperation("设置key")
    @PostMapping("set")
    public String setRedisKey(@RequestBody CacheBody request){

        redisTemplate.opsForValue().set(request.getKey(), request.getVlaue(), request.getExpire(), TimeUnit.SECONDS);

        return "success";
    }


}
