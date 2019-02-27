package com.dyny.redis.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.dyny.redis.dao.CacheDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author:wanggl
 * @date:2018-10-18
 * @version:1.0.0
 */
@Repository
public class RedisDaoImpl implements CacheDao {
    RedisTemplate redisTemplate;


    /**
     * 更改默认的序列化方法
     * 避免前缀出现类似\xac\xed\x00\x05t\x00\x19的字符串
     *
     * @param redisTemplate
     */
    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void set(String key, String value) {
        ValueOperations<String, String> operations = this.redisTemplate.opsForValue();
        operations.set(key, value);
    }


    @Override
    public void set(String key, String value, long timeout, TimeUnit timeUnit) {
        ValueOperations<String, String> operations = this.redisTemplate.opsForValue();
        operations.set(key, value, timeout, timeUnit);

    }

    @Override
    public void update(String key, Object value, Class targetClass) {
        if (redisTemplate.hasKey(key)) {
            this.set(key, value, targetClass);
        }
    }


    @Override
    public void update(String key, Object value) {
        if (redisTemplate.hasKey(key)) {
            this.set(key, value);
        }
    }

    @Override
    public void updateTimeout(String key, long timeout, TimeUnit timeUnit) {
        if (this.contains(key)) {
            redisTemplate.expire(key, timeout, timeUnit);
        }
    }

    @Override
    public void set(String key, String value, Class targetClass) {
        this.set(targetClass.getSimpleName() + key, value);
    }

    @Override
    public void set(String key, String value, Class classNamePrefix, long timeout, TimeUnit timeUnit) {
        this.set(classNamePrefix.getSimpleName() + key, value, timeout, timeUnit);

    }

    @Override
    public void set(String key, Object value) {
        if (value != null) {
            this.set(key, JSONObject.toJSONString(value));
        }
    }

    @Override
    public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        if (value != null) {
            this.set(key, JSONObject.toJSONString(value), timeout, timeUnit);
        }
    }

    /**
     * @param key
     * @param value
     * @param targetClass 注意只是类名没有包名
     */
    @Override
    public void set(String key, Object value, Class targetClass) {
        if (value != null) {
            this.set(targetClass.getSimpleName() + key, JSONObject.toJSONString(value));
        }
    }


    @Override
    public void set(String key, Object value, Class classNamePrefix, long timeout, TimeUnit timeUnit) {
        if (value != null) {
            this.set(classNamePrefix.getSimpleName() + key, JSONObject.toJSONString(value), timeout, timeUnit);
        }
    }


    @Override
    public String get(String key) {
        if (this.contains(key)) {
            ValueOperations<String, String> operations = this.redisTemplate.opsForValue();
            return operations.get(key);
        } else {
            return null;
        }
    }

    @Override
    public <T> T get(String key, Class<T> targetClass) {
        String strValue = this.get(key);
        if (!StringUtils.isEmpty(strValue)) {
            return JSONObject.parseObject(strValue, targetClass);
        } else {
            return null;
        }
    }


    @Override
    public <T> List<T> getList(String key, Class<T> targetClass) {
        String strValue = this.get(key);
        if (!StringUtils.isEmpty(strValue)) {
            return JSONObject.parseArray(strValue, targetClass);
        } else {
            return null;
        }
    }

    @Override
    public <T> List<T> getList(String key, Class<T> targetClass, boolean autoPrefix) {
        if (autoPrefix) {
            return this.getList(targetClass.getSimpleName() + key, targetClass);
        } else {
            return this.getList(key, targetClass);
        }
    }

    @Override
    public <T> T get(String key, Class<T> targetClass, boolean autoPrefix) {
        if (autoPrefix) {
            return this.get(targetClass.getSimpleName() + key, targetClass);
        } else {
            return this.get(key, targetClass);
        }
    }

    @Override
    public Long delete(Set<String> keyList) {
        return this.redisTemplate.delete(keyList);
    }

    @Override
    public int delete(String key) {
        return this.redisTemplate.delete(key) ? 1 : 0;
    }

    @Override
    public int delete(String key, String className) {
        return this.delete(className + key);
    }

    @Override
    public Long deleteAll() {
        return this.deleteByPattern("*");
    }

    @Override
    public Long deleteByPattern(String pattern) {
        Set<String> keys = this.getKeys(pattern);
        return this.delete(keys);
    }

    @Override
    public boolean contains(String key) {
        return this.redisTemplate.hasKey(key);
    }

    @Override
    public Set<String> getKeys(String pattern) {
        return this.redisTemplate.keys(pattern);
    }

}
