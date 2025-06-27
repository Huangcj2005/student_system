package com.example.student_system.domain.entity.account;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("users")
public class User {
    @TableId(value = "id",type = IdType.INPUT)
    private Integer id;
    @TableField(value = "user_id")
    private int userId;
    
    // 手动添加getter方法，以防Lombok没有正常工作
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }

}
