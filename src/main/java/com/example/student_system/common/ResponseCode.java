package com.example.student_system.common;

import lombok.Getter;

@Getter
public enum ResponseCode {
    // 成功码从 1000 开始
    SUCCESS(1,"成功"),

    // --- 10xx:用户业务成功状态码
    USER_REGISTER_SUCCESS(1000,"用户注册成功"),


    // --- 11xx:课程业务成功状态码
    CATEGORY_LIST_FETCH_SUCCESS(1100,"获取分类列表成功"),

    // --- 12xx:作业业务成功状态码
    HOMEWORK_LIST_FETCH_SUCCESS(1200, "用户作业列表获取成功"),
    HOMEWORK_ASSIGN_SUCCESS(1201, "作业发布成功"),
    HOMEWORK_SUBMISSION_SUCCESS(1202,"作业提交成功"),
    HOMEWORK_REMARK_SUCCESS(1203, "作业评价成功"),
    HOMEWORK_SCORE_FETCH_SUCCESS(1204, "学生作业平均成绩获取成功"),
    HOMEWORK_DETAIL_FETCH_SUCCESS(1205, "作业详情获取成功"),

    EXAM_CREATE_SUCCESS(1300, "考试创建成功"),
    EXAM_LIST_FETCH_SUCCESS(1301, "考试列表查询成功"),
    PAPER_GENERATE_SUCCESS(1302, "试卷生成成功"),
    USER_EXAM_LIST_FETCH_SUCCESS(1303, "用户考试列表获取成功"),


    // 错误码从 2000 开始，命名格式与成功码相同
    ERROR(2,"错误"),

    // --- 20xx:用户业务失败状态码
    TOKEN_MISSING(2000,"缺少令牌"),
    TOKEN_INVALID(2001,"无效的令牌"),
    TOKEN_EXPIRED(2002,"令牌已过期"),

    // --- 21xx:课程业务失败状态码


    // --- 22xx:作业业务失败状态码
    HOMEWORK_EXISTS(2200,"作业已存在"),
    SUFFIX_NOT_ALLOWED(2201, "不支持的附件类型"),
    UPLOAD_FAILED(2202, "文件上传失败"),

    PAPER_GENERATE_FAILED(2303, "试卷生成失败")


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
