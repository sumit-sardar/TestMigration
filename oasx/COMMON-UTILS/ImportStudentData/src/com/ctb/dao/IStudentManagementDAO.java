package com.ctb.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ctb.bean.OrgNodeStudent;
import com.ctb.bean.OrganizationNode;
import com.ctb.bean.StudentAccommodations;
import com.ctb.bean.StudentDemographicData;
import com.ctb.bean.StudentDemographicValue;
import com.ctb.bean.UploadStudent;

public interface IStudentManagementDAO {
	
	void populateActualStudentUserName(List<UploadStudent> finalStudentList, String userNames, Integer studentCount) throws Exception;
	List<UploadStudent> populateActualStudentIds(List<UploadStudent> finalStudentList, Integer newStudentCount) throws Exception;
	void insertStudentDetails(List<UploadStudent> finalStudentList) throws Exception;
	void createStudentAccommodations(List<UploadStudent> finalStudentList) throws Exception;
	void createStudentDemographicData(List<UploadStudent> finalStudentList) throws Exception;
	void createOrgnodeStudent(List<UploadStudent> finalStudentList) throws Exception;

	void populateStudentOrgNodes(String inClause, Map<Integer, ArrayList<OrganizationNode>> studentOrgMap, Integer customerId) throws Exception;
	void populateStudentAccommodation(String inClause, Map<Integer, StudentAccommodations> studentAccomMap) throws Exception;
	void populateStudentDemoValue(String studentIds, String demographicIds, Map<Integer, HashMap<Integer, ArrayList<StudentDemographicValue>>> studentDemoMap) throws Exception;
	void updateStudent(List<UploadStudent> finalStudentList, Map<Integer, ArrayList<OrganizationNode>> studentOrgMap) throws Exception;
	void updateAccommodation(List<UploadStudent> finalStudentList) throws Exception;
	void deleteVisibleStudentDemographicDataForStudent(String string) throws Exception;
	void deleteStudentDemographicDataForStudentAndCustomerDemographic(String string) throws Exception;
	void createStudentDemographicDataDuringUpdate(List<StudentDemographicData> insertStudentDemoList) throws Exception;
	void setRosterUpdateFlag(String string) throws Exception;
	void createOrgnodeStudentDuringUpdate(List<OrgNodeStudent> newOrgStudentList) throws Exception;

}
