package com.example.student_system.domain.dto.note;

import lombok.Data;

@Data
public class NoteInsertDTO {
    private int user_id;
    private int course_id;
    private String course_name;
    private String chapter_id;
    private String chapter_name;
    private String note_content;
}
