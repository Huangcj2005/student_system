package com.example.student_system.service.task;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.dto.task.HomeworkDTO;
import com.example.student_system.domain.entity.task.Homework;
import com.example.student_system.domain.vo.task.HomeworkVO;

import java.math.BigDecimal;
import java.util.BitSet;
import java.util.List;


public interface HomeworkService
{
    boolean isHomeworkExists(String homework_title, int course_id, int user_id);
    CommonResponse<Homework> assignHomework(Homework newHomework);
    CommonResponse<List<Homework>> getHomeworkByUserid(int user_id);
    CommonResponse<List<Homework>> getHomeworkByUserid(int user_id, int course_id);
    CommonResponse<HomeworkVO> getHomeworkDetail(int user_id, int course_id, String title);
    CommonResponse<Homework> submitHomework(Homework homework);
    CommonResponse<Homework> remarkHomework(Homework homework);
    CommonResponse<List<Homework>> getUnremarkedHomework(int course_id);
    CommonResponse<Homework> syncHomework(int user_id, int course_id);
    BigDecimal getHomeworkScore(int user_id, int course_id);

}