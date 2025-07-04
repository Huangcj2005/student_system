package com.example.student_system.controller.course;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.dto.course.CourseDTO;
import com.example.student_system.domain.vo.course.CourseVo;
import com.example.student_system.service.course.CourseService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {
    @Resource
    private CourseService courseService;

    @GetMapping("/getCourse")
    public CommonResponse<List<CourseVo>> getCourseList()
    {
        return courseService.getCourseList();
    }

    @PostMapping("/addCourse")
    public CommonResponse<String> addCourse(@RequestBody CourseDTO newCourseDTO){return courseService.addCourse(newCourseDTO);}

    @GetMapping("/getCourseName/{course_id}")
    public CommonResponse<String> getCourseName(@PathVariable int course_id)
    {
        return courseService.getCourseName(course_id);
    }

}
