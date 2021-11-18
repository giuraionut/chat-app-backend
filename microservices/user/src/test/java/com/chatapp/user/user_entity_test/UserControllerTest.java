package com.chatapp.user.user_entity_test;

import com.chatapp.user.user_entity.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private final String URL = "/api/v1/user";

    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectWriter writer = mapper.writer();

    @Test
    void createUser() throws Exception {

        UserDto.Register registerUserDto = new UserDto.Register();
        registerUserDto.setUsername("test");
        registerUserDto.setPassword("pass123");
        registerUserDto.setUsername("test@gmail.com");

        Mockito.when(userService.add(any(User.class))).thenReturn(registerUserDto.toUser());
        String json = writer.writeValueAsString(registerUserDto);

        mockMvc.perform(MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated());
    }

    @Test
    void readUser() throws Exception {
        UserDto.Return returnUserDto = new UserDto.Return();

        returnUserDto.setUsername("test");
        returnUserDto.setEmail("test@gmail.com");
        returnUserDto.setAvatar("null");

        Mockito.when(userService.findByUsername(any(String.class))).thenReturn(returnUserDto.toUser());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL + "/{username}", returnUserDto.getUsername());
        mockMvc.perform(requestBuilder)
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.username").value(returnUserDto.getUsername()))
                .andExpect(jsonPath("$.email").value(returnUserDto.getEmail()));

    }

    @Test
    void readUserById() throws Exception {
        UserDto.Return returnUserDto = new UserDto.Return();

        returnUserDto.setUsername("test");
        returnUserDto.setEmail("test@gmail.com");
        returnUserDto.setAvatar("null");

        Mockito.when(userService.findById(any(UUID.class))).thenReturn(returnUserDto.toUser());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL + "/id/{id}", UUID.randomUUID());
        mockMvc.perform(requestBuilder)
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.username").value(returnUserDto.getUsername()))
                .andExpect(jsonPath("$.email").value(returnUserDto.getEmail()));

    }

    @Test
    void updateUser() throws Exception {

        UserDto.Update updateUserDto = new UserDto.Update();
        updateUserDto.setUsername("test123");
        updateUserDto.setPassword("pass123");
        updateUserDto.setEmail("test123@gmail.com");
        updateUserDto.setAvatar("https://test123.avatar.com");

        final String json = writer.writeValueAsString(updateUserDto);
        Mockito.when(this.userService.update(any(UUID.class), any(UserDto.Update.class)))
                .thenReturn(updateUserDto.toUser());

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/{username}", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(updateUserDto.getUsername()));

    }

    @Test
    void deleteUser() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(URL + "/{username}", "test");
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }
}