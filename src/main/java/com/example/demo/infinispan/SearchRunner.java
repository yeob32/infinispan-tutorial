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
 * 쿼리는 항상 단일 엔터티 유형을 대상으로하며 단일 캐시의 내용에 대해 평가됩니다.
 * 여러 캐시에서 쿼리를 실행하거나 여러 엔터티 유형 (조인)을 대상으로하는 쿼리를 만드는 것은 지원되지 않습니다.
 */
@RequiredArgsConstructor
@Component
public class SearchRunner implements ApplicationRunner {

    private final InfinispanCacheManager infinispanCacheManager;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Cache<String, Book> cache = infinispanCacheManager.localCache();

        // Embedded Query using Java Objects
        QueryFactory queryFactory = Search.getQueryFactory(cache);
        Query<Book> q = queryFactory.create("from com.example.demo.infinispan.domain.Book where price > 2000");

        // Execute the query
        QueryResult<Book> queryResult = q.execute();
        System.out.println("queryResult.list() : " + queryResult.list().size());

        // sorted by year and match all books that have "clustering" in their title
        // and return the third page of 10 results
        Query<Book> query = queryFactory.create("FROM com.example.demo.infinispan.domain.Book WHERE title like '%book%' ORDER BY publicationYear");
        query.startOffset(20).maxResults(10);
        List<Book> books = query.execute().list();
        System.out.println("books : " + books);

        // Defining a query to search for various authors and publication years
//        Query<Book> query2 = queryFactory.create("SELECT title FROM com.example.demo.infinispan.domain.Book WHERE author = :authorName AND publicationYear = :publicationYear");

        // Set actual parameter values
//        query2.setParameter("authorName", "Doe");
//        query2.setParameter("publicationYear", 2010);

        // Execute the query
//        List<Book> found = query2.execute().list();
    }
}
