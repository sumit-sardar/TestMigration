package com.ctb.lexington.domain.score.collector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ctb.lexington.db.ConnectionFactory;
import com.ctb.lexington.db.constants.ATSDatabaseConstants;
import com.ctb.lexington.db.data.StudentData;
import com.ctb.lexington.db.utils.DataHelper;
import com.ctb.lexington.util.SQLUtil;

public class StudentCollector {
    private final Connection conn;

    public StudentCollector(Connection conn) {
        this.conn = conn;
    }

    public StudentData collectStudentData(Long oasRosterId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        StudentData data;
        Long studentId;
        try {
            String sql = "select * "
                + "from student, test_roster, customer "
                + "where student.student_id=test_roster.student_id "
                + "and customer.customer_id = test_roster.customer_id "
                + "and test_roster.test_roster_id=?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, oasRosterId.longValue());
            rs = ps.executeQuery();
            data = new StudentData();

            rs.next();
            studentId = SQLUtil.getLong(rs, "student_id");
            data.setOasStudentId(studentId);
            data.setFirstName(SQLUtil.getString(rs, "FIRST_NAME"));
            data.setLastName(SQLUtil.getString(rs, "LAST_NAME"));
            data.setCustomer(SQLUtil.getString(rs, "CUSTOMER_NAME"));
            data.setMiddleInitial(SQLUtil.getString(rs, "MIDDLE_NAME"));
            data.setBirthDate(SQLUtil.asDate(SQLUtil.getTimestamp(rs, "BIRTHDATE")));
            data.setEmail(SQLUtil.getString(rs, "EMAIL"));
            data.setGender(SQLUtil.getString(rs, "GENDER"));

            data.setStudentSpecialCode(ATSDatabaseConstants.HARDCODED_STUDENT_SPECIAL_CODE);
            data.setStudentIdentifier1(SQLUtil.getString(rs, "EXT_PIN1"));
            data.setStudentIdentifier2(SQLUtil.getString(rs, "EXT_PIN2"));
            data.setStudentIdentifier3(SQLUtil.getString(rs, "EXT_PIN3"));
            data.setExtElmId(SQLUtil.getString(rs, "Ext_elm_id"));
            data.setGrade(SQLUtil.getString(rs, "grade"));
            data.setBarcode(SQLUtil.getString(rs, "barcode"));
            data.setStartDateTime(SQLUtil.getTimestamp(rs, "start_date_time"));

            //data.setStudentGrade(SQLUtil.getString(rs, "GRADE"));
            if(data.getBirthDate() != null) {
	            data.setAgeInYears(new Long(DataHelper.getDifferenceInYears(new java.sql.Date(data
	                    .getBirthDate().getTime())))); // months between sysdate and birthdate/12
	            data.setAgeInMonths(new Long(DataHelper.getDifferenceInMonths(new java.sql.Date(data
	                    .getBirthDate().getTime())))); // months after test admin start date
            }
        } finally {
            SQLUtil.close(rs);
            ConnectionFactory.getInstance().release(ps);
        }

        populateOrgNodeId(studentId, data);
        populateStudentContactData(data, studentId);
        return data;
    }

    private void populateOrgNodeId(Long studentId, StudentData data) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select org_node_id from org_node_student where student_id=? order by updated_by";
            //could be multiple results, but just use the 1st.
            ps = conn.prepareStatement(sql);
            ps.setLong(1, studentId.longValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                //REDTAG - Is it valid for a student not to have an org_node_student row ?
                data.setOrgNode(SQLUtil.getString(rs, "org_node_id"));
            }
        } finally {
            SQLUtil.close(rs);
            ConnectionFactory.getInstance().release(ps);
        }
    }

    private void populateStudentContactData(StudentData data, Long studentId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select * from student_contact where student_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, studentId.longValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                data.setAddressLine1(SQLUtil.getString(rs, "STREET_LINE1"));
                data.setAddressLine2(SQLUtil.getString(rs, "STREET_LINE2"));
                data.setCity(SQLUtil.getString(rs, "CITY"));
                data.setState(SQLUtil.getString(rs, "STATEPR"));
                data.setZipCode(SQLUtil.getString(rs, "ZIPCODE"));
            }
        } finally {
            SQLUtil.close(rs);
            ConnectionFactory.getInstance().release(ps);
        }
    }
}