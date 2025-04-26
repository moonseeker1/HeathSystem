package com.healthSystem.api.domain;


import com.healthSystem.api.domain.SystemUser;

public class SystemUserPage extends SystemUser {
    private Integer pageSize=5;

    private Integer pageNum=1;

    private String name;

    private String email;
}
