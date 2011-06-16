package com.ctb.oas.normsdata;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.List;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class NPScorerTest extends NormsDataTestCase {
    private final String NP_SCORE = "test/data/np_score.txt";

    public void testScorer() throws FileNotFoundException {
        loader.load(new LineNumberReader(new FileReader(NP_SCORE)));
        final List scoreList = ParsedData.INSTANCE.getScoreList(ScoreType.NATIONAL_PERCENTILE);
        assertEquals(1, scoreList.size()); // the first NP score should not be added as it does not have score data
        NormsData data = (NormsData) scoreList.get(0);
        ContentAreaScore score = (ContentAreaScore) data.getContentAreaScores().get("Reading");
        assertEquals("Reading", score.getContentAreaString());
        assertEquals(1000, score.getTargetScores().size());
        final String scoreString = (String) score.getTargetScores().get(0);
        assertEquals(1, Integer.parseInt(scoreString));
        final String scoreString2 = (String) score.getTargetScores().get(score.getTargetScores().size() - 1);
        assertEquals(99, Integer.parseInt(scoreString2));
    }
}
