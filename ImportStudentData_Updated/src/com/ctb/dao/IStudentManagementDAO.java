package com.ctb.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ctb.bean.StudentAccommodations;
import com.ctb.bean.StudentDemographicValue;
import com.ctb.utils.cache.StudentNewRecordCacheImpl;
import com.ctb.utils.cache.StudentUpdateRecordCacheImpl;

public interface IStudentManagementDAO {
	
	void populateActualStudentUserName(StudentNewRecordCacheImpl newStdRecordCacheImpl, String userNames, Integer studentCount) throws Exception;
	void populateActualStudentIds(StudentNewRecordCacheImpl newStdRecordCacheImpl, Integer newStudentCount , Map<String,Integer> studentIdExtPinMap) throws Exception;
	void insertStudentDetails(StudentNewRecordCacheImpl newStdRecordCacheImpl) throws Exception;
	void createStudentAccommodations(StudentNewRecordCacheImpl newStdRecordCacheImpl) throws Exception;
	void createStudentDemographicData(StudentNewRecordCacheImpl newStdRecordCacheImpl) throws Exception;
	void createOrgnodeStudent(StudentNewRecordCacheImpl newStdRecordCacheImpl) throws Exception;

	void populateStudentAccommodation(String inClause, Map<Integer, StudentAccommodations> studentAccomMap) throws Exception;
	void populateStudentDemoValue(String studentIds, String demographicIds, Map<Integer, HashMap<Integer, ArrayList<StudentDemographicValue>>> studentDemoMap) throws Exception;
	void updateStudent(StudentUpdateRecordCacheImpl updateStdRecordCacheImpl,   Map<String,Integer> studentIdExtPinMap ) throws Exception;
	void updateAccommodation(StudentUpdateRecordCacheImpl updateStdRecordCacheImpl) throws Exception;
	void deleteVisibleStudentDemographicDataForStudent(String string) throws Exception;
	void deleteStudentDemographicDataForStudentAndCustomerDemographic(String string) throws Exception;
	void createStudentDemographicDataDuringUpdate(StudentUpdateRecordCacheImpl updateStdRecordCacheImpl) throws Exception;
	void setRosterUpdateFlag(String string) throws Exception;
	void createOrgnodeStudentDuringUpdate(StudentUpdateRecordCacheImpl updateStdRecordCacheImpl) throws Exception;

}
