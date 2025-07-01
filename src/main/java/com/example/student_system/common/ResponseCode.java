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
    NOTE_ASSIGN_SUCCESS(1101,"笔记创建成功"),
    NOTE_LIST_FETCH_SUCCESS(1102,"笔记列表获取成功"),
    NOTE_FETCH_SUCCESS(1103,"笔记获取成功"),
    NOTE_DELETE_SUCCESS(1104,"笔记删除成功"),
    NOTE_UPDATE_SUCCESS(1105,"笔记更新成功"),
    COURSE_LIST_FETCH_SUCCESS(1106,"获取课程列表成功"),
    COURSE_ADD_SUCCESS(1107,"课程添加成功"),
    USER_FETCH_SUCCESS(1108,"选课用户查询成功"),
    ENROLLMENT_FETCH_SUCCESS(1109,"选择课程查询成功"),
    ENROLLMENT_ADD_SUCCESS(1110,"选课成功"),
    ENROLLMENT_DELETE_SUCCESS(1101,"退课成功"),
    // --- 12xx:作业业务成功状态码



    // 错误码从 2000 开始，命名格式与成功码相同
    ERROR(2,"错误"),

    // --- 20xx:用户业务失败状态码
    TOKEN_MISSING(2000,"缺少令牌"),
    TOKEN_INVALID(2001,"无效的令牌"),
    TOKEN_EXPIRED(2002,"令牌已过期"),

    // --- 21xx:课程业务失败状态码
    NOTE_DELETE_FAIL(2100,"笔记删除失败"),
    NOTE_FETCH_FAIL(2101,"没有笔记"),
    NOTE_UPDATE_FAIL(2102,"笔记更新失败"),
    NOTE_ASSIGN_FAIL(2103,"笔记添加失败"),

    // --- 22xx:作业业务失败状态码
    HOMEWORK_LIST_FETCH_SUCCESS(2200, "用户作业列表获取成功"),
    HOMEWORK_ASSIGN_SUCCESS(2201, "作业发布成功"),


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
