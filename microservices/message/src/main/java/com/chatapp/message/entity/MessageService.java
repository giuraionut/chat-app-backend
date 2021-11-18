package com.chatapp.message.entity;

import com.chatapp.message.dto.MessageDto;
import com.chatapp.message.user.User;
import com.chatapp.message.utils.Route;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public record MessageService(MessageRepository messageRepository, RestTemplate restTemplate) {

    public Message create(Message message) {
        return this.messageRepository.save(message);
    }

    public Message findById(UUID messageId) {
        return this.messageRepository.findById(messageId).orElseThrow();
    }

    public MessageDto.Built buildMessage(UUID messageId) {
        final Message message = this.messageRepository.findById(messageId).orElseThrow();
        User sender;
        try {
            sender = restTemplate.getForObject(Route.User.GET.BY_ID + message.getSenderId(), User.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            sender = new User();
        }

        return new MessageDto.Built(sender,
                message.getContent(),
                message.getTimestamp());

    }

    public List<MessageDto.Built> getChatHistory(UUID recipientId, UUID senderId) {
        final List<Message> chatHistory = findByRecipientAndSender(recipientId, senderId);
        User sender1;
        User sender2;
        List<MessageDto.Built> historyChatHistory = new ArrayList<>();
        try {
            sender1 = restTemplate.getForObject(Route.User.GET.BY_ID + senderId, User.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            sender1 = new User();
        }
        try {
            sender2 = restTemplate.getForObject(Route.User.GET.BY_ID + recipientId, User.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            sender2 = new User();
        }
        final User s1 = sender1;
        final User s2 = sender2;
        chatHistory.forEach(message -> {
            if(message.getSenderId().equals(recipientId))
            historyChatHistory.add(new MessageDto.Built(s2, message.getContent(), message.getTimestamp()));
            else
                historyChatHistory.add(new MessageDto.Built(s1, message.getContent(), message.getTimestamp()));

        });
        return historyChatHistory;
    }


    public Message update(UUID id, MessageDto.Update sendReceiveMessageDto) {
        final Message oldMessage = this.findById(id);
        oldMessage.setContent(sendReceiveMessageDto.getContent());
        return this.messageRepository.save(oldMessage);
    }

    public void delete(UUID messageId) {
        final Message message = this.findById(messageId);
        this.messageRepository.delete(message);
    }

    public List<Message> findByRecipientAndSender(UUID recipientId, UUID senderId) {
        return this.messageRepository.findByRecipientIdAndSenderId(recipientId, senderId);
    }

}
