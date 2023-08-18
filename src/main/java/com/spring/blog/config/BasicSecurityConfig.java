package com.spring.blog.config;

import com.spring.blog.config.jwt.TokenProvider;
import com.spring.blog.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.spring.blog.config.oauth.OAuth2SuccessHandler;
import com.spring.blog.config.oauth.OAuth2UserCustomService;
import com.spring.blog.repository.RefreshTokenRepository;
import com.spring.blog.service.UserService;
import com.spring.blog.service.UsersService;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // 설정 클래스 상위에 붙이는 어노테이션
@RequiredArgsConstructor
public class BasicSecurityConfig { // 베이직 방식 인증을 사용하도록 설정하는 파일
    // final 을 붙이는 이유는 해당 객체들을 변경할 이유가 없기 때문에 불변성 보장
    // 등록할 시큐리티 서비스 멤버변수로 작성하기
    private final UserDetailsService userService;
    private final TokenProvider tokenProvider;

    // OAuth2.0을 활용하기 위한 객체들 추가하기
    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UsersService usersService;

//    public BasicSecurityConfig(UserDetailsService userService, TokenProvider tokenProvider){
//        this.userService = userService;
//        this.tokenProvider = tokenProvider;
//    }

    // 정적 파일이나 .jsp 파일 등 스프링 시큐리티가 기본적으로 적용되지 않을 영역 설정하기.
    @Bean // @Configuration 어노테이션 붙은 클래스 내부 메서드가 리턴하는 자료는 자동으로 빈에 등록됩니다.
    public WebSecurityCustomizer configure(){

        return web -> web.ignoring() // 시큐리티 적용을 안한 경로
                .requestMatchers("/static/**")
                // 기본 경로는 src/main/java/resources 로 잡히고
                // 추후 설정할 정적자원 저장 경로에 보안을 풀었음.
                .dispatcherTypeMatchers(DispatcherType.FORWARD);
                // MVC방식에서 뷰단 파일을 로딩하는것을 보안범위에서 해제.
                // 이 설정을 하지 않으면, .jsp 파일이 화면에 출력되지 않습니다.

    }

    // http 요청에 대한 웹 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http // 6.1 버전 이후
                .authorizeHttpRequests(authorizationConfig -> {
                    authorizationConfig
                            .requestMatchers("/login", "/signup", "/user")
                            .permitAll()
                            .anyRequest()
                            .authenticated();
                })
                .formLogin(formLoginConfig -> {
                    formLoginConfig
                            //.loginPage("/login")
                            //.defaultSuccessUrl("/blog/list")
                            .disable();
                })
                .logout(logoutConfig -> {
                    logoutConfig
                            // 디폴트로 "/logout" 로 잡아주기 때문에 굳이 설정할 필요 없음 바꾸고 싶은 경우만 설정해주기
                            //.logoutUrl("/logout")
                            .logoutSuccessUrl("/login")
                            .invalidateHttpSession(true);
                })
                .sessionManagement(sessionConfig -> {
                    sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                // 한줄짜리 람다식은 이렇게 표현도 가능
                .csrf(AbstractHttpConfigurer::disable)
                // OAuth2.0 에 관련된 형식 추가
                .oauth2Login(oauth2Config -> {
                    // 로그인 성공시 리다이렉트할 페이지 설정
                    oauth2Config.loginPage("/login")
                        // 인가에 대한 요청을 서비스가 들어오면 처리해줄 레포지토리를 지정
                        .authorizationEndpoint(endpointConfig -> endpointConfig
                            .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())) // 하단에 메서드
                         // 로그인에 성공 했을때 처리해줄 핸들러를 매칭
                        .successHandler(oAuth2SuccessHandler()) // 하단에 메서드
                         // 유저정보를 어떻게 처리해줄지를 설정
                        .userInfoEndpoint(userInfoConfig -> userInfoConfig
                            .userService(oAuth2UserCustomService));
                })
               // Before시점(Request를 서버가 처리하기 직전 시점)에 해당 필터를 사용해 로그인을 검증하도록 설정
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();


                /*6.1 버전 이전(지금은 사용 안함)
                .authorizeRequests() // 인증, 인가 설정 시작부에 사용하는 메서드
                .requestMatchers("/login", "/signup", "/user")
                .permitAll() // 위 경로들은 인증 없이 접속 가능
                .anyRequest().authenticated() // 위 적힌 경로 말고는 로그인 필수
                .and() // 다음 설정으로 넘어가기
                .formLogin() // 로그인 폼으로 로그인 제어
                .loginPage("/login") // 로그인 페이지로 지정할 주소
                .defaultSuccessUrl("/blog/list") // 로그인 하면 처음으로 보여질 페이지
                .and()
                .logout() // 로그아웃 관련 설정
                .logoutSuccessUrl("/login") // 로그아웃 성공했으면 넘어갈 경로
                .invalidateHttpSession(true) // 로그아웃하면 다음 접속시 로그인이 풀려있게 설정
                .and()
                .csrf().disable() // csrf 공격 방지용 토큰을 쓰지 않음
                .build();*/


    }

    // 위의 설정을 따라가는 인증은 어떤 서비스 클래스를 통해서 설정할 것인가?
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,
                                                       UserService userService) throws Exception{
        return http
                .getSharedObject(AuthenticationManagerBuilder.class)
                // userService에 기술된 내용을 토대로 로그인 처리
                .userDetailsService(userService)
                // 비밀번호 암호화 저장 모듈
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    // 암호화 모듈 임포트
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // 필터 클래스 생성 (필터 클래스도 빈 컨테이너에 적재되어 있어야 사용 가능하므로)
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(){
        // 필터는 생성자에서 토큰 제공자(TokenProvider 클래스)를 요구합니다.
        return new TokenAuthenticationFilter(tokenProvider);
    }

    // Oauth2.0 활용에 필요한 빈 정의
    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(tokenProvider,
                refreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository(),
                usersService);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }


}
