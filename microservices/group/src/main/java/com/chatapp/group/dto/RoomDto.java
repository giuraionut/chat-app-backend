package com.chatapp.group.dto;

import com.chatapp.group.components.Room;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class RoomDto {
    private RoomDto() {
    }

    @Data
    public static class Display {
        private String name;
    }

    @Data
    public static class Base {
        private String name;

        public Room toEntity() {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(this, Room.class);
        }
    }
}

