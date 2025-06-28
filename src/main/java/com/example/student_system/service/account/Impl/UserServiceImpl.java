package com.example.student_system.service.account.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import com.example.student_system.domain.dto.account.LoginRequest;
import com.example.student_system.domain.dto.account.RegisterRequest;
import com.example.student_system.domain.entity.account.User;
import com.example.student_system.domain.dto.account.UserInfo;
import com.example.student_system.domain.vo.LoginResponse;
import com.example.student_system.mapper.account.UserMapper;
import com.example.student_system.service.account.MailService;
import com.example.student_system.service.account.UserService;
import com.example.student_system.util.AccountUtil;
import com.example.student_system.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private MailService mailService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();  // 密码解密
    // TODO：此处默认头像路径后续需要更改，或者使用config进行配置
    final static String defaultAvatar = "src\\images\\userPic.png";

    @Override
    public CommonResponse<LoginResponse> login(LoginRequest loginRequest) {
        // 根据邮箱查询用户
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("email", loginRequest.getEmail());
        User user = userMapper.selectOne(userQueryWrapper);

        // 账号存在性检验
        if (user == null) {
            return CommonResponse.createForError(
                    ResponseCode.ACCOUNT_INVALID.getCode(),
                    ResponseCode.ACCOUNT_INVALID.getDescription()
            );
        }

        // 验证密码
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return CommonResponse.createForError(
                    ResponseCode.PASSWORD_ERROR.getCode(),
                    ResponseCode.PASSWORD_ERROR.getDescription()
            );
        }

        // 检查用户状态
        if (user.getStatus() != 1) {
            return CommonResponse.createForError(
                    ResponseCode.ACCOUNT_INVALID.getCode(),
                    ResponseCode.ACCOUNT_INVALID.getDescription()
            );
        }

        // 生成JWT token
        String token = jwtUtil.generateToken(user);

        // 构建响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);

        response.setUserInfo(AccountUtil.UserToInfo(user));
        
        return CommonResponse.createForSuccess(
                ResponseCode.USER_LOGIN_SUCCESS.getCode(),
                ResponseCode.USER_LOGIN_SUCCESS.getDescription(),
                response
        );
    }

    @Override
    public CommonResponse<String> register(RegisterRequest registerRequest) {
        // 检查邮箱是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", registerRequest.getEmail());
        User existingUser = userMapper.selectOne(queryWrapper);
        
        if (existingUser != null) {
            return CommonResponse.createForError(
                    ResponseCode.EMAIL_ALREADY_USED.getCode(),
                    ResponseCode.EMAIL_ALREADY_USED.getDescription()
            );
        }
        
        // 检查用户名是否已存在
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", registerRequest.getUserName());
        existingUser = userMapper.selectOne(queryWrapper);
        
        if (existingUser != null) {
            return CommonResponse.createForError(
                    ResponseCode.USERNAME_ALREADY_USED.getCode(),
                    ResponseCode.USERNAME_ALREADY_USED.getDescription()
            );
        }

        // 验证码校验
        String email = registerRequest.getEmail();
        String code = registerRequest.getCode();
        String redisCode = redisTemplate.opsForValue().get("email:code:" + email);
        if (redisCode == null) {
            return CommonResponse.createForError(
                    ResponseCode.VALIDATECODE_INVALID.getCode(),
                    ResponseCode.VALIDATECODE_INVALID.getDescription()
            );
        }
        if (!redisCode.equals(code)) {
            return CommonResponse.createForError(
                    ResponseCode.VALIDATECODE_ERROR.getCode(),
                    ResponseCode.VALIDATECODE_ERROR.getDescription()
            );
        }
        // 验证通过后删除验证码
        redisTemplate.delete("email:code:" + email);

        
        // 创建新用户
        User user = AccountUtil.RegisterToUser(
                registerRequest,
                passwordEncoder.encode(registerRequest.getPassword()),
                defaultAvatar);
        
        // 生成用户ID（这里简单使用时间戳，实际项目中可能需要更复杂的逻辑）
        user.setUserId((int) (System.currentTimeMillis() % 1000000));
        
        userMapper.insert(user);
        return CommonResponse.createForSuccess(
                ResponseCode.USER_REGISTER_SUCCESS.getCode(),
                ResponseCode.USER_REGISTER_SUCCESS.getDescription()
        );
    }

    @Override
    public CommonResponse<UserInfo> getUserInfo(int userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        User user = userMapper.selectOne(queryWrapper);
        
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

    @Override
    public CommonResponse<Integer> validateCode(String email) {
        // 生成6位随机验证码
        int code = (int)((Math.random() * 9 + 1) * 100000);

        // 发送邮件
        String subject = "您的验证码";
        String content = "您的验证码是：" + code + "，请在5分钟内完成验证。";

        // 存入Redis，key为邮箱，value为验证码，5分钟过期
        redisTemplate.opsForValue().set("email:code:" + email, String.valueOf(code), 5, TimeUnit.MINUTES);

        mailService.sendSimpleMail(email, subject, content);

        // TODO: 返回验证码（仅用于开发测试，生产环境不要返回）
        return CommonResponse.createForSuccess(
                ResponseCode.EMAIL_VALIDATECODE_SEND_SUCCESS.getCode(),
                ResponseCode.EMAIL_VALIDATECODE_SEND_SUCCESS.getDescription(),
                code
        );
    }
}
