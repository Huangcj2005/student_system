package com.example.student_system.domain.dto.task;

import lombok.Data;

import java.util.Date;

@Data
public class ExamDTO
{
    private int course_id;
    private String course_name;
    private int exam_id;
    private String exam_name;
    private Date start_time;
    private Date end_time;
}
