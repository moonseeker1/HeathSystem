package com.healthsystem.health.controller;

import com.healthSystem.common.core.api.CommonResult;
import com.healthsystem.health.domain.entity.Health;
import com.healthsystem.health.domain.entity.Sport;
import com.healthsystem.health.service.HealthService;
import com.healthsystem.health.service.SportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/health/sport")
public class SportController {
    @Autowired
    SportService sportService;
    @PostMapping("/save")
    public CommonResult create(@RequestBody Sport sport) {
        sportService.save(sport);
        return CommonResult.success();
    }
    @PutMapping("/update")
    public CommonResult update(@RequestBody Sport sport) {
        sportService.update(sport);
        return CommonResult.success();
    }
    @GetMapping("/get/{userId}")
    public CommonResult<Sport> get(@PathVariable("userId") String userId) {
        Sport sport = sportService.get(userId);
        return CommonResult.success(sport);
    }
    @PostMapping("/listSport")
    public CommonResult<List<Sport>> listSport(@RequestBody List<String> userIds) {
        return CommonResult.success(sportService.listSport(userIds));
    }
}
