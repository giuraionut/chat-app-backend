package com.chatapp.directmessage.entity;

import com.chatapp.directmessage.dto.DirectMessageDto;
import com.chatapp.directmessage.user.User;
import com.chatapp.directmessage.utils.Route;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public record DirectMessageService(DirectMessageRepository directMessageRepository, RestTemplate restTemplate) {

    public DirectMessage create(DirectMessage directMessage) {
        return this.directMessageRepository.save(directMessage);
    }

    public DirectMessage readById(UUID id) {
        return this.directMessageRepository.findById(id).orElseThrow();
    }

    public DirectMessageDto.ReturnWithUsers readMessageWithUsers(UUID id) {
        final DirectMessage directMessage = this.directMessageRepository.findById(id).orElseThrow();

        final UUID senderId = directMessage.getSenderId();
        final UUID receiverId = directMessage.getReceiverId();

        final User sender = restTemplate.getForObject(Route.User.GET.BY_ID + senderId, User.class);
        final User receiver = restTemplate.getForObject(Route.User.GET.BY_ID + receiverId, User.class);

        return new DirectMessageDto.ReturnWithUsers(sender,
                receiver,
                directMessage.getContent(),
                directMessage.getTimestamp());

    }

    public DirectMessage update(UUID id, DirectMessageDto.Update sendReceiveDirectMessageDto) {
        final DirectMessage oldDirectMessage = this.readById(id);
        oldDirectMessage.setContent(sendReceiveDirectMessageDto.getContent());
        return this.directMessageRepository.save(oldDirectMessage);
    }

    public void delete(UUID id) {
        final DirectMessage directMessage = this.readById(id);
        this.directMessageRepository.delete(directMessage);
    }


}
