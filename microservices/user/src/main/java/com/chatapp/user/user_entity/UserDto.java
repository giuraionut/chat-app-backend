package com.chatapp.user.user_entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

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

}
