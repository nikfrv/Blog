package com.web.blog.core.repository;

import com.web.blog.core.domain.model.Post;
import com.web.blog.core.domain.model.User;
import com.web.blog.core.domain.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}