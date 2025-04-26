package com.healthSystem.admin.service;


import com.healthSystem.admin.domain.dto.SystemAdminInfo;
import com.healthSystem.admin.domain.dto.SystemAdminPage;
import com.healthSystem.admin.domain.entity.SystemAdmin;
import com.healthSystem.api.domain.SystemUser;

import java.util.List;


public interface AdminService {
    public String login(SystemAdmin admin);

    public void register(SystemAdmin admin);

    public void update(SystemAdmin admin);

    public void delete(String adminId);

    public SystemAdmin getById(String adminId);

    public SystemAdminInfo getInfo();

    List<SystemAdmin> listAdmin(SystemAdminPage systemAdminPage);
}
