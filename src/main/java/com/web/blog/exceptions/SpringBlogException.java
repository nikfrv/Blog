package com.web.blog.exceptions;

import org.springframework.http.HttpStatus;

public class SpringBlogException extends BlogApplicationException {

    public SpringBlogException(String exMessage, HttpStatus internalServerError) {
        super(exMessage);
    }
}