package com.healthsystem.bigmoduleApi.domain.dto;

import lombok.Data;

@Data
public class BigModuleResult {
    private String userId;
    private long promptTokens;
    private long completionTokens;
    private long totalTokens;
}
