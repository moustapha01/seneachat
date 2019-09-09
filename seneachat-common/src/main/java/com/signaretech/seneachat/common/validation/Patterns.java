package com.signaretech.seneachat.common.validation;

import java.util.regex.Pattern;

public final class Patterns {

    private Patterns() {}

    private static final Pattern NUMERIC = Pattern.compile("[+-]?([0-9]*[.])?[0-9]+");

    public static final boolean isNumeric(String value) {
        return NUMERIC.matcher(value).matches();
    }

    public static final boolean isValidPhone(String value, String pattern) {
        final Pattern PHONE = Pattern.compile(pattern);
        return PHONE.matcher(value).matches();
    }
}
