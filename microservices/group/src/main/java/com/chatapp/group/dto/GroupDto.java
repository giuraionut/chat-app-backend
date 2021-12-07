package com.chatapp.group.dto;

import com.chatapp.group.components.Category;
import com.chatapp.group.entity.GroupEntity;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;
import java.util.UUID;

public class GroupDto {
    private GroupDto() {
    }

    @Data
    public static class Base {
        private String name;

        public GroupEntity toEntity() {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(this, GroupEntity.class);
        }
    }

    @Data
    public static class Display {
        private String name;
        private UUID ownerId;
        private String avatar;
    }

    @Data
    public static class Update {
        private String name;
        private String avatar;
        private Category category;
    }
}
