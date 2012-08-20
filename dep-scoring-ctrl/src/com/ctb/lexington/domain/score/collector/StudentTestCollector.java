package com.ctb.lexington.domain.score.collector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ctb.lexington.db.ConnectionFactory;
import com.ctb.lexington.db.constants.ATSDatabaseConstants;
import com.ctb.lexington.db.data.StudentTestData;
import com.ctb.lexington.db.data.StudentTestDetails;
import com.ctb.lexington.exception.DataException;
import com.ctb.lexington.util.SQLUtil;

public class StudentTestCollector {
    private final Connection conn;

    public StudentTestCollector(Connection conn) {
        this.conn = conn;
    }

    public StudentTestData collectStudentTestData(Long oasRosterId)
            throws SQLException, DataException {
        final String sql =
            "select distinct " +
                   "admin.session_number, " + 
                   "cat.test_name test_name, " + 
                   "td.item_set_form, " + 
                   "td.item_set_level, " +
                   "tc.grade, " +
                   "cust.customer_name, " + 
                   "td.item_set_name sub_test_name, " +
                   "td.item_set_id as subtestId, " +
                   "td.subject as subject, " +
                   "td.sample as sample, " +
                   "siss.completion_date_time as subtestCompletionTimestamp, " +
                   "siss.completion_status as completionStatus " +
            "from " +
                "test_admin admin, " +
                "item_set tc, " +
                "item_set td, " +
                "customer cust, " +
                "test_roster rost, " +
				"student_item_set_status siss, " +
                "test_catalog cat " +
            "where " +
                 "rost.test_roster_id = ? " +
				 "and siss.test_roster_id = rost.test_roster_id " +
                 "and rost.test_admin_id = admin.test_Admin_id " +
                 "and admin.customer_id = cust.customer_id " +
                 "and admin.item_set_id = tc.item_set_id " +
				 "and siss.item_set_id = td.item_set_id " +
                 "and tc.item_set_type in ('TC', 'AT') " +
                 "and td.item_set_type = 'TD' " +
                 "and cat.item_set_id = tc.item_set_id " +
            "order by td.subject";

        PreparedStatement ps = null;
        ResultSet rs = null;
        final StudentTestData data = new StudentTestData();
        try {
            ps = conn.prepareStatement(sql);
            ps.setLong(1, oasRosterId.longValue());
            rs = ps.executeQuery();
            while (rs.next()) {
                StudentTestDetails detail = new StudentTestDetails();
                detail.setTestName(SQLUtil.getString(rs, "TEST_NAME"));
                detail.setSubTestName(SQLUtil.getString(rs, "SUB_TEST_NAME"));
                detail.setTestForm(SQLUtil.getString(rs, "ITEM_SET_FORM"));
                detail.setTestLevel(SQLUtil.getString(rs, "ITEM_SET_LEVEL"));
                detail.setTestGrade(SQLUtil.getString(rs, "GRADE"));
                detail.setCustomer(SQLUtil.getString(rs, "CUSTOMER_NAME"));
                detail.setSessionNumber(SQLUtil.getString(rs, "session_number"));
                detail.setTestBookNumber(ATSDatabaseConstants.NOT_APPLY);
                detail.setScoringPattern(ATSDatabaseConstants.SCORING_PATTERN_T);
                detail.setTestScoreType(ATSDatabaseConstants.OAS_SCORE_TYPE);
                detail.setSubtestId(SQLUtil.getLong(rs, "subtestId"));
                detail.setSubtestCompletionTimestamp(SQLUtil.getTimestamp(rs, "subtestCompletionTimestamp"));
                detail.setSample(SQLUtil.getString(rs, "SAMPLE"));
                detail.setSubject(SQLUtil.getString(rs, "SUBJECT"));
                detail.setCompletionStatus(SQLUtil.getString(rs, "COMPLETIONSTATUS"));
                data.add(detail);
            }
            if (data.size() == 0) {
                throw new DataException(
                        "No Student test data for TestRosterID " + oasRosterId);
            }
            return data;
        } finally {
            SQLUtil.close(rs);
            ConnectionFactory.getInstance().release(ps);
        }
    }
}