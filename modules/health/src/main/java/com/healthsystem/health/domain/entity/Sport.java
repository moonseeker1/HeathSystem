package com.healthsystem.health.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("system_health_sport")
public class Sport {
    @TableId
    String sportId;
    String userId;
    String type;
    Integer rate;
    Integer time;
}
