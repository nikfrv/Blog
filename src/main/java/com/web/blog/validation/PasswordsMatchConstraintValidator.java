package com.web.blog.validation;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsMatchConstraintValidator implements ConstraintValidator<PasswordsMatch, Object> {

    private String fieldName;

    private String matchFieldName;

    private String messageTemplate;

    @Override
    public void initialize(PasswordsMatch constraint) {
        fieldName = constraint.fieldName();
        matchFieldName = constraint.matchFieldName();
        messageTemplate = constraint.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Object passwordValue = new BeanWrapperImpl(value).getPropertyValue(fieldName);
        Object passwordMatchValue = new BeanWrapperImpl(value).getPropertyValue(matchFieldName);

        if (passwordsDontMatch(passwordValue, passwordMatchValue)) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(messageTemplate)
                    .addPropertyNode(matchFieldName).addConstraintViolation();

            return false;
        }

        return true;
    }

    private boolean passwordsDontMatch(Object passwordValue, Object passwordMatchValue) {
        return (passwordValue != null && !passwordValue.equals(passwordMatchValue))
                || (passwordMatchValue != null && !passwordMatchValue.equals(passwordValue));
    }
}
