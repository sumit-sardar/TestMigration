package com.ctb.oas.normsdata;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Writer;

import com.ctb.oas.normsdata.parser.TotalMathReader;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class TABELoader {
    private File dir;
    private static final String TOTAL_MATHEMATICS = "Total Mathematics";
    private static final String EGED = "EGED";

    public TABELoader(File dir) {
        this.dir = dir;
    }


    public ParsedData load(Writer writer) {
    	
    	final File[] files = dir.listFiles(new CSVFileNameFilter());
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            String contentArea = ScorerUtil.getContentAreafromFileName(file.getName());
            if (contentArea != null && !contentArea.equalsIgnoreCase(TOTAL_MATHEMATICS)) {
                TABEScorer scorer = new TABEScorer(file, writer);
                scorer.score();
            	//System.out.println("Ignoring " + file.getName());
            }

            else if (contentArea != null && contentArea.equalsIgnoreCase(TOTAL_MATHEMATICS)) {
                TotalMathReader reader = new TotalMathReader(file);
                reader.writeScoreRecord(writer);
            }

            else if (file.getName().indexOf(EGED) != -1) {
                EGEDScorer scorer = new EGEDScorer(writer, file);
                scorer.score();
            }
            
            else if (file.getName().matches("TABE\\d+_SS_GE\\..*") ) {
        		TABESSToGEScorer scorer = new TABESSToGEScorer(writer, file);
                scorer.score();
            }

            else if ( file.getName().matches("T[M|B]\\d+SS_NCE.*?\\..*") ) {
            //else if (file.getName().indexOf("TM910SS_NCE") != -1 || file.getName().indexOf("TB910SS_NCE") != -1) {
                TabeCompositeScorer scorer = new TabeCompositeNCEScorer(file, writer);
                scorer.score();
            }

            else if ( file.getName().matches("T[B|M]\\d+SS_NPR.*?\\..*") ) {
            //else if (file.getName().indexOf("TM910SS_NPR") != -1 || file.getName().indexOf("TB910SS_NPR") != -1) {
                TabeCompositeNPStanineScorer scorer = new TabeCompositeNPStanineScorer(file, writer);
                scorer.score();
            }
            else
                throw new RuntimeException("unknown file type - " + file.getName());
        }

        return ParsedData.INSTANCE;
    }

    public static class CSVFileNameFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            if (name.endsWith(".csv") || name.endsWith(".CSV") || name.endsWith(".txt"))
                return true;

            return false;
        }
    }

    public static void main(String args[]) {
        ScorerUtil.validateArgs(args);
        File dir = ScorerUtil.getInputFileFromArgs(args);
        File outputFile = ScorerUtil.getOutputFileFromArgs(args);
        TABELoader loader = new TABELoader(dir);
        try {
            FileWriter writer = new FileWriter(outputFile);
            loader.load(writer);
            writer.flush();
            writer.close();
        }
        catch (IOException e) {
            throw new RuntimeException("Cannot open output file for writing", e);
        }
    }

}
