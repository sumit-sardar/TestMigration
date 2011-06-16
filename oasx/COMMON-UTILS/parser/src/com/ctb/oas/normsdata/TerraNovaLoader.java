package com.ctb.oas.normsdata;

import java.io.*;
import java.util.List;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class TerraNovaLoader {
    private File dataFile;
    protected ParsedData parsedData = ParsedData.INSTANCE;

    public TerraNovaLoader(File dataFile) {
        this.dataFile = dataFile;
    }

    public TerraNovaLoader() {
    };

    public ParsedData load() {
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(dataFile));
            return load(reader);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ParsedData load(LineNumberReader reader) {
        try {
            String line;
            TerraNovaScorer terraNovaScorer = null;
            TableType type = null;
            while ((line = reader.readLine()) != null) {
                if (ScorerUtil.containsHeaderCode(line) && (type = TableType.getTableType(line)) != null) {
                    if (terraNovaScorer != null) {
                        addScaleScore(terraNovaScorer.getNormsData());
                    }

                    terraNovaScorer = ScorerFactory.getScorer(type.getDestScoreType());
                    if (terraNovaScorer == null) {
                        line = reader.readLine();
                        continue;
                    }
                    else
                        terraNovaScorer.handleHeader(line);
                }

                else if (ScorerUtil.containsInstructionCode(line)) {
                    terraNovaScorer.handleInstruction(line);
                }

                else if (ScorerUtil.containsScoreCode(line)) {
                    terraNovaScorer.handleScoreLine(line);
                }
            }
            addScaleScore(terraNovaScorer.getNormsData());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return parsedData;
    }


    public static void main(String args[]) {
        ScorerUtil.validateArgs(args);
        File inputFile = ScorerUtil.getInputFileFromArgs(args);
        File outFile = ScorerUtil.getOutputFileFromArgs(args);

        TerraNovaLoader loader = new TerraNovaLoader(inputFile);
        loader.load();
        loader.write(outFile);
    }


    protected void addScaleScore(NormsData data) {
        if (data == null)
            return;

        List dataArray = (List) parsedData.getScoreList(data.getDestScoreType());
        dataArray.add(data);
    }

    public void write(File outFile) {
        ScoreType[] scoreTypes = ScoreType.TERRANOVA_TYPES;
        FileWriter writer = null;
        try {
            writer = new FileWriter(outFile);
        }
        catch (IOException e) {
            throw new RuntimeException("cannot writer for outfile", e);
        }
        for (int i = 0; i < scoreTypes.length; i++) {
            ScoreType scoreType = scoreTypes[i];
            List normsList = ParsedData.INSTANCE.getScoreList(scoreType);
            if (!normsList.isEmpty())
                write(scoreType, normsList, writer);
        }

        try {
            writer.flush();
            writer.close();
        }
        catch (IOException e) {
            throw new RuntimeException("cannot flush or close file writer ", e);
        }
    }

    public void write(ScoreType scoreType, List normsDataList, Writer writer) {
        ScoreRecordWriter recordWriter = ScoreRecordWriterFactory.getScoreRecordWriter(scoreType);
        for (int i = 0; i < normsDataList.size(); i++) {
            NormsData normsData = (NormsData) normsDataList.get(i);
            recordWriter.writeScoreRecord(writer, normsData);
        }
    }

}
