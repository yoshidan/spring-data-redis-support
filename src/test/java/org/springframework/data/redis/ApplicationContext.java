/**
 * 
 */
package org.springframework.data.redis;

import com.sun.org.apache.xpath.internal.operations.Mult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.MasterSlaveRedisCacheManager;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisSentinelConnection;
import org.springframework.data.redis.core.MultiConnectionRedisTemplate;
import org.springframework.data.redis.core.RedisObservable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.Cache;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author yoshidan
 */
@SpringBootApplication
@EnableCaching
@RestController
@RequestMapping("/api/v1/")
public class ApplicationContext {

  @Autowired
  TestRepository testRepository;

  @RequestMapping("/{key}")
  @ResponseBody
  public Object cache(@PathVariable String key){
    return testRepository.pingPong(key);
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(ApplicationContext.class, args);
  }

  @Bean
  @Autowired
  public RedisObservable<Object,Object> reader2observable(@Qualifier("reader2") RedisTemplate reader) {
    return new RedisObservable<>(reader);
  }

  @Bean
  @Autowired
  public RedisObservable<Object,Object> readerObservable(@Qualifier("reader") RedisTemplate reader) {
    return new RedisObservable<>(reader);
  }

  @Bean
  @Autowired
  public RedisObservable<Object,Object> writerObservable(@Qualifier("readerMaster") RedisTemplate reader) {
    return new RedisObservable<>(reader);
  }

  @Bean
  @Autowired
  public MultiConnectionRedisTemplate<Object,Object> multiReader(Collection<RedisObservable<Object,Object>> obs) {
    return new MultiConnectionRedisTemplate<>(obs);
  }

  @Bean
  public RedisTemplate<Object,Object> reader2() {
    RedisTemplate<Object,Object> reader = new RedisTemplate<Object,Object>();
    reader.setConnectionFactory(createJedisConnectionFactory("localhost",6381));
    return reader;
  }
  
  @Bean
  public RedisTemplate<Object,Object> reader() {
    RedisTemplate<Object,Object> reader = new RedisTemplate<Object,Object>();  
    reader.setConnectionFactory(createJedisConnectionFactory("localhost",6380));
    return reader;
  }

  @Bean
  public RedisTemplate<Object,Object> readerMaster() {
    RedisTemplate<Object,Object> writer = new RedisTemplate<Object,Object>();
    writer.setConnectionFactory(createJedisConnectionFactory("localhost",6379));
    return writer;
  }
  
  @Bean
  public RedisTemplate<Object,Object> writer() {
    RedisTemplate<Object,Object> writer = new RedisTemplate<Object,Object>();
    RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration();
    sentinelConfiguration.master("cluster1");
    sentinelConfiguration.addSentinel(new RedisNode("192.168.59.103",26379));
    sentinelConfiguration.addSentinel(new RedisNode("192.168.59.103",26380));
    sentinelConfiguration.addSentinel(new RedisNode("192.168.59.103",26381));
    JedisConnectionFactory factory = new JedisConnectionFactory(sentinelConfiguration);
    factory.setUsePool(true);
    factory.afterPropertiesSet();
    writer.setConnectionFactory(factory);
    return writer;
  }
  
  @Bean
  @Autowired
  public CacheManager cacheManager(MultiConnectionRedisTemplate<Object,Object> reader ,
      @Qualifier("writer") RedisTemplate<Object,Object> writer) {
    MasterSlaveRedisCacheManager m = new MasterSlaveRedisCacheManager(writer, reader);
    m.setDefaultExpiration(1);
    return m;
  }
  
  private JedisConnectionFactory createJedisConnectionFactory(String host ,int port) {

    JedisConnectionFactory factory = new JedisConnectionFactory();
    factory.setUsePool(true);
    factory.setHostName(host);
    factory.setPort(port);   
    factory.afterPropertiesSet();
    return factory;
  }

}
