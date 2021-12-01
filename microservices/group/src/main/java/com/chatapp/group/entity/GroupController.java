package com.chatapp.group.entity;

import com.chatapp.group.components.Category;
import com.chatapp.group.components.Role;
import com.chatapp.group.components.Room;
import com.chatapp.group.dto.*;
import com.chatapp.group.exceptions.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/group")
public record GroupController(GroupService groupService) {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);

    @PostMapping()
    public GroupDto.Display createGroup(@RequestBody GroupDto.Base baseGroup, Principal principal) {
        final GroupEntity groupEntity = baseGroup.toEntity();
        return this.groupService.createGroup(groupEntity, principal).toDisplay();
    }

    @PostMapping(path = "{groupId}/category")
    public CategoryDto.Display createCategory(@RequestBody CategoryDto.Base baseCategory, @PathVariable("groupId") UUID groupId,
                                              Principal principal) throws CustomException {
        final GroupEntity group = this.groupService.findGroupById(groupId);
        group.checkPermission(UUID.fromString(principal.getName()));
        final Category category = baseCategory.toEntity();
        group.addCategory(category);
        this.groupService.updateGroup(groupId, group.toUpdate());
        return category.toDisplay();

    }

    @PostMapping(path = "{groupId}/category/{categoryId}/room")
    public RoomDto.Display createRoom(@RequestBody RoomDto.Base baseRoom, @PathVariable("groupId") UUID groupId,
                                      @PathVariable("categoryId") UUID categoryId, Principal principal) throws CustomException {
        final GroupEntity group = this.groupService.findGroupById(groupId);
        group.checkPermission(UUID.fromString(principal.getName()));
        final Room room = baseRoom.toEntity();
        room.setType(1);
        group.getCategories().stream().filter(c -> c.getId().equals(categoryId)).findFirst().orElseThrow().addRoom(room);
        this.groupService.updateGroup(groupId, group.toUpdate());
        return room.toDisplay();

    }

    @GetMapping(path = "{groupId}")
    public GroupDto.Display readGroup(@PathVariable("groupId") UUID groupId, Principal principal) throws CustomException {
        final GroupEntity group = this.groupService.findGroupById(groupId);

        group.checkPermission(UUID.fromString(principal.getName()));
        return group.toDisplay();
    }

    @GetMapping(path = "{groupId}/categories")
    public List<CategoryDto.Display> getGroupCategories(@PathVariable("groupId") UUID groupId, Principal principal) throws CustomException {
        final GroupEntity group = this.groupService.findGroupById(groupId);

        group.checkPermission(UUID.fromString(principal.getName()));
        return group.getCategories().stream().map(Category::toDisplay).collect((Collectors.toList()));
    }

    @GetMapping(path = "{groupId}/category/{categoryId}/rooms")
    public List<RoomDto.Display> getGroupRooms(@PathVariable("groupId") UUID groupId, @PathVariable("categoryId") UUID categoryId,
                                               Principal principal) throws CustomException {
        final GroupEntity group = this.groupService.findGroupById(groupId);
        group.checkPermission(UUID.fromString(principal.getName()));
        final Category requestedCategory = group.getCategories()
                .stream().filter(category -> category.getId().equals(categoryId))
                .findFirst().orElseThrow();
        return requestedCategory.getRooms().stream().map(Room::toDisplay).collect(Collectors.toList());
    }

    @GetMapping(path = "{groupId}/users")
    public List<UserDto> getUsers(@PathVariable("groupId") UUID groupId) throws CustomException {
        final GroupEntity groupById = this.groupService.findGroupById(groupId);
        final List<Role> roles = groupById.getRoles();
        final List<UUID> usersId = groupById.getUsersId();

        List<UserDto> users = new ArrayList<>();


        for (UUID uuid : usersId) {
            List<RoleDto> userRoles = new ArrayList<>();
            for (Role role : roles) {
                if (role.getUsersId().contains(uuid)) {
                    userRoles.add(role.toRoleDto());
                }
            }
            users.add(new UserDto(uuid, userRoles));
        }
        return users;
    }

    @PutMapping(path = "{groupId}")
    public GroupDto.Display updateGroup(@PathVariable("groupId") UUID groupId, @RequestBody GroupDto.Update newGroup, Principal principal) throws CustomException {
        final GroupEntity oldGroup = this.groupService.findGroupById(groupId);
        oldGroup.checkPermission(UUID.fromString(principal.getName()));
        return this.groupService.updateGroup(groupId, newGroup).toDisplay();
    }

    @PutMapping(path = "{groupId}/category/{categoryId}")
    public CategoryDto.Display updateCategory(@PathVariable("groupId") UUID groupId, @PathVariable("categoryId") UUID categoryId,
                                              @RequestBody CategoryDto.Update newCategory, Principal principal) throws CustomException {
        final GroupEntity groupEntity = this.groupService.findGroupById(groupId);
        groupEntity.checkPermission(UUID.fromString(principal.getName()));
        return this.groupService.updateCategory(groupEntity, categoryId, newCategory);

    }
//    @DeleteMapping()
}
