package com.example.student_system.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogAction {
    /**
     * 操作类型描述
     * @return 返回操作类型字符串（示例："用户登录"、"修改密码" 等）
     */
    String value() default "";
}
