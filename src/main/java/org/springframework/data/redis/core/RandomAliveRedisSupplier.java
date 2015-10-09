package org.springframework.data.redis.core;

import org.springframework.data.redis.RedisConnectionFailureException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yoshidan on 2015/09/28.
 */
public class RandomAliveRedisSupplier<K,V> implements RedisSupplier<K,V> {

    /** state of redis. */
    private final Collection<RedisState<K, V>> redisStates;

    /**
     * Constructor.
     *
     * @param redisStates the state of redis
     */
    public RandomAliveRedisSupplier(Collection<RedisState<K, V>> redisStates) {
       this.redisStates = redisStates;
    }

    /**
     * Get one of alive redis.
     *
     * @return alive redis
     */
    public RedisTemplate<K,V> get() {
        List<RedisState> aliveOnly = redisStates.stream().filter(e -> e.isAlive()).collect(Collectors.toList());
        if(aliveOnly.isEmpty()){
            throw new RedisConnectionFailureException("no redis is available");
        }
        return aliveOnly.get((int)(System.currentTimeMillis() % aliveOnly.size())).getRedisTemplate();
    }
}
