package com.ctb.oas.normsdata;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.List;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class DefaultScoreRecordWriterTest extends ScoreRecordWriterTest {
    private List normsDataList;
    private DefaultScoreRecordWriter generator;
    private static final String FORM_A = "A";
    private static final String LEVEL = "20";

    public void setUp() throws Exception {
        super.setUp();
        loader.load(new LineNumberReader(new FileReader(COMPLETE_BATTERY)));
        generator = new DefaultScoreRecordWriter();
    }

    public void testGenerateSQL() {
        NormsData data = getFirstNormsData(ScoreType.SCALE_SCORE);
        ContentAreaScore contentArea = (ContentAreaScore) data.getContentAreaScores().get(READING);
        assertNotNull(contentArea);
        int sourceScore = contentArea.getSourceScoreStartIndex();
        Object targetScore = contentArea.getTargetScores().get(0);
        generator.writeScoreRecord(writer, data, contentArea.getContentAreaString(), new Integer(sourceScore), targetScore);
        String sql = writer.toString();
        assertNameExists(READING, sql);
        assertNameExists(FORM_A, sql);
        assertNameExists(LEVEL, sql);
        assertNameExists(ScoreType.NUMBER_CORRECT.getSQLValue(), sql);
        assertNameExists(ScoreType.SCALE_SCORE.getSQLValue(), sql);
    }

    public void testWriteSQLForNCToSS() throws IOException {
        NormsData data = getFirstNormsData(ScoreType.SCALE_SCORE);
        ContentAreaScore contentAreaScore = (ContentAreaScore) data.getContentAreaScores().get(READING);
        assertSQLOutput(generator, data, contentAreaScore, "0,355", "20,626", 21);
    }

    public void testWriteSQLForNCToSEM() throws IOException {
        NormsData data = getFirstNormsData(ScoreType.STANDARD_ERROR_OF_MEASUREMENT);
        ContentAreaScore contentAreaScore = (ContentAreaScore) data.getContentAreaScores().get(READING);
        assertSQLOutput(generator, data, contentAreaScore, "0,126", "20,61", 21);
    }

    public void testWriteSQLForSSToGED() throws IOException {
        NormsData data = getFirstNormsData(ScoreType.GRADE_EQUIVALENT);
        ContentAreaScore contentAreaScore = (ContentAreaScore) data.getContentAreaScores().get(READING);
        int lowestScaleScore = ParsedData.INSTANCE.getLowestContentAreaScaleScore(contentAreaScore.getContentAreaString());
        int highestScaleScore = ParsedData.INSTANCE.getHighestContentAreaScaleScore(contentAreaScore.getContentAreaString());
        assertSQLOutput(generator, data, contentAreaScore, lowestScaleScore + ",0.0", highestScaleScore + ",11.2", highestScaleScore - lowestScaleScore + 1);
    }

    public void testWriteSQLForSSToMGED() throws IOException {
        NormsData data = getFirstNormsData(ScoreType.MEAN_GRADE_EQUIVALENT);
        ContentAreaScore contentAreaScore = (ContentAreaScore) data.getContentAreaScores().get(READING);
        int lowestScaleScore = ParsedData.INSTANCE.getLowestContentAreaScaleScore(contentAreaScore.getContentAreaString());
        int highestScaleScore = ParsedData.INSTANCE.getHighestContentAreaScaleScore(contentAreaScore.getContentAreaString());
        assertSQLOutput(generator, data, contentAreaScore, lowestScaleScore + ",0.0", highestScaleScore + ",11.7", highestScaleScore - lowestScaleScore + 1);
    }

    public void testWriteSQLForSSToNCE() throws IOException {
        NormsData data = getFirstNormsData(ScoreType.NORMAL_CURVE_EQUIVALENT);
        ContentAreaScore contentAreaScore = (ContentAreaScore) data.getContentAreaScores().get(READING);
        int lowestScaleScore = ParsedData.INSTANCE.getLowestContentAreaScaleScore(contentAreaScore.getContentAreaString());
        int highestScaleScore = ParsedData.INSTANCE.getHighestContentAreaScaleScore(contentAreaScore.getContentAreaString());
        assertSQLOutput(generator, data, contentAreaScore, lowestScaleScore + ",1", highestScaleScore + ",99", highestScaleScore - lowestScaleScore + 1);
    }

    public void testWriteSQLForNCToNP() throws IOException {
        NormsData data = getFirstNormsData(ScoreType.NATIONAL_PERCENTILE);
        ContentAreaScore contentAreaScore = (ContentAreaScore) data.getContentAreaScores().get(READING);
        int lowestScaleScore = ParsedData.INSTANCE.getLowestContentAreaScaleScore(contentAreaScore.getContentAreaString());
        int highestScaleScore = ParsedData.INSTANCE.getHighestContentAreaScaleScore(contentAreaScore.getContentAreaString());
        assertSQLOutput(generator, data, contentAreaScore, lowestScaleScore + ",1", highestScaleScore + ",99", highestScaleScore - lowestScaleScore + 1);
    }

    private NormsData getFirstNormsData(ScoreType scoreType) {
        normsDataList = ParsedData.INSTANCE.getScoreList(scoreType);
        return (NormsData) normsDataList.get(0);
    }


    public void assertNameNotExists(String columnName, String sql) {
        if (sql.indexOf(columnName) == 1)
            fail("sql should not contain :" + columnName);
    }

    public void assertNameExists(String columnName, String sql) {
        if (sql.indexOf(columnName) == -1)
            fail("sql does not contain :" + columnName);
    }
}
