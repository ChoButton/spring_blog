package com.spring.blog.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class RefreshTokenRequestDTO {

    private String refreshToken;

}
