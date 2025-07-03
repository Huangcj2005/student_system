package com.example.student_system.domain.vo.task;

import lombok.Data;

import java.util.List;

@Data
public class PaperVO
{
    private int exam_id;
    private String exam_title;
    private List<QuestionVO> questionVOList;
}
