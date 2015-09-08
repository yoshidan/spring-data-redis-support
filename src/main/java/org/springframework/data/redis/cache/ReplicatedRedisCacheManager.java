/**
 * 
 */
package org.springframework.data.redis.cache;

import java.util.Collection;
import java.util.Collections;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

public class ReplicatedRedisCacheManager extends RedisCacheManager{

	private RedisTemplate<?,?> readTemplate;
	
	public ReplicatedRedisCacheManager(RedisTemplate<?,?> forRead , RedisTemplate<?,?> forWrite) {
		this(forRead, forWrite,Collections.<String> emptyList());
	}
	
	public ReplicatedRedisCacheManager(RedisTemplate<?,?> forRead, RedisTemplate<?,?> forWrite,Collection<String> cacheNames) {
		super(forWrite,cacheNames);
		this.readTemplate = forRead;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected RedisCache createCache(String cacheName) {
		long expiration = computeExpiration(cacheName);
		return new ReplicatedRedisCache(cacheName, 
				(isUsePrefix() ? getCachePrefix().prefix(cacheName) : null), 
				getTemplate(), readTemplate, expiration);
	}


}
