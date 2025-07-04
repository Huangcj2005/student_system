package com.example.student_system.domain.vo.course;

import lombok.Data;

import java.util.Date;

@Data
public class DiscussionVo {
//    private int user_id;
    private String user_name;
    private String discussion_id;
    private int course_id;
    private String chapter_id;
    private String course_name;
    private String chapter_name;
    private String content;
//    private int reply_id;
    private String reply_name;
    private String reply_content;
    private Date create_time;
}
