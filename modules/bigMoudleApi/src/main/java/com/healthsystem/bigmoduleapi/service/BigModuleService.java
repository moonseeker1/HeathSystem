package com.healthsystem.bigmoduleapi.service;

import com.healthsystem.bigmoduleapi.domain.entity.RequestText;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionChunk;
import reactor.core.publisher.Flux;

import java.io.IOException;

public interface BigModuleService {
    void analysis() throws IOException;
    void chat(RequestText text) throws IOException;
    String healthTips();

    void diet() throws IOException;

    void sport() throws IOException;
}
