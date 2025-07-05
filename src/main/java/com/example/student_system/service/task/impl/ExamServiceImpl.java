package com.example.student_system.service.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import com.example.student_system.domain.entity.task.*;
import com.example.student_system.domain.vo.task.ExamScoreVO;
import com.example.student_system.domain.vo.task.ExamVO;
import com.example.student_system.domain.vo.task.PaperVO;
import com.example.student_system.domain.vo.task.QuestionVO;
import com.example.student_system.mapper.task.*;
import com.example.student_system.service.task.ExamService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service("ExamService")
public class ExamServiceImpl implements ExamService
{
    @Autowired
    private QuestionBankMapper bankMapper;

    @Autowired
    private QuestionRecordMapper recordMapper;

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private ExamRecordMapper examRecordMapper;

    @Autowired
    private ExamScoreMapper examScoreMapper;


    /**
     对于自动生成的试卷，默认选择题10道，每道4分
                        判断题10道，每道2分
                        填空题10道，每道4分
     */
    @Override
    public CommonResponse<List<QuestionVO>> generatePaper( int exam_id,int choice_num, int judge_num,int text_num, String tags, boolean isInsert)
    {
        List<QuestionBank> question_list = new ArrayList<>();
        List<QuestionBank> choice_list = getRandomQuestion("选择题", tags, choice_num);
        List<QuestionBank> judge_list = getRandomQuestion("判断题", tags, judge_num);
        List<QuestionBank> text_list = getRandomQuestion("填空题", tags, text_num);

        if(choice_list == null || judge_list == null || text_list == null)
        {
            return CommonResponse.createForError(
                    ResponseCode.PAPER_GENERATE_FAILED.getCode(),
                    ResponseCode.PAPER_GENERATE_FAILED.getDescription()
            );
        }

        //将生成的题目加入
        question_list.addAll(choice_list);
        question_list.addAll(judge_list);
        question_list.addAll(text_list);

        int id_count = 1;
        // 生成问题列表
        List<QuestionVO> questionVOList = new ArrayList<>();

        for(QuestionBank question : question_list)
        {
            QuestionVO questionVO = new QuestionVO();
            questionVO.setQuestion_id(id_count);
            questionVO.setQuestion_type(question.getQuestion_type());
            questionVO.setQuestion_content(question.getQuestion_content());
            questionVOList.add(questionVO);

            ExamRecord examRecord = new ExamRecord();
            examRecord.setExam_id(exam_id);
            examRecord.setQuestion_id(id_count);
            examRecord.setQuestion_type(question.getQuestion_type());
            examRecord.setQuestion_content(question.getQuestion_content());
            examRecord.setRight_answer(question.getQuestion_answer());
            examRecord.setCreate_time(new Date());

            // 先检查是否已有试题，已有则删除
            QueryWrapper<ExamRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("exam_id", exam_id)
                    .eq("question_id", id_count);
            examRecordMapper.delete(queryWrapper);

            // 将生成的问题插入至数据库
            examRecordMapper.insert(examRecord);

            id_count++;
        }

        // 设置考试的状态为未开始
        UpdateWrapper<Exam> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("exam_id", exam_id)
                .set("status", "0");
        examMapper.update(updateWrapper);

        return CommonResponse.createForSuccess(
                ResponseCode.PAPER_GENERATE_SUCCESS.getCode(),
                ResponseCode.PAPER_GENERATE_SUCCESS.getDescription(),
                questionVOList
        );
    }

    public CommonResponse<Exam> createExam(Exam exam)
    {
        // 设置exam_id
        examMapper.insert(exam);
        // 将 id 设置为 exam_id
        exam.setExam_id(exam.getId());
        // 设置状态(创建基础信息后仍然是未发布的状态)
        exam.setStatus("3");
        // 更新 exam_id
        examMapper.updateById(exam);  // 通过 id 更新 exam_id

        checkExamStatus(exam.getExam_id());
        return CommonResponse.createForSuccess(
                ResponseCode.EXAM_CREATE_SUCCESS.getCode(),
                ResponseCode.EXAM_CREATE_SUCCESS.getDescription()
        );
    }

