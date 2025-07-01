package com.example.student_system.service.task;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.entity.task.Exam;
import com.example.student_system.domain.entity.task.QuestionRecord;
import com.example.student_system.domain.vo.task.ExamVO;
import com.example.student_system.domain.vo.task.QuestionVO;

import java.util.List;

public interface ExamService
{
    public CommonResponse<List<QuestionVO>> generatePaper(int exam_id, int choice_num, int judge_num, int text_num, String tags, boolean isInsert);
    public CommonResponse<Exam> createExam(Exam exam);
    public CommonResponse<List<Exam>> getExamList(int course_id);
    public CommonResponse<List<Exam>> getUserExamList(int[] course_id_list);
    public CommonResponse<QuestionRecord> saveOrUpdateQuestionRecord(QuestionRecord questionRecord);

}
