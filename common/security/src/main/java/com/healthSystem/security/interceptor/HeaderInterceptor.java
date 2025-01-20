package com.healthSystem.security.interceptor;


import com.healthSystem.security.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.healthSystem.api.model.LoginUser;
import com.healthSystem.core.constant.SecurityConstants;
import com.healthSystem.core.context.SecurityContextHolder;
import com.healthSystem.core.util.ServletUtils;
import com.healthSystem.core.util.StringUtils;
import com.healthSystem.security.auth.AuthUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

/**
 * 自定义请求头拦截器，将Header数据封装到线程变量中方便获取
 * 注意：此拦截器会同时验证当前用户有效期自动刷新有效期
 *
 * @author ruoyi
 */
public class HeaderInterceptor implements AsyncHandlerInterceptor
{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        if (!(handler instanceof HandlerMethod))
        {
            return true;
        }

        SecurityContextHolder.setUserId(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USER_ID));
        SecurityContextHolder.setUserName(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USERNAME));
        SecurityContextHolder.setUserKey(ServletUtils.getHeader(request, SecurityConstants.USER_KEY));

        String token = SecurityUtils.getToken();
        if (StringUtils.isNotEmpty(token))
        {
            LoginUser loginUser = AuthUtil.getLoginUser(token);
            if (StringUtils.isNotNull(loginUser))
            {
                AuthUtil.verifyLoginUserExpire(loginUser);
                SecurityContextHolder.set(SecurityConstants.LOGIN_USER, loginUser);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception
    {
        SecurityContextHolder.remove();
    }
}
