package com.healthsystem.bigmoduleApi.service;

import com.healthSystem.api.domain.SystemUserPage;
import com.healthsystem.bigmoduleApi.domain.entity.UserBigModule;

import java.util.List;

public interface BigModuleUserService {
    UserBigModule getByUserId(String userId);
    public void insert(UserBigModule userBigModule);
    public void update(UserBigModule userBigModule);

    List<UserBigModule> list(SystemUserPage systemUserPage);

    void delete(String userModuleId);
}
