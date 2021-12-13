package com.chatapp.group.components;

import com.chatapp.group.dto.MessageDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "messages_rooms_mapping")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Message {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    private UUID messageId;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    public MessageDto.Display toDisplay() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        final MessageDto.Display map = modelMapper.map(this, MessageDto.Display.class);
        map.setRoomId(room.getId());
        return map;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Message message = (Message) o;
        return id != null && Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
