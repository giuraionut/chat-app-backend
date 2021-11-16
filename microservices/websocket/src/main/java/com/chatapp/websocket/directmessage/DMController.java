package com.chatapp.websocket.directmessage;

import com.chatapp.websocket.dto.DirectMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/websocket")
public record DMController(DMService dmService) {
    @PostMapping(path = "/dm")
    public void sendDirectMessage(@RequestBody DirectMessage directMessage) {
        this.dmService.sendDM(directMessage);
    }
}
