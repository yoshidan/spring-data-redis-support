package org.springframework.data.redis.core;

import org.springframework.data.redis.RedisConnectionFailureException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by yoshidan on 2015/09/28.
 */
public class RedisObserver<K,V> implements Observer {

    /** state of redis. */
    private final Map<RedisObservable<K,V>, Boolean> redisState;

    /**
     * Constructor.
     *
     * @param redisObservables the state of redis
     */
    public RedisObserver(Collection<RedisObservable<K,V>> redisObservables) {
        this.redisState = redisObservables.stream().
                collect(Collectors.toMap(e -> e, e -> true, (f, s) -> f && s, ConcurrentHashMap::new));
        this.redisState.keySet().forEach(r->r.addObserver(this));
    }

    /**
     * Change state.
     *
     * @param o observer
     * @param alive (true -> alive / false -> dead)
     */
    @Override
    public void update(Observable o, Object alive) {
        redisState.put((RedisObservable<K,V>)o, (Boolean) alive);
    }

    /**
     * Get one of alive redis.
     *
     * @return alive redis
     */
    public RedisTemplate<K,V> select() {
        List<RedisObservable> alives = redisState.entrySet().stream().filter(e->e.getValue()).map(e->e.getKey()).collect(Collectors.toList());
        if(alives.isEmpty()){
            throw new RedisConnectionFailureException("no redis is available");
        }
        return alives.get((int)(System.currentTimeMillis() % alives.size())).getRedisTemplate();
    }
}
