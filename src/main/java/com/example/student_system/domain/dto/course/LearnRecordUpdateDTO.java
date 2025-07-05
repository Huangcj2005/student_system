package com.example.student_system.domain.dto.course;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LearnRecordUpdateDTO {
    private BigDecimal progress;
    private BigDecimal study_time;
}
