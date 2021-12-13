package com.chatapp.socket.controller;

import com.chatapp.socket.message_dto.Message;
import com.chatapp.socket.utils.Route;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/socket")
public class MessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private KeycloakRestTemplate keycloakRestTemplate;


    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat.sendPublicMessage")
    public void sendPublicMessage(@Header("simpUser") KeycloakAuthenticationToken token, @Payload Message.Base message) {
        message.setSenderId(UUID.fromString(token.getAccount().getPrincipal().getName()));
        message.setSenderUsername(token.getAccount().getKeycloakSecurityContext().getToken().getPreferredUsername());
        message.setTimestamp(Instant.now());

        LOGGER.info("CONTENT: {}", message.getContent());
        LOGGER.info("DESTINATION: {}", message.getDestination());

        messagingTemplate.convertAndSend("/topic/" + message.getDestination(), message.toDisplay());
        final Message.fromMessageService messageFromMessageService =
                keycloakRestTemplate.postForObject(Route.MESSAGE.CREATE, message.toPersist(), Message.fromMessageService.class);

        System.out.println(messageFromMessageService);
        final Message.fromGroupService fromGroupService =
                keycloakRestTemplate.patchForObject(Route.GROUP.CREATE + message.getDestination(), messageFromMessageService, Message.fromGroupService.class);
        System.out.println(fromGroupService);

    }

    @MessageMapping("/chat.sendPrivateMessage")
    public void sendPrivateMessage(@Header("simpUser") KeycloakAuthenticationToken token, @Payload Message.Base message) {
        message.setSenderId(UUID.fromString(token.getAccount().getPrincipal().getName()));
        message.setSenderUsername(token.getAccount().getKeycloakSecurityContext().getToken().getPreferredUsername());
        message.setTimestamp(Instant.now());

        LOGGER.info("PRIVATE MESSAGE: {}", message);

        messagingTemplate.convertAndSend("/user/" + message.getDestination() + "/queue/private", message); // destination
        messagingTemplate.convertAndSend("/user/" + token.getAccount().getPrincipal().getName() + "/queue/private", message); // self
//      keycloakRestTemplate.postForLocation(Route.MESSAGE.CREATE, message.toPersist());
    }

}
