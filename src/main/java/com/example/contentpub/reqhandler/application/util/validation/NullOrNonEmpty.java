package com.example.contentpub.reqhandler.application.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * The custom validator annotation to validate fields in incoming request body.
 */
@Target( { FIELD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NullOrNonEmptyValidator.class)
public @interface NullOrNonEmpty {

    public String message() default "Non-null parameter cannot be empty";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
