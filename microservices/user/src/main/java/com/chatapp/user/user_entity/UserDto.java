package com.chatapp.user.user_entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.modelmapper.ModelMapper;

import java.util.UUID;

@Data
public class UserDto {
    private UserDto() {
    }

    @Data
    public static class Register {
        private String username;
        private String password;
        private String email;

        public User toUser() {
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(this, User.class);
        }
    }

    @Data
    public static class Return {
        private UUID id;
        private String username;
        private String email;
        private String avatar;

        public User toUser() {
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(this, User.class);
        }
    }

    @Data
    public static class Update {
        private String username;
        private String password;
        private String email;
        private String avatar;

        public User toUser() {
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(this, User.class);
        }
    }

    @Data
    @EqualsAndHashCode
    public static class Identifier {
        private UUID id;
    }
}
