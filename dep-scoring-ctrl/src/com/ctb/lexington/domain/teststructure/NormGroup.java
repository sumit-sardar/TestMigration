package com.ctb.lexington.domain.teststructure;

import java.util.HashMap;

import com.ctb.lexington.util.SafeHashMap;

/**
 * @author mnkamiya
 * @version $Id$
 */
public final class NormGroup extends StringConstant {
    private static final HashMap ALL_STATUSES_BY_INTEGER = new SafeHashMap(Integer.class, NormGroup.class);

    public final static NormGroup FALL = new NormGroup(new Integer(6), "FALL", "Fall");
    public final static NormGroup FALLTN = new NormGroup(new Integer(7), "FALL", "Fall");
    public final static NormGroup WINTER = new NormGroup(new Integer(19), "WINTER", "Winter");
    public final static NormGroup SPRING = new NormGroup(new Integer(30), "SPRING", "Spring");
    public final static NormGroup SPRINGTN = new NormGroup(new Integer(31), "SPRING", "Spring");

    private final Integer intValue;

    private NormGroup(final Integer intValue, final String code, final String description) {
        super(code, description);
        this.intValue = intValue;
        ALL_STATUSES_BY_INTEGER.put(intValue, this);
    }

    public final Integer getIntValue() {
        return intValue;
    }

    public static NormGroup getByIntegerValue(final Integer integerValue) {
        if (integerValue == null)
            return null;
        if (!ALL_STATUSES_BY_INTEGER.containsKey(integerValue)) {
            throw new IllegalArgumentException("No NormGroup found for: " + integerValue);
        }
        return (NormGroup) ALL_STATUSES_BY_INTEGER.get(integerValue);
    }

    public static String getCodeForIntegerValue(final Integer integerValue) {
        final NormGroup group = getByIntegerValue(integerValue);
        return group == null ? null : group.getCode();
    }

    public static String getCodeForStringValue(final String integerValue) {
        try {
            return getCodeForIntegerValue(Integer.valueOf(integerValue));
        } catch (NumberFormatException nfe) {
            return integerValue;
        } catch (NullPointerException npe) {
            return null;
        }
    }
}