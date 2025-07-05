package com.example.student_system.domain.vo.task;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExamScoreVO
{
    private String exam_name;
    private int exam_id;
    BigDecimal score;
    private Date start_time;
    private Date end_time;
}
