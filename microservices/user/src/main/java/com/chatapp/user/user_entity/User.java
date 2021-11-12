package com.chatapp.user.user_entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    //    private Instant dateOfBirth;
    private String avatar;

    public User(String username, String password, String email, /*Instant dateOfBirth,*/ String avatar) {
        this.username = username;
        this.password = password;
        this.email = email;
        //this.dateOfBirth = dateOfBirth;
        this.avatar = avatar;
    }

    public UserDto toDto() {
        return new UserDto(username, password, email, /*dateOfBirth,*/ avatar);
    }
}
