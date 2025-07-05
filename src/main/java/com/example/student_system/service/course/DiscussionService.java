package com.example.student_system.service.course;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.dto.course.DiscussionDTO;
import com.example.student_system.domain.vo.course.DiscussionVo;

import java.util.List;

public interface DiscussionService {
    CommonResponse<String> insertHeadDiscussion(DiscussionDTO discussionDTO);
    CommonResponse<List<DiscussionVo>> getDiscussionById(String discussion_id);
    CommonResponse<String> insertFollowDiscussion(String discussion_id,DiscussionDTO discussionDTO);
//    CommonResponse<List<DiscussionVo>> getDiscussionByUserId(int user_id,String discussion_id);
    CommonResponse<Integer> getBlockCountByChapter(String chapter_id);
    CommonResponse<Integer> getBlockCountByUser(int user_id);
    CommonResponse<List<String>> getAllDiscussionIdsByChapter(String chapter_id);

    CommonResponse<List<String>> getDiscussionIdsByUser(int user_id);

}
