package com.chatapp.group.permissions;

import java.util.List;

import static com.chatapp.group.permissions.Permission.*;

public enum Role {
    OWNER(List.of(
            CATEGORY_ALL,
            ROOM_ALL,
            MESSAGE_ALL)),
    ADMIN(List.of(
            CATEGORY_ALL,
            ROOM_ALL,
            MESSAGE_CREATE, MESSAGE_DELETE)),
    MOD(List.of(
            CATEGORY_ALL,
            ROOM_ALL,
            MESSAGE_ALL)),
    USER(List.of(
            CATEGORY_ALL,
            ROOM_ALL));

    private final List<Permission> permissions;

    Role(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
