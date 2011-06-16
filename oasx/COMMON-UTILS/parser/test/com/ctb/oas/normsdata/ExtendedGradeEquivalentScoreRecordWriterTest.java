package com.ctb.oas.normsdata;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.List;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class ExtendedGradeEquivalentScoreRecordWriterTest extends ScoreRecordWriterTest {
    public void testOutput() throws IOException {
        loader.load(new LineNumberReader(new FileReader(COMPLETE_BATTERY)));
        List normsList = (List) ParsedData.INSTANCE.getScoreList(ScoreType.EXTENDED_GRADE_EQUIVALENT);
        NormsData data = (NormsData) normsList.get(0);
        ContentAreaScore contentAreaScore = (ContentAreaScore) data.getContentAreaScores().get(READING);
        ExtendedGradeEquivalentScoreRecordWriter generator = new ExtendedGradeEquivalentScoreRecordWriter();
        assertSQLOutput(generator, data, contentAreaScore, "711,12.9", "711,12.9", 1);
    }
}
