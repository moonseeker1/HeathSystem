package com.healthSystem.user.domain.dto;

import lombok.Data;

@Data
public class SystemUserInfo
{
    String userName;
    String userId;
    Integer status;
    Integer dietStatus;
    Integer sportStatus;
}
