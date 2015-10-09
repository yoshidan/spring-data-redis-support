## ユースケース

* RedisのMaster/Slave構成においてRead対象ノードが複数存在する場合に利用します。
* Writeの方はSentinelを利用してください。

 TODO 構成図

## 内部動作
* RedisState単位でスレッドを起動します。
* 5秒に一回RedisStateが保持するRedisTemplate(JedisConnectionFactory)に対してpingを発行します。
* ping結果NGの場合そのRedisStateは利用不能にマークされます。
* Redisに対する操作時には利用可能なRedisStateからランダムで一つ選択します。

## 設定

```
@Configuration
@EnableCaching
public class RedisConfiguraiont {

  @Bean
  public RedisTemplate<Object,Object> readerRedisTemplate1() {
    RedisTemplate<Object,Object> reader = new RedisTemplate<Object,Object>();  
    reader.setConnectionFactory(createJedisConnectionFactory("192.168.59.103",6379));
    return reader;
  }

  @Bean
  public RedisTemplate<Object,Object> readerRedisTemplate2() {
    RedisTemplate<Object,Object> reader = new RedisTemplate<Object,Object>();
    reader.setConnectionFactory(createJedisConnectionFactory("192.168.59.104",6379));
    return reader;
  }

  @Bean
  public RedisTemplate<Object,Object> readerRedisTemplate3() {
    RedisTemplate<Object,Object> writer = new RedisTemplate<Object,Object>();
    writer.setConnectionFactory(createJedisConnectionFactory("192.168.59.105",6379));
    return writer;
  }

  @Bean
  public RedisTemplate<Object,Object> writerRedisTemplate() {
    RedisTemplate<Object,Object> writer = new RedisTemplate<Object,Object>();
    RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration();
    sentinelConfiguration.master("cluster1");
    sentinelConfiguration.addSentinel(new RedisNode("192.168.59.103",26379));
    sentinelConfiguration.addSentinel(new RedisNode("192.168.59.104",26379));
    sentinelConfiguration.addSentinel(new RedisNode("192.168.59.105",26379));
    JedisConnectionFactory factory = new JedisConnectionFactory(sentinelConfiguration);
    factory.setUsePool(true);
    factory.afterPropertiesSet();
    writer.setConnectionFactory(factory);
    return writer;
  }


  @Bean
  @Autowired
  public RedisState<Object,Object> readerRedisState1(@Qualifier("readerRedisTemplate1") RedisTemplate reader) {
    return new RedisState<>(reader);
  }

  @Bean
  @Autowired
  public RedisState<Object,Object> readerRedisState2(@Qualifier("readerRedisTemplate2") RedisTemplate reader) {
    return new RedisState<>(reader);
  }

  @Bean
  @Autowired
  public RedisState<Object,Object> readerRedisState3(@Qualifier("readerRedisTemplate3") RedisTemplate reader) {
    return new RedisState<>(reader);
  }

  @Bean
  @Autowired
  public RedisOperationsProxy<Object,Object> redisOperationsProxy(Collection<RedisState<Object,Object>> obs) {
    return new MultiConnectionRedisTemplate<>(new RandomAliveRedisSupplier(obs));
  }

  @Bean
  @Autowired
  public CacheManager cacheManager(RedisOperationsProxy<Object,Object> reader ,
      @Qualifier("writerRedisTemplate") RedisTemplate<Object,Object> writer) {
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
```

## 利用方法

* RedisTemplateを利用するのと同じです。

```
@Autowired
MultiConnectionRedisTemplate<Object,Object> template;

public Object data(@PathVariable String key){
   return template.opsForZSet().reverseRange(key, 0, 10);
}

```
* Cacheの方はCacheManagerを利用するのでCacheableやCacheEvictがそのまま利用できます。
* getの時だけMultiConnectionRedisTemplateが利用されます。

```
@Repository
public class TestRepository {

  @Cacheable(value="cache",key="'test/' + #key")
  public Object ping(String key) {
     return getBySQL(key);
  }

  @CacheEvict(value="cache",key="'test/' + #key")
  public void evict(String key) {
      deleteBySql(key);
  }
}
```
