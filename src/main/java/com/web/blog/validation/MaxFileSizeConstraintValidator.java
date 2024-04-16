package com.web.blog.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MaxFileSizeConstraintValidator implements ConstraintValidator<MaxFileSize, MultipartFile> {

    private static final int BYTES_IN_MEGABYTE = 1_000_000;

    private int maxSizeBytes;

    @Override
    public void initialize(MaxFileSize constraintAnnotation) {
        maxSizeBytes = BYTES_IN_MEGABYTE * constraintAnnotation.maxSizeInMB();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        return file == null
                || file.getSize() <= maxSizeBytes;
    }
}
