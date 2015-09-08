/**
 * 
 */
package org.springframework.data.redis.cache;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.redis.ApplicationContext;
import org.springframework.data.redis.TestRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ApplicationContext.class})
public class ReplicatedRedisCacheTest extends Assert{
  
  @Autowired
  private TestRepository testRepository;
  
  @Autowired
  @Qualifier("reader")
  private RedisTemplate<Object,Object> reader;
  
  @Test
  public void get(){
    String key = "key";
    assertEquals(key,testRepository.pingPong(key));
    assertEquals(key,reader.opsForValue().get("test/"+key));    
  }
  
  @Test
  public void evict(){
    String key = "key";
    testRepository.evict(key);
    assertNull(reader.opsForValue().get("test/"+key));    
  }

}
