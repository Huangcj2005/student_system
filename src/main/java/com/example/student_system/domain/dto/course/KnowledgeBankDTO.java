package com.example.student_system.domain.dto.course;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class KnowledgeBankDTO {
    private int course_id;
    private String course_name;
    private String chapter_id;
    private String chapter_name;
    private String chapter_detail;
    private String course_video_url;
    private String courseware_url;
}
