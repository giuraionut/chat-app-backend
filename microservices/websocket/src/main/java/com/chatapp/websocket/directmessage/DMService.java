package com.chatapp.websocket.directmessage;

import com.chatapp.websocket.dto.DirectMessage;
import com.chatapp.websocket.utils.Route;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public record DMService(SimpMessagingTemplate messagingTemplate) {

    public void sendDM(DirectMessage dm) {
        messagingTemplate.convertAndSend(
                Route.WS.SEND.DM+dm.getChannelId(),
                dm);
    }
}
