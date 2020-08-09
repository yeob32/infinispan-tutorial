package com.example.demo.infinispan.domain;

import lombok.Getter;
import org.hibernate.search.annotations.*;
import org.springframework.stereotype.Indexed;

import javax.persistence.Id;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// Values you want to index need to be annotated with @Indexed,
// then you pick which fields and how they are to be indexed:
@Indexed
@Getter
public class Book {
    @Id
    @Field
    Long id;

    @Field
    String title;

    @Field
    String description;

    @Field
    @DateBridge(resolution = Resolution.YEAR)
    Date publicationYear;

    @Field
    Long price;

    @IndexedEmbedded
    Set<Author> authors = new HashSet<>();

    public static Book createBook(long id, String title, String description, long price, Author author) {
        Book book = new Book();
        book.id = id;
        book.title = title;
        book.description = description;
        book.price = price;
        book.authors.add(author);

        return book;
    }
}
