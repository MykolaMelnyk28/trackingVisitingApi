package com.trackingVisitingApi.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {
    String message() default "Start date must be before end date";
    String start();
    String end();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}