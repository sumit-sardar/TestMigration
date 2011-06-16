package com.ctb.oas.normsdata;

/**
 * @author TCS
 *
 */
public class SEMLasLinksScorer extends LasLinksScorer {

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
