package com.healthsystem.bigmoduleApi.service;

import com.healthsystem.bigmoduleApi.domain.dto.BigModuleResult;
import com.healthsystem.bigmoduleApi.domain.entity.BigModuleLog;
import com.healthsystem.bigmoduleApi.domain.entity.RequestText;

import java.io.IOException;

public interface BigModuleService {
    BigModuleResult analysis() throws IOException;
    BigModuleResult chat(RequestText text) throws IOException;
    String healthTips();

    BigModuleResult diet() throws IOException;

    BigModuleResult sport() throws IOException;

}
