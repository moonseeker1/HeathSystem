package com.healthSystem.user.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.healthSystem.api.RemoteDietService;
import com.healthSystem.api.RemoteHealthService;
import com.healthSystem.api.RemoteSportService;
import com.healthSystem.api.domain.Diet;
import com.healthSystem.api.domain.Health;
import com.healthSystem.api.domain.Sport;
import com.healthSystem.common.core.constant.SecurityConstants;
import com.healthSystem.common.core.exception.ServiceException;

import com.healthSystem.common.core.util.JwtUtils;
import com.healthSystem.common.core.util.StringUtils;

import com.healthSystem.common.redis.service.RedisService;
import com.healthSystem.common.security.utils.SecurityUtils;
import com.healthSystem.user.domain.dto.SystemUserInfo;
import com.healthSystem.user.domain.dto.SystemUserPage;
import com.healthSystem.user.domain.entity.SystemUser;
import com.healthSystem.user.domain.vo.SystemUserDiet;
import com.healthSystem.user.domain.vo.SystemUserHealth;
import com.healthSystem.user.domain.vo.SystemUserSport;
import com.healthSystem.user.mapper.UserMapper;
import com.healthSystem.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RemoteHealthService remoteHealthService;
    @Autowired
    private RemoteDietService remoteDietService;
    @Autowired
    private RemoteSportService remoteSportService;
    @Override
    public String login(SystemUser user) {
        if (StringUtils.isAnyBlank(user.getUserName(), user.getPassword()))
        {
            throw new ServiceException("用户/密码必须填写");
        }
        LambdaQueryWrapper<SystemUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.isNotBlank(user.getUserName()),SystemUser::getUserName,user.getUserName());
        List<SystemUser> list = userMapper.selectList(lambdaQueryWrapper);
        if(list.isEmpty()){
            throw new ServiceException("用户不存在");
        }else{
            if(!user.getPassword().equals(list.get(0).getPassword())){
                throw new ServiceException("密码错误");
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("userId", list.get(0).getUserId());
            map.put("userName", list.get(0).getUserName());
            String token = JwtUtils.createToken(map);
            return token;
        }
    }
    @Override
    public void register(SystemUser user) {
        String id = String.valueOf(IdUtil.getSnowflake().nextId());
        user.setCreateTime(Date.from(DateTime.now().toInstant()));
        user.setUpdateTime(Date.from(DateTime.now().toInstant()));
        user.setUserId(id);
        user.setStatus(0);
        userMapper.insert(user);
    }

    @Override
    public void update(SystemUser user) {
        if(user.getUserId()==null){
            user.setUserId(String.valueOf(SecurityUtils.getLoginUser().getUserid()));
        }

        user.setUpdateTime(Date.from(DateTime.now().toInstant()));
        userMapper.updateById(user);
    }

    @Override
    public void delete(String userId) {
        SystemUser systemUser = userMapper.selectById(userId);
        if(systemUser.getStatus()==0||systemUser.getDietStatus()==0||systemUser.getSportStatus()==0){
            throw new ServiceException("用户还存在相关数据，无法删除");
        }
        userMapper.deleteById(userId);
    }

    @Override
    public SystemUser getById(String userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public SystemUserInfo getInfo() {
        SystemUser systemUser = userMapper.selectById((SecurityUtils.getLoginUser().getUserid()));
        SystemUserInfo systemUserInfo = new SystemUserInfo();
        systemUserInfo.setUserName(systemUser.getUserName());
        systemUserInfo.setUserId(systemUser.getUserId());
        systemUserInfo.setStatus(systemUser.getStatus());
        systemUserInfo.setDietStatus(systemUser.getDietStatus());
        systemUserInfo.setSportStatus(systemUser.getSportStatus());
        return systemUserInfo;
    }



    @Override
    public List<SystemUser> listAll() {
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public List<SystemUser> list(SystemUserPage systemUserPage) {
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(systemUserPage.getUserName()),SystemUser::getUserName,systemUserPage.getUserName());
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public List<SystemUserHealth> listHealth(SystemUserPage systemUserPage) {
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(systemUserPage.getUserName()),SystemUser::getUserName,systemUserPage.getUserName());
        List<SystemUser> list = userMapper.selectList(queryWrapper);
        List<String> userIds = list.stream().map(SystemUser::getUserId).toList();
        List<Health> list1 = remoteHealthService.listHealth(userIds, SecurityConstants.INNER).getData();
        HashMap<String,Health> hashMap = new HashMap<>();
        for (Health health : list1) {
            hashMap.put(health.getUserId(),health);
        }
        List<SystemUserHealth> systemUserHealthList = new ArrayList<>();
        for (SystemUser systemUser : list) {
            SystemUserHealth systemUserHealth = new SystemUserHealth();
            systemUserHealth.setUserId(systemUser.getUserId());
            systemUserHealth.setUserName(systemUser.getUserName());
            Health health = hashMap.get(systemUser.getUserId());
            if(health != null){
                systemUserHealth.setHealthId(health.getHealthId());
                systemUserHealth.setAge(health.getAge());
                systemUserHealth.setSex(health.getSex());
                systemUserHealth.setHeight(health.getHeight());
                systemUserHealth.setWeight(health.getWeight());
                systemUserHealth.setBloodPressure(health.getBloodPressure());
                systemUserHealth.setBloodSugar(health.getBloodSugar());
                systemUserHealthList.add(systemUserHealth);
            }

        }

        return systemUserHealthList;
    }

    @Override
    public List<SystemUserDiet> listDiet(SystemUserPage systemUserPage) {
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(systemUserPage.getUserName()),SystemUser::getUserName,systemUserPage.getUserName());
        List<SystemUser> list = userMapper.selectList(queryWrapper);
        List<String> userIds = list.stream().map(SystemUser::getUserId).toList();
        List<Diet> list1 = remoteDietService.listDiet(userIds, SecurityConstants.INNER).getData();
        HashMap<String,Diet> hashMap = new HashMap<>();
        for (Diet diet : list1) {
            hashMap.put(diet.getUserId(),diet);
        }
        List<SystemUserDiet> systemUserDietList = new ArrayList<>();
        for (SystemUser systemUser : list) {
            SystemUserDiet systemUserDiet = new SystemUserDiet();
            systemUserDiet.setUserId(systemUser.getUserId());
            systemUserDiet.setUserName(systemUser.getUserName());
            Diet diet = hashMap.get(systemUser.getUserId());
            if(diet != null){
                systemUserDiet.setDietId(diet.getDietId());
                systemUserDiet.setBreakfast(diet.getBreakfast());
                systemUserDiet.setLunch(diet.getLunch());
                systemUserDiet.setDinner(diet.getDinner());
                systemUserDietList.add(systemUserDiet);

            }

        }

        return systemUserDietList;
    }

    @Override
    public List<SystemUserSport> listSport(SystemUserPage systemUserPage) {
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(systemUserPage.getUserName()),SystemUser::getUserName,systemUserPage.getUserName());
        List<SystemUser> list = userMapper.selectList(queryWrapper);
        List<String> userIds = list.stream().map(SystemUser::getUserId).toList();
        List<Sport> list1 = remoteSportService.listSport(userIds, SecurityConstants.INNER).getData();
        HashMap<String,Sport> hashMap = new HashMap<>();
        for (Sport sport : list1) {
            hashMap.put(sport.getUserId(),sport);
        }
        List<SystemUserSport> systemUserSportList = new ArrayList<>();
        for (SystemUser systemUser : list) {
            SystemUserSport systemUserSport = new SystemUserSport();
            systemUserSport.setUserId(systemUser.getUserId());
            systemUserSport.setUserName(systemUser.getUserName());
            Sport sport = hashMap.get(systemUser.getUserId());
            if(sport != null){
                systemUserSport.setSportId(sport.getSportId());
                systemUserSport.setType(sport.getType());
                systemUserSport.setRate(sport.getRate());
                systemUserSport.setTime(sport.getTime());
                systemUserSportList.add(systemUserSport);
            }
        }

        return systemUserSportList;
    }


}
