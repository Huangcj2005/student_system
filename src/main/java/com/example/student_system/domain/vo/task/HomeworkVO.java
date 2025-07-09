package com.example.student_system.domain.vo.task;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class HomeworkVO
{
    private int course_id;
    private String homework_title;
    private String homework_content;
    private String attachment_url;
    private String submit_url;
    private String submit_content;
    private String status;
    private BigDecimal score;
    private String remark;
    private Date submit_time;
    private Date start_time;
    private Date end_time;
}
