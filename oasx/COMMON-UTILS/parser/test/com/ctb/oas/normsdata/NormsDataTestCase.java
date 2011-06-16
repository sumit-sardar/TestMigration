package com.ctb.oas.normsdata;

import junit.framework.TestCase;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public abstract class NormsDataTestCase extends TestCase {
    public static final String READING = "Reading";
    protected TerraNovaLoader loader = new TerraNovaLoader();
    protected String COMPLETE_BATTERY = "test/data/complete_battery.txt";
    protected static final String SCALE_SCORE_DATA = "1 NUMBER CORRECT - SCALE SCORE NORMS FOR TN CS            FM A  Level 12\n" +
            "2 073   001001001\n" +
            "5 0026001\n" +
            "6 423423423423423423508530542550557563569574579584589595601609617627638652673722";

    protected void tearDown() throws Exception {
        super.tearDown();
        ParsedData.INSTANCE.clearData();
    }
}
