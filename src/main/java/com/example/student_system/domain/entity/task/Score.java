package com.example.student_system.domain.entity.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("score")
public class Score
{
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private int user_id;
    private int course_id;
    private String course_name;
    private BigDecimal video_score;
    private BigDecimal homework_score;
    private BigDecimal exam_score;
    private Date create_time;
    private Date update_time;
    private Date delete_time;
    private String unused;
}
