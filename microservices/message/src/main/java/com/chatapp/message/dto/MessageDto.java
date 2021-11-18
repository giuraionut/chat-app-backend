package com.chatapp.message.dto;

import com.chatapp.message.entity.Message;
import com.chatapp.message.user.User;
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
    @EqualsAndHashCode
    public static class Pure {
        private UUID senderId;
        private UUID recipientId;
        private String content;

        public Message toMessage() {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(this, Message.class);
        }
    }

    @Data
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Built {
        private User sender;
        private String content;
        private Instant timestamp;
    }

    @Data
    @EqualsAndHashCode
    public static class Update {
        private String content;

        public Message toMessage() {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(this, Message.class);
        }
    }

    @Data
    @EqualsAndHashCode
    public static class Identifier{
        private UUID id;
    }
}
