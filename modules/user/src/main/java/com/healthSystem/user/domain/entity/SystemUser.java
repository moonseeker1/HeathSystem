package com.healthSystem.user.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.healthSystem.common.core.web.dto.BaseEntity;
import lombok.Data;

@Data
@TableName("system_user")
public class SystemUser extends BaseEntity {
    @TableId
    private String userId;
    private String userName;
    private String password;
    private String phonenumber;
    private String email;
    private Integer status;
    private Integer dietStatus;
    private Integer sportStatus;
}
