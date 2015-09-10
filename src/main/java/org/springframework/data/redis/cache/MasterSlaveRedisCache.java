/**
 * 
 */
package org.springframework.data.redis.cache;

import java.util.Arrays;

import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@SuppressWarnings(value={"rawtypes","unchecked"}) 
public class MasterSlaveRedisCache extends RedisCache{
	
	private final RedisTemplate readTemplate;
	private final byte[] prefix;
	private final byte[] cacheLockName;
	private long WAIT_FOR_LOCK = 300;
	
	@Override
	public ValueWrapper get(final Object key) {
      return (ValueWrapper) readTemplate.execute(new RedisCallback<ValueWrapper>() {

          public ValueWrapper doInRedis(RedisConnection connection) throws DataAccessException {
              waitForLock(connection);
              byte[] bs = connection.get(computeKey(key));
              Object value = readTemplate.getValueSerializer() != null ? readTemplate.getValueSerializer().deserialize(bs) : bs;
              return (bs == null ? null : new SimpleValueWrapper(value));
          }
      }, true);
  }

	/**
	 * @see RedisCache#RedisCache(String, byte[], RedisTemplate, long)
	 */
	public MasterSlaveRedisCache(String name, byte[] prefix, 
			RedisTemplate<? extends Object, ? extends Object> writeTemplate,
			RedisTemplate<? extends Object, ? extends Object> readTemplate,
			long expiration) {

		super(name,prefix,writeTemplate,expiration);
		this.readTemplate = readTemplate;
		this.prefix = prefix;
		StringRedisSerializer stringSerializer = new StringRedisSerializer();		
		this.cacheLockName = stringSerializer.serialize(name + "~lock");
	}

	/**
	 * @see RedisCache#computeKey
	 */
	private byte[] computeKey(Object key) {

		byte[] keyBytes = convertToBytesIfNecessary(readTemplate.getKeySerializer(), key);

		if (prefix == null || prefix.length == 0) {
			return keyBytes;
		}

		byte[] result = Arrays.copyOf(prefix, prefix.length + keyBytes.length);
		System.arraycopy(keyBytes, 0, result, prefix.length, keyBytes.length);

		return result;
	}

	/**
	 * @see RedisCache#waitForLock
	 */
	private boolean waitForLock(RedisConnection connection) {

		boolean retry;
		boolean foundLock = false;
		do {
			retry = false;
			if (connection.exists(cacheLockName)) {
				foundLock = true;
				try {
					Thread.sleep(WAIT_FOR_LOCK);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				retry = true;
			}
		} while (retry);

		return foundLock;
	}

	/**
	 * @see RedisCache#convertToBytesIfNecessary
	 */
	private byte[] convertToBytesIfNecessary(RedisSerializer<Object> serializer, Object value) {

		if (serializer == null && value instanceof byte[]) {
			return (byte[]) value;
		}

		return serializer.serialize(value);
	}

}
