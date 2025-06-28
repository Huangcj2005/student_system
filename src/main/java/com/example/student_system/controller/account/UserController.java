package com.example.student_system.controller.account;

import com.auth0.jwt.interfaces.Claim;
import com.example.student_system.annotation.RateLimit;
import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import com.example.student_system.domain.dto.account.LoginRequest;
import com.example.student_system.domain.dto.account.RegisterRequest;
import com.example.student_system.domain.dto.account.UserInfo;
import com.example.student_system.domain.vo.LoginResponse;
import com.example.student_system.service.account.UserService;
import com.example.student_system.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户登录
     */
    @PostMapping("login")
    public CommonResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    /**
     * 用户注册
     */
    @PostMapping("register")
    public CommonResponse<String> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return userService.register(registerRequest);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("student-info")
    public CommonResponse<UserInfo> getUserInfo(@RequestHeader("Authorization") String token) {
        // 解析 jwt 获取 userId
        String actualToken = token.replace("Bearer ", "");
        Map<String, Claim> claims = jwtUtil.verifyToken(actualToken);
        int userId = claims.get("userId").asInt();

        return userService.getUserInfo(userId);
    }

    /**
     * 该方法用于验证 token 的有效性
     * 在用户登录、请求访问需要 token 的 url 时进行调用
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

    // 限制每分钟最多发送一条邮件
    // 后端限制该方法的使用时间，用于避免直接访问 url 可能带来的问题
    @PostMapping("/verification-codes")
    @RateLimit(value = 1, timeUnit = 60000)
    public CommonResponse<Integer> sendCode(@RequestParam String email) {
        return userService.validateCode(email);
    }
}
