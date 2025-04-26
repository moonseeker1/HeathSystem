package com.healthSystem.admin.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.healthSystem.admin.domain.dto.SystemAdminInfo;
import com.healthSystem.admin.domain.dto.SystemAdminPage;
import com.healthSystem.admin.domain.entity.SystemAdmin;
import com.healthSystem.admin.mapper.AdminMapper;
import com.healthSystem.admin.service.AdminService;
import com.healthSystem.api.domain.SystemUser;
import com.healthSystem.common.core.exception.ServiceException;

import com.healthSystem.common.core.util.JwtUtils;
import com.healthSystem.common.core.util.StringUtils;

import com.healthSystem.common.redis.service.RedisService;
import com.healthSystem.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private RedisService redisService;
    @Override
    public String login(SystemAdmin admin) {
        if (StringUtils.isAnyBlank(admin.getAdminName(), admin.getPassword()))
        {
            throw new ServiceException("用户/密码必须填写");
        }
        LambdaQueryWrapper<SystemAdmin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.isNotBlank(admin.getAdminName()),SystemAdmin::getAdminName,admin.getAdminName());
        List<SystemAdmin> list = adminMapper.selectList(lambdaQueryWrapper);
        if(list.isEmpty()){
            throw new ServiceException("用户不存在");
        }else{
            if(!admin.getPassword().equals(list.get(0).getPassword())){
                throw new ServiceException("密码错误");
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("userId", list.get(0).getAdminId());
            map.put("userName", list.get(0).getAdminName());
            String token = JwtUtils.createToken(map);
            return token;
        }
    }
    @Override
    public void register(SystemAdmin admin) {
        String id = String.valueOf(IdUtil.getSnowflake().nextId());
        admin.setAdminId(id);
        admin.setStatus(0);
        admin.setCreateTime(Date.from(DateTime.now().toInstant()));
        admin.setUpdateTime(Date.from(DateTime.now().toInstant()));
        adminMapper.insert(admin);
    }

    @Override
    public void update(SystemAdmin admin) {
        if(admin.getPassword().equals("")) {
            admin.setPassword(null);
        }
        admin.setAdminId(String.valueOf(SecurityUtils.getLoginUser().getUserid()));
        admin.setUpdateTime(Date.from(DateTime.now().toInstant()));
        adminMapper.updateById(admin);
    }

    @Override
    public void delete(String adminId) {
        adminMapper.deleteById(adminId);
    }

    @Override
    public SystemAdmin getById(String adminId) {
        return adminMapper.selectById(adminId);
    }

    @Override
    public SystemAdminInfo getInfo() {
        SystemAdmin systemadmin = adminMapper.selectById((SecurityUtils.getLoginUser().getUserid()));
        SystemAdminInfo systemadminInfo = new SystemAdminInfo();
        systemadminInfo.setAdminName(systemadmin.getAdminName());
        systemadminInfo.setAdminId(systemadmin.getAdminId());
        systemadminInfo.setStatus(systemadmin.getStatus());
        return systemadminInfo;
    }

    @Override
    public List<SystemAdmin> listAdmin(SystemAdminPage systemAdminPage) {
        LambdaQueryWrapper<SystemAdmin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.isNotBlank(systemAdminPage.getAdminName()),SystemAdmin::getAdminName,systemAdminPage.getAdminName());
        lambdaQueryWrapper.orderByDesc(SystemAdmin::getCreateTime);
        List<SystemAdmin> list = adminMapper.selectList(lambdaQueryWrapper);
        return list;
    }


}
