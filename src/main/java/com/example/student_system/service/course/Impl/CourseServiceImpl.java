package com.example.student_system.service.course.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import com.example.student_system.domain.dto.course.CourseDTO;
import com.example.student_system.domain.entity.course.Course;
import com.example.student_system.domain.vo.CourseVo;
import com.example.student_system.mapper.course.CourseMapper;
import com.example.student_system.service.course.CourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
        if (!courseVoList.isEmpty())
        {
            return CommonResponse.createForSuccess(
                    ResponseCode.COURSE_LIST_FETCH_SUCCESS.getCode(),
                    ResponseCode.COURSE_LIST_FETCH_SUCCESS.getDescription(),
                    courseVoList
            );
        }
        else
        {
            return CommonResponse.createForSuccess(
                    ResponseCode.COURSE_LIST_FETCH_FAIL.getCode(),
                    ResponseCode.COURSE_LIST_FETCH_FAIL.getDescription(),
                    courseVoList
            );
        }

    }

    @Override
    public CommonResponse<String> addCourse(CourseDTO dto) {
        Course course=new Course();
        BeanUtils.copyProperties(dto, course);
        course.setCreate_time(new Date());
        course.setUpdate_time(new Date());

        courseMapper.insert(course);
        return CommonResponse.createForSuccess(
                ResponseCode.COURSE_ADD_SUCCESS.getCode(),
                ResponseCode.COURSE_ADD_SUCCESS.getDescription()
        );
    }

    @Override
    public CommonResponse<String> getCourseName(int course_id) {
        QueryWrapper<Course> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("course_id",course_id);
        Course course=courseMapper.selectOne(queryWrapper);
        String course_name=course.getCourse_name();

        return CommonResponse.createForSuccess(
                ResponseCode.COURSE_NAME_GET_SUCCESS.getCode(),
                ResponseCode.COURSE_NAME_GET_SUCCESS.getDescription(),
                course_name
        );
    }

}
