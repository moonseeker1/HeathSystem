package com.healthsystem.bigmoduleApi.webSocket;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author GXM
 * @version 1.0.0
 * @Description TODO
 * @createTime 2023年07月19日
 */
@Slf4j

public class WebSocketServiceImpl implements WebSocketService {

    private final Map<Long, WebSocketSession> clients = new ConcurrentHashMap<>();

    @Override
    public void handleOpen(WebSocketSession session) {
        // 这个时候就需要在建立 webSocket 时存储的 用户信息了
        Map<String, Object> attributes = session.getAttributes();
        Claims claims = (Claims) attributes.get("userInfo");
        clients.put(Long.parseLong((String) claims.get("userId")), new ConcurrentWebSocketSessionDecorator(session, 10 * 1000, 64000));
        log.info("a new connection opened，current online count：{}", clients.size());
    }


    @Override
    public void handleClose(WebSocketSession session) {
        // 这个时候就需要在建立 webSocket 时存储的 用户信息了
        Map<String, Object> attributes = session.getAttributes();
        Claims claims = (Claims) attributes.get("userInfo");
        clients.remove(Long.parseLong((String) claims.get("userId")));
        log.info("a new connection closed，current online count：{}", clients.size());
    }

    @Override
    public void handleMessage(WebSocketSession session, String message) {
        // 只处理前端传来的文本消息，并且直接丢弃了客户端传来的消息
        log.info("received a message：{}", message);
    }

    @Override
    public void sendMessage(WebSocketSession session, String message) throws IOException {
        this.sendMessage(session, new TextMessage(message));
    }

    @Override
    public void sendMessage(Long userId, TextMessage message) throws IOException {
        WebSocketSession webSocketSession = clients.get(userId);

        if (webSocketSession.isOpen()) {
            webSocketSession.sendMessage(message);
        }
    }

    @Override
    public void sendMessage(Long userId, String message) throws IOException {
        WebSocketSession webSocketSession = clients.get(userId);
        Map<String,Object> attributes = webSocketSession.getAttributes();
        Claims claims = (Claims) attributes.get("userInfo");
        this.sendMessage(Long.parseLong((String) claims.get("userId")), new TextMessage(message));
    }

    @Override
    public void sendMessage(WebSocketSession session, TextMessage message) throws IOException {
        session.sendMessage(message);
    }

    @Override
    public void broadCast(String message) throws IOException {
        clients.values().forEach(session -> {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void broadCast(TextMessage message) throws IOException {
        clients.values().forEach(session -> {
            if (session.isOpen()) {
                try {
                    session.sendMessage(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void handleError(WebSocketSession session, Throwable error) {
        log.error("websocket error：{}，session id：{}", error.getMessage(), session.getId());
        log.error("", error);
    }

}

