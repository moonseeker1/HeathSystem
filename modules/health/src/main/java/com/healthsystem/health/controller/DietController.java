package com.healthsystem.health.controller;

import com.healthSystem.common.core.api.CommonResult;
import com.healthsystem.health.domain.entity.Diet;
import com.healthsystem.health.domain.entity.Health;
import com.healthsystem.health.service.DietService;
import com.healthsystem.health.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/health/diet")
public class DietController {
    @Autowired
    DietService dietService;
    @RequestMapping("/save")
    public CommonResult save(@RequestBody Diet diet) {
        dietService.save(diet);
        return CommonResult.success();
    }
    @RequestMapping("/update")
    public CommonResult update(@RequestBody Diet diet) {
        dietService.update(diet);
        return CommonResult.success();
    }
    @GetMapping("/get/{userId}")
    public CommonResult<Diet> get(@PathVariable("userId") String userId) {
        Diet diet = dietService.get(userId);
        return CommonResult.success(diet);
    }
    @PostMapping("/listDiet")
    public CommonResult<List<Diet>> listDiet(@RequestBody List<String> userIds) {
        return CommonResult.success(dietService.listDiet(userIds));
    }
    @DeleteMapping("/delete/{dietId}")
    public CommonResult delete(@PathVariable("dietId") String dietId) {
        dietService.delete(dietId);
        return CommonResult.success();
    }
}