    @Override
    public CommonResponse<List<Exam>> getExamList(int course_id)
    {
        QueryWrapper<Exam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", course_id);
        List<Exam> examList = examMapper.selectList(queryWrapper);
        for(Exam exam : examList)
        {
            Exam updated_exam = checkExamStatus(exam.getExam_id());
            //对于未发布的考试不计入列表中
            if(updated_exam == null)
                continue;
            exam.setStatus(updated_exam.getStatus());;
        }
        return CommonResponse.createForSuccess(
                ResponseCode.EXAM_LIST_FETCH_SUCCESS.getCode(),
                ResponseCode.EXAM_CREATE_SUCCESS.getDescription(),
                examList
        );
    }

    @Override
    public CommonResponse<List<ExamVO>> getUserExamList(List<Integer> course_id_list)
    {
        QueryWrapper<Exam> queryWrapper = new QueryWrapper<>();
        List<Exam> newExamList = new ArrayList<>();
        for(int id : course_id_list)
        {
            queryWrapper.eq("course_id", id);
            List<Exam> examList = examMapper.selectList(queryWrapper);
            // 设置状态
            for(Exam exam : examList)
            {
                Exam updated_exam = checkExamStatus(exam.getExam_id());
                //对于未发布的考试不计入列表中
                if(updated_exam == null)
                    continue;

                // 已经考完的考试也不计入期中
                QueryWrapper<ExamScore> examScoreQueryWrapper = new QueryWrapper<>();
                examScoreQueryWrapper.eq("exam_id", exam.getExam_id());
                if(examScoreMapper.selectOne(examScoreQueryWrapper) != null)
                    continue;

                exam.setStatus(updated_exam.getStatus());
                newExamList.add(exam);
            }

            queryWrapper.clear();
        }

        List<ExamVO> examVOList = new ArrayList<>();
        for (Exam exam : newExamList)
        {
            ExamVO examVO = new ExamVO();
            BeanUtils.copyProperties(exam, examVO);
            examVOList.add(examVO);
        }

        return CommonResponse.createForSuccess(
                ResponseCode.USER_EXAM_LIST_FETCH_SUCCESS.getCode(),
                ResponseCode.USER_EXAM_LIST_FETCH_SUCCESS.getDescription(),
                examVOList
        );
    }

    @Override
    public CommonResponse<QuestionRecord> saveOrUpdateQuestionRecord(QuestionRecord questionRecord)
    {
        System.out.println(questionRecord);

        // 查询对应的答题记录
        QueryWrapper<QuestionRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("exam_id", questionRecord.getExam_id())
                .eq("user_id", questionRecord.getUser_id())
                .eq("question_id", questionRecord.getQuestion_id());

        List<QuestionRecord> questionRecordList = recordMapper.selectList(queryWrapper);
        // 如果没有该问题，则进行插入
        if(questionRecordList.isEmpty())
        {
            // 设置正确答案
            QueryWrapper<ExamRecord> examQueryWrapper = new QueryWrapper<>();
            examQueryWrapper.eq("exam_id", questionRecord.getExam_id())
                    .eq("question_id", questionRecord.getQuestion_id());
            ExamRecord examRecord = examRecordMapper.selectOne(examQueryWrapper);
            questionRecord.setRight_answer(examRecord.getRight_answer());
            questionRecord.setCreate_time(new Date());

            recordMapper.insert(questionRecord);
            return CommonResponse.createForSuccess(
                    ResponseCode.QUESTION_RECORD_INSERT_SUCCESS.getCode(),
                    ResponseCode.QUESTION_RECORD_INSERT_SUCCESS.getDescription()
            );
        }

        // 否则存在该条记录，则进行更新
        UpdateWrapper<QuestionRecord> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("exam_id", questionRecord.getExam_id())
                .eq("question_id", questionRecord.getQuestion_id())
                .set("answer", questionRecord.getAnswer())
                .set("update_time", new Date());

        recordMapper.update(updateWrapper);

        return CommonResponse.createForSuccess(
                ResponseCode.QUESTION_RECORD_UPDATE_SUCCESS.getCode(),
                ResponseCode.QUESTION_RECORD_UPDATE_SUCCESS.getDescription()
        );
    }

