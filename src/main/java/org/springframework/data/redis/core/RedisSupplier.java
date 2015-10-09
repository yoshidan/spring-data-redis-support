package org.springframework.data.redis.core;

import java.util.function.Supplier;

/**
 * Created by 01005901 on 2015/10/09.
 */
public interface RedisSupplier<K,V> extends Supplier<RedisOperations<K,V>> {
}
