package com.web.blog.validation;


import com.web.blog.encryption.PasswordCoder;
import com.web.blog.exceptions.PasswordCodingException;
import org.passay.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class PasswordConstraintValidator implements ConstraintValidator<Password, String> {

    private static final Logger LOG = LoggerFactory.getLogger(PasswordConstraintValidator.class);

    private int minLength;

    private int maxLength;

    private int upperCaseCount;

    private int lowerCaseCount;

    private int specialCharCount;

    private final PasswordCoder passwordCoder;

    public PasswordConstraintValidator(PasswordCoder passwordCoder) {
        this.passwordCoder = passwordCoder;
    }

    @Override
    public void initialize(Password password) {
        minLength = password.minLength();
        maxLength = password.maxLength();
        upperCaseCount = password.upperCaseCount();
        lowerCaseCount = password.lowerCaseCount();
        specialCharCount = password.specialCharCount();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password == null
                || buildPasswordValidator().validate(new PasswordData(decryptPassword(password))).isValid();
    }

    private String decryptPassword(String encryptedPassword) {
        String incorrectPassword = "";
        try {
            return passwordCoder.decrypt(encryptedPassword);
        } catch (PasswordCodingException e) {
            LOG.warn("Password has not encrypted, password: {}", encryptedPassword);
            return incorrectPassword;
        }
    }

    private PasswordValidator buildPasswordValidator() {
        return new PasswordValidator(
                new LengthRule(minLength, maxLength),
                new CharacterRule(EnglishCharacterData.UpperCase, upperCaseCount),
                new CharacterRule(EnglishCharacterData.LowerCase, lowerCaseCount),
                new CharacterRule(EnglishCharacterData.Special, specialCharCount)
        );
    }
}
