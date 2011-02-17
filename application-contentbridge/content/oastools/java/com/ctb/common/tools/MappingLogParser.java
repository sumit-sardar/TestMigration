package com.ctb.common.tools;


import java.io.*;
import java.util.*;


public class MappingLogParser {

    private File log;

    public MappingLogParser(File log) {
        this.log = log;
    }

    public List parseToMappedItems() {
        try {
            List items = new ArrayList();
            BufferedReader reader = new BufferedReader(new FileReader(log));
            String line = null;

            while ((line = reader.readLine()) != null) {
                if (line.indexOf("mapped") > -1
                        && line.indexOf("successful") > -1) {
                    items.add(parseMappedItemFromSuccessLine(line));
                }
            }
            return items;
        } catch (IOException e) {
            throw new SystemException("unable to read log file", e);
        }
    }

    public Map parseToMappedItemsByItemsMap() {
        try {
            Map items = new HashMap();
            BufferedReader reader = new BufferedReader(new FileReader(log));
            String line = null;

            while ((line = reader.readLine()) != null) {
                if (line.indexOf("mapped") > -1
                        && line.indexOf("successful") > -1) {
                    String item = parseItemFromSuccessLine(line);
                    String mappedItem = parseMappedItemFromSuccessLine(line);

                    items.put(item, mappedItem);
                }
            }
            return items;
        } catch (IOException e) {
            throw new SystemException("unable to read log file", e);
        }
    }

    // Item '4R.1.1.1.02' mapped to '4R.1.1.1.02_WV_RLA.4.1.1' and imported successfully
    String parseMappedItemFromSuccessLine(String line) {
        int idx = line.indexOf("' and imported successfully");
        String mappedItem = line.substring(0, idx);

        idx = line.indexOf("mapped to '");
        return mappedItem.substring(idx + "mapped to '".length());
    }

    // Item '4R.1.1.1.02' mapped to '4R.1.1.1.02_WV_RLA.4.1.1' and imported successfully
    String parseItemFromSuccessLine(String line) {
        int idx = line.indexOf("' mapped to");
        String mappedItem = line.substring(0, idx);

        idx = line.indexOf("Item '");
        return mappedItem.substring(idx + "Item '".length());
    }
}
