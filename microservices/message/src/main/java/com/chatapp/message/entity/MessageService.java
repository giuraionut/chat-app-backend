package com.chatapp.message.entity;

import com.chatapp.message.dto.MessageDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public record MessageService(MessageRepository messageRepository) {

    public MessageEntity create(MessageEntity messageEntity) {
        return this.messageRepository.save(messageEntity);
    }

    public MessageEntity findById(UUID messageId) {
        return this.messageRepository.findById(messageId).orElseThrow();
    }

    public MessageDto.Display buildMessage(UUID messageId) {
        final MessageEntity messageEntity = this.messageRepository.findById(messageId).orElseThrow();

        return new MessageDto.Display(messageEntity.getSenderId(),
                messageEntity.getContent(),
                messageEntity.getTimestamp());

    }

    public List<MessageDto.Display> getChatHistory(UUID recipientId, UUID senderId) {
        final List<MessageEntity> chatHistory = findByRecipientAndSender(recipientId, senderId);

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


    public MessageEntity update(UUID id, MessageDto.Update sendReceiveMessageDto) {
        final MessageEntity oldMessageEntity = this.findById(id);
        oldMessageEntity.setContent(sendReceiveMessageDto.getContent());
        return this.messageRepository.save(oldMessageEntity);
    }

    public void delete(UUID messageId) {
        final MessageEntity messageEntity = this.findById(messageId);
        this.messageRepository.delete(messageEntity);
    }

    public List<MessageEntity> findByRecipientAndSender(UUID recipientId, UUID senderId) {
        return this.messageRepository.findByRecipientIdAndSenderId(recipientId, senderId);
    }

}
