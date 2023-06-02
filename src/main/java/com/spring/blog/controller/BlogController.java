package com.spring.blog.controller;

import com.spring.blog.entity.Blog;
import com.spring.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller // 컨트롤러 어노테이션은 1.빈 등록 2.url 매핑 처리 기능을 함께 가지고 있으므로 다른 어노테이션과
            // 교환해서 쓸 수 없다.
@RequestMapping("/blog")
public class BlogController {

    // 컨트롤러 레이어는 서비스 레이어를 직접 호출합니다.

    private BlogService blogService;

    @Autowired
    public BlogController(BlogService blogService){
        this.blogService = blogService;
    }

    // /blog/list 주소로 get방식 접속했을때
    // 1. 서비스 객체를 이용해 게시글 전체를 얻어오세요
    // 2. 얻어온 게시글을 .jsp 로 보낼 수 있도록 적재해 주세요
    // 3. .jsp 에서 볼 수 있도록 출력해 주세요
    // 헤당 파일의 이름은 bord/list.jsp 입니다.

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model){
        List<Blog> blogList = blogService.findAll();

        model.addAttribute("blogList", blogList);

        return "blog/list";
    }

}
