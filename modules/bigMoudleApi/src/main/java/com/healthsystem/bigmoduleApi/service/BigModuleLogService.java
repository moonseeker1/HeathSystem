package com.healthsystem.bigmoduleApi.service;

import com.healthSystem.api.domain.SystemUserPage;
import com.healthsystem.bigmoduleApi.domain.entity.BigModuleLog;

import java.util.List;

public interface BigModuleLogService {
    void insertLog(BigModuleLog bigModuleLog);

    void deleteLog(String id);
    List<BigModuleLog> list(SystemUserPage systemUserPage);

    void delete(String logId);
}
