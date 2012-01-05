package com.ctb.testSessionInfo.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ctb.bean.testAdmin.ScheduledSession;
import com.ctb.util.OperationStatus;

@SuppressWarnings("all")
public class ScheduledSavedTestVo implements Serializable {

	private ScheduledSession savedTestDetails = null;
	private ScheduledSession savedStudentsDetails = null;
	private ScheduledSession savedProctorsDetails = null;
	private ScheduleTestVo productsDetails = null;
	private boolean testSessionExpired  = false;
	private String userRoleName = "";
	private List<ObjectIdName> testZoneDropDownList = new ArrayList<ObjectIdName>();
	private List<ObjectIdName> topNodeDropDownList = new ArrayList<ObjectIdName>();
	private OperationStatus status;

	private static final long serialVersionUID = 1L;

	public void setSavedTestDetails(ScheduledSession savedTestDetails) {
		this.savedTestDetails = savedTestDetails;
	}

	public void setTestSessionExpired(boolean testSessionExpired) {
		this.testSessionExpired = testSessionExpired;
		
	}

	public void setUserRole(String userRoleName) {
		this.userRoleName  = userRoleName;
		
	}

	public void setOperationStatus(OperationStatus status) {
		this.status= status;
		
	}

	public void setSavedStudentsDetails(ScheduledSession savedStudentsDetails) {
		this.savedStudentsDetails = savedStudentsDetails;
		
	}

	public void setSavedProctorsDetails(ScheduledSession savedProctorsDetails) {
		this.savedProctorsDetails =savedProctorsDetails;
		
	}

	public void setUserProductsDetails(ScheduleTestVo productsDetails) {
		this.productsDetails = productsDetails;
		
	}
	public void populateTimeZone() {
		List<String> timeZoneList = DateUtils.getTimeZoneList(); 
		for(String val : timeZoneList){
			testZoneDropDownList.add(new ObjectIdName(val,val));
		}
		
		
	}
	public void populateTopOrgnode(Map<Integer, String> topNodesMap) {
		for(Map.Entry<Integer, String> entry : topNodesMap.entrySet()) {
			topNodeDropDownList.add(new ObjectIdName(entry.getKey().toString(), entry.getValue()));
		}
		
	}

}
