package com.ctb.oas.normsdata;

import junit.framework.TestCase;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class ScoreRecordTest extends TestCase {
    private static final String SCORE_LOOKUP_ID = "ID";
    private static final String READING = "Reading";
    private static final Integer SOURCE_SCORE_VALUE = new Integer(100);
    private static final Float DEST_SCORE_VALUE = new Float(0.9);
    private static final String EXPECTED_RESULT = "ID,,,Reading,,,,,,,,,100,0.9";

    public void testToString() {
        ScoreRecord record = new ScoreRecord();
        record.putValue(Column.SCORE_LOOKUP_ID, SCORE_LOOKUP_ID);
        record.putValue(Column.CONTENT_AREA, READING);
        record.putValue(Column.SOURCE_SCORE_VALUE, SOURCE_SCORE_VALUE);
        record.putValue(Column.DEST_SCORE_VALUE, DEST_SCORE_VALUE);
        assertEquals(EXPECTED_RESULT, record.toString());
    }
}
