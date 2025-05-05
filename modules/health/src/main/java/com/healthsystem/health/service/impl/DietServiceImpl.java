package com.healthsystem.health.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.healthSystem.api.RemoteUserService;
import com.healthSystem.api.domain.SystemUser;
import com.healthSystem.common.core.constant.SecurityConstants;
import com.healthSystem.common.core.util.StringUtils;
import com.healthSystem.common.security.utils.SecurityUtils;
import com.healthsystem.health.domain.entity.Diet;
import com.healthsystem.health.domain.entity.Health;
import com.healthsystem.health.mapper.DietMapper;
import com.healthsystem.health.service.DietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DietServiceImpl implements DietService {
    @Autowired
    private DietMapper dietMapper;
    @Autowired
    private RemoteUserService remoteUserService;
    @Override
    public void save(Diet diet) {
        dietMapper.insert(diet);
        SystemUser systemUser = new SystemUser();
        systemUser.setUserId(diet.getUserId());
        systemUser.setDietStatus(1);
        remoteUserService.update(systemUser, SecurityConstants.INNER);
    }

    @Override
    public void update(Diet diet) {
        LambdaQueryWrapper<Diet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.isNotBlank(diet.getUserId()),Diet::getUserId, diet.getUserId());
        dietMapper.update(diet,lambdaQueryWrapper);
    }

    @Override
    public Diet get(String userId) {
        LambdaQueryWrapper<Diet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.isNotEmpty(userId),Diet::getUserId, userId);
        Diet diet = dietMapper.selectOne(lambdaQueryWrapper);
        return diet;
    }

    @Override
    public List<Diet> listDiet(List<String> userIds) {
        LambdaQueryWrapper<Diet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(StringUtils.isNotEmpty(userIds),Diet::getUserId, userIds);
        List<Diet> diets = dietMapper.selectList(lambdaQueryWrapper);
        return diets;
    }

    @Override
    public void delete(String dietId) {
        SystemUser systemUser = new SystemUser();
        Diet diet = dietMapper.selectById(dietId);
        systemUser.setUserId(diet.getUserId());
        systemUser.setDietStatus(0);
        remoteUserService.update(systemUser, SecurityConstants.INNER);
        dietMapper.deleteById(dietId);
    }
}
