package com.chatapp.websocket.controller;

import com.chatapp.websocket.dto.DirectMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class WSController {

//     @MessageMapping("/direct_message") //id aici, nu jos si dai send fara to user
////   @SendToUser("/topic/direct_message")
//    public DirectMessage send(@DestinationVariable("id") String id, DirectMessage directMessage) {
//        return directMessage;
//    }
}
