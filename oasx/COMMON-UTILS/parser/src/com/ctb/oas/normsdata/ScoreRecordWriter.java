package com.ctb.oas.normsdata;

import java.io.Writer;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public interface ScoreRecordWriter {
    public void writeScoreRecord(Writer writer, NormsData normsData);

    public void writeScoreRecord(Writer writer, NormsData normsData, ContentAreaScore contentAreaScore);

    public void writeScoreRecord(Writer writer, NormsData data, String contentArea, Object sourceScore, Object targetScore);
}
