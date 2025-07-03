package com.example.student_system.exception;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理404错误 - 访问不存在的URL地址
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResponse<String> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        
        logger.warn("访问不存在的URL地址: {} {}", method, requestURI);
        
        return CommonResponse.createForError(
                ResponseCode.RESOURCE_NOT_FOUND.getCode(),
                "请求的资源不存在: " + requestURI
        );
    }

    /**
     * 处理限流异常
     */
    @ExceptionHandler(RateLimitException.class)
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<String> handleRateLimitException(RateLimitException e) {
        logger.warn("限流异常: {}", e.getMessage());
        return CommonResponse.createForError(
            ResponseCode.RATE_LIMIT_EXCEEDED.getCode(),
            ResponseCode.RATE_LIMIT_EXCEEDED.getDescription()
        );
    }

    /**
     * 处理认证异常
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResponse<String> handleAuthenticationException(AuthenticationException e) {
        logger.warn("认证异常: {}", e.getMessage());
        return CommonResponse.createForError(
                ResponseCode.TOKEN_INVALID.getCode(),
                e.getMessage());
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<String> handleValidationException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String errorMessage = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        
        logger.warn("参数验证失败: {}", errorMessage);
        return CommonResponse.createForError(
                ResponseCode.PARAMETER_VALIDATION_ERROR.getCode(),
                errorMessage);
    }

    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse<String> handleBindException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String errorMessage = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        
        logger.warn("参数绑定失败: {}", errorMessage);
        return CommonResponse.createForError(
                ResponseCode.PARAMETER_VALIDATION_ERROR.getCode(),
                errorMessage);
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse<String> handleRuntimeException(RuntimeException e) {
        logger.error("运行时异常: ", e);
        return CommonResponse.createForError(ResponseCode.ERROR.getCode(), e.getMessage());
    }

    /**
     * 处理通用异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse<String> handleException(Exception e) {
        logger.error("系统异常: ", e);
        return CommonResponse.createForError(ResponseCode.ERROR.getCode(), "系统内部错误");
    }
}
