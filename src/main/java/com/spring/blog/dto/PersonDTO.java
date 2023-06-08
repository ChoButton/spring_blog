package com.spring.blog.dto;

import lombok.*;

@Getter @Setter @ToString @Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {

    private long id;
    private String name;
    private int age;

}
