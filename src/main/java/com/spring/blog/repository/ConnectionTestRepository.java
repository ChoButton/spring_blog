package com.spring.blog.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper // 빈 컨테이너에 Mybatis 관리 파일로서 적재
public interface ConnectionTestRepository {

    // getNow 는 실행시 호출할 SQL구문은 xml파일 내부에 작성합니다.
    String getNow();

}

/*
  1. 블로그 포스팅 기능
    닉네임, 제목, 본문, 글번호, 수정날짜, 조회수

  2. 회원가입 기능
    아이디, 비밀번호, 닉네임, 회원번호

  3. 회원이름별로 블로그 포스팅 테이블을 가짐

 */