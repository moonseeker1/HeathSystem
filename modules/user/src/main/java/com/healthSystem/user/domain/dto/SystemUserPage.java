package com.healthSystem.user.domain.dto;

import com.healthSystem.user.domain.entity.SystemUser;
import lombok.Data;

@Data
public class SystemUserPage {
    private Integer pageSize=5;

    private Integer pageNum=1;

    private String userName;

    private String email;
}
