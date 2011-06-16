package com.ctb.oas.normsdata;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class NCETerraNovaScorer extends TerraNovaScorer {
    private boolean skipTable = false;

    protected ScoreType getSourceScoreType() {
        return ScoreType.SCALE_SCORE;
    }

    protected ScoreType getDestScoreType() {
        return ScoreType.NORMAL_CURVE_EQUIVALENT;
    }

    protected ScoringStrategy getScoringStrategy() {
        return ScoringStrategy.GET_SCORES_FOR_SCALE_SCORE;
    }

    public void handleHeader(String headerString) {
        super.handleHeader(headerString);
        if (normsData.getNormsGroup() == null) {
            normsData = null;
            skipTable = true;
        }
    }

    public void handleInstruction(String line) {
        if (skipTable)
            return;
        super.handleInstruction(line);
    }

    public void handleScoreLine(String line) {
        if (skipTable)
            return;
        super.handleScoreLine(line);
    }
}
