package com.chatapp.message.entity;

import com.chatapp.message.dto.MessageDto;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageController messageController;

    private final String URL = "/api/v1/message";

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
        MessageDto.Base message = new MessageDto.Base();
        UUID senderId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        message.setRecipientId(recipientId);
        message.setSenderId(senderId);
        message.setContent("hi");

        MessageDto.Display displayMessage = new MessageDto.Display();
        displayMessage.setContent(message.getContent());
        String json = writer.writeValueAsString(message);
        Mockito.when(this.messageService.create(any(MessageEntity.class))).thenReturn(message.toMessageEntity());
        Mockito.when(this.messageService.buildMessage(any(UUID.class))).thenReturn(displayMessage);
        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value(displayMessage.getContent()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void readMessage() throws Exception {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setContent("blablabla");
        MessageDto.Display messageWithUsers = new MessageDto.Display();
        messageWithUsers.setContent(messageEntity.getContent());
        Mockito.when(this.messageService.buildMessage(any(UUID.class))).thenReturn(messageWithUsers);
        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/{messageId}", UUID.randomUUID()))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.content").value(messageEntity.getContent()))
                .andReturn();
    }

    @Test
    void updateMessage() throws Exception {
        MessageDto.Update update = new MessageDto.Update();
        update.setContent("blablabla");
        final String json = writer.writeValueAsString(update);
        Mockito.when(this.messageService.update(any(UUID.class), any(MessageDto.Update.class)))
                .thenReturn(update.toMessageEntity());

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