package com.spring.blog.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("jwt") //프로펄티즈 파일중 jwt 프로퍼티에 속한 항목을 받아서 바인딩해중
public class JwtProperties {
    private String issuer;   // jwt.issuer 값을 저장
    private String secretKey; // 원래 저장은 secret_Key였으나, 자동으로 카멜케이스로 바꿔서 매핑됨
                              // jwt.secret_Key값을 저장
}
