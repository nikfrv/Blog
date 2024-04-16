package com.web.blog.exceptions;

public class PasswordCodingException extends BlogApplicationException {

    public PasswordCodingException(String message) {
        super(message);
    }

    public PasswordCodingException(String message, Throwable cause) {
        super(message, cause);
    }
}
