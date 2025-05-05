package com.healthsystem.bigmoduleApi.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("system_user_bigModule")
public class UserBigModule {
    @TableId
    private String userBigModuleId;
    private String userId;
    private String userName;
    private Long promptTokens;
    private Long completionTokens;
    private Long totalTokens;
    private Integer count;



}
