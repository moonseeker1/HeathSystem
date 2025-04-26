package com.healthSystem.admin.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.healthSystem.admin.domain.entity.SystemAdmin;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface AdminMapper extends BaseMapper<SystemAdmin> {


}
