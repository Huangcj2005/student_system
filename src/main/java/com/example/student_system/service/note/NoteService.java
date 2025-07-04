package com.example.student_system.service.note;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.dto.note.NoteInsertDTO;
import com.example.student_system.domain.dto.note.NoteUpdateDTO;
import com.example.student_system.domain.vo.NoteVo;

import java.util.List;

public interface NoteService {
    CommonResponse<String> insertNote(NoteInsertDTO dto);
    CommonResponse<List<NoteVo>> getNoteListByUserId(int user_id, int course_id);
    CommonResponse<String> updateNoteById(int note_id, NoteUpdateDTO dto);
    CommonResponse<String> deleteNoteById(int note_id);
}
