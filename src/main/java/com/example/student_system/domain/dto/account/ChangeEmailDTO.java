package com.example.student_system.domain.dto.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeEmailDTO {
    @NotBlank(message = "旧邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String oldEmail;
    @NotBlank(message = "新邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String newEmail;
    @NotBlank(message = "验证码不能为空")
    private String code;
}
