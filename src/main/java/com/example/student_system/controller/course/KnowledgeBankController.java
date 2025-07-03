package com.example.student_system.controller.course;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.dto.course.KnowledgeBankDTO;
import com.example.student_system.domain.entity.course.KnowledgeBank;
import com.example.student_system.domain.vo.KnowledgeBankVo;
import com.example.student_system.service.course.KnowledgeBankService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/knowledge_bank")
public class KnowledgeBankController {
    @Resource
    private KnowledgeBankService knowledgeBankService;
    @PostMapping("/addKnowledgeBank")
    public CommonResponse<String> addKnowledgeBank(@RequestBody KnowledgeBankDTO knowledgeBankDTO)
    {
        return knowledgeBankService.addKnowledgeBank(knowledgeBankDTO);
    }

    @GetMapping("/getKnowledgeBank/{course_id}")
    public CommonResponse<List<KnowledgeBankVo>> getKnowledgeBank(@PathVariable int course_id)
    {
        return knowledgeBankService.getKnowledgeBankByCourseId(course_id);
    }
}
