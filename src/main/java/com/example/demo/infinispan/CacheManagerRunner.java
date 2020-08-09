package com.example.demo.infinispan;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CacheManagerRunner implements ApplicationRunner {

    private final EmbeddedCacheManager cacheManager;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("status : {}, clusterName: {}, address: {}, members: {}, getNumberOfNodes: {}"
                , cacheManager.getStatus(), cacheManager.getClusterName(), cacheManager.getAddress()
                , cacheManager.getMembers(), cacheManager.getHealth().getClusterHealth().getNumberOfNodes());

//        simpleCache.evict("hihi"); // 캐시 데이터 삭제, 모든 노드로 전파되지 않음
//        simpleCache.remove("hihi"); // 캐시 데이터 삭제, 모든 노드로 전파
    }
}
