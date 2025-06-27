package com.example.student_system.domain.dto.account;

import lombok.Data;

@Data
public class UserInfo {
    private Integer id;
    private Integer userId;
    private String userName;
    private String email;
    private String role;
    private String sex;
    private String photo;
    private String profile;
}
