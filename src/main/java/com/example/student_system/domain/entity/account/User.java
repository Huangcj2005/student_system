package com.example.student_system.domain.entity.account;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("users")
public class User {
    @TableId(value = "id",type = IdType.INPUT)
    private Integer id;
    @TableField(value = "user_id")
    private Integer userId;
    @TableField(value = "user_name")
    private String userName;
    private String email;
    private String password;
    private String role;
    private String sex;
    private String photo;
    private String profile;
    private int status;
    @TableField(value = "create_time")
    private Date createTime;
    @TableField(value = "update_time")
    private Date updateTime;
    @TableField(value = "delete_time")
    private Date deleteTime;
    private String unused;
    
    // 手动添加getter方法，以防Lombok没有正常工作
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }

}
