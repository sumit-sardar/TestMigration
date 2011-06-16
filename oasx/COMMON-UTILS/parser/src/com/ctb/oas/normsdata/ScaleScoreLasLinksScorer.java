package com.ctb.oas.normsdata;

/**
 * @author TCS
 *
 */
public class ScaleScoreLasLinksScorer extends LasLinksScorer {

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
