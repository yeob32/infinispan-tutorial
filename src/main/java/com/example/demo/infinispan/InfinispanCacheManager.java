package com.example.demo.infinispan;

import org.hibernate.search.cfg.Environment;
import org.hibernate.search.cfg.SearchMapping;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.StorageType;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.transaction.lookup.GenericTransactionManagerLookup;
import org.infinispan.util.concurrent.IsolationLevel;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Component
public class InfinispanCacheManager {

    private final EmbeddedCacheManager cacheManager;

    public InfinispanCacheManager(EmbeddedCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public <K, V> Cache<K, V> createCache(String cacheName) {
        Cache<K, V> cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            ConfigurationBuilder config = new ConfigurationBuilder();
            config.memory().whenFull(EvictionStrategy.REMOVE).maxSize("1000");
            config.expiration().lifespan(2, TimeUnit.HOURS); // 캐시 만료 시간

            cacheManager.defineConfiguration(cacheName, config.build());
            cache = cacheManager.getCache(cacheName);
        }

        return cache;
    }

    public <K, V> Cache<K, V> localCache() {
        String cacheName = "local-cache";

        Cache<K, V> cache = cacheManager.getCache(cacheName, false);
        if (cache == null) {
            ConfigurationBuilder config = new ConfigurationBuilder();
            config.memory().whenFull(EvictionStrategy.REMOVE).maxCount(1000);
            config.expiration()
                    .wakeUpInterval(1, TimeUnit.MINUTES) // 메모리에서 만료된 항목을 정리하기 위한 후속 실행 간의 간격(밀리초), 정기적인 제거 프로세스를 모두 실행 중지하려면 웨이크업을 설정하십시오.-1까지의 간격.
                    .lifespan(1, TimeUnit.SECONDS) // lifespan 시간 후 만료
                    .maxIdle(2, TimeUnit.SECONDS); // maxIdle 마지막 엑세스 시간 후 만료

            cacheManager.defineConfiguration(cacheName, config.build());
            cache = cacheManager.getCache(cacheName);
        }

        return cache;
    }

    public <K, V> Cache<K, V> storeCache(String cacheName) {
        ConfigurationBuilder config = new ConfigurationBuilder();
        config.locking()
                .isolationLevel(IsolationLevel.REPEATABLE_READ);
        config.transaction()
                .lockingMode(LockingMode.OPTIMISTIC) // 낙관적 잠금
                .autoCommit(true)
                .completedTxTimeout(60000)
                .transactionMode(TransactionMode.TRANSACTIONAL)
                .useSynchronization(false)
                .notifications(true)
                .reaperWakeUpInterval(30000)
                .cacheStopTimeout(30000)
                .transactionManagerLookup(new GenericTransactionManagerLookup())
                .recovery().enabled(false).recoveryInfoCacheName("__recoveryInfoCacheName__");

        config.persistence()
                .passivation(false) // true -> Infinispan이 항목을 제거 할 때 캐시 저장소에 데이터를 유지
                .addSingleFileStore()
                .preload(true)
                .shared(false)
                .fetchPersistentState(true)
                .ignoreModifications(false) // 수정 반영 안하는거?
                .purgeOnStartup(false) // 스타트업 할때 다 지우는건가?
                .location("classpath:store")
                .indexing().enable().withProperties(getIndexProperties());
//                .async()
//                .enabled(true);

        cacheManager.defineConfiguration(cacheName, config.build());
        Cache<K, V> cache = cacheManager.getCache(cacheName);
        return cache;
    }

    public Properties getIndexProperties() {
        SearchMapping mapping = new SearchMapping();
//        mapping.entity(Author.class).indexed()
//                .property("name", ElementType.METHOD).field()
//                .property("surname", ElementType.METHOD).field();

        Properties properties = new Properties();
//        properties.put("default.indexmanager", "near-real-time"); // 데이터 손실 발생 가능
        properties.put("default.directory_provider", "filesystem");
        properties.put("default.indexBase", "classpath:store/index");
        properties.put(Environment.MODEL_MAPPING, mapping);

        return properties;
    }
}
