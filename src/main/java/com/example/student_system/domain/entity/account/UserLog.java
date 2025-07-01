package com.example.student_system.domain.entity.account;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user_log")
public class UserLog {
    @TableId(value = "id",type = IdType.INPUT)
    private Integer id;
    @TableField(value = "user_id")
    private Integer userId;
    @TableField(value = "user_name")
    private String username;
    private String ip;
    private String action;
    @TableField(value = "create_time")
    private Date createTime;
    @TableField(value = "update_time")
    private Date updateTime;
    @TableField(value = "delete_time")
    private Date deleteTime;
    private String unused;
}
