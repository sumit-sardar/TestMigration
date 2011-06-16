package com.ctb.oas.normsdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class ParsedData {
    private Map scores = new HashMap();
    public static ParsedData INSTANCE = new ParsedData();

    private ParsedData() {
    };

    public List getScoreList(ScoreType destScoreType) {
        List list = (List) scores.get(destScoreType);
        if (list == null) {
            list = new ArrayList();
            scores.put(destScoreType, list);
        }

        return (List) list;
    }

    public int getLowestContentAreaScaleScore(String contentArea) {
        int lowestScore = Integer.MAX_VALUE;
        List scaleScoreNormsList = getScoreList(ScoreType.SCALE_SCORE);
        for (int i = 0; i < scaleScoreNormsList.size(); i++) {
            NormsData normsData = (NormsData) scaleScoreNormsList.get(i);
            ContentAreaScore contentAreaScore = (ContentAreaScore) normsData.getContentAreaScores().get(contentArea);
            if (contentAreaScore != null) {
                String scoreString = (String) contentAreaScore.getTargetScores().get(0);
                int scaleScore = Integer.parseInt(scoreString);
                if (scaleScore < lowestScore)
                    lowestScore = scaleScore;
            }
        }

        if (lowestScore == Integer.MAX_VALUE)
            return 0;

        return lowestScore;
    }

    public int getHighestContentAreaScaleScore(String contentAreaString) {
        int highestScore = 1;
        List scaleScoreNormsList = (List) scores.get(ScoreType.SCALE_SCORE);
        for (int i = 0; i < scaleScoreNormsList.size(); i++) {
            NormsData normsData = (NormsData) scaleScoreNormsList.get(i);
            ContentAreaScore contentAreaScore = (ContentAreaScore) normsData.getContentAreaScores().get(contentAreaString);
            if (contentAreaScore != null) {
                List targetScores = contentAreaScore.getTargetScores();
                for (int j = 0; j < targetScores.size(); j++) {
                    String scoreString = (String) targetScores.get(j);
                    int scaleScore = (Integer.parseInt(scoreString));
                    if (scaleScore > highestScore)
                        highestScore = scaleScore;
                }

            }
        }
        if (highestScore == 1)
            return 999;
        return highestScore;
    }

    public Map getScores() {
        return scores;
    }

    public void clearData() {
        scores.clear();
    }

    public int getExtendedGEDScore(final String normsGroup, final String contentArea) {
        final List extendedGEDScoreList = getScoreList(ScoreType.EXTENDED_GRADE_EQUIVALENT);
        for (int i = 0; i < extendedGEDScoreList.size(); i++) {
            final NormsData normsData = (NormsData) extendedGEDScoreList.get(i);
            if (normsData.getNormsGroup().equals(normsGroup)) {
                ContentAreaScore contentAreaScore = (ContentAreaScore) normsData.getContentAreaScores().get(contentArea);
                String scaleScoreCutOffForExtendedGED = (String) contentAreaScore.getTargetScores().get(0);
                return Integer.parseInt(scaleScoreCutOffForExtendedGED);
            }
        }
        return -1;
    }
}
