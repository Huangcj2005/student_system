package com.example.student_system.controller.account;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import com.example.student_system.domain.dto.account.LoginRequest;
import com.example.student_system.domain.dto.account.RegisterRequest;
import com.example.student_system.domain.vo.LoginResponse;
import com.example.student_system.service.account.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public CommonResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = userService.login(loginRequest);
            return CommonResponse.createForSuccess(ResponseCode.SUCCESS.getCode(), "登录成功", response);
        } catch (Exception e) {
            return CommonResponse.createForError(ResponseCode.ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public CommonResponse<String> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            boolean success = userService.register(registerRequest);
            if (success) {
                return CommonResponse.createForSuccess(ResponseCode.USER_REGISTER_SUCCESS.getCode(), 
                    ResponseCode.USER_REGISTER_SUCCESS.getDescription());
            } else {
                return CommonResponse.createForError(ResponseCode.ERROR.getCode(), "注册失败");
            }
        } catch (Exception e) {
            return CommonResponse.createForError(ResponseCode.ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/user/info")
    public CommonResponse<LoginResponse.UserInfo> getUserInfo(HttpServletRequest request) {
        try {
            Integer userId = (Integer) request.getAttribute("userId");
            if (userId == null) {
                return CommonResponse.createForError(ResponseCode.TOKEN_MISSING.getCode(), 
                    ResponseCode.TOKEN_MISSING.getDescription());
            }
            
            LoginResponse.UserInfo userInfo = userService.getUserInfo(userId);
            if (userInfo == null) {
                return CommonResponse.createForError(ResponseCode.ERROR.getCode(), "用户不存在");
            }
            
            return CommonResponse.createForSuccess(ResponseCode.SUCCESS.getCode(), "获取用户信息成功", userInfo);
        } catch (Exception e) {
            return CommonResponse.createForError(ResponseCode.ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 验证token
     */
    @GetMapping("/verify")
    public CommonResponse<String> verifyToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                Integer userId = userService.validateToken(token);
                if (userId != null) {
                    return CommonResponse.createForSuccess(ResponseCode.SUCCESS.getCode(), "token有效");
                }
            }
            return CommonResponse.createForError(ResponseCode.TOKEN_INVALID.getCode(), 
                ResponseCode.TOKEN_INVALID.getDescription());
        } catch (Exception e) {
            return CommonResponse.createForError(ResponseCode.ERROR.getCode(), e.getMessage());
        }
    }
}
