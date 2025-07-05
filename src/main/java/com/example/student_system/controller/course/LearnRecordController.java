package com.example.student_system.controller.course;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.dto.course.LearnRecordInsertDTO;
import com.example.student_system.domain.dto.course.LearnRecordUpdateDTO;
import com.example.student_system.domain.vo.course.LearnRecordVo;
import com.example.student_system.service.course.LearnRecordService;
import com.example.student_system.util.UserContext;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/learnRecords")
public class LearnRecordController {
    @Resource
    private LearnRecordService learnRecordService;

    @PostMapping("/insert")
    public CommonResponse<String> insertLearnRecord(@RequestBody LearnRecordInsertDTO dto)
    {
        dto.setUser_id(UserContext.getCurrentUserId());
        return learnRecordService.insertLearnRecord(dto);
    }

    @PutMapping("/update/course_id/{course_id}/chapter_id/{chapter_id}")
    public CommonResponse<String> updateLearnRecord(@PathVariable int course_id,@PathVariable String chapter_id,@RequestBody LearnRecordUpdateDTO dto)
    {
        return learnRecordService.updateLearnRecord(course_id,chapter_id,dto);
    }

    @GetMapping("/get/course_id/{course_id}/chapter_id/{chapter_id}")
    public CommonResponse<LearnRecordVo> getLearnRecordVo(@PathVariable int course_id, @PathVariable String chapter_id)
    {
        Integer user_id= UserContext.getCurrentUserId();
        return learnRecordService.getLearnRecord(user_id,course_id,chapter_id);
    }

//    @GetMapping("/getList/course_id/{course_id}")
//    public CommonResponse<List<LearnRecordVo>> getLearnRecordVoList(@PathVariable int course_id)
//    {
//        return learnRecordService.gerLearnRecordList(course_id);
//    }
}
