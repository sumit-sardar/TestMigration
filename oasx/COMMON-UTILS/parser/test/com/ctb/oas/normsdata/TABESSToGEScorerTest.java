package com.ctb.oas.normsdata;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

/**
 * @author Sreenivas  Ananthakrishna
 */
public class TABESSToGEScorerTest extends TestCase {
    private File file = new File("test/data/tabe/ge/tabe_ss_ge.csv");
    private TABESSToGEScorer scorer;
    private static final String SCORE_LINE = "9,940,129";

    protected void setUp() throws Exception {
        super.setUp();
        scorer = new TABESSToGEScorer(writer, file);
    }


    private StringWriter writer = new StringWriter();

    public void testHandleScoreLine() {
        scorer.handleScoreLine(SCORE_LINE);
        String output = writer.toString();
        assertContains(output, ContentAreaLookup.getTABEContentArea("9"));
        assertContains(output, "940,12.9");
        assertContains(output, ScoreRecord.TABE_FRAMEWORK_CODE);
        assertContains(output, ScoreRecord.TABE_FORM_VALUE);

    }

    public void testScore() throws IOException {
        scorer.score();
        TestUtils.assertLineCount(writer, 9000);

    }

    private void assertContains(String output, String expected) {
        assertNotNull(output);
        assertTrue(output.indexOf(expected) != -1);
    }
}

