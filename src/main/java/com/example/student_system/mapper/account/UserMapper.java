package com.example.student_system.mapper.account;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.student_system.domain.entity.account.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
}
