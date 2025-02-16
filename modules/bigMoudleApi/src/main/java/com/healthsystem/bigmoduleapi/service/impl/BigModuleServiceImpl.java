package com.healthsystem.bigmoduleapi.service.impl;

import com.healthSystem.api.RemoteHealthService;
import com.healthSystem.api.domain.Health;
import com.healthSystem.common.core.constant.SecurityConstants;
import com.healthSystem.common.redis.service.RedisService;
import com.healthSystem.common.security.utils.SecurityUtils;
import com.healthsystem.bigmoduleapi.service.BigModuleService;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionChunk;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import io.reactivex.Flowable;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class BigModuleServiceImpl implements BigModuleService {
    @Autowired
    private RemoteHealthService healthService;
    @Autowired
    private RedisService redisService;
    static String apiKey = "52c2a12e-7a33-41bc-abf8-55ea6be9473b";
    static ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
    static Dispatcher dispatcher = new Dispatcher();
    static ArkService service = ArkService.builder().dispatcher(dispatcher).connectionPool(connectionPool).baseUrl("https://ark.cn-beijing.volces.com/api/v3").apiKey(apiKey).build();
    @Override
    public Flux<ChatCompletionChunk> analysis() {
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

        Flowable<ChatCompletionChunk> flowable = service.streamChatCompletion(streamChatCompletionRequest);
        Flux<ChatCompletionChunk> flux = Flux.from(flowable);
        return flux;
    }

    @Override
    public String healthTips() {
        String text = redisService.getCacheObject("healthTips");
        return text;
    }
}
