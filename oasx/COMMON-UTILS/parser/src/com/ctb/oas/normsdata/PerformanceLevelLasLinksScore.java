package com.ctb.oas.normsdata;

/**
 * @author TCS
 *
 */
public class PerformanceLevelLasLinksScore extends LasLinksScorer {

	protected ScoreType getDestScoreType() {
		return ScoreType.PERFORMANCE_LEVEL;
	}

	protected ScoringStrategy getScoringStrategy() {
		// return ScoringStrategy.GET_SCORES_FOR_SCALE_SCORE;
		return ScoringStrategy.GET_ALL_SCORES;
	}

	protected ScoreType getSourceScoreType() {
		return ScoreType.SCALE_SCORE;
	}

}
