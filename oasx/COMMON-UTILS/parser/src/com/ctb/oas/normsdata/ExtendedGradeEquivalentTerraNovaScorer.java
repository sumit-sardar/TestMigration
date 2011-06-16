package com.ctb.oas.normsdata;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class ExtendedGradeEquivalentTerraNovaScorer extends TerraNovaScorer {

    protected ScoreType getSourceScoreType() {
        return ScoreType.SCALE_SCORE;
    }

    protected ScoreType getDestScoreType() {
        return ScoreType.EXTENDED_GRADE_EQUIVALENT;
    }

    protected ScoringStrategy getScoringStrategy() {
        return ScoringStrategy.GET_ALL_SCORES;
    }
}
