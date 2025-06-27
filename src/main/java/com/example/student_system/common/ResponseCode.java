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
    VALIDATECODE_INVALID(1004,"验证码无效"),
    VALIDATECODE_ERROR(1005,"验证码错误"),


    // --- 11xx:课程业务成功状态码
    CATEGORY_LIST_FETCH_SUCCESS(1100,"获取分类列表成功"),

    // --- 12xx:作业业务成功状态码



    // 错误码从 2000 开始，命名格式与成功码相同
    ERROR(2,"错误"),

    // --- 20xx:用户业务失败状态码
    TOKEN_MISSING(2000,"缺少令牌"),
    TOKEN_INVALID(2001,"无效的令牌"),
    TOKEN_EXPIRED(2002,"令牌已过期"),
    ACCOUNT_INVALID(2003,"账号错误或不存在"),
    PASSWORD_ERROR(2004,"密码错误"),

    // --- 201x:注册失败
    EMAIL_ALREADY_USED(2010,"邮箱已被使用"),
    USERNAME_ALREADY_USED(2011,"用户名已被使用"),

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
