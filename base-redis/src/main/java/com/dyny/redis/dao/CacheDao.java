package com.dyny.redis.dao;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author:wanggl
 * @date:2018-10-18
 * @version:1.0.0
 */
public interface CacheDao {

    void set(String key, String value);

    void set(String key, String value, Class classNamePrefix);

    void set(String key, Object value);

    void set(String key, Object value, Class classNamePrefix);

    void set(String key, String value, long timeout, TimeUnit timeUnit);

    void set(String key, String value, Class classNamePrefix, long timeout, TimeUnit timeUnit);

    void set(String key, Object value, long timeout, TimeUnit timeUnit);

    void set(String key, Object value, Class classNamePrefix, long timeout, TimeUnit timeUnit);

    void update(String key, Object value, Class targetClass);

    void update(String key, Object value);

    void updateTimeout(String key, long timeout, TimeUnit timeUnit);

    String get(String key);

    <T> T get(String key, Class<T> targetClass);

    <T> T get(String key, Class<T> targetClass, boolean autoPrefix);

    <T> List<T> getList(String key, Class<T> targetClass);

    <T> List<T> getList(String key, Class<T> targetClass, boolean autoPrefix);

    Long delete(Set<String> keyList);

    Long deleteAll();

    Long deleteByPattern(String pattern);

    int delete(String key);

    int delete(String key, String className);

    boolean contains(String key);

    Set<String> getKeys(String pattern);


}
