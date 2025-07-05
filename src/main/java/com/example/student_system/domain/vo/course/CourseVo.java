package com.example.student_system.domain.vo.course;

import lombok.Data;

import java.util.Date;

@Data
public class CourseVo {
    private int course_id;
    private String course_name;
    private String course_img_url;
    private Date start_date;
    private String course_detail;
    private int teacher_id;
    private Date end_time;
}
