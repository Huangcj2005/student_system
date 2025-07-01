package com.example.student_system.filter;

import com.example.student_system.service.account.UserService;
import com.example.student_system.service.mail.CodeService;
import com.example.student_system.util.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtFilter implements HandlerInterceptor {

    @Autowired
    private CodeService codeService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        String requestURI = request.getRequestURI();
        System.out.println("JwtFilter 拦截请求: " + requestURI);
        
        // 检查是否是错误页面请求，如果是则直接放行
        if ("/error".equals(requestURI)) {
            return true;
        }
        
        // 获取请求头中的Authorization
        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authHeader);
        
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // 去掉"Bearer "前缀
            
            // 验证token
            Integer userId = codeService.validateToken(token);
            System.out.println("Token 验证结果 - userId: " + userId);
            
            if (userId != null) {
                // 使用UserContext设置用户ID，确保数据存储的一致性
                UserContext.setCurrentUserId(userId);
                return true;
            }
        }
        
        // 如果没有有效的token，返回401未授权
        System.out.println("认证失败，返回401错误");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"status\":401,\"message\":\"未授权访问\"}");
        return false;
    }
} 