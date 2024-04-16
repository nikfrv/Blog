package com.web.blog.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PersonNameConstraintValidator implements ConstraintValidator<PersonName, String> {

    private static final Pattern VALID_PERSON_NAME_PATTERN = Pattern.compile("^[A-ZА-ЯІ][a-zа-яіє'’]+$");

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return name == null
                || VALID_PERSON_NAME_PATTERN.matcher(name).matches();
    }
}
