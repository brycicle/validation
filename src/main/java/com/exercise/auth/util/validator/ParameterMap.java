package com.exercise.auth.util.validator;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Data
public class ParameterMap extends HashMap<String, Object> {

    private Map<String, String> errorMap = new HashMap<>();

    /**
     * Returns the String used on validator
     * @return The String used on validator
     */
    public String getString(final String key) {
        final Optional<Object> val = Optional.ofNullable(get(key));
        String stringValue = "";

        if (val.isPresent()) {
            stringValue = val.get() instanceof String ? val.get().toString() : String.valueOf(val.get());
        }

        return val.isEmpty() ? null : stringValue;
    }

    /** Reject
     * @param field FieldName to be rejected
     * @param message Error Message to be thrown
     */
    public void reject(final String field, final String message) {
        reject(field, message, new Object[] {});
    }

    /** Reject
     * @param field FieldName to be rejected
     * @param message Error Message to be thrown
     */
    public void reject(final String field, final String message, final Object... arguments) {
        log.info("Request Field Exception : {}", field);
        errorMap.put(field, message);
    }

    /** Calls the constructor of FieldValidation
     * @return FieldValidation object used to validate fields
     */
    public FieldValidation validate(final String fieldName) {
        return new FieldValidation(fieldName, getString(fieldName), this);
    }

    /** Calls the equals of ConcurrentHashMap
     * @return Boolean value if the compared object is equal
     */
    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (super.equals(obj)) {
            result = true;
        }
        return result;
    }

    /** Calls the hashCode of ConcurrentHashMap
     * @return Hashcode value
     */
    @Override
    public int hashCode() {
        final int hashCode = super.hashCode();
        return hashCode;
    }

    /** Checks if there is an error thrown on validation
     * @return hasValidationError
     */
    public boolean hasErrors() {
        return !errorMap.isEmpty();
    }


    /** Checks if there is an error for a field
     * @return hasError
     */
    public boolean hasError(String key) {
        return errorMap.containsKey(key);
    }
}
