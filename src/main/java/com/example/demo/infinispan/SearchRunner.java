package com.example.demo.infinispan;

import com.example.demo.infinispan.domain.Book;
import lombok.RequiredArgsConstructor;
import org.infinispan.Cache;
import org.infinispan.query.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.infinispan.query.dsl.QueryResult;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 쿼리는 항상 단일 엔터티 유형을 대상으로하며 단일 캐시의 내용에 대해 평가됩니다. 여러 캐시에서 쿼리를 실행하거나 여러 엔터티 유형 (조인)을 대상으로하는 쿼리를 만드는 것은 지원되지 않습니다.
 */
@RequiredArgsConstructor
@Component
public class SearchRunner implements ApplicationRunner {

    private final InfinispanCacheManager infinispanCacheManager;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Cache<String, Book> cache = infinispanCacheManager.localCache();

        QueryFactory queryFactory = Search.getQueryFactory(cache);
        // Defining a query to search for various authors and publication years
        Query<Book> query = queryFactory.create("SELECT title FROM com.acme.Book WHERE author = :authorName AND publicationYear = :publicationYear");

        // Set actual parameter values
        query.setParameter("authorName", "Doe");
        query.setParameter("publicationYear", 2010);

        // Execute the query
        List<Book> found = query.execute().list();



        // get the query factory from the cache:
        // QueryFactory queryFactory = Search.getQueryFactory(cache);

        // create an Ickle query that will do a full-text search (operator ':') on fields 'title' and 'authors.name'
        Query<Book> fullTextQuery = queryFactory.create("FROM com.acme.Book WHERE title:'infinispan' AND authors.name:'sanne'");

        // The ('=') operator is not a full-text operator, thus can be used in both indexed and non-indexed caches
        Query<Book> exactMatchQuery = queryFactory.create("FROM com.acme.Book WHERE title = 'Programming Infinispan' AND authors.name = 'Sanne Grinnovero'");

        // Full-text and non-full text operators can be part of the same query
        Query<Book> query2 = queryFactory.create("FROM com.query.Book b where b.author.name = 'Stephen' and b.description : (+'dark' -'tower')");

        // get the results
        List<Book> found2 = query.execute().list();
    }
}
