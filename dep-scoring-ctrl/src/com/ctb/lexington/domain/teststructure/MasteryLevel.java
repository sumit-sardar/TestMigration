package com.ctb.lexington.domain.teststructure;

public final class MasteryLevel extends StringConstant {
    public static final MasteryLevel NON_MASTERY = new MasteryLevel("NM", "NON-MASTERY");
    public static final MasteryLevel PARTIAL_MASTERY = new MasteryLevel("PM", "PARTIAL MASTERY");
    public static final MasteryLevel MASTERY = new MasteryLevel("M", "MASTERY");

    private MasteryLevel(final String code, final String description) {
        super(code, description);
    }

    public boolean isMastered() {
        return MASTERY == this;
    }

    public static MasteryLevel masteryLevelForPercentage(final int percentage) {
        if (percentage > 100) {
            throw new IllegalArgumentException("percentage cannot be greater than 100: "
                    + percentage);
        }
        if (percentage < 0) {
            throw new IllegalArgumentException("percentage must be positive: " + percentage);
        }

        MasteryLevel result = NON_MASTERY;
        if (percentage > 49 && percentage < 75) {
            result = PARTIAL_MASTERY;
        } else if (percentage > 74) {
            result = MASTERY;
        }

        return result;
    }
}