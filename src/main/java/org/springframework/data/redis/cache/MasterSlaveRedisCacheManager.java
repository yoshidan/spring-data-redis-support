/**
 * 
 */
package org.springframework.data.redis.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.springframework.data.redis.core.RedisOperations;

/**
 * RedisCacheManager for Master/Slave Replication
 *  
 * @author yoshidan
 */
@SuppressWarnings("rawtypes")
public class MasterSlaveRedisCacheManager extends RedisCacheManager{

    /** read only operations **/
    private final RedisOperations redisReadOperations;

    /** statistics enabmed **/
    private boolean statisticsEnabled = false;

    /**
     * @param statisticsEnabled to set
     */
    public void setStatisticsEnabled(boolean statisticsEnabled){
        this.statisticsEnabled = statisticsEnabled;
    }

    /**
     * Construct a {@link MasterSlaveRedisCacheManager}.
     * 
     * @param writer for write data
     * @param reader for read data
     */
    public MasterSlaveRedisCacheManager(RedisOperations writer , RedisOperations reader) {
		this(writer, reader,Collections.<String> emptyList());
    }
	
    /**
     * Construct a static {@link MasterSlaveRedisCacheManager}, managing caches for the specified cache names only.
     * 
     * @param writer for write data
     * @param reader for read data     
     * @param cacheNames
     */
	public MasterSlaveRedisCacheManager(RedisOperations writer, RedisOperations reader,Collection<String> cacheNames) {
		super(writer,cacheNames);
		this.redisReadOperations = reader;
	}
	
	/**
	 * @see RedisCacheManager#createCache(String)
	 */
	@Override
	@SuppressWarnings("unchecked")
    protected RedisCache createCache(String cacheName) {
        long expiration = computeExpiration(cacheName);
        MasterSlaveRedisCache cache = new MasterSlaveRedisCache(cacheName,
            (isUsePrefix() ? getCachePrefix().prefix(cacheName) : null),
            getRedisOperations(), expiration, redisReadOperations);
        cache.setStatisticsEnabled(this.statisticsEnabled);
        return cache;
    }

}
