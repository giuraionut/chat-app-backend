package com.chatapp.group.entity;

import com.chatapp.group.components.*;
import com.chatapp.group.dto.CategoryDto;
import com.chatapp.group.dto.GroupDto;
import com.chatapp.group.dto.RoomDto;
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

        Member member = new Member();
        member.setUserId(UUID.fromString(principal.getName()));

        Role role = new Role();
        role.setName("OWNER");
        role.addPermissions(Permission.MESSAGES_MANAGE);
        role.addPermissions(Permission.ROOMS_MANAGE);
        role.addPermissions(Permission.ROOMS_VIEW);
        member.addRole(role);

        Category category = new Category();
        category.setName("text channels");

        Room room = new Room();
        room.setCategory(category);
        room.setName("general");
        room.setType(RoomType.PUBLIC);
        category.addRoom(room);

        group.generateRole(role);
        group.addCategory(category);
        group.addMember(member);
        return this.groupRepository.save(group);
    }

    @Transactional
    public GroupEntity addMember(UUID groupId, UUID userId) throws CustomException {
        final GroupEntity group = this.findGroupById(groupId);
        Member member = new Member();
        member.setUserId(userId);
        try {
            group.getGeneratedRoles().stream().filter(role -> role.getName().equals("MEMBER")).findFirst()
                    .ifPresent(member::addRole);
        } catch (NullPointerException ex) {
            Role role = new Role();
            role.setName("MEMBER");
            role.addPermissions(Permission.GROUP_VIEW);
            role.addPermissions(Permission.ROOMS_VIEW);
            group.generateRole(role);
            member.addRole(role);
        }
        group.addMember(member);
        return this.groupRepository.save(group);
    }

    public GroupEntity findGroupById(UUID groupId) throws CustomException {
        return this.groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ExceptionResource.GROUP_NOT_FOUND));
    }

    @Transactional
    public GroupEntity updateGroup(UUID id, GroupDto.Update newGroup) throws CustomException {
        final GroupEntity oldGroupEntity = this.findGroupById(id);
        oldGroupEntity.setName(newGroup.getName());
        oldGroupEntity.setAvatar(newGroup.getAvatar());
        if (newGroup.getCategory() != null)
            oldGroupEntity.addCategory(newGroup.getCategory());
        return this.groupRepository.save(oldGroupEntity);
    }

    @Transactional
    public void deleteGroup(GroupEntity group) throws CustomException {
        final GroupEntity groupEntity = this.findGroupById(group.getId());
        this.groupRepository.delete(groupEntity);
    }

    @Transactional
    public CategoryDto.Display updateCategory(GroupEntity groupEntity, UUID categoryId, CategoryDto.Update newCategory) throws CustomException {
        final Category category = groupEntity.getCategories().stream().filter(c -> c.getId().equals(categoryId))
                .findFirst().orElseThrow(() -> new CustomException(ExceptionResource.CATEGORY_NOT_FOUND));

        category.setName(newCategory.getName());
        this.groupRepository.save(groupEntity);
        return category.toDisplay();
    }

    @Transactional
    public RoomDto.Display updateRoom(GroupEntity group, UUID categoryId, UUID roomId, RoomDto.Update updatedRoom) throws CustomException {
        final Category category = group.getCategoryById(categoryId);
        final Room room = category.getRoomById(roomId);
        room.setName(updatedRoom.getName());
        if (updatedRoom.getCategoryId() != null) {
            final Category newCategory = group.getCategoryById(updatedRoom.getCategoryId());
            newCategory.addRoom(room);
        }
        this.groupRepository.save(group);
        return room.toDisplay();
    }
}
