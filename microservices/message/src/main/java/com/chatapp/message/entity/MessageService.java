package com.chatapp.message.entity;

import com.chatapp.message.dto.MessageDto;
import com.chatapp.message.exceptions.CustomException;
import com.chatapp.message.exceptions.ExceptionResource;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public record MessageService(MessageRepository messageRepository) {

    public MessageEntity create(MessageEntity messageEntity) {
        return this.messageRepository.save(messageEntity);
    }

    public MessageEntity findById(UUID messageId) throws CustomException {
        final Optional<MessageEntity> messageOptional = this.messageRepository.findById(messageId);
        if (messageOptional.isEmpty()) {
            throw new CustomException(ExceptionResource.MESSAGE_NOT_FOUND);
        }
        return messageOptional.get();
    }

    public MessageEntity update(UUID id, MessageDto.Update sendReceiveMessageDto) throws CustomException {
        final MessageEntity oldMessageEntity = this.findById(id);
        oldMessageEntity.setContent(sendReceiveMessageDto.getContent());
        return this.messageRepository.save(oldMessageEntity);
    }

    public void delete(UUID messageId) throws CustomException {
        final MessageEntity messageEntity = this.findById(messageId);
        this.messageRepository.delete(messageEntity);
    }
}
