package com.jaagro.tms.biz.utils;

import com.jaagro.tms.biz.common.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;


/**
 * @author @Gao.
 * redis分布式锁
 */
@Slf4j
@Component
public class RedisLock {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedisOperator redisOperator;

    /**
     * 加锁
     *
     * @param key
     * @param value 当前时间 + 超时时间
     * @return
     */
    public boolean lock(String key, String value) {
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            redisTemplate.expire(key, 10, TimeUnit.MINUTES);
            return true;
        }
        String currentValue = redisTemplate.opsForValue().get(key);
        if (!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            //获取上一个锁的时间
            String oldValue = redisTemplate.opsForValue().getAndSet(key, value);
            if (!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 解锁
     *
     * @param key
     * @return
     */
    public boolean unLock(String key) {
        try {
                redisOperator.del(key);
        } catch (Exception e) {
            log.error("O unLock error:{}", e);
        }
        return true;
    }
}
