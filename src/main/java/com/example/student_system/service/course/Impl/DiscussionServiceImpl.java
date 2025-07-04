package com.example.student_system.service.course.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import com.example.student_system.domain.dto.course.DiscussionDTO;
import com.example.student_system.domain.entity.account.User;
import com.example.student_system.domain.entity.course.Discussion;
import com.example.student_system.domain.vo.course.DiscussionVo;
import com.example.student_system.mapper.account.UserMapper;
import com.example.student_system.mapper.course.DiscussionMapper;
import com.example.student_system.service.course.DiscussionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("DiscussionService")
public class DiscussionServiceImpl implements DiscussionService {
    @Autowired
    private DiscussionMapper discussionMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public CommonResponse<String> insertHeadDiscussion(DiscussionDTO discussionDTO) {
        Discussion discussion=new Discussion();
        BeanUtils.copyProperties(discussionDTO,discussion);
        discussion.setCreate_time(new Date());
        discussion.setUpdate_time(new Date());
        discussion.setDiscussion_id(UUID.randomUUID().toString());
        discussionMapper.insert(discussion);

        return CommonResponse.createForSuccess(
                ResponseCode.DISCUSSION_INSERT_SUCCESS.getCode(),
                ResponseCode.DISCUSSION_INSERT_SUCCESS.getDescription()
        );
    }

    @Override
    public CommonResponse<List<DiscussionVo>> getDiscussionById(String discussion_id) {//获取一个楼层的全部评论
        QueryWrapper<Discussion> wrapper = new QueryWrapper<>();
        wrapper.eq("discussion_id", discussion_id);

        List<Discussion> discussionList = discussionMapper.selectList(wrapper);

        Set<Integer> userIdSet = discussionList.stream()
                .map(Discussion::getUser_id)
                .collect(Collectors.toSet());

        //一次查出所有 User 对象
        List<User> userList = userMapper.selectList(
                new QueryWrapper<User>().in("user_id", userIdSet)  // 注意字段名是 user_id，不是 id
        );
        System.out.println("userIdSet = " + userIdSet);
        System.out.println("userList = " + userList);

        Map<Integer, String> userIdNameMap = userList.stream()
                .collect(Collectors.toMap(User::getUserId, User::getUserName));

        List<DiscussionVo> voList = discussionList.stream().map(discussion -> {
            DiscussionVo vo = new DiscussionVo();
            BeanUtils.copyProperties(discussion, vo);
            vo.setUser_name(userIdNameMap.get(discussion.getUser_id()));
            return vo;
        }).collect(Collectors.toList());

        if(!voList.isEmpty())
        {
            return CommonResponse.createForSuccess(
                    ResponseCode.DISCUSSION_LIST_FETCH_SUCCESS.getCode(),
                    ResponseCode.DISCUSSION_LIST_FETCH_SUCCESS.getDescription(),
                    voList);
        }
        else
        {
            return CommonResponse.createForError(
                    ResponseCode.DISCUSSION_BLOCK_COUNT_FETCH_FAIL.getCode(),
                    ResponseCode.DISCUSSION_BLOCK_COUNT_FETCH_FAIL.getDescription()
            );
        }

    }

    @Override
    public CommonResponse<String> insertFollowDiscussion(String discussion_id, DiscussionDTO discussionDTO) {
        Discussion discussion=new Discussion();
        BeanUtils.copyProperties(discussionDTO,discussion);
        discussion.setDiscussion_id(discussion_id);
        discussion.setCreate_time(new Date());
        discussion.setUpdate_time(new Date());
        discussionMapper.insert(discussion);

        return CommonResponse.createForSuccess(
                ResponseCode.DISCUSSION_INSERT_SUCCESS.getCode(),
                ResponseCode.DISCUSSION_INSERT_SUCCESS.getDescription()
        );
    }

//    @Override
//    public CommonResponse<List<DiscussionVo>> getDiscussionByUserId(int user_id,String discussion_id) {//
//        QueryWrapper<Discussion> queryWrapper=new QueryWrapper<>();
//        queryWrapper.eq("user_id",user_id)
//                .eq("discussion_id",discussion_id);
//
//        List<Discussion>discussionList=discussionMapper.selectList(queryWrapper);
//        List<DiscussionVo> voList = discussionList.stream().map(discussion -> {
//            DiscussionVo vo = new DiscussionVo();
//            BeanUtils.copyProperties(discussion, vo);
//            return vo;
//        }).collect(Collectors.toList());
//
//        if (!voList.isEmpty())
//        {
//            return CommonResponse.createForSuccess(
//                    ResponseCode.DISCUSSION_LIST_FETCH_SUCCESS.getCode(),
//                    ResponseCode.DISCUSSION_LIST_FETCH_SUCCESS.getDescription(),
//                    voList);
//        }
//        else
//        {
//            return CommonResponse.createForError(
//                    ResponseCode.DISCUSSION_BLOCK_COUNT_FETCH_FAIL.getCode(),
//                    ResponseCode.DISCUSSION_BLOCK_COUNT_FETCH_FAIL.getDescription()
//            );
//        }
//
//    }


