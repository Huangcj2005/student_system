package com.example.student_system.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记需要认证的接口
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresAuth {
    /**
     * 是否需要认证，默认为true
     */
    boolean value() default true;
    
    /**
     * 需要的角色，默认为空（任何角色都可以）
     */
    String[] roles() default {};
} 