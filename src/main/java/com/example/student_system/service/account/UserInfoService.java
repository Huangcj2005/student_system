package com.example.student_system.service.account;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.domain.dto.account.ChangePrivacyDTO;
import com.example.student_system.domain.dto.account.ChangeUserInfoDTO;
import com.example.student_system.domain.dto.account.UserInfo;
import com.example.student_system.domain.entity.account.UserPrivacy;

public interface UserInfoService {
    /**
     * 根据用户 ID 获取用户信息
     * @param userId 用户 ID
     * @return 用户信息
     */
    CommonResponse<UserInfo> getUserInfo(int userId);

    /**
     * 根据用户 ID 更新用户信息
     * @param userInfo 用户信息
     * @param userId 用户 ID
     */
    CommonResponse<String> updateUserInfo(ChangeUserInfoDTO userInfo,int userId);

    /**
     * 根据用户 ID 更新密码
     * @param userId 用户 ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    CommonResponse<String> updatePassword(int userId, String oldPassword, String newPassword);

    /**
     * 根据用户 ID 获取用户隐私设置对象
     * @param userId 用户 ID
     * @return 用户隐私设置对象
     */
    CommonResponse<ChangePrivacyDTO> getUserPrivacy(int userId);

    /**
     * 根据用户 ID 更新用户隐私设置
     * @param userId 用户 ID
     * @param privacyDTO 用户隐私对象
     */
    CommonResponse<String> updatePrivacy(int userId, ChangePrivacyDTO privacyDTO);

}
