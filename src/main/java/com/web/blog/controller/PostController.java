package com.web.blog.controller;

import com.web.blog.core.domain.dto.PostRequest;
import com.web.blog.core.domain.payload.PostResponse;
import com.web.blog.core.service.impl.PostServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostServiceImpl postServiceImpl;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest) {
        postServiceImpl.save(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return status(HttpStatus.OK).body(postServiceImpl.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return status(HttpStatus.OK).body(postServiceImpl.getPost(id));
    }

    @GetMapping("by-theme/{id}")
    public ResponseEntity<List<PostResponse>> getPostsByTheme(Long id) {
        return status(HttpStatus.OK).body(postServiceImpl.getPostsByTheme(id));
    }

    @GetMapping("by-user/{name}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String name) {
        return status(HttpStatus.OK).body(postServiceImpl.getPostsByUsername(name));
    }
}