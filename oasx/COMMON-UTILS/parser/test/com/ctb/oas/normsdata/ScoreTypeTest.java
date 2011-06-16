package com.ctb.oas.normsdata;

import junit.framework.TestCase;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class ScoreTypeTest extends TestCase {
    public void testGetScoreTypeForTABE() {
        assertEquals(ScoreType.NUMBER_CORRECT, ScoreType.getScoreTypeForTABE("NC"));
        assertEquals(ScoreType.ABE_ALL_P, ScoreType.getScoreTypeForTABE("ABE all P"));
        assertEquals(ScoreType.ABE_J_P, ScoreType.getScoreTypeForTABE("ABE Juvenile P"));
    }
}
