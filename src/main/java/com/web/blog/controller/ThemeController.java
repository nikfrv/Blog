package com.web.blog.controller;

import com.web.blog.core.domain.dto.ThemeDto;
import com.web.blog.core.service.impl.ThemeServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theme")
@AllArgsConstructor
@Slf4j
public class ThemeController {

    private final ThemeServiceImpl themeServiceImpl;

    @PostMapping
    public ResponseEntity<ThemeDto> createTheme(@RequestBody ThemeDto themeDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(themeServiceImpl.save(themeDto));
    }

    @GetMapping
    public ResponseEntity<List<ThemeDto>> getAllThemes() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(themeServiceImpl.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThemeDto> getTheme(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(themeServiceImpl.getTheme(id));
    }
}