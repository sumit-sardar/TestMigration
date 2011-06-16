package com.ctb.oas.normsdata;

import java.io.*;
import java.util.List;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class NCEScorerTest extends NormsDataTestCase {
    private static final String VALID_NCE_SCORE_FILE = "test/data/nce.txt";


    private static final String INVALID_NCE_SCORE = "1 SS - NCE FOR TN      LEVELS 10 - 22           GRADE-KG,QUARTERMONTH-05";

    public void testForValidScore() throws FileNotFoundException {
        Reader reader = new FileReader(new File(VALID_NCE_SCORE_FILE));
        loader.load(new LineNumberReader(reader));
        List normsdatalist = (List) ParsedData.INSTANCE.getScoreList(ScoreType.NORMAL_CURVE_EQUIVALENT);
        assertEquals(1, normsdatalist.size());
        NormsData data = (NormsData) normsdatalist.get(0);
        ContentAreaScore score = (ContentAreaScore) data.getContentAreaScores().get("Reading");
        assertEquals(1000, score.getTargetScores().size());
        final String targetScoreString = (String) score.getTargetScores().get(0);
        assertEquals(1, Integer.parseInt(targetScoreString));
        final String targetScoreString2 = (String) score.getTargetScores().get(score.getTargetScores().size() - 1);
        assertEquals(99, Integer.parseInt(targetScoreString2));
    }

    public void testForInvalidScore() {
        loader.load(new LineNumberReader(new StringReader(INVALID_NCE_SCORE)));
        List normsdatalist = (List) ParsedData.INSTANCE.getScoreList(ScoreType.NORMAL_CURVE_EQUIVALENT);
        assertEquals(0, normsdatalist.size());
    }
}
