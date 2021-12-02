package com.chatapp.group.permissions;

import com.chatapp.group.components.Role;
import com.chatapp.group.entity.GroupController;
import com.chatapp.group.entity.GroupEntity;
import com.chatapp.group.exceptions.CustomException;
import com.chatapp.group.exceptions.ExceptionResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.util.UUID;

public class AccessManager {

    private AccessManager() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);


    public static void checkPermission(Principal principal, GroupEntity group, Permission... permission) throws CustomException {
        if (group.getOwnerId().equals(UUID.fromString(principal.getName()))) {
            return;
        }

        final Role role = group.getRoles().stream().filter(r -> r.getUsersId().contains(UUID.fromString(principal.getName()))).findFirst()
                .orElseThrow(() -> new CustomException(ExceptionResource.ACCESS_DENIED));
        role.getPermissions()
                .stream().filter(p -> Permission.valueOf(p).equals(permission[0])).findFirst().orElseThrow(() -> new CustomException(ExceptionResource.ACCESS_DENIED));
    }
}
