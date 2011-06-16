package com.ctb.oas.normsdata;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public abstract class TerraNovaScorer {
    protected NormsData normsData = new NormsData();
    protected ContentAreaScore currentContentAreaScore;
    protected int currentScoreIndex;

    protected boolean skipLine = false;

    protected abstract ScoreType getSourceScoreType();

    protected abstract ScoreType getDestScoreType();

    protected abstract ScoringStrategy getScoringStrategy();

    public void handleHeader(String headerString) {
        normsData.setFrameworkCode(ScoreRecord.TERRANOVA_FRAMEWORK_CODE);
        normsData.setProduct(ScoreRecord.TERRANOVA_PRODUCT_NAME);
        normsData.setForm(ScorerUtil.getForm(headerString));
        normsData.setLevel(ScorerUtil.getLevel(headerString));
        normsData.setNormsGroup(ScorerUtil.getTrimester(headerString));
        normsData.setNormsYear(ScoreRecord.NORMS_YEAR_VALUE);
        normsData.setGrade(ScorerUtil.getGrade(headerString));
        normsData.setSourceScoreType(getSourceScoreType());
        normsData.setDestScoreType(getDestScoreType());
    }


    public void handleInstruction(String instructionString) {
        currentContentAreaScore = null;
        currentScoreIndex = 0;
        final String contentAreaString = ScorerUtil.getContentArea(instructionString);
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

    public void handleScoreLine(String scoreLine) {
        String scoreString = ScorerUtil.getScoreString(scoreLine);

        if (skipCurrentLine(scoreString))
            return;

        List list = currentContentAreaScore.getTargetScores();

        for (int i = 0; i < scoreString.length(); i = i + 3, currentScoreIndex++) {
            String score = scoreString.substring(i, i + 3);
            if (currentScoreIndex >= currentContentAreaScore.getSourceScoreStartIndex() && currentScoreIndex <= currentContentAreaScore.getSourceScoreEndIndex()) {
                addScore(list, score);
            }
        }

    }

    protected void addScore(List list, String score) {
        list.add(score);
    }


    protected int getNumScores(String instructionLine) {
        instructionLine = instructionLine.trim();
        return Integer.parseInt(instructionLine.substring(2, 6));
    }


    public NormsData getNormsData() {
        if (normsData != null && checkScores())
            return normsData;

        return null;
    }

    public boolean checkScores() {
        Map contentAreaScores = normsData.getContentAreaScores();
        if (contentAreaScores.isEmpty())
            return false;

        Set keys = contentAreaScores.keySet();
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            String contentArea = (String) iterator.next();
            ContentAreaScore contentAreaScore = (ContentAreaScore) contentAreaScores.get(contentArea);

            final int expectedScore = contentAreaScore.getSourceScoreEndIndex() - contentAreaScore.getSourceScoreStartIndex() + 1;
            if (contentAreaScore.getTargetScores().size() != expectedScore)
                throw new InconstentScoreException(contentAreaScore.getTargetScores().size(), contentAreaScore.getContentAreaString(),
                        expectedScore, getClass().getName());
        }
        return true;
    }

    private boolean skipCurrentLine(String scoreString) {
        if (currentContentAreaScore == null)
            return true;

        int numScoresInLine = scoreString.length() / 3;

        final int start = currentContentAreaScore.getSourceScoreStartIndex();
        final int end = currentContentAreaScore.getSourceScoreEndIndex();
        if (((currentScoreIndex + numScoresInLine) < start) || (currentScoreIndex > end)) {
            currentScoreIndex += numScoresInLine;
            return true;
        }

        return false;
    }

    protected void setContentAreaScoreRange(int start, int end) {
        currentContentAreaScore.setSourceScoreStart(start);
        currentContentAreaScore.setSourceScoreEnd(end);
    }

}
