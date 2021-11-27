package com.chatapp.group.entity;

import com.chatapp.group.components.Category;
import com.chatapp.group.components.Room;
import com.chatapp.group.dto.CategoryDto;
import com.chatapp.group.dto.GroupDto;
import com.chatapp.group.dto.RoomDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/group")
public record GroupController(GroupService groupService) {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);

    private static final String GROUP_NOT_FOUND = "Group does not exists";
    private static final String CATEGORY_NOT_FOUND = "Category does not exists";

    private static final String NO_ACCESS = "You don't have enough privileges to access this resource";

    @PostMapping()
    public GroupDto.Display createGroup(@RequestBody GroupDto.Base baseGroup, HttpServletResponse response, Principal principal) {
        final GroupEntity groupEntity = baseGroup.toEntity();
        return this.groupService.createGroup(groupEntity).toDisplay();
    }


    @PostMapping(path = "{groupId}/category")
    public CategoryDto.Display createCategory(@RequestBody CategoryDto.Base baseCategory, @PathVariable("groupId") UUID groupId,
                                              HttpServletResponse response, Principal principal) {
        try {
            final GroupEntity group = this.groupService.findGroupById(groupId);
            if (group.getOwnerId().equals(UUID.fromString(principal.getName()))) {
                final Category category = baseCategory.toEntity();
                group.addCategory(category);
                this.groupService.updateGroup(groupId, group.toUpdate());
                return category.toDisplay();
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, NO_ACCESS);
            }
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    GROUP_NOT_FOUND);
        }
    }

    @PostMapping(path = "{groupId}/category/{categoryId}/room")
    public RoomDto.Display createRoom(@RequestBody RoomDto.Base baseRoom, @PathVariable("groupId") UUID groupId, @PathVariable("categoryId") UUID categoryId,
                                      HttpServletResponse response, Principal principal) {
        try {
            final GroupEntity group = this.groupService.findGroupById(groupId);
            if (group.getOwnerId().equals(UUID.fromString(principal.getName()))) {
                try {
                    final Room room = baseRoom.toEntity();
                    room.setType(1);
                    group.getCategories().stream().filter(c -> c.getId().equals(categoryId)).findFirst().orElseThrow().addRoom(room);
                    this.groupService.updateGroup(groupId, group.toUpdate());
                    return room.toDisplay();
                } catch (NoSuchElementException ex) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                            CATEGORY_NOT_FOUND);
                }
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, NO_ACCESS);
            }
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    GROUP_NOT_FOUND);
        }
    }

    @GetMapping(path = "{groupId}")
    public GroupDto.Display readGroup(@PathVariable("groupId") UUID groupId, HttpServletResponse response, Principal principal) {
        try {
            final GroupEntity group = this.groupService.findGroupById(groupId);
            final UUID ownerId = group.getOwnerId();
            final List<UUID> usersId = group.getUsersId();
            LOGGER.info("PRINCIPAL: {}", principal.getName());

            if (ownerId.equals(UUID.fromString(principal.getName())) || usersId.contains(UUID.fromString(principal.getName()))) {
                return group.toDisplay();
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, NO_ACCESS);
            }
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    GROUP_NOT_FOUND);
        }
    }

    @GetMapping(path = "{groupId}/categories")
    public List<CategoryDto.Display> getGroupCategories(@PathVariable("groupId") UUID groupId, HttpServletResponse response, Principal principal) {
        try {
            final GroupEntity group = this.groupService.findGroupById(groupId);
            final UUID ownerId = group.getOwnerId();
            final List<UUID> usersId = group.getUsersId();
            LOGGER.info("PRINCIPAL: {}", principal.getName());

            if (ownerId.equals(UUID.fromString(principal.getName())) || usersId.contains(UUID.fromString(principal.getName()))) {
                return group.getCategories().stream().map(Category::toDisplay).collect((Collectors.toList()));
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, NO_ACCESS);
            }
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    GROUP_NOT_FOUND);
        }
    }

    @GetMapping(path = "{groupId}/category/{categoryId}/rooms")
    public List<RoomDto.Display> getGroupRooms(@PathVariable("groupId") UUID groupId, @PathVariable("categoryId") UUID categoryId, HttpServletResponse response,
                                               Principal principal) {
        try {
            final GroupEntity group = this.groupService.findGroupById(groupId);
            final UUID ownerId = group.getOwnerId();
            final List<UUID> usersId = group.getUsersId();
            LOGGER.info("PRINCIPAL: {}", principal.getName());

            if (ownerId.equals(UUID.fromString(principal.getName())) || usersId.contains(UUID.fromString(principal.getName()))) {
                final Category requestedCategory = group.getCategories()
                        .stream().filter(category -> category.getId().equals(categoryId))
                        .findFirst().orElseThrow();
                return requestedCategory.getRooms().stream().map(Room::toDisplay).collect(Collectors.toList());

            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, NO_ACCESS);
            }
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    GROUP_NOT_FOUND);
        }
    }
//    @PutMapping()
//
//    @DeleteMapping()
}
