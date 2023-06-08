package com.spring.blog.dto;

import lombok.*;

@Getter @Setter @ToString @Builder
@AllArgsConstructor
@NoArgsConstructor
public class BmiDTO {
    private double height;
    private double weight;
}
