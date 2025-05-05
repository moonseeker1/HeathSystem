package com.healthsystem.bigmoduleApi.webSocket;


import com.healthSystem.common.core.constant.TokenConstants;
import com.healthSystem.common.core.util.StringUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import com.healthSystem.common.core.util.JwtUtils;
import java.util.Map;

/**
 * @author GXM
 * @version 1.0.0
 * @Description TODO
 * @createTime 2023年07月19日
 */

@Slf4j
public class WebSocketInterceptor implements HandshakeInterceptor {

    /**
     * 建立请求之前，可以用来做权限判断
     *
     * @param request    the current request
     * @param response   the current response
     * @param wsHandler  the target WebSocket handler
     * @param attributes the attributes from the HTTP handshake to associate with the WebSocket
     *                   session; the provided attributes are copied, the original map is not used.
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
            // 模拟用户（通常利用JWT令牌解析用户信息）
            String token = getToken(request);
            try {
                Claims claims = JwtUtils.parseToken(token);
                attributes.put("userInfo",claims);
            } catch (Exception e) {
                log.error("webSocket 认证失败 ", e);
                return false;
            }
            return true;
    }


    /**
     * 建立请求之后
     *
     * @param request   the current request
     * @param response  the current response
     * @param wsHandler the target WebSocket handler
     * @param exception an exception raised during the handshake, or {@code null} if none
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {

    }
    private String getToken(ServerHttpRequest request)
    {
        String token = request.getHeaders().getFirst(TokenConstants.AUTHENTICATION);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX))
        {
            token = token.replaceFirst(TokenConstants.PREFIX, StringUtils.EMPTY);
        }
        return token;
    }
}


