package com.healthsystem.bigmoduleapi.service;

import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionChunk;
import reactor.core.publisher.Flux;

public interface BigModuleService {
    Flux<ChatCompletionChunk> analysis();

    String healthTips();
}
