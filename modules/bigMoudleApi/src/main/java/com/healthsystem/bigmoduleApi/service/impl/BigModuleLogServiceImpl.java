package com.healthsystem.bigmoduleApi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.healthSystem.api.domain.SystemUserPage;
import com.healthsystem.bigmoduleApi.domain.entity.BigModuleLog;
import com.healthsystem.bigmoduleApi.domain.entity.UserBigModule;
import com.healthsystem.bigmoduleApi.mapper.BigModuleLogMapper;
import com.healthsystem.bigmoduleApi.service.BigModuleLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BigModuleLogServiceImpl implements BigModuleLogService {
    @Autowired
    private BigModuleLogMapper bigModuleLogMapper;
    @Override

    public void insertLog(BigModuleLog bigModuleLog){
        bigModuleLogMapper.insert(bigModuleLog);
    }
    @Override
    public void deleteLog(String id){
        bigModuleLogMapper.deleteById(id);
    }
    @Override
    public List<BigModuleLog> list(SystemUserPage systemUserPage){
        LambdaQueryWrapper<BigModuleLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(BigModuleLog::getUserName,systemUserPage.getUserName());
        return bigModuleLogMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public void delete(String logId) {
        bigModuleLogMapper.deleteById(logId);
    }


}
