package com.example.student_system.domain.dto.course;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LearnRecordInsertDTO {
    private int user_id;
    private int course_id;
    private String course_name;
    private String chapter_id;
    private String chapter_name;
    private BigDecimal progress;
    private BigDecimal study_time;
}
