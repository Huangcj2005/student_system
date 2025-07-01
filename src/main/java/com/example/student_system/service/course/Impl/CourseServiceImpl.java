package com.example.student_system.service.course.Impl;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import com.example.student_system.domain.entity.course.Course;
import com.example.student_system.domain.vo.CourseVo;
import com.example.student_system.mapper.CourseMapper;
import com.example.student_system.service.course.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("CourseService")
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;
    @Override
    public CommonResponse<List<CourseVo>> getCourseList() {
        List<Course> courseList=courseMapper.selectList(null);
        List<CourseVo> courseVoList=courseList.stream().map(
                course -> {
                    CourseVo vo=new CourseVo();
                    vo.setCourse_id(course.getCourse_id());
                    vo.setCourse_name(course.getCourse_name());
                    vo.setCourse_detail(course.getCourse_detail());
                    vo.setCourse_img_url(course.getCourse_img_url());
                    vo.setStart_date(course.getStart_time());
                    vo.setEnd_time(course.getEnd_time());
                    vo.setTeacher_id(course.getTeacher_id());
                    return vo;
                }
        ).collect(Collectors.toList());
        return CommonResponse.createForSuccess(
                ResponseCode.COURSE_LIST_FETCH_SUCCESS.getCode(),
                ResponseCode.COURSE_LIST_FETCH_SUCCESS.getDescription(),
                courseVoList
        );
    }

    @Override
    public CommonResponse<Course> addCourse(Course course) {
        courseMapper.insert(course);
        return CommonResponse.createForSuccess(
                ResponseCode.COURSE_ADD_SUCCESS.getCode(),
                ResponseCode.COURSE_ADD_SUCCESS.getDescription()
        );
    }

}
