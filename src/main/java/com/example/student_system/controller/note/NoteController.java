package com.example.student_system.controller.note;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.dto.note.NoteInsertDTO;
import com.example.student_system.domain.dto.note.NoteUpdateDTO;
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

    @PostMapping("/insert")
    public CommonResponse<String> insertNote(@RequestBody NoteInsertDTO dto) {
        return noteService.insertNote(dto);
    }

    @GetMapping("/user/{user_id}/course/{course_id}")
    public CommonResponse<List<NoteVo>> getNotesByUserAndCourse(
            @PathVariable int user_id,
            @PathVariable int course_id) {
        return noteService.getNoteListByUserId(user_id, course_id);
    }

    @PutMapping("/update/{note_id}")
    public CommonResponse<String> updateNote(
            @PathVariable int note_id,
            @RequestBody NoteUpdateDTO dto) {
        return noteService.updateNoteById(note_id, dto);
    }

    @DeleteMapping("/delete/{note_id}")
    public CommonResponse<String> deleteNote(@PathVariable int note_id) {
        return noteService.deleteNoteById(note_id);
    }
}
