package com.example.student_system.domain.entity.course;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("discussion")
public class Discussion {
    @TableId("id")
    private int id;
    private String discussion_id;
    private int user_id;
    private int course_id;
    private String course_name;
    private String chapter_name;
    private String content;
    private int reply_id;
    private String reply_name;
    private String reply_content;
    private String reply_time;
    private Date create_time;
    private Date update_time;
    private Date delete_time;
    private String unused;
}
