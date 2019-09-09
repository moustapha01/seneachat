package com.signaretech.seneachat.common.validation;

import javax.validation.*;
import java.util.Set;

/**
 * Validator to validate an entity for specific constraints set on the entity.
 */

public class EntityValidator {

    private final Validator validator;

    public EntityValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * @param entity, object to validate
     * @param <T>, the type of the entity
     * @return a set of {@link ConstraintViolation} or empty set if the entity is valid
     */
    public <T> Set<ConstraintViolation<T>> isValid(T entity) {
        return validator.validate(entity);
    }

    /**
     * Validates and throws a {@link ConstraintViolationException}
     * with a set of {@link ConstraintViolation} if entity is not valid.
     *
     * @param entity, object to validate
     * @param <T>, type for the entity
     */
    public <T> void validate(T entity) {
        final Set<ConstraintViolation<T>> violations = isValid(entity);
        if( !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
