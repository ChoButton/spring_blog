package com.spring.blog.service;

import com.spring.blog.entity.Blog;
//import com.spring.blog.entity.User;
import com.spring.blog.repository.BlogJPARepository;
import com.spring.blog.repository.BlogRepository;
import com.spring.blog.repository.ReplyJPARepository;
import com.spring.blog.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService{

    BlogRepository blogRepository;
    ReplyRepository replyRepository;
    BlogJPARepository blogJPARepository;
    ReplyJPARepository replyJPARepository;

    @Autowired // 생성자 주입이 속도가 빠름
    public BlogServiceImpl(BlogRepository blogRepository,
                           ReplyRepository replyRepository,
                           BlogJPARepository blogJPARepository,
                           ReplyJPARepository replyJPARepository){
        this.blogRepository = blogRepository;
        this.replyRepository = replyRepository;
        this.blogJPARepository = blogJPARepository;
        this.replyJPARepository = replyJPARepository;
    }

    @Override
    public Page<Blog> findAll(Integer pageNumber) { // 페이지 정보를 함께 포함하고 있는 리스트인 Page를 리턴해야함
        //return blogRepository.findAll(); //마이바티스를 이용해 전체글 가져오기
        //return blogJPARepository.findAll(); // JPA를 이용해 전체글 가져오기

        pageNumber = getCalibratedPageNum(pageNumber);

        // 페이징 처리에 관련된 정보를 먼저 객체로 생성합니다

        Pageable pageable = PageRequest.of((pageNumber - 1), 5, Sort.by("blogId").descending());
        // 생성된 페이징 정보를 파라미터로 제공해서 findAll을 호출합니다.
        return blogJPARepository.findAll(pageable);
    }

    @Override
    public Blog findById(long blogId){
        blogRepository.viewsUp(blogId);
        //JPA의 findById는 옵셔널을 리턴하므로, 일반 객체로 만들기 위해 뒤에 .get()을 사용합니다.
        //옵셔널은 참조형 변수에 대해 null검사 및 처리를 쉽게 할 수 있도록 제공하는 제너릭입니다.
        //JPA는 옵셔널을 쓰는것을 권장하기 위해 리턴 자료형으로 강제해뒀습니다.
        return blogJPARepository.findById(blogId).get();
    }

    @Transactional
    @Override
    public void deleteById(long blogId) {
        //replyRepository.deleteAllReplyByBlogId(blogId);
        //blogRepository.deleteById(blogId);
        replyJPARepository.deleteAllByBlogId(blogId);
        blogJPARepository.deleteById(blogId);
    }

    @Override
    public void save(Blog blog) {
        //blogRepository.save(blog);
        blogJPARepository.save(blog);
    }

    @Override
    public void update(Blog blog) {
        // JPA의 수정은, findById()를 이용해 얻어온 엔터티 클래스의 내부 내용물을 수정한 다음
        // 해당 요소를 save() 해서 이뤄집니다.
        Blog updatedBlog = blogJPARepository.findById(blog.getBlogId()).get(); // 준영속 상태
        updatedBlog.setBlogContent(blog.getBlogContent());
        updatedBlog.setBlogTitle(blog.getBlogTitle());
        updatedBlog.setUpdatedAt(LocalDateTime.now());
        blogJPARepository.save(updatedBlog);
    }

    @Override
    public void createBlogTable(String loginId) {
        blogRepository.createBlogTable(loginId);
    }

    public int getCalibratedPageNum(Integer pageNumber){
        // 아무 주소가 없을시 첫페이지 리턴
        if(pageNumber == null || pageNumber <= 0){
            pageNumber = 1;

            return pageNumber;
        }

        int totalPagesCount = (int)Math.ceil(blogJPARepository.count() / 5.0);

        pageNumber = pageNumber > totalPagesCount ? totalPagesCount : pageNumber;
        return pageNumber;
    }

}
