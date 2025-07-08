package com.example.student_system.service.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import com.example.student_system.domain.entity.course.Course;
import com.example.student_system.domain.entity.task.Score;
import com.example.student_system.domain.vo.task.ScoreVO;
import com.example.student_system.mapper.course.CourseMapper;
import com.example.student_system.mapper.task.ScoreMapper;
import com.example.student_system.service.task.ExamService;
import com.example.student_system.service.task.HomeworkService;
import com.example.student_system.service.task.ScoreService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.encrypt.RsaAlgorithm;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service("ScoreService")
public class ScoreServiceImpl implements ScoreService
{
    @Autowired
    private ScoreMapper scoreMapper;
    @Autowired
    private CourseMapper courseMapper;

    // 用于获取各部分成绩
    @Autowired
    private ExamService examService;
    @Autowired
    private HomeworkService homeworkService;

    @Override
    public CommonResponse<String> insertScoreRecord(int user_id, int course_id)
    {
        // 新建成绩项
        Score score = new Score();
        score.setUser_id(user_id);
        score.setCourse_id(course_id);
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", course_id);
        score.setCourse_name(courseMapper.selectOne(queryWrapper).getCourse_name());

        score.setHomework_score(null);
        score.setExam_score(null);
        score.setVideo_score(new BigDecimal("0"));

        score.setCreate_time(new Date());
        scoreMapper.insert(score);
        return CommonResponse.createForSuccess(
                ResponseCode.SCORE_CREATE_SUCCESS.getCode(),
                ResponseCode.SCORE_CREATE_SUCCESS.getDescription()
        );
    }

    @Override
    public CommonResponse<ScoreVO> getUserScore(int user_id, int course_id)
    {
        QueryWrapper<Score> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id)
                .eq("course_id", course_id)
                .ne("delete_time", null);
        Score score = scoreMapper.selectOne(queryWrapper);
        ScoreVO scoreVO = new ScoreVO();
        BeanUtils.copyProperties(score, scoreVO);

        return CommonResponse.createForSuccess(
                ResponseCode.SCORE_FETCH_SUCCESS.getCode(),
                ResponseCode.SCORE_FETCH_SUCCESS.getDescription(),
                scoreVO
        );
    }

    @Override
    public CommonResponse<String> deleteScore(int user_id, int course_id)
    {
        UpdateWrapper<Score> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", user_id)
                .eq("course_id", course_id)
                .set("delete_time", new Date());
        scoreMapper.update(updateWrapper);

        return CommonResponse.createForSuccess(
                ResponseCode.SUCCESS.getCode(),
                ResponseCode.SUCCESS.getDescription()
        );
    }

    @Override
    public CommonResponse<String> updateVideoScore(int user_id, int course_id, BigDecimal score)
    {
        UpdateWrapper<Score> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", user_id)
                .eq("course_id", course_id)
                .ne("delete_time", null)
                .set("video_score", score)
                .set("update_time", new Date());
        scoreMapper.update(updateWrapper);

        return CommonResponse.createForSuccess(
                ResponseCode.SUCCESS.getCode(),
                ResponseCode.SUCCESS.getDescription()
        );
    }

    @Override
    public CommonResponse<String> updateHomeworkScore(int user_id, int course_id, BigDecimal score)
    {
        UpdateWrapper<Score> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", user_id)
                .eq("course_id", course_id)
                .ne("delete_time", null)
                .set("homework_score", score)
                .set("update_time", new Date());
        scoreMapper.update(updateWrapper);

        return CommonResponse.createForSuccess(
                ResponseCode.SUCCESS.getCode(),
                ResponseCode.SUCCESS.getDescription()
        );
    }

    @Override
    public CommonResponse<String> updateExamScore(int user_id, int course_id, BigDecimal score)
    {
        UpdateWrapper<Score> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", user_id)
                .eq("course_id", course_id)
                .ne("delete_time", null)
                .set("exam_score", score)
                .set("update_time", new Date());
        scoreMapper.update(updateWrapper);

        return CommonResponse.createForSuccess(
                ResponseCode.SUCCESS.getCode(),
                ResponseCode.SUCCESS.getDescription()
        );
    }

    @Override
    public CommonResponse<ScoreVO> updateAndGetScore(int user_id, int course_id)
    {
        // 先判断是否存在，如果不存在，则插入
        QueryWrapper<Score> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id)
                .eq("course_id", course_id);
        if(scoreMapper.selectOne(queryWrapper) == null)
        {
            insertScoreRecord(user_id, course_id);
        }

        //TODO: 完善video_score的获取逻辑
        BigDecimal video_score = new BigDecimal("100");
        // homework和exam都是有对应的考试和作业才会有值,课程没有考试或作业那么成绩为null
        BigDecimal homework_score = homeworkService.getHomeworkScore(user_id, course_id);
        BigDecimal exam_score = examService.getAverageExamScore(user_id, course_id);

        updateVideoScore(user_id, course_id, video_score);
        updateHomeworkScore(user_id,course_id,homework_score);
        updateExamScore(user_id, course_id, exam_score);

        ScoreVO scoreVO = new ScoreVO();
        scoreVO.setVideo_score(video_score);
        scoreVO.setHomework_score(homework_score);
        scoreVO.setExam_score(exam_score);
        scoreVO.setCourse_id(course_id);
        // 课程名?

        return CommonResponse.createForSuccess(
                ResponseCode.SCORE_FETCH_AND_UPDATE_SUCCESS.getCode(),
                ResponseCode.SCORE_FETCH_AND_UPDATE_SUCCESS.getDescription(),
                scoreVO
        );
    }


}
