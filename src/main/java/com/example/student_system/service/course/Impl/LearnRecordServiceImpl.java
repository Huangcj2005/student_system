package com.example.student_system.service.course.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import com.example.student_system.domain.dto.course.LearnRecordInsertDTO;
import com.example.student_system.domain.dto.course.LearnRecordUpdateDTO;
import com.example.student_system.domain.entity.course.LearnRecord;
import com.example.student_system.domain.vo.course.LearnRecordVo;
import com.example.student_system.mapper.course.LearnRecordMapper;
import com.example.student_system.service.course.LearnRecordService;
import com.example.student_system.util.UserContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("LearnRecordService")
public class LearnRecordServiceImpl implements LearnRecordService {
    @Autowired
    private LearnRecordMapper learnRecordMapper;
    @Override
    public CommonResponse<String> insertLearnRecord(LearnRecordInsertDTO dto) {
        LearnRecord learnRecord=new LearnRecord();
        BeanUtils.copyProperties(dto, learnRecord);
        learnRecord.setCreate_time(new Date());
        learnRecord.setUpdate_time(new Date());
        learnRecordMapper.insert(learnRecord);

        return CommonResponse.createForSuccess(
                ResponseCode.LEARN_RECORD_INSERT_SUCCESS.getCode(),
                ResponseCode.LEARN_RECORD_INSERT_SUCCESS.getDescription()
        );
    }

    @Override
    public CommonResponse<String> updateLearnRecord(int course_id, String chapter_id, LearnRecordUpdateDTO dto) {

        UpdateWrapper<LearnRecord> wrapper = new UpdateWrapper<>();
        wrapper.eq("course_id", course_id)
                .eq("chapter_id",chapter_id)
                        .set("progress",dto.getProgress())
                                .set("study_time",dto.getStudy_time())
                                        .set("update_time",new Date());
        learnRecordMapper.update(wrapper);

        return CommonResponse.createForSuccess(
                ResponseCode.LEARN_RECORD_UPDATE_SUCCESS.getCode(),
                ResponseCode.LEARN_RECORD_UPDATE_SUCCESS.getDescription()
        );
    }

    @Override
    public CommonResponse<LearnRecordVo> getLearnRecord(int user_id,int course_id,String chapter_id) {
        QueryWrapper<LearnRecord> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("course_id",course_id)
                .eq("chapter_id",chapter_id)
                .eq("user_id",user_id);

        LearnRecord learnRecord = learnRecordMapper.selectOne(queryWrapper);
        LearnRecordVo learnRecordVo=new LearnRecordVo();
        BeanUtils.copyProperties(learnRecord, learnRecordVo);

        return CommonResponse.createForSuccess(
                ResponseCode.LEARN_RECORD_FETCH_SUCCESS.getCode(),
                ResponseCode.LEARN_RECORD_FETCH_SUCCESS.getDescription(),
                learnRecordVo
        );
    }

//    @Override
//    public CommonResponse<List<LearnRecordVo>> gerLearnRecordList(int course_id) {
//        QueryWrapper<LearnRecord> queryWrapper=new QueryWrapper<>();
//        queryWrapper.eq("course_id",course_id);
//        List<LearnRecord> learnRecordList=learnRecordMapper.selectList(queryWrapper);
//        List<LearnRecordVo> learnRecordVos = learnRecordList.stream().map(learnRecord -> {
//            LearnRecordVo vo = new LearnRecordVo();
//            BeanUtils.copyProperties(learnRecord, vo);
//            return vo;
//        }).collect(Collectors.toList());
//
//        return CommonResponse.createForSuccess(
//                ResponseCode.LEARN_RECORD_LIST_FETCH_SUCCESS.getCode(),
//                ResponseCode.LEARN_RECORD_LIST_FETCH_SUCCESS.getDescription(),
//                learnRecordVos);
//    }


}
