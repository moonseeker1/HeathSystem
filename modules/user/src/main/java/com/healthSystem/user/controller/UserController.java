package com.healthSystem.user.controller;


import com.healthSystem.common.core.api.CommonResult;

import com.healthSystem.user.domain.entity.SystemUser;
import com.healthSystem.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public CommonResult login(@RequestBody SystemUser user){
        String token = userService.login(user);
        return CommonResult.success(token);
    }
    @PostMapping("/register")
    public CommonResult register(@RequestBody SystemUser user){
        userService.register(user);
        return CommonResult.success();
    }
    @PutMapping("/update")
    public CommonResult update(@RequestBody SystemUser user){
        userService.update(user);
        return CommonResult.success();
    }
    @DeleteMapping("/{userId}")
    public CommonResult delete(@PathVariable("userId") String userId){
        userService.delete(userId);
        return CommonResult.success();
    }
    @GetMapping("/{userId}")
    public CommonResult getById(@PathVariable("userId") String userId){
        return CommonResult.success(userService.getById(userId));
    }

}
