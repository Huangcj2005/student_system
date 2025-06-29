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
    public boolean isHomeworkExists(String homework_title, int course_id, int user_id);
    public CommonResponse<Homework> assignHomework(Homework newHomework);
    public CommonResponse<List<Homework>> getHomeworkByUserid(int user_id);
    public CommonResponse<List<Homework>> getHomeworkByUserid(int user_id, int course_id);
    public CommonResponse<HomeworkVO> getHomeworkDetail(int course_id, String title);
    public CommonResponse<Homework> submitHomework(Homework homework);
    public CommonResponse<Homework> remarkHomework(Homework homework);
    public CommonResponse<BigDecimal> getHomeworkScore(int user_id, int course_id);



}