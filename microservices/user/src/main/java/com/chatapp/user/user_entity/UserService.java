package com.chatapp.user.user_entity;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public record UserService(UserRepository userRepository) {

    public User add(User user) {
        return this.userRepository.save(user);
    }

    public User update(UUID userId, UserDto.Update userUpdate) {
        final User oldUser = this.findById(userId);
        final User newUser = userUpdate.toUser();
        newUser.setId(oldUser.getId());
        return this.userRepository.save(newUser);
    }

    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username).orElseThrow();
    }

    public void delete(UUID userId) {
        final User user = findById(userId);
        this.userRepository.delete(user);
    }

    public User findById(UUID id) {
        return this.userRepository.findById(id).orElseThrow();
    }

    public List<UserDto.Return> findAllById(List<UserDto.Identifier> ids) {
        final List<User> users = this.userRepository.findAllById(ids.stream().map(UserDto.Identifier::getId).toList());
        return users.stream().map(User::toReturnDto).toList();
    }

}
