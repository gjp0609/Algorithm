package com.onysakura.algorithm.spring.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class RedisUtils<T> {

    public RedisUtils(RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private final RedisTemplate<String, T> redisTemplate;

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public String getString(String key) {
        String str = null;
        Object obj = redisTemplate.opsForValue().get(key);
        if (ObjectUtils.isNotEmpty(obj)) {
            str = obj.toString();
        }
        return str;
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, T value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于1如果time小于等于0将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, T value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
