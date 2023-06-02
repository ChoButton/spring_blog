package com.spring.blog.service;

import com.spring.blog.entity.Blog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BlogServiceTest {

    @Autowired
    BlogService blogService;

    @Test
    @Transactional // 디비 커밋을 하지 않음 (실제 디비에 어떠한 영향도 주지않음)
    public void findAllTest(){
        // given : 없음

        // when : 전체 데이터 가져오기
        List<Blog> blogList = blogService.findAll();
        // then : 길이가 3일 것이다
        assertEquals(3, blogList.size());
        assertThat(blogList.size()).isEqualTo(3); //import assertj
    }

    @Test
    @Transactional
    public void findById(){
        long id = 2;

        Blog blog = blogService.findById(id);

        assertEquals("2번유저", blog.getWriter());
        assertEquals("2번제목", blog.getBlogTitle());
        assertEquals(2, blog.getBlogId());
    }

    @Test
    @Transactional
    //@Commit // 트렌젝션 적용된 테스트의 결과를 커밋해서 디비에 반영하도록 만듦
    public void deleteByIdTest(){
        long id = 2;

        blogService.deleteById(id);

        assertEquals(2, blogService.findAll().size());
        assertNull(blogService.findById(id));
    }

    @Test
    @Transactional
    public void saveTest(){
        String writer = "4번유저";
        String blogTitle = "4번제목";
        String blogContent = "4번본문";

        Blog newBlog = Blog.builder()
                .writer(writer)
                .blogTitle(blogTitle)
                .blogContent(blogContent)
                .build();
        int id = 3;

        blogService.save(newBlog);
        List<Blog> blogList = blogService.findAll();

        assertEquals(4, blogList.size());
        assertEquals(writer, blogList.get(id).getWriter());
        assertEquals(blogTitle, blogList.get(id).getBlogTitle());
        assertEquals(blogContent, blogList.get(id).getBlogContent());
    }

    @Test
    @Transactional
    public void updateTest(){
        String blogTitle = "수정된제목";
        String blogContent = "수정된본문";
        long id = 2;

        Blog blog = blogService.findById(id);
        blog.setBlogTitle(blogTitle);
        blog.setBlogContent(blogContent);

        blogService.update(blog);

        assertEquals(2, blog.getBlogId());
        assertEquals(blogTitle, blog.getBlogTitle());
        assertEquals(blogContent, blog.getBlogContent());
    }
}