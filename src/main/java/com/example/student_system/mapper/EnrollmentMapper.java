package com.example.student_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.student_system.domain.entity.course.Enrollment;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentMapper extends BaseMapper<Enrollment> {
}
