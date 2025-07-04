package com.example.student_system.service.course.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import com.example.student_system.domain.dto.course.KnowledgeBankDTO;
import com.example.student_system.domain.entity.course.KnowledgeBank;
import com.example.student_system.domain.vo.KnowledgeBankVo;
import com.example.student_system.mapper.course.KnowledgeBankMapper;
import com.example.student_system.service.course.KnowledgeBankService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("KnowledgeBankService")
public class KnowledgeBankServiceImpl implements KnowledgeBankService
{
    @Autowired
    private KnowledgeBankMapper knowledgeBankMapper;
    @Override
    public CommonResponse<String> addKnowledgeBank(KnowledgeBankDTO newKnowledgeBankDTO) {
        KnowledgeBank knowledgeBank=new KnowledgeBank();
        BeanUtils.copyProperties(newKnowledgeBankDTO, knowledgeBank);
        knowledgeBank.setCreate_time(new Date());
        knowledgeBank.setUpdate_time(new Date());

        knowledgeBankMapper.insert(knowledgeBank);
        return CommonResponse.createForSuccess(
                ResponseCode.KNOWLEDGEBANK_ADD_SUCCESS.getCode(),
                ResponseCode.KNOWLEDGEBANK_ADD_SUCCESS.getDescription()
        );
    }

    @Override
    public CommonResponse<List<KnowledgeBankVo>> getKnowledgeBankByCourseId(int course_id) {
        QueryWrapper<KnowledgeBank> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("course_id",course_id);
        List<KnowledgeBank> knowledgeBanks=knowledgeBankMapper.selectList(queryWrapper);
        List<KnowledgeBankVo> knowledgeBankVoList=knowledgeBanks.stream().map(
                knowledgeBank -> {
                    KnowledgeBankVo vo=new KnowledgeBankVo();
                    vo.setCourse_id(knowledgeBank.getCourse_id());
                    vo.setChapter_id(knowledgeBank.getChapter_id());
                    vo.setChapter_detail(knowledgeBank.getChapter_detail());
                    vo.setCourse_name(knowledgeBank.getCourse_name());
                    vo.setChapter_name(knowledgeBank.getChapter_name());
                    vo.setCourse_video_url(knowledgeBank.getCourse_video_url());
                    vo.setCourseware_url(knowledgeBank.getCourseware_url());
                    return vo;
                }
        ).collect(Collectors.toList());


        if(!knowledgeBankVoList.isEmpty())
        {
            return CommonResponse.createForSuccess(
                    ResponseCode.KNOWLEDGEBANK_FETCH_SUCCESS.getCode(),
                    ResponseCode.KNOWLEDGEBANK_FETCH_SUCCESS.getDescription(),
                    knowledgeBankVoList
            );
        }
        else
        {
            return CommonResponse.createForError(
                    ResponseCode.KNOWLEDGEBANK_FETCH_FAIL.getCode(),
                    ResponseCode.KNOWLEDGEBANK_FETCH_FAIL.getDescription()
            );
        }

    }
}
