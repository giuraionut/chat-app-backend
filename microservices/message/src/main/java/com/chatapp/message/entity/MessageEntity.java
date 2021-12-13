package com.chatapp.message.entity;


import com.chatapp.message.dto.MessageDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "messages")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class MessageEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID messageId;

    private UUID senderId;
    private String content;
    private Boolean seen = false;
    private Instant timestamp = Instant.now();

    public MessageDto.Base toBaseMessage() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(this, MessageDto.Base.class);
    }

    public MessageDto.Display toDisplayMessage(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(this, MessageDto.Display.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MessageEntity that = (MessageEntity) o;
        return messageId != null && Objects.equals(messageId, that.messageId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
