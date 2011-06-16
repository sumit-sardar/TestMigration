package com.ctb.oas.normsdata;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author Sreenivas  Ananthakrishna
 */
public class EGEDScorerTest extends TestCase {
    private static final String FILE1 = "test/data/tabe/eged/TM9EGEDMA.csv";
    private EGEDScorer scorer;
    private static final String SCORE_LINE = "250-255,180";
    private Writer writer = new StringWriter();
    private static final String TOTAL_MATHEMATICS = "Total Mathematics";

    protected void setUp() throws Exception {
        super.setUp();
        File inputFile = new File(FILE1);
        assertTrue(inputFile.exists());
        scorer = new EGEDScorer(writer, inputFile);
    }

    public void testHandleScoreLine() throws IOException {
        scorer.handleScoreLine(SCORE_LINE);
        TestUtils.assertLineCount(writer, 6);
        String output = writer.toString();
        String firstLine = output.substring(0, output.indexOf("\n"));
        assertTrue(firstLine.indexOf("250,180") != -1);
        assertTrue(firstLine.indexOf(ScoreType.SCALE_SCORE.getSQLValue() + "," + ScoreType.EGEDMA.getSQLValue()) != -1);
        assertTrue(firstLine.indexOf(TOTAL_MATHEMATICS) != -1);
    }

}
