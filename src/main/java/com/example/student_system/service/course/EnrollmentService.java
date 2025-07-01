package com.example.student_system.service.course;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.entity.account.User;
import com.example.student_system.domain.entity.course.Course;
import com.example.student_system.domain.entity.course.Enrollment;
import com.example.student_system.domain.vo.CourseVo;

import java.util.List;

public interface EnrollmentService
{
    public CommonResponse<List<User>> getUsersByCourseId(int course_id);
    public CommonResponse<List<CourseVo>> getCoursesByUserId(int user_id);
    public CommonResponse<Enrollment> addEnrollment(Enrollment enrollment);
    public CommonResponse<Enrollment> deleteEnrollmentById(int user_id, int course_id);
}