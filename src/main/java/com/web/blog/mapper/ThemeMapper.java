package com.web.blog.mapper;

import com.web.blog.core.domain.dto.ThemeDto;
import com.web.blog.core.domain.model.Post;
import com.web.blog.core.domain.model.Theme;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ThemeMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(theme.getPosts()))")
    ThemeDto mapThemeToDto(Theme theme);

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Theme mapDtoToThemes(ThemeDto themeDto);
}