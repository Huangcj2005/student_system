package com.example.student_system.domain.entity.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("exam_score")
public class ExamScore
{
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private int exam_id;
    private int user_id;
    private BigDecimal score;
    private Date create_time;
    private Date update_time;
    private Date delete_time;
    private String unused;
}
