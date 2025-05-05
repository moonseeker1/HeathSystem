package com.healthsystem.bigmoduleApi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.healthsystem.bigmoduleApi.domain.entity.UserBigModule;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserBigModuleMapper extends BaseMapper<UserBigModule> {
    // Define any additional methods if needed
}
