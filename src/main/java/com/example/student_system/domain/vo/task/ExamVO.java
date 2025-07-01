package com.example.student_system.domain.vo.task;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ExamVO
{
    private int exam_id;
    private String exam_name;
    private Date start_time;
    private Date end_time;

}
