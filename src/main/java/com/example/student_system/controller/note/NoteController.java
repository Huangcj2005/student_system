package com.example.student_system.controller.note;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.dto.note.NoteInsertDTO;
import com.example.student_system.domain.dto.note.NoteUpdateDTO;
import com.example.student_system.domain.vo.note.NoteVo;
import com.example.student_system.service.note.NoteService;
import com.example.student_system.util.UserContext;
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
        dto.setUser_id(UserContext.getCurrentUserId());
        return noteService.insertNote(dto);
    }

    @GetMapping("/course/{course_id}")
    public CommonResponse<List<NoteVo>> getNotesByUserAndCourse(
            @PathVariable int course_id) {
        Integer user_id=UserContext.getCurrentUserId();
        return noteService.getNoteListByUserId(user_id, course_id);
    }

    @PutMapping("/update/{note_id}")
    public CommonResponse<String> updateNote(
            @PathVariable String note_id,
            @RequestBody NoteUpdateDTO dto) {
        return noteService.updateNoteById(note_id, dto);
    }

    @DeleteMapping("/delete/{note_id}")
    public CommonResponse<String> deleteNote(@PathVariable String note_id) {
        return noteService.deleteNoteById(note_id);
    }
}
