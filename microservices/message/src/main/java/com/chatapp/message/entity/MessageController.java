package com.chatapp.message.entity;

import com.chatapp.message.dto.MessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/message")
public record MessageController(MessageService messageService) {
    private static final String MESSAGE_NOT_FOUND = "Message does not exists";
    private static final String CONVERSATION_NOT_FOUND = "Conversation does not exists";

    @PostMapping()
    public MessageDto.Built createMessage(@RequestBody MessageDto.Pure pureMessage, HttpServletResponse response) {
        response.setStatus(HttpStatus.CREATED.value());
        final Message createdMessage = this.messageService.create(pureMessage.toMessage());
        return this.messageService.buildMessage(createdMessage.getId());
    }

    @GetMapping(path = "{messageId}")
    public MessageDto.Pure readMessage(HttpServletResponse response, @PathVariable("messageId") UUID messageId) {
        try {
            response.setStatus(HttpStatus.FOUND.value());
            return this.messageService.findById(messageId).toPureMessage();
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    MESSAGE_NOT_FOUND);
        }
    }

    @GetMapping(path = "collection/{recipientId}/{senderId}")
    public List<MessageDto.Built> getChatHistory(HttpServletResponse response, @PathVariable("recipientId") UUID recipientId,
                                                 @PathVariable("senderId") UUID senderId) {
        try {
            response.setStatus(HttpStatus.FOUND.value());
            return this.messageService.getChatHistory(recipientId, senderId);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    CONVERSATION_NOT_FOUND);
        }
    }

    @PutMapping(path = "{messageId}")
    public MessageDto.Pure updateMessage(HttpServletResponse response, @RequestBody MessageDto.Update updateMessage,
                                         @PathVariable("messageId") UUID messageId){
        try {
            response.setStatus(HttpStatus.OK.value());
            return this.messageService.update(messageId, updateMessage).toPureMessage();
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    MESSAGE_NOT_FOUND);
        }
    }

    @DeleteMapping(path = "{messageId}")
    public void deleteMessage(HttpServletResponse response, @PathVariable("messageId") UUID messageId) {
        try {
            this.messageService.delete(messageId);
            response.setStatus(HttpStatus.OK.value());
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    MESSAGE_NOT_FOUND);
        }
    }
}