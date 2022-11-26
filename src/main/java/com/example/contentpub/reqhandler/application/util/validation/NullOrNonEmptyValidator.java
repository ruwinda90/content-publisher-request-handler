package com.example.contentpub.reqhandler.application.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * The custom validator.
 */
public class NullOrNonEmptyValidator implements ConstraintValidator<NullOrNonEmpty, String> {

    public boolean isValid(String value, ConstraintValidatorContext cxt) {

        return value == null || !value.isEmpty(); // The value should be either null, or non-empty.

    }
}