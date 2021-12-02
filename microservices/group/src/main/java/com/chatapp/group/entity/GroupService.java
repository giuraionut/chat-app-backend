package com.chatapp.group.entity;

import com.chatapp.group.components.Category;
import com.chatapp.group.components.Role;
import com.chatapp.group.components.Room;
import com.chatapp.group.components.RoomType;
import com.chatapp.group.dto.CategoryDto;
import com.chatapp.group.dto.GroupDto;
import com.chatapp.group.exceptions.CustomException;
import com.chatapp.group.exceptions.ExceptionResource;
import com.chatapp.group.permissions.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.UUID;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Transactional
    public GroupEntity createGroup(GroupEntity group, Principal principal) {
        group.setOwnerId(UUID.fromString(principal.getName()));
        Role role = new Role();
        role.setName("OWNER");
        role.addPermissions(Permission.MESSAGES_MANAGE);
        role.addPermissions(Permission.ROOMS_MANAGE);
        role.addPermissions(Permission.ROOMS_VIEW);
        role.addUsers(UUID.fromString(principal.getName()));
        Category category = new Category();
        category.setName("text channels");
        Room room = new Room();
        room.setName("general");
        room.setType(RoomType.PUBLIC);
        category.addRoom(room);
        group.generateRole(role);
        group.addCategory(category);
        group.addUser(UUID.fromString(principal.getName()));
        return this.groupRepository.save(group);
    }

    @Transactional
    public GroupEntity addMember(UUID groupId, UUID userId) throws CustomException {
        final GroupEntity group = this.findGroupById(groupId);
        try {
            group.getGeneratedRoles().stream().filter(role -> role.getName().equals("MEMBER")).findFirst()
                    .ifPresent(r -> r.addUsers(userId));
        } catch (NullPointerException ex) {
            Role role = new Role();
            role.setName("MEMBER");
            role.addPermissions(Permission.GROUP_VIEW);
            role.addPermissions(Permission.ROOMS_VIEW);
            role.addUsers(userId);
            group.generateRole(role);
        }
        group.addUser(userId);
        return this.groupRepository.save(group);
    }

    public GroupEntity findGroupById(UUID groupId) throws CustomException {
        return this.groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ExceptionResource.GROUP_NOT_FOUND));
    }

    public GroupEntity updateGroup(UUID id, GroupDto.Update newGroup) throws CustomException {
        final GroupEntity oldGroupEntity = this.findGroupById(id);
        oldGroupEntity.setName(newGroup.getName());
        oldGroupEntity.setAvatar(newGroup.getAvatar());
        return this.groupRepository.save(oldGroupEntity);
    }

    public void deleteGroup(UUID groupId) throws CustomException {
        final GroupEntity groupEntity = this.findGroupById(groupId);
        this.groupRepository.delete(groupEntity);
    }

    public CategoryDto.Display updateCategory(GroupEntity groupEntity, UUID categoryId, CategoryDto.Update newCategory) throws CustomException {
        final Category category = groupEntity.getCategories().stream().filter(c -> c.getId().equals(categoryId))
                .findFirst().orElseThrow(() -> new CustomException(ExceptionResource.CATEGORY_NOT_FOUND));

        category.setName(newCategory.getName());
        this.groupRepository.save(groupEntity);
        return category.toDisplay();
    }


}
