package com.example.student_system.service.course;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.dto.course.LearnRecordInsertDTO;
import com.example.student_system.domain.dto.course.LearnRecordUpdateDTO;
import com.example.student_system.domain.entity.course.LearnRecord;
import com.example.student_system.domain.vo.LearnRecordVo;

import java.util.List;

public interface LearnRecordService {
    CommonResponse<String> insertLearnRecord(LearnRecordInsertDTO dto);
    CommonResponse<String> updateLearnRecord(int course_id,String chapter_id, LearnRecordUpdateDTO dto);
    CommonResponse<LearnRecordVo> getLearnRecord(int user_id,int course_id,String chapter_id);
//    CommonResponse<Integer> getLearnRecordCount(int course_id);

}
