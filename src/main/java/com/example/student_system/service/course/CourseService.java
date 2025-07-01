package com.example.student_system.service.course;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.entity.course.Course;
import com.example.student_system.domain.vo.CourseVo;

import java.util.List;

public interface CourseService {
    public CommonResponse<List<CourseVo>> getCourseList();
    public CommonResponse<Course> addCourse(Course course);

}
