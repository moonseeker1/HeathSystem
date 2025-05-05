package com.healthsystem.bigmoduleApi.controller;


import com.healthSystem.api.domain.SystemUserPage;
import com.healthSystem.common.core.api.CommonPage;
import com.healthSystem.common.core.api.CommonResult;
import com.healthsystem.bigmoduleApi.domain.entity.BigModuleLog;
import com.healthsystem.bigmoduleApi.domain.entity.RequestText;
import com.healthsystem.bigmoduleApi.domain.entity.UserBigModule;
import com.healthsystem.bigmoduleApi.service.BigModuleLogService;
import com.healthsystem.bigmoduleApi.service.BigModuleUserService;
import com.healthsystem.bigmoduleApi.service.impl.BigModuleServiceImpl;
import com.healthsystem.bigmoduleApi.webSocket.WebSocketService;
import com.volcengine.ark.runtime.service.ArkService;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
@RestController
@RequestMapping("/bigModule")
public class BigModuleController {
    static String apiKey = "52c2a12e-7a33-41bc-abf8-55ea6be9473b";
    static ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
    static Dispatcher dispatcher = new Dispatcher();
    static ArkService service = ArkService.builder().dispatcher(dispatcher).connectionPool(connectionPool).baseUrl("https://ark.cn-beijing.volces.com/api/v3").apiKey(apiKey).build();
    @Autowired
    private BigModuleServiceImpl bigModuleService;
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private BigModuleLogService bigModuleLogService;
    @Autowired
    private BigModuleUserService bigModuleUserService;

    @GetMapping(value = "/chat")
    public CommonResult chat(RequestText text) throws IOException {
        bigModuleService.chat(text);
        return CommonResult.success();

    }
    @GetMapping(value = "/analysis")
    public CommonResult analysis() throws IOException {
        bigModuleService.analysis();
        return CommonResult.success();
    }
    @GetMapping(value = "/healthTips")
    public CommonResult<String> healthTips() {
        String text = bigModuleService.healthTips();
        return CommonResult.success(text);
    }
    @GetMapping(value = "/diet")
    public CommonResult<String> diet() throws IOException {
        bigModuleService.diet();
        return CommonResult.success();
    }
    @GetMapping(value = "/sport")
    public CommonResult<String> sport() throws IOException {
        bigModuleService.sport();
        return CommonResult.success();
    }
    @GetMapping(value = "/list/logs")
    public CommonResult<CommonPage<BigModuleLog>> listLogs(SystemUserPage systemUserPage) {
        return CommonResult.success(CommonPage.restPage(bigModuleLogService.list(systemUserPage)));
    }
    @GetMapping(value = "/list/userModule")
    public CommonResult<CommonPage<UserBigModule>> listUserModule(SystemUserPage systemUserPage) {
        return CommonResult.success(CommonPage.restPage(bigModuleUserService.list(systemUserPage)));
    }
    @DeleteMapping(value = "/delete/userModule/{userModuleId}")
    public CommonResult deleteUserModule(@PathVariable("userModuleId") String userModuleId) {
        bigModuleUserService.delete(userModuleId);
        return CommonResult.success();
    }
    @DeleteMapping(value = "/delete/log/{logId}")
    public CommonResult deleteLog(@PathVariable("logId") String logId) {
        bigModuleLogService.delete(logId);
        return CommonResult.success();
    }
}
