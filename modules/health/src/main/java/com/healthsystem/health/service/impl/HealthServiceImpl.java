package com.healthsystem.health.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.healthSystem.api.RemoteUserService;
import com.healthSystem.api.domain.SystemUser;
import com.healthSystem.common.core.constant.SecurityConstants;
import com.healthSystem.common.core.exception.ApiException;
import com.healthSystem.common.core.exception.ServiceException;
import com.healthSystem.common.core.util.StringUtils;
import com.healthSystem.common.security.utils.SecurityUtils;
import com.healthsystem.health.domain.entity.Health;
import com.healthsystem.health.mapper.HealthMapper;
import com.healthsystem.health.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthServiceImpl implements HealthService {
    @Autowired
    private HealthMapper healthMapper;
    @Autowired
    RemoteUserService remoteUserService;
    @Override
    public void create(Health health) {

        health.setHealthId(String.valueOf(IdUtil.getSnowflake().nextId()));
        health.setUserId(String.valueOf((SecurityUtils.getLoginUser().getUserid())));
        String userId = String.valueOf((SecurityUtils.getLoginUser().getUserid()));
        int status = 1;
        SystemUser systemUser = new SystemUser();
        systemUser.setUserId(userId);
        systemUser.setStatus(status);
        remoteUserService.update(systemUser, SecurityConstants.INNER);
        healthMapper.insert(health);
    }

    @Override
    public void update(Health health) {
        health.setUserId(String.valueOf((SecurityUtils.getLoginUser().getUserid())));
        LambdaQueryWrapper<Health> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.isNotEmpty(health.getUserId()),Health::getUserId, health.getUserId());
        healthMapper.update(health, lambdaQueryWrapper);

    }

    @Override
    public Health get(String userId) {
        LambdaQueryWrapper<Health> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.isNotEmpty(userId),Health::getUserId, userId);
        Health health = healthMapper.selectOne(lambdaQueryWrapper);
        if(health==null){
            throw new ApiException("未建立健康档案");
        }
        return health;
    }

    @Override
    public List<Health> listHealth(List<String> userIds) {
        LambdaQueryWrapper<Health> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(StringUtils.isNotEmpty(userIds),Health::getUserId, userIds);
        List<Health> healthList = healthMapper.selectList(lambdaQueryWrapper);
        return healthList;
    }
}
