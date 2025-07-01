package com.example.student_system.service.mail;

import com.example.student_system.common.CommonResponse;

public interface CodeService {
    public void sendSimpleMail(String email, String subject, String content);

    /**
     * 用于实现邮箱验证码功能
     * @return
     */
    CommonResponse<Integer> validateCode(String email);

    /**
     * 验证token是否有效
     * @param token JWT token
     * @return 用户ID，如果无效返回null
     */
    Integer validateToken(String token);
}
