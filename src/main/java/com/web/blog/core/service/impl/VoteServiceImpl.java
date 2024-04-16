package com.web.blog.core.service.impl;

import com.web.blog.core.domain.dto.VoteDto;
import com.web.blog.exceptions.PostNotFoundException;
import com.web.blog.exceptions.SpringBlogException;
import com.web.blog.core.domain.model.Post;
import com.web.blog.core.domain.model.Vote;
import com.web.blog.core.repository.PostRepository;
import com.web.blog.core.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import static com.web.blog.core.domain.enums.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteServiceImpl {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final LoginService loginService;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, loginService.getCurrentUser());
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteDto.getVoteType())) {
            throw new SpringBlogException("You have already "
                    + voteDto.getVoteType() + "'d for this post", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(loginService.getCurrentUser())
                .build();
    }
}