package com.example.demo.infinispan;

import com.example.demo.infinispan.domain.Book;
import lombok.RequiredArgsConstructor;
import org.infinispan.Cache;
import org.infinispan.query.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Ickle Query Language Parser Syntax
 * https://infinispan.org/docs/stable/titles/developing/developing.html#query_ickle
 */
@RequiredArgsConstructor
@Component
public class SearchRunner2 implements ApplicationRunner {

    private final InfinispanCacheManager infinispanCacheManager;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Cache<String, Book> cache = infinispanCacheManager.localCache();

        // get the query factory from the cache:
        QueryFactory queryFactory = Search.getQueryFactory(cache);
        Query<Book> query = queryFactory.create("from com.example.demo.infinispan.domain.Book where price > 2000");

        // create an Ickle query that will do a full-text search (operator ':') on fields 'title' and 'authors.name'
//        Query<Book> fullTextQuery = queryFactory.create("FROM com.example.demo.infinispan.domain.Book WHERE title:'infinispan' AND authors.name:'sanne'");

        // The ('=') operator is not a full-text operator, thus can be used in both indexed and non-indexed caches
//        Query<Book> exactMatchQuery = queryFactory.create("FROM com.example.demo.infinispan.domain.Book WHERE title = 'Programming Infinispan' AND authors.name = 'Sanne Grinnovero'");

        // Full-text and non-full text operators can be part of the same query
//        Query<Book> query = queryFactory.create("FROM com.example.demo.infinispan.domain.Book b where b.author.name = 'Stephen' and b.description : (+'dark' -'tower')");

        // get the results
        List<Book> found = query.execute().list();
    }
}
