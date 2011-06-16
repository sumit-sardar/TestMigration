package com.ctb.oas.normsdata;

import java.io.*;
import java.util.StringTokenizer;

/**
 * @author Sreenivas  Ananthakrishna
 */
public class TABESSToGEScorer {
    private File file;
    private static final String SCORE_DELIMITER = ",";
    private Writer writer;

    public TABESSToGEScorer(Writer writer, File file) {
        this.file = file;
        this.writer = writer;
    }

    public void handleScoreLine(String scoreLine) {
        NormsData normsData = getNormsData();

        StringTokenizer tokenizer = new StringTokenizer(scoreLine, SCORE_DELIMITER);
        String contentAreaCode = tokenizer.nextToken();
        Integer scaleScore = new Integer(tokenizer.nextToken());
        String geScore = tokenizer.nextToken();
        String contentAreaName = ContentAreaLookup.getTABEContentArea(contentAreaCode);
        ScoreRecordWriter scoreRecordWriter = ScoreRecordWriterFactory.getScoreRecordWriter(ScoreType.GRADE_EQUIVALENT);
        scoreRecordWriter.writeScoreRecord(writer, normsData, contentAreaName, scaleScore, geScore);
    }

    private NormsData getNormsData() {
        NormsData normsData = new NormsData();
        //normsData.setForm(ScoreRecord.TABE_FORM_VALUE);
        normsData.setForm(ScorerUtil.getFormFromFileName(file.getName()));
        normsData.setFrameworkCode(ScoreRecord.TABE_FRAMEWORK_CODE);
        normsData.setNormsYear(ScoreRecord.NORMS_YEAR_VALUE);
        normsData.setSourceScoreType(ScoreType.SCALE_SCORE);
        normsData.setDestScoreType(ScoreType.GRADE_EQUIVALENT);
        return normsData;
    }

    public void score() {
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(file));
            reader.readLine(); // skip the header line
            String line;
            while ((line = reader.readLine()) != null) {
                handleScoreLine(line);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Cannot read from input file ");
        }
    }
}