    @Override
    public CommonResponse<BigDecimal> submitExam(int user_id, int exam_id)
    {
        UpdateWrapper<QuestionRecord> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", user_id)
                .eq("exam_id", exam_id)
                .set("delete_time", new Date());
        recordMapper.update(updateWrapper);

        // 计算考试成绩
        BigDecimal score = getExamScoreByUserId(exam_id, user_id, null, null, null);

        ExamScore examScore = new ExamScore();
        examScore.setExam_id(exam_id);
        examScore.setUser_id(user_id);
        examScore.setScore(score);
        examScore.setCreate_time(new Date());

        examScoreMapper.insert(examScore);

        return CommonResponse.createForSuccess(
                ResponseCode.EXAM_SUBMIT_SUCCESS.getCode(),
                ResponseCode.EXAM_SUBMIT_SUCCESS.getDescription(),
                score
        );
    }

    @Override
    public CommonResponse<PaperVO> getPaper(int exam_id)
    {
        PaperVO paperVO = new PaperVO();

        // 先获取问题列表
        QueryWrapper<ExamRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("exam_id", exam_id)
                .orderByAsc("question_id");
        List<ExamRecord> examRecordList = examRecordMapper.selectList(queryWrapper);

        // 复制属性
        List<QuestionVO> questionVOList = new ArrayList<>();
        for(ExamRecord examRecord : examRecordList)
        {
            QuestionVO questionVO = new QuestionVO();
            questionVO.setQuestion_id(examRecord.getQuestion_id());
            questionVO.setQuestion_type(examRecord.getQuestion_type());
            questionVO.setQuestion_content(examRecord.getQuestion_content());
            questionVOList.add(questionVO);
        }
        QueryWrapper<Exam> examQueryWrapper = new QueryWrapper<>();
        queryWrapper.eq("exam_id", exam_id);

        paperVO.setExam_title(examMapper.selectOne(examQueryWrapper).getExam_name());
        paperVO.setExam_id(exam_id);
        paperVO.setQuestionVOList(questionVOList);

        return CommonResponse.createForSuccess(
                ResponseCode.PAPER_FETCH_SUCCESS.getCode(),
                ResponseCode.PAPER_FETCH_SUCCESS.getDescription(),
                paperVO
        );
    }

    @Override
    public CommonResponse<List<ExamScoreVO>> getFinishedExam(int user_id)
    {
        QueryWrapper<ExamScore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id);
        List<ExamScore> examScoreList = examScoreMapper.selectList(queryWrapper);

        List<ExamScoreVO> examScoreVOList = new ArrayList<>();

        // 设置VO
        for(ExamScore examScore : examScoreList)
        {
            // 根据exam_id查询基本信息
            QueryWrapper<Exam> examQueryWrapper = new QueryWrapper<>();
            queryWrapper.eq("exam_id", examScore.getExam_id());
            Exam exam = examMapper.selectOne(examQueryWrapper);

            ExamScoreVO examScoreVO = new ExamScoreVO();

            BeanUtils.copyProperties(exam, examScoreVO);
            examScoreVO.setScore(examScore.getScore());

            examScoreVOList.add(examScoreVO);
        }

