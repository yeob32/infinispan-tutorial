package com.example.demo.infinispan;

import com.example.demo.infinispan.InfinispanCacheManager;
import com.example.demo.infra.utils.IOUtil;
import lombok.RequiredArgsConstructor;
import org.infinispan.Cache;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class AppRunner implements ApplicationRunner {

    private final ApplicationContext context;
    private final InfinispanCacheManager infinispanCacheManager;

    // ICE 프로세스
    // nodeService.init()
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // infinispan store example
        // 영구 저장소 -> 루씬 ?
        // 루씬 검색까지 해보고, 캐싱 예제 진행
        Resource resource = context.getResource("classpath:data/testData.json");
        List<Map<String, Object>> maps = IOUtil.inputStreamToList(resource.getInputStream());

        Cache<String, Object> cache = infinispanCacheManager.localCache();
    }
}
