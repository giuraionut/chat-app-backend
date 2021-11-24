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
        private UUID recipientId;

        private String senderUsername;
        private String content;
        private Instant timestamp;

        public Display toDisplay() {
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(this, Display.class);
        }

        public Persist toPersist() {
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(this, Persist.class);
        }
    }

    @Data
    public static class Display{
        private String senderUsername;
        private String content;
        private Instant timestamp;
    }
    @Data
    public static class Persist{
        private UUID senderId;
        private UUID recipientId;

        private String content;
        private Instant timestamp;
    }

}
