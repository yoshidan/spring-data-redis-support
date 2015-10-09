package org.springframework.data.redis.core;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by yoshidan on 2015/09/28.
 */
public class MultiConnectionRedisTemplate<K,V> implements RedisOperations<K,V> {

    /** redis supplier. */
    private final RedisSupplier<K,V> supplier;

    /**
     * Constructor.
     *
     * @param supplier to set
     */
    public MultiConnectionRedisTemplate(RandomAliveRedisSupplier<K,V> supplier) {
        this.supplier = supplier;
    }

    @Override
    public <T> T execute(RedisCallback<T> action) {
        return supplier.get().execute(action);
    }

    @Override
    public <T> T execute(SessionCallback<T> session) {
        return supplier.get().execute(session);
    }

    @Override
    public List<Object> executePipelined(RedisCallback<?> action) {
        return supplier.get().executePipelined(action);
    }

    @Override
    public List<Object> executePipelined(RedisCallback<?> action, RedisSerializer<?> resultSerializer) {
        return supplier.get().executePipelined(action,resultSerializer);
    }

    @Override
    public List<Object> executePipelined(SessionCallback<?> session) {
        return supplier.get().executePipelined(session);
    }

    @Override
    public List<Object> executePipelined(SessionCallback<?> session, RedisSerializer<?> resultSerializer) {
        return supplier.get().executePipelined(session,resultSerializer);
    }

    @Override
    public <T> T execute(RedisScript<T> script, List<K> keys, Object... args) {
        return supplier.get().execute(script, keys, args);
    }

    @Override
    public <T> T execute(RedisScript<T> script, RedisSerializer<?> argsSerializer, RedisSerializer<T> resultSerializer, List<K> keys, Object... args) {
        return supplier.get().execute(script,argsSerializer,resultSerializer,keys,args);
    }

    @Override
    public Boolean hasKey(K key) {
        return supplier.get().hasKey(key);
    }

    @Override
    public void delete(K key) {
        supplier.get().delete(key);
    }

    @Override
    public void delete(Collection<K> key) {
        supplier.get().delete(key);
    }

    @Override
    public DataType type(K key) {
        return supplier.get().type(key);
    }

    @Override
    public Set<K> keys(K pattern) {
        return supplier.get().keys(pattern);
    }

    @Override
    public K randomKey() {
        return supplier.get().randomKey();
    }

    @Override
    public void rename(K oldKey, K newKey) {
        supplier.get().rename(oldKey, newKey);
    }

    @Override
    public Boolean renameIfAbsent(K oldKey, K newKey) {
        return supplier.get().renameIfAbsent(oldKey,newKey);
    }

    @Override
    public Boolean expire(K key, long timeout, TimeUnit unit) {
        return supplier.get().expire(key,timeout,unit);
    }

    @Override
    public Boolean expireAt(K key, Date date) {
        return supplier.get().expireAt(key,date);
    }

    @Override
    public Boolean persist(K key) {
        return supplier.get().persist(key);
    }

    @Override
    public Boolean move(K key, int dbIndex) {
        return supplier.get().move(key,dbIndex);
    }

    @Override
    public byte[] dump(K key) {
        return supplier.get().dump(key);
    }

    @Override
    public void restore(K key, byte[] value, long timeToLive, TimeUnit unit) {
        supplier.get().restore(key, value, timeToLive, unit);
    }

    @Override
    public Long getExpire(K key) {
        return supplier.get().getExpire(key);
    }

    @Override
    public Long getExpire(K key, TimeUnit timeUnit) {
        return supplier.get().getExpire(key, timeUnit);
    }

    @Override
    public void watch(K keys) {
        supplier.get().watch(keys);
    }

    @Override
    public void watch(Collection<K> keys) {
        supplier.get().watch(keys);
    }

    @Override
    public void unwatch() {
        supplier.get().unwatch();
    }

    @Override
    public void multi() {
        supplier.get().multi();
    }

    @Override
    public void discard() {
        supplier.get().discard();
    }

    @Override
    public List<Object> exec() {
        return supplier.get().exec();
    }

    @Override
    public List<RedisClientInfo> getClientList() {
        return supplier.get().getClientList();
    }

    @Override
    public List<Object> exec(RedisSerializer<?> valueSerializer) {
        return supplier.get().exec();
    }

    @Override
    public void convertAndSend(String destination, Object message) {
        supplier.get().convertAndSend(destination, message);
    }

    @Override
    public ValueOperations<K, V> opsForValue() {
        return supplier.get().opsForValue();
    }

    @Override
    public BoundValueOperations<K, V> boundValueOps(K key) {
        return supplier.get().boundValueOps(key);
    }

    @Override
    public ListOperations<K, V> opsForList() {
        return supplier.get().opsForList();
    }

    @Override
    public BoundListOperations<K, V> boundListOps(K key) {
        return supplier.get().boundListOps(key);
    }

    @Override
    public SetOperations<K, V> opsForSet() {
        return supplier.get().opsForSet();
    }

    @Override
    public BoundSetOperations<K, V> boundSetOps(K key) {
        return supplier.get().boundSetOps(key);
    }

    @Override
    public ZSetOperations<K, V> opsForZSet() {
        return supplier.get().opsForZSet();
    }

    @Override
    public HyperLogLogOperations<K, V> opsForHyperLogLog() {
        return supplier.get().opsForHyperLogLog();
    }

    @Override
    public BoundZSetOperations<K, V> boundZSetOps(K key) {
        return supplier.get().boundZSetOps(key);
    }

    @Override
    public <HK, HV> HashOperations<K, HK, HV> opsForHash() {
        return supplier.get().opsForHash();
    }

    @Override
    public <HK, HV> BoundHashOperations<K, HK, HV> boundHashOps(K key) {
        return supplier.get().boundHashOps(key);
    }

    @Override
    public List<V> sort(SortQuery<K> query) {
        return supplier.get().sort(query);
    }

    @Override
    public <T> List<T> sort(SortQuery<K> query, RedisSerializer<T> resultSerializer) {
        return supplier.get().sort(query, resultSerializer);
    }

    @Override
    public <T> List<T> sort(SortQuery<K> query, BulkMapper<T, V> bulkMapper) {
        return supplier.get().sort(query, bulkMapper);
    }

    @Override
    public <T, S> List<T> sort(SortQuery<K> query, BulkMapper<T, S> bulkMapper, RedisSerializer<S> resultSerializer) {
        return supplier.get().sort(query, bulkMapper, resultSerializer);
    }

    @Override
    public Long sort(SortQuery<K> query, K storeKey) {
        return supplier.get().sort(query,storeKey);
    }

    @Override
    public RedisSerializer<?> getValueSerializer() {
        return supplier.get().getValueSerializer();
    }

    @Override
    public RedisSerializer<?> getKeySerializer() {
        return supplier.get().getKeySerializer();
    }

    @Override
    public RedisSerializer<?> getHashKeySerializer() {
        return supplier.get().getHashKeySerializer();
    }

    @Override
    public RedisSerializer<?> getHashValueSerializer() {
        return supplier.get().getHashValueSerializer();
    }

    @Override
    public void killClient(String host, int port) {
        supplier.get().killClient(host,port);
    }

    @Override
    public void slaveOf(String host, int port) {
        supplier.get().slaveOf(host,port);
    }

    @Override
    public void slaveOfNoOne() {
        supplier.get().slaveOfNoOne();
    }

}
