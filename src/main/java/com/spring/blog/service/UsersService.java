package com.spring.blog.service;

import com.spring.blog.entity.User;
import com.spring.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor // final 또는 notnull 이 붙은 생성자를 생성해주는 어노테이션
@Service
public class UsersService { // UserService는 "인증"만 담당하고, 나머지 회원가입 등은 이 서비스 객체로 처리함,

    private final UserRepository userRepository;
    // 암호화 객체가 필요함(디비에 비밀번호를 암호화해서 넣어야 하기 떄문)
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /* @RequiredArgsConstructor 어노테이션을 통해 생성자를 자동으로 생성해줌
    @Autowired
    public UsersService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }*/

    // 폼에서 날려준 정보를 DB에 적재하되, 비밀번호는 암호화(인코딩)을 진행한 구문을 인서트
    public void save(User user){
        User newUser = User.builder()
                        .email(user.getEmail())
                        .loginId(user.getLoginId())
                         // 입력된 password를 bCrypt 인코딩후 적재
                        .password(bCryptPasswordEncoder.encode(user.getPassword()))
                        .build();
        userRepository.save(newUser);
    }

    // 아이디를 집어넣으면, 해당 계정 전체 정보를 얻어올 수 있는 메서드 작성
    public User getByCredentials(String loginId){
        return userRepository.findByLoginId(loginId);
    }
}
