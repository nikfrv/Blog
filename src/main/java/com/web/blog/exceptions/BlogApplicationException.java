package com.web.blog.exceptions;

public abstract class BlogApplicationException extends RuntimeException{
    public BlogApplicationException(String message) {
        super(message);
    }

    public BlogApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlogApplicationException(Throwable cause) {
        super(cause);
    }
}
