package com.chatapp.user.user_entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.UUID;


@RestController
@RequestMapping(path = "api/v1/user")
public record UserController(UserService userService) {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final String USER_NOT_FOUND = "User does not exists";
    private static final String USERNAME_DUPLICATE = "Username already exists";

    @PostMapping()
    public UserDto.Return createUser(@RequestBody UserDto.Register registerUserDto, HttpServletResponse response) throws IOException {
        try {
            response.setStatus(HttpStatus.CREATED.value());
            return this.userService.add(registerUserDto.toUser()).toReturnDto();
        } catch (Exception ex) {
            response.sendError(HttpStatus.CONFLICT.value(), USERNAME_DUPLICATE);
            logger.trace("User Controller - createUser", ex);
            return null;
        }
    }

    @GetMapping(path = "{username}")
    public UserDto.Return readUser(HttpServletResponse response, @PathVariable("username") String username) throws IOException {
        try {
            response.setStatus(HttpStatus.FOUND.value());
            return this.userService.findByUsername(username).toReturnDto();
        } catch (NoSuchElementException ex) {
            logger.trace("User Controller - readUser", ex);
            response.sendError(HttpStatus.NOT_FOUND.value(), USER_NOT_FOUND);
            return null;
        }
    }

    @GetMapping(path = "id/{id}")
    public UserDto.Return readUserById(HttpServletResponse response, @PathVariable("id") UUID id) throws IOException {
        try {
            response.setStatus(HttpStatus.FOUND.value());
            return this.userService.findById(id).toReturnDto();
        } catch (NoSuchElementException ex) {
            logger.trace("User Controller - readUser", ex);
            response.sendError(HttpStatus.NOT_FOUND.value(), USER_NOT_FOUND);
            return null;
        }
    }


    @PutMapping(path = "{username}")
    public UserDto.Return updateUser(HttpServletResponse response, @PathVariable("username") String username, @RequestBody UserDto.Update updateUserDto) throws IOException {
        try {
            response.setStatus(HttpStatus.OK.value());
            return this.userService.update(username, updateUserDto).toReturnDto();
        } catch (NoSuchElementException ex) {
            logger.trace("User Controller - updateUser", ex);
            response.sendError(HttpStatus.NOT_FOUND.value(), USER_NOT_FOUND);
            return null;
        }
    }

    @DeleteMapping(path = "{username}")
    public void deleteUser(HttpServletResponse response, @PathVariable("username") String username) throws IOException {
        try {
            this.userService.delete(username);
            response.setStatus(HttpStatus.OK.value());
        } catch (NoSuchElementException ex) {
            logger.trace("User Controller - deleteUser", ex);
            response.sendError(HttpStatus.NOT_FOUND.value(), USER_NOT_FOUND);
        }
    }
}
