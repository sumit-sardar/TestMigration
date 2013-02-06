package com.ctb.contentBridge.core.publish.dao;

import java.sql.*;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.log4j.*;

import com.ctb.contentBridge.core.exception.SystemException;

/**
 *
 */
public class DBContentTempGateway {
    private static Logger logger = Logger.getLogger(DBContentTempGateway.class);

	Session session;
	DBConnection dbConnection = null;
	
    public DBContentTempGateway(Session session) {
        this.session = session;
        try {
            this.dbConnection = new DBConnection(session.connection());
        } catch (HibernateException e) {
        	throw new SystemException(e.getMessage());
        }
    }

    public int countRows() {
        String query = "SELECT count(*) from content_tmp";

        try {
            return dbConnection.executeCountQuery(query, null);
        } catch (SQLException e) {
            throw new SystemException(
                "SQL Error counting table CONTENT_TMP");
        }
    }

    public void insertContentTemp(
        String grade,
        String itemSetName,
        String cmsItemSetID,
        String parentCMSItemSetID,
        int categoryLevel,
        String displayName) {
        if (grade == null) {
            grade = "";
        }
        if (displayName == null) {
            displayName = "";
        }

        checkNotNull(itemSetName, "itemSetName");
        checkNotNull(cmsItemSetID, "cmsItemSetID");
        checkNotNull(parentCMSItemSetID, "parentCMSItemSetID");
        checkNotNull(displayName, "displayName");

        Object[] args =
            {
                grade,
                itemSetName,
                cmsItemSetID,
                parentCMSItemSetID,
                new Integer(categoryLevel),
                displayName };

        dbConnection.executeUpdate(
            "INSERT INTO CONTENT_TMP(GRADE, ITEM_SET_NAME, EXT_CMS_ITEM_SET_ID, "
                + "PARENT_EXT_CMS_ITEM_SET_ID, ITEM_SET_CATEGORY_LEVEL, INTERNAL_PRODUCT_DISPLAY_NAME)"
                + " VALUES ('{0}','{1}','{2}','{3}','{4}','{5}')",
            args);
    }

    public void clearTable() {
        dbConnection.executeUpdate("delete content_tmp");
    }

    public void executeFrameworkImportProc(String frameworkCode, int productLevel ) {
        try {
            CallableStatement stmt = null;

            // todo : move away from this hard-coded logic
            if ( productLevel == 2 ) 
            {
                System.out.println("Use: SP_FRAMEWORK_IMPORT_WV_40");
                stmt =
                    dbConnection.prepareCall(
                        "{call SP_FRAMEWORK_IMPORT_WV_40(?)}");
            } else {
                stmt =
                    dbConnection.prepareCall("{call SP_FRAMEWORK_IMPORT_40(?)}");
            }
            DBObjectivesGateway dbog = new DBObjectivesGateway(session);
            int frameworkdId = dbog.getFrameWorkID(frameworkCode);

            stmt.setInt(1, frameworkdId);
            stmt.execute();
        } catch (SQLException sqlEx) {
            throw new SystemException(
                "unexpected error invoking import procedure: "
                    + sqlEx.getMessage());
        }
    }

    void checkNotNull(String param, String paramName) {
        if (param == null) {
            throw new SystemException(
                "Parameter " + paramName + " should not be null");
        }
    }

}
