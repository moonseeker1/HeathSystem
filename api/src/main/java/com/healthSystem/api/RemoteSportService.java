package com.healthSystem.api;

import com.healthSystem.api.domain.Diet;
import com.healthSystem.api.domain.Health;
import com.healthSystem.api.domain.Sport;
import com.healthSystem.common.core.api.CommonResult;
import com.healthSystem.common.core.constant.SecurityConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(contextId = "RemoteSportService", value = "system-health")
public interface RemoteSportService {
    @GetMapping("/health/sport/get/{userId}")
    public CommonResult<Sport> get(@PathVariable("userId") String userId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    @PostMapping("/health/sport/listSport")
    public CommonResult<List<Sport>>listSport(@RequestBody List<String> userIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
