package com.healthsystem.bigmoduleapi.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("system_bigModule_log")
public class BigModuleLog {
    @TableId(value = "log_id")
    String logId;
    String userId;
    String logType;
    Integer tokenCount;
    String userName;
    String time;
}
