package com.example.student_system.domain.dto.course;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class EnrollmentDTO {
    private int user_id;
    private int course_id;
    private String course_name;
    private String course_img_url;
    private Date start_date;
    private String status;
}
