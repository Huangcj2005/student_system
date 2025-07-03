package com.example.student_system.domain.dto.course;

import lombok.Data;

@Data
public class DiscussionDTO {
    private int user_id;
    private String discussion_id;
    private int course_id;
    private String course_name;
    private String chapter_name;
    private String content;
    private int reply_id;
    private String reply_name;
    private String reply_content;
}
