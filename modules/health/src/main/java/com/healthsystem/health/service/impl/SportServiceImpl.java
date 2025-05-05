package com.healthsystem.health.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.healthSystem.api.RemoteUserService;
import com.healthSystem.api.domain.SystemUser;
import com.healthSystem.common.core.constant.SecurityConstants;
import com.healthSystem.common.core.util.StringUtils;
import com.healthSystem.common.security.utils.SecurityUtils;
import com.healthsystem.health.domain.entity.Diet;
import com.healthsystem.health.domain.entity.Sport;
import com.healthsystem.health.mapper.SportMapper;
import com.healthsystem.health.service.HealthService;
import com.healthsystem.health.service.SportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SportServiceImpl implements SportService {
    @Autowired
    private SportMapper sportMapper;
    @Autowired
    private RemoteUserService remoteUserService;

    @Override
    public void update(Sport sport) {
        LambdaQueryWrapper<Sport> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.isNotBlank(sport.getUserId()),Sport::getUserId, sport.getUserId());
        sportMapper.update(sport,lambdaQueryWrapper);
    }
    @Override
    public Sport get(String userId) {
        LambdaQueryWrapper<Sport> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.isNotEmpty(userId),Sport::getUserId, userId);

        Sport sport = sportMapper.selectOne(lambdaQueryWrapper);
        return sport;
    }

    @Override
    public List<Sport> listSport(List<String> userIds) {
        LambdaQueryWrapper<Sport> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(StringUtils.isNotEmpty(userIds),Sport::getUserId, userIds);
        List<Sport> sports = sportMapper.selectList(lambdaQueryWrapper);
        return sports;
    }

    @Override
    public void save(Sport sport) {
        sport.setSportId(String.valueOf(IdUtil.getSnowflake().nextId()));
        sport.setUserId(String.valueOf((SecurityUtils.getLoginUser().getUserid())));
        String userId = String.valueOf((SecurityUtils.getLoginUser().getUserid()));
        int status = 1;
        SystemUser systemUser = new SystemUser();
        systemUser.setUserId(userId);
        systemUser.setSportStatus(status);
        remoteUserService.update(systemUser, SecurityConstants.INNER);
        sportMapper.insert(sport);
    }

    @Override
    public void delete(String sportId) {
        SystemUser systemUser = new SystemUser();
        Sport sport = sportMapper.selectById(sportId);
        systemUser.setUserId(sport.getUserId());
        systemUser.setSportStatus(0);
        remoteUserService.update(systemUser, SecurityConstants.INNER);
        sportMapper.deleteById(sportId);
    }
    // Implement the methods defined in the SportService interface
}
