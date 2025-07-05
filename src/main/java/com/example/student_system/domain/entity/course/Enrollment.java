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
    @TableField("user_id")
    private int userId;

    private int course_id;

    @TableField("course_name")
    private String course_name;

    @TableField("course_img")
    private String course_img_url;

    @TableField("start_date")
    private Date start_date;

    @TableField("status")
    private String status;

    @TableField("create_time")
    private Date create_time;

    @TableField("update_time")
    private Date update_time;

    @TableField("delete_time")
    private Date delete_time;
    private String unused;
}
