package com.ctb.lexington.domain.teststructure;

public final class MasteryLevel extends StringConstant {
    public static final MasteryLevel NON_MASTERY = new MasteryLevel("NM", "NON-MASTERY");
    public static final MasteryLevel PARTIAL_MASTERY = new MasteryLevel("PM", "PARTIAL MASTERY");
    public static final MasteryLevel MASTERY = new MasteryLevel("M", "MASTERY");
    //New mastery levels added for tabe adaptive (CAT) engine
    public static final MasteryLevel BEGINNER = new MasteryLevel("BG", "BEGINNER");
    public static final MasteryLevel ADVANCED = new MasteryLevel("AV", "ADVANCED");

    private MasteryLevel(final String code, final String description) {
        super(code, description);
    }

    public boolean isMastered() {
        return MASTERY == this;
    }
    
    public boolean isMasteredForTabeAdaptive() {
        return (MASTERY == this || ADVANCED == this);
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
    
    // Added for tabe adaptive product
    public static MasteryLevel masteryLevelForAdaptive(final int level) {
        
        if (level < 0) {
            throw new IllegalArgumentException("level must be positive: " + level);
        }

        MasteryLevel result = NON_MASTERY;
        if(level == 0) {
        	result = NON_MASTERY;
        } else if (level == 1) {
        	result = BEGINNER;
        } else if (level == 2) {
        	result = PARTIAL_MASTERY;
        } else if (level == 3) {
        	result = MASTERY;
        } else if (level == 4) {
        	result = ADVANCED;
        }

        return result;
    }
    
}