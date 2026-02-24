package com.uzumtech.finespenalties.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberConstraint, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        Pattern pattern = Pattern.compile("^998\\d{9}$");
        return (s == null) || pattern.matcher(s).matches();
    }
}
