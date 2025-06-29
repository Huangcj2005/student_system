package com.example.student_system.service.note.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import com.example.student_system.domain.entity.note.Note;
import com.example.student_system.mapper.NoteMapper;
import com.example.student_system.service.note.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public CommonResponse<List<Note>> getNoteListByUserId(int user_id,int course_id) {
        QueryWrapper<Note> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",user_id)
                .eq("course_id",course_id);
        List<Note> noteList=noteMapper.selectList(queryWrapper);

        if (noteList.size()>0)
        {
            return CommonResponse.createForSuccess(
                    ResponseCode.NOTE_LIST_FETCH_SUCCESS.getCode(),
                    ResponseCode.NOTE_LIST_FETCH_SUCCESS.getDescription(),
                    noteList
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
