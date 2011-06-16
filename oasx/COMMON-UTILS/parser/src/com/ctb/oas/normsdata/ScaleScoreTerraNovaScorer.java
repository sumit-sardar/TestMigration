package com.ctb.oas.normsdata;


/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class ScaleScoreTerraNovaScorer extends TerraNovaScorer {

    protected ScoreType getSourceScoreType() {
        return ScoreType.NUMBER_CORRECT;
    }

    protected ScoreType getDestScoreType() {
        return ScoreType.SCALE_SCORE;
    }

    protected ScoringStrategy getScoringStrategy() {
        return ScoringStrategy.GET_ALL_SCORES;
    }

}
