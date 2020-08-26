package com.example.demo.infinispan.domain;

import lombok.*;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Id;
import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
@ToString
@Indexed
public class Member implements Serializable {

    // 역직렬화 시 필요
    private static final long serialVersionUID = -5831165803975670426L;

    @Id
    @Field
    private Long id;

    @Field
    private String name;

    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
