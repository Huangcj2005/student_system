package com.example.student_system.controller;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.entity.course.Course;
import com.example.student_system.domain.vo.CourseVo;
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
    public CommonResponse<Course> addCourse(@RequestBody Course newCourse){return courseService.addCourse(newCourse);}

}
