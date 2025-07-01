package com.example.student_system.domain.vo;

import lombok.Data;

@Data
public class NoteVo {
    private int course_id;
    private String course_name;
    private String chapter_id;
    private String chapter_name;
    private String note_content;
}
