package com.example.student_system.util;

import com.example.student_system.domain.dto.account.ChangeUserInfoDTO;
import com.example.student_system.domain.dto.account.RegisterRequest;
import com.example.student_system.domain.entity.account.User;
import com.example.student_system.domain.dto.account.UserInfo;
import lombok.Data;

import java.util.Date;


@Data
public class AccountUtil {
    public static UserInfo UserToInfo(User user){
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(user.getUserName());
        userInfo.setProfile(user.getProfile());
        userInfo.setPhoto(user.getPhoto());
        userInfo.setEmail(user.getEmail());
        userInfo.setRole(user.getRole());
        userInfo.setSex(user.getSex());

        return userInfo;
    }

    public static User RegisterToUser(RegisterRequest registerRequest,String pwd,String url){
        User user = new User();
        user.setUserName(registerRequest.getUserName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(pwd);
        user.setSex(registerRequest.getSex());
        user.setProfile(registerRequest.getProfile());
        user.setPhoto(url);
        user.setRole("student"); // 默认角色为学生
        user.setStatus(1); // 默认状态为启用
        user.setCreateTime(new Date(System.currentTimeMillis()));
        user.setUpdateTime(user.getCreateTime());

        return user;
    }

    public static User InfoToUser(ChangeUserInfoDTO userInfoDTO,User user){
        user.setUserName(userInfoDTO.getUserName());
        user.setRole(userInfoDTO.getRole());
        user.setProfile(userInfoDTO.getProfile());
        user.setUpdateTime(new Date(System.currentTimeMillis()));

        return user;
    }
}
