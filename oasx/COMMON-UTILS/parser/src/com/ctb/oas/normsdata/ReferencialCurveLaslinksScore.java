package com.ctb.oas.normsdata;

/**
 * @author TCS
 *
 */
public class ReferencialCurveLaslinksScore extends LasLinksScorer {

	protected ScoreType getDestScoreType() {
		return ScoreType.REFERENCIAL_NATIONAL_CURVE;
	}

	protected ScoringStrategy getScoringStrategy() {
		return ScoringStrategy.GET_SCORES_FOR_SCALE_SCORE;
		//return ScoringStrategy.GET_ALL_SCORES;
	}

	protected ScoreType getSourceScoreType() {
		return ScoreType.SCALE_SCORE;
	}

}
