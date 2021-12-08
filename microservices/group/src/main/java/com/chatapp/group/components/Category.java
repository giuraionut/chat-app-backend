package com.chatapp.group.components;

import com.chatapp.group.dto.CategoryDto;
import com.chatapp.group.entity.GroupEntity;
import com.chatapp.group.exceptions.CustomException;
import com.chatapp.group.exceptions.ExceptionResource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "categories")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Category {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Room> rooms = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private GroupEntity groupEntity;

    public void addRoom(Room room) {
        room.setCategory(this);
        this.rooms.add(room);
    }

    public CategoryDto.Display toDisplay() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(this, CategoryDto.Display.class);
    }

    public Room getRoomById(UUID roomId) throws CustomException {
        return this.rooms.stream().filter(room -> room.getId().equals(roomId)).findFirst().orElseThrow(() -> new CustomException(ExceptionResource.ROOM_NOT_FOUND));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Category category = (Category) o;
        return id != null && Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
