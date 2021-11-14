package com.chatapp.directmessage.directmessage_entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.time.Instant;
import java.util.UUID;

@Data
public class DirectMessageDto {
    private DirectMessageDto() {
    }

    @Data
    @EqualsAndHashCode
    public static class Create {
        private UUID senderId;
        private UUID receiverId;
        private String content;

        public DirectMessage toDirectMessage() {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(this, DirectMessage.class);
        }
    }

    @Data
    @EqualsAndHashCode
    public static class Return {
        private UUID senderId;
        private UUID receiverId;
        private String content;
        private Instant timestamp;

        public DirectMessage toDirectMessage() {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(this, DirectMessage.class);
        }
    }

    @Data
    @EqualsAndHashCode
    public static class Update {
        private String content;

        public DirectMessage toDirectMessage() {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(this, DirectMessage.class);
        }
    }
}
