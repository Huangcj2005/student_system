package com.example.student_system.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.student_system.domain.entity.account.User;
import com.example.student_system.domain.entity.account.UserPrivacy;

/**
 * 通用查询工具类
 * 提供常用的查询方法，减少重复代码
 */
public class QueryUtil {

    /**
     * 根据用户ID查询用户
     * @param userMapper 用户Mapper
     * @param userId 用户ID
     * @return 用户对象，如果不存在返回null
     */
    public static User getUserById(BaseMapper<User> userMapper, int userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return userMapper.selectOne(queryWrapper);
    }

    /**
     * 根据邮箱查询用户
     * @param userMapper 用户Mapper
     * @param email 邮箱
     * @return 用户对象，如果不存在返回null
     */
    public static User getUserByEmail(BaseMapper<User> userMapper, String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        return userMapper.selectOne(queryWrapper);
    }

    /**
     * 根据用户名查询用户
     * @param userMapper 用户Mapper
     * @param userName 用户名
     * @return 用户对象，如果不存在返回null
     */
    public static User getUserByUserName(BaseMapper<User> userMapper, String userName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userName);
        return userMapper.selectOne(queryWrapper);
    }

    /**
     * 根据用户ID查询用户隐私设置
     * @param userPrivacyMapper 用户隐私Mapper
     * @param userId 用户ID
     * @return 用户隐私对象，如果不存在返回null
     */
    public static UserPrivacy getUserPrivacyById(BaseMapper<UserPrivacy> userPrivacyMapper, int userId) {
        QueryWrapper<UserPrivacy> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return userPrivacyMapper.selectOne(queryWrapper);
    }

    /**
     * 检查邮箱是否已存在
     * @param userMapper 用户Mapper
     * @param email 邮箱
     * @return true表示已存在，false表示不存在
     */
    public static boolean isEmailExists(BaseMapper<User> userMapper, String email) {
        return getUserByEmail(userMapper, email) != null;
    }

    /**
     * 检查用户名是否已存在
     * @param userMapper 用户Mapper
     * @param userName 用户名
     * @return true表示已存在，false表示不存在
     */
    public static boolean isUserNameExists(BaseMapper<User> userMapper, String userName) {
        return getUserByUserName(userMapper, userName) != null;
    }
}