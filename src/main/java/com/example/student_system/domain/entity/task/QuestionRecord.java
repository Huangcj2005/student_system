package com.example.student_system.domain.entity.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("question_record")
public class QuestionRecord
{
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private int user_id;
    private int exam_id;
    private int question_id;
    private String question_type;
    private String question_content;
    private String answer;
    private String right_answer;
    private Date create_time;
    private Date update_time;
    private Date delete_time;
    private String unused;
}
