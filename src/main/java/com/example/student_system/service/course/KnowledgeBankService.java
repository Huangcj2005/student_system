package com.example.student_system.service.course;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.dto.course.KnowledgeBankDTO;
import com.example.student_system.domain.entity.course.KnowledgeBank;
import com.example.student_system.domain.vo.KnowledgeBankVo;

import java.util.List;

public interface KnowledgeBankService {
    CommonResponse<String> addKnowledgeBank(KnowledgeBankDTO dto);
    CommonResponse<List<KnowledgeBankVo>> getKnowledgeBankByCourseId(int course_id);

}
