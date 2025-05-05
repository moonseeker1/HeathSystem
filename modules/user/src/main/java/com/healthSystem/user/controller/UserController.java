package com.healthSystem.user.controller;


import com.healthSystem.common.core.api.CommonPage;
import com.healthSystem.common.core.api.CommonResult;

import com.healthSystem.user.domain.vo.SystemUserDiet;
import com.healthSystem.user.domain.vo.SystemUserHealth;

import com.healthSystem.user.domain.dto.SystemUserInfo;
import com.healthSystem.user.domain.dto.SystemUserPage;
import com.healthSystem.user.domain.entity.SystemUser;
import com.healthSystem.user.domain.vo.SystemUserSport;
import com.healthSystem.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    public CommonResult<SystemUser> getById(@PathVariable("userId") String userId){
        return CommonResult.success(userService.getById(userId));
    }
    @GetMapping("/getInfo")
    public CommonResult<SystemUserInfo> getInfo(){
        return CommonResult.success(userService.getInfo());
    }
    @GetMapping("/listAll")
    public CommonResult<List<SystemUser>> listAll(){
        return CommonResult.success(userService.listAll());
    }
    @PostMapping
    public CommonPage<SystemUser> list(SystemUserPage systemUserPage){
        List<SystemUser> list = userService.list(systemUserPage);
        return CommonPage.restPage(list);
    }
    @GetMapping("/listHealth")
    public CommonResult<CommonPage<SystemUserHealth>> listHealth(SystemUserPage systemUserPage){
        List<SystemUserHealth> list = userService.listHealth(systemUserPage);
        CommonPage<SystemUserHealth> result = CommonPage.restPage(list);
        return CommonResult.success(result);
    }
    @GetMapping("/listDiet")
    public CommonResult<CommonPage<SystemUserDiet>> listDiet(SystemUserPage systemUserPage){
        List<SystemUserDiet> list = userService.listDiet(systemUserPage);
        CommonPage<SystemUserDiet> result = CommonPage.restPage(list);
        return CommonResult.success(result);
    }
    @GetMapping("/listSport")
    public CommonResult<CommonPage<SystemUserSport>> listSport(SystemUserPage systemUserPage){
        List<SystemUserSport> list = userService.listSport(systemUserPage);
        CommonPage<SystemUserSport> result = CommonPage.restPage(list);
        return CommonResult.success(result);
    }

}
