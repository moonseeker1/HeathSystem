package com.healthsystem.bigmoduleapi.service.impl;

import com.alibaba.fastjson2.JSON;
import com.healthSystem.api.RemoteDietService;
import com.healthSystem.api.RemoteHealthService;
import com.healthSystem.api.RemoteSportService;
import com.healthSystem.api.domain.Diet;
import com.healthSystem.api.domain.Health;
import com.healthSystem.api.domain.Sport;
import com.healthSystem.common.core.constant.SecurityConstants;
import com.healthSystem.common.redis.service.RedisService;
import com.healthSystem.common.security.utils.SecurityUtils;
import com.healthsystem.bigmoduleapi.domain.dto.Message;
import com.healthsystem.bigmoduleapi.domain.entity.RequestText;
import com.healthsystem.bigmoduleapi.service.BigModuleService;
import com.healthsystem.bigmoduleapi.webSocket.WebSocketService;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class BigModuleServiceImpl implements BigModuleService {
    @Autowired
    private RemoteHealthService healthService;
    @Autowired
    private RemoteDietService dietService;
    @Autowired
    private RemoteSportService sportService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private WebSocketService webSocketService;
    static String apiKey = "52c2a12e-7a33-41bc-abf8-55ea6be9473b";
    static ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
    static Dispatcher dispatcher = new Dispatcher();
    static ArkService service = ArkService.builder().dispatcher(dispatcher).connectionPool(connectionPool).baseUrl("https://ark.cn-beijing.volces.com/api/v3").apiKey(apiKey).build();

    @Override
    public void analysis() throws IOException {
        final List<ChatMessage> streamMessages = new ArrayList<>();
        final ChatMessage streamSystemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content("你是健康分析师，根据我的健康数据进行分析").build();
        Health health = healthService.get(String.valueOf(SecurityUtils.getUserId()), SecurityConstants.INNER).getData();
        StringBuilder text = new StringBuilder();
        if(health.getSex()=='1'){
            text.append("我是男性"+ health.getAge() +"岁");

        }else{
            text.append("我是女性"+health.getAge()+"岁");
        }
        text.append("我的身高是"+health.getHeight()+"cm");
        text.append("我的体重是"+health.getWeight()+"kg");
        text.append("我的血压是"+health.getBloodPressure()+"mmHg");
        text.append("我的血糖是"+health.getBloodSugar()+"mmol/L");
        final ChatMessage streamUserMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(String.valueOf(text)).build();
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
                                Message message = new Message();
                                message.setType("suggestion");
                                String content = (String) choice.getChoices().get(0).getMessage().getContent();
                                message.setContent(content);
                                String messageJson = JSON.toJSONString(message);
                                // 发送消息到 WebSocket
                                webSocketService.sendMessage(SecurityUtils.getLoginUser().getUserid(),messageJson);
                            }
                        }
                );
        Message message = new Message();
        message.setType("suggestion");
        message.setContent("$$$");
        String messageJson = JSON.toJSONString(message);
        webSocketService.sendMessage(SecurityUtils.getLoginUser().getUserid(),messageJson);

    }

    @Override
    public void chat(RequestText text) throws IOException {
        final List<ChatMessage> streamMessages = new ArrayList<>();
        final ChatMessage streamSystemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content("你是健康分析师，根据我的健康数据进行分析").build();
        final ChatMessage streamUserMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(text.getText()).build();
        streamMessages.add(streamSystemMessage);
        streamMessages.add(streamUserMessage);
        ChatCompletionRequest.ChatCompletionRequestStreamOptions  chatCompletionRequestStreamOptions= new ChatCompletionRequest.ChatCompletionRequestStreamOptions(true);
        ChatCompletionRequest streamChatCompletionRequest = ChatCompletionRequest.builder()
                .model("ep-20250204104402-xlcg2")
                .streamOptions(chatCompletionRequestStreamOptions)
                .messages(streamMessages)
                .build();
        service.streamChatCompletion(streamChatCompletionRequest)
                .doOnError(Throwable::printStackTrace)
                .blockingForEach(
                        choice -> {
                            if (choice.getChoices().size() > 0) {
                                String content = (String) choice.getChoices().get(0).getMessage().getContent();
                                Message message = new Message();
                                message.setContent(content);
                                message.setType("chat");
                                String messageJson = JSON.toJSONString(message);
                                // 发送消息到 WebSocket
                                webSocketService.sendMessage(SecurityUtils.getLoginUser().getUserid(),messageJson);
                            }
                            if (choice.getUsage() != null) {
                                long promptTokens = choice.getUsage().getPromptTokens();
                                long completionTokens = choice.getUsage().getCompletionTokens();
                                long totalTokens = choice.getUsage().getTotalTokens();

                            }

                        }
                );
        Message message = new Message();
        message.setContent("$$$");
        message.setType("chat");
        String messageJson = JSON.toJSONString(message);
        webSocketService.sendMessage(SecurityUtils.getLoginUser().getUserid(),messageJson);
    }

    @Override
    public String healthTips() {
        String text = redisService.getCacheObject("healthTips");
        return text;
    }

    @Override
    public void diet() throws IOException {
        final List<ChatMessage> streamMessages = new ArrayList<>();
        final ChatMessage streamSystemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content("你是健康分析师，根据我的健康数据和饮食情况进行饮食建议").build();
        Health health = healthService.get(String.valueOf(SecurityUtils.getUserId()), SecurityConstants.INNER).getData();
        Diet diet = dietService.get(String.valueOf(SecurityUtils.getUserId()),SecurityConstants.INNER).getData();
        StringBuilder text = new StringBuilder();
        if(health.getSex()=='1'){
            text.append("我是男性"+ health.getAge() +"岁");

        }else{
            text.append("我是女性"+health.getAge()+"岁");
        }
        text.append("我的身高是"+health.getHeight()+"cm");
        text.append("我的体重是"+health.getWeight()+"kg");
        text.append("我的血压是"+health.getBloodPressure()+"mmHg");
        text.append("我的血糖是"+health.getBloodSugar()+"mmol/L");
        text.append("我的饮食情况是"+"早餐："+diet.getBreakfast()+"午餐："+diet.getLunch()+"晚餐："+diet.getDinner());
        text.append("只需要提供饮食建议");
        final ChatMessage streamUserMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(String.valueOf(text)).build();
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
                                Message message = new Message();
                                message.setType("diet");
                                String content = (String) choice.getChoices().get(0).getMessage().getContent();
                                message.setContent(content);
                                String messageJson = JSON.toJSONString(message);
                                // 发送消息到 WebSocket
                                webSocketService.sendMessage(SecurityUtils.getLoginUser().getUserid(),messageJson);
                            }
                        }
                );
        Message message = new Message();
        message.setType("diet");
        message.setContent("$$$");
        String messageJson = JSON.toJSONString(message);
        webSocketService.sendMessage(SecurityUtils.getLoginUser().getUserid(),messageJson);
    }

    @Override
    public void sport() throws IOException {
        final List<ChatMessage> streamMessages = new ArrayList<>();
        final ChatMessage streamSystemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content("你是健康分析师，根据我的健康数据和运动情况进行运动建议").build();
        Health health = healthService.get(String.valueOf(SecurityUtils.getUserId()), SecurityConstants.INNER).getData();
        Sport sport = sportService.get(String.valueOf(SecurityUtils.getUserId()),SecurityConstants.INNER).getData();
        StringBuilder text = new StringBuilder();
        if(health.getSex()=='1'){
            text.append("我是男性"+ health.getAge() +"岁");

        }else{
            text.append("我是女性"+health.getAge()+"岁");
        }
        text.append("我的身高是"+health.getHeight()+"cm");
        text.append("我的体重是"+health.getWeight()+"kg");
        text.append("我的血压是"+health.getBloodPressure()+"mmHg");
        text.append("我的血糖是"+health.getBloodSugar()+"mmol/L");
        text.append("我的运动情况是"+"运动类型："+sport.getType()+"运动频率："+"每周"+sport.getRate()+"次"+"运动时长"+sport.getTime()+"分钟");
        text.append("只需要提供运动建议");
        final ChatMessage streamUserMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(String.valueOf(text)).build();
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
                                Message message = new Message();
                                message.setType("sport");
                                String content = (String) choice.getChoices().get(0).getMessage().getContent();
                                message.setContent(content);
                                String messageJson = JSON.toJSONString(message);
                                // 发送消息到 WebSocket
                                webSocketService.sendMessage(SecurityUtils.getLoginUser().getUserid(),messageJson);
                            }
                        }
                );
        Message message = new Message();
        message.setType("sport");
        message.setContent("$$$");
        String messageJson = JSON.toJSONString(message);
        webSocketService.sendMessage(SecurityUtils.getLoginUser().getUserid(),messageJson);
    }
}
