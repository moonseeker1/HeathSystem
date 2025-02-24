package com.healthsystem.bigmoduleapi.controller;


import com.healthSystem.common.core.api.CommonResult;
import com.healthsystem.bigmoduleapi.domain.entity.RequestText;
import com.healthsystem.bigmoduleapi.service.BigModuleService;
import com.healthsystem.bigmoduleapi.service.impl.BigModuleServiceImpl;
import com.healthsystem.bigmoduleapi.webSocket.WebSocketMessageService;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionChunk;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import io.reactivex.Flowable;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
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
    private WebSocketMessageService webSocketMessageService;
    @GetMapping(value = "/chat")
    public CommonResult chat(RequestText text) {

//        final List<ChatMessage> streamMessages = new ArrayList<>();
//        final ChatMessage streamSystemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content("你是健康分析师，根据我的健康数据进行分析").build();
//        final ChatMessage streamUserMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(text.getText()).build();
//        streamMessages.add(streamSystemMessage);
//        streamMessages.add(streamUserMessage);
//
//        ChatCompletionRequest streamChatCompletionRequest = ChatCompletionRequest.builder()
//                .model("ep-20250204104402-xlcg2")
//                .messages(streamMessages)
//                .build();
//
//        Flowable<ChatCompletionChunk> flowable = service.streamChatCompletion(streamChatCompletionRequest);
//        Flux<ChatCompletionChunk> flux = Flux.from(flowable);
//        return ResponseEntity.ok(flux);
        final List<ChatMessage> streamMessages = new ArrayList<>();
        final ChatMessage streamSystemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content("你是健康分析师，根据我的健康数据进行分析").build();
        final ChatMessage streamUserMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(text.getText()).build();
        streamMessages.add(streamSystemMessage);
        streamMessages.add(streamUserMessage);

        ChatCompletionRequest streamChatCompletionRequest = ChatCompletionRequest.builder()
                .model("ep-20250204104402-xlcg2")
                .messages(streamMessages)
                .build();
        service.streamChatCompletion(streamChatCompletionRequest)
                .doOnError(Throwable::printStackTrace)
                .blockingForEach(
                        choice -> {
                            if (choice.getChoices().size() > 0) {
                                String content = (String) choice.getChoices().get(0).getMessage().getContent();
                                // 发送消息到 WebSocket
                                webSocketMessageService.sendMessage("/topic/chat", content);
                            }
                        }
                );
        return CommonResult.success();

    }

    @GetMapping(value = "/analysis")
    public ResponseEntity<Flux<ChatCompletionChunk>> analysis() {
        Flux<ChatCompletionChunk> flux = bigModuleService.analysis();
        return ResponseEntity.ok(flux);
    }
    @GetMapping(value = "/healthTips")
    public CommonResult<String> healthTips() {
        String text = bigModuleService.healthTips();
        return CommonResult.success(text);
    }
}
