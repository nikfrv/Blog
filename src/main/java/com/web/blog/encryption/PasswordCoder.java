package com.web.blog.encryption;

import com.web.blog.exceptions.PasswordCodingException;

public interface PasswordCoder {

    String encrypt(String rawPassword) throws PasswordCodingException;

    String decrypt(String encryptedPassword) throws PasswordCodingException;
}
