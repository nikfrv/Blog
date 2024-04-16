package com.web.blog.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = {MaxFileSizeConstraintValidator.class})
@Documented
public @interface MaxFileSize {

    String message() default "{messages.validation.maxFileSize}";

    int maxSizeInMB() default 3;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
