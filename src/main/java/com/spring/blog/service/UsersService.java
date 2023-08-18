package com.spring.blog.service;

import com.spring.blog.entity.User;
import com.spring.blog.repository.BlogRepository;
import com.spring.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

//@RequiredArgsConstructor // final 또는 notnull 이 붙은 생성자를 생성해주는 어노테이션
@Service
public class UsersService { // UserService는 "인증"만 담당하고, 나머지 회원가입 등은 이 서비스 객체로 처리함,

    private final UserRepository userRepository;

    // 암호화 객체가 필요함(디비에 비밀번호를 암호화해서 넣어야 하기 떄문)

    /*순환참조 문제 해결 1
      의존성 주입이 아닌 생성자를 통해서 BCryptPasswordEncoder 사용*/
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // @RequiredArgsConstructor 어노테이션을 통해 생성자를 자동으로 생성해줌
    @Autowired
    public UsersService(UserRepository userRepository,
                        //  순환참조 문제 해결 2 @Lazy 어노테이션을 통해 순환참조 문제 해결가능 (지연주입)
                        @Lazy BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // 폼에서 날려준 정보를 DB에 적재하되, 비밀번호는 암호화(인코딩)을 진행한 구문을 인서트
    public void save(User user){
        // 순환참조 문제 해결 1
        //BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
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

    // 회원 가입이 되었는지 안되었는지 체킹하기 위해서 조회하는 구문 추가
    public User findById(Long userId){
        return userRepository.findById(userId).get();
    }

    // 소셜로그인은 이메일 기반 로그인이 되므로 이메일로도 조회
    public User findByEmail(String email){
        // 유저레포지토리에 쿼리메서드 형식으로 이메일 조회 추가
        return userRepository.findByEmail(email);
    }

}
