package com.signaretech.seneachat.common.validation;

import org.hibernate.validator.constraints.NotBlank;
import org.junit.Test;

import javax.validation.ConstraintViolationException;

public class EntityValidatorTest {

    private final EntityValidator validator = new EntityValidator();

    static class TestEntity {

        @NotBlank(message = "Name cannot be null")
        private String name;

        public TestEntity() {}

        public TestEntity(final String name){ this.name = name; }
    }

    @Test(expected = ConstraintViolationException.class)
    public void testNullEntity() {
        validator.validate(new TestEntity());
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyNameEntity() {
        validator.validate(new TestEntity(""));
    }

    @Test
    public void testValidEntity() {
        validator.validate(new TestEntity("My Name"));
    }
}
