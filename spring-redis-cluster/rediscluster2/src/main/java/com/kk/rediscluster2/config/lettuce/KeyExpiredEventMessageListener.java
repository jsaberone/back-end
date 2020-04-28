package com.kk.rediscluster2.config.lettuce;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author zhouzijia
 * @version 1.0
 * @description
 * @date 2020/4/27 14:10
 */

@Slf4j
@Component
public class KeyExpiredEventMessageListener implements MessageListener {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = new String(message.getChannel());
        String action = new String(message.getBody());
        if(!StringUtils.isEmpty(key) && key.contains("expired") && action.startsWith("tracker") ) {
            dohandleKey2(key, action);
        }
    }

    /**
     * @description 数据处理，分布式集群式处理，防止多次处理
     * @param key, action
     */
    public void dohandleKey2(String key , String action){
        String lockValue = redisTemplate.opsForValue().getAndSet("ex:"+ action, "1");
        if("1".equals(lockValue)){
            return;
        }else{
            log.info("监听到的过期事件：{},redis key 是:{}", key, action);
        }
        //todo 处理自己的东西
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            redisTemplate.delete("ex:"+action);
        }
    }
}
