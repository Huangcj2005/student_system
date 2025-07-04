package com.example.student_system.service.note.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import com.example.student_system.domain.dto.note.NoteInsertDTO;
import com.example.student_system.domain.dto.note.NoteUpdateDTO;
import com.example.student_system.domain.entity.course.LearnRecord;
import com.example.student_system.domain.entity.note.Note;
import com.example.student_system.domain.vo.note.NoteVo;
import com.example.student_system.mapper.note.NoteMapper;
import com.example.student_system.service.note.NoteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("NoteService")
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteMapper noteMapper;

    @Override
    public CommonResponse<String> insertNote(NoteInsertDTO dto) {
        Note note = new Note();
        BeanUtils.copyProperties(dto, note);
        note.setCreate_time(new Date());
        note.setUpdate_time(new Date());
        note.setNote_id(UUID.randomUUID().toString());

        int result = noteMapper.insert(note);
        if (result > 0) {
            return CommonResponse.createForSuccess(
                    ResponseCode.NOTE_ASSIGN_SUCCESS.getCode(),
                    ResponseCode.NOTE_ASSIGN_SUCCESS.getDescription()
            );
        } else {
            return CommonResponse.createForError(
                    ResponseCode.NOTE_ASSIGN_FAIL.getCode(),
                    ResponseCode.NOTE_ASSIGN_FAIL.getDescription()
            );
        }
    }

    @Override
    public CommonResponse<List<NoteVo>> getNoteListByUserId(int user_id, int course_id) {
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id).eq("course_id", course_id);

        List<Note> noteList = noteMapper.selectList(queryWrapper);
        List<NoteVo> noteVoList = noteList.stream().map(note -> {
            NoteVo vo = new NoteVo();
            BeanUtils.copyProperties(note, vo);
            return vo;
        }).collect(Collectors.toList());

        if (!noteVoList.isEmpty()) {
            return CommonResponse.createForSuccess(
                    ResponseCode.NOTE_LIST_FETCH_SUCCESS.getCode(),
                    ResponseCode.NOTE_LIST_FETCH_SUCCESS.getDescription(),
                    noteVoList
            );
        } else {
            return CommonResponse.createForSuccess(
                    ResponseCode.NOTE_FETCH_FAIL.getCode(),
                    ResponseCode.NOTE_FETCH_FAIL.getDescription()
            );
        }
    }

    @Override
    public CommonResponse<String> updateNoteById(String note_id, NoteUpdateDTO dto) {
        UpdateWrapper<Note> wrapper = new UpdateWrapper<>();
        wrapper.eq("note_id", note_id)
                .set("content",dto.getNote_content())
                .set("update_time",new Date());
        noteMapper.update(wrapper);

        return CommonResponse.createForSuccess(
                    ResponseCode.NOTE_UPDATE_SUCCESS.getCode(),
                    ResponseCode.NOTE_UPDATE_SUCCESS.getDescription()
            );
    }

    @Override
    public CommonResponse<String> deleteNoteById(String note_id) {
        QueryWrapper<Note> wrapper = new QueryWrapper<>();
        wrapper.eq("note_id", note_id);
        int result = noteMapper.delete(wrapper);

        if (result > 0) {
            return CommonResponse.createForSuccess(
                    ResponseCode.NOTE_DELETE_SUCCESS.getCode(),
                    ResponseCode.NOTE_DELETE_SUCCESS.getDescription()
            );
        } else {
            return CommonResponse.createForError(
                    ResponseCode.NOTE_DELETE_FAIL.getCode(),
                    ResponseCode.NOTE_DELETE_FAIL.getDescription()
            );
        }
    }
}

