package com.example.student_system.domain.dto.course;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class CourseDTO {
    private int course_id;
    private String course_name;
    private String course_detail;
    private String course_img_url;
    private int teacher_id;
    private Date start_time;
    private Date end_time;
}
