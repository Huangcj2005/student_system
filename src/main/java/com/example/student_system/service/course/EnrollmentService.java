package com.example.student_system.service.course;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.dto.course.EnrollmentDTO;
import com.example.student_system.domain.entity.account.User;
import com.example.student_system.domain.vo.course.CourseVo;


import java.util.List;

public interface EnrollmentService
{
    CommonResponse<List<User>> getUsersByCourseId(int course_id);
    CommonResponse<List<CourseVo>> getCoursesByUserId(int user_id);
    CommonResponse<String> addEnrollment(EnrollmentDTO dto);
    CommonResponse<String> deleteEnrollmentById(int user_id, int course_id);
    CommonResponse<List<CourseVo>> getUnPickedCourseByUserId(int user_id);



}