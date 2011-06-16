package com.ctb.oas.normsdata;

import java.io.File;
import java.io.Writer;

/**
 * @author Sreenivas  Ananthakrishna
 */
public class TabeCompositeNCEScorer extends TabeCompositeScorer {

    public TabeCompositeNCEScorer(File file, Writer writer) {
        super(file, writer);
    }

    protected void handleScoreLine(String line) {
        final String[] tokens = line.split("\\s+");
        final int nce = Integer.parseInt(tokens[1]);
        final int sclAdultStart = Integer.parseInt(tokens[2]);
        final int sclAdultEnd = Integer.parseInt(tokens[4]);
        final int sclJuvStart = Integer.parseInt(tokens[5]);
        final int sclJuvEnd = Integer.parseInt(tokens[7]);
        writeScores(sclJuvStart, sclJuvEnd, nce, ScoreType.ABE_J_NCE);
        writeScores(sclAdultStart, sclAdultEnd, nce, ScoreType.ABE_ALL_NCE);
    }
}
