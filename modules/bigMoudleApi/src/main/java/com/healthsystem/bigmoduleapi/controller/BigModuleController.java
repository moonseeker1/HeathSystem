package com.healthsystem.bigmoduleapi.controller;


import com.alibaba.fastjson2.JSON;
import com.healthSystem.common.core.api.CommonResult;
import com.healthSystem.common.security.utils.SecurityUtils;
import com.healthsystem.bigmoduleapi.domain.dto.Message;
import com.healthsystem.bigmoduleapi.domain.entity.RequestText;
import com.healthsystem.bigmoduleapi.service.impl.BigModuleServiceImpl;
import com.healthsystem.bigmoduleapi.webSocket.WebSocketService;
import com.healthsystem.bigmoduleapi.webSocket.WebSocketServiceImpl;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionChunk;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
}
