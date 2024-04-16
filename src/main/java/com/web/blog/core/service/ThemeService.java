package com.web.blog.core.service;

import com.web.blog.core.domain.dto.ThemeDto;

import java.util.List;

public interface ThemeService {
    public ThemeDto save(ThemeDto themeDto);

    public List<ThemeDto> getAll();

    public ThemeDto getTheme(Long id);
}
