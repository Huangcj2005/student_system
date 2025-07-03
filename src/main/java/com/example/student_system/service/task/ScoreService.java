package com.example.student_system.service.task;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.vo.task.ScoreVO;

import java.math.BigDecimal;

public interface ScoreService
{
    CommonResponse<String> insertScoreRecord(int user_id, int course_id);
    CommonResponse<ScoreVO> getUserScore(int user_id, int course_id);
    CommonResponse<String> deleteScore(int user_id, int course_id);
    CommonResponse<String> updateVideoScore(int user_id, int course_id, BigDecimal score);
    CommonResponse<String> updateHomeworkScore(int user_id, int course_id, BigDecimal score);
    CommonResponse<String> updateExamScore(int user_id, int course_id, BigDecimal score);
    CommonResponse<ScoreVO> updateAndGetScore(int user_id, int course_id);
}
