package com.example.student_system.domain.entity.course;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("enrollment")
public class Enrollment
{
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private int user_id;
    private int course_id;
    private String course_name;
    @TableField("course_img")
    private String course_img_url;
    private Date start_date;
    private String status;
    private Date create_time;
    private Date update_time;
    private Date delete_time;
    private String unused;
}
