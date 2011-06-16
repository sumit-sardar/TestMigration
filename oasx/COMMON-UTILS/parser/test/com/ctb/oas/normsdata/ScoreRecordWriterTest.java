package com.ctb.oas.normsdata;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public abstract class ScoreRecordWriterTest extends NormsDataTestCase {
    protected StringWriter writer = new StringWriter();

    public void assertSQLOutput(ScoreRecordWriter writer, NormsData data, ContentAreaScore contentAreaScore, String firstScorePair, String lastScorePair, int count) throws IOException {
        writer.writeScoreRecord(this.writer, data, contentAreaScore);
        String output = this.writer.toString();
        LineNumberReader reader = new LineNumberReader(new StringReader(output));
        String sql = reader.readLine();
        String lastStatement = null;
        if (sql.indexOf(firstScorePair) == -1)
            fail("Could not find " + firstScorePair + " in " + sql);
        int x = 1;

        while ((sql = reader.readLine()) != null) {
            x = x + 1;
            lastStatement = sql;
        }
        assertEquals(count, x);

        if (lastStatement != null && lastStatement.indexOf(lastScorePair) == -1)
            fail("Could not find " + lastScorePair + " in " + lastStatement);
    }
}