        return CommonResponse.createForSuccess(
                ResponseCode.FINISHED_EXAM_FETCH_SUCCESS.getCode(),
                ResponseCode.FINISHED_EXAM_FETCH_SUCCESS.getDescription(),
                examScoreVOList
        );
    }

    @Override
    public BigDecimal getExamScoreByUserId(int exam_id, int user_id, BigDecimal choice_score, BigDecimal judge_score, BigDecimal text_score)
    {
        //TODO: 后续可在exam表中添加对应得分字段
        choice_score = new BigDecimal("4");
        judge_score = new BigDecimal("2");
        text_score = new BigDecimal("4");

        //在答题记录表中对比学生答案和用户答案
        QueryWrapper<QuestionRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("exam_id", exam_id)
                .eq("user_id", user_id);
        List<QuestionRecord> questionRecordList = recordMapper.selectList(queryWrapper);
        BigDecimal sum = new BigDecimal("0");

        for(QuestionRecord questionRecord : questionRecordList)
        {
            // 计算选择题得分
            if(questionRecord.getQuestion_type().equals("选择题"))
            {
                if(questionRecord.getAnswer().equals(questionRecord.getRight_answer()))
                    sum = sum.add(choice_score);
            }

            // 计算选择题得分
            if(questionRecord.getQuestion_type().equals("判断题"))
            {
                if(questionRecord.getAnswer().equals(questionRecord.getRight_answer()))
                    sum = sum.add(judge_score);
            }

            // 计算选择题得分
            if(questionRecord.getQuestion_type().equals("填空题"))
            {
                if(questionRecord.getAnswer().equals(questionRecord.getRight_answer()))
                    sum = sum.add(text_score);
            }
        }

        System.out.println(sum);
        return sum;
    }

    @Override
    public BigDecimal getAverageExamScore(int user_id, int course_id)
    {
        // 找出对应课程的所有考试
        QueryWrapper<Exam> examQueryWrapper = new QueryWrapper<>();
        examQueryWrapper.eq("course_id", course_id);
        List<Exam> examList = examMapper.selectList(examQueryWrapper);

        // 课程没有考试直接返回空
        if(examList.isEmpty())
            return null;

        BigDecimal sum = new BigDecimal("0");
        BigDecimal exam_count = new BigDecimal("0");

        for(Exam exam : examList)
        {
            QueryWrapper<ExamScore> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("exam_id", exam.getExam_id());
            // 获取成绩项
            ExamScore targetExam = examScoreMapper.selectOne(queryWrapper);
            // 如果已经考完
            if(targetExam != null)
            {
                exam_count = exam_count.add(new BigDecimal("1"));
                sum = sum.add(targetExam.getScore());
            }
        }

        // 至少一门有成绩
        if(sum.compareTo(new BigDecimal("0")) > 0)
        {
            // 返回结果
            return sum.divide(exam_count, RoundingMode.HALF_UP);
        }

        // 返回空
        return null;
    }


    // 辅助函数，随机选取试题
    private List<QuestionBank> getRandomQuestion(String question_type, String tags, int num) {
        // 构建查询条件
        QueryWrapper<QuestionBank> queryWrapper = new QueryWrapper<>();

        if (question_type != null && !question_type.isEmpty()) {
            queryWrapper.eq("question_type", question_type);  // 根据题目类型过滤
        }

        if (tags != null && !tags.isEmpty()) {
            queryWrapper.like("tag", tags);  // 根据标签过滤
        }

        // 查询符合条件的题目
        List<QuestionBank> questionList = bankMapper.selectList(queryWrapper);

        // 如果查询出的题目数量少于需要的数量，返回创建失败
        if (questionList.size() <= num) {
            return null;
        }

        // 随机选取题目
        Collections.shuffle(questionList);  // 打乱顺序
        return questionList.subList(0, num);  // 返回前 num 个题目
    }

    // 辅助函数，收到请求时对请求的考试状态进行访问，并根据时间设置其状态
    private Exam checkExamStatus(int exam_id)
    {
        QueryWrapper<Exam> queryWrapper_i = new QueryWrapper<>();
        queryWrapper_i.eq("exam_id", exam_id);
        Exam exam = examMapper.selectOne(queryWrapper_i);
        // 如果考试还未发布则返回空
        if(exam.getStatus().equals("3"))
            return null;

        Date current_time = new Date();
        UpdateWrapper<Exam> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("exam_id", exam_id)
                .ge("start_time", current_time)
                .le("end_time", current_time)
                .set("status", "1");
        examMapper.update(updateWrapper);
        updateWrapper.clear();

        updateWrapper.eq("exam_id", exam_id)
                .le("start_time", current_time)
                .set("status", "0");
        examMapper.update(updateWrapper);
        updateWrapper.clear();

        // 设置状态已结束
        updateWrapper.eq("exam_id", exam_id)
                .ge("end_time", current_time)
                .set("status", "2");
        examMapper.update(updateWrapper);
        updateWrapper.clear();

        QueryWrapper<Exam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("exam_id", exam_id);
        return examMapper.selectOne(queryWrapper);
    }
}
