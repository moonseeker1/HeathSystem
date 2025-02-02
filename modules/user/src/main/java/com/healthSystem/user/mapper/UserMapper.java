package com.healthSystem.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.healthSystem.user.domain.entity.SystemUser;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper extends BaseMapper<SystemUser> {


}
