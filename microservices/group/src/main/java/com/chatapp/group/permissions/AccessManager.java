package com.chatapp.group.permissions;

import com.chatapp.group.components.Role;
import com.chatapp.group.entity.GroupEntity;
import com.chatapp.group.exceptions.CustomException;
import com.chatapp.group.exceptions.ExceptionResource;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

public class AccessManager {

    private AccessManager() {
    }

    public static void checkPermission(Principal principal, GroupEntity group, Permission permission) throws CustomException {
        final Optional<Role> role = group.getRoles().stream().filter(r -> r.getUsersId().contains(UUID.fromString(principal.getName()))).findFirst();
        if (role.isEmpty()) {
            throw new CustomException(ExceptionResource.ACCESS_DENIED);
        }

        if (role.get().getPermissions()
                .stream().filter(p -> Permission.valueOf(p).equals(permission)).findFirst().isEmpty()) {
            throw new CustomException(ExceptionResource.ACCESS_DENIED);
        }

    }
}
