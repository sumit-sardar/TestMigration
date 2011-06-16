package com.ctb.oas.normsdata;

/**
 * @author TCS
 *
 */
public abstract class LasLinksScorer extends TerraNovaScorer {
	 
	public void handleHeader(String headerString) {
	        normsData.setFrameworkCode(ScoreRecord.LASLINKS_FRAMEWORK_CODE);
	        normsData.setProduct(ScoreRecord.LASLINKS_PRODUCT_NAME);
	        normsData.setForm(ScorerUtil.getForm(headerString));
	        normsData.setLevel(ScorerUtil.getLasLinkLevel(headerString));
	        normsData.setNormsGroup(ScorerUtil.getTrimester(headerString));
	        normsData.setNormsYear(ScoreRecord.NORMS_YEAR_VALUE);
	        normsData.setGrade(ScorerUtil.getLasLinksGrade(headerString));
	        normsData.setSourceScoreType(getSourceScoreType());
	        normsData.setDestScoreType(getDestScoreType());
	    }
	
	 public void handleInstruction(String instructionString) {
	        currentContentAreaScore = null;
	        currentScoreIndex = 0;
	        final String contentAreaString = ScorerUtil.getLasLinksContentArea(instructionString);
	        if (contentAreaString == null) {
	            return;
	        }

	        currentContentAreaScore = new ContentAreaScore(contentAreaString);
	        normsData.getContentAreaScores().put(contentAreaString, currentContentAreaScore);

	        if (getScoringStrategy().equals(ScoringStrategy.GET_ALL_SCORES))
	            setContentAreaScoreRange(0, getNumScores(instructionString) - 1);

	        if (getScoringStrategy().equals(ScoringStrategy.GET_SCORES_FOR_SCALE_SCORE)) {
	            final int startIndex = ParsedData.INSTANCE.getLowestContentAreaScaleScore(contentAreaString);
	            final int endIndex = ParsedData.INSTANCE.getHighestContentAreaScaleScore(contentAreaString);
	            setContentAreaScoreRange(startIndex, endIndex);
	        }
	    }
}
