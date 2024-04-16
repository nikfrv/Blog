package com.web.blog.exceptions;

public class ThemeNotFoundException extends BlogApplicationException {
    public ThemeNotFoundException(String message) {
        super(message);
    }
}