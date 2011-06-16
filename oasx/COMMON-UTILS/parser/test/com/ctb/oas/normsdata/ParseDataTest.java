package com.ctb.oas.normsdata;

import java.io.File;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class ParseDataTest extends NormsDataTestCase {
    public void testGetLowestContentAreaScaleScore() {
        loader = new TerraNovaLoader(new File(COMPLETE_BATTERY));
        loader.load();
        int lowestScore = ParsedData.INSTANCE.getLowestContentAreaScaleScore(READING);
        int highestScore = ParsedData.INSTANCE.getHighestContentAreaScaleScore(READING);
        assertEquals(355, lowestScore);
        assertEquals(701, highestScore);
    }
}
