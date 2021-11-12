package com.exercise.auth.util.validator;

import lombok.AllArgsConstructor;
import java.util.Optional;

@AllArgsConstructor
public final class FieldValidation {

    private String fieldName;
    private String fieldValue;
    private ParameterMap parameterMap;

    public String getValue() {
        return this.fieldValue;
    }


    public FieldValidation isUuid(final String message) {
        return condition(value -> {
            boolean condition = false;
            if (Validator.isEmpty(value) || Validator.isNull(value)) {
                condition = false;
            } else {
                condition = Validator.isUuid(value);
            }
            return condition;
        }, message);
    }

    public FieldValidation isInt(final String message) {
        return condition(value -> {
            boolean condition = false;
            if (Validator.isEmpty(value) || Validator.isNull(value)) {
                condition = false;
            } else {
                condition = Validator.isInt(value);
            }
            return condition;
        }, message);
    }

    public FieldValidation isDouble(final String message) {
        return condition(value -> {
            boolean condition = false;
            if (Validator.isEmpty(value) || Validator.isNull(value)) {
                condition = false;
            } else {
                condition = Validator.isDouble(value);
            }
            return condition;
        }, message);
    }

    public FieldValidation isLong(final String message) {
        return condition(value -> {
            boolean condition = false;
            if (Validator.isEmpty(value) || Validator.isNull(value)) {
                condition = false;
            } else {
                condition = Validator.isLong(value);
            }
            return condition;
        }, message);
    }

    public FieldValidation isBoolean(final String message) {
        return condition(value -> {
            boolean condition = false;
            if (Validator.isEmpty(value)) {
                condition = false;
            }
            condition = Validator.isBoolean(value);
            return condition;
        }, message);
    }

    public FieldValidation required(final String message) {
        return condition(value -> !Validator.isEmpty(value), message);
    }

    public FieldValidation minLength(final int length, final String message) {
        return condition(value -> {
            boolean condition = false;
            if (Validator.isEmpty(value) || Validator.isNull(value)) {
                condition = false;
            } else {
                condition = Validator.isMinLength(value, length);
            }
            return condition;
        }, message);
    }

    public FieldValidation maxLength(final int length, final String message) {
        return condition(value -> {
            boolean condition = false;
            if (Validator.isEmpty(value) || Validator.isNull(value)) {
                condition = false;
            } else {
                condition = Validator.isMaxLength(value, length);
            }
            return condition;
        }, message);
    }

    public FieldValidation minValue(final double minValue, final String message) {
        return condition(value -> {
            boolean condition = false;
            if (Validator.isEmpty(value) || Validator.isNull(value)) {
                condition = false;
            } else if (!Validator.isNumber(fieldValue)) {
                condition = false;
            } else {
                condition = Validator.isMinValue(fieldValue, minValue);
            }
            return condition;
        }, message);
    }

    public FieldValidation maxValue(final double maxValue, final String message) {
        return condition(value -> {
            boolean condition = false;
            if (Validator.isEmpty(value)) {
                condition = false;
            } else if (!Validator.isNumber(fieldValue)) {
                condition = false;
            } else {
                condition = Validator.isMaxValue(fieldValue, maxValue);
            }
            return condition;
        }, message);
    }

    public FieldValidation isEmail(final String message) {
        return condition(value -> {
            boolean condition;
            if (Validator.isEmpty(value)) {
                condition = true;
            } else {
                condition = Validator.isEmail(fieldValue);
            }
            return condition;
        }, message);
    }

    public FieldValidation isNumber(final String message) {
        return condition(value -> {
            boolean condition;
            if (Validator.isEmpty(value)) {
                condition = true;
            } else {
                condition = Validator.isNumber(fieldValue);
            }
            return condition;
        }, message);
    }

    private boolean hasErrorAlready() {
        return parameterMap.hasError(fieldName);
    }

    public FieldValidation condition(final BooleanCondition condition, final String message) {
        return condition(condition, message, new Object[] {});
    }

    public FieldValidation condition(
            final BooleanCondition condition,
            final String message,
            final Object... arguments
    ) {
        final Optional<FieldValidation> fieldValidation = Optional.of(this);

        if (!condition.isTrue(fieldValue)) {
            parameterMap.reject(fieldName, message, arguments);
        }

        return fieldValidation.get();
    }
}
