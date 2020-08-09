package com.example.demo.infinispan;

import com.example.demo.infinispan.InfinispanCacheManager;
import com.example.demo.infinispan.domain.Author;
import com.example.demo.infinispan.domain.Book;
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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Resource resource = context.getResource("classpath:data/testData.json");
        List<Map<String, Object>> maps = IOUtil.inputStreamToList(resource.getInputStream());

        Cache<Long, Book> cache = infinispanCacheManager.localCache();
        for(Map<String, Object> map : maps) {
            long id = Long.parseLong(map.get("id").toString());
            String title = map.get("title").toString();
            String description = map.get("description").toString();
            long price = Long.parseLong(map.get("price").toString());

            Map<String, Object> authors = (Map<String, Object>) map.get("authors");
            String name = authors.get("name").toString();
            String surname = authors.get("surname").toString();

            Author author = new Author(name, surname);
            Book book = Book.createBook(id, title, description, price, author);
            cache.put(book.getId(), book);
        }

        System.out.println(cache.size());
    }
}
