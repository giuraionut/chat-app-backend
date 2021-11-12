package com.chatapp.user.user_entity;

import org.springframework.stereotype.Service;

@Service
public record UserService(UserRepository userRepository) {
    public User add(User user) {
        return this.userRepository.save(user);
    }

    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username).orElse(null);
    }

    public boolean delete(String username) {
        final User user = findByUsername(username);
        if (user != null) {
            this.userRepository.delete(user);
            return true;
        } else {
            return false;
        }
    }
}
