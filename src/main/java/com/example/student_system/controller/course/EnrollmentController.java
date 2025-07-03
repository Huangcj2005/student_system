package com.example.student_system.controller.course;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.dto.course.EnrollmentDTO;
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
    public CommonResponse<String> addEnrollment(@RequestBody EnrollmentDTO newEnrollmentDTO)
    {
        return enrollmentService.addEnrollment(newEnrollmentDTO);
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

    @DeleteMapping("/user/{user_id}/course/{course_id}")
    public CommonResponse<String> deleteCourseById(@PathVariable int user_id,@PathVariable int course_id)
    {
        return enrollmentService.deleteEnrollmentById(user_id,course_id);
    }

}
