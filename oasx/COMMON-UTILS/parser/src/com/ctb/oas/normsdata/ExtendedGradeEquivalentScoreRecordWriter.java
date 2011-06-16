package com.ctb.oas.normsdata;

import java.io.Writer;
import java.util.List;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class ExtendedGradeEquivalentScoreRecordWriter extends DefaultScoreRecordWriter {

    public void writeScoreRecord(Writer writer, NormsData data, ContentAreaScore contentAreaScore) {
        List targetScores = contentAreaScore.getTargetScores();
        String sourceScoreString = (String) targetScores.get(0);
        Integer sourceScore = new Integer(sourceScoreString);
        super.writeScoreRecord(writer, data, contentAreaScore.getContentAreaString(), sourceScore, targetScores.get(1));
    }
}
