package com.ctb.oas.normsdata;

import java.util.List;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class MeanGEDTerraNovaScorer extends TerraNovaScorer {
    boolean extendedGMEValueStart = false;

    protected ScoreType getSourceScoreType() {
        return ScoreType.SCALE_SCORE;
    }

    protected ScoreType getDestScoreType() {
        return ScoreType.MEAN_GRADE_EQUIVALENT;
    }

    protected ScoringStrategy getScoringStrategy() {
        return ScoringStrategy.GET_SCORES_FOR_SCALE_SCORE;
    }

    public void handleInstruction(String instructionString) {
        extendedGMEValueStart = false;
        super.handleInstruction(instructionString);
    }

    protected void addScore(List list, String inputScoreString) {
        if (extendedGMEValueStart)
            list.add(inputScoreString + "+");
        else {
            int gmeValue = Integer.parseInt(inputScoreString);

            if (gmeValue == 129)
                extendedGMEValueStart = true;

            list.add(inputScoreString);
        }
    }
}