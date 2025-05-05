package com.healthsystem.bigmoduleApi.aspect;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.healthSystem.api.RemoteUserService;
import com.healthSystem.api.domain.SystemUser;
import com.healthSystem.common.core.constant.SecurityConstants;
import com.healthSystem.common.core.util.DateUtils;
import com.healthsystem.bigmoduleApi.domain.dto.BigModuleResult;
import com.healthsystem.bigmoduleApi.domain.entity.BigModuleLog;
import com.healthsystem.bigmoduleApi.domain.entity.UserBigModule;

import com.healthsystem.bigmoduleApi.service.impl.BigModuleLogServiceImpl;
import com.healthsystem.bigmoduleApi.service.impl.BigModuleServiceImpl;
import com.healthsystem.bigmoduleApi.service.impl.BigModuleUserServiceImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Aspect
@Component
public class BigModelAnnotationAspect {
    @Autowired
    RemoteUserService remoteUserService;
    @Autowired
    BigModuleLogServiceImpl bigModuleLogService;
    @Autowired
    BigModuleUserServiceImpl bigModuleUserService;
    // 定义切入点，匹配使用 BigModelCallLog 注解的方法
    @Pointcut("@annotation(com.healthsystem.bigmoduleApi.annotation.BigModuleLog)")
    public void bigModelLogPointcut() {}

    // 定义切入点，匹配使用 UserBigModelUsageRecord 注解的方法
    @Pointcut("@annotation(com.healthsystem.bigmoduleApi.annotation.UserBigModule)")

    public void userBigModelPointcut() {}

    // 处理 BigModelCallLog 注解
    @AfterReturning(pointcut = "bigModelLogPointcut()", returning = "result")
    public void handleBigModelLog(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        com.healthsystem.bigmoduleApi.annotation.BigModuleLog annotation = method.getAnnotation( com.healthsystem.bigmoduleApi.annotation.BigModuleLog.class);
        if (annotation != null) {
            BigModuleLog bigModuleLog = new BigModuleLog();
            bigModuleLog.setLogType(annotation.value());
            bigModuleLog.setTime(LocalDateTime.now());
            BigModuleResult bigModuleResult = (BigModuleResult) result;
            bigModuleLog.setTokenCount(bigModuleResult.getTotalTokens());
            SystemUser systemUser = (SystemUser) remoteUserService.getById(bigModuleResult.getUserId(), SecurityConstants.INNER).getData();
            bigModuleLog.setUserName(systemUser.getUserName());
            bigModuleLog.setUserId(systemUser.getUserId());
            bigModuleLogService.insertLog(bigModuleLog);

        }
    }

    // 处理 UserBigModelUsageRecord 注解
    @AfterReturning(pointcut = "userBigModelPointcut()", returning = "result")
    public void handleUserBigModelUsageRecord(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        com.healthsystem.bigmoduleApi.annotation.UserBigModule annotation = method.getAnnotation(com.healthsystem.bigmoduleApi.annotation.UserBigModule.class);
        BigModuleResult bigModuleResult = (BigModuleResult) result;
        if (annotation != null) {
            UserBigModule userBigModule = bigModuleUserService.getByUserId(bigModuleResult.getUserId());
            if(userBigModule==null){
                userBigModule = new UserBigModule();
                userBigModule.setPromptTokens(bigModuleResult.getPromptTokens());
                userBigModule.setCompletionTokens(bigModuleResult.getCompletionTokens());
                userBigModule.setTotalTokens(bigModuleResult.getTotalTokens());
                SystemUser systemUser = (SystemUser)(remoteUserService.getById(bigModuleResult.getUserId(), SecurityConstants.INNER).getData());
                userBigModule.setUserId(systemUser.getUserId());
                userBigModule.setUserName(systemUser.getUserName());
                userBigModule.setCount(1);
                bigModuleUserService.insert(userBigModule);

            }else{
                UserBigModule userBigModule1 = new UserBigModule();
                userBigModule1.setUserName(userBigModule.getUserName());
                userBigModule1.setUserId(userBigModule1.getUserId());
                userBigModule1.setUserBigModuleId(userBigModule.getUserBigModuleId());
                userBigModule1.setCount(userBigModule.getCount()+1);
                userBigModule1.setPromptTokens(bigModuleResult.getPromptTokens()+userBigModule.getPromptTokens());
                userBigModule1.setCompletionTokens(bigModuleResult.getCompletionTokens()+userBigModule.getCompletionTokens());
                userBigModule1.setTotalTokens(bigModuleResult.getTotalTokens()+userBigModule.getTotalTokens());
                bigModuleUserService.update(userBigModule1);

            }


        }
    }
}