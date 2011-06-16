package com.ctb.oas.normsdata;

/**
 * @author TCS
 * 
 */
public class ReferencialPercentileLaslinksScore extends LasLinksScorer {

	protected ScoreType getDestScoreType() {
		return ScoreType.REFERENCIAL_NATIONAL_PERCENTILE;
	}

	protected ScoringStrategy getScoringStrategy() {
		return ScoringStrategy.GET_SCORES_FOR_SCALE_SCORE;
		//return ScoringStrategy.GET_ALL_SCORES;
	}

	protected ScoreType getSourceScoreType() {
		return ScoreType.SCALE_SCORE;
	}

}
