package com.web.blog.core.service.impl;

import com.web.blog.core.domain.dto.ThemeDto;
import com.web.blog.core.service.ThemeService;
import com.web.blog.exceptions.SpringBlogException;
import com.web.blog.mapper.ThemeMapper;
import com.web.blog.core.domain.model.Theme;
import com.web.blog.core.repository.ThemeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class ThemeServiceImpl implements ThemeService {

    private final ThemeRepository themeRepository;
    private final ThemeMapper themeMapper;

    @Transactional
    public ThemeDto save(ThemeDto themeDto) {
        Theme save = themeRepository.save(themeMapper.mapDtoToThemes(themeDto));
        themeDto.setId(save.getId());
        return themeDto;
    }

    @Transactional(readOnly = true)
    public List<ThemeDto> getAll() {
        return themeRepository.findAll()
                .stream()
                .map(themeMapper::mapThemeToDto)
                .collect(toList());
    }

    public ThemeDto getTheme(Long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new SpringBlogException("No themes found with ID - " + id, HttpStatus.INTERNAL_SERVER_ERROR));
        return themeMapper.mapThemeToDto(theme);
    }
}