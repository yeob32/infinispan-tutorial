package com.example.demo.infinispan;

import com.example.demo.infinispan.domain.Book;
import com.example.demo.infinispan.domain.Member;
import com.example.demo.infra.utils.IOUtil;
import lombok.RequiredArgsConstructor;
import org.infinispan.Cache;
import org.infinispan.query.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class StoreRunner {

    private final ApplicationContext context;
    private final InfinispanCacheManager infinispanCacheManager;

    @Bean
    ApplicationRunner applicationRunner(ApplicationArguments args) throws IOException {
        return args1 -> {
            Cache<String, Object> cache = infinispanCacheManager.storeCache("storeCache");

            // 저장소 캐시 읽어서 메모리 로드
//            Resource resource = context.getResource("classpath:data/testData2.json");
//            List<Map<String, Object>> maps = IOUtil.inputStreamToList(resource.getInputStream());
//
//            maps.forEach(map -> {
//                long id = Long.parseLong(map.get("id").toString());
//                String title = map.get("name").toString();
//
//                Member member = new Member(id, title);
//                cache.put("member_" + member.getId(), member);
//            });

            System.out.println("storeCache : " + cache.size());

            QueryFactory queryFactory = Search.getQueryFactory(cache);
//            Query<Member> query = queryFactory.create("from com.example.demo.infinispan.domain.Member where name like '%yeob%'");
            Query<Member> query = queryFactory.create("from com.example.demo.infinispan.domain.Member where name = :name");
            query.setParameter("name", "yeob");

            Optional<Member> findMember = query.execute().list().stream().findAny();
            findMember.ifPresent(member -> System.out.println("findMember : " + findMember));
        };
    }
}
