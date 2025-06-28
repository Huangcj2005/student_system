package com.example.student_system.exception;

/**
 * 限流异常
 * 当请求频率超过限制时抛出此异常
 */
public class RateLimitException extends RuntimeException {
    public RateLimitException(String message) {
        super(message);
    }
    public RateLimitException(String message, Throwable cause) {
        super(message, cause);
    }
} 