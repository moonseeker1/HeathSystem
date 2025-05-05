package com.healthsystem.bigmoduleApi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.healthSystem.api.RemoteUserService;
import com.healthSystem.api.domain.SystemUser;
import com.healthSystem.api.domain.SystemUserPage;
import com.healthSystem.common.core.constant.SecurityConstants;
import com.healthSystem.common.security.utils.SecurityUtils;
import com.healthsystem.bigmoduleApi.domain.entity.UserBigModule;
import com.healthsystem.bigmoduleApi.mapper.UserBigModuleMapper;
import com.healthsystem.bigmoduleApi.service.BigModuleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BigModuleUserServiceImpl implements BigModuleUserService {
    @Autowired
    private UserBigModuleMapper userBigModuleMapper;
    @Autowired
    private RemoteUserService remoteUserService;
    @Override
    public UserBigModule getByUserId(String userId) {
        LambdaQueryWrapper<UserBigModule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserBigModule::getUserId, userId);
        return userBigModuleMapper.selectOne(queryWrapper);
    }
    @Override
    public void insert(UserBigModule userBigModule) {
        userBigModuleMapper.insert(userBigModule);
    }
    @Override
    public void update(UserBigModule userBigModule) {
        userBigModuleMapper.updateById(userBigModule);
    }

    @Override
    public List<UserBigModule> list(SystemUserPage systemUserPage) {
        LambdaQueryWrapper<UserBigModule> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(UserBigModule::getUserName, systemUserPage.getUserName());
        return userBigModuleMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public void delete(String userModuleId) {
        userBigModuleMapper.deleteById(userModuleId);
    }

}
