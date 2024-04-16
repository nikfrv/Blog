package com.web.blog.exceptions;

public class PostNotFoundException extends BlogApplicationException {
    public PostNotFoundException(String message) {
        super(message);
    }
}