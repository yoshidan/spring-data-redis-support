# spring-data-redis-wrapper

 * 標準のRedisCacheManagerは一つのRedisTemplateしか扱えないため、複数のRedisTemplateを扱えるようにします。

## Use case

* RedisのMaster/Slave構成においてReadとWriteで接続先ノードを分けるRedis構成の場合に利用します。

 TODO 構成図
 
* Writerの方はVIPを利用せずにSentinelを利用しても問題ありません。

## Repository

http://nysd.github.io/archivar/

## Dependency 

### maven

```
<dependency>
	<groupId>spring.wrapper</groupId>
	<artifactId>spring-data-redis-wrapper</groupId>
	<version>1.4.3-SNAPSHOT</version>
</dependency>
```

### gradle

```
dependencies {
    compile 'spring.wrapper:spring-data-redis-wrapper:1.4.3'
}
```

## Configuration

```
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
    return new ReplicatedRedisCacheManager(reader, writer);
  }
  
```


