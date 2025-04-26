//package com.healthSystem.api.factory;
//
//
//import com.healthSystem.api.RemoteUserService;
//import com.healthSystem.api.domain.SysUser;
//import com.healthSystem.api.domain.SystemUser;
//import com.healthSystem.api.domain.SystemUserPage;
//import com.healthSystem.api.model.LoginUser;
//import com.healthSystem.common.core.api.CommonPage;
//import com.healthSystem.common.core.api.CommonResult;
//import com.healthSystem.common.core.dto.R;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.cloud.openfeign.FallbackFactory;
//import org.springframework.stereotype.Component;
//
///**
// * 用户服务降级处理
// *
// * @author ruoyi
// */
//@Component
//public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService>
//{
//    private static final Logger log = LoggerFactory.getLogger(RemoteUserFallbackFactory.class);
//
//    @Override
//    public RemoteUserService create(Throwable throwable)
//    {
//        log.error("用户服务调用失败:{}", throwable.getMessage());
//        return new RemoteUserService()
//        {
//            @Override
//            public CommonResult update(SystemUser sysUser, String source) {
//                return null;
//            }
//
//            @Override
//            public R<LoginUser> getUserInfo(String username, String source)
//            {
//                return R.fail("获取用户失败:" + throwable.getMessage());
//            }
//
//            @Override
//            public R<Boolean> registerUserInfo(SysUser sysUser, String source)
//            {
//                return R.fail("注册用户失败:" + throwable.getMessage());
//            }
//
//            @Override
//            public R<Boolean> recordUserLogin(SysUser sysUser, String source)
//            {
//                return R.fail("记录用户登录信息失败:" + throwable.getMessage());
//            }
//
//            @Override
//            public CommonPage<SystemUser> list(SystemUserPage systemUserPage, String source) {
//                return null;
//            }
//
//
//
//
//        };
//    }
//}
