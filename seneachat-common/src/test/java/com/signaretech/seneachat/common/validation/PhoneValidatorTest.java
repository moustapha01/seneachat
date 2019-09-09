package com.signaretech.seneachat.common.validation;

import org.junit.Test;

import javax.validation.ConstraintViolationException;

public class PhoneValidatorTest {

    private final EntityValidator validator = new EntityValidator();

    class Phone {

        @ValidPhone(pattern = "\\d{9,10}")
        private String number;

        Phone(){}

        Phone(final String number) {
            this.number = number;
        }
    }

    @Test(expected = ConstraintViolationException.class)
    public void testInvalidNullPhoneNumber() {
        validator.validate(new Phone());
    }

    @Test(expected = ConstraintViolationException.class)
    public void testInvalidIncorrectPhoneNumberFormat() {
        validator.validate(new Phone("405-328-8268"));
    }

    @Test
    public void testValidNineDigitPhoneNumber() {
        validator.validate(new Phone("773335555"));
    }

    @Test
    public void testValidTenDigitPhoneNumber() {
        validator.validate(new Phone("4051119999"));
    }
}
