package com.example.student_system.service.account;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.dto.account.LoginRequest;
import com.example.student_system.domain.dto.account.RegisterRequest;
import com.example.student_system.domain.dto.account.UserInfo;
import com.example.student_system.domain.vo.LoginResponse;

public interface UserService {
    /**
     * 用户登录
     * @param loginRequest 登录请求
     * @return 登录响应，包含token和用户信息
     */
    CommonResponse<LoginResponse> login(LoginRequest loginRequest);
    
    /**
     * 用户注册
     * @param registerRequest 注册请求
     * @return 注册结果
     */
    CommonResponse<String> register(RegisterRequest registerRequest);
}
