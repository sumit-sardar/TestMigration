package com.ctb.common.jessica;


import java.io.*;

import com.ctb.common.tools.*;


/**
 * print a CSV file representation of a 2-dimensional int array,
 * including labels and totals
 */
public class ArraySummaryPrinter {

    private String title;
    private String[] xLabels;
    private String[] yLabels;
    private int[][] array;

    public ArraySummaryPrinter(String title, String[] xLabels, String[] yLabels, int[][] array) {
        this.title = title;
        this.xLabels = xLabels;
        this.yLabels = yLabels;
        this.array = array;

        if (array.length != xLabels.length) {
            throw new SystemException("the size of xLabels (" + xLabels.length
                    + ") does not equal the length of the array ("
                    + array.length + ")");
        }
        if (array[0].length != yLabels.length) {
            throw new SystemException("the size of yLabels (" + yLabels.length
                    + ") does not equal the width of the array ("
                    + array[0].length + ")");
        }
    }

    public void print(OutputStream stream) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));

        // title
        writer.write(title + "\n");

        // x labels
        for (int i = 0; i < xLabels.length; i++) {
            writer.write("," + xLabels[i]);
        }
        writer.write("\n");

        int arrayTotal = 0;

        // lines
        for (int y = 0; y < yLabels.length; y++) {
            int rowTotal = 0;

            writer.write(yLabels[y]);
            for (int x = 0; x < xLabels.length; x++) {
                writer.write("," + array[x][y]);
                rowTotal += array[x][y];
            }
            writer.write("," + rowTotal + "\n");
            arrayTotal += rowTotal;
        }

        // column totals
        for (int x = 0; x < xLabels.length; x++) {
            int columnTotal = 0;

            for (int y = 0; y < yLabels.length; y++) {
                columnTotal += array[x][y];
            }
            writer.write("," + columnTotal);
        }
        writer.write("," + arrayTotal + "\n\n");
        writer.flush();
        writer.close();
    }
}
