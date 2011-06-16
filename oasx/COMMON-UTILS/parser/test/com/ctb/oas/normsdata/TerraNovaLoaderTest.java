package com.ctb.oas.normsdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.List;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class TerraNovaLoaderTest extends NormsDataTestCase {
    protected File surveryFile = new File("test/data/terranova/complete_battery.txt");
    private static final String FORM_A = "A";
    private static final String FALL_TRIMESTER = "FALL";
    private static final String READING = "Reading";
    private static final Object VOCABULARY = "Vocabulary";


    public void setUp() {
        assertTrue(surveryFile.exists());
    }

    public void testLoadScaleScore() throws FileNotFoundException {
        loader.load(new LineNumberReader(new FileReader(surveryFile)));
        assertScaleScoreData();
        assertSEMData();
        assertGEDData();
        assertExtendedGEDData();
        assertMeanGradeEquivalentData();

    }

    private void assertMeanGradeEquivalentData() {
        List meangeNormsList = (List) ParsedData.INSTANCE.getScoreList(ScoreType.MEAN_GRADE_EQUIVALENT);
        assertNotNull(meangeNormsList);
        assertEquals(meangeNormsList.size(), 1);
        NormsData mgeNorms = (NormsData) meangeNormsList.get(0);
        ContentAreaScore score = (ContentAreaScore) mgeNorms.getContentAreaScores().get(READING);
        String mge = (String) score.getTargetScores().get(score.getTargetScores().size() - 1);
        assertEquals("129+", mge);
    }

    private void assertExtendedGEDData() {
        List xgedNormsList = (List) ParsedData.INSTANCE.getScoreList(ScoreType.EXTENDED_GRADE_EQUIVALENT);
        assertNotNull(xgedNormsList);
        assertEquals(1, xgedNormsList.size());

        NormsData xgedNorms = (NormsData) xgedNormsList.get(0);
        assertEquals(FALL_TRIMESTER, xgedNorms.getNormsGroup());
        assertEquals(14, xgedNorms.getContentAreaScores().size());
        ContentAreaScore readingScore = (ContentAreaScore) xgedNorms.getContentAreaScores().get(READING);
        assertEquals(0, readingScore.getSourceScoreStartIndex());
        assertEquals(1, readingScore.getSourceScoreEndIndex());
        final List targetScores = readingScore.getTargetScores();
        assertEquals(2, targetScores.size());

        final String scoreString1 = (String) targetScores.get(0);
        assertEquals(711, Integer.parseInt(scoreString1));
        final String scoreString2 = (String) targetScores.get(1);
        assertEquals(129, Integer.parseInt(scoreString2));

    }

    private void assertSEMData() {
        List scaleScoreNormsList = (List) ParsedData.INSTANCE.getScoreList(ScoreType.STANDARD_ERROR_OF_MEASUREMENT);
        assertEquals(2, scaleScoreNormsList.size());

        NormsData scaleScoreNorms = (NormsData) scaleScoreNormsList.get(1);
        assertEquals(FORM_A, scaleScoreNorms.getForm());
        assertEquals("11", scaleScoreNorms.getLevel());
        assertEquals(8, scaleScoreNorms.getContentAreaScores().size());

        final ContentAreaScore scienceContentAreaScore = (ContentAreaScore) scaleScoreNorms.getContentAreaScores().get("Science");
        assertEquals(0, scienceContentAreaScore.getSourceScoreStartIndex());
        assertEquals(20, scienceContentAreaScore.getSourceScoreEndIndex());
        List scienceScoreList = scienceContentAreaScore.getTargetScores();
        assertEquals(scienceContentAreaScore.getSourceScoreEndIndex() + 1, scienceScoreList.size());
        final String scoreString = (String) scienceScoreList.get(scienceScoreList.size() - 1);
        assertEquals(86, Integer.parseInt(scoreString));
    }

    private void assertScaleScoreData() {
        List scaleScoreNormsList = ParsedData.INSTANCE.getScoreList(ScoreType.SCALE_SCORE);
        assertEquals(2, scaleScoreNormsList.size());

        NormsData scaleScoreNorms = (NormsData) scaleScoreNormsList.get(1);
        assertEquals(FORM_A, scaleScoreNorms.getForm());
        assertEquals("11", scaleScoreNorms.getLevel());
        assertEquals(8, scaleScoreNorms.getContentAreaScores().size());

        ContentAreaScore mathContentAreaScore = (ContentAreaScore) scaleScoreNorms.getContentAreaScores().get("Mathematics");
        assertEquals(0, mathContentAreaScore.getSourceScoreStartIndex());
        assertEquals(47, mathContentAreaScore.getSourceScoreEndIndex());
        List mathScoreList = mathContentAreaScore.getTargetScores();
        assertEquals(mathContentAreaScore.getSourceScoreEndIndex() + 1, mathScoreList.size());
        final String scoreString = (String) mathScoreList.get(mathScoreList.size() - 1);
        assertEquals(680, Integer.parseInt(scoreString));
    }

    private void assertGEDData() {
        List gedList = ParsedData.INSTANCE.getScoreList(ScoreType.GRADE_EQUIVALENT);
        assertEquals(1, gedList.size());
        NormsData ged = (NormsData) gedList.get(0);
        assertEquals(FALL_TRIMESTER, ged.getNormsGroup());
        assertEquals(13, ged.getContentAreaScores().size());
        ContentAreaScore contentAreaScore = (ContentAreaScore) ged.getContentAreaScores().get(READING);
        int startScaleScore = contentAreaScore.getSourceScoreStartIndex();
        int endScaleScore = contentAreaScore.getSourceScoreEndIndex();
        assertEquals(355, startScaleScore);
        assertEquals(711, endScaleScore);
        assertEquals("129+", contentAreaScore.getTargetScores().get(contentAreaScore.getTargetScores().size() - 1));
    }
}
