package com.chatapp.message.user;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserSet {
    private List<User> users;

    public UserSet() {
        users = new ArrayList<>();
    }
}
