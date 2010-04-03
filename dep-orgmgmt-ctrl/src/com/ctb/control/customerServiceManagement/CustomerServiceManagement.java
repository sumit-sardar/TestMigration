package com.ctb.control.customerServiceManagement;

import org.apache.beehive.controls.api.bean.ControlInterface;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.AuditFileReopenSubtest;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;

@ControlInterface()

public interface CustomerServiceManagement {

	com.ctb.bean.testAdmin.Student getStudentDetail(java.lang.String loginUserName,java.lang.String studentLoginId) throws CTBBusinessException;
	
	com.ctb.bean.testAdmin.TestSessionData getStudentTestSessionData(java.lang.String loginUserName,java.lang.Integer studentId, String  accessCode, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException;
	
	com.ctb.bean.testAdmin.StudentSessionStatusData getSubtestListForStudent(java.lang.Integer rosterId, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException;
	
	com.ctb.bean.testAdmin.ScheduleElementData   getSubTestListForTestSession(java.lang.String loginUserName,java.lang.String accessCode) throws CTBBusinessException ;
	com.ctb.bean.testAdmin.StudentSessionStatusData getStudentListForSubTest(java.lang.Integer testAdminId, java.lang.Integer itemSetId,FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException;
	void reopenSubtest(AuditFileReopenSubtest [] auditFileReopenSubtest,User user)	throws CTBBusinessException;
}