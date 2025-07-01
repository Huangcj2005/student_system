package com.example.student_system.service.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import com.example.student_system.domain.entity.task.Exam;
import com.example.student_system.domain.entity.task.ExamRecord;
import com.example.student_system.domain.entity.task.QuestionBank;
import com.example.student_system.domain.entity.task.QuestionRecord;
import com.example.student_system.domain.vo.task.QuestionVO;
import com.example.student_system.mapper.task.ExamMapper;
import com.example.student_system.mapper.task.ExamRecordMapper;
import com.example.student_system.mapper.task.QuestionBankMapper;
import com.example.student_system.mapper.task.QuestionRecordMapper;
import com.example.student_system.service.task.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    @Override
    public CommonResponse<List<QuestionVO>> generatePaper( int exam_id,int choice_num, int judge_num,int text_num, String tags, boolean isInsert)
    {
        List<QuestionBank> question_list = new ArrayList<>();
        List<QuestionBank> choice_list = getRandomQuestion("选择题", tags, choice_num);
        List<QuestionBank> judge_list = getRandomQuestion("判断题", tags, judge_num);
        List<QuestionBank> text_list = getRandomQuestion("简答题", tags, text_num);

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

        ExamRecord examRecord = new ExamRecord();
        examRecord.setExam_id(exam_id);
        for(QuestionBank question : question_list)
        {
            QuestionVO questionVO = new QuestionVO();
            questionVO.setQuestion_id(id_count);
            questionVO.setQuestion_type(question.getQuestion_type());
            questionVO.setQuestion_content(question.getQuestion_content());
            questionVOList.add(questionVO);

            examRecord.setQuestion_id(id_count);
            examRecord.setQuestion_type(question.getQuestion_type());
            examRecord.setQuestion_content(question.getQuestion_content());
            examRecord.setRight_answer(question.getQuestion_answer());
            examRecord.setCreate_time(new Date());

            id_count++;
        }

        // 将生成的问题插入至数据库
        examRecordMapper.insert(examRecord);

        return CommonResponse.createForSuccess(
                ResponseCode.PAPER_GENERATE_SUCCESS.getCode(),
                ResponseCode.PAPER_GENERATE_SUCCESS.getDescription(),
                questionVOList
        );
    }

    public CommonResponse<Exam> createExam(Exam exam)
    {
        examMapper.insert(exam);
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
        return CommonResponse.createForSuccess(
                ResponseCode.EXAM_LIST_FETCH_SUCCESS.getCode(),
                ResponseCode.EXAM_CREATE_SUCCESS.getDescription(),
                examList
        );
    }

    @Override
    public CommonResponse<List<Exam>> getUserExamList(int[] course_id_list)
    {
        QueryWrapper<Exam> queryWrapper = new QueryWrapper<>();
        List<Exam> newExamList = new ArrayList<>();
        for(int id : course_id_list)
        {
            queryWrapper.eq("course_id", id);
            List<Exam> examList = examMapper.selectList(queryWrapper);
            newExamList.addAll(examList);
            queryWrapper.clear();
        }

        return CommonResponse.createForSuccess(
                ResponseCode.USER_EXAM_LIST_FETCH_SUCCESS.getCode(),
                ResponseCode.USER_REGISTER_SUCCESS.getDescription(),
                newExamList
        );
    }

    @Override
    public CommonResponse<QuestionRecord> saveOrUpdateQuestionRecord(QuestionRecord questionRecord)
    {
        // 查询对应的答题记录
        QueryWrapper<QuestionRecord> queryWrapper = new QueryWrapper<>();

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
}
