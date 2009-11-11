package com.ctb.lexington.util.subtestsection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;

import com.ctb.lexington.db.JunitConnectionProvider;
import com.ctb.lexington.util.SQLUtil;

/**
 * @author mnkamiya
 * @version $Id$
 */
public class SubtestDataRetriever {
    private static final String SECTIONABLE_PRODUCT_TYPE="TV";//only TerraNova tests have sections for now

    Connection connection;
    PreparedStatement subtestInfoQuery;
    PreparedStatement itemInfoQuery;

    public SubtestDataRetriever() {
    }

    public void getSubtestSectionInfo(List subtests) {
        initConnection();
        try {
            for (Iterator i = subtests.iterator(); i.hasNext();) {
                Subtest subtest = (Subtest) i.next();
                try {
                    retrieveSubtestInfo(subtest);
                    validateSubtestInfo(subtest);
                    for (Iterator j = subtest.getSections().iterator(); j.hasNext();) {
                        Section section = (Section) j.next();
                        retrieveSectionInfo(subtest, section);
                    }
                } catch (Exception e) {
                    System.err.println("WARNING: Unable to retrieve information for subtest: " + subtest.getName() +".  " + e.getMessage());
                    // do not rethrow, and let the process continue
                    // (some environments may not have all the content loaded)
                }
            }
        } finally {
            closeConnection();
        }
    }

    public void getSubtestSectionInfo(Subtest subtest) {
        initConnection();
        try {
            retrieveSubtestInfo(subtest);
            validateSubtestInfo(subtest);
            for (Iterator i = subtest.getSections().iterator(); i.hasNext();) {
                Section section = (Section) i.next();
                retrieveSectionInfo(subtest, section);
            }
        } finally {
            closeConnection();
        }

    }

    private void validateSubtestInfo(Subtest subtest) {
        int sumTimeLimit = 0;
        for (Iterator i = subtest.getSections().iterator(); i.hasNext();) {
            Section s = (Section) i.next();
            sumTimeLimit += s.getTimeLimit();
        }
        if (sumTimeLimit != subtest.getTimeLimit())
            throw new RuntimeException("Section time limits (" + sumTimeLimit + ") do not sum to Subtest time limit (" + subtest.getTimeLimit() + ").");
    }

    private void retrieveSubtestInfo(Subtest subtest) {
        try {
            PreparedStatement query = getSutestInfoQuery();
            query.setString(1,subtest.getLevel());
            query.setString(2,subtest.getName());
            ResultSet result = query.executeQuery();
            if (result.next()) {
                subtest.setItemSetId(SQLUtil.getLong(result,"ITEM_SET_ID"));
                subtest.setTimeLimit(result.getInt("TIME_LIMIT"));
            } else {
                throw new RuntimeException("Unable to find item set. (name: " + subtest.getName() + ", level: " + subtest.getLevel() + ")");
            }
            if (result.next()) {
                throw new RuntimeException("Subtest resolved to more than one item set. (name: " + subtest.getName() + ", level: " + subtest.getLevel() + ")");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving info for subtest: " + subtest.getName() + ". " + e.getMessage());
        }
    }

    private void retrieveSectionInfo(Subtest subtest, Section section) {
        try {
            section.setFirstItemId(getItemId(subtest.getItemSetId().longValue(), section.getFirstItemNumber()));
            section.setLastItemId(getItemId(subtest.getItemSetId().longValue(), section.getLastItemNumber()));
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving info for section (itemSet: " + subtest.getItemSetId() + ", section#: " + section.getOrder() + ")." + e.getMessage());
        }
    }

    private PreparedStatement getSutestInfoQuery() {
        if (subtestInfoQuery == null) {
            try {
                subtestInfoQuery = connection.prepareStatement(
                        "select item_set_id, time_limit " +
                        "from item_set " +
                        "where " +
                            "item_set_level=? " +
                            "and sample='F' " +
                            "and activation_status='AC' " +
                            "and item_set_id in " +
                            "( " +
                                "select item_set_id " +
                                "from item_set_product " +
                                "where " +
                                "product_id in " +
                                "( " +
                                    "select product_id " +
                                    "from product " +
                                    "where product_type='"+SECTIONABLE_PRODUCT_TYPE+"'" +
                                ") " +
                            ") " +
                            "and ext_tst_item_set_id = ?");
            } catch (SQLException e) {
                throw new RuntimeException("Error preparing SQL statement. " + e.getMessage(), e);
            }
        }
        try {
            subtestInfoQuery.clearParameters();
            return subtestInfoQuery;
        } catch (SQLException e) {
            throw new RuntimeException("Error clearing parameters of subtest info query. " + e.getMessage(), e);
        }
    }

    private String getItemId(long itemSetId, int itemNumber) throws SQLException {
        PreparedStatement query = getItemInfoQuery();
        query.setLong(1, itemSetId);
        query.setInt(2, itemNumber);
        ResultSet result = query.executeQuery();
        if (result.next()) {
            return result.getString("ITEM_ID");
        } else {
            throw new RuntimeException("Item not found.  (itemset: " + itemSetId + ", itemNumber: " + itemNumber + ")");
        }
    }

    private PreparedStatement getItemInfoQuery() {
        if (itemInfoQuery == null) {
            try {
                itemInfoQuery = connection.prepareStatement("select item_id " +
                        "from item_set_item " +
                        "where " +
                        "item_set_id=? " +
                        "and item_sort_order=?");
            } catch (SQLException e) {
                throw new RuntimeException("Error preparing SQL statement. " + e.getMessage(), e);
            }
        }
        try {
            itemInfoQuery.clearParameters();
            return itemInfoQuery;
        } catch (SQLException e) {
            throw new RuntimeException("Error clearing parameters of subtest info query. " + e.getMessage(), e);
        }
    }

    private void initConnection() {
        try {
            connection = new JunitConnectionProvider().getOASConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Error while connecting to the database.", e);
        }
    }

    private void closeConnection() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            // do nothing
        }
    }
}
