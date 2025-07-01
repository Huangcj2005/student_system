package com.example.student_system.common;

import lombok.Getter;

@Getter
public enum ResponseCode {
    // 成功码从 1000 开始
    SUCCESS(1,"成功"),

    // --- 10xx:用户业务成功状态码
    USER_REGISTER_SUCCESS(1000,"用户注册成功"),
    USER_LOGIN_SUCCESS(1001,"用户登录成功"),
    USER_INFO_GET_SUCCESS(1002,"用户信息获取成功"),
    EMAIL_VALIDATECODE_SEND_SUCCESS(1003,"邮件验证码发送成功"),
    USER_PRIVACY_FETCH_SUCCESS(1006,"用户信息获取成功"),

    // --- 101x:用户信息更新业务成功状态码
    USER_INFO_UPDATE_SUCCESS(1010,"用户信息更新成功"),
    USER_PASSWORD_UPDATE_SUCCESS(1011,"用户密码更新成功"),
    USER_PRIVACY_UPDATE_SUCCESS(1012,"用户隐私更新成功"),


    // --- 11xx:课程业务成功状态码
    CATEGORY_LIST_FETCH_SUCCESS(1100,"获取分类列表成功"),

    // --- 12xx:作业业务成功状态码



    // 错误码从 2000 开始，命名格式与成功码相同
    ERROR(2,"错误"),
    PARAMETER_VALIDATION_ERROR(3,"参数验证失败"),

    // --- 20xx:用户业务失败状态码
    TOKEN_MISSING(2000,"缺少令牌"),
    TOKEN_INVALID(2001,"令牌无效或已过期"),
    ACCOUNT_INVALID(2003,"账号错误或不存在"),
    PASSWORD_ERROR(2004,"密码错误"),
    VALIDATECODE_INVALID(2005,"验证码无效"),
    VALIDATECODE_ERROR(2006,"验证码错误"),

    // --- 201x:注册失败
    EMAIL_ALREADY_USED(2010,"邮箱已被使用"),
    USERNAME_ALREADY_USED(2011,"用户名已被使用"),
    RATE_LIMIT_EXCEEDED(2012,"请求过于频繁，请稍后再试"),
    RESOURCE_NOT_FOUND(2013,"请求的资源不存在"),

    // --- 21xx:课程业务失败状态码


    // --- 22xx:作业业务失败状态码


    ;
    private final int code;
    private final String description;

    ResponseCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
