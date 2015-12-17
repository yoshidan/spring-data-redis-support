package org.springframework.data.redis.cache;

import org.springframework.cache.Cache;
import org.springframework.util.Assert;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by yoshidan on 2015/12/17.
 */
public class StatisticsAwareCacheDecorator implements Cache {

    private final Cache targetCache;

    private final AtomicLong hitCount;
    private final AtomicLong missCount;

    public long getHitCount() {
        return hitCount.get();
    }
    public long getMissCount() {
        return missCount.get();
    }

    /**
     * Create a new StatisticsAwareCacheDecorator for the given target Cache.
     * @param targetCache the target Cache to decorate
     */
    public StatisticsAwareCacheDecorator(Cache targetCache) {
        Assert.notNull(targetCache, "Target Cache must not be null");
        this.targetCache = targetCache;
        this.hitCount = new AtomicLong(0);
        this.missCount = new AtomicLong(0);
    }

    @Override
    public String getName() {
        return this.targetCache.getName();
    }

    @Override
    public Object getNativeCache() {
        return this.targetCache.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        return trace(this.targetCache.get(key));
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return trace(this.targetCache.get(key, type));
    }

    @Override
    public void put(final Object key, final Object value) {
        this.targetCache.put(key,value);
    }

    @Override
    public ValueWrapper putIfAbsent(final Object key, final Object value) {
        return this.targetCache.putIfAbsent(key, value);
    }

    @Override
    public void evict(final Object key) {
       this.targetCache.evict(key);
    }

    @Override
    public void clear() {
        this.targetCache.clear();
    }

    private <T> T trace(T result){
        if(result == null){
            missCount.incrementAndGet();
        }else {
            hitCount.incrementAndGet();
        }
        return result;
    }
}
