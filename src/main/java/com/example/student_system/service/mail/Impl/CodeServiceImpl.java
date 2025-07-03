package com.example.student_system.service.mail.Impl;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import com.example.student_system.service.mail.CodeService;
import com.example.student_system.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Service("mailService")
public class CodeServiceImpl implements CodeService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void sendSimpleMail(String email, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    @Override
    public CommonResponse<Integer> validateCode(String email) {
        // 生成6位随机验证码
        int code = (int)((Math.random() * 9 + 1) * 100000);

        // 发送邮件
        String subject = "您的验证码";
        String content = "您的验证码是：" + code + "，请在5分钟内完成验证。";

        // 存入Redis，key为邮箱，value为验证码，5分钟过期
        redisTemplate.opsForValue().set("email:code:" + email, String.valueOf(code), 5, TimeUnit.MINUTES);

        sendSimpleMail(email, subject, content);

        return CommonResponse.createForSuccess(
                ResponseCode.EMAIL_VALIDATECODE_SEND_SUCCESS.getCode(),
                ResponseCode.EMAIL_VALIDATECODE_SEND_SUCCESS.getDescription()
        );
    }

    @Override
    public Integer validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }

        try {
            // 验证token并获取用户ID
            var claims = jwtUtil.verifyToken(token);
            return claims.get("userId").asInt();
        } catch (Exception e) {
            return null;
        }
    }

}
