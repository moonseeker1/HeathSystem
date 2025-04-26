package com.healthSystem.admin.domain.dto;

import lombok.Data;

@Data
public class SystemAdminPage {
    private Integer pageSize=5;

    private Integer pageNum=1;

    private String adminName;

    private String email;
}
