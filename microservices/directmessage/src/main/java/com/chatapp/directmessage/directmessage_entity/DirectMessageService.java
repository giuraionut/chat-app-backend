package com.chatapp.directmessage.directmessage_entity;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public record DirectMessageService(DirectMessageRepository directMessageRepository) {

    public DirectMessage add(DirectMessage directMessage) {
        return this.directMessageRepository.save(directMessage);
    }

    public DirectMessage update(UUID id, DirectMessageDto.Update sendReceiveDirectMessageDto) {
        final DirectMessage oldDirectMessage = this.findById(id);
        oldDirectMessage.setContent(sendReceiveDirectMessageDto.getContent());
        return this.directMessageRepository.save(oldDirectMessage);
    }

    public DirectMessage findById(UUID id) {
        return this.directMessageRepository.findById(id).orElseThrow();
    }

    public void delete(UUID id) {
        final DirectMessage directMessage = this.findById(id);
        this.directMessageRepository.delete(directMessage);
    }
}
