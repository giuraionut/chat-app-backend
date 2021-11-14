package com.chatapp.directmessage.entity;

import com.chatapp.directmessage.dto.DirectMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DirectMessageController.class)
class DirectMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DirectMessageService directMessageService;

    @MockBean
    private DirectMessageRepository directMessageRepository;

    @InjectMocks
    private DirectMessageController directMessageController;

    private final String URL = "/api/v1/direct_message";

    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectWriter writer = mapper.writer();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    } 

    @Test
    void createMessage() throws Exception {
        DirectMessageDto.Create message = new DirectMessageDto.Create();
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        message.setReceiverId(receiverId);
        message.setSenderId(senderId);
        message.setContent("hi");

        String json = writer.writeValueAsString(message);
        Mockito.when(this.directMessageService.create(any(DirectMessage.class))).thenReturn(message.toDirectMessage());

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value(message.getContent()));
    }

    @Test
    void readMessage() throws Exception {
        DirectMessage message = new DirectMessage();
        message.setContent("blablabla");
        Mockito.when(this.directMessageService.readById(any(UUID.class))).thenReturn(message);
        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/{messageId}", UUID.randomUUID()))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.content").value(message.getContent()));
    }

    @Test
    void updateMessage() throws Exception {
        DirectMessageDto.Update update = new DirectMessageDto.Update();
        update.setContent("blablabla");
        final String json = writer.writeValueAsString(update);
        Mockito.when(this.directMessageService.update(any(UUID.class), any(DirectMessageDto.Update.class)))
                .thenReturn(update.toDirectMessage());

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/{messageId}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(update.getContent()));
    }

    @Test
    void deleteMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/{messageId}", UUID.randomUUID()))
                .andExpect(status().isOk());
    }
}