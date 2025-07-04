package com.example.student_system.domain.vo.note;

import lombok.Data;

import java.util.Date;

@Data
public class NoteVo {
    private String note_id;
    private int user_id;
    private int course_id;
    private String course_name;
    private String chapter_id;
    private String chapter_name;
    private String note_content;
    private Date create_time;
    private Date update_time;
}
