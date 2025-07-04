package com.example.student_system.domain.dto.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeUserInfoDTO {
    @NotBlank(message = "用户名不能为空")
    private String userName;
    @NotBlank(message = "请选择性别")
    private String sex;
    private String role;
    private String profile;
}
