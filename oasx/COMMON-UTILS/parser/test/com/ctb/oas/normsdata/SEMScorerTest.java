package com.ctb.oas.normsdata;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.List;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class SEMScorerTest extends NormsDataTestCase {
    private final static String SEM_SCORE = "1 NUMBER CORRECT - STANDARD ERROR OF MEASUREMENT TN CB    FM A  Level 12\n" +
            " 2 072   001009001\n" +
            " 5 0033001\n" +
            " 6 135135135135135135135088042027020016014013012011011010010010010010011011012012\n" +
            " 6 013014015017020027059\n" +
            " 5 0021002\n" +
            " 6 100100100100059034027024023022020019019019019019019020024039054\n" +
            "0\n" +
            "1 NUMBER CORRECT - STANDARD ERROR OF MEASUREMENT TN CB    FM B  Level 13\n" +
            " 2 072   001009001\n" +
            " 5 0043001\n" +
            " 6 138138138138138138138138088052035026022019017015014013012011011011010010010010\n" +
            " 6 010010010010010010010011011012013014015018022033064";

    public void testGetNormsData() {
        StringReader reader = new StringReader(SEM_SCORE);
        loader.load(new LineNumberReader(reader));
        List SEMList = (List) ParsedData.INSTANCE.getScoreList(ScoreType.STANDARD_ERROR_OF_MEASUREMENT);
        assertEquals(2, SEMList.size());
        NormsData sem1 = (NormsData) SEMList.get(0);
        NormsData sem2 = (NormsData) SEMList.get(1);
        assertEquals("A", sem1.getForm());
        assertEquals("B", sem2.getForm());
        final ContentAreaScore score = (ContentAreaScore) sem1.getContentAreaScores().get("Reading");
        final List readingScoreList = score.getTargetScores();
        assertEquals(33, readingScoreList.size());
        final String scoreString = (String) readingScoreList.get(readingScoreList.size() - 1);
        assertEquals(59, Integer.parseInt(scoreString));
        final ContentAreaScore score2 = (ContentAreaScore) sem2.getContentAreaScores().get("Reading");
        assertEquals(43, score2.getTargetScores().size());
        final String scoreString2 = (String) score2.getTargetScores().get(score2.getTargetScores().size() - 1);
        assertEquals(64, Integer.parseInt(scoreString2));
    }
}
