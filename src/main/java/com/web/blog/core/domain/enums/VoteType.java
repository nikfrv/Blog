package com.web.blog.core.domain.enums;


import com.web.blog.exceptions.SpringBlogException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public enum VoteType {
    UPVOTE(1), DOWNVOTE(-1);

    private int direction;

    VoteType(int direction) {
    }

    public static VoteType lookup(Integer direction) {
        return Arrays.stream(VoteType.values())
                .filter(value -> value.getDirection().equals(direction))
                .findAny()
                .orElseThrow(() -> new SpringBlogException("Vote not found", HttpStatus.INTERNAL_SERVER_ERROR));
    }

    public Integer getDirection() {
        return direction;
    }
}