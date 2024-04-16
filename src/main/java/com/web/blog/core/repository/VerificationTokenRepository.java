package com.web.blog.core.repository;

import com.web.blog.core.domain.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {

    VerificationToken findByTokenValue(String token);
}
