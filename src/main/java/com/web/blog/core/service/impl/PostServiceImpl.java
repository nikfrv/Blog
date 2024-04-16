package com.web.blog.core.service.impl;

import com.web.blog.core.domain.dto.PostRequest;
import com.web.blog.core.domain.model.Post;
import com.web.blog.core.domain.model.Theme;
import com.web.blog.core.domain.model.User;
import com.web.blog.core.domain.payload.PostResponse;
import com.web.blog.core.repository.PostRepository;
import com.web.blog.core.repository.ThemeRepository;
import com.web.blog.core.repository.UserRepository;
import com.web.blog.core.service.PostService;
import com.web.blog.exceptions.PostNotFoundException;
import com.web.blog.exceptions.ThemeNotFoundException;
import com.web.blog.mapper.PostMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ThemeRepository themeRepository;
    private final UserRepository userRepository;
    private final LoginService loginService;
    private final PostMapper postMapper;

    public void save(PostRequest postRequest) {
        Theme theme = themeRepository.findByName(postRequest.getThemeName())
                .orElseThrow(() -> new ThemeNotFoundException(postRequest.getThemeName()));
        postRepository.save(postMapper.map(postRequest, theme, loginService.getCurrentUser()));
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByTheme(Long themeId) {
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ThemeNotFoundException(themeId.toString()));
        List<Post> posts = postRepository.findAllByTheme(theme);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }
}