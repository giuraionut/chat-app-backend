package com.chatapp.directmessage.dto;

import com.chatapp.directmessage.entity.DirectMessage;
import com.chatapp.directmessage.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReturnWithUsers{
        private User sender;
        private User receiver;
        private String content;
        private Instant timestamp;
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
