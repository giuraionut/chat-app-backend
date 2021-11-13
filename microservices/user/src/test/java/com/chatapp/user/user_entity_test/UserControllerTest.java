package com.chatapp.user.user_entity_test;

import com.chatapp.user.user_entity.UserController;
import com.chatapp.user.user_entity.UserDto;
import com.chatapp.user.user_entity.UserRepository;
import com.chatapp.user.user_entity.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void createUser() throws Exception {

        UserDto.Register registerUserDto = new UserDto.Register();
        registerUserDto.setUsername("test");
        registerUserDto.setPassword("pass123");
        registerUserDto.setUsername("test@gmail.com");

        Mockito.when(userService.add(registerUserDto.toUser())).thenReturn(registerUserDto.toUser());

        String url = "/api/v1/user";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(registerUserDto);

        mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).content(requestJson)).andExpect(status().isCreated());
    }

    @Test
    void readUser() throws Exception {
        UserDto.Return returnUserDto = new UserDto.Return();

        returnUserDto.setUsername("test");
        returnUserDto.setEmail("test@gmail.com");
        returnUserDto.setAvatar("null");

        Mockito.when(userService.findByUsername("test")).thenReturn(returnUserDto.toUser());

        String url = "/api/v1/user/{username}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url, returnUserDto.getUsername());
        final MvcResult result = mvc.perform(requestBuilder).andExpect(status().isFound()).andReturn();
        assertEquals("{\"username\":\"test\"," +
                        "\"email\":\"test@gmail.com\"," +
                        "\"avatar\":\"null\"" +
                        "}",
                result.getResponse().getContentAsString());
    }

    @Test
    void updateUser() throws Exception {

        UserDto.Update updateUserDto = new UserDto.Update();
        updateUserDto.setUsername("test123");
        updateUserDto.setPassword("pass123");
        updateUserDto.setEmail("test123@gmail.com");
        updateUserDto.setAvatar("https://test123.avatar.com");

        Mockito.when(userService.update("test", updateUserDto)).thenReturn(updateUserDto.toUser());

        String url = "/api/v1/user/{username}";

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(updateUserDto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(url, updateUserDto.getUsername())
                .contentType(MediaType.APPLICATION_JSON).content(requestJson);
        mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }

    @Test
    void deleteUser() throws Exception {
        String url = "/api/v1/user/{username}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(url, "test");
        mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }
}