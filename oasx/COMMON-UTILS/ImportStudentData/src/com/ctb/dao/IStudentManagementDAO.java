package com.ctb.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.ctb.bean.ManageStudent;
import com.ctb.bean.OrganizationNode;
import com.ctb.bean.StudentAccommodations;
import com.ctb.bean.StudentDemographicData;
import com.ctb.bean.StudentDemographicValue;
import com.ctb.bean.UploadStudent;

public interface IStudentManagementDAO {
	
	OrganizationNode [] getAssignedOrganizationNodesForStudentAtAndBelowUserTopNodes(int studentId) throws SQLException;
	StudentAccommodations getStudentAccommodations(Integer studentId) throws SQLException;
	void setRosterUpdateFlag(Integer studentId) throws SQLException;
	void deleteVisibleStudentDemographicDataForStudent(Integer studentId) throws SQLException;
	Integer getNextPKForStudentDemographicData() throws SQLException;
	
	Integer [] getTopOrgNodeIdsForUser(String username) throws SQLException;
	void updateStudent(ManageStudent student, Date updatedDateTime) throws SQLException;
	void deleteStudentDemographicDataForStudentAndCustomerDemographic(Integer studentId, Integer customerDemographicId) throws SQLException;
	StudentDemographicValue [] getStudentDemographicValues(int customerDemographicId, int studentId) throws SQLException;
	
	void populateActualStudentUserName(List<UploadStudent> finalStudentList, String userNames, Integer studentCount) throws Exception;
	List<UploadStudent> populateActualStudentIds(List<UploadStudent> finalStudentList, Integer newStudentCount) throws Exception;
	
	void insertStudentDetails(List<UploadStudent> finalStudentList) throws Exception;
	void createStudentAccommodations(List<UploadStudent> finalStudentList) throws Exception;
	void createStudentDemographicData(List<UploadStudent> finalStudentList) throws Exception;
	void createOrgnodeStudent(List<UploadStudent> finalStudentList) throws Exception;


}
