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
public class StudentMessageType {

	private Integer _studentId;
	private Integer _customerId;
	private Calendar _updatedDateTime;

	public Integer getStudentId() {
		return _studentId;
	}

	public void setStudentId(Integer studentId) {
		_studentId = studentId;
	}

	public Integer getCustomerId() {
		return _customerId;
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

}
