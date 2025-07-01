package com.example.student_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import com.example.student_system.domain.entity.note.Note;

@Repository
public interface NoteMapper extends BaseMapper<Note> {
}
