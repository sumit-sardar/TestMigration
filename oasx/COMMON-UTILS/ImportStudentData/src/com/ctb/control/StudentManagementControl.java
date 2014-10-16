package com.ctb.control;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ctb.bean.UploadStudent;
import com.ctb.dao.IStudentManagementDAO;
import com.ctb.dao.StudentManagementDAO;
import com.ctb.utils.cache.StudentNewRecordCacheImpl;
import com.ctb.utils.cache.StudentUpdateRecordCacheImpl;

/**
 * Class used for Student Related Data Transactions Control Handling.
 * 
 * @author TCS
 * 
 */
public class StudentManagementControl {
	private static Logger logger = Logger
			.getLogger(StudentManagementControl.class.getName());
	final int BATCH_SIZE = 998;
	private IStudentManagementDAO studentManagement = new StudentManagementDAO();

	/**
	 * Class used for Populating the Unique user-names for Each
	 * Student,Assigning Sequence Generated ids for each student. The Student
	 * Record is then inserted in Student Table, Accommodations,demographics and
	 * Org-node-student association is populated.
	 * 
	 * @param newStdRecordCacheImpl
	 *            - Cache having student records for Insertion
	 * @param studentUserNames
	 *            - Set having User-names of Students
	 * @param studentIdExtPinMap
	 *            - Map of ExtPin1 and StudentId
	 * @throws Exception
	 */
	public void executeStudentCreation(
			StudentNewRecordCacheImpl newStdRecordCacheImpl,
			Set<String> studentUserNames,
			Map<String, Integer> studentIdExtPinMap) throws Exception {
		StringBuilder inClause = new StringBuilder();
		boolean firstValue = true;
		int newStudentCount = 0;
		for (String username : studentUserNames) {
			if (firstValue) {
				firstValue = false;
				inClause.append("like ('");
			} else {
				inClause.append(" or USER_NAME like  ('");
			}
			inClause.append(username);
			inClause.append("%')");
			newStudentCount++;
			if (newStudentCount % 50 == 0) {
				firstValue = true;
				inClause.append("#");
			}
		}
		studentManagement.populateActualStudentUserName(newStdRecordCacheImpl,
				inClause.toString(), new Integer(newStudentCount));
		studentManagement.populateActualStudentIds(newStdRecordCacheImpl,
				new Integer(newStudentCount), studentIdExtPinMap);

		studentManagement.insertStudentDetails(newStdRecordCacheImpl);
		studentManagement.createOrgnodeStudent(newStdRecordCacheImpl);
		/** Commented out because of story : OAS-636 & OAS-637 **/
		studentManagement.createStudentAccommodations(newStdRecordCacheImpl);//Uncommented for defect #80711 : Populating accommodations for new student with default values
		studentManagement.createStudentDemographicData(newStdRecordCacheImpl);

	}

	/**
	 * Class used for update operation for Students
	 * 
	 * @param updateStdRecordCacheImpl
	 *            - Cache having student records for Update
	 * @param customerId
	 *            - Customer-id
	 * @param studentIdExtPinMap
	 *            - Map of ExtPin1 and StudentId
	 * @throws Exception
	 */
	public void executeStudentUpdate(
			StudentUpdateRecordCacheImpl updateStdRecordCacheImpl,
			Integer customerId, Map<String, Integer> studentIdExtPinMap)
			throws Exception {

		studentManagement.updateStudent(updateStdRecordCacheImpl,
				studentIdExtPinMap);
		updateOrgNodeStudent(updateStdRecordCacheImpl, customerId);
 		/** Commented out because of story : OAS-636 & OAS-637 **/ 
		// updateAccommodation(updateStdRecordCacheImpl);
		updateStudentDemographicData(updateStdRecordCacheImpl);
		System.gc();

	}

	/**
	 * Class used for Update of Org-node-student Association
	 * 
	 * @param updateStdRecordCacheImpl
	 *            - Cache having student records for Update
	 * @param customerId
	 *            - Customer-id
	 * @throws Exception
	 */
	private void updateOrgNodeStudent(
			StudentUpdateRecordCacheImpl updateStdRecordCacheImpl,
			Integer customerId) throws Exception {

		try {

			studentManagement
					.createOrgnodeStudentDuringUpdate(updateStdRecordCacheImpl);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			logger.info("updateOrgNodeStudent() completed");
		}

	}

	/**
	 * Class used for Accommodation update
	 * 
	 * @param updateStdRecordCacheImpl
	 *            - Cache having student records for Update
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private void updateAccommodation(
			StudentUpdateRecordCacheImpl updateStdRecordCacheImpl)
			throws Exception {

		studentManagement.updateAccommodation(updateStdRecordCacheImpl);

	}

	/**
	 * Class used for Demographic Data update of students
	 * 
	 * @param updateStdRecordCacheImpl
	 *            - Cache having student records for Update
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void updateStudentDemographicData(
			StudentUpdateRecordCacheImpl updateStdRecordCacheImpl)
			throws Exception {
		StringBuilder studentIds = new StringBuilder();
		StringBuilder studentAndDemoIds = new StringBuilder();
		boolean firstValue = true;
		int counter = 0;
		List<String> keys = updateStdRecordCacheImpl.getKeys();
		for (String key : keys) {
			UploadStudent upload = ((UploadStudent) updateStdRecordCacheImpl
					.getUpdatedStudent(key));

			Integer studentId = upload.getManageStudent().getId();
			if (firstValue) {
				firstValue = false;
			} else {
				studentIds.append(",");
			}
			studentIds.append(studentId);
			counter++;
			if (counter % BATCH_SIZE == 0) {
				studentIds.append("#");
				studentAndDemoIds.append("#");
				firstValue = true;
			}
		}

		if (null != studentIds && studentIds.length() > 0)
			studentManagement
					.deleteVisibleStudentDemographicDataForStudent(studentIds
							.toString());

		if (updateStdRecordCacheImpl.getCacheSize() > 0)
			studentManagement
					.createStudentDemographicDataDuringUpdate(updateStdRecordCacheImpl);

		logger.info("updateStudentDemographicData() done executing");
	}
}
