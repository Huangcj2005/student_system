package com.example.student_system.service.account.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import com.example.student_system.domain.dto.account.ChangePrivacyDTO;
import com.example.student_system.domain.dto.account.ChangeUserInfoDTO;
import com.example.student_system.domain.dto.account.UserInfo;
import com.example.student_system.domain.entity.account.User;
import com.example.student_system.domain.entity.account.UserPrivacy;
import com.example.student_system.mapper.account.UserMapper;
import com.example.student_system.mapper.account.UserPrivacyMapper;
import com.example.student_system.service.account.UserInfoService;
import com.example.student_system.util.AccountUtil;
import com.example.student_system.util.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserPrivacyMapper userPrivacyMapper;

    @Override
    public CommonResponse<UserInfo> getUserInfo(int userId) {
        User user = QueryUtil.getUserById(userMapper, userId);

        if (user == null) {
            return CommonResponse.createForError(
                    ResponseCode.ERROR.getCode(),
                    "数据库内部错误"
            );
        }

        return CommonResponse.createForSuccess(
                ResponseCode.USER_INFO_GET_SUCCESS.getCode(),
                ResponseCode.USER_INFO_GET_SUCCESS.getDescription(),
                AccountUtil.UserToInfo(user)
        );
    }

    public CommonResponse<String> updateUserInfo(ChangeUserInfoDTO userInfo,int userId){
        User existingUser = QueryUtil.getUserById(userMapper, userId);
        User updatedUser = AccountUtil.InfoToUser(userInfo, existingUser);

        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("user_id",userId);
        userMapper.update(updatedUser,userUpdateWrapper);

        return CommonResponse.createForSuccess(
                ResponseCode.USER_INFO_UPDATE_SUCCESS.getCode(),
                ResponseCode.USER_INFO_UPDATE_SUCCESS.getDescription()
        );
    }

    public CommonResponse<String> updatePassword(int userId, String oldPassword, String newPassword){
        User user = QueryUtil.getUserById(userMapper, userId);

        if(user == null){
            return CommonResponse.createForError(
                    ResponseCode.ERROR.getCode(),
                    "用户不存在"
            );
        }

        // 密码解密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(!passwordEncoder.matches(oldPassword, user.getPassword())){
            return CommonResponse.createForError(
                    ResponseCode.PASSWORD_ERROR.getCode(),
                    ResponseCode.PASSWORD_ERROR.getDescription()
            );
        }

        // 对密码进行加密
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateTime(new Date(System.currentTimeMillis()));

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId);
        userMapper.update(user, updateWrapper);

        return CommonResponse.createForSuccess(
                ResponseCode.USER_PASSWORD_UPDATE_SUCCESS.getCode(),
                ResponseCode.USER_PASSWORD_UPDATE_SUCCESS.getDescription()
        );
    }

    public CommonResponse<ChangePrivacyDTO> getUserPrivacy(int userId){
        UserPrivacy userPrivacy = QueryUtil.getUserPrivacyById(userPrivacyMapper, userId);

        if(userPrivacy == null){
            return CommonResponse.createForError(
                    ResponseCode.ERROR.getCode(),
                    "数据库内部错误"
            );
        }

        ChangePrivacyDTO privacyDTO = new ChangePrivacyDTO();
        privacyDTO.setCourseLearningVisible(userPrivacy.getCourseLearningVisible());
        privacyDTO.setCourseLikeVisible(userPrivacy.getCourseLikeVisible());
        privacyDTO.setScoreVisible(userPrivacy.getScoreVisible());

        return CommonResponse.createForSuccess(
                ResponseCode.USER_PRIVACY_FETCH_SUCCESS.getCode(),
                ResponseCode.USER_PRIVACY_FETCH_SUCCESS.getDescription(),
                privacyDTO
        );
    }


    public CommonResponse<String> updatePrivacy(int userId, ChangePrivacyDTO privacyDTO){
        UserPrivacy userPrivacy = QueryUtil.getUserPrivacyById(userPrivacyMapper, userId);

        userPrivacy.setCourseLearningVisible(privacyDTO.getCourseLearningVisible());
        userPrivacy.setCourseLikeVisible(privacyDTO.getCourseLikeVisible());
        userPrivacy.setScoreVisible(privacyDTO.getScoreVisible());
        userPrivacy.setUpdateTime(new Date(System.currentTimeMillis()));

        UpdateWrapper<UserPrivacy> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId);
        userPrivacyMapper.update(userPrivacy, updateWrapper);

        return CommonResponse.createForSuccess(
                ResponseCode.USER_PRIVACY_UPDATE_SUCCESS.getCode(),
                ResponseCode.USER_PRIVACY_UPDATE_SUCCESS.getDescription()
        );
    }
}
