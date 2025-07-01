package com.example.student_system.domain.entity.course;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("course")
public class Course {
    @TableId("id")
    private int id;
    private int course_id;
    private String course_name;
    private String course_detail;
    @TableField("course_img")
    private String course_img_url;
    private int teacher_id;
    private Date start_time;
    private Date end_time;
    private Date create_time;
    private Date update_time;
    private Date delete_time;
    private String unused;
}
