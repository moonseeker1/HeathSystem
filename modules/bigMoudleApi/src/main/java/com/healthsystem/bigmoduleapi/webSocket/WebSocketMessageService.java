package com.healthsystem.bigmoduleapi.webSocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketMessageService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendMessage(String destination, String message) {
        messagingTemplate.convertAndSend(destination, message);
    }
}