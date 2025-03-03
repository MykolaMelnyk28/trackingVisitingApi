package com.trackingVisitingApi.validate;

import com.trackingVisitingApi.payload.v1.VisitRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalDateTime;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, VisitRequest> {

    private String startFieldName;
    private String endFieldName;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.startFieldName = constraintAnnotation.start();
        this.endFieldName = constraintAnnotation.end();
    }

    @Override
    public boolean isValid(VisitRequest request, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(request);
        Object start = beanWrapper.getPropertyValue(startFieldName);
        Object end = beanWrapper.getPropertyValue(endFieldName);

        if (!(start instanceof LocalDateTime) || !(end instanceof LocalDateTime)) {
            return true;
        }

        LocalDateTime startDate = (LocalDateTime) start;
        LocalDateTime endDate = (LocalDateTime) end;

        boolean isValid = true;

        context.disableDefaultConstraintViolation();

        if (endDate.isBefore(startDate)) {
            context.buildConstraintViolationWithTemplate("End date must be after the start date")
                    .addPropertyNode(endFieldName)
                    .addConstraintViolation();
            isValid = false;
        }

        if (startDate.isEqual(endDate)) {
            context.buildConstraintViolationWithTemplate("Start date cannot be equal to the end date")
                    .addPropertyNode(startFieldName)
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
