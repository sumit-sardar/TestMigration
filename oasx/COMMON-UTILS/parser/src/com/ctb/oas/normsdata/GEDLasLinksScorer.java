package com.ctb.oas.normsdata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TCS
 *
 */
public class GEDLasLinksScorer extends LasLinksScorer {
	Map extendedGEDScoresMap = new HashMap();

	protected ScoreType getSourceScoreType() {
		return ScoreType.SCALE_SCORE;
	}

	protected ScoreType getDestScoreType() {
		return ScoreType.GRADE_EQUIVALENT;
	}

	protected ScoringStrategy getScoringStrategy() {
		return ScoringStrategy.GET_SCORES_FOR_SCALE_SCORE;
	}

	public void handleInstruction(String instructionString) {
		super.handleInstruction(instructionString);
		int scaleScoreCutOffForExtendeGED = ParsedData.INSTANCE
				.getExtendedGEDScore(normsData.getNormsGroup(),
						currentContentAreaScore.getContentAreaString());
		extendedGEDScoresMap.put(
				currentContentAreaScore.getContentAreaString(), new Integer(
						scaleScoreCutOffForExtendeGED));
	}

	protected void addScore(final List list, final String inputScoreString) {
		final Integer scaleScoreCutOffForExtendeGED = (Integer) extendedGEDScoresMap
				.get(currentContentAreaScore.getContentAreaString());

		if (scaleScoreCutOffForExtendeGED.intValue() != -1
				&& currentScoreIndex >= scaleScoreCutOffForExtendeGED
						.intValue())
			list.add(inputScoreString + "+");
		else
			list.add(inputScoreString);
	}
}
