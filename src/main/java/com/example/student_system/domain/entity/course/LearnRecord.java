package com.example.student_system.domain.entity.course;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("learn_record")
public class LearnRecord {
    @TableId("id")
    private int id;
    private int user_id;
    private int course_id;
    private String course_name;
    private String chapter_id;
    private String chapter_name;
    private BigDecimal progress;
    private BigDecimal study_time;
    private Date create_time;
    private Date update_time;
    private Date delete_time;
    private String unused;
}
