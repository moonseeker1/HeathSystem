package com.healthSystem.admin.controller;






import com.healthSystem.admin.domain.dto.SystemAdminInfo;
import com.healthSystem.admin.domain.dto.SystemAdminPage;
import com.healthSystem.api.RemoteUserService;
import com.healthSystem.api.domain.SystemUser;
import com.healthSystem.api.domain.SystemUserPage;
import com.healthSystem.admin.domain.entity.SystemAdmin;
import com.healthSystem.admin.service.AdminService;
import com.healthSystem.common.core.api.CommonPage;
import com.healthSystem.common.core.api.CommonResult;
import com.healthSystem.common.core.constant.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private RemoteUserService userService;
    @PostMapping("/login")
    public CommonResult login(@RequestBody SystemAdmin admin){
        String token = adminService.login(admin);
        return CommonResult.success(token);
    }
    @PostMapping("/register")
    public CommonResult register(@RequestBody SystemAdmin admin){
        adminService.register(admin);
        return CommonResult.success();
    }
    @PutMapping("/update")
    public CommonResult update(@RequestBody SystemAdmin admin){
        adminService.update(admin);
        return CommonResult.success();
    }
    @DeleteMapping("/{adminId}")
    public CommonResult delete(@PathVariable("adminId") String adminId){
        adminService.delete(adminId);
        return CommonResult.success();
    }
    @GetMapping("/{adminId}")
    public CommonResult getById(@PathVariable("adminId") String adminId){
        return CommonResult.success(adminService.getById(adminId));
    }
    @GetMapping("/getInfo")
    public CommonResult<SystemAdminInfo> getInfo(){
        return CommonResult.success(adminService.getInfo());
    }

    @GetMapping("/user")
    public CommonResult<CommonPage<SystemUser>> list(SystemUserPage systemUserPage){
        CommonPage<SystemUser> list = userService.list(systemUserPage, SecurityConstants.INNER);
        return CommonResult.success(list);
    }
    @GetMapping
    public CommonResult<CommonPage<SystemAdmin>> listAdmin(SystemAdminPage systemAdminPage){
        List<SystemAdmin> list = adminService.listAdmin(systemAdminPage);
        return CommonResult.success(CommonPage.restPage(list));
    }
}
