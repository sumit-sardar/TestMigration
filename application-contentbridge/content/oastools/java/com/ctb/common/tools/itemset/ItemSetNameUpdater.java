package com.ctb.common.tools.itemset;


import java.io.*;
import java.sql.*;
import java.util.*;

import com.ctb.common.tools.*;


public class ItemSetNameUpdater {

    private DBConfig dbConfig;
    private String inputFile;

    public ItemSetNameUpdater(String input, DBConfig dbConfig) {
        this.dbConfig = dbConfig;
        this.inputFile = input;
    }

    public void go() {
        try {
            List itemSets = parseFile(inputFile);
            Connection conn = dbConfig.openConnection();

            for (Iterator iterator = itemSets.iterator(); iterator.hasNext();) {
                ItemSetData itemSetData = (ItemSetData) iterator.next();

                updateItemSet(itemSetData, conn);
            }
            conn.close();
        } catch (SQLException e) {
            throw new SystemException("unexpected error updating ITEM_SET", e);
        }
    }

    private void updateItemSet(ItemSetData itemSet, Connection conn) {
        System.out.println("updating " + itemSet);
        try {
            String updateSql = "UPDATE ITEM_SET SET ITEM_SET_NAME = ? WHERE ITEM_SET_ID = ? AND EXT_CMS_ITEM_SET_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateSql);

            pstmt.setString(1, itemSet.itemSetName.trim());
            pstmt.setLong(2, itemSet.itemSetId);
            pstmt.setString(3, itemSet.extCmsItemSetId);
            int rows = pstmt.executeUpdate();

            if (rows != 1) {
                throw new SystemException("update failed for:  " + itemSet);
            } else {
                conn.commit();
            }
            pstmt.close();
        } catch (SQLException e) {
            throw new SystemException("unexpected SQL error executing update", e);
        }
    }

    private List parseFile(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;
            List list = new ArrayList();

            while ((line = reader.readLine()) != null) {
                if (line != null && line.trim().length() > 0) {
                    list.add(parseLine(line.trim()));
                }
            }
            reader.close();
            return list;
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            throw new SystemException("unable to build list from file", ioEx);
        }
    }

    private ItemSetData parseLine(String line) {
        StringTokenizer toker = new StringTokenizer(line, "\t");

        if (toker.countTokens() != 3) {
            throw new SystemException(toker.countTokens()
                    + "tokens, invalid input line:  " + line);
        }
        ItemSetData data = new ItemSetData();

        data.itemSetId = Long.parseLong(toker.nextToken());
        data.extCmsItemSetId = toker.nextToken();
        data.itemSetName = toker.nextToken();
        return data;
    }

    private class ItemSetData {
        public long itemSetId;
        public String extCmsItemSetId;
        public String itemSetName;

        public String toString() {
            return "ItemSetData[itemSetId=" + itemSetId + ",extCmsItemSetId="
                    + extCmsItemSetId + "]";
        }
    }

    public static void main(String[] args) {
        // (String host, String sid, String user, String password, boolean useThin)
        DBConfig config = new DBConfig("198.45.17.68", "ONAP", "oas", "wyslm64",
                true);
        ItemSetNameUpdater updater = new ItemSetNameUpdater("CAPITALIZATION all_wva_objectives_prod_0813.txt",
                config);

        updater.go();
    }
}
