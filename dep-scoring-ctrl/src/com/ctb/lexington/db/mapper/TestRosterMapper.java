package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

import com.ctb.lexington.db.data.ScoringInvokeRecord;
import com.ctb.lexington.db.data.TestRosterRecord;
import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCItemFactData;
import com.ctb.lexington.db.record.Persistent;
import com.ctb.lexington.domain.teststructure.CompletionStatus;
import com.ctb.lexington.domain.teststructure.ScoringStatus;
import com.ctb.lexington.util.mosaicobject.MSSAuditLogVO;
import com.ibatis.sqlmap.client.SqlMapClient;

public class TestRosterMapper extends AbstractDBMapper {
    public static final String FIND_TESTROSTER = "findTestRoster";
    public static final String UPDATE_SCORING_STATUS = "updateRosterScoringStatus";
    public static final String UPDATE_TEST_COMPLETION_STATUS = "updateRosterTestCompletionStatus";
    public static final String INSERT_MOSAIC_ERROR_ROSTER = "insertMosaicErrorRecord";
    public static final String UPDATE_MOSAIC_ERROR_ROSTER = "updateMosaicErrorRecord";
    public static final String UPDATE_MOSAIC_ERROR_SUCCESS = "updateMosaicErrorRecordSuccess";
    
    public TestRosterMapper(Connection conn) {
        super(conn);
    }

    public TestRosterRecord findTestRoster(Long testRosterId)
    {
        Persistent record = find(FIND_TESTROSTER, testRosterId);

        if (record == null)
             throw new IllegalArgumentException("Test Roster with ID " + testRosterId + " not found in TEST_ROSTER table.");
        return (TestRosterRecord)record;
    }

    public void updateScoringStatus(Long testRosterId, ScoringStatus status) {
        try {
        	TestRosterRecord roster = new TestRosterRecord();
            roster.setTestRosterId(testRosterId);
            roster.setScoringStatus(status.getCode());
            update(UPDATE_SCORING_STATUS,roster);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void updateTestCompletionStatus(Long testRosterId, CompletionStatus status, Timestamp completionTime) {
        try {
        	TestRosterRecord roster = new TestRosterRecord();
            roster.setTestRosterId(testRosterId);
            roster.setTestCompletionStatus(status.getCode());
            roster.setCompletionTime(completionTime);
            update(UPDATE_TEST_COMPLETION_STATUS,roster);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void insertMosaicErrorRecord(ScoringInvokeRecord record) {
        try {
            insert(INSERT_MOSAIC_ERROR_ROSTER,record);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
	public void updateMosaicErrorRecord(ScoringInvokeRecord record) {
        try {
            update(UPDATE_MOSAIC_ERROR_ROSTER,record);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
	
	public void updateMosaicErrorRecordSuccess(ScoringInvokeRecord record) {
        try {
            update(UPDATE_MOSAIC_ERROR_SUCCESS,record);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
	
}