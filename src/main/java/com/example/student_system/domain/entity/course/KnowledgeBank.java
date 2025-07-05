package com.example.student_system.domain.entity.course;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("knowledge_bank")
public class KnowledgeBank {
    @TableId("id")
    private int id;
    private int course_id;
    private String course_name;
    private String chapter_id;
    private String chapter_name;
    private String chapter_detail;
    @TableField("video")
    private String course_video_url;
    @TableField("courseware")
    private String courseware_url;
    private Date create_time;
    private Date update_time;
    private Date delete_time;
    private String unused;
}
