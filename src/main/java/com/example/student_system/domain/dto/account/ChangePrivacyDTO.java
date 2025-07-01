package com.example.student_system.domain.dto.account;

import lombok.Data;

@Data
public class ChangePrivacyDTO {
    private Integer courseLearningVisible;
    private Integer courseLikeVisible;
    private Integer scoreVisible;
}
