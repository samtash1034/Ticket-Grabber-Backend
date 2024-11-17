package com.project.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 保存數據到 Redis
     *
     * @param key   鍵
     * @param value 值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }


    /**
     * 從 Redis 中獲取數據
     *
     * @param key 鍵
     * @return 值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }


}

