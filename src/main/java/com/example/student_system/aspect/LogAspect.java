package com.example.student_system.aspect;

import com.example.student_system.annotation.LogAction;
import com.example.student_system.domain.entity.account.UserLog;
import com.example.student_system.service.account.UserLogService;
import com.example.student_system.service.account.UserService;
import com.example.student_system.util.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Component
public class LogAspect {
    @Autowired
    private UserLogService userLogService;
    
    @Autowired
    private UserService userService;

    // 定义切点，进行拦截
    @Pointcut("@annotation(com.example.student_system.annotation.LogAction)")
    public void logPointCut(){}

    // 方法执行后记录日志
    @AfterReturning(pointcut = "logPointCut()",returning = "result")
    public void logAfterReturning(JoinPoint joinPoint,Object result){
        try {
            // 获取当前请求
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if(attributes == null){
                return;
            }

            HttpServletRequest request = attributes.getRequest();

            // 获取方法签名
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            // 获取注解信息
            LogAction logAction = method.getAnnotation(LogAction.class);
            String action = logAction.value();

            // 获取当前用户信息
            int userId = UserContext.getCurrentUserId();
            String username = getUsernameFromContext();

            // 获取 ip
            String ip = getClientIpAddress(request);

            // 创建 userLog
            UserLog userLog = new UserLog();
            userLog.setUserId(userId);
            userLog.setIp(ip);
            userLog.setUsername(username);
            userLog.setAction(action);
            userLog.setCreateTime(new Date(System.currentTimeMillis()));
            userLog.setUpdateTime(new Date(System.currentTimeMillis()));

            saveLogAsync(userLog);
        }catch (Exception e){
            System.err.println("记录用户操作日志失败：" + e.getMessage());
        }
    }

    private void saveLogAsync(UserLog userLog){
        // 使用新线程异步保存日志
        new Thread(() ->{
            try{
                userLogService.saveUserLog(userLog);
            }catch (Exception e){
                System.err.println("异步保存用户日志失败：" + e.getMessage());
            }
        }).start();
    }

    // 获取用户 ip
    String getClientIpAddress(HttpServletRequest request){
        // 按优先级顺序检查各种IP头
        String[] ipHeaders = {
            "X-Forwarded-For",
            "Proxy-Client-IP", 
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
        };
        
        String ip = null;
        
        // 遍历所有IP头，找到第一个有效的IP
        for (String header : ipHeaders) {
            ip = request.getHeader(header);
            if (isValidIp(ip)) {
                break;
            }
        }
        
        // 如果所有头都没有有效IP，使用远程地址
        if (!isValidIp(ip)) {
            ip = request.getRemoteAddr();
        }

        // 如果是多个IP，取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip != null ? ip : "未知IP";
    }
    
    // 判断IP是否有效
    private boolean isValidIp(String ip) {
        return ip != null && ip.length() > 0 && !"unknown".equalsIgnoreCase(ip);
    }

    // 获取用户名
    private String getUsernameFromContext(){
        try {
            Integer userId = UserContext.getCurrentUserId();
            if (userId == null) {
                return "未知用户";
            }
            
            // 通过UserService获取用户信息
            var response = userService.getUserInfo(userId);
            // 检查状态码是否在成功范围内（1000-1999）
            if (response.getStatus() >= 1000 && response.getStatus() < 2000 && response.getData() != null) {
                return response.getData().getUserName();
            } else {
                return "用户ID: " + userId;
            }
        } catch (Exception e) {
            System.err.println("获取用户名失败：" + e.getMessage());
            return "获取用户名失败";
        }
    }
}
