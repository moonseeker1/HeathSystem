package com.healthSystem.api.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("system_health_diet")
public class Diet {
    @TableId
    private String dietId;
    private String userId;
    private String breakfast;
    private String lunch;
    private String dinner;

}
