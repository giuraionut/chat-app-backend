package com.chatapp.directmessage.entity;

import com.chatapp.directmessage.dto.DirectMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/direct_message")
public record DirectMessageController(DirectMessageService directMessageService) {
    private static final Logger logger = LoggerFactory.getLogger(DirectMessageController.class);
    private static final String DIRECT_MESSAGE_NOT_FOUND = "Direct message does not exists";

    @PostMapping()
    public DirectMessageDto.Return createMessage(@RequestBody DirectMessageDto.Create createDirectMessage, HttpServletResponse response) {
        response.setStatus(HttpStatus.CREATED.value());
        return this.directMessageService.create(createDirectMessage.toDirectMessage()).toDirectMessageDtoReturn();
    }

    @GetMapping(path = "{message_id}")
    public DirectMessageDto.ReturnWithUsers readMessage(HttpServletResponse response, @PathVariable("message_id") UUID messageId) throws IOException {
        try {
            response.setStatus(HttpStatus.FOUND.value());
            return this.directMessageService.readMessageWithUsers(messageId);
        } catch (NoSuchElementException ex) {
            logger.trace("DirectMessage Controller - readMessage", ex);
            response.sendError(HttpStatus.NOT_FOUND.value(), DIRECT_MESSAGE_NOT_FOUND);
            return null;
        }
    }

    @PutMapping(path = "{message_id}")
    public DirectMessageDto.Create updateMessage(HttpServletResponse response, @RequestBody DirectMessageDto.Update updateDirectMessage,
                                                 @PathVariable("message_id") UUID messageId) throws IOException {
        try {
            response.setStatus(HttpStatus.OK.value());
            return this.directMessageService.update(messageId, updateDirectMessage).toDirectMessageDtoCreate();
        } catch (NoSuchElementException ex) {
            logger.trace("DirectMessage Controller - updateMessage", ex);
            response.sendError(HttpStatus.NOT_FOUND.value(), DIRECT_MESSAGE_NOT_FOUND);
            return null;
        }
    }

    @DeleteMapping(path = "{message_id}")
    public void deleteMessage(HttpServletResponse response, @PathVariable("message_id") UUID messageId) throws IOException {
        try {
            this.directMessageService.delete(messageId);
            response.setStatus(HttpStatus.OK.value());
        } catch (NoSuchElementException ex) {
            logger.trace("DirectMessage Controller - deleteMessage", ex);
            response.sendError(HttpStatus.NOT_FOUND.value(), DIRECT_MESSAGE_NOT_FOUND);
        }
    }
}