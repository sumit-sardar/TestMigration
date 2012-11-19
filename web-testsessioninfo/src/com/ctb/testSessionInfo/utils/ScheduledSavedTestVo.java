package com.ctb.testSessionInfo.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ctb.bean.testAdmin.ScheduledSession;
import com.ctb.bean.testAdmin.SessionStudent;
import com.ctb.bean.testAdmin.TestElement;
import com.ctb.testSessionInfo.dto.TestSessionVO;
import com.ctb.testSessionInfo.dto.UserProfileInformation;
import com.ctb.util.OperationStatus;

@SuppressWarnings("all")
public class ScheduledSavedTestVo implements Serializable {

	private ScheduledSession savedTestDetails = null;
	private List<SessionStudent> savedStudentsDetails = null;
	private  List<UserProfileInformation> savedProctorsDetails = null;
	private ScheduleTestVo productsDetails = null;
	private boolean testSessionExpired  = false;
	private String userRoleName = "";
	private List<ObjectIdName> testZoneDropDownList = new ArrayList<ObjectIdName>();
	private List<ObjectIdName> topNodeDropDownList = new ArrayList<ObjectIdName>();
	private OperationStatus status;
	private String productType = TestSessionUtils.GENERIC_PRODUCT_TYPE;
	private String minLoginEndDate = "";
	private SessionStudent[] restrictedStudents;
	private Integer totalStudent ;
	private String userTimeZone = "";
	private boolean hasLocator = false;
	private Integer locatorId = null;
	Map<Integer,Map> accomodationMap;
	private boolean isCopySession = false;
    private String startDate;
	private String endDate;
	private TestSessionVO testSession;
	private boolean isOkAdmin = false;
	private boolean forceTestBreak = false;
	private Boolean selectGE = false;
	
	private static final long serialVersionUID = 1L;
	
	
	public TestSessionVO getTestSession() {
		return testSession;
	}

	public void setTestSession(TestSessionVO testSession) {
		this.testSession = testSession;
	}

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
	public void setSavedStudentsDetails(List<SessionStudent> savedStudentsDetails) {
		this.savedStudentsDetails = savedStudentsDetails;
		
	}
	public void setSavedProctorsDetails(List<UserProfileInformation> savedProctorsDetails) {
		this.savedProctorsDetails =savedProctorsDetails;
		
	}
	public void setUserProductsDetails(ScheduleTestVo productsDetails) {
		this.productsDetails = productsDetails;
		
	}
	public void populateTimeZone() {
		List<String> timeZoneList = DateUtils.getTimeZoneList(); 
		ObjectIdName userTimeZoneObj = new ObjectIdName(this.userTimeZone,this.userTimeZone);
		Boolean contains = false;
		for(String val : timeZoneList){
			testZoneDropDownList.add(new ObjectIdName(val,val));	
			if(val == userTimeZoneObj.getName()){
				contains = true;
			}
		}
		if(!contains){
			testZoneDropDownList.add(userTimeZoneObj);	
		}
	}
	
	public void populateTopOrgnode(Map<Integer, String> topNodesMap) {
		for(Map.Entry<Integer, String> entry : topNodesMap.entrySet()) {
			topNodeDropDownList.add(new ObjectIdName(entry.getKey().toString(), entry.getValue()));
		}		
	}

	
	/**
	 * @return the productType
	 */
	public String getProductType() {
		return productType;
	}

	
	/**
	 * @param productType the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}

	

	
	/**
	 * @return the minLoginEndDate
	 */
	public String getMinLoginEndDate() {
		return minLoginEndDate;
	}

	
	/**
	 * @param minLoginEndDate the minLoginEndDate to set
	 */
	public void setMinLoginEndDate(String minLoginEndDate) {
		this.minLoginEndDate = minLoginEndDate;
	}

		
	/**
	 * @return the restrictedStudents
	 */
	public SessionStudent[] getRestrictedStudents() {
		return restrictedStudents;
	}

	
	/**
	 * @param restrictedStudents the restrictedStudents to set
	 */
	public void setRestrictedStudents(SessionStudent[] restrictedStudents) {
		this.restrictedStudents = restrictedStudents;
	}

	
	/**
	 * @return the totalStudent
	 */
	public Integer getTotalStudent() {
		return totalStudent;
	}

	
	/**
	 * @param totalStudent the totalStudent to set
	 */
	public void setTotalStudent(Integer totalStudent) {
		this.totalStudent = totalStudent;
	}

	/**
	 * @return the userTimeZone
	 */
	public String getUserTimeZone() {
		return userTimeZone;
	}

	/**
	 * @param userTimeZone the userTimeZone to set
	 */
	public void setUserTimeZone(String userTimeZone) {
		this.userTimeZone = userTimeZone;
	}

	public void setHasLocator(boolean hasLocator) {
		this.hasLocator = hasLocator;
	}

	public void setLocatorId(Integer locatorId) {
		this.locatorId = locatorId;
	}

	public void populateLocatorInformation() {
		
		if(this.savedTestDetails != null && this.savedTestDetails.getScheduledUnits()!=null ) {
			for( TestElement elem : this.savedTestDetails.getScheduledUnits()) {
				if(elem.getItemSetName().indexOf("Locator") > 0){
					setHasLocator(true);
					setLocatorId(elem.getItemSetId());
				}
				
			}
			
			
		}
		
	}

	public void setSavedTestDetailsWithDefaultValue( ScheduledSession savedTestDetails) {
		TestElement[] tes = savedTestDetails.getScheduledUnits();
		List<TestElement> list = new ArrayList<TestElement>(tes.length);
		for (TestElement te : tes ) {
			if(te.getSessionDefault().equalsIgnoreCase("T")){
				list.add(te);
			}
		}
		 
		tes = list.toArray(new TestElement[list.size()]);
		savedTestDetails.setScheduledUnits(tes);
		this.savedTestDetails = savedTestDetails;
		
	}

	public Map<Integer, Map> getAccomodationMap() {
		return accomodationMap;
	}

	public void setAccomodationMap(Map<Integer, Map> accomodationMap) {
		this.accomodationMap = accomodationMap;
	}

	public boolean isCopySession() {
		return isCopySession;
	}

	public void setCopySession(boolean isCopySession) {
		this.isCopySession = isCopySession;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
		
	}

	public void setEndDate(String endDate) {
		this.endDate=endDate;
		
	}

	public boolean isOkAdmin() {
		return isOkAdmin;
	}

	public void setOkAdmin(boolean isOkAdmin) {
		this.isOkAdmin = isOkAdmin;
	}

	public boolean isForceTestBreak() {
		return forceTestBreak;
	}


	public void setForceTestBreak(boolean forceTestBreak) {
		this.forceTestBreak = forceTestBreak;
	}
	
	public Boolean isSelectGE() {
		return selectGE;
	}

	public void setSelectGE(Boolean selectGE) {
		this.selectGE = selectGE;
	}

}
