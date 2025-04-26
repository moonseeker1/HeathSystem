package com.healthsystem.health.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("system_health")
public class Health {
    @TableId
    String healthId;
    String userId;
    Integer age;
    Integer sex;
    Double height;
    Double weight;
    String bloodPressure;
    String bloodSugar;

}
