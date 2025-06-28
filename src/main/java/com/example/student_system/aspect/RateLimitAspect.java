package com.example.student_system.aspect;

import com.example.student_system.annotation.RateLimit;
import com.example.student_system.exception.RateLimitException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class RateLimitAspect {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint point, RateLimit rateLimit) throws Throwable {
        String key = generateKey(point, rateLimit);
        if (!canProceed(key, rateLimit)) {
            throw new RateLimitException("请求过于频繁，请稍后再试...");
        }

        try {
            return point.proceed();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 生成限流键
     */
    private String generateKey(ProceedingJoinPoint point, RateLimit rateLimit) {
        // 如果注解中指定了key，优先使用
        if (StringUtils.hasText(rateLimit.key())) {
            return "rate_limit:" + rateLimit.key();
        }
        
        // 否则使用邮箱参数作为key（针对发送验证码场景）
        Object[] args = point.getArgs();
        if (args.length > 0 && args[0] instanceof String) {
            String email = (String) args[0];
            return "rate_limit:email:" + email;
        }
        
        // 降级方案：使用方法名
        String methodName = point.getSignature().getName();
        return "rate_limit:method:" + methodName;
    }

    /**
     * 检查是否可以执行
     */
    private boolean canProceed(String key, RateLimit rateLimit) {
        String value = redisTemplate.opsForValue().get(key);
        
        if (value == null) {
            // 如果key不存在，说明可以执行，设置key并设置过期时间
            Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, "1", rateLimit.timeUnit(), TimeUnit.MILLISECONDS);
            return success != null && success;
        }
        
        // 如果key存在，说明在限流时间内，不允许执行
        return false;
    }
}
