package com.example.student_system.controller.account;

import com.example.student_system.annotation.LogAction;
import com.example.student_system.annotation.RateLimit;
import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import com.example.student_system.domain.dto.account.*;
import com.example.student_system.domain.vo.LoginResponse;
import com.example.student_system.service.account.UserInfoService;
import com.example.student_system.service.account.UserService;
import com.example.student_system.service.mail.CodeService;
import com.example.student_system.util.UserContext;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CodeService codeService;
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("login")
    @LogAction("用户登录")
    public CommonResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("register")
    @LogAction("用户注册")
    public CommonResponse<String> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return userService.register(registerRequest);
    }

    @GetMapping("student-info")
    @LogAction("获取用户基本信息")
    public CommonResponse<UserInfo> getUserInfo() {
        Integer userId = UserContext.getCurrentUserId();
        if (userId == null) {
            return CommonResponse.createForError(
                ResponseCode.TOKEN_INVALID.getCode(),
                ResponseCode.TOKEN_INVALID.getDescription()
            );
        }

        return userInfoService.getUserInfo(userId);
    }

    @PostMapping("update/info")
    @LogAction("更新用户基本信息")
    public CommonResponse<String> updateInfo(@Valid @RequestBody ChangeUserInfoDTO userInfoDTO) {
        Integer userId = UserContext.getCurrentUserId();
        if (userId == null) {
            return CommonResponse.createForError(
                ResponseCode.TOKEN_INVALID.getCode(),
                ResponseCode.TOKEN_INVALID.getDescription()
            );
        }

        return userInfoService.updateUserInfo(userInfoDTO,userId);
    }

    @PostMapping("update/photo")
    @LogAction("更新用户头像")
    public CommonResponse<String> updatePhoto(@RequestParam("file") MultipartFile file){
        Integer userId = UserContext.getCurrentUserId();
        if (userId == null) {
            return CommonResponse.createForError(
                    ResponseCode.TOKEN_INVALID.getCode(),
                    ResponseCode.TOKEN_INVALID.getDescription()
            );
        }

        return userInfoService.updateUserPhoto(userId, file);
    }
    
    @PostMapping("update/pwd")
    @LogAction("更新用户密码")
    public CommonResponse<String> updatePwd(@Valid @RequestBody ChangePasswordDTO passwordDTO) {
        Integer userId = UserContext.getCurrentUserId();
        if (userId == null) {
            return CommonResponse.createForError(
                    ResponseCode.TOKEN_INVALID.getCode(),
                    ResponseCode.TOKEN_INVALID.getDescription()
            );
        }

        return userInfoService.updatePassword(userId, passwordDTO.getOldPassword(), passwordDTO.getNewPassword());
    }

    @GetMapping("student-privacy")
    @LogAction("访问用户隐私设置")
    public CommonResponse<ChangePrivacyDTO> getUserPrivacy(){
        Integer userId = UserContext.getCurrentUserId();
        if (userId == null) {
            return CommonResponse.createForError(
                    ResponseCode.TOKEN_INVALID.getCode(),
                    ResponseCode.TOKEN_INVALID.getDescription()
            );
        }

        return userInfoService.getUserPrivacy(userId);
    }

    @PostMapping("update/privacy")
    @LogAction("更新用户隐私设置")
    public CommonResponse<String> updatePrivacy(@Valid @RequestBody ChangePrivacyDTO privacyDTO){
        Integer userId = UserContext.getCurrentUserId();
        if (userId == null) {
            return CommonResponse.createForError(
                    ResponseCode.TOKEN_INVALID.getCode(),
                    ResponseCode.TOKEN_INVALID.getDescription()
            );
        }

        return userInfoService.updatePrivacy(userId,privacyDTO);
    }

    // 限制每分钟最多发送一条邮件
    // 后端限制该方法的使用时间，用于避免直接访问 url 可能带来的问题
    @PostMapping("verification-codes")
    @RateLimit(value = 1, timeUnit = 60000)
    public CommonResponse<Integer> sendCode(@RequestParam String email) {
        return codeService.validateCode(email);
    }

    /**
     * 该方法用于验证 token 的有效性
     * 在用户登录、请求访问需要 token 的 url 时进行调用
     */
    @GetMapping("verify")
    public CommonResponse<String> verifyToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                Integer userId = codeService.validateToken(token);
                if (userId != null) {
                    return CommonResponse.createForSuccess(ResponseCode.SUCCESS.getCode(), "token有效");
                }
            }
            return CommonResponse.createForError(ResponseCode.TOKEN_INVALID.getCode(),
                    ResponseCode.TOKEN_INVALID.getDescription());
        } catch (Exception e) {
            return CommonResponse.createForError(ResponseCode.ERROR.getCode(), e.getMessage());
        }
    }
}
