package com.ctb.oas.normsdata;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class ScoringStrategy {
    private int type;
    public static ScoringStrategy GET_ALL_SCORES = new ScoringStrategy(0);
    public static ScoringStrategy GET_SCORES_FOR_SCALE_SCORE = new ScoringStrategy(1);


    private ScoringStrategy(int type) {
        this.type = type;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ScoringStrategy) {
            ScoringStrategy scoringStrategy = (ScoringStrategy) obj;
            return (scoringStrategy.type == this.type);
        }
        return false;
    }
}
