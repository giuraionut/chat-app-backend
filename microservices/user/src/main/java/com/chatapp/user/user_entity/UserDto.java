package com.chatapp.user.user_entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private String password;
    private String email;
    //private Instant dateOfBirth;
    private String avatar;

    public User toUser() {
        return new User(username, password, email, /*dateOfBirth,*/ avatar);
    }
}
