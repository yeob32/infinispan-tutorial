package com.example.demo.infinispan.domain;

import lombok.*;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

@Getter
@EqualsAndHashCode
public class Author {
    @Field
    String name;

    @Field
    String surname;
    // hashCode() and equals() omitted

    public Author(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }
}
