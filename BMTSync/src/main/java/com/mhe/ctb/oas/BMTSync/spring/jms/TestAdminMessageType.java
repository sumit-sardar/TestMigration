package com.mhe.ctb.oas.BMTSync.spring.jms;

/**
 * Provides a datum implementation of STUDENT_MESSAGE_TYP
 * 
 * create or replace TYPE BMTSYNC_TESTADMIN_TYP IS OBJECT  (
 * CUSTOMER_ID       INTEGER,
 * TEST_ADMIN_ID     INTEGER
 * )
 */
public class TestAdminMessageType implements EnqueueableMessage {

	private Integer _customerId;
	private Integer _testAdminId;

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
	public String getMessageType() {
		return "TestAdmin";
	}
}
