package com.healthsystem.health.controller;

import com.healthSystem.api.RemoteUserService;
import com.healthSystem.api.domain.SystemUser;
import com.healthSystem.common.core.api.CommonResult;
import com.healthsystem.health.domain.entity.Health;
import com.healthsystem.health.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/health")
public class HealthController {
    @Autowired
    HealthService healthService;

    @PostMapping("/create")
    public CommonResult create(@RequestBody Health health) {
        healthService.create(health);
        return CommonResult.success();
    }
    @PutMapping("/update")
    public CommonResult update(@RequestBody Health health) {
        healthService.update(health);
        return CommonResult.success();
    }
    @GetMapping("/get/{userId}")
    public CommonResult<Health> get(@PathVariable("userId") String userId) {
        Health health = healthService.get(userId);
        return CommonResult.success(health);
    }
    @PostMapping("/listHealth")
    public CommonResult<List<Health>> listHealth(@RequestBody List<String> userIds) {
        return CommonResult.success(healthService.listHealth(userIds));
    }
    @DeleteMapping("/delete/{healthId}")
    public CommonResult delete(@PathVariable("healthId") String healthId) {
        healthService.delete(healthId);
        return CommonResult.success();
    }

}
