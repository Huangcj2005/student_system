package com.example.student_system.controller;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.entity.account.User;
import com.example.student_system.domain.entity.course.Course;
import com.example.student_system.domain.entity.course.Enrollment;
import com.example.student_system.domain.vo.CourseVo;
import com.example.student_system.service.course.EnrollmentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController{
    @Resource
    private EnrollmentService enrollmentService;

    @PostMapping("/addEnrollment")
    public CommonResponse<Enrollment> addEnrollment(@RequestBody Enrollment newEnrollment)
    {
        return enrollmentService.addEnrollment(newEnrollment);
    }

    @GetMapping("/getUser/{course_id}")
    public CommonResponse<List<User>> getUsersByCourseId(@PathVariable int course_id)
    {
        return enrollmentService.getUsersByCourseId(course_id);
    }

    @GetMapping("/getCourse/{user_id}")
    public CommonResponse<List<CourseVo>> getCoursesByUserId(@PathVariable int user_id)
    {
        return enrollmentService.getCoursesByUserId(user_id);
    }

    @DeleteMapping("/deleteCourse/{user_id}/{course_id}")
    public CommonResponse<Enrollment> deleteCourseById(@PathVariable int user_id,@PathVariable int course_id)
    {
        return enrollmentService.deleteEnrollmentById(user_id,course_id);
    }

}
