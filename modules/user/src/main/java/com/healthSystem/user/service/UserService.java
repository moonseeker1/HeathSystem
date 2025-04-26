package com.healthSystem.user.service;

import com.healthSystem.user.domain.dto.SystemUserInfo;
import com.healthSystem.user.domain.dto.SystemUserPage;
import com.healthSystem.user.domain.entity.SystemUser;
import com.healthSystem.user.domain.vo.SystemUserDiet;
import com.healthSystem.user.domain.vo.SystemUserHealth;
import com.healthSystem.user.domain.vo.SystemUserSport;

import java.util.List;


public interface UserService {
    public String login(SystemUser user);

    public void register(SystemUser user);

    public void update(SystemUser user);

    public void delete(String userId);

    public SystemUser getById(String userId);

    public SystemUserInfo getInfo();



    List<SystemUser> listAll();

    List<SystemUser> list(SystemUserPage systemUserPage);

    List<SystemUserHealth> listHealth(SystemUserPage systemUserPage);

    List<SystemUserDiet> listDiet(SystemUserPage systemUserPage);

    List<SystemUserSport> listSport(SystemUserPage systemUserPage);
}
