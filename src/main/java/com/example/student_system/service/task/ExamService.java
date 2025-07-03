package com.example.student_system.service.task;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.entity.task.Exam;
import com.example.student_system.domain.entity.task.QuestionRecord;
import com.example.student_system.domain.vo.task.ExamVO;
import com.example.student_system.domain.vo.task.PaperVO;
import com.example.student_system.domain.vo.task.QuestionVO;

import java.math.BigDecimal;
import java.util.List;

public interface ExamService
{
    CommonResponse<List<QuestionVO>> generatePaper(int exam_id, int choice_num, int judge_num, int text_num, String tags, boolean isInsert);
    CommonResponse<Exam> createExam(Exam exam);
    CommonResponse<List<Exam>> getExamList(int course_id);
    CommonResponse<List<ExamVO>> getUserExamList(List<Integer> course_id_list);
    CommonResponse<QuestionRecord> saveOrUpdateQuestionRecord(QuestionRecord questionRecord);
    CommonResponse<BigDecimal> submitExam(int user_id, int exam_id);
    CommonResponse<PaperVO> getPaper(int exam_id);
    BigDecimal getExamScoreByUserId(int exam_id, int user_id, BigDecimal choice_score, BigDecimal judge_score, BigDecimal text_score);
    BigDecimal getAverageExamScore(int user_id, int course_id);
}
