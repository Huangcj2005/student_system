package com.example.student_system.domain.entity.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("question_bank")
public class QuestionBank
{
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private int bank_id;
    private String tag;
    private String question_type;
    @TableField(value = "content")
    private String question_content;
    @TableField(value = "answer")
    private String question_answer;
    private Date create_time;
    private Date update_time;
    private Date delete_time;
    private String unused;
}
