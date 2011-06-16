package com.ctb.oas.normsdata;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class TABEScorerTest extends TestCase {
    private TABEScorer scorer;
    private File file = new File("test/data/tabe/CB9AMA.CSV");
    StringWriter writer;
    private static final String SCORE_TYPES_LINE = "\"NC\",\"SS\",\"SEM\",\"GE\",\"ABE all P\",\"ABE all NCE\",\"ABE all S\",\"ABE Juvenile P\",\"ABE Juvenile NCE\",\"ABE Juvenile S\"";
    private static final String SCORE_LINE = "\"49\",\"795\",\" 50\",\"12.9+\",\"99\",\"99\",\"9\",\"99\",\"99\",\"9\"";
    private static final int NUM_SCORES_PER_LINE = 9;
    private static final int NUM_SCORES_IN_FILE = 375;


    protected void setUp() throws Exception {
        super.setUp();
        assertTrue(file.exists());
        writer = new StringWriter();
        scorer = new TABEScorer(file, writer);
        scorer.normsData.setFrameworkCode(ScoreRecord.TABE_FRAMEWORK_CODE);
    }


    public void testContainScoreTypes() {
        assertTrue(scorer.containsScoreTypes(SCORE_TYPES_LINE));
    }

    public void testBuildScoreTypesArray() {
        scorer.buildScoreTypesList(SCORE_TYPES_LINE);
        assertEquals(10, scorer.scoreTypeList.size());
        assertEquals(ScoreType.NUMBER_CORRECT, scorer.scoreTypeList.get(0));
        assertEquals(ScoreType.ABE_J_S, scorer.scoreTypeList.get(scorer.scoreTypeList.size() - 1));
    }

    public void testContainsScore() {
        assertTrue(scorer.containsScore(SCORE_LINE));
    }

    public void testHandleScoreLine() throws IOException {
        scorer.buildScoreTypesList(SCORE_TYPES_LINE);
        scorer.handleScoreLine(SCORE_LINE);
        TestUtils.assertLineCount(writer, NUM_SCORES_PER_LINE);
    }

    public void testScore() throws IOException {
        scorer.score();
        TestUtils.assertLineCount(writer, NUM_SCORES_IN_FILE);
    }

}
