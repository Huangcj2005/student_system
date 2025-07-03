package com.example.student_system.controller.course;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.dto.course.LearnRecordInsertDTO;
import com.example.student_system.domain.dto.course.LearnRecordUpdateDTO;
import com.example.student_system.domain.entity.course.LearnRecord;
import com.example.student_system.domain.vo.LearnRecordVo;
import com.example.student_system.service.course.LearnRecordService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/learnRecords")
public class LearnRecordController {
    @Resource
    private LearnRecordService learnRecordService;

    @PostMapping("/insert")
    public CommonResponse<String> insertLearnRecord(@RequestBody LearnRecordInsertDTO dto)
    {
        return learnRecordService.insertLearnRecord(dto);
    }

    @PutMapping("/update/course_id/{course_id}/chapter_id/{chapter_id}")
    public CommonResponse<String> updateLearnRecord(@PathVariable int course_id,@PathVariable String chapter_id,@RequestBody LearnRecordUpdateDTO dto)
    {
        return learnRecordService.updateLearnRecord(course_id,chapter_id,dto);
    }

    @GetMapping("/get/user_id/{user_id}/course_id/{course_id}/chapter_id/{chapter_id}")
    public CommonResponse<LearnRecordVo> getLearnRecordVo(@PathVariable int user_id,@PathVariable int course_id, @PathVariable String chapter_id)
    {
        return learnRecordService.getLearnRecord(user_id,course_id,chapter_id);
    }

//    @GetMapping("/getList/course_id/{course_id}")
//    public CommonResponse<List<LearnRecordVo>> getLearnRecordVoList(@PathVariable int course_id)
//    {
//        return learnRecordService.gerLearnRecordList(course_id);
//    }
}
