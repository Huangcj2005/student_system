package com.example.student_system.service.note.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import com.example.student_system.domain.entity.note.Note;
import com.example.student_system.domain.vo.NoteVo;
import com.example.student_system.mapper.NoteMapper;
import com.example.student_system.service.note.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("NoteService")
public class NoteServiceImpl implements NoteService {
    @Autowired
    private NoteMapper noteMapper;
    @Override
    public CommonResponse<Note> recordNote(Note note) {
        noteMapper.insert(note);
        return CommonResponse.createForSuccess(
                ResponseCode.NOTE_ASSIGN_SUCCESS.getCode(),
                ResponseCode.NOTE_ASSIGN_SUCCESS.getDescription()
                );
    }

    @Override
    public CommonResponse<List<NoteVo>> getNoteListByUserId(int user_id, int course_id) {
        QueryWrapper<Note> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",user_id)
                .eq("course_id",course_id);
        List<Note> noteList=noteMapper.selectList(queryWrapper);
        List<NoteVo> noteVoList=noteList.stream().map(
                note -> {
                    NoteVo vo=new NoteVo();
                    vo.setNote_content(note.getNote_content());
                    vo.setChapter_name(note.getChapter_name());
                    vo.setChapter_id(note.getChapter_id());
                    vo.setCourse_id(note.getCourse_id());
                    vo.setCourse_name(note.getCourse_name());
                    return vo;
                }
        ).collect(Collectors.toList());
        if (noteVoList.size()>0)
        {
            return CommonResponse.createForSuccess(
                    ResponseCode.NOTE_LIST_FETCH_SUCCESS.getCode(),
                    ResponseCode.NOTE_LIST_FETCH_SUCCESS.getDescription(),
                    noteVoList
            );
        }
        else {
            return CommonResponse.createForSuccess(
                    ResponseCode.NOTE_FETCH_FAIL.getCode(),
                    ResponseCode.NOTE_FETCH_FAIL.getDescription()
            );
        }

    }

//    @Override
//    public CommonResponse<Note> getNoteById(int note_id) {
//        QueryWrapper<Note> queryWrapper=new QueryWrapper<>();
//        queryWrapper.eq("note_id",note_id);
//        Note note=noteMapper.selectOne(queryWrapper);
//
//
//        return CommonResponse.createForSuccess(
//                ResponseCode.NOTE_FETCH_SUCCESS.getCode(),
//                ResponseCode.NOTE_FETCH_SUCCESS.getDescription(),
//                note
//        );
//    }

    @Override
    public CommonResponse<Note> deleteNoteById(int note_id) {
        QueryWrapper<Note> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("note_id",note_id);
        int result = noteMapper.delete(queryWrapper);

        if (result > 0) {
            // 删除成功
            return CommonResponse.createForSuccess(
                    ResponseCode.NOTE_DELETE_SUCCESS.getCode(),
                    ResponseCode.NOTE_DELETE_SUCCESS.getDescription()
            );
        } else {
            // 删除失败（比如没有该 ID）
            return CommonResponse.createForError(
                    ResponseCode.NOTE_DELETE_FAIL.getCode(),
                    ResponseCode.NOTE_DELETE_FAIL.getDescription()
            );
        }

    }

    @Override
    public CommonResponse<Note> updateNoteById(int note_id,Note newNote) {
        QueryWrapper<Note> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("note_id",note_id);
        noteMapper.update(newNote,queryWrapper);

        return CommonResponse.createForSuccess(
                ResponseCode.NOTE_UPDATE_SUCCESS.getCode(),
                ResponseCode.NOTE_UPDATE_SUCCESS.getDescription()
        );
    }

}
