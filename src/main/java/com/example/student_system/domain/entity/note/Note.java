package com.example.student_system.domain.entity.note;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("note")
public class Note {
    @TableId("id")
    private int id;
    private int note_id;
    private int user_id;
    private int course_id;
    private String course_name;
    private String chapter_id;
    private String chapter_name;
    @TableField("content")
    private String note_content;
    private Date create_time;
    private Date update_time;
    private Date delete_time;
    private String unused;
}
