package com.example.student_system.service.course.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import com.example.student_system.domain.entity.account.User;
import com.example.student_system.domain.entity.course.Course;
import com.example.student_system.domain.entity.course.Enrollment;
import com.example.student_system.domain.vo.CourseVo;
import com.example.student_system.mapper.CourseMapper;
import com.example.student_system.mapper.EnrollmentMapper;
import com.example.student_system.mapper.account.UserMapper;
import com.example.student_system.service.course.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("EnrollmentService")
public class EnrollmentServiceImpl implements EnrollmentService
{
    @Autowired
    private EnrollmentMapper enrollmentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CourseMapper courseMapper;

    @Override
    public CommonResponse<List<User>> getUsersByCourseId(int course_id)
    {
        QueryWrapper<Enrollment> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("course_id",course_id);
        List<Enrollment> enrollmentList=enrollmentMapper.selectList(queryWrapper);

        List<Integer> userIdList = new ArrayList<>();

        for(Enrollment enrollment : enrollmentList)
        {
            userIdList.add(enrollment.getUserId());
        }

        List<User> userList = new ArrayList<>();
        for(Integer userId : userIdList){
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("user_id",userId);
            userList.add(userMapper.selectOne(userQueryWrapper));
        }

        return CommonResponse.createForSuccess(
                ResponseCode.USER_FETCH_SUCCESS.getCode(),
                ResponseCode.USER_FETCH_SUCCESS.getDescription(),
                userList
        );
    }

    @Override
    public CommonResponse<List<CourseVo>> getCoursesByUserId(int user_id) {
        QueryWrapper<Enrollment> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",user_id);
        List<Enrollment> enrollmentList=enrollmentMapper.selectList(queryWrapper);
        List<Integer> courseIdList=enrollmentList.stream()
                .map(Enrollment::getCourse_id)
                .collect(Collectors.toList());

        List<Course> courseList=courseMapper.selectBatchIds(courseIdList);
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
                ResponseCode.ENROLLMENT_FETCH_SUCCESS.getCode(),
                ResponseCode.ENROLLMENT_FETCH_SUCCESS.getDescription(),
                courseVoList
        );
    }

    @Override
    public CommonResponse<Enrollment> addEnrollment(Enrollment newEnrollment) {
        enrollmentMapper.insert(newEnrollment);
        return CommonResponse.createForSuccess(
                ResponseCode.ENROLLMENT_ADD_SUCCESS.getCode(),
                ResponseCode.ENROLLMENT_ADD_SUCCESS.getDescription()
        );
    }

    @Override
    public CommonResponse<Enrollment> deleteEnrollmentById(int user_id, int course_id) {
        QueryWrapper<Enrollment> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",user_id)
                .eq("course_id",course_id);
        enrollmentMapper.delete(queryWrapper);

        return CommonResponse.createForSuccess(
                ResponseCode.ENROLLMENT_DELETE_SUCCESS.getCode(),
                ResponseCode.ENROLLMENT_DELETE_SUCCESS.getDescription()
        );
    }
}