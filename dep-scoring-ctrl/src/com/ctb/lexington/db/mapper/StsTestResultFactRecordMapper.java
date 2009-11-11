package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.data.StsTestResultFactDetails;

public class StsTestResultFactRecordMapper extends AbstractDBMapper {
    private static final String FIND_NAME = "findStsTestResultFact";
    private static final String INSERT_NAME = "insertStsTestResultFact";
    private static final String UPDATE_NAME = "updateStsTestResultFact";
    private static final String FIND_BY_STUDENT_ADMIN_TEST_NAME = "findStsTestResultFactByStudentAdminTest";

    /**
     * @param conn
     */
    public StsTestResultFactRecordMapper(Connection conn) {
        super(conn);
    }

    public StsTestResultFactDetails find(Long factId) {
        StsTestResultFactDetails template = new StsTestResultFactDetails();
        template.setFactId(factId);
        return (StsTestResultFactDetails) find(FIND_NAME, template);
    }

    public StsTestResultFactDetails findByStudentAdminTest(Long studentId, Long adminId,
            String contentAreaName) {
        StsTestResultFactDetails template = new StsTestResultFactDetails();
        template.setContentAreaName(contentAreaName);
        template.setStudentDimId(studentId);
        template.setAdminDimId(adminId);

        return (StsTestResultFactDetails) find(FIND_BY_STUDENT_ADMIN_TEST_NAME, template);
    }

    /**
     * @param record
     */
    public void upsert(StsTestResultFactDetails record, boolean updateContextData) throws SQLException {
        StsTestResultFactDetails existing = findByStudentAdminTest(
        		record.getStudentDimId(),
				record.getAdminDimId(),
				record.getContentAreaName());
        if (existing == null) {
            insert(INSERT_NAME, record);
            StsTestResultFactDetails newFact = findByStudentAdminTest(
            		record.getStudentDimId(),
					record.getAdminDimId(),
					record.getContentAreaName());
            Long factId = newFact.getFactId();
            record.setFactId(factId);
        } else {
            record.setFactId(existing.getFactId());
            if(!updateContextData) {
            	record.setStudentDimId(existing.getStudentDimId());
            	record.setStudentDimVersionId(existing.getStudentDimVersionId());
            	record.setOrgNodeDimId(existing.getOrgNodeDimId());
            	record.setOrgNodeDimVersionId(existing.getOrgNodeDimVersionId());
            	record.setDemographicDimId(existing.getDemographicDimId());
            }
            update(UPDATE_NAME, record);
        }
    }
}