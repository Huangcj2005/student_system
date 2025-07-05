package com.example.student_system.domain.dto.task;

import lombok.Data;

@Data
public class QuestionRecordDTO
{
    private int course_id;
    private int question_id;
    private String question_type;
    private String question_content;
    private String answer;
}
