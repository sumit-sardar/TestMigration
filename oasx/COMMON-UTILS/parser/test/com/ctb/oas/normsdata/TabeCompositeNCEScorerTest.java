package com.ctb.oas.normsdata;

import junit.framework.TestCase;

import java.io.File;
import java.io.StringWriter;

/**
 * @author Sreenivas  Ananthakrishna
 */
public class TabeCompositeNCEScorerTest extends TestCase {
    private File file = new File("test/data/tabe/totma/TM910SS_NCE1.txt");
    private StringWriter writer = new StringWriter();

    public void setUp() {
        assertTrue(file.exists());
    }

    public void testScore() {
        TabeCompositeScorer scorer = new TabeCompositeNCEScorer(file, writer);
        scorer.score();
        System.out.println(writer.toString());
    }
}
