package com.spring.blog.service;

import com.spring.blog.entity.Blog;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogService {

    // 비즈니스 로직을 담당할 메서드를 "정의"만 하면 됩니다.
    // 전체 블로그 포스팅을 조회하는 메서드 findAll()을 선언
    Page<Blog> findAll(Integer pageNumber);

    Blog findById(long blogId);

    void deleteById(long blogId);

    void save(Blog blog);

    void update(Blog blog);
}
