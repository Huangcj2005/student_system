package com.example.student_system.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class JwtConfig {
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private Long expiration;
}
