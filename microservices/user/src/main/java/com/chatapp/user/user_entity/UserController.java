package com.chatapp.user.user_entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping(path = "api/v1/user")
public record UserController(UserService userService) {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping()
    public UserDto createUser(@RequestBody UserDto userDto, HttpServletResponse response) throws IOException {
        try {
            response.setStatus(HttpStatus.CREATED.value());
            return this.userService.add(userDto.toUser()).toDto();
        } catch (Exception ex) {
            response.sendError(HttpStatus.CONFLICT.value(), "Username already exists");
            logger.trace("User Controller - createUser", ex);
            return null;
        }
    }

    @GetMapping(path = "{username}")
    public UserDto readUser(HttpServletResponse response, @PathVariable("username") String username) throws IOException {
        final User user = this.userService.findByUsername(username);
        if (user != null) {
            response.setStatus(HttpStatus.FOUND.value());
            return user.toDto();
        } else {
            response.sendError(HttpStatus.NOT_FOUND.value(), "User not found");
            return null;
        }
    }

    @PutMapping(path = "{username}")
    public UserDto updateUser(HttpServletResponse response, @PathVariable("username") String username, @RequestBody UserDto userDto) throws IOException {
        final User user = this.userService.findByUsername(username);
        if (user != null) {
            response.setStatus(HttpStatus.OK.value());
            final User fromDto = userDto.toUser();
            fromDto.setId(user.getId());
            this.userService.add(fromDto);
            return user.toDto();
        } else {
            response.sendError(HttpStatus.NOT_FOUND.value(), "User not found");
            return null;
        }
    }


    @DeleteMapping(path = "{username}")
    public void deleteUser(HttpServletResponse response, @PathVariable("username") String username) throws IOException {
        final boolean deleted = this.userService.delete(username);
        if (deleted) {
            response.setStatus(HttpStatus.OK.value());
        } else {
            response.sendError(HttpStatus.NOT_FOUND.value(), "User not found");
        }
    }


}
