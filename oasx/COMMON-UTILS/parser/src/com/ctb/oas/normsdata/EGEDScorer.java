package com.ctb.oas.normsdata;

import java.io.*;

/**
 * @author Sreenivas  Ananthakrishna
 */
public class EGEDScorer {
    private File file;
    private Writer writer;
    private String contentArea;
    private static final char RANGE_DELIMITER = '-';
    private static final char SCORE_DELIMITER = ',';
    private NormsData normsData = new NormsData();

    public EGEDScorer(Writer writer, File file) {
        this.file = file;
        this.writer = writer;
        final String fileName = trimExtension(file.getName());
        this.contentArea = ContentAreaLookup.getContentArea(fileName.substring(0, 2));
        //final String scoreTypeString = fileName.substring(3, fileName.length());
        final String scoreTypeString = ScorerUtil.getScoreTypeFromFileName(file.getName());;
        normsData.setSourceScoreType(ScoreType.SCALE_SCORE);
        normsData.setDestScoreType(ScoreType.getScoreTypeForTABE(scoreTypeString));
        //normsData.setForm(fileName.substring(2, 3));
        normsData.setForm(ScorerUtil.getFormFromFileName(file.getName()));
        normsData.setFrameworkCode(ScoreRecord.TABE_FRAMEWORK_CODE);
        normsData.setNormsYear(ScoreRecord.NORMS_YEAR_VALUE);
    }

    private String trimExtension(String name) {
        return name.substring(0, name.indexOf('.'));
    }

    public void handleScoreLine(String line) {
        final ContentAreaScore score = new ContentAreaScore(contentArea);

        final String scaleScoreString = line.substring(0, line.indexOf(SCORE_DELIMITER)).trim();
        final String destScoreString = line.substring(line.indexOf(SCORE_DELIMITER) + 1, line.length()).trim();

        if (scaleScoreString.indexOf(RANGE_DELIMITER) != -1) {
            final String sourceScoreStartString = scaleScoreString.substring(0, scaleScoreString.indexOf(RANGE_DELIMITER));
            score.setSourceScoreStart(Integer.parseInt(sourceScoreStartString));
            final String sourceScoreEndString = scaleScoreString.substring(scaleScoreString.indexOf(RANGE_DELIMITER) + 1, scaleScoreString.length());
            score.setSourceScoreEnd(Integer.parseInt(sourceScoreEndString));
        }
        else {
            final String sourceScoreStartString = scaleScoreString.substring(0, scaleScoreString.length());
            score.setSourceScoreStart(Integer.parseInt(sourceScoreStartString));
            score.setSourceScoreEnd(Integer.parseInt(sourceScoreStartString));
        }

        for (int i = score.getSourceScoreStartIndex(); i <= score.getSourceScoreEndIndex(); i++)
            score.getTargetScores().add(destScoreString);

        ScoreRecordWriter recordWriter = ScoreRecordWriterFactory.getScoreRecordWriter(normsData.getDestScoreType());
        recordWriter.writeScoreRecord(writer, normsData, score);
    }

    public void score() {
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(this.file));
            String line;
            while ((line = reader.readLine()) != null) {
                handleScoreLine(line);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("cannot read from input file", e);
        }

    }
}
