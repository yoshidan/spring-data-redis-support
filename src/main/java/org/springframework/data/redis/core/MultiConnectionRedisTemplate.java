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

    /** observe redis state. */
    private final RedisObserver<K,V> observer;

    /**
     * Constructor.
     *
     * @param redisObservables to set
     */
    public MultiConnectionRedisTemplate(Collection<RedisObservable<K,V>> redisObservables){
        observer = new RedisObserver<>(redisObservables);
    }

    @Override
    public <T> T execute(RedisCallback<T> action) {
        return observer.select().execute(action);
    }

    @Override
    public <T> T execute(SessionCallback<T> session) {
        return observer.select().execute(session);
    }

    @Override
    public List<Object> executePipelined(RedisCallback<?> action) {
        return observer.select().executePipelined(action);
    }

    @Override
    public List<Object> executePipelined(RedisCallback<?> action, RedisSerializer<?> resultSerializer) {
        return observer.select().executePipelined(action,resultSerializer);
    }

    @Override
    public List<Object> executePipelined(SessionCallback<?> session) {
        return observer.select().executePipelined(session);
    }

    @Override
    public List<Object> executePipelined(SessionCallback<?> session, RedisSerializer<?> resultSerializer) {
        return observer.select().executePipelined(session,resultSerializer);
    }

    @Override
    public <T> T execute(RedisScript<T> script, List<K> keys, Object... args) {
        return observer.select().execute(script, keys, args);
    }

    @Override
    public <T> T execute(RedisScript<T> script, RedisSerializer<?> argsSerializer, RedisSerializer<T> resultSerializer, List<K> keys, Object... args) {
        return observer.select().execute(script,argsSerializer,resultSerializer,keys,args);
    }

    @Override
    public Boolean hasKey(K key) {
        return observer.select().hasKey(key);
    }

    @Override
    public void delete(K key) {
        observer.select().delete(key);
    }

    @Override
    public void delete(Collection<K> key) {
        observer.select().delete(key);
    }

    @Override
    public DataType type(K key) {
        return observer.select().type(key);
    }

    @Override
    public Set<K> keys(K pattern) {
        return observer.select().keys(pattern);
    }

    @Override
    public K randomKey() {
        return observer.select().randomKey();
    }

    @Override
    public void rename(K oldKey, K newKey) {
        observer.select().rename(oldKey, newKey);
    }

    @Override
    public Boolean renameIfAbsent(K oldKey, K newKey) {
        return observer.select().renameIfAbsent(oldKey,newKey);
    }

    @Override
    public Boolean expire(K key, long timeout, TimeUnit unit) {
        return observer.select().expire(key,timeout,unit);
    }

    @Override
    public Boolean expireAt(K key, Date date) {
        return observer.select().expireAt(key,date);
    }

    @Override
    public Boolean persist(K key) {
        return observer.select().persist(key);
    }

    @Override
    public Boolean move(K key, int dbIndex) {
        return observer.select().move(key,dbIndex);
    }

    @Override
    public byte[] dump(K key) {
        return observer.select().dump(key);
    }

    @Override
    public void restore(K key, byte[] value, long timeToLive, TimeUnit unit) {
        observer.select().restore(key, value, timeToLive, unit);
    }

    @Override
    public Long getExpire(K key) {
        return observer.select().getExpire(key);
    }

    @Override
    public Long getExpire(K key, TimeUnit timeUnit) {
        return observer.select().getExpire(key, timeUnit);
    }

    @Override
    public void watch(K keys) {
        observer.select().watch(keys);
    }

    @Override
    public void watch(Collection<K> keys) {
        observer.select().watch(keys);
    }

    @Override
    public void unwatch() {
        observer.select().unwatch();
    }

    @Override
    public void multi() {
        observer.select().multi();
    }

    @Override
    public void discard() {
        observer.select().discard();
    }

    @Override
    public List<Object> exec() {
        return observer.select().exec();
    }

    @Override
    public List<RedisClientInfo> getClientList() {
        return observer.select().getClientList();
    }

    @Override
    public List<Object> exec(RedisSerializer<?> valueSerializer) {
        return observer.select().exec();
    }

    @Override
    public void convertAndSend(String destination, Object message) {
        observer.select().convertAndSend(destination, message);
    }

    @Override
    public ValueOperations<K, V> opsForValue() {
        return observer.select().opsForValue();
    }

    @Override
    public BoundValueOperations<K, V> boundValueOps(K key) {
        return observer.select().boundValueOps(key);
    }

    @Override
    public ListOperations<K, V> opsForList() {
        return observer.select().opsForList();
    }

    @Override
    public BoundListOperations<K, V> boundListOps(K key) {
        return observer.select().boundListOps(key);
    }

    @Override
    public SetOperations<K, V> opsForSet() {
        return observer.select().opsForSet();
    }

    @Override
    public BoundSetOperations<K, V> boundSetOps(K key) {
        return observer.select().boundSetOps(key);
    }

    @Override
    public ZSetOperations<K, V> opsForZSet() {
        return observer.select().opsForZSet();
    }

    @Override
    public HyperLogLogOperations<K, V> opsForHyperLogLog() {
        return observer.select().opsForHyperLogLog();
    }

    @Override
    public BoundZSetOperations<K, V> boundZSetOps(K key) {
        return observer.select().boundZSetOps(key);
    }

    @Override
    public <HK, HV> HashOperations<K, HK, HV> opsForHash() {
        return observer.select().opsForHash();
    }

    @Override
    public <HK, HV> BoundHashOperations<K, HK, HV> boundHashOps(K key) {
        return observer.select().boundHashOps(key);
    }

    @Override
    public List<V> sort(SortQuery<K> query) {
        return observer.select().sort(query);
    }

    @Override
    public <T> List<T> sort(SortQuery<K> query, RedisSerializer<T> resultSerializer) {
        return observer.select().sort(query, resultSerializer);
    }

    @Override
    public <T> List<T> sort(SortQuery<K> query, BulkMapper<T, V> bulkMapper) {
        return observer.select().sort(query, bulkMapper);
    }

    @Override
    public <T, S> List<T> sort(SortQuery<K> query, BulkMapper<T, S> bulkMapper, RedisSerializer<S> resultSerializer) {
        return observer.select().sort(query, bulkMapper, resultSerializer);
    }

    @Override
    public Long sort(SortQuery<K> query, K storeKey) {
        return observer.select().sort(query,storeKey);
    }

    @Override
    public RedisSerializer<?> getValueSerializer() {
        return observer.select().getValueSerializer();
    }

    @Override
    public RedisSerializer<?> getKeySerializer() {
        return observer.select().getKeySerializer();
    }

    @Override
    public RedisSerializer<?> getHashKeySerializer() {
        return observer.select().getHashKeySerializer();
    }

    @Override
    public RedisSerializer<?> getHashValueSerializer() {
        return observer.select().getHashValueSerializer();
    }

    @Override
    public void killClient(String host, int port) {
        observer.select().killClient(host,port);
    }

    @Override
    public void slaveOf(String host, int port) {
        observer.select().slaveOf(host,port);
    }

    @Override
    public void slaveOfNoOne() {
        observer.select().slaveOfNoOne();
    }

}
