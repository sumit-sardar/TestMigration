package com.ctb.oas.normsdata;

/**
 * @author TCS
 *
 */
public class NPLasLinksScorer extends NCELasLinksScorer {
	protected ScoreType getDestScoreType() {
		return ScoreType.NATIONAL_PERCENTILE;
	}

	protected ScoringStrategy getScoringStrategy() {
		return ScoringStrategy.GET_SCORES_FOR_SCALE_SCORE;
	}
}
