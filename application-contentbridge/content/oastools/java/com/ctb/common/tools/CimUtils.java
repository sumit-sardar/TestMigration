package com.ctb.common.tools;

import java.sql.*;
import org.apache.log4j.*;

public class CimUtils {
	private static Logger logger = Logger.getLogger(CimUtils.class);

    public static int executeItemCheckQry(DBConnection dbconnection, String itemId) throws SQLException {
        String sql = "select count(*) from item where item_id = '{0}'";

        return dbconnection.executeCountQuery(sql, new Object[] {itemId});
    }

    public static int executeItemSetItemCheckQry(DBConnection dbconnection, String itemId, String cmsId, String frameworkCode) throws SQLException {
        Object[] arguments = {itemId, cmsId, frameworkCode};
        String sql = "select count(item_set_item.item_set_id) from item, item_set_item, "
                + "item_set, item_set_category, product where item.item_id = '"
                + "{0}" + "' and "
                + "item.item_id = item_set_item.item_id and item_set_item.item_set_id = "
                + "item_set.item_set_id and item_set.item_set_type = 'RE' and "
                + "item_set.ext_cms_item_set_id = '" + "{1}"
                + "' and item_set.item_set_category_id = "
                + "item_set_category.item_set_category_id and item_set_category.framework_product_id = "
                + "product.product_id and product.internal_display_name = '"
                + "{2}" + "'";

        return dbconnection.executeCountQuery(sql, arguments);
    }

    public static int executeDatapointConditionCodeCheckQry(DBConnection dbconnection, String itemId) throws SQLException {
        String sql = "select count(dcc.condition_code_id) from datapoint_condition_code dcc, datapoint d "
                + "where d.item_id = '{0}' and d.datapoint_id = dcc.datapoint_id";

        return dbconnection.executeCountQuery(sql, new Object[] {itemId});
    }

    public static int executeDatapointCheckQry(DBConnection dbconnection, String itemId) throws SQLException {
        String sql = "select count(*) from datapoint where item_id = '{0}'";

        return dbconnection.executeCountQuery(sql, new Object[] {itemId});
    }

    public static int executeItemMediaCheckQry(DBConnection dbconnection, String itemId) throws SQLException {
        String sql = "select count(*) from item_media where item_id = '{0}'";

        return dbconnection.executeCountQuery(sql, new Object[] {itemId});
    }

    public static void printItemDetails(DBConnection dbconnection, String itemId) throws SQLException {
        boolean itemFound = false;
        String sql = "SELECT ACTIVATION_STATUS, UPDATED_DATE_TIME FROM ITEM "
                + "WHERE ITEM_ID = '" + itemId + "'";
        String sql1 = "SELECT MEDIA_TYPE, DBMS_LOB.getLength(MEDIA_BLOB) FROM ITEM_MEDIA "
                + "WHERE ITEM_ID = '" + itemId
                + "' AND  (MEDIA_TYPE LIKE '%SWF' "
                + "OR MEDIA_TYPE LIKE '%PDF')";
        String sql2 = "SELECT MEDIA_TYPE, DBMS_LOB.getLength(MEDIA_CLOB) FROM ITEM_MEDIA "
                + "WHERE ITEM_ID = '" + itemId + "' AND MEDIA_TYPE LIKE '%XML' ";
        Statement st = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;

        try {
            st = dbconnection.createStatement();
            rs1 = st.executeQuery(sql);
            if (rs1.next()) {
                itemFound = true;
                String s = "Data retrieved for Item: " + itemId;
                s += "\n  ACTIVATION_STATUS: " + rs1.getString(1);
				s += "\n  UPDATED_DATE_TIME: " + rs1.getString(2);
				logger.info(s);
            }

            if (!itemFound) {
                return;
            }

            rs2 = st.executeQuery(sql1);
            while (rs2.next()) {
                logger.info("Length of " + rs2.getString(1) + ": "
                        + rs2.getString(2));
            }
            rs3 = st.executeQuery(sql2);
            while (rs3.next()) {
                logger.info("Length of " + rs3.getString(1) + ": "
                        + rs3.getString(2));
            }
        } catch (SQLException e) {
        	logger.error("", e);
        }
        finally {
            DBConnection.safeClose(st, rs1);
            if (rs2 != null) {
                rs2.close();
            }
            if (rs3 != null) {
                rs3.close();
            }			
        }

    }

}
