package com.example.demo.infinispan.config;

import org.infinispan.commons.marshall.JavaSerializationMarshaller;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.jboss.marshalling.core.JBossUserMarshaller;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CacheConfiguration {

    @Primary
    @Bean("cacheManager")
    public EmbeddedCacheManager cacheManager() {
//        GlobalConfiguration globalConfig = new GlobalConfigurationBuilder()
//                .cacheContainer().statistics(true) // Enables Cache Manager statistics.
//                .jmx().enable() // Exports statistics via JMX MBeans.
//                .build();

        //.transport().defaultTransport()
        // 클러스터의 이름을 설정
        //.clusterName("some-cache-cluster") // 클러스터링 모드를 안쓸거면 필요없잖아?.
        // 기본 캐시의 이름을 설정
        //.defaultCacheName("default-cache");

        // 기본 캐시 설정 . 이것도 필요 없잖아?
//        ConfigurationBuilder config = new ConfigurationBuilder();
//        config.memory().evictionType(EvictionType.COUNT).size(1024L);
//        config.simpleCache(true);

//        EmbeddedCacheManager cacheManager = new DefaultCacheManager(globalConfig);

        GlobalConfigurationBuilder builder = new GlobalConfigurationBuilder();
        builder.serialization()
                .marshaller(new JavaSerializationMarshaller())
                .whiteList()
                .addRegexps("com.example.demo.infinispan.domain.Member");

//        builder.serialization()
//                .marshaller(new JBossUserMarshaller());

        return new DefaultCacheManager(builder.build());
    }
}
