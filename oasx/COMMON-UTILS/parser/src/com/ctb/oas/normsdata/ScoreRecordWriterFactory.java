package com.ctb.oas.normsdata;


/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class ScoreRecordWriterFactory {
    private static DefaultScoreRecordWriter DEFAULT_INSTANCE = new DefaultScoreRecordWriter();
    private static ExtendedGradeEquivalentScoreRecordWriter XGE_INSTANCE = new ExtendedGradeEquivalentScoreRecordWriter();

    public static ScoreRecordWriter getScoreRecordWriter(ScoreType destScoreType) {
        if (destScoreType.equals(ScoreType.EXTENDED_GRADE_EQUIVALENT))
            return XGE_INSTANCE;
        else
            return DEFAULT_INSTANCE;
    }
}
