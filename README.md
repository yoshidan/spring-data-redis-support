# spring-data-redis-support

## Function
以下の機能を提供します。

* [RedisCacheManagerで複数のRedisTemplateを利用する](./docs/cache.md)
* [複数のRedisから読み取り負荷分散を行う](./docs/loadbalance.md)

## How to use

### Repository

http://nysd.github.io/archivar/

### Dependency

* spring-data-redis-supportのバージョンはspring-data-redisのリリースバージョンに対応しています。
* spring bootのRELEASEバージョンが依存するspring-data-redisのバージョンのみ対応しています。

#### maven

```
<dependency>
	<groupId>spring.support</groupId>
	<artifactId>spring-data-redis-support</groupId>
	<version>1.6.0</version>
</dependency>
```

#### gradle

```
dependencies {
    compile 'spring.support:spring-data-redis-support:1.6.0'
}
```
