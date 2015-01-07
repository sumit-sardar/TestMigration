package com.mhe.ctb.oas.BMTSync.spring.jms;

import java.util.Calendar;

/**
 * Provides a datum implementation of STUDENT_MESSAGE_TYP
 * 
 * create or replace TYPE BMTSYNC_ASSIGNMENT_TYP IS OBJECT  (
 *     CUSTOMER_ID       INTEGER,
 *     TEST_ADMIN_ID     INTEGER,
 *     STUDENT_ID        INTEGER,
 *     TEST_ROSTER_ID    INTEGER,
 *     UPDATED_DATE_TIME DATE
 * )
 */
public class TestAssignmentMessageType implements EnqueueableMessage {

	private Integer _studentId;
	private Integer _customerId;
	private Integer _testAdminId;
	private Integer _testRosterId;
	private Calendar _updatedDateTime;

	public Integer getStudentId() {
		return _studentId;
	}

	public void setStudentId(Integer studentId) {
		_studentId = studentId;
	}

	@Override
	public Integer getCustomerId() {
		return _customerId;
	}

	public void setCustomerId(Integer customerId) {
		_customerId = customerId;
	}

	public Integer getTestAdminId() {
		return _testAdminId;
	}

	public void setTestAdminId(Integer testAdminId) {
		this._testAdminId = testAdminId;
	}

	public Integer getTestRosterId() {
		return _testRosterId;
	}

	public void setTestRosterId(Integer _testRosterId) {
		this._testRosterId = _testRosterId;
	}

	public Calendar getUpdatedDateTime() {
		return _updatedDateTime;
	}

	public void setUpdatedDateTime(Calendar updatedDateTime) {
		_updatedDateTime = updatedDateTime;
	}

	@Override
	public String getPrimaryKeyName() {
		return "testAdminId";
	}

	@Override
	public String getPrimaryKeyValue() {
		if (_testAdminId == null) {
			return null;
		}
		return _testAdminId.toString();
	}

	@Override
	public String getSecondaryKeyName() {
		return "studentId";
	}

	@Override
	public String getSecondaryKeyValue() {
		if (_studentId == null) {
			return null;
		}
		return _studentId.toString();
	}

	@Override
	public String getLogDetails() {
		return String.format(" [%s=%s,%s=%s]",
				getPrimaryKeyName(), getPrimaryKeyValue(),
				getSecondaryKeyName(), getSecondaryKeyValue());
	}

	@Override
	public String getMessageType() {
		return "TestAssignment";
	}
}
