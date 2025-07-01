package com.example.student_system.domain.entity.account;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user_privacy")
public class UserPrivacy {
    @TableId(value = "id",type = IdType.INPUT)
    private Integer id;
    @TableField(value = "user_id")
    private Integer userId;
    @TableField(value = "course_learning_visible")
    private Integer courseLearningVisible;
    @TableField("course_like_visible")
    private Integer courseLikeVisible;
    @TableField(value = "score_visible")
    private Integer scoreVisible;
    @TableField(value = "create_time")
    private Date createTime;
    @TableField(value = "update_time")
    private Date updateTime;
    @TableField(value = "delete_time")
    private Date deleteTime;
    private String unused;

    public UserPrivacy(Integer userId){
        this.userId = userId;
        this.courseLearningVisible = 0;
        this.courseLikeVisible = 0;
        this.scoreVisible = 0;
    }
}
