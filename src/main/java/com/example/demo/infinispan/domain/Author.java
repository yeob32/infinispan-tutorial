package com.example.demo.infinispan.domain;

import lombok.*;
import org.hibernate.search.annotations.Field;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Author {
    @Field
    String name;

    @Field
    String surname;
    // hashCode() and equals() omitted
}
