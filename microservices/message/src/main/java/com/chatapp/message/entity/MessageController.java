package com.chatapp.message.entity;

import com.chatapp.message.dto.MessageDto;
import com.chatapp.message.websocket.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/message")
public record MessageController(MessageService messageService, SocketService socketService) {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private static final String MESSAGE_NOT_FOUND = "Message does not exists";

    @PostMapping()
    public MessageDto.Built createMessage(@RequestBody MessageDto.Pure pureMessage, HttpServletResponse response) {
        response.setStatus(HttpStatus.CREATED.value());
        final Message createdMessage = this.messageService.create(pureMessage.toMessage());
        final MessageDto.Built messageToDeliver = this.messageService.buildMessage(createdMessage.getId());
        this.socketService.sendMessage(messageToDeliver, pureMessage.getRecipientId());
        return messageToDeliver;
    }

    @GetMapping(path = "{messageId}")
    public MessageDto.Pure readMessage(HttpServletResponse response, @PathVariable("messageId") UUID messageId) throws IOException {
        try {
            response.setStatus(HttpStatus.FOUND.value());
            return this.messageService.findById(messageId).toPureMessage();
        } catch (NoSuchElementException ex) {
            logger.trace("Message Controller - readMessage", ex);
            response.sendError(HttpStatus.NOT_FOUND.value(), MESSAGE_NOT_FOUND);
            return null;
        }
    }

    @GetMapping(path = "collection/{recipientId}/{senderId}")
    public List<MessageDto.Built> getChatHistory(HttpServletResponse response, @PathVariable("recipientId") UUID recipientId,
                                                 @PathVariable("senderId") UUID senderId) throws IOException {
        try {
            response.setStatus(HttpStatus.FOUND.value());
            return this.messageService.getChatHistory(recipientId, senderId);
        } catch (Exception ex) {
            logger.trace("Message Controller - getChatHistory", ex);
            response.sendError(HttpStatus.NOT_FOUND.value(), MESSAGE_NOT_FOUND);
            return new ArrayList<>();
        }
    }

    @PutMapping(path = "{messageId}")
    public MessageDto.Pure updateMessage(HttpServletResponse response, @RequestBody MessageDto.Update updateMessage,
                                         @PathVariable("messageId") UUID messageId) throws IOException {
        try {
            response.setStatus(HttpStatus.OK.value());
            return this.messageService.update(messageId, updateMessage).toPureMessage();
        } catch (NoSuchElementException ex) {
            logger.trace("Message Controller - updateMessage", ex);
            response.sendError(HttpStatus.NOT_FOUND.value(), MESSAGE_NOT_FOUND);
            return null;
        }
    }

    @DeleteMapping(path = "{messageId}")
    public void deleteMessage(HttpServletResponse response, @PathVariable("messageId") UUID messageId) throws IOException {
        try {
            this.messageService.delete(messageId);
            response.setStatus(HttpStatus.OK.value());
        } catch (NoSuchElementException ex) {
            logger.trace("Message Controller - deleteMessage", ex);
            response.sendError(HttpStatus.NOT_FOUND.value(), MESSAGE_NOT_FOUND);
        }
    }
}