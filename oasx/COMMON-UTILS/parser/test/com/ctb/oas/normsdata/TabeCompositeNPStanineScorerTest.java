package com.ctb.oas.normsdata;

import junit.framework.TestCase;

import java.io.File;
import java.io.StringWriter;

/**
 * @author Sreenivas  Ananthakrishna
 */
public class TabeCompositeNPStanineScorerTest extends TestCase {
    File file = new File("test/data/tabe/totma/TM910SS_NPR2.txt");
    private StringWriter writer = new StringWriter();

    protected void setUp() throws Exception {
        super.setUp();
        assertTrue(file.exists());
    }

    public void testScore() {
        TabeCompositeNPStanineScorer scorer = new TabeCompositeNPStanineScorer(file, writer);
        scorer.score();
        System.out.println(writer.toString());
    }
}
