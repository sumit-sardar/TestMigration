/*
 * Created on Aug 25, 2004
 *
 */
package com.ctb.oas.normsdata.parser;

import com.ctb.oas.normsdata.*;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

/**
 * @author arathore
 */
public class TotalMathReader extends GenericNormDataReader {
    private Map pairs;
    private NormsData data;
    private String contentArea;

    public TotalMathReader(File file) {
        this.inputFile = file;
        String inputFilename = file.getName();
        data = new NormsData();
        initNormsData(inputFilename);
        contentArea = ScorerUtil.getContentAreafromFileName(inputFilename);
        try {
            reader = new LineNumberReader(new FileReader(file));
            start();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void initNormsData(String inputFilename) {
        data.setFrameworkCode(ScoreRecord.TABE_FRAMEWORK_CODE);
        data.setProduct(ScorerUtil.getProductFromFileName(inputFilename));
        data.setForm(ScorerUtil.getFormFromFileName(inputFilename));
        data.setLevel(ScorerUtil.getLevelFromFileName(inputFilename));
        data.setNormsYear(ScoreRecord.NORMS_YEAR_VALUE);
        data.setSourceScoreType(ScoreType.SCALE_SCORE);
        data.setDestScoreType(ScoreType.GRADE_EQUIVALENT);
    }

    public void writeScoreRecord(Writer writer) {
        try {
            DefaultScoreRecordWriter recordWriter = new DefaultScoreRecordWriter();
            for (Iterator i = pairs.keySet().iterator(); i.hasNext();) {
                String key = (String) i.next();
                String value = (String) pairs.get(key);
                recordWriter.writeScoreRecord(writer, data, contentArea, Integer.valueOf(key.trim()), value);
            }
            writer.flush();
            pairs.clear(); //reset the map for the next file
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        ScorerUtil.advanceReader(reader, 7);
        TotalMathData data = getMoreData();
        while (data != null) {
            data = getMoreData();
        }
    }

    private TotalMathData getMoreData() {
        String line1 = line();
        String line2 = line();
        if (line1 == null || line2 == null)
            return null;
        TotalMathData result = new TotalMathData(line1, line2);
        pairs = result.pairs();
        return result;
    }

    public Map pairs() {
        return pairs;
    }

    public static void main(String[] args) {
        TotalMathReader reader = new TotalMathReader(new File("C:\\shared\\FinalTABE9\\TOTMA\\CB9TMEp2.txt"));
        for (Iterator i = reader.pairs().keySet().iterator(); i.hasNext();) {
            String ss = (String) i.next();
            System.out.println("SS - " + ss + " GE - " + reader.pairs().get(ss));
        }
        System.out.println("Total number of pairs - " + reader.pairs().keySet().size());
        try {
            reader.writeScoreRecord(new FileWriter("c:\\shared\\totma.sql"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}