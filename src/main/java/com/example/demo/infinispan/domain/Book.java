package com.example.demo.infinispan.domain;

import org.hibernate.search.annotations.*;
import org.springframework.stereotype.Indexed;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// Values you want to index need to be annotated with @Indexed,
// then you pick which fields and how they are to be indexed:
@Indexed
public class Book {
    @Field
    String title;

    @Field
    String description;

    @Field
    @DateBridge(resolution = Resolution.YEAR)
    Date publicationYear;

    @IndexedEmbedded
    Set<Author> authors = new HashSet<Author>();
}
