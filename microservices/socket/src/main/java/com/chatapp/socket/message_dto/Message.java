package com.chatapp.socket.message_dto;

import lombok.Data;
import org.modelmapper.ModelMapper;

import java.time.Instant;
import java.util.UUID;


public class Message {
    private Message() {
    }

    @Data
    public static class Base {
        private UUID senderId;
        private String destination;

        private String senderUsername;
        private String content;
        private Instant timestamp;

        public fromMessageService toDisplay() {
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(this, fromMessageService.class);
        }

        public Persist toPersist() {
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(this, Persist.class);
        }
    }

    @Data
    public static class fromMessageService {
        private UUID messageId;
        private UUID senderId;
        private String content;
        private Instant timestamp;
    }
    @Data
    public static class fromGroupService {
        private UUID messageId;
        private UUID roomId;
    }

    @Data
    public static class Persist{
        private UUID senderId;
        private String content;
        private Instant timestamp;
    }

}
