package com.ctb.lexington.domain.teststructure;

import java.util.HashMap;

import com.ctb.lexington.util.SafeHashMap;

public final class PerformanceLevel extends StringConstant {
    private static final HashMap ALL_LEVELS = new SafeHashMap(String.class, PerformanceLevel.class);

    public static final PerformanceLevel ONE = new PerformanceLevel("1", "Starting Out (Primary)/Step 1");
    public static final PerformanceLevel TWO = new PerformanceLevel("2", "Progressing");
    public static final PerformanceLevel THREE = new PerformanceLevel("3", "Nearing Proficiency");
    public static final PerformanceLevel FOUR = new PerformanceLevel("4", "Proficient");
    public static final PerformanceLevel FIVE = new PerformanceLevel("5", "Advanced");

    private PerformanceLevel(final String code, final String description) {
        super(code, description);
        ALL_LEVELS.put(code, this);
    }

    public static PerformanceLevel getByCode(final String code) {
        if (!ALL_LEVELS.containsKey(code)) {
            throw new IllegalArgumentException("No PerformanceLevel found for: " + code);
        }
        return (PerformanceLevel) ALL_LEVELS.get(code);
    }
}