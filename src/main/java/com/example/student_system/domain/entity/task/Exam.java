package com.example.student_system.domain.entity.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("exam")
public class Exam
{
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private int course_id;
    private String course_name;
    private int teacher_id;
    private int exam_id;
    private String exam_name;
    private Date start_time;
    private Date end_time;
    private String status;
    private Date create_time;
    private Date update_time;
    private Date delete_time;
    private String unused;
}
