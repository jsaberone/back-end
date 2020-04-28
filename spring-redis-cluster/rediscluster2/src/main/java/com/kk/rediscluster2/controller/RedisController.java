package com.kk.rediscluster2.controller;

import com.kk.rediscluster2.config.lettuce.RedisLock;
import com.kk.rediscluster2.model.CacheBody;
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

    @Autowired
    private RedisLock redisLock;

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

    @ApiOperation("分布式锁方式设置key")
    @PostMapping("lock/set")
    public String redisLock(@RequestBody CacheBody request){
        String requestId = String.valueOf(request.hashCode());
        boolean flag = true;
        while(flag) {
            if (redisLock.tryLock(requestId, requestId, 2, TimeUnit.SECONDS)) {
                redisTemplate.opsForValue().set(request.getKey(), request.getVlaue(), request.getExpire(), TimeUnit.SECONDS);
                System.out.println("请求为 == " + request.toString());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    redisLock.releaseLock(requestId, requestId);
                    flag = false;
                }
            }
        }
        return "success";
    }

}
