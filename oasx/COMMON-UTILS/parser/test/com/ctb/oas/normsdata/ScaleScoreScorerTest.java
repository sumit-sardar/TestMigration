package com.ctb.oas.normsdata;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.List;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class ScaleScoreScorerTest extends NormsDataTestCase {
    private static final String SCALE_SCORE_INSTRUCTION = " 5 0033007";

    private ScaleScoreTerraNovaScorer scorer = new ScaleScoreTerraNovaScorer();


    public void testGetNumScores() {
        assertEquals(33, scorer.getNumScores(SCALE_SCORE_INSTRUCTION));
    }

    public void testGetNormsDataList() throws IOException {
        StringReader reader = new StringReader(SCALE_SCORE_DATA);
        loader.load(new LineNumberReader(reader));
        List normsList = ParsedData.INSTANCE.getScoreList(ScoreType.SCALE_SCORE);
        assertEquals(1, normsList.size());
    }
}
