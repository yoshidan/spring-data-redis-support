## ユースケース

* RedisのMaster/Slave構成においてReadとWriteで接続先ノードを分けるRedis構成の場合に利用します。

 TODO 構成図

* Writerの方はVIPを利用せずにSentinelを利用しても問題ありません。

## 設定

* 以下は例です。設定値の実際はapplication.ymlに記載してspring-boot-autoconfigureのRedisProperties経由で取得するのがよいです。
* spring-boot-autoconfigureで自動的に登録されるRedisTemplate/JedisConnectionFactoryは混乱の元なのでBean化しないことをおすすめします。

```
@Configuration
@EnableCaching
public class RedisConfiguraion {

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
    // sentinelが必要ならJedisSentinelConnectionを利用する。
    return writer;
  }

  @Bean
  @Autowired
  public CacheManager cacheManager(
  	  @Qualifier("reader") RedisTemplate<Object,Object> reader ,
      @Qualifier("writer") RedisTemplate<Object,Object> writer) {
    return new MasterSlaveRedisCacheManager(reader, writer);
  }
}
```

## 利用方法

* CacheManagerを利用するのでCacheableやCacheEvictがそのまま利用できます。
* getの時だけread用のRedisTemplateが利用されます。

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
