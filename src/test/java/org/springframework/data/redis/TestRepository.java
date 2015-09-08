/**
 * 
 */
package org.springframework.data.redis;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * @author yoshidan
 */
@Repository
public class TestRepository {

  @Cacheable(value="cache",key="'test/' + #key")
  public Object pingPong(String key) {
     return key;
  }
  
  @CacheEvict(value="cache",key="'test/' + #key")
  public void evict(String key) {    
    
  }
}
