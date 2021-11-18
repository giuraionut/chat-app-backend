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
        MessageDto.Pure message = new MessageDto.Pure();
        UUID senderId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        message.setChannelId(channelId);
        message.setSenderId(senderId);
        message.setContent("hi");

        MessageDto.Built messageHistory = new MessageDto.Built();
        messageHistory.setContent(message.getContent());
        String json = writer.writeValueAsString(message);
        Mockito.when(this.messageService.create(any(Message.class))).thenReturn(message.toMessage());
        Mockito.when(this.messageService.buildMessage(any(UUID.class))).thenReturn(messageHistory);

        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value(messageHistory.getContent()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void readMessage() throws Exception {
        Message message = new Message();
        message.setContent("blablabla");
        MessageDto.Built messageWithUsers = new MessageDto.Built();
        messageWithUsers.setContent(message.getContent());
        Mockito.when(this.messageService.buildMessage(any(UUID.class))).thenReturn(messageWithUsers);
        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/{messageId}", UUID.randomUUID()))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.content").value(message.getContent()))
                .andReturn();
    }

    @Test
    void updateMessage() throws Exception {
        MessageDto.Update update = new MessageDto.Update();
        update.setContent("blablabla");
        final String json = writer.writeValueAsString(update);
        Mockito.when(this.messageService.update(any(UUID.class), any(MessageDto.Update.class)))
                .thenReturn(update.toMessage());

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