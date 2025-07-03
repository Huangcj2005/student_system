package com.example.student_system.domain.vo.task;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ScoreVO
{
    private int course_id;
    private String course_name;
    private BigDecimal video_score;
    private BigDecimal homework_score;
    private BigDecimal exam_score;
}
