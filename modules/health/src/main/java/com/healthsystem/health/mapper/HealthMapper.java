package com.healthsystem.health.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.healthsystem.health.domain.entity.Health;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HealthMapper extends BaseMapper<Health> {
}
