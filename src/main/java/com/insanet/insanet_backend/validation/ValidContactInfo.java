package com.insanet.insanet_backend.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ContactInfoValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidContactInfo {
    String message() default "Invalid contact info. Must be a valid email or phone number.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
