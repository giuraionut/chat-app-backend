package com.chatapp.group.entity;

import com.chatapp.group.components.Category;
import com.chatapp.group.components.Room;
import com.chatapp.group.dto.GroupDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public record GroupService(GroupRepository groupRepository) {

    public GroupEntity createGroup(GroupEntity groupEntity) {
        Category category = new Category();
        category.setName("text channels");
        Room room = new Room();
        room.setName("general");
        room.setType(1);
        category.addRoom(room);
        groupEntity.addCategory(category);
        return this.groupRepository.save(groupEntity);
    }

    public GroupEntity findGroupById(UUID groupId) {
        return this.groupRepository.findById(groupId).orElseThrow();
    }

    public GroupEntity updateGroup(UUID id, GroupDto.Update newGroup) {
        final GroupEntity oldGroupEntity = this.findGroupById(id);
        oldGroupEntity.setName(newGroup.getName());
        return this.groupRepository.save(oldGroupEntity);
    }

    public void deleteGroup(UUID groupId) {
        final GroupEntity groupEntity = this.findGroupById(groupId);
        this.groupRepository.delete(groupEntity);
    }

}
