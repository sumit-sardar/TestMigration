package com.ctb.oas.normsdata;

import java.io.File;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sreenivas  Ananthakrishna
 */
public class TabeCompositeNPStanineScorer extends TabeCompositeScorer {
    public TabeCompositeNPStanineScorer(File file, Writer writer) {
        super(file, writer);
    }

    protected void handleScoreLine(final String line) {
        Pattern pattern = Pattern.compile("(\\d+\\s*-\\s*\\d+)|\\d+");
        Matcher matcher = pattern.matcher(line);
        final int stanine = Integer.parseInt(getMatch(matcher));
        final int np = Integer.parseInt(getMatch(matcher));
        String sclAdult = getMatch(matcher);
        String sclJuv = getMatch(matcher);


        final int sclJuvStart = getRangeStart(sclJuv);
        final int sclJuvEnd = getRangeEnd(sclJuv);
        final int sclAdultStart = getRangeStart(sclAdult);
        final int sclAdultEnd = getRangeEnd(sclAdult);

        writeScores(sclJuvStart, sclJuvEnd, stanine, ScoreType.ABE_J_S);
        writeScores(sclJuvStart, sclJuvEnd, np, ScoreType.ABE_J_P);
        writeScores(sclAdultStart, sclAdultEnd, stanine, ScoreType.ABE_ALL_S);
        writeScores(sclAdultStart, sclAdultEnd, np, ScoreType.ABE_ALL_P);
    }

    private String getMatch(Matcher matcher) {
        if (!matcher.find())
            return null;

        return matcher.group().trim();
    }

    private int getRangeEnd(String string) {
        if (containsRange(string))
            return Integer.parseInt(string.split("-")[1].trim());
        else
            return Integer.parseInt(string.trim());
    }

    private int getRangeStart(String string) {
        if (containsRange(string))
            return Integer.parseInt(string.split("-")[0].trim());
        else
            return Integer.parseInt(string.trim());
    }

    private boolean containsRange(String sclJuv) {
        return sclJuv.indexOf('-') != -1;
    }
}