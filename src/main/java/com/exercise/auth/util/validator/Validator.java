package com.exercise.auth.util.validator;

import com.exercise.auth.exceptions.FieldValidationException;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.HashMap;
import java.util.Optional;

@Component
public abstract class Validator<T> {

    /**
     * Returns the validated object
     * @param body Body of the request to be validated.
     * @return The validated object
     */
    public T validate(final ParameterMap body) {
        final T result = doValidate(body);

        if(body.hasErrors()) {
            throw new FieldValidationException(new HashMap<>(body.getErrorMap()));
        }

        return result;
    }

    @Transactional
    protected abstract T doValidate(ParameterMap body);

    protected abstract void validateFields(ParameterMap body);

    public static boolean isEmpty(final Object obj) {
        return String.valueOf(obj).isEmpty() || Optional.ofNullable(obj).isEmpty();
    }

    public static boolean isNull(final Object obj) {
        return Optional.ofNullable(obj).isEmpty();
    }

    public static boolean isUuid(final String value) {
        return value.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$");
    }

    public static boolean isInt(final String value) {
        return isNumber(value) && isMinValue(value, Integer.MIN_VALUE) && isMaxValue(value, Integer.MAX_VALUE);
    }

    public static boolean isDouble(final String value) {
        return isDecimal(value) && isMinValue(value, Double.MIN_VALUE) && isMaxValue(value, Double.MAX_VALUE);
    }

    public static boolean isLong(final String value) {
        return isNumber(value) && isMinValue(value, Long.MIN_VALUE) && isMaxValue(value, Long.MAX_VALUE);
    }

    public static boolean isBoolean(final String booleanValue) {
        return Optional.ofNullable(BooleanUtils.toBooleanObject(booleanValue)).isPresent();
    }

    public static boolean isNumber(final String value) {
        return value.matches("-?[0-9]+");
    }

    public static boolean isDecimal(final String value) {
        return value.matches("^[-]?((\\d+(\\.\\d*)?)|(\\.\\d+))$");
    }

    public static boolean isMinLength(final String value, final int length) {
        return value.length() > length;
    }

    public static boolean isMaxLength(final String value, final int length) {
        return value.length() < length;
    }

    public static boolean isMinValue(final String value, final int minValue) {
        return Integer.parseInt(value) > minValue;
    }

    public static boolean isMaxValue(final String value, final int maxValue) {
        return Integer.parseInt(value) < maxValue;
    }

    public static boolean isMinValue(final String value, final long minValue) {
        return Long.parseLong(value) > minValue;
    }

    public static boolean isMaxValue(final String value, final long maxValue) {
        return Long.parseLong(value) < maxValue;
    }

    public static boolean isMinValue(final String value, final double minValue) {
        return Double.parseDouble(value) > minValue;
    }

    public static boolean isMaxValue(final String value, final double maxValue) {
        return Double.parseDouble(value) < maxValue;
    }

    public static boolean isEmail(final String value) {
        return value.matches("\\S+@\\S+\\.\\S+");
    }

    public static boolean isDate(final String value) {
        boolean isValid = true;
        // uuuu-MM-dd'T'HH:mm:ss.SSSXXX = 2021-05-10T22:00:15.000+08:00
        // uuuu-MM-dd'T'HH:mm:ss.SSSZZ = 2021-05-10T22:00:15.000+0800
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofPattern("uuuu-MM-dd'T'HH:mm:ssXXX")
                .withResolverStyle(ResolverStyle.STRICT);
        try {
            OffsetDateTime.parse(value, dateTimeFormatter);
            // log.info(dateTimeFormatter.format(OffsetDateTime.parse(value, dateTimeFormatter)));
        } catch (final DateTimeParseException dateTimeParseException) {
            isValid = false;
            // log.info(dateTimeParseException.getMessage());
        }
        return isValid;
    }

}
