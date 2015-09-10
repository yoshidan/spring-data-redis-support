/**
 * 
 */
package org.springframework.data.redis.cache;

import java.util.Collection;
import java.util.Collections;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

public class MasterSlaveRedisCacheManager extends RedisCacheManager{

	private RedisTemplate<?,?> readTemplate;
	
	public MasterSlaveRedisCacheManager(RedisTemplate<?,?> forRead , RedisTemplate<?,?> forWrite) {
		this(forRead, forWrite,Collections.<String> emptyList());
	}
	
	public MasterSlaveRedisCacheManager(RedisTemplate<?,?> forRead, RedisTemplate<?,?> forWrite,Collection<String> cacheNames) {
		super(forWrite,cacheNames);
		this.readTemplate = forRead;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected RedisCache createCache(String cacheName) {
		long expiration = computeExpiration(cacheName);
		return new MasterSlaveRedisCache(cacheName, 
				(isUsePrefix() ? getCachePrefix().prefix(cacheName) : null), 
				getTemplate(), readTemplate, expiration);
	}


}
