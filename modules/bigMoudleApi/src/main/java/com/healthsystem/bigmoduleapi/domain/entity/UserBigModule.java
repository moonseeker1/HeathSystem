package com.healthsystem.bigmoduleapi.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("system_user_bigModule")
public class UserBigModule {
    @TableId
    private String userBigModuleId;
    private String userId;

    private Integer prompt_tokens;
    private Integer completion_tokens;
    private Integer total_tokens;
    private Integer count;



}
