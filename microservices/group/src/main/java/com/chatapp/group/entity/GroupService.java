package com.chatapp.group.entity;

import com.chatapp.group.components.Category;
import com.chatapp.group.components.Room;
import com.chatapp.group.dto.CategoryDto;
import com.chatapp.group.dto.GroupDto;
import com.chatapp.group.exceptions.CustomException;
import com.chatapp.group.exceptions.ExceptionResource;
import org.springframework.stereotype.Service;

import java.util.Optional;
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

    public GroupEntity findGroupById(UUID groupId) throws CustomException {
        final Optional<GroupEntity> group = this.groupRepository.findById(groupId);

        if (group.isEmpty()) {
            throw new CustomException(ExceptionResource.GROUP_NOT_FOUND);
        }
        return group.get();
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
        final Optional<Category> optionalCategory = groupEntity.getCategories().stream().filter(c -> c.getId().equals(categoryId))
                .findFirst();

        if (optionalCategory.isEmpty()) {
            throw new CustomException(ExceptionResource.CATEGORY_NOT_FOUND);
        }

        final Category category = optionalCategory.get();
        category.setName(newCategory.getName());
        this.groupRepository.save(groupEntity);
        return category.toDisplay();
    }

}
