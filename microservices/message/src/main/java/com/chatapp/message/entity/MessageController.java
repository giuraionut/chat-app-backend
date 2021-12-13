package com.chatapp.message.entity;

import com.chatapp.message.dto.MessageDto;
import com.chatapp.message.exceptions.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/message")
public record MessageController(MessageService messageService) {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @PostMapping()
    public MessageDto.Display createMessage(@RequestBody MessageDto.Base baseMessage, HttpServletResponse response, Principal principal){
        response.setStatus(HttpStatus.CREATED.value());
        LOGGER.info(principal.getName());
        return this.messageService.create(baseMessage.toMessageEntity()).toDisplayMessage();
    }

    @GetMapping(path = "{messageId}")
    public MessageDto.Base readMessage(HttpServletResponse response, @PathVariable("messageId") UUID messageId) throws CustomException {
        response.setStatus(HttpStatus.FOUND.value());
        return this.messageService.findById(messageId).toBaseMessage();
    }

    @PutMapping(path = "{messageId}")
    public MessageDto.Base updateMessage(HttpServletResponse response, @RequestBody MessageDto.Update updateMessage,
                                         @PathVariable("messageId") UUID messageId) throws CustomException {
        response.setStatus(HttpStatus.OK.value());
        return this.messageService.update(messageId, updateMessage).toBaseMessage();
    }

    @DeleteMapping(path = "{messageId}")
    public void deleteMessage(HttpServletResponse response, @PathVariable("messageId") UUID messageId) throws CustomException {
        this.messageService.delete(messageId);
        response.setStatus(HttpStatus.OK.value());
    }
}