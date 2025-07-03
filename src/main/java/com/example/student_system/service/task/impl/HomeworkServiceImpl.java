package com.example.student_system.service.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import com.example.student_system.domain.entity.task.Homework;
import com.example.student_system.domain.vo.task.HomeworkVO;
import com.example.student_system.mapper.task.HomeworkMapper;
import com.example.student_system.service.task.HomeworkService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service("HomeworkService")
public class HomeworkServiceImpl implements HomeworkService
{

    @Autowired
    private HomeworkMapper homeworkMapper;

    @Override
    public boolean isHomeworkExists(String homework_title, int course_id, int user_id)
    {
        QueryWrapper<Homework> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", homework_title)
                .eq("course_id", course_id)
                .eq("user_id", user_id);

        List<Homework> homeworkList = homeworkMapper.selectList(queryWrapper);
        return !homeworkList.isEmpty();
    }

    //FIXME:存在一定逻辑漏洞
    @Override
    public CommonResponse<Homework> assignHomework(Homework newHomework) {
        //作业可发布逻辑:
        //1.不同班级可以有相同标题的作业
        //2.相同班级不可以存在相同标题的作业
        //3.判断逻辑：找到了所有同课程同标题的作业->不能有相同的学生拥有同名作业
        if(isHomeworkExists(newHomework.getHomework_title(), newHomework.getCourse_id(), newHomework.getUser_id()))
            return CommonResponse.createForError(
                    ResponseCode.HOMEWORK_EXISTS.getCode(),
                    ResponseCode.HOMEWORK_EXISTS.getDescription()
            );

        // 设置创建时间
        newHomework.setCreate_time(new Date());

        homeworkMapper.insert(newHomework);
        return CommonResponse.createForSuccess(
                ResponseCode.HOMEWORK_ASSIGN_SUCCESS.getCode(),
                ResponseCode.HOMEWORK_ASSIGN_SUCCESS.getDescription()
        );
    }

    @Override
    public CommonResponse<List<Homework>> getHomeworkByUserid(int user_id) {
        QueryWrapper<Homework> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id);
        //根据user_id获取全部作业
        List<Homework> homeworkList = homeworkMapper.selectList(queryWrapper);

        return CommonResponse.createForSuccess(
                ResponseCode.HOMEWORK_LIST_FETCH_SUCCESS.getCode(),
                ResponseCode.HOMEWORK_LIST_FETCH_SUCCESS.getDescription(),
                homeworkList
        );
    }

    @Override
    public CommonResponse<List<Homework>> getHomeworkByUserid(int user_id, int course_id)
    {
        QueryWrapper<Homework> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id)
                .eq("course_id", course_id);
        // 获取用户某个课程的全部作业
        List<Homework> homeworkList = homeworkMapper.selectList(queryWrapper);

        return CommonResponse.createForSuccess(
                ResponseCode.HOMEWORK_LIST_FETCH_SUCCESS.getCode(),
                ResponseCode.HOMEWORK_LIST_FETCH_SUCCESS.getDescription(),
                homeworkList
        );
    }


    /**
     这里只有登录的学生才能获取作业详情
     VO中 score submit_url submit_content 和 remark 属性可能为空(取决于用户是否已提交和老师是否已批阅)
     status字段 0未交 1已交未阅 2已阅
     */
    @Override
    public CommonResponse<HomeworkVO> getHomeworkDetail(int user_id, int course_id, String title)
    {
        QueryWrapper<Homework> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", course_id)
                .eq("title", title)
                .eq("user_id", user_id);
        Homework homework = homeworkMapper.selectOne(queryWrapper);
        HomeworkVO homeworkVO = new HomeworkVO();
        BeanUtils.copyProperties(homeworkVO, homework);

        return CommonResponse.createForSuccess(
                ResponseCode.HOMEWORK_DETAIL_FETCH_SUCCESS.getCode(),
                ResponseCode.HOMEWORK_DETAIL_FETCH_SUCCESS.getDescription(),
                homeworkVO
        );
    }

    @Override
    public CommonResponse<Homework> submitHomework(Homework homework)
    {
        //根据作业标题更新作业
        UpdateWrapper<Homework> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("title", homework.getHomework_title())
                .eq("course_id", homework.getCourse_id())
                .eq("user_id", homework.getUser_id())
                .set("status", "1")
                .set("update_time", new Date());

        homeworkMapper.update(updateWrapper);
        return CommonResponse.createForSuccess(
                ResponseCode.HOMEWORK_SUBMISSION_SUCCESS.getCode(),
                ResponseCode.HOMEWORK_SUBMISSION_SUCCESS.getDescription()
        );
    }

    @Override
    public CommonResponse<Homework> remarkHomework(Homework homework)
    {
        UpdateWrapper<Homework> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("title", homework.getHomework_title())
                .eq("course_id", homework.getCourse_id())
                .eq("user_id", homework.getUser_id())
                .set("status", "2")
                .set("remark", homework.getRemark())
                .set("update_time", new Date());

        return CommonResponse.createForSuccess(
            ResponseCode.HOMEWORK_REMARK_SUCCESS.getCode(),
            ResponseCode.HOMEWORK_ASSIGN_SUCCESS.getDescription()
        );
    }

    @Override
    public CommonResponse<Homework> syncHomework(int user_id, int course_id)
    {
        return null;
    }

    @Override
    public BigDecimal getHomeworkScore(int user_id, int course_id)
    {
        // 先获取对应用户对应课程的所有作业
        QueryWrapper<Homework> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id)
                .eq("course_id", course_id);
        List<Homework> homeworkList = homeworkMapper.selectList(queryWrapper);

        // 如果作业数为0, 直接返回null
        if(homeworkList.isEmpty())
            return null;

        // 设置总分
        BigDecimal sum = new BigDecimal("0");
        BigDecimal homework_num = new BigDecimal(homeworkList.size());
        BigDecimal final_score;

        for(Homework homework : homeworkList)
        {
            BigDecimal score = homework.getScore();
            if(score == null)
                continue;
            sum = sum.add(score);
        }
        final_score = sum.divide(homework_num, RoundingMode.HALF_UP);

        return final_score;
    }


}