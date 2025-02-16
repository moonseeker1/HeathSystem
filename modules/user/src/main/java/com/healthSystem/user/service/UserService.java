package com.healthSystem.user.service;

import com.healthSystem.user.domain.dto.SystemUserInfo;
import com.healthSystem.user.domain.entity.SystemUser;


public interface UserService {
    public String login(SystemUser user);

    public void register(SystemUser user);

    public void update(SystemUser user);

    public void delete(String userId);

    public SystemUser getById(String userId);

    public SystemUserInfo getInfo();
}
