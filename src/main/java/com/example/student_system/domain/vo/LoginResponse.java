package com.example.student_system.domain.vo;

import com.example.student_system.domain.dto.account.UserInfo;
import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private UserInfo userInfo;
} 