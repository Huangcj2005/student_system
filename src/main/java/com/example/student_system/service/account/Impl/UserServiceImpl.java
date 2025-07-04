package com.example.student_system.service.account.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import com.example.student_system.domain.dto.account.LoginRequest;
import com.example.student_system.domain.dto.account.RegisterRequest;
import com.example.student_system.domain.entity.account.User;
import com.example.student_system.domain.dto.account.UserInfo;
import com.example.student_system.domain.entity.account.UserPrivacy;
import com.example.student_system.domain.vo.LoginResponse;
import com.example.student_system.mapper.account.UserMapper;
import com.example.student_system.mapper.account.UserPrivacyMapper;
import com.example.student_system.service.mail.CodeService;
import com.example.student_system.service.account.UserService;
import com.example.student_system.util.AccountUtil;
import com.example.student_system.util.JwtUtil;
import com.example.student_system.util.UserContext;
import com.example.student_system.util.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserPrivacyMapper userPrivacyMapper;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();  // 密码解密
    // TODO：此处默认头像路径后续需要更改，或者使用config进行配置
    final static String defaultAvatar = "src/main/resources/static/avatar/userPic.png";

    @Override
    public CommonResponse<LoginResponse> login(LoginRequest loginRequest) {
        // 根据邮箱查询用户
        User user = QueryUtil.getUserByEmail(userMapper, loginRequest.getEmail());

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
        
        // 设置用户上下文，让@LogAction注解能够获取到用户ID
        UserContext.setCurrentUserId(user.getUserId());
        
        return CommonResponse.createForSuccess(
                ResponseCode.USER_LOGIN_SUCCESS.getCode(),
                ResponseCode.USER_LOGIN_SUCCESS.getDescription(),
                response
        );
    }

    @Override
    public CommonResponse<String> register(RegisterRequest registerRequest) {
        // 检查邮箱是否已存在
        if (QueryUtil.isEmailExists(userMapper, registerRequest.getEmail())) {
            return CommonResponse.createForError(
                    ResponseCode.EMAIL_ALREADY_USED.getCode(),
                    ResponseCode.EMAIL_ALREADY_USED.getDescription()
            );
        }
        
        // 检查用户名是否已存在
        if (QueryUtil.isUserNameExists(userMapper, registerRequest.getUserName())) {
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

        
        // 创建新用户(已包含创建时间)
        User user = AccountUtil.RegisterToUser(
                registerRequest,
                passwordEncoder.encode(registerRequest.getPassword()),
                defaultAvatar);
        
        // 生成用户ID（这里简单使用时间戳，实际项目中可能需要更复杂的逻辑）
        user.setUserId((int) (System.currentTimeMillis() % 1000000));

        // 设置用户上下文，让@LogAction注解能够获取到用户ID
        UserContext.setCurrentUserId(user.getUserId());
        
        userMapper.insert(user);

        // 创建用户隐私策略
        UserPrivacy userPrivacy = new UserPrivacy(user.getUserId());
        userPrivacy.setCreateTime(new Date(System.currentTimeMillis()));
        userPrivacy.setUpdateTime(userPrivacy.getCreateTime());

        userPrivacyMapper.insert(userPrivacy);

        return CommonResponse.createForSuccess(
                ResponseCode.USER_REGISTER_SUCCESS.getCode(),
                ResponseCode.USER_REGISTER_SUCCESS.getDescription()
        );
    }
}
