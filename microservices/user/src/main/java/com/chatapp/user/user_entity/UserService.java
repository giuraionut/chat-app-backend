package com.chatapp.user.user_entity;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public record UserService(UserRepository userRepository) {

    public User add(User user) {
        return this.userRepository.save(user);
    }

    public User update(String username, UserDto.Update userUpdate) {
        final User oldUser = this.findByUsername(username);
        final User newUser = userUpdate.toUser();
        newUser.setId(oldUser.getId());
        return this.userRepository.save(newUser);
    }

    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username).orElseThrow();
    }

    public void delete(String username) {
        final User user = findByUsername(username);
        this.userRepository.delete(user);
    }

    public User findById(UUID id) {
        return this.userRepository.findById(id).orElseThrow();
    }
}
