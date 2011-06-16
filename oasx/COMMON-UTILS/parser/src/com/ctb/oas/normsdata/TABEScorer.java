package com.ctb.oas.normsdata;

import java.io.*;
import java.util.*;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class TABEScorer {
    private File file;
    protected String contentArea;
    private static final String NC = "NC";
    protected List scoreTypeList = new ArrayList();
    private static final String TABE_SCORE_REGEX = "^\\s*\"?\\s*(\\d+.?\\d*\\+?)+\\s*\"?\\s*,";
    private Writer writer;
    protected NormsData normsData = new NormsData();
    private Map scaleScoreMap = new HashMap();

    public TABEScorer(File file, Writer writer) {
        this.file = file;
        this.writer = writer;
        contentArea = ScorerUtil.getContentAreafromFileName(file.getName());
        normsData.setFrameworkCode(ScoreRecord.TABE_FRAMEWORK_CODE);
        normsData.setProduct(ScorerUtil.getProductFromFileName(file.getName()));
        normsData.setNormsYear(ScoreRecord.NORMS_YEAR_VALUE);
        normsData.setLevel(ScorerUtil.getLevelFromFileName(file.getName()));
        normsData.setForm(ScorerUtil.getFormFromFileName(file.getName()));
    }

    public void score() {

        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (containsScoreTypes(line)) {
                    buildScoreTypesList(line);
                }
                if (containsScore(line)) {
                    handleScoreLine(line);
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void handleScoreLine(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line, ",");
        Map scoresMap = new HashMap();
        Iterator iterator = scoreTypeList.iterator();
        while (tokenizer.hasMoreTokens() && iterator.hasNext()) {
            String token = tokenizer.nextToken();
            String score = stripQuotes(token);

            ScoreType scoreType = (ScoreType) iterator.next();
            scoresMap.put(scoreType, score);
        }
        writeScoreRecord(scoresMap);
    }

    private boolean findDuplicateDestScores(Map scoresMap) {
        Object currentScaleScore = scoresMap.get(ScoreType.SCALE_SCORE);
        Map destMap = (Map) scaleScoreMap.get(currentScaleScore);
        if (destMap == null) {
            scaleScoreMap.put(currentScaleScore, new HashMap());
            return false;
        }

        Iterator iterator = destMap.keySet().iterator();
        while (iterator.hasNext()) {
            Object scoreType = iterator.next();
            if (!scoreType.equals(ScoreType.NUMBER_CORRECT)) {
                Object currentValue = scoresMap.get(scoreType);
                Object cachedValue = destMap.get(scoreType);
                if (currentValue.equals(cachedValue)) {
                    System.out.println("Inconsistent dest scores for scale score " + currentScaleScore + "in file " + file.getName());
                }
            }

        }

        return true;
    }

    protected void writeScoreRecord(Map scoresMap) {
        ScoreRecordWriter writer = new DefaultScoreRecordWriter();
        if (findDuplicateDestScores(scoresMap)) {
            // exclude any SS->* score types
            writeScoreRecord(ScoreType.NUMBER_CORRECT, ScoreType.SCALE_SCORE, scoresMap, writer);
            writeScoreRecord(ScoreType.NUMBER_CORRECT, ScoreType.STANDARD_ERROR_OF_MEASUREMENT, scoresMap, writer);
            return;
        }

        Iterator iterator = scoresMap.keySet().iterator();

        while (iterator.hasNext()) {
            ScoreType scoreType = (ScoreType) iterator.next();
            if (scoreType.equals(ScoreType.SCALE_SCORE) || scoreType.equals(ScoreType.STANDARD_ERROR_OF_MEASUREMENT)) {
                writeScoreRecord(ScoreType.NUMBER_CORRECT, scoreType, scoresMap, writer);
            }
            else if (!scoreType.equals(ScoreType.NUMBER_CORRECT)) {
                writeScoreRecord(ScoreType.SCALE_SCORE, scoreType, scoresMap, writer);
            }
        }
    }

    private void writeScoreRecord(ScoreType source, ScoreType dest, Map scoresMap, ScoreRecordWriter writer) {
        Object sourceScore;
        Object destScore;
        normsData.setSourceScoreType(source);
        normsData.setDestScoreType(dest);
        sourceScore = scoresMap.get(source);
        destScore = scoresMap.get(dest);
        writer.writeScoreRecord(this.writer, normsData, contentArea, sourceScore, destScore);
    }

    protected boolean containsScore(String line) {
        return ScorerUtil.getMatchString(line, TABE_SCORE_REGEX) != null;
    }

    protected void buildScoreTypesList(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line, ",");
        while (tokenizer.hasMoreElements()) {
            String token = tokenizer.nextToken();
            String scoreTypeName = stripQuotes(token);
            final ScoreType scoreType = ScoreType.getScoreTypeForTABE(scoreTypeName);
            if (scoreType != null)
                scoreTypeList.add(scoreType);
        }
        if (scoreTypeList.size() < 10) {
            System.out.println("score types might be incorrect in " + file.getName() + "!");
        }
    }

    private String stripQuotes(String token) {
        if (token == null || !token.startsWith("\""))
            return token;

        return token.substring(1, token.length() - 1);
    }

    protected boolean containsScoreTypes(String line) {
        return line.indexOf(NC) != -1;
    }
}
