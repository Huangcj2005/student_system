package com.example.student_system.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户上下文工具类
 */
// TODO：是否意味着那些 controller 中需要验证 token 的部分可以被替换成这里的 getUserId 方法？
public class UserContext {
    
    private static final String USER_ID_KEY = "userId";
    
    /**
     * 获取当前用户ID
     */
    public static Integer getCurrentUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            Object userId = request.getAttribute(USER_ID_KEY);
            if (userId instanceof Integer) {
                return (Integer) userId;
            }
        }
        return null;
    }
    
    /**
     * 设置当前用户ID
     */
    public static void setCurrentUserId(Integer userId) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            request.setAttribute(USER_ID_KEY, userId);
        }
    }
    
    /**
     * 清除当前用户信息
     */
    public static void clear() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            request.removeAttribute(USER_ID_KEY);
        }
    }
} 