package com.example.student_system.controller.course;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.dto.course.DiscussionDTO;
import com.example.student_system.domain.vo.DiscussionVo;
import com.example.student_system.service.course.DiscussionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discussions")
public class DiscussionController {
    @Resource
    private DiscussionService discussionService;

    @PostMapping("/insertHead")
    public CommonResponse<String> insertHead(@RequestBody DiscussionDTO dto)
    {
        return discussionService.insertHeadDiscussion(dto);
    }

    @PostMapping("/insertFollow/discussion/{discussion_id}")
    public CommonResponse<String> insertFollow(@PathVariable String discussion_id, @RequestBody DiscussionDTO dto)
    {
        return discussionService.insertFollowDiscussion(discussion_id,dto);
    }

    @GetMapping("/getDiscussionIdByChapter/chapter/{chapter_id}")
    public CommonResponse<List<String>> getDiscussionIdByChapter(@PathVariable String chapter_id)
    {
        return discussionService.getAllDiscussionIdsByChapter(chapter_id);
    }

    @GetMapping("/getDiscussionIdByUser/user/{user_id}")
    public CommonResponse<List<String>> getDiscussionIdByUser(@PathVariable int user_id)
    {
        return discussionService.getDiscussionIdsByUser(user_id);
    }

    @GetMapping("/getBlockCountByChapter/chapter/{chapter_id}")
    public CommonResponse<Integer> getBlockByChapter(@PathVariable String chapter_id)
    {
        return discussionService.getBlockCountByChapter(chapter_id);
    }

    @GetMapping("/getBlockCountByUser/user/{user_id}")
    public CommonResponse<Integer> getBlockByUser(@PathVariable int user_id)
    {
        return discussionService.getBlockCountByUser(user_id);
    }

    @GetMapping("/getDiscussionBlock/discussion_id/{discussion_id}")
    CommonResponse<List<DiscussionVo>> getDiscussionById(@PathVariable String discussion_id)
    {
        return discussionService.getDiscussionById(discussion_id);
    }

    @GetMapping("/getDiscussionByUser/user/{user_id}/discussion_id/{discussion_id}")
    CommonResponse<List<DiscussionVo>> getDiscussionByUserId(@PathVariable int user_id,@PathVariable String discussion_id)
    {
        return discussionService.getDiscussionByUserId(user_id,discussion_id);
    }
}
