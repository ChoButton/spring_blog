package com.spring.blog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


// Entity는 불변성을 지키기 위해 Setter를 제공하지 않습니다.
// 한번 생성된 데이터가 변경될 가능성을 없앱니다.
@Getter @ToString @Builder @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;
    @Column(nullable = false)
    private Long blogId;
    @Column(nullable = false)
    private String replyWriter;
    @Column(nullable = false)
    private String replyContent;
    private LocalDateTime publishedAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void setDefaultValue(){
        this.publishedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}