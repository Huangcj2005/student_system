package com.example.student_system.domain.dto.task;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class HomeworkDTO
{
    private int course_id;
    private int teacher_id;
    private int user_id;
    private String homework_title;
    private String homework_content;
    private String remark;
    private BigDecimal score;
    private Date start_time;
    private Date end_time;
}