    @Override
    public CommonResponse<Integer> getBlockCountByChapter(String chapter_id) {//获取章节内一共有多少块，方便创建页面
        QueryWrapper<Discussion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id", chapter_id);
        queryWrapper.select("discussion_id"); // 只查 discussion_id 字段

        List<Discussion> discussions = discussionMapper.selectList(queryWrapper);

        // 用 Set 去重，得到不同 discussion_id 的数量
        Set<String> uniqueBlocks = discussions.stream()
                .map(Discussion::getDiscussion_id)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        int count = uniqueBlocks.size();

        if(count!=0)
        {
            return CommonResponse.createForSuccess(
                    ResponseCode.DISCUSSION_BLOCK_COUNT_FETCH_SUCCESS.getCode(),
                    ResponseCode.DISCUSSION_BLOCK_COUNT_FETCH_SUCCESS.getDescription(),
                    count
            );
        }
        else
        {
            return CommonResponse.createForError(
                    ResponseCode.DISCUSSION_BLOCK_COUNT_FETCH_FAIL.getCode(),
                    ResponseCode.DISCUSSION_BLOCK_COUNT_FETCH_FAIL.getDescription()
            );
        }

    }

    @Override
    public CommonResponse<Integer> getBlockCountByUser(int user_id) {//获取我一共发布了多少帖子，方便前端循环创建块
        QueryWrapper<Discussion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id);
        queryWrapper.select("discussion_id"); // 只查 discussion_id 字段

        List<Discussion> discussions = discussionMapper.selectList(queryWrapper);

        // 用 Set 去重，得到不同 discussion_id 的数量
        Set<String> uniqueBlocks = discussions.stream()
                .map(Discussion::getDiscussion_id)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        int count = uniqueBlocks.size();

        if(count!=0)
        {
            return CommonResponse.createForSuccess(
                    ResponseCode.DISCUSSION_BLOCK_COUNT_FETCH_SUCCESS.getCode(),
                    ResponseCode.DISCUSSION_BLOCK_COUNT_FETCH_SUCCESS.getDescription(),
                    count
            );
        }
        else
        {
            return CommonResponse.createForError(
                    ResponseCode.DISCUSSION_BLOCK_COUNT_FETCH_FAIL.getCode(),
                    ResponseCode.DISCUSSION_BLOCK_COUNT_FETCH_FAIL.getDescription()
            );
        }
    }

    @Override
    public CommonResponse<List<String>> getAllDiscussionIdsByChapter(String chapter_id) {//获取所有discussion_id
        QueryWrapper<Discussion> wrapper = new QueryWrapper<>();
        wrapper.select("DISTINCT discussion_id");
        wrapper.eq("chapter_id", chapter_id);

        List<Discussion> discussionList = discussionMapper.selectList(wrapper);

        // 提取 discussion_id 字段并去重
        List<String> discussionIdList = discussionList.stream()
                .map(Discussion::getDiscussion_id)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if(!discussionIdList.isEmpty())
        {
            return CommonResponse.createForSuccess(
                    ResponseCode.DISCUSSION_BLOCK_FETCH_SUCCESS.getCode(),
                    ResponseCode.DISCUSSION_BLOCK_FETCH_SUCCESS.getDescription(),
                    discussionIdList
            );
        }
        else {
            return CommonResponse.createForError(
                    ResponseCode.DISCUSSION_BLOCK_COUNT_FETCH_FAIL.getCode(),
                    ResponseCode.DISCUSSION_BLOCK_COUNT_FETCH_FAIL.getDescription()
            );
        }

    }

    @Override
    public CommonResponse<List<String>> getDiscussionIdsByUser(int user_id) {//个人中心获取自己发布的帖子id
        QueryWrapper<Discussion> wrapper = new QueryWrapper<>();
        wrapper.select("DISTINCT discussion_id");
        wrapper.eq("user_id", user_id);

        List<Discussion> discussionList = discussionMapper.selectList(wrapper);

        List<String> discussionIdList = discussionList.stream()
                .map(Discussion::getDiscussion_id)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if(!discussionIdList.isEmpty())
        {
            return CommonResponse.createForSuccess(
                    ResponseCode.DISCUSSION_BLOCK_FETCH_SUCCESS.getCode(),
                    ResponseCode.DISCUSSION_BLOCK_FETCH_SUCCESS.getDescription(),
                    discussionIdList
            );
        }
        else
        {
            return CommonResponse.createForError(
                    ResponseCode.DISCUSSION_BLOCK_COUNT_FETCH_FAIL.getCode(),
                    ResponseCode.DISCUSSION_BLOCK_COUNT_FETCH_FAIL.getDescription()
            );
        }

    }

}
