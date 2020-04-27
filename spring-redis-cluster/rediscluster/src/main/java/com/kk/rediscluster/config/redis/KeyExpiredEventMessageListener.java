package com.kk.rediscluster.config.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
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

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = new String(message.getChannel());
        String action = new String(message.getBody());
        if(!StringUtils.isEmpty(key) && key.contains("expired")) {
            log.info("监听到的过期事件：{},redis key 是:{}", key, action);
        }
    }
}
