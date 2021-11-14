package com.chatapp.directmessage.entity;


import com.chatapp.directmessage.dto.DirectMessageDto;
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
@Table(name = "directMessages")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class DirectMessage {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    private UUID senderId;
    private UUID receiverId;

    private String content;
    private Instant timestamp = Instant.now();

    public DirectMessageDto.Create toDirectMessageDtoCreate() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(this, DirectMessageDto.Create.class);
    }

    public DirectMessageDto.Return toDirectMessageDtoReturn() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(this, DirectMessageDto.Return.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DirectMessage that = (DirectMessage) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
