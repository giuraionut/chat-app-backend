package com.chatapp.message.dto;

import com.chatapp.message.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.time.Instant;
import java.util.UUID;

@Data
public class MessageDto {
    private MessageDto() {
    }

    @Data
    public static class Base {
        private UUID senderId;
        private UUID recipientId;
        private String content;
        private Instant timestamp;

        public Message toMessage() {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(this, Message.class);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Display {
        private UUID sender;
        private String content;
        private Instant timestamp;
    }

    @Data
    public static class Update {
        private String content;

        public Message toMessage() {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(this, Message.class);
        }
    }

    @Data
    public static class Identifier{
        private UUID id;
    }
}
