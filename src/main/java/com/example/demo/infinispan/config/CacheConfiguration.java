package com.example.demo.infinispan.config;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.Index;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.eviction.EvictionType;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfiguration {

    @Primary
    @Bean("cacheManager")
    public EmbeddedCacheManager cacheManager() {
        GlobalConfiguration globalConfig = new GlobalConfigurationBuilder()
                .cacheContainer().statistics(true) // Enables Cache Manager statistics.
                .jmx().enable() // Exports statistics via JMX MBeans.
                .build();

        //.transport().defaultTransport()
        // 클러스터의 이름을 설정
        //.clusterName("some-cache-cluster") // 클러스터링 모드를 안쓸거면 필요없잖아?.
        // 기본 캐시의 이름을 설정
        //.defaultCacheName("default-cache");

        // 기본 캐시 설정 . 이것도 필요 없잖아?
        ConfigurationBuilder config = new ConfigurationBuilder();
        config.memory().evictionType(EvictionType.COUNT).size(1024L);
        config.simpleCache(true);

        EmbeddedCacheManager cacheManager = new DefaultCacheManager(globalConfig, config.build());

        return cacheManager;
    }

    @Bean("simpleCache")
    public Cache<String, String> simpleCache(@Qualifier("cacheManager") EmbeddedCacheManager cacheManager) {

        ConfigurationBuilder config = new ConfigurationBuilder();
        config.expiration().lifespan(2, TimeUnit.HOURS);
        config.memory().evictionType(EvictionType.COUNT).size(1024L);
        config.simpleCache(true);

        cacheManager.defineConfiguration("simple-cache", config.build());
        Cache<String, String> simpleCache = cacheManager.getCache("simple-cache");

        return simpleCache;
    }

    // Local 캐시 빈 생성
    @Bean("localCache")
    public Cache<String, String> localCache(@Qualifier("cacheManager") EmbeddedCacheManager cacheManager) {

        ConfigurationBuilder config = new ConfigurationBuilder();
        config.expiration().lifespan(2, TimeUnit.HOURS); // 캐시 만료 시간
        config.memory().evictionType(EvictionType.COUNT).size(1024L);
        config.clustering().cacheMode(CacheMode.LOCAL);

        cacheManager.defineConfiguration("local-cache", config.build());
        Cache<String, String> localCache = cacheManager.getCache("local-cache");

        return localCache;
    }

    // Invalidation 캐시 빈 생성
    @Bean("invalidationCache")
    public Cache<String, String> invalidationCache(@Qualifier("cacheManager") EmbeddedCacheManager cacheManager) {
        cacheManager.defineConfiguration("invalidation-cache", new ConfigurationBuilder()
                .expiration().lifespan(2, TimeUnit.HOURS)
                .memory().evictionType(EvictionType.COUNT).size(1024L)
                .clustering().cacheMode(CacheMode.INVALIDATION_SYNC)
                .build());

        Cache<String, String> invalidationCache = cacheManager.getCache("invalidation-cache");

        return invalidationCache;
    }

//    @Bean("indexedCache")
//    public <K, V> Cache<K, V> indexedCache(@Qualifier("cacheManager") EmbeddedCacheManager cacheManager) {
//
//        ConfigurationBuilder config = new ConfigurationBuilder();
//        config.memory()
//                .evictionType(EvictionType.COUNT)
//                .size(65536);
//        config.clustering()
//                .cacheMode(CacheMode.REPL_SYNC);
//        config.indexing().index(Index.ALL)
//                // 인덱스 반영을 비동기로 진행하여 성능 향상, 하지만 조회 결과에 즉각 반영되지 않음, 동기를 원할 경우 directory-based를 명시
//                .addProperty("default.indexmanager", "near-real-time")
//                // 인덱스 정보를 JVM의 힙 영역에 저장하여 성능 향상, 단점은 노드 셧다운시 인덱스 정보가 증발, 생략할 경우 파일 시스템에 저장
//                .addProperty("default.directory_provider", "local-heap");
//
//        cacheManager.defineConfiguration("indexed-cache", config.build());
//        Cache<K, V> indexedCache = cacheManager.getCache("indexed-cache");
//
//        return indexedCache;
//    }
}
