package com.dale.utils;


import androidx.annotation.NonNull;
import androidx.collection.LruCache;

import com.dale.constant.CacheConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存工具类
 */
public final class CacheMemoryUtils implements CacheConstants {

    private static final int DEFAULT_MAX_COUNT = 256;

    private static final Map<String, CacheMemoryUtils> CACHE_MAP = new HashMap<>();

    private final String mCacheKey;
    private final LruCache<String, CacheValue> mMemoryCache;


    public static CacheMemoryUtils getInstance() {
        return getInstance(DEFAULT_MAX_COUNT);
    }

    /**
     * @param maxCount 缓存的最大计数
     */
    public static CacheMemoryUtils getInstance(final int maxCount) {
        return getInstance(String.valueOf(maxCount), maxCount);
    }

    /**
     * @param cacheKey 缓存的键
     * @param maxCount 缓存的最大计数
     */
    public static CacheMemoryUtils getInstance(final String cacheKey, final int maxCount) {
        CacheMemoryUtils cache = CACHE_MAP.get(cacheKey);
        if (cache == null) {
            synchronized (CacheMemoryUtils.class) {
                cache = CACHE_MAP.get(cacheKey);
                if (cache == null) {
                    cache = new CacheMemoryUtils(cacheKey, new LruCache<String, CacheValue>(maxCount));
                    CACHE_MAP.put(cacheKey, cache);
                }
            }
        }
        return cache;
    }

    private CacheMemoryUtils(String cacheKey, LruCache<String, CacheValue> memoryCache) {
        mCacheKey = cacheKey;
        mMemoryCache = memoryCache;
    }

    @Override
    public String toString() {
        return mCacheKey + "@" + Integer.toHexString(hashCode());
    }

    /**
     * @param key   缓存的键
     * @param value 缓存的值
     */
    public void put(@NonNull final String key, final Object value) {
        put(key, value, -1);
    }

    /**
     * Put bytes in cache.
     *
     * @param key      缓存的键
     * @param value    缓存的值
     * @param saveTime 缓存的保存时间（秒）。
     */
    public void put(@NonNull final String key, final Object value, int saveTime) {
        if (value == null) return;
        long dueTime = saveTime < 0 ? -1 : System.currentTimeMillis() + saveTime * 1000;
        mMemoryCache.put(key, new CacheValue(dueTime, value));
    }

    /**
     * 返回缓存中的值
     *
     * @param key 缓存的键
     * @param <T> 值类型。
     * @return 缓存中的值
     */
    public <T> T get(@NonNull final String key) {
        return get(key, null);
    }

    /**
     * 返回缓存中的值
     *
     * @param key          缓存的键
     * @param defaultValue 缓存不存在时的默认值。
     * @param <T>          值类型。
     * @return 缓存中的值
     */
    public <T> T get(@NonNull final String key, final T defaultValue) {
        CacheValue val = mMemoryCache.get(key);
        if (val == null) return defaultValue;
        if (val.dueTime == -1 || val.dueTime >= System.currentTimeMillis()) {
            //noinspection unchecked
            return (T) val.value;
        }
        mMemoryCache.remove(key);
        return defaultValue;
    }

    /**
     * 返回缓存数量
     *
     * @return 缓存数量
     */
    public int getCacheCount() {
        return mMemoryCache.size();
    }

    /**
     * 删除指定key缓存
     *
     * @param key 缓存的键
     * @return {@code true}: success<br>{@code false}: fail
     */
    public Object remove(@NonNull final String key) {
        CacheValue remove = mMemoryCache.remove(key);
        if (remove == null) return null;
        return remove.value;
    }

    /**
     * 清理所有的缓存
     */
    public void clear() {
        mMemoryCache.evictAll();
    }

    private static final class CacheValue {
        long dueTime;
        Object value;

        CacheValue(long dueTime, Object value) {
            this.dueTime = dueTime;
            this.value = value;
        }
    }
}