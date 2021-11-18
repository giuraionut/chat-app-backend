package com.chatapp.user.user_entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.util.Set;
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

    private String avatar;

    public UserDto.Register toRegisterDto() {
        return new ModelMapper().map(this, UserDto.Register.class);
    }

    public UserDto.Update toUpdateDto() {
        return new ModelMapper().map(this, UserDto.Update.class);
    }

    public UserDto.Return toReturnDto() {
        return new ModelMapper().map(this, UserDto.Return.class);
    }
}
