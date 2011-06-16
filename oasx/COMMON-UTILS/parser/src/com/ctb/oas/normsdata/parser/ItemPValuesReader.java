package com.ctb.oas.normsdata.parser;

import com.ctb.oas.normsdata.ScorerUtil;

import java.io.*;

/**
 * @author Sreenivas  Ananthakrishna
 */
public class ItemPValuesReader {
    private final File file;
    private final Writer writer;

    public ItemPValuesReader(final File file, final Writer writer) {
        this.file = file;
        this.writer = writer;
    }

    public void read() {
        try {
            final LineNumberReader reader = new LineNumberReader(new FileReader(file));
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                final String tokens [] = line.split(",");
                final String form = tokens[0].trim();
                final String level = tokens[1].trim();
                final String grade = tokens[2].trim();
                final String itemDisplayName = tokens[4].trim();
                final double pValueFall = Double.parseDouble(tokens[8].trim());
                final double pValueWinter = Double.parseDouble(tokens[9].trim());
                final double pValueSpring = Double.parseDouble(tokens[10].trim());
                writer.write(getDataRow(form, level, grade, itemDisplayName, pValueFall, "FALL"));
                writer.write(getDataRow(form, level, grade, itemDisplayName, pValueSpring, "SPRING"));
                writer.write(getDataRow(form, level, grade, itemDisplayName, pValueWinter, "WINTER"));
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getDataRow(String form, String level, String grade, String itemDisplayName, double pValue, String normGroup) {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(form);
        buffer.append(",");
        buffer.append(level);
        buffer.append(",");
        buffer.append(grade);
        buffer.append(",");
        buffer.append(itemDisplayName);
        buffer.append(",");
        buffer.append(pValue);
        buffer.append(",");
        buffer.append(normGroup);
        buffer.append("\n");
        return buffer.toString();
    }

    public static void main(String args[]) throws IOException {
        File file = ScorerUtil.getInputFileFromArgs(args);
        Writer writer = new FileWriter(ScorerUtil.getOutputFileFromArgs(args));
        ItemPValuesReader reader = new ItemPValuesReader(file, writer);
        reader.read();
        writer.flush();
        writer.close();
    }
}
