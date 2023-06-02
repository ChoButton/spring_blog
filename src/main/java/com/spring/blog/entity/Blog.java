package com.spring.blog.entity;

import lombok.*;

import java.sql.Date;

// 역직렬화(디비 -> 자바객체) 가 가능하도록 blog 테이블 구조에 맞춰서 멤버변수를 선언해주세요
@Setter @Getter @ToString
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 멤버 변수를 다 요구하는 생성자
@Builder // 빌더패턴 생성자를 쓸수있게 해줌 (생성자가 있어야 쓸수 있음 @AllArgsConstructor 이 있기때문에 사용가능)
public class Blog {
    private long blogId; // 숫자는 어지간하면 long형을 사용합니다.
    private String writer;
    private String blogTitle;
    private String blogContent;
    private Date publishedAt;
    private Date updatedAt;
    private long blogCount;
}
