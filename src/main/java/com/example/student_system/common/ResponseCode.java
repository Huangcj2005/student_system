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
    USER_PHOTO_UPDATE_SUCCESS(1013, "用户头像更新成功"),


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
    HOMEWORK_LIST_FETCH_SUCCESS(1200, "用户作业列表获取成功"),
    HOMEWORK_ASSIGN_SUCCESS(1201, "作业发布成功"),
    HOMEWORK_SUBMISSION_SUCCESS(1202,"作业提交成功"),
    HOMEWORK_REMARK_SUCCESS(1203, "作业评价成功"),
    HOMEWORK_SCORE_FETCH_SUCCESS(1204, "学生作业平均成绩获取成功"),
    HOMEWORK_DETAIL_FETCH_SUCCESS(1205, "作业详情获取成功"),
    UNREMARKED_HOMEWORK_FETCH_SUCCESS(1206,"教师获取未评阅作业成功"),

    EXAM_CREATE_SUCCESS(1300, "考试创建成功"),
    EXAM_LIST_FETCH_SUCCESS(1301, "考试列表查询成功"),
    PAPER_GENERATE_SUCCESS(1302, "试卷生成成功"),
    USER_EXAM_LIST_FETCH_SUCCESS(1303, "用户考试列表获取成功"),
    QUESTION_RECORD_INSERT_SUCCESS(1304, "用户答题保存成功"),
    QUESTION_RECORD_UPDATE_SUCCESS(1305, "用户答题更新成功"),
    EXAM_SUBMIT_SUCCESS(1306, "考试已提交"),
    PAPER_FETCH_SUCCESS(1307, "试卷获取成功"),
    EXAM_SCORE_FETCH_SUCCESS(1308, "考试成绩获取成功"),
    FINISHED_EXAM_FETCH_SUCCESS(1309, "已完成考试获取成功"),

    SCORE_CREATE_SUCCESS(1400, "课程成绩创建成功"),
    SCORE_FETCH_SUCCESS(1401, "课程成绩获取成功"),
    SCORE_FETCH_AND_UPDATE_SUCCESS(1402, "课程成绩获取并更新成功"),



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
    // --- 202x:用户隐私更新失败
    USER_PHOTO_UPDATE_ERROR(2020, "传入头像不能为空"),

    // --- 21xx:课程业务失败状态码
    NOTE_DELETE_FAIL(2100,"笔记删除失败"),
    NOTE_FETCH_FAIL(2101,"没有笔记"),
    NOTE_UPDATE_FAIL(2102,"笔记更新失败"),
    NOTE_ASSIGN_FAIL(2103,"笔记添加失败"),

    // --- 22xx:作业业务失败状态码
    HOMEWORK_EXISTS(2200,"作业已存在"),
    SUFFIX_NOT_ALLOWED(2201, "不支持的附件类型"),
    UPLOAD_FAILED(2202, "文件上传失败"),
    HOMEWORK_ASSIGN_FAIL(2203, "课程没有学生"),
    HOMEWORK_JSON_ERROR(2204, "Homework的JSON格式错误"),
    HOMEWORK_NOT_EXISTS(2205, "作业不存在"),

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
