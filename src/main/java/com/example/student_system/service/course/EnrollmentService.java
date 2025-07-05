package com.example.student_system.service.course;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.dto.course.EnrollmentDTO;
import com.example.student_system.domain.entity.account.User;
import com.example.student_system.domain.vo.course.CourseVo;
import com.example.student_system.domain.vo.user.UserVo;

import java.util.List;

public interface EnrollmentService
{
    public CommonResponse<List<User>> getUsersByCourseId(int course_id);
    public CommonResponse<List<CourseVo>> getCoursesByUserId(int user_id);
    public CommonResponse<String> addEnrollment(EnrollmentDTO dto);
    public CommonResponse<String> deleteEnrollmentById(int user_id, int course_id);
}