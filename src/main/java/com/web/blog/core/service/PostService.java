package com.web.blog.core.service;

import com.web.blog.core.domain.dto.PostRequest;
import com.web.blog.core.domain.payload.PostResponse;

import java.util.List;

public interface PostService {

    public void save(PostRequest postRequest);


    public PostResponse getPost(Long id);

    public List<PostResponse> getAllPosts();

    public List<PostResponse> getPostsByTheme(Long themeId);

    public List<PostResponse> getPostsByUsername(String username);
}
