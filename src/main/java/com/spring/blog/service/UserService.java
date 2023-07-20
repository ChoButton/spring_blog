package com.spring.blog.service;

import com.spring.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 인터페이스는 라이브러리에 이미 내장됨, 그래서 구현체를 바로 만들수있음
@RequiredArgsConstructor // final 이나 notnull 이 붙은 생성자를 생성해주는 어노테이션
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    /*
    @Autowired // 스프링 4버전부터 단일 멤버변수는 생성자만 있으면 자동 주입됨
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }*/

    @Override // 회원 관련해서는 로그인 되는지 마는지 여부만 따지므로 이것만 구현하면 됨.
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return userRepository.findByLoginId(loginId); // 로그인 아아디 입력되면 회원 정보 리턴
    }


}
