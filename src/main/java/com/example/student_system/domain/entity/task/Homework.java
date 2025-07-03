package com.example.student_system.domain.entity.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("homework")
public class Homework {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private int user_id;
    private int course_id;
    private String course_name;
    private int teacher_id;
    @TableField("title")
    private String homework_title;
    @TableField("content")
    private String homework_content;
    @TableField("attachment")
    private String attachment_url;
    @TableField("submit_attachment")
    private String submit_url;
    private String submit_content;
    private BigDecimal score;
    private String remark;
    private Date start_time;
    private Date end_time;
    private String status;
    private Date submit_time;
    private Date create_time;
    private Date update_time;
    private Date delete_time;
    private String unused;

}