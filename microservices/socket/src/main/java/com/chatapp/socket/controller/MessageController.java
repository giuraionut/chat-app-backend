package com.chatapp.socket.controller;

import com.chatapp.socket.message_dto.Message;
import com.google.gson.*;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/socket")
public class MessageController {

    Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat.sendPublicMessage")
    public void sendPublicMessage(@Header("simpUser") KeycloakAuthenticationToken token, @Payload Message.Base message) {
        message.setSenderId(UUID.fromString(token.getAccount().getPrincipal().getName()));
        message.setSenderUsername(token.getAccount().getKeycloakSecurityContext().getToken().getPreferredUsername());
        message.setTimestamp(Instant.now());

        logger.info("PUBLIC MESSAGE: {}", message);

        messagingTemplate.convertAndSend("/topic/"+message.getRecipientId(),message.toDisplay());
    }

    @MessageMapping("/chat.sendPrivateMessage")
    public void sendPrivateMessage(@Header("simpUser") KeycloakAuthenticationToken token, @Payload Message.Base message) {
        message.setSenderId(UUID.fromString(token.getAccount().getPrincipal().getName()));
        message.setSenderUsername(token.getAccount().getKeycloakSecurityContext().getToken().getPreferredUsername());
        message.setTimestamp(Instant.now());

        logger.info("PRIVATE MESSAGE: {}", message);

        messagingTemplate.convertAndSend("/user/"+message.getRecipientId()+"/queue/private",message); // destination
        messagingTemplate.convertAndSend("/user/"+token.getAccount().getPrincipal().getName()+"/queue/private",message); // self
    }
}
