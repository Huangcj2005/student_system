package com.example.student_system.domain.vo.task;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class HomeworkVO
{
    private String homework_title;
    private String homework_content;
    private String attachment_url;
    private String submit_url;
    private String submit_content;
    private BigDecimal score;
    private String remark;
    private Date start_time;
    private Date end_time;
}
