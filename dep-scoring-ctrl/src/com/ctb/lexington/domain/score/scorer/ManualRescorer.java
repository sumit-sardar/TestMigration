package com.ctb.lexington.domain.score.scorer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import javax.naming.NamingException;

import com.ctb.lexington.db.JunitConnectionProvider;
import com.ctb.lexington.domain.score.ConnectionProvider;
import com.ctb.lexington.domain.score.event.AssessmentStartedEvent;
import com.ctb.lexington.util.SQLUtil;
import com.ctb.lexington.util.Stringx;

public class ManualRescorer {
    public static void main(String[] args) {
        if (args.length != 2 && args.length != 4) {
            System.err.println("Arguments passed in: " + Stringx.doubleQuote(Arrays.asList(args)));
            System.err.println("usage: rescore [-r <rosterId>] [-a <adminId>] [-u <y/n>]");
            System.exit(-1);
        }

        try {
            String rosterId = null;
            String testAdminId = null;
            String customerId = null;
            boolean updateContextData = true;
            for (int i = 0; i < args.length; i++) {
                if ("-c".equals(args[i])) {
                    customerId = args[++i];
                } else if ("-r".equals(args[i])) {
                    rosterId = args[++i];
                } else if ("-a".equals(args[i])) {
                    testAdminId = args[++i];
                } else if ("-u".equals(args[i])) {
                    String update = args[++i];
                    if (update != null && !update.equals("y")) {
                    	updateContextData = false;
                    }
                }
            }

            System.setProperty("testRun", "true");
            scoreCustomer(new JunitConnectionProvider(), customerId, testAdminId, rosterId, updateContextData);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static void scoreTestAdmin(final ConnectionProvider connProvider, String testAdminId,
            final String rosterId, final boolean updateContextData) throws SQLException, NamingException {
        final String basicSql = "select distinct admin.test_admin_id, "
                + "roster.test_roster_id, prod.product_id, prod.product_type, "
                + "stu.grade, stu.student_id from test_admin admin, "
                + "test_roster roster, item_set iset, item_set_product isp, "
                + "product prod, student stu, item_response ir where "
                + "roster.test_admin_id = admin.test_admin_id "
                + "and iset.item_set_id = admin.item_set_id "
                + "and isp.item_set_id = admin.item_set_id "
                + "and prod.product_id = isp.product_id "
                + "and stu.student_id = roster.student_id "
                + "and ir.test_roster_id = roster.test_roster_id ";

        String sql = basicSql;
        
        final Connection oasConn = connProvider.getOASConnection();
        final Connection atsConn = connProvider.getIRSConnection();
        
        boolean wholeAdmin = false;
        if (testAdminId != null) {
            sql += " and admin.test_admin_id = " + testAdminId;
            deleteExistingFactRecordsForAdmin(atsConn, new Long(Long.parseLong(testAdminId)));
            wholeAdmin = true;
        }
        if (rosterId != null) {
            sql += " and roster.test_roster_id = " + rosterId;
        }

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = oasConn.createStatement();
            rs = stmt.executeQuery(sql);

            Long rosterIdFromDb = null;
            Long testAdminIdFromDb = null;
            while (rs.next()) {
                try {
                    testAdminIdFromDb = SQLUtil.getLong(rs, "TEST_ADMIN_ID");
                    rosterIdFromDb = SQLUtil.getLong(rs, "TEST_ROSTER_ID");

                    // pass in the connection so as to do all three updates in one transaction
                    if (!wholeAdmin) {
                    	deleteExistingFactRecords(atsConn, testAdminIdFromDb, SQLUtil.getLong(rs,
                            "STUDENT_ID"));
                    }
                    
                    final AssessmentStartedEvent assessmentStartedEvent = new AssessmentStartedEvent(
                            rosterIdFromDb, SQLUtil.getInteger(rs, "PRODUCT_ID"), SQLUtil
                                    .getString(rs, "PRODUCT_TYPE"), SQLUtil.getString(rs, "GRADE"));
                    ScorerFactory.createScorer(assessmentStartedEvent);
                    ScorerFactory.releaseScorer(rosterIdFromDb, updateContextData);
                    System.out.println("+Scored roster: " + rosterIdFromDb + " for admin: "
                            + testAdminIdFromDb);
                } catch (Exception e) {
                	e.printStackTrace();
                    System.err.println("-Error scoring roster: " + rosterIdFromDb + " for admin: "
                            + testAdminIdFromDb + ": " + e.getMessage());
                    ScorerFactory.releaseFailedScorer(rosterIdFromDb);
                }
            }
        } finally {
            SQLUtil.close(rs, stmt);
            SQLUtil.close(oasConn);
            SQLUtil.close(atsConn);
        }
    }

    private static void scoreCustomer(final ConnectionProvider connProvider, String customerId, String testAdminId,
            final String rosterId, final boolean updateContextData) throws SQLException, NamingException {
        final String basicSql = "select distinct admin.test_admin_id, "
                + "roster.test_roster_id, prod.product_id, prod.product_type, "
                + "stu.grade, stu.student_id from test_admin admin, "
                + "test_roster roster, item_set iset, item_set_product isp, "
                + "product prod, student stu, item_response ir where "
                + "roster.test_admin_id = admin.test_admin_id "
                + "and iset.item_set_id = admin.item_set_id "
                + "and isp.item_set_id = admin.item_set_id "
                + "and prod.product_id = isp.product_id "
                + "and stu.student_id = roster.student_id "
                + "and ir.test_roster_id = roster.test_roster_id ";

        String sql = basicSql;
        
        final Connection oasConn = connProvider.getOASConnection();
        final Connection atsConn = connProvider.getIRSConnection();
        
        boolean wholeAdmin = false;
        if (customerId != null) {
            sql += " and admin.customer_id = " + testAdminId;
        }
        if (testAdminId != null) {
            sql += " and admin.test_admin_id = " + testAdminId;
            deleteExistingFactRecordsForAdmin(atsConn, new Long(Long.parseLong(testAdminId)));
            wholeAdmin = true;
        }
        if (rosterId != null) {
            sql += " and roster.test_roster_id = " + rosterId;
        }

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = oasConn.createStatement();
            rs = stmt.executeQuery(sql);

            Long rosterIdFromDb = null;
            Long testAdminIdFromDb = null;
            while (rs.next()) {
                try {
                    testAdminIdFromDb = SQLUtil.getLong(rs, "TEST_ADMIN_ID");
                    rosterIdFromDb = SQLUtil.getLong(rs, "TEST_ROSTER_ID");

                    // pass in the connection so as to do all three updates in one transaction
                    if (!wholeAdmin) {
                    	deleteExistingFactRecords(atsConn, testAdminIdFromDb, SQLUtil.getLong(rs,
                            "STUDENT_ID"));
                    }
                    
                    final AssessmentStartedEvent assessmentStartedEvent = new AssessmentStartedEvent(
                            rosterIdFromDb, SQLUtil.getInteger(rs, "PRODUCT_ID"), SQLUtil
                                    .getString(rs, "PRODUCT_TYPE"), SQLUtil.getString(rs, "GRADE"));
                    ScorerFactory.createScorer(assessmentStartedEvent);
                    ScorerFactory.releaseScorer(rosterIdFromDb, updateContextData);
                    System.out.println("+Scored roster: " + rosterIdFromDb + " for admin: "
                            + testAdminIdFromDb);
                } catch (Exception e) {
                	e.printStackTrace();
                    System.err.println("-Error scoring roster: " + rosterIdFromDb + " for admin: "
                            + testAdminIdFromDb + ": " + e.getMessage());
                    ScorerFactory.releaseFailedScorer(rosterIdFromDb);
                }
            }
        } finally {
            SQLUtil.close(rs, stmt);
            SQLUtil.close(oasConn);
            SQLUtil.close(atsConn);
        }
    }

