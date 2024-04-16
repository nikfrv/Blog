package com.web.blog.core.repository;

import com.web.blog.core.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);

    boolean existsByUsername(String username);

    User findById(String id);
}
