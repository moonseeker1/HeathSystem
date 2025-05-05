package com.healthsystem.bigmoduleApi.domain.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("system_bigModule_log")
public class BigModuleLog {
    @TableId(value = "log_id")
    String logId;
    String userId;
    String logType;
    Long tokenCount;
    String userName;
    LocalDateTime time;
}
