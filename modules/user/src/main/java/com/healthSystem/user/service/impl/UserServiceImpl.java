package com.healthSystem.user.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.healthSystem.common.core.exception.ServiceException;

import com.healthSystem.common.core.util.JwtUtils;
import com.healthSystem.common.core.util.StringUtils;

import com.healthSystem.common.redis.service.RedisService;
import com.healthSystem.common.security.utils.SecurityUtils;
import com.healthSystem.user.domain.entity.SystemUser;
import com.healthSystem.user.mapper.UserMapper;
import com.healthSystem.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisService redisService;
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
        user.setUserId(id);
        userMapper.insert(user);
    }

    @Override
    public void update(SystemUser user) {
        user.setUserId(String.valueOf(SecurityUtils.getUserId()));
        userMapper.updateById(user);
    }

    @Override
    public void delete(String userId) {
        userMapper.deleteById(userId);
    }

    @Override
    public SystemUser getById(String userId) {
        return userMapper.selectById(userId);
    }


}
