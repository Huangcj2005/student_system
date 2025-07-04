package com.example.student_system.domain.vo.course;

import lombok.Data;

@Data
public class KnowledgeBankVo {
    private int course_id;
    private String course_name;
    private String chapter_id;
    private String chapter_name;
    private String chapter_detail;
    private String course_video_url;
    private String courseware_url;
}
