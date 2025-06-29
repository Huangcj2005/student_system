package com.example.student_system.service.note;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.entity.note.Note;

import java.util.List;

public interface NoteService {
    public CommonResponse<Note> recordNote(Note note);
    public CommonResponse<List<Note>> getNoteListByUserId(int user_id,int course_id);
//    public CommonResponse<Note> getNoteById(int note_id);
    public CommonResponse<Note> deleteNoteById(int note_id);
    public CommonResponse<Note> updateNoteById(int note_id,Note note);

}
