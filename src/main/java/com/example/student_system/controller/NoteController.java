package com.example.student_system.controller;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.entity.note.Note;
import com.example.student_system.domain.vo.NoteVo;
import com.example.student_system.service.note.NoteService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {
    @Resource
    private NoteService noteService;

    @PostMapping("/insertNote")
    public CommonResponse<Note> recordNote(@RequestBody Note newNote){
        return noteService.recordNote(newNote);
    }

    @GetMapping("/getNote/{user_id}/{course_id}")
    public CommonResponse<List<NoteVo>> getNote(@PathVariable int user_id, @PathVariable int course_id){
        return noteService.getNoteListByUserId(user_id,course_id);
    }

    @PutMapping("/updateNote/{note_id}")
    public CommonResponse<Note> updateNote(@PathVariable int note_id,@RequestBody Note newNote){
        return noteService.updateNoteById(note_id,newNote);
    }

    @DeleteMapping("/deleteNote/{note_id}")
    public CommonResponse<Note> deleteNote(@PathVariable int note_id)
    {
        return noteService.deleteNoteById(note_id);
    }
}
