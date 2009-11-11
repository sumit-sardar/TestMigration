package com.ctb.lexington.domain.teststructure;

import java.util.HashMap;

import com.ctb.lexington.util.SafeHashMap;

/**
 * Enumeration of test validation statuses
 * 
 * @author Vijay Aravamudhan
 * @version $Id$
 */
public final class ValidationStatus extends StringConstant {
    private static final HashMap ALL_STATUSES = new SafeHashMap(String.class, ValidationStatus.class);

    public static final ValidationStatus VALID = new ValidationStatus("VA", "Valid");
    public static final ValidationStatus INVALID = new ValidationStatus("IN", "Invalid");
    public static final ValidationStatus PARTIAL = new ValidationStatus("PI", "Partially Invalid");

    private ValidationStatus(final String code, final String description) {
        super(code, description);
        ALL_STATUSES.put(code, this);
    }

    public boolean isValid() {
        return VALID == this || PARTIAL == this;
    }

    public static ValidationStatus getByCode(final String code) {
        if (!ALL_STATUSES.containsKey(code)) {
            throw new IllegalArgumentException("No ValidationStatus found for: " + code);
        }
        return (ValidationStatus) ALL_STATUSES.get(code);
    }
}