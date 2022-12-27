package org.example.util.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

public class ValidatorUtil {
    private final Validator validator;

    public ValidatorUtil() {
        this.validator = (Validator) Validation.buildDefaultValidatorFactory().getValidator();
    }

    public <T> void validate(T object) {
        final Set<ConstraintViolation<T>> errors = validator.validate(object);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toSet()));
        }
    }
}
