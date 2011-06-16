package com.ctb.oas.normsdata;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class SEMTerraNovaScorer extends TerraNovaScorer {

    protected ScoreType getSourceScoreType() {
        return ScoreType.NUMBER_CORRECT;
    }

    protected ScoreType getDestScoreType() {
        return ScoreType.STANDARD_ERROR_OF_MEASUREMENT;
    }

    protected ScoringStrategy getScoringStrategy() {
        return ScoringStrategy.GET_ALL_SCORES;
    }
}
