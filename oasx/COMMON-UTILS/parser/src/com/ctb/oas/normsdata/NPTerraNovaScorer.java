package com.ctb.oas.normsdata;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class NPTerraNovaScorer extends NCETerraNovaScorer {
    protected ScoreType getDestScoreType() {
        return ScoreType.NATIONAL_PERCENTILE;
    }

    protected ScoringStrategy getScoringStrategy() {
        return ScoringStrategy.GET_SCORES_FOR_SCALE_SCORE;
    }
}
