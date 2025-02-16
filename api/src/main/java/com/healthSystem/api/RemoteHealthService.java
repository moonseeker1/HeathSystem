package com.healthSystem.api;

import com.healthSystem.api.domain.Health;
import com.healthSystem.api.factory.RemoteFileFallbackFactory;
import com.healthSystem.common.core.api.CommonResult;
import com.healthSystem.common.core.constant.SecurityConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(contextId = "RemoteHealthService", value = "system-health")
public interface RemoteHealthService {
    @GetMapping("/health/get/{userId}")
    public CommonResult<Health> get(@PathVariable("userId") String userId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
