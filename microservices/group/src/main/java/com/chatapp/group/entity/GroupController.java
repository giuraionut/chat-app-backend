package com.chatapp.group.entity;

import com.chatapp.group.components.*;
import com.chatapp.group.dto.*;
import com.chatapp.group.exceptions.CustomException;
import com.chatapp.group.permissions.AccessManager;
import com.chatapp.group.permissions.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
@RestController
@RequestMapping(path = "api/v1/group")
public record GroupController(GroupService groupService) {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);

    @PostMapping()
    public GroupDto.Display createGroup(@RequestBody GroupDto.Base baseGroup, Principal principal) {
        final GroupEntity groupEntity = baseGroup.toEntity();
        return this.groupService.createGroup(groupEntity, principal).toDisplay();
    }

    @PostMapping(path = "{groupId}/member")
    public GroupDto.Display addMember(@PathVariable("groupId") UUID groupId, @RequestBody MemberDto.Add member) throws CustomException {
        return this.groupService.addMember(groupId, member.getUserId()).toDisplay();
    }

    @PostMapping(path = "{groupId}/category")
    public CategoryDto.Display createCategory(@RequestBody CategoryDto.Base baseCategory, @PathVariable("groupId") UUID groupId,
                                              Principal principal) throws CustomException {
        final GroupEntity group = this.groupService.findGroupById(groupId);

        AccessManager.checkPermission(principal, group, Permission.ROOMS_MANAGE);
        final Category category = baseCategory.toEntity();
        final GroupDto.Update updatedGroup = group.toUpdate();
        updatedGroup.setCategory(category);
        this.groupService.updateGroup(groupId, updatedGroup);
        return category.toDisplay();
    }

    @PostMapping(path = "{groupId}/category/{categoryId}/room")
    public RoomDto.Display createRoom(@RequestBody RoomDto.Base baseRoom, @PathVariable("groupId") UUID groupId,
                                      @PathVariable("categoryId") UUID categoryId, Principal principal) throws CustomException {
        final GroupEntity group = this.groupService.findGroupById(groupId);

        AccessManager.checkPermission(principal, group, Permission.ROOMS_MANAGE);

        final Room room = baseRoom.toEntity();
        room.setType(RoomType.PUBLIC);
        group.getCategories().stream().filter(c -> c.getId().equals(categoryId)).findFirst().orElseThrow().addRoom(room);
        this.groupService.updateGroup(groupId, group.toUpdate());
        return room.toDisplay();
    }

    @GetMapping(path = "{groupId}")
    public GroupDto.Display readGroup(@PathVariable("groupId") UUID groupId, Principal principal) throws CustomException {
        final GroupEntity group = this.groupService.findGroupById(groupId);

        AccessManager.checkPermission(principal, group, Permission.GROUP_VIEW);

        return group.toDisplay();
    }

    @GetMapping(path = "{groupId}/categories")
    public List<CategoryDto.Display> getGroupCategories(@PathVariable("groupId") UUID groupId, Principal principal) throws CustomException {
        final GroupEntity group = this.groupService.findGroupById(groupId);

        AccessManager.checkPermission(principal, group, Permission.ROOMS_VIEW);

        return group.getCategories().stream().map(Category::toDisplay).toList();
    }

    @GetMapping(path = "{groupId}/category/{categoryId}/rooms")
    public List<RoomDto.Display> getGroupRooms(@PathVariable("groupId") UUID groupId, @PathVariable("categoryId") UUID categoryId,
                                               Principal principal) throws CustomException {
        final GroupEntity group = this.groupService.findGroupById(groupId);

        AccessManager.checkPermission(principal, group, Permission.ROOMS_VIEW);

        final Category requestedCategory = group.getCategories()
                .stream().filter(category -> category.getId().equals(categoryId))
                .findFirst().orElseThrow();
        return requestedCategory.getRooms().stream().map(Room::toDisplay).toList();
    }

    @GetMapping(path = "{groupId}/users")
    public List<MemberDto.Display> getUsers(@PathVariable("groupId") UUID groupId, Principal principal) throws CustomException {

        final GroupEntity group = this.groupService.findGroupById(groupId);

        AccessManager.checkPermission(principal, group, Permission.GROUP_VIEW);

        return group.getMembers().stream().map(Member::toDisplay).toList();
    }

    @GetMapping(path = "{groupId}/roles")
    public List<RoleDto> getGroupRoles(@PathVariable("groupId") UUID groupId, Principal principal) throws CustomException {

        final GroupEntity group = this.groupService.findGroupById(groupId);

        AccessManager.checkPermission(principal, group, Permission.GROUP_VIEW);

        return group.getRoles().stream().map(Role::toRoleDto).toList();
    }

    @PutMapping(path = "{groupId}")
    public GroupDto.Display updateGroup(@PathVariable("groupId") UUID groupId, @RequestBody GroupDto.Update updatedGroup, Principal principal) throws CustomException {
        final GroupEntity oldGroup = this.groupService.findGroupById(groupId);

        AccessManager.checkPermission(principal, oldGroup, Permission.GROUP_MANAGE);

        return this.groupService.updateGroup(groupId, updatedGroup).toDisplay();
    }

    @PutMapping(path = "{groupId}/category/{categoryId}")
    public CategoryDto.Display updateCategory(@PathVariable("groupId") UUID groupId, @PathVariable("categoryId") UUID categoryId,
                                              @RequestBody CategoryDto.Update updatedCategory, Principal principal) throws CustomException {
        final GroupEntity group = this.groupService.findGroupById(groupId);

        AccessManager.checkPermission(principal, group, Permission.ROOMS_MANAGE);

        return this.groupService.updateCategory(group, categoryId, updatedCategory);
    }

    @PutMapping(path = "{groupId}/category/{categoryId}/room/{roomId}")
    public RoomDto.Display updateRoom(@PathVariable("groupId") UUID groupId, @PathVariable("categoryId") UUID categoryId,
                                      @PathVariable("roomId") UUID roomId,
                                      @RequestBody RoomDto.Update updatedRoom, Principal principal) throws CustomException {
        final GroupEntity group = this.groupService.findGroupById(groupId);

        AccessManager.checkPermission(principal, group, Permission.ROOMS_MANAGE);

        return this.groupService.updateRoom(group, categoryId, roomId, updatedRoom);
    }

    @DeleteMapping(path = "{groupId}")
    public String deleteGroup(@PathVariable("groupId") UUID groupId, Principal principal) throws CustomException {
        final GroupEntity group = this.groupService.findGroupById(groupId);
        AccessManager.checkPermission(principal, group);
        this.groupService.deleteGroup(group);
        return group.getName() + " deleted successfully";
    }

    @PatchMapping(path = "{groupId}/category/{categoryId}/room/{roomId}/message")
    public MessageDto.Display persistMessage(@PathVariable("groupId") UUID groupId, @PathVariable("categoryId") UUID categoryId,
                                             @PathVariable("roomId") UUID roomId, Principal principal, @RequestBody MessageDto.Base message) throws CustomException {
        final GroupEntity group = this.groupService.findGroupById(groupId);
        AccessManager.checkPermission(principal, group, Permission.GROUP_VIEW);
        final MessageDto.Display display = this.groupService.addMessageInRoom(group, categoryId, roomId, message);
        System.out.println(display);
        return display;
    }

}
