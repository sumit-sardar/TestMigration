package com.ctb.oas.normsdata;

/**
 * @author TCS
 * 
 */
public class ExtendedGradeEquivalentLasLinksScorer extends LasLinksScorer {

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
