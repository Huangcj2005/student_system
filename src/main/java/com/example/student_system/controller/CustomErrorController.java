package com.example.student_system.controller;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 全局错误控制器
 * 处理所有未匹配的URL请求，返回统一的404错误响应
 */
@RestController
public class CustomErrorController implements ErrorController {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);
    
    /**
     * 处理所有错误请求
     * 包括404、500等错误
     */
    @RequestMapping("/error")
    public ResponseEntity<CommonResponse<String>> handleError(HttpServletRequest request) {
        // 获取错误状态码
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String requestURI = (String) request.getAttribute("javax.servlet.error.request_uri");
        String method = request.getMethod();
        
        // 记录错误日志
        logger.warn("访问错误URL: {} {} - 状态码: {}", method, requestURI, statusCode);
        
        // 根据状态码返回不同的错误信息
        if (statusCode != null) {
            switch (statusCode) {
                case 404:
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(CommonResponse.createForError(
                                    ResponseCode.RESOURCE_NOT_FOUND.getCode(),
                                    "请求的资源不存在: " + requestURI
                            ));
                case 403:
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(CommonResponse.createForError(
                                    ResponseCode.TOKEN_INVALID.getCode(),
                                    "访问被拒绝，请检查权限"
                            ));
                case 500:
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(CommonResponse.createForError(
                                    ResponseCode.ERROR.getCode(),
                                    "服务器内部错误"
                            ));
                default:
                    return ResponseEntity.status(HttpStatus.valueOf(statusCode))
                            .body(CommonResponse.createForError(
                                    ResponseCode.ERROR.getCode(),
                                    "请求处理失败，状态码: " + statusCode
                            ));
            }
        }
        
        // 默认错误响应
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponse.createForError(
                        ResponseCode.ERROR.getCode(),
                        "未知错误"
                ));
    }
} 