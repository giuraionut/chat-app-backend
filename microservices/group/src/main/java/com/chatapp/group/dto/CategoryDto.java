package com.chatapp.group.dto;


import com.chatapp.group.components.Category;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class CategoryDto {
    private CategoryDto(){}

    @Data
    public static class Display{
        private String name;
    }
    @Data
    public static class Base{
        private String name;

        public Category toEntity() {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(this, Category.class);
        }
    }
    @Data
    public static class Update{
        private String name;
    }
}
