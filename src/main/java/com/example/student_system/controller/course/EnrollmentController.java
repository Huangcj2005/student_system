package com.example.student_system.controller.course;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import com.example.student_system.domain.dto.course.EnrollmentDTO;
import com.example.student_system.domain.entity.account.User;
import com.example.student_system.domain.vo.course.CourseVo;
import com.example.student_system.domain.vo.user.UserVo;
import com.example.student_system.service.course.EnrollmentService;
import com.example.student_system.util.UserContext;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public CommonResponse<List<UserVo>> getUsersByCourseId(@PathVariable int course_id)
    {

       List<User> userList=enrollmentService.getUsersByCourseId(course_id).getData();
       List<UserVo> voList = userList.stream().map(user -> {
            UserVo vo = new UserVo();
            BeanUtils.copyProperties(user, vo);
            return vo;
        }).collect(Collectors.toList());
       return CommonResponse.createForSuccess(
               ResponseCode.USER_FETCH_SUCCESS.getCode(),
               ResponseCode.USER_FETCH_SUCCESS.getDescription(),
               voList
       );
    }

    @GetMapping("/getCourse")
    public CommonResponse<List<CourseVo>> getCoursesByUserId()
    {
        Integer user_id= UserContext.getCurrentUserId();
        return enrollmentService.getCoursesByUserId(user_id);
    }

    @DeleteMapping("/course/{course_id}")
    public CommonResponse<String> deleteCourseById(@PathVariable int course_id)
    {
        Integer user_id= UserContext.getCurrentUserId();
        return enrollmentService.deleteEnrollmentById(user_id,course_id);
    }

}
