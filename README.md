# spring-data-redis-support

## Function
以下の機能を提供します。

* [RedisCacheManagerで複数のRedisTemplateを利用する](./docs/cache.md)
* [複数のRedisから読み取り負荷分散を行う](./docs/loadbalance.md)

## 使い方

```
<repositories>
    <repository>
        <id>nysd.maven</id>
        <name>nysd maven repository</name>
        <url>http://nysd.github.io/archivar/</url>
		</repository>
</repositories>

<deppendencies>
    <dependency>
        <groupId>spring.support</groupId>
        <artifactId>spring-data-redis-support</groupId>
        <version>1.6.1.1</version>
    </dependency>
</dependencies>
```

* spring-data-redis-supportのバージョンはspring-data-redisのリリースバージョンに対応しています。
* spring bootのRELEASEバージョンが依存するspring-data-redisのバージョンのみ対応しています。
