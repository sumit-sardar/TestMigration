package com.ctb.lexington.domain.score.collector;

import com.ctb.lexington.db.ConnectionFactory;
import com.ctb.lexington.db.data.StudentTestHistoryData;
import com.ctb.lexington.util.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentTestHistoryCollector {
    private final Connection conn;

    public StudentTestHistoryCollector(Connection conn) {
        this.conn = conn;
    }

    public StudentTestHistoryData collectStudentTestHistoryData(Long oasRosterId) throws SQLException {
        PreparedStatement selectStatement = null;
        ResultSet rs = null;
        final StudentTestHistoryData testHistoryData = new StudentTestHistoryData();

        try{
            selectStatement = conn.prepareStatement(SELECT_SQL);
            selectStatement.setLong(1, oasRosterId.longValue());
            rs = selectStatement.executeQuery();

            populateData(testHistoryData, rs);
        } finally {
            SQLUtil.close(rs);
            ConnectionFactory.getInstance().release(selectStatement);
        }
        return testHistoryData;
    }
    
    private void populateData(StudentTestHistoryData testHistoryData, ResultSet rs) throws SQLException {
        if (!rs.next())
            throw new IllegalStateException("Insufficient data in STUDENT_TEST_HISTORY result set");

        //testHistoryData.setStudentTestHistoryId(SQLUtil.getLong(rs, "STUDENT_TEST_HISTORY_ID")));
        testHistoryData.setTestRosterId(SQLUtil.getLong(rs, "TEST_ROSTER_ID"));
        testHistoryData.setCustomerId(SQLUtil.getLong(rs, "CUSTOMER_ID"));
        testHistoryData.setTestAdminId(SQLUtil.getLong(rs, "TEST_ADMIN_ID"));
        testHistoryData.setTestCompletionStatus(SQLUtil.getString(rs, "TEST_COMPLETION_STATUS"));
        testHistoryData.setStudentId((SQLUtil.getLong(rs, "STUDENT_ID")));
        testHistoryData.setStartDateTime(SQLUtil.getTimestamp(rs, "START_DATE_TIME"));
        testHistoryData.setCompletionDateTime(SQLUtil.getTimestamp(rs, "COMPLETION_DATE_TIME"));
        testHistoryData.setTestName(SQLUtil.getString(rs, "TEST_NAME"));
        testHistoryData.setTestAdminName(SQLUtil.getString(rs, "TEST_ADMIN_NAME"));
        testHistoryData.setTestCatalogId(SQLUtil.getLong(rs, "TEST_CATALOG_ID"));
        testHistoryData.setTestItemSetId(SQLUtil.getLong(rs, "TEST_ITEM_SET_ID"));
        testHistoryData.setProductId(SQLUtil.getLong(rs, "PRODUCT_ID"));
        testHistoryData.setProductName(SQLUtil.getString(rs, "PRODUCT_NAME"));
        testHistoryData.setProductType(SQLUtil.getString(rs, "PRODUCT_TYPE"));
        testHistoryData.setTestValidationStatus(SQLUtil.getString(rs, "VALIDATION_STATUS"));
        testHistoryData.setTestValidationUpdatedBy(SQLUtil.getLong(rs, "VALIDATION_UPDATED_BY"));
        testHistoryData.setTestValidationUpdatedDate(SQLUtil.getTimestamp(rs, "VALIDATION_UPDATED_DATE"));
        testHistoryData.setTestValidationUpdatedNote(SQLUtil.getString(rs, "VALIDATION_UPDATED_NOTE"));
        testHistoryData.setLexingtonVersion(SQLUtil.getString(rs, "LEXINGTON_VERSION"));
        testHistoryData.setTutorialId(SQLUtil.getLong(rs, "TUTORIAL_ID"));
        testHistoryData.setUserName(SQLUtil.getString(rs, "USER_NAME"));
        testHistoryData.setLastName(SQLUtil.getString(rs, "LAST_NAME"));
        testHistoryData.setFirstName(SQLUtil.getString(rs, "FIRST_NAME"));
        testHistoryData.setMiddleName(SQLUtil.getString(rs, "MIDDLE_NAME"));
        testHistoryData.setPrefix(SQLUtil.getString(rs, "PREFIX"));
        testHistoryData.setSuffix(SQLUtil.getString(rs, "SUFFIX"));
        testHistoryData.setBirthdate(SQLUtil.getTimestamp(rs, "BIRTHDATE"));
        testHistoryData.setGender(SQLUtil.getString(rs, "GENDER"));
        testHistoryData.setEthnicity(SQLUtil.getString(rs, "ETHNICITY"));
        testHistoryData.setEmail(SQLUtil.getString(rs, "EMAIL"));
        testHistoryData.setGrade(SQLUtil.getString(rs, "GRADE"));
        testHistoryData.setAge(SQLUtil.getLong(rs, "AGE"));
        testHistoryData.setExtPin1(SQLUtil.getString(rs, "EXT_PIN1"));
        testHistoryData.setExtPin2(SQLUtil.getString(rs, "EXT_PIN2"));
        testHistoryData.setExtPin3(SQLUtil.getString(rs, "EXT_PIN3"));
        testHistoryData.setExtSchoolId(SQLUtil.getString(rs, "EXT_SCHOOL_ID"));
        testHistoryData.setExtElmId(SQLUtil.getString(rs, "EXT_ELM_ID"));
        testHistoryData.setCustomerName(SQLUtil.getString(rs, "CUSTOMER_NAME"));
        testHistoryData.setCreatedDateTime(SQLUtil.getTimestamp(rs, "CREATED_DATE_TIME"));
        testHistoryData.setScoringStatus(SQLUtil.getString(rs, "SCORING_STATUS"));
        testHistoryData.setCreatorOrgNodeId(SQLUtil.getLong(rs, "CREATOR_ORG_NODE_ID"));
        testHistoryData.setParentOrgNodeId(SQLUtil.getLong(rs, "PARENT_ORG_NODE_ID"));
        testHistoryData.setGrandparentOrgNodeId(SQLUtil.getLong(rs, "GRANDPARENT_ORG_NODE_ID"));
        testHistoryData.setCreatorOrgNodeName(SQLUtil.getString(rs, "CREATOR_ORG_NODE_NAME"));
        testHistoryData.setParentOrgNodeName(SQLUtil.getString(rs, "PARENT_ORG_NODE_NAME"));
        testHistoryData.setGrandparentOrgNodeName(SQLUtil.getString(rs, "GRANDPARENT_ORG_NODE_NAME"));
        testHistoryData.setContactName(SQLUtil.getString(rs, "CONTACT_NAME"));
        testHistoryData.setContactType(SQLUtil.getString(rs, "CONTACT_TYPE"));
        testHistoryData.setContactEmail(SQLUtil.getString(rs, "CONTACT_EMAIL"));
        testHistoryData.setStreetLine1(SQLUtil.getString(rs, "STREET_LINE1"));
        testHistoryData.setStreetLine2(SQLUtil.getString(rs, "STREET_LINE2"));
        testHistoryData.setStreetLine3(SQLUtil.getString(rs, "STREET_LINE3"));
        testHistoryData.setCity(SQLUtil.getString(rs, "CITY"));
        testHistoryData.setStatepr(SQLUtil.getString(rs, "STATEPR"));
        testHistoryData.setCountry(SQLUtil.getString(rs, "COUNTRY"));
        testHistoryData.setZipcode(SQLUtil.getString(rs, "ZIPCODE"));
        testHistoryData.setPrimaryPhone(SQLUtil.getString(rs, "PRIMARY_PHONE"));
        testHistoryData.setSecondaryPhone(SQLUtil.getString(rs, "SECONDARY_PHONE"));
        testHistoryData.setFax(SQLUtil.getString(rs, "FAX"));
        testHistoryData.setSchedulerUserId(SQLUtil.getLong(rs, "CREATED_BY"));
        testHistoryData.setSchedulerUserName(SQLUtil.getString(rs, "SCHEDULER_USER_NAME"));
        //testHistoryData.setAtsArchive(SQLUtil.getString(rs, "ATS_ARCHIVE"));
        testHistoryData.setStudentOrgNodeId(SQLUtil.getLong(rs, "STUDENT_ORG_NODE_ID"));
        testHistoryData.setCaptureMethod(SQLUtil.getString(rs, "CAPTURE_METHOD"));
        testHistoryData.setAtsStudentOrgNodeId(SQLUtil.getLong(rs, "STUDENT_ORG_NODE_ID"));
        
    }

    private static final String SELECT_SQL = "select " +
		"tr.test_roster_id, " +
		"tr.customer_id, " +
		"tr.test_admin_id, " +
		"tr.test_completion_status, " +
		"tr.scoring_status, " +
		"tr.capture_method, " +
		"tr.student_id, " +
		"tr.start_date_time, " +
		"max(siss.completion_date_time) as completion_date_time, " +
		"tc.TEST_NAME, " +
		"ta.TEST_ADMIN_NAME, " +
		"tc.TEST_CATALOG_ID, " +
		"tc.ITEM_SET_ID as test_item_set_id, " +
		"ta.PRODUCT_ID, " +
		"pr.PRODUCT_NAME, " +
        "pr.PRODUCT_TYPE, " +
		"tr.VALIDATION_STATUS as validation_status, " +
		"tr.VALIDATION_UPDATED_BY as validation_updated_by, " +
		"tr.VALIDATION_UPDATED_DATE_TIME as validation_updated_date, " +
		"tr.VALIDATION_UPDATED_NOTE as validation_updated_note, " +
		"ta.LEXINGTON_VERSION, " +
		"ta.TUTORIAL_ID, " +
		"st.USER_NAME, " +
		"st.LAST_NAME, " +
		"st.FIRST_NAME, " +
		"st.MIDDLE_NAME, " +
		"st.PREFIX, " +
		"st.SUFFIX, " +
		"st.BIRTHDATE, " +
		"st.GENDER, " +
		"st.ETHNICITY, " +
		"st.EMAIL, " +
		"st.GRADE, " +
		"round(months_between(sysdate,st.birthdate)/12) as age, " +
		"st.EXT_PIN1, " +
		"st.EXT_PIN2, " +
		"st.EXT_PIN3, " +
		"st.EXT_SCHOOL_ID, " +
		"st.EXT_ELM_ID, " +
		"tr.org_node_id as student_org_node_id, " +
		"cust.CUSTOMER_NAME, " +
		"sysdate, " +
		"ta.CREATOR_ORG_NODE_ID, " +
		"ond.org_node_id as parent_org_node_id, " +
		"on2.org_node_id as grandparent_org_node_id, " +
		"on3.org_node_name as creator_org_node_name, " +
		"ond.org_node_name as parent_org_node_name, " +
		"on2.org_node_name as grandparent_org_node_name, " +
		"sc.CONTACT_NAME, " +
		"sc.CONTACT_TYPE, " +
		"sc.CONTACT_EMAIL, " +
		"sc.STREET_LINE1, " +
		"sc.STREET_LINE2, " +
		"sc.STREET_LINE3, " +
		"sc.CITY, " +
		"sc.STATEPR, " +
		"sc.COUNTRY, " +
		"sc.ZIPCODE, " +
		"sc.PRIMARY_PHONE, " +
		"sc.SECONDARY_PHONE, " +
		"sc.FAX, " +
		"ta.created_by, " +
		"ta.created_date_time, " +
		"trim(users.first_name) || ' ' || trim(users.last_name) as scheduler_user_name " +
		"from test_catalog tc, " +
		"     test_admin ta, " +
		"	 product pr, " +
		"	 student st, " +
		"	 org_node_student ons, " +
		"	 student_contact sc, " +
		"	 customer cust, " +
		"	 users, " +
		"	 test_roster tr, " +
		"	 org_node_parent onp, " +
		"	 org_node ond, " +
		"	 org_node_parent onp2, " +
		"	 org_node on2, " +
		"	 org_node on3, " +
        "    student_item_set_status siss " +
	    "where tc.test_catalog_id = ta.test_catalog_id " +
		"  and ta.test_admin_id = tr.test_admin_id " +
		"  and ta.product_id = pr.product_id " +
		"  and st.student_id = tr.student_id " +
		"  and cust.customer_id = ta.customer_id " +
		"  and users.user_id = ta.created_by " +
		"  and st.student_id = sc.student_id (+) " +
		"  and tr.test_roster_id = ? " +
		"  and ta.creator_org_node_id = onp.org_node_id (+) " +
		"  and onp.parent_org_node_id = ond.org_node_id (+) " +  
		"  and ond.org_node_id = onp2.org_node_id (+) " +
		"  and onp2.parent_org_node_id = on2.org_node_id (+) " + 
		"  and on3.org_node_id = ta.creator_org_node_id " + 
		"  and ons.student_id = st.student_id " +
        "  and siss.test_Roster_id = tr.test_Roster_id " +
        " group by " +
        "tr.test_roster_id, " +
		"tr.customer_id, " +
		"tr.test_admin_id, " +
		"tr.test_completion_status, " +
		"tr.scoring_status, " +
		"tr.capture_method, " +
		"tr.student_id, " +
		"tr.start_date_time, " +
		"tc.TEST_NAME, " +
		"ta.TEST_ADMIN_NAME, " +
		"tc.TEST_CATALOG_ID, " +
		"tc.ITEM_SET_ID, " +
		"ta.PRODUCT_ID, " +
		"pr.PRODUCT_NAME, " +
        "pr.PRODUCT_TYPE, " +
		"tr.VALIDATION_STATUS, " +
		"tr.VALIDATION_UPDATED_BY, " +
		"tr.VALIDATION_UPDATED_DATE_TIME, " +
		"tr.VALIDATION_UPDATED_NOTE, " +
		"ta.LEXINGTON_VERSION, " +
		"ta.TUTORIAL_ID, " +
		"st.USER_NAME, " +
		"st.LAST_NAME, " +
		"st.FIRST_NAME, " +
		"st.MIDDLE_NAME, " +
		"st.PREFIX, " +
		"st.SUFFIX, " +
		"st.BIRTHDATE, " +
		"st.GENDER, " +
		"st.ETHNICITY, " +
		"st.EMAIL, " +
		"st.GRADE, " +
		"round(months_between(sysdate,st.birthdate)/12), " +
		"st.EXT_PIN1, " +
		"st.EXT_PIN2, " +
		"st.EXT_PIN3, " +
		"st.EXT_SCHOOL_ID, " +
		"st.EXT_ELM_ID, " +
		"tr.org_node_id, " +
		"cust.CUSTOMER_NAME, " +
		"sysdate, " +
		"ta.CREATOR_ORG_NODE_ID, " +
		"ond.org_node_id, " +
		"on2.org_node_id, " +
		"on3.org_node_name, " +
		"ond.org_node_name, " +
		"on2.org_node_name, " +
		"sc.CONTACT_NAME, " +
		"sc.CONTACT_TYPE, " +
		"sc.CONTACT_EMAIL, " +
		"sc.STREET_LINE1, " +
		"sc.STREET_LINE2, " +
		"sc.STREET_LINE3, " +
		"sc.CITY, " +
		"sc.STATEPR, " +
		"sc.COUNTRY, " +
		"sc.ZIPCODE, " +
		"sc.PRIMARY_PHONE, " +
		"sc.SECONDARY_PHONE, " +
		"sc.FAX, " +
		"ta.created_by, " +
		"ta.created_date_time, " +
		"trim(users.first_name) || ' ' || trim(users.last_name) " +
		"  order by student_org_node_id desc";
}