package com.healthSystem.api;

import com.healthSystem.api.domain.Diet;
import com.healthSystem.api.domain.Health;
import com.healthSystem.common.core.api.CommonResult;
import com.healthSystem.common.core.constant.SecurityConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(contextId = "RemoteDietService", value = "system-health")
public interface RemoteDietService {
    @GetMapping("/health/diet/get/{userId}")
    public CommonResult<Diet> get(@PathVariable("userId") String userId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    @PostMapping("/health/diet/listDiet")
    public CommonResult<List<Diet>>listDiet(@RequestBody List<String> userIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
