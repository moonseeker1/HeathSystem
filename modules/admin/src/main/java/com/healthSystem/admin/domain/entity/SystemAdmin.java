package com.healthSystem.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.healthSystem.common.core.web.dto.BaseEntity;
import lombok.Data;

@Data
@TableName("system_admin")
public class SystemAdmin extends BaseEntity {
    @TableId
    private String adminId;
    private String adminName;
    private String password;
    private String phonenumber;
    private String email;
    private Integer status;
}
