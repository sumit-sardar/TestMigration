package com.ctb.contentBridge.core.publish.tools;

import java.io.*;
import java.sql.*;
import java.util.*;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.dao.DBConfig;
import com.ctb.contentBridge.core.publish.dao.DBConnection;
import com.ctb.contentBridge.core.publish.dao.DBItemGateway;
import com.ctb.contentBridge.core.publish.hibernate.HibernateUtils;
import com.ctb.contentBridge.core.publish.mapping.ItemMap;

/**
 * Generates reports on a database based on a given list of item IDs and produces a report containing
 *  1. Items that have already been mapped from a source framework (CAB) to another framework
 *  2. Items that have not yet been mapped, but exist in the source framework
 *  3. Items that are not in the source framework
 *  4. Items that do not exist in the mapping file
 */
public class MappedItemStatusReport {
    int itemsInList;
    ArrayList mappedItems = new ArrayList();
    ArrayList unmappedItems = new ArrayList();
    ArrayList missingItems = new ArrayList();
    ArrayList notFoundInMap = new ArrayList();
    ArrayList mappedInactiveItems = new ArrayList();
    ArrayList unmappedInactiveItem = new ArrayList();
    ArrayList mappedItemsNotInList = new ArrayList();

    SortedSet allItemIDs = null;
    ArrayList inactiveItems = null;
    DBItemGateway gateway;
    boolean detailedReport = false;

    ArrayList itemColumns[] =
        {
            mappedItems,
            unmappedItems,
            missingItems,
            notFoundInMap,
            mappedInactiveItems,
            unmappedInactiveItem,
            mappedItemsNotInList };
    String columnNames[] =
        {
            "Mapped Active",
            "UnMapped Active (to be mapped)",
            "Missing from DB but part of Framework",
            "Not Found in map",
            "Mapped Inactive",
            "UnMapped Inactive",
            "In Mapping but not in the item list" };

    public MappedItemStatusReport(DBConnection connection) {
    	//TODO: this class should only read from the item gateway since there is no hibernate decorater to flush the session
        this.gateway =
            new DBItemGateway(
                HibernateUtils.getSession(connection.getConnection()));
    }

    public void run(String frameworkCode, ItemMap itemMap) {
        ArrayList itemIDs = new ArrayList();

        for (Iterator iter = itemMap.getAllItemIDs(); iter.hasNext();) {
            itemIDs.add(iter.next());
        }
        run(frameworkCode, itemMap, itemIDs);
    }

    public void run(String frameworkCode, ItemMap itemMap, ArrayList itemIDs) {
        itemsInList = itemIDs.size();
        loadItemIDs();
        categorizeItemsInList(frameworkCode.toUpperCase(), itemMap, itemIDs);
        findMappedItemsNotInList(itemMap, itemIDs);
    }

    private void findMappedItemsNotInList(ItemMap itemMap, ArrayList itemIDs) {
        // find what items are in the map, but not in the list
        for (Iterator iter = itemMap.getAllItemIDs(); iter.hasNext();) {
            String itemID = (String) iter.next();

            if (!itemIDs.contains(itemID)) {
                mappedItemsNotInList.add(itemID);
            }
        }
    }

    private void categorizeItemsInList(
        String frameworkCode,
        ItemMap itemMap,
        ArrayList itemIDs) {
        for (Iterator iter = itemIDs.iterator(); iter.hasNext();) {
            String itemID = (String) iter.next();
            String curriculumID = itemMap.curriculumId(itemID);

            if (curriculumID == null) {
                notFoundInMap.add(itemID);
            } else {
                String mappedItemID =
                    getMappedItemID(frameworkCode, itemID, curriculumID);

                if (itemExistsInCAB(mappedItemID)) {
                    if (inactiveItems.contains(mappedItemID)) {
                        mappedInactiveItems.add(itemID);
                    } else {
                        mappedItems.add(itemID);
                    }
                } else if (itemExistsInCAB(itemID)) {
                    if (inactiveItems.contains(itemID)) {
                        unmappedInactiveItem.add(itemID);
                    } else {
                        unmappedItems.add(itemID);
                    }
                } else {
                    missingItems.add(itemID);
                }
            }
        }
    }

    public boolean itemExistsInFramework(
        String frameworkCode,
        String itemID,
        String curriculumID) {
        return itemExistsInCAB(
            getMappedItemID(frameworkCode, itemID, curriculumID));
    }

    public String getMappedItemID(
        String frameworkCode,
        String itemID,
        String curriculumID) {
        return itemID + "_" + frameworkCode;
    }

    public boolean itemExistsInCAB(String itemID) {
        return allItemIDs.contains(itemID);
    }

    void loadItemIDs() {
        allItemIDs = new TreeSet();
        inactiveItems = new ArrayList();
        gateway.getAllItemIDs(allItemIDs, inactiveItems);
    }

    public void print() {
        System.out.println("Items in list, Items in Database");
        System.out.println(itemsInList + "," + allItemIDs.size());

        // print the item header
        String header = "";

        for (int i = 0; i < columnNames.length; i++) {
            header += columnNames[i];
            if (i < columnNames.length - 1) {
                header += ",";
            }
        }
        System.out.println(header);

        // print the lengths of the columns
        for (int i = 0; i < itemColumns.length; i++) {
            System.out.print(itemColumns[i].size());
            if (i < itemColumns.length - 1) {
                System.out.print(",");
            }
        }
        System.out.println();

        if (detailedReport) {
            printDetailedReport();
        }
    }

    private void printDetailedReport() {
        int maxRows = 0;

        // find the max # of rows of all the columns
        for (int i = 0; i < itemColumns.length; i++) {
            if (itemColumns[i].size() > maxRows) {
                maxRows = itemColumns[i].size();
            }
        }

        // print each row
        for (int i = 0; i < maxRows; i++) {
            for (int j = 0; j < itemColumns.length; j++) {
                if (i < itemColumns[j].size()) {
                    System.out.print(itemColumns[j].get(i));
                }
                if (j < itemColumns.length - 1) {
                    System.out.print(",");
                }
            }
            System.out.println();
        }
    }

    void setDetail(boolean detail) {
        this.detailedReport = detail;
    }

    static DBConnection openConnection(String env) {
        try {
            File curDir = new File(".");
            DBConfig dbconfig =
                new DBConfig(
                    new File(curDir.getCanonicalFile(), env + ".properties"));
            Connection connection = dbconfig.openConnection();

            return new DBConnection(connection);
        } catch (Exception e) {
            throw new SystemException(
                "Error opening connection : " + e.getMessage());
        }
    }

    static ArrayList getItems(File file) throws IOException {
        // System.out.println("Reading input Items");
        ArrayList items = new ArrayList();
        String line;
        BufferedReader lines = new BufferedReader(new FileReader(file));

        while ((line = lines.readLine()) != null) {
            if (line.trim().equals("")) {
                continue;
            }
            if (items.contains(line)) {
                System.out.println("Ignoring duplicate input item " + line);
                continue;
            }
            items.add(line);
        }
        // System.out.println("Loaded " + items.size() + " items");
        return items;
    }
}
