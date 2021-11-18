package com.chatapp.message.websocket;

import com.chatapp.message.dto.MessageDto;
import com.chatapp.message.utils.Route;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public record SocketService(SimpMessagingTemplate messagingTemplate, RestTemplate restTemplate) {
    public void sendMessage(MessageDto.Built message, UUID recipient) {
        messagingTemplate.convertAndSend(
                Route.WS.SEND.DM + recipient,
                message);
    }

}
