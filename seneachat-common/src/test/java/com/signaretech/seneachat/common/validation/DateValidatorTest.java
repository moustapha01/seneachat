package com.signaretech.seneachat.common.validation;

import org.junit.Ignore;
import org.junit.Test;

import javax.validation.ConstraintViolationException;

@Ignore
public class DateValidatorTest {

    private EntityValidator validator = new EntityValidator();

    class DateObject {

        @ValidDate(pattern = "M/d/yyyy")
        private String date;

        DateObject(String date) {
            this.date = date;
        }
    }

    @Test
    public void testValidDateTwoDigitMonthAndDay() {
        validator.validate(new DateObject("02/03/2011"));
    }

    @Test
    public void testValidDateOneDigitMonthAndDay() {
        validator.validate(new DateObject("2/3/2011"));
    }

    @Test
    public void testValidDateOneDigitTwoDigitDay() {
        validator.validate(new DateObject("2/03/2011"));
    }

    @Test
    public void testValidDate() {
        validator.validate(new DateObject("02/03/2011"));
    }

    @Test( expected = ConstraintViolationException.class )
    public void testInvalidDate() {
        validator.validate(new DateObject("2988"));
    }

    @Test( expected = ConstraintViolationException.class )
    public void testInvalidMonth() {
        validator.validate(new DateObject("13/03/2011"));
    }

    @Test( expected = ConstraintViolationException.class )
    public void testInvalidDay() {
        validator.validate(new DateObject("12/34/2011"));
    }

    @Test( expected = ConstraintViolationException.class )
    public void testInvalidYear() {
        validator.validate(new DateObject("12/01/20"));
    }
}
