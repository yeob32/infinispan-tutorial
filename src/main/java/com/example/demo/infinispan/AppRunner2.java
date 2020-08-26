package com.example.demo.infinispan;

import com.example.demo.infinispan.domain.Member;
import lombok.RequiredArgsConstructor;
import org.infinispan.Cache;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AppRunner2 implements ApplicationRunner {

    private final InfinispanCacheManager infinispanCacheManager;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Member member1 = new Member(1L, "yeob32");
        Member member2 = new Member(2L, "sykim");
        Cache<Long, Member> cache = infinispanCacheManager.createCache("member");
        cache.put(member1.getId(), member1);
        cache.put(member2.getId(), member2);

        System.out.println("AppRunner2 member : " + cache.get(1L));

        member1.setName("yeob33");
        System.out.println("AppRunner2 member : " + cache.get(1L));

        cache.put(1L, member1);
        System.out.println("AppRunner2 member : " + cache.get(1L));
    }
}
