/*
 * Created on Sep 1, 2004
 *
 */
package com.ctb.oas.normsdata.parser;

import com.ctb.oas.normsdata.*;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author arathore
 */
public class PValuesReader extends GenericNormDataReader {
    private Map objectiveMap;
    private static final String FALL = "FALL";
    private static final String SPRING = "SPRING";
    private static final String WINTER = "WINTER";

    public PValuesReader(File inputFile) throws FileNotFoundException {
        reader = new LineNumberReader(new FileReader(inputFile));
        objectiveMap = new HashMap();
        ScorerUtil.advanceReader(reader, 1);
        start();
    }

    private void start() {
        String line = line();
        while (line != null) {
            String[] tokens = line.split(",");
            PValueDetails details = new PValueDetails(tokens);
            accumalateInObjectivesMap(details);
            line = line();
        }
    }

    private void accumalateInObjectivesMap(PValueDetails details) {
        Map gradeMap = (Map) objectiveMap.get(details.getObjective());
        if (gradeMap == null) {
            gradeMap = new HashMap();
            objectiveMap.put(details.getObjective(), gradeMap);
        }
        accumalateInGradeMap(gradeMap, details);
    }

    private void accumalateInGradeMap(Map gradeMap, PValueDetails newDetails) {
        PValueDetails oldDetails = (PValueDetails) gradeMap.get(newDetails.getGrade());
        if (oldDetails == null) {
            gradeMap.put(newDetails.getGrade(), newDetails);
            return;
        }
        oldDetails.updateAverages(newDetails);
    }

    private void printToConsole(PValueDetails details) {
        System.out.println("Found details for objective " + details.getObjective() + " and " + details.getGrade());
    }

    private Map getObjectiveMap() {
        return objectiveMap;
    }

    private Map getGradeMap(String objective) {
        return (Map) objectiveMap.get(objective);
    }

    public static void main(String[] args) throws IOException {
        File inputFile = ScorerUtil.getInputFileFromArgs(args);
        File outFile = ScorerUtil.getOutputFileFromArgs(args);
        Writer writer = new FileWriter(outFile);
        PValuesReader reader = new PValuesReader(inputFile);
        for (Iterator i = reader.getObjectiveMap().keySet().iterator(); i.hasNext();) {
            String objective = (String) i.next();
//			System.out.println("**** For objective " + objective);
            Map gradeMap = reader.getGradeMap(objective);
            for (Iterator k = gradeMap.keySet().iterator(); k.hasNext();) {
                String grade = (String) k.next();
                PValueDetails details = (PValueDetails) gradeMap.get(grade);
                writScoreRecord(writer, details, FALL);
                writScoreRecord(writer, details, SPRING);
                writScoreRecord(writer, details, WINTER);
//                System.out.println(">>> Grade " + grade + "     " + details);
            }
        }
        writer.flush();
        writer.close();
    }

    private static void writScoreRecord(Writer writer, PValueDetails details, String normsGroup) {
        NormsData data = getNormsData(details, normsGroup);
        ScoreRecordWriter recordWriter = ScoreRecordWriterFactory.getScoreRecordWriter(data.getDestScoreType());

        float pValue = getPValueString(details, normsGroup);
        recordWriter.writeScoreRecord(writer, data, details.getContentArea(), new Integer(0), new Float(pValue));
    }

    private static float getPValueString(PValueDetails details, String normsGroup) {
        if (normsGroup.equals(FALL))
            return details.getFallPValue();

        if (normsGroup.equals(SPRING))
            return details.getSpringPValue();

        if (normsGroup.equals(WINTER))
            return details.getWinterPValue();

        return -1;
    }

    private static NormsData getNormsData(PValueDetails details, String normsGroup) {
        NormsData data = new NormsData();
        data.setForm(details.getForm());
        data.setFrameworkCode(ScoreRecord.TERRANOVA_FRAMEWORK_CODE);
        data.setGrade(details.getGrade());
        data.setLevel(details.getLevel());
        data.setNormsGroup(normsGroup);
        data.setNormsYear(ScoreRecord.NORMS_YEAR_VALUE);
        data.setObjective(details.getObjective());
        data.setProduct(ScoreRecord.TERRANOVA_PRODUCT_NAME);
        data.setSourceScoreType(ScoreType.NUMBER_CORRECT);
        data.setDestScoreType(ScoreType.PVALUE);
        return data;
    }
}
