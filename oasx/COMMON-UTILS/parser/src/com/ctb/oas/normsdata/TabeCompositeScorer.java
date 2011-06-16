package com.ctb.oas.normsdata;

import java.io.*;

/**
 * @author Sreenivas  Ananthakrishna
 */
public abstract class TabeCompositeScorer {
    protected final File file;
    protected final Writer writer;
    private final ScoreRecordWriter recordWriter = new DefaultScoreRecordWriter();
    protected final String contentArea;
    private final NormsData normsData = new NormsData();

    protected TabeCompositeScorer(File file, Writer writer) {
        this.file = file;
        this.writer = writer;
        final String TOTAL_MATHEMATICS = "Total Mathematics";
        final String TOTAL_BATTERY = "Total Battery";

        if (file.getName().indexOf("TM") != -1)
            contentArea = TOTAL_MATHEMATICS;
        else if (file.getName().indexOf("TB") != -1)
            contentArea = TOTAL_BATTERY;
        else
            throw new RuntimeException("unknown content area");

        initNormsData();
    }

    protected void initNormsData() {
        //normsData.setForm(ScoreRecord.TABE_FORM_VALUE);
    	normsData.setForm( ScorerUtil.getFormFromFileName(file.getName()));
        normsData.setFrameworkCode(ScoreRecord.TABE_FRAMEWORK_CODE);
        normsData.setNormsYear(ScoreRecord.NORMS_YEAR_VALUE);
        normsData.setSourceScoreType(ScoreType.SCALE_SCORE);
    }

    public void score() {
        try {
            final LineNumberReader reader = new LineNumberReader(new FileReader(file));
            String line;
            ScorerUtil.advanceReader(reader, 6);
            while ((line = reader.readLine()) != null) {
                handleScoreLine(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void writeScores(int sclStart, int sclEnd, int nce, ScoreType destScoreType) {
        normsData.setDestScoreType(destScoreType);
        for (int i = sclStart; i <= sclEnd; i++) {
            recordWriter.writeScoreRecord(writer, normsData, contentArea, new Integer(i), new Integer(nce));
        }
    }

    protected abstract void handleScoreLine(String line);


}
