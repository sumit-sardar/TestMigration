package com.mhe.ctb.oas.BMTSync.spring.jms;

import java.util.Calendar;

/**
 * Provides a datum implementation of STUDENT_MESSAGE_TYP
 * 
 * CREATE TYPE STUDENT_MESSAGE_TYP AS OBJECT ( STUDENT_ID INTEGER, CUSTOMER_ID
 * NUMBER, UPDATED_DATE_TIME DATE );
 *
 * @author cparis
 *
 */
public class StudentMessageType implements EnqueueableMessage {

	private Integer _studentId;
	private Integer _customerId;
	private Calendar _updatedDateTime;

	public Integer getStudentId() {
		return _studentId;
	}

	public void setStudentId(Integer studentId) {
		_studentId = studentId;
	}

	public void setCustomerId(Integer customerId) {
		_customerId = customerId;
	}

	public Calendar getUpdatedDateTime() {
		return _updatedDateTime;
	}

	public void setUpdatedDateTime(Calendar updatedDateTime) {
		_updatedDateTime = updatedDateTime;
	}

	@Override
	public String getPrimaryKeyName() {
		return "studentId";
	}

	@Override
	public String getPrimaryKeyValue() {
		if (_studentId == null) {
			return null;
		}
		return _studentId.toString();
	}

	@Override
	public String getSecondaryKeyName() {
		return "customerId";
	}

	@Override
	public String getSecondaryKeyValue() {
		if (_customerId == null) {
			return null;
		}
		return _customerId.toString();
	}

	@Override
	public String getLogDetails() {
		return String.format(" [%s=%s,%s=%s]",
				getPrimaryKeyName(), getPrimaryKeyValue(),
				getSecondaryKeyName(), getSecondaryKeyValue());
	}

	@Override
	public Integer getCustomerId() {
		return _customerId;
	}

}
