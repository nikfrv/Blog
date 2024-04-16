package com.web.blog.core.repository;

import com.web.blog.core.domain.model.RefreshToken;
import com.web.blog.core.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    Optional<RefreshToken> findByToken(String token);
    @Modifying
    int deleteByUser(User user);
}
