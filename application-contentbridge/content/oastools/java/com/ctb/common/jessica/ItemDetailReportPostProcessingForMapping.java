package com.ctb.common.jessica;

import java.io.*;
import java.sql.*;
import java.util.*;

import org.apache.commons.lang.*;

import com.ctb.common.tools.*;
import com.ctb.hibernate.HibernateUtils;

/**
 * perform post processing on 'jessica's report' to make it more
 * useful to QA after a mapping run.
 *
 * given input of the following format (and valid java.util.Properties format):
 * 3R.2.2.2.03=3R.2.2.2.03_WV_RLA.3.1.2
 * 3R.3.1.2.02=3R.3.1.2.02_WV_RLA.3.1.3
 * 3R.4.1.1.04=3R.4.1.1.04_WV_RLA.3.1.4
 *
 * this post processing will filter jessica's report to include only
 * items mapped and will also append answer keys for each item.
 */
public class ItemDetailReportPostProcessingForMapping {

    private File itemDetailReport;
    private File itemDetailReportPostProcess;
    private Map cabItemIdByMappedItemId;
    private DBConfig dbConfig;

    public ItemDetailReportPostProcessingForMapping(
        String detailFilename,
        Map mappingResults,
        String env) {
        setupDBConfig(env);
        setupItemDetailReport(detailFilename);
        setupMappingResults(mappingResults);
        setupItemDetailReportPostProcess(detailFilename);
    }

    private void setupItemDetailReportPostProcess(String detailFilename) {
        itemDetailReportPostProcess =
            new File(
                detailFilename.substring(0, detailFilename.lastIndexOf(".csv"))
                    + "-mapped.csv");
    }

    private void setupMappingResults(Map mappingResults) {
        cabItemIdByMappedItemId = new HashMap();
        for (Iterator iterator = mappingResults.keySet().iterator();
            iterator.hasNext();
            ) {
            Object key = (Object) iterator.next();

            cabItemIdByMappedItemId.put(mappingResults.get(key), key);
        }
    }

    private void setupItemDetailReport(String detailFilename) {
        itemDetailReport = new File(detailFilename);
        if (!itemDetailReport.exists()) {
            throw new IllegalArgumentException(
                detailFilename + " does not exist");
        }
    }

    private void setupDBConfig(String env) {
        dbConfig = new DBConfig(new File(env + ".properties"));
    }

    public File processItemDetailReport() throws IOException {
        if (!fileContainsItems(itemDetailReport)) {
            return null;
        }
        BufferedReader reader =
            new BufferedReader(new FileReader(itemDetailReport));
        BufferedWriter writer =
            new BufferedWriter(new FileWriter(itemDetailReportPostProcess));
        String header = reader.readLine();

        writer.write(header + ",Source Item Answer, Source Item ID\n");
        String line = null;

        while ((line = reader.readLine()) != null) {
            if (line.trim().length() == 0) {
                continue;
            }

            String[] detailParts = parseLine(line);
            String mappedItemId = detailParts[0];
            String cabItemId =
                (String) cabItemIdByMappedItemId.get(mappedItemId);

            if (cabItemId == null) {
                // not in mapping results so we'll skip it
                continue;
            }

            String itemType = detailParts[1];

            if (itemType == null
                && !itemType.equals(OASConstants.ITEM_TYPE_SR)
                && !itemType.equals(OASConstants.ITEM_TYPE_CR)) {
                throw new SystemException(
                    "unexpected item type of "
                        + itemType
                        + " for item "
                        + mappedItemId);
            }

            writer.write(line);
            writer.write(
                ","
                    + (itemType.equals("CR")
                        ? getDatapoint(cabItemId)
                        : getCorrectAnswer(cabItemId)));
            writer.write("," + cabItemId);
            writer.write("\n");
            writer.flush();
        }
        reader.close();
        writer.flush();
        writer.close();
        return itemDetailReportPostProcess;
    }

    private boolean fileContainsItems(File f) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String line = null;

            while ((line = reader.readLine()) != null) {
                if (line.trim().length() > 0) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
            return false;
        } catch (IOException e) {
            throw new SystemException(
                "unable to determine if report contains items",
                e);
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

    /**
     * return correct answer for an item id
     */
    private String getCorrectAnswer(String itemId) {
        Connection conn = null;

        try {
            conn = dbConfig.openConnection();
            DBItemGateway igw =
                new DBItemGateway(HibernateUtils.getSession(conn));

            return igw.getCorrectAnswer(itemId);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqlEx) {
                    // nothing to do
                    sqlEx.printStackTrace();
                }
            }
        }
    }

    private String getDatapoint(String itemId) {
        Connection conn = null;
        conn = dbConfig.openConnection();
        DBDatapointGateway dpgw =
            new DBDatapointGateway(HibernateUtils.getSession(conn));
        
        Datapoint dp = dpgw.getAnyDatapoint(itemId);
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException sqlEx) {
                // nothing to do
                sqlEx.printStackTrace();
            }
        }
        return "min:" + dp.getMinPoints() + " max:" + dp.getMaxPoints();
    }

    private static void usage() {
        System.out.println(
            "java -cp <classpath> com.ctb.common.jessica.ItemDetailReportPostProcessingForMapping <itemDetailReport> <mappingResults> <env>");
    }

    public static void main(String[] args) {
        if (args == null || args.length != 3) {
            usage();
            System.exit(1);
        }
        try {
            Properties mappingResults = new Properties();

            mappingResults.load(new FileInputStream(args[1]));
            new ItemDetailReportPostProcessingForMapping(
                args[0],
                mappingResults,
                args[2])
                .processItemDetailReport();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
