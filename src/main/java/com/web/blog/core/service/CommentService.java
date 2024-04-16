package com.web.blog.core.service;

import com.web.blog.core.domain.dto.CommentsDto;

import java.util.List;

public interface CommentService {

    public void save(CommentsDto commentsDto);

    public List<CommentsDto> getAllCommentsForPost(Long postId);

    public List<CommentsDto> getAllCommentsForUser(String userName);
}
