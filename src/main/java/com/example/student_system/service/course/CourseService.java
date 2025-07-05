package com.example.student_system.service.course;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.dto.course.CourseDTO;
import com.example.student_system.domain.vo.course.CourseVo;

import java.util.List;

public interface CourseService {
    CommonResponse<List<CourseVo>> getCourseList();
    CommonResponse<String> addCourse(CourseDTO dto);
    CommonResponse<String> getCourseName(int course_id);
    CommonResponse<List<CourseVo>> getCourseListByKeyword(String keyword);

}
