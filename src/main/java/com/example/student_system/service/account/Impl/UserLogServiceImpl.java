package com.example.student_system.service.account.Impl;

import com.example.student_system.domain.entity.account.UserLog;
import com.example.student_system.mapper.account.UserLogMapper;
import com.example.student_system.service.account.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userLogService")
public class UserLogServiceImpl implements UserLogService {
    @Autowired
    private UserLogMapper userLogMapper;
    @Override
    public void saveUserLog(UserLog userLog) {
        userLogMapper.insert(userLog);
    }
}