    private static void deleteExistingFactRecords(final Connection atsConn, final Long testAdminId,
            final Long studentId) throws SQLException {
        final String factSql = "delete from sts_test_result_fact where "
                + "admin_dim_id = (select distinct admin_dim_id from "
                + "sts_administration_dim where instance_number = ?) "
                + "and student_dim_id = (select distinct student_dim_id from "
                + "sts_student_dim where oas_student_id = ?) ";

        final String totalSql = "delete from sts_total_student_score where "
                + "admin_dim_id = (select distinct admin_dim_id from "
                + "sts_administration_dim where instance_number = ?) "
                + "and student_dim_id = (select distinct student_dim_id from "
                + "sts_student_dim where oas_student_id = ?) ";

        final String predSql = "delete from student_predicted_scores where "
                + "admin_dim_id = (select distinct admin_dim_id from "
                + "sts_administration_dim where instance_number = ?) "
                + "and student_dim_id = (select distinct student_dim_id from "
                + "sts_student_dim where oas_student_id = ?) ";

        runUpdate(testAdminId, studentId, factSql, atsConn);
        runUpdate(testAdminId, studentId, totalSql, atsConn);
        runUpdate(testAdminId, studentId, predSql, atsConn);
        // commit explicitly since this connection's autoCommit is set to false
        atsConn.commit();
    }
    
    private static void deleteExistingFactRecordsForAdmin(final Connection atsConn, final Long testAdminId) throws SQLException {
        final String factSql = "delete from sts_test_result_fact where "
                + "admin_dim_id = (select distinct admin_dim_id from "
                + "sts_administration_dim where instance_number = ?)";

        final String totalSql = "delete from sts_total_student_score where "
                + "admin_dim_id = (select distinct admin_dim_id from "
                + "sts_administration_dim where instance_number = ?)";

        final String predSql = "delete from student_predicted_scores where "
                + "admin_dim_id = (select distinct admin_dim_id from "
                + "sts_administration_dim where instance_number = ?)";

        runUpdate(testAdminId, factSql, atsConn);
        runUpdate(testAdminId, totalSql, atsConn);
        runUpdate(testAdminId, predSql, atsConn);
        // commit explicitly since this connection's autoCommit is set to false
        atsConn.commit();
    }

    private static void runUpdate(final Long testAdminId, final Long studentId,
            final String factSql, final Connection conn) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(factSql);
            stmt.setLong(1, testAdminId.longValue());
            stmt.setLong(2, studentId.longValue());
            stmt.executeUpdate();
        } finally {
            SQLUtil.close(stmt);
        }
    }
    
    private static void runUpdate(final Long testAdminId,
            final String factSql, final Connection conn) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(factSql);
            stmt.setLong(1, testAdminId.longValue());
            stmt.executeUpdate();
        } finally {
            SQLUtil.close(stmt);
        }
    }
}