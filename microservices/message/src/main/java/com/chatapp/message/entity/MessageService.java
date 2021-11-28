package com.chatapp.message.entity;

import com.chatapp.message.dto.MessageDto;
import com.chatapp.message.exceptions.CustomException;
import com.chatapp.message.exceptions.ExceptionResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    public MessageDto.Display buildMessage(UUID messageId) throws CustomException {
        final MessageEntity messageEntity = this.findById(messageId);

        return new MessageDto.Display(messageEntity.getSenderId(),
                messageEntity.getContent(),
                messageEntity.getTimestamp());

    }

    public List<MessageDto.Display> getChatHistory(UUID recipientId, UUID senderId) throws CustomException {
        final List<MessageEntity> chatHistory = findByRecipientAndSender(recipientId, senderId);
        if (chatHistory.isEmpty()) {
            throw new CustomException(ExceptionResource.CHAT_HISTORY_NOT_FOUND);
        }
        List<MessageDto.Display> historyChatHistory = new ArrayList<>();
        chatHistory.forEach(message -> {
            if (message.getSenderId().equals(recipientId)) {
                historyChatHistory.add(new MessageDto.Display(senderId, message.getContent(), message.getTimestamp()));
            } else {
                historyChatHistory.add(new MessageDto.Display(recipientId, message.getContent(), message.getTimestamp()));
            }
        });
        return historyChatHistory;
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

    public List<MessageEntity> findByRecipientAndSender(UUID recipientId, UUID senderId) {
        return this.messageRepository.findByRecipientIdAndSenderId(recipientId, senderId);
    }

}
