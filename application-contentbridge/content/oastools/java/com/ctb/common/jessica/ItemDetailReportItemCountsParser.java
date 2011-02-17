package com.ctb.common.jessica;


import java.io.*;
import java.util.*;

import org.apache.commons.lang.*;

import com.ctb.common.tools.*;


/**
 * parse the "jessica report" in order to provide
 * various item count rollups.
 */
public class ItemDetailReportItemCountsParser {

    private static final String DETAIL_HEADER_GRADE = "Grade";
    private static final String DETAIL_HEADER_CONTENT_AREA = "Content Area";
    private static final String DETAIL_HEADER_ITEM_TYPE = "Item Type";

    private File detailReport;
    private int itemTypeIndex = -1;
    private int contentAreaIndex = -1;
    private int gradeIndex = -1;

    /**
     * Constructor
     * @param detailReport the "jessica detail report"
     */
    public ItemDetailReportItemCountsParser(File detailReport) {
        this.detailReport = detailReport;
    }

    /**
     * @return List of ItemInfo for each line of the report
     */
    public List parseReport() {
        try {
            List itemInfo = new ArrayList();
            BufferedReader reader = new BufferedReader(new FileReader(detailReport));
            String header = reader.readLine();

            parseHeader(header);
            String line = null;

            while ((line = reader.readLine()) != null) {
                itemInfo.add(parseLineAsItemInfo(line));
            }
            return itemInfo;
        } catch (IOException ioEx) {
            throw new SystemException("unable to parse report", ioEx);
        }
    }

    /**
     * parse header line to determine which columns hold grade, content area, and item type
     */
    private void parseHeader(String header) {
        String[] parts = parseLine(header);

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals(DETAIL_HEADER_CONTENT_AREA)) {
                contentAreaIndex = i;
            } else if (parts[i].equals(DETAIL_HEADER_GRADE)) {
                gradeIndex = i;
            } else if (parts[i].equals(DETAIL_HEADER_ITEM_TYPE)) {
                itemTypeIndex = i;
            }
        }
        if (contentAreaIndex < 0) {
            throw new SystemException("unable to parse content area index from header");
        }
        if (gradeIndex < 0) {
            throw new SystemException("unable to parse grade area index from header");
        }
        if (itemTypeIndex < 0) {
            throw new SystemException("unable to parse item type index from header");
        }
    }

    private String[] parseLine(String line) {
        String[] parts = StringUtils.split(line, ",");

        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
            parts[i] = StringUtils.strip(parts[i], "\"");
        }
        return parts;
    }

    private ItemInfo parseLineAsItemInfo(String line) {
        String[] parts = parseLine(line);

        return new ItemInfo(parts[0], parts[itemTypeIndex], parts[gradeIndex],
                parts[contentAreaIndex]);
    }

    public class ItemInfo {
        public ItemInfo(String id, String type, String grade, String contentArea) {
            this.id = id;
            this.type = type;
            this.grade = grade;
            this.contentArea = contentArea;
        }
        public String id;
        public String type;
        public String grade;
        public String contentArea;
    }
}
