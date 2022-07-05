package com.wuxianggujun.robotbase.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对象缓存 将反射Bot对象的实例缓存拿出来，相当于中间件交互作用
 *
 * @author 无相孤君
 * @date 2022/07/04
 */
public class ObjectCache {

    /**
     * 实例
     */
    private static volatile ObjectCache instance = null;
    /**
     * 缓存
     */
    private final Map<String, Object> cache;

    private ObjectCache() {
        cache = new ConcurrentHashMap<>();
    }

    public static ObjectCache getInstance() {
        if (instance == null) {
            synchronized (ObjectCache.class) {
                if (instance == null) {
                    instance = new ObjectCache();
                }
            }
        }
        return instance;
    }

    public Map<String, Object> getCache() {
        return cache;
    }

    public Object getCache(Class<?> clazz) {
        String key = clazz.getName();
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        return null;
    }

    public void putCache(Class<?> clazz, Object value) {
        System.out.println("Key:" + clazz + " | " + "value:" + value);
        cache.put(clazz.getName(), value);
    }


}
