package com.example.student_system.domain.dto.task;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class HomeworkDTO
{
    private Integer user_id;
    private int course_id;
    private String homework_title;
    private String homework_content;
    private String submit_content;
    private Date submit_time;
    private String remark;
    private BigDecimal score;
    private Date start_time;
    private Date end_time;
}
