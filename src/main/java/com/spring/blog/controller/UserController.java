package com.spring.blog.controller;

import com.spring.blog.config.jwt.TokenProvider;
import com.spring.blog.dto.AccessTokenResponseDTO;
import com.spring.blog.entity.User;
import com.spring.blog.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Duration;

@Controller
//@RequiredArgsConstructor
public class UserController {

    private final UsersService usersService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder; // 암호구문 비교를 위해서 추가

    private final TokenProvider tokenProvider; // 로그인 성공자에 대해서 토큰 발급을 위해서 추가

    public UserController(UsersService usersService, BCryptPasswordEncoder bCryptPasswordEncoder, TokenProvider tokenProvider){
        this.usersService = usersService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenProvider = tokenProvider;
    }

    // Get방식으로 로그인창으로 넘어가는 로직을 작성해 주세요.
    // /WEB-INF/views/user/login.jsp 로 넘어가게 해주세요
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String longin(){
        return "user/login";
    }

    // Get방식으로 회원가입 폼으로 넘어가는 로직을 작성해주세요
    // /WEB-INF/views/user/signup.jsp 로 넘어가게 해주세요
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signupForm(){
        return "user/signup";
    }

    // Post방식으로 회원가입 요청을 처리하게 해주세요
    // 주소는 localhost:8080/singup 입니다.
    // 커맨드 객체로 User Entity를 선언해서, 가입정보를 받아 .save()를 호출해 주시면 됩니다.
    // 실행결과는 리다이렉트로 /login으로 돌아가게 해주세요
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(User user){
        usersService.save(user);
        return "redirect:/login";
    }

    @PostMapping("/login")
    @ResponseBody // REST컨트롤러가 아닌 컨트롤러에서 REST 형식으로 리턴을 하게 하고 싶으면 메서드 위에 해당 어노테이션을 붙이기
    public ResponseEntity<?> authenticate(User user){

        // 폼에서 입력한 로그인 아이디를 이용해 DB애 저장된 전체 정보 얻어오기
        User userInfo = usersService.getByCredentials(user.getLoginId());

        // 유저가 폼에서 날려주는 정보는 id랑 비번인데, 먼저 아이디를 통해 위에서 정보를 얻어오고
        // 비밀번호는 암호화 구문끼리 비교해야 하므로, 이 경우 bCryptEncoder의 .match(평문, 암호문)를 이용하면
        // 같은 암호화 구문끼리 비교하는 효과가 생깁니다.
        // 상단에 bCryptPasswordEncoder 의존성 생성한 후, if문 내부에서 비교합니다.
                                        // 폼에서 날려준 평문   // 디비에 들어있던 암호문

        if(bCryptPasswordEncoder.matches(user.getPassword(), userInfo.getPassword())){
                        // 아이디와 비번을 모두 정확하게 입력한 사용자에게 토큰 발급
                                                    //사용자 정보를 토대로 2시간 동안 유요한 엑세스 토큰 생성
            String token = tokenProvider.generateToken(userInfo, Duration.ofHours(2));
            // json으로 리턴을 하고 싶으면, 클래스 요소를 리턴해야 합니다.
            // AccessTokenResponseDTO를 dto패키지에 생성해야 합니다. 멤버변수로 token만 가집니다.
            AccessTokenResponseDTO tokenDTO = new AccessTokenResponseDTO(token);
            return ResponseEntity.ok(tokenDTO); // 발급 성공시 토큰 리턴
        }else {
            return ResponseEntity.badRequest().body("login failed"); // 비번이나 아이디 틀리면 로그인 실패
        }

    }
}
