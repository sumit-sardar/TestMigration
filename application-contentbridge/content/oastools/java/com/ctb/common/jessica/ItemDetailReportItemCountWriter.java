package com.ctb.common.jessica;


import java.io.*;
import java.util.*;

import org.apache.commons.lang.*;

import com.ctb.common.tools.*;
import com.ctb.xmlProcessing.item.*;


public class ItemDetailReportItemCountWriter {

    private File detailReport;

    public ItemDetailReportItemCountWriter(File detailReport) {
        this.detailReport = detailReport;
    }

    public File writeReport() throws IOException {
        String countsReportName = StringUtils.replace(detailReport.getAbsolutePath(),
                "_Detail_", "_Counts_");
        File countsReport = new File(countsReportName);

        // parse & sort
        List itemInfo = new ItemDetailReportItemCountsParser(detailReport).parseReport();
        SortedMap contentAreas = setupContentAreas(itemInfo);
        SortedMap grades = setupGrades(itemInfo);

        String[] xLabels = (String[]) contentAreas.keySet().toArray(new String[contentAreas.size()]);
        String[] yLabels = (String[]) grades.keySet().toArray(new String[grades.size()]);

        // print all item counts
        int[][] counts = setupCountsArray(contentAreas, grades, itemInfo, null);
        ArraySummaryPrinter printer = new ArraySummaryPrinter("ALL ITEMS",
                xLabels, yLabels, counts);

        printer.print(new FileOutputStream(countsReport));

        // print CR counts
        counts = setupCountsArray(contentAreas, grades, itemInfo,
                Item.CONSTRUCTED_RESPONSE);
        printer = new ArraySummaryPrinter("CR ITEMS", xLabels, yLabels, counts);
        printer.print(new FileOutputStream(countsReport.getAbsolutePath(), true));

        // print SR counts
        counts = setupCountsArray(contentAreas, grades, itemInfo,
                Item.SELECTED_RESPONSE);
        printer = new ArraySummaryPrinter("SR ITEMS", xLabels, yLabels, counts);
        printer.print(new FileOutputStream(countsReport.getAbsolutePath(), true));

        return countsReport;
    }

    /**
     * populate an array (contentArea x grade) with item counts
     */
    private int[][] setupCountsArray(SortedMap contentAreas, SortedMap grades, List itemInfo, String itemTypeFilter) {
        int[][] counts = new int[contentAreas.size()][grades.size()];

        for (Iterator iterator = itemInfo.iterator(); iterator.hasNext();) {
            ItemDetailReportItemCountsParser.ItemInfo info = (ItemDetailReportItemCountsParser.ItemInfo) iterator.next();

            if (itemTypeFilter != null && !itemTypeFilter.equals(info.type)) {
                continue;
            }
            int contentAreaIndex = ((Integer) contentAreas.get(info.contentArea)).intValue();
            int gradeIndex = ((Integer) grades.get(info.grade)).intValue();

            counts[contentAreaIndex][gradeIndex]++;
        }
        return counts;
    }

    /**
     * @return a SortedMap of the item content areas contained in the itemInfo list.
     * the keys to the map are the content area text and the values are the
     * sorted position of that content area, which is useful when assembling the counts array.
     */
    private SortedMap setupContentAreas(List itemInfo) {
        SortedMap map = new TreeMap();

        for (Iterator iterator = itemInfo.iterator(); iterator.hasNext();) {
            ItemDetailReportItemCountsParser.ItemInfo info = (ItemDetailReportItemCountsParser.ItemInfo) iterator.next();

            map.put(info.contentArea, "place holder");
        }
        int index = 0;

        for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
            Object key = iterator.next();

            map.put(key, new Integer(index++));
        }
        return map;
    }

    /**
     * @return a SortedMap of the item grades contained in the itemInfo list.
     * the keys to the map are the grade text and the values are the
     * sorted position of that grade, which is useful when assembling the counts array.
     */
    private SortedMap setupGrades(List itemInfo) {
        SortedMap map = new TreeMap(new GradeComparator());

        for (Iterator iterator = itemInfo.iterator(); iterator.hasNext();) {
            ItemDetailReportItemCountsParser.ItemInfo info = (ItemDetailReportItemCountsParser.ItemInfo) iterator.next();

            try {
                map.put(info.grade, "place holder");
            } catch (SystemException sysEx) {
                throw new SystemException("error parsing grade for item "
                        + info.id);
            }
        }
        int index = 0;

        for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
            Object key = iterator.next();

            map.put(key, new Integer(index++));
        }
        return map;
    }

    public static void main(String[] args) {
        ItemDetailReportItemCountWriter writer = new ItemDetailReportItemCountWriter(new File("CTB_Item_Detail_davidrice_09172003.csv"));

        try {
            writer.writeReport();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }
}


/**
 * the detail report uses strings such as 'Grade 1,' 'Grade 10,'
 * and 'Grade 10-11' so we need a custom comparator.
 */
class GradeComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        String grade1 = (String) o1;
        String grade2 = (String) o2;

        return extractGrade(grade1).compareTo(extractGrade(grade2));
    }

    private Integer extractGrade(String grade) {
        StringBuffer numbers = new StringBuffer();
        boolean foundNumbers = false;

        for (int i = 0; i < grade.length(); i++) {
            String character = grade.substring(i, i + 1);
            boolean isNumber = StringUtils.isNumeric(character);

            if (!foundNumbers && isNumber) {
                foundNumbers = true;
            }
            if (foundNumbers && isNumber) {
                numbers.append(character);
            }
            if (foundNumbers && !isNumber) {
                break;
            }
        }
        if (numbers.length() < 1) {
            throw new SystemException("unable to parse grade text:  " + grade);
        }
        return new Integer(numbers.toString());
    }

}
