/**
 * 
 */
package org.springframework.data.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.MasterSlaveRedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author yoshidan
 */
@SpringBootApplication
@EnableCaching
public class ApplicationContext {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(ApplicationContext.class, args);
  }
  
  @Bean
  public RedisTemplate<Object,Object> reader() {
    RedisTemplate<Object,Object> reader = new RedisTemplate<Object,Object>();  
    reader.setConnectionFactory(createJedisConnectionFactory("localhost",6380));
    return reader;
  }
  
  @Bean
  public RedisTemplate<Object,Object> writer() {
    RedisTemplate<Object,Object> writer = new RedisTemplate<Object,Object>();  
    writer.setConnectionFactory(createJedisConnectionFactory("localhost",6379));
    return writer;
  }
  
  @Bean
  @Autowired
  public CacheManager cacheManager(@Qualifier("reader") RedisTemplate<Object,Object> reader ,
      @Qualifier("writer") RedisTemplate<Object,Object> writer) {   
    return new MasterSlaveRedisCacheManager(writer, reader);
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
