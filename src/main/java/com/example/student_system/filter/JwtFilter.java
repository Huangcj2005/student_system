package com.example.student_system.filter;

import com.example.student_system.service.account.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtFilter implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        // 获取请求头中的Authorization
        String authHeader = request.getHeader("Authorization");
        
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // 去掉"Bearer "前缀
            
            // 验证token
            Integer userId = userService.validateToken(token);
            
            if (userId != null) {
                // 将用户ID设置到请求属性中，供后续使用
                request.setAttribute("userId", userId);
                return true;
            }
        }
        
        // 如果没有有效的token，返回401未授权
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"status\":401,\"message\":\"未授权访问\"}");
        return false;
    }
} 