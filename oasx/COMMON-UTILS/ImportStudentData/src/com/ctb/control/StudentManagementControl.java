package com.ctb.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ctb.bean.ManageStudent;
import com.ctb.bean.OrgNodeStudent;
import com.ctb.bean.OrganizationNode;
import com.ctb.bean.StudentAccommodations;
import com.ctb.bean.StudentDemoGraphics;
import com.ctb.bean.StudentDemographicData;
import com.ctb.bean.StudentDemographicValue;
import com.ctb.bean.UploadStudent;
import com.ctb.dao.IStudentManagementDAO;
import com.ctb.dao.StudentManagementDAO;
import com.ctb.utils.Constants;
import com.ctb.utils.cache.StudentNewRecordCacheImpl;
import com.ctb.utils.cache.StudentUpdateRecordCacheImpl;

public class StudentManagementControl {
	private static Logger logger = Logger.getLogger(StudentManagementControl.class.getName());
	final int BATCH_SIZE = 998;
	private IStudentManagementDAO studentManagement=new StudentManagementDAO();
	
	public void executeStudentCreation(StudentNewRecordCacheImpl newStdRecordCacheImpl, Set<String> studentUserNames ,
										Map<String,Integer> studentIdExtPinMap) throws Exception {
		StringBuilder inClause = new StringBuilder();
		boolean firstValue = true;
		int newStudentCount = 0;
		for(String username : studentUserNames){
				if ( firstValue ) {
					firstValue = false;
					inClause.append("like ('");
				} else {
					inClause.append(" or USER_NAME like  ('");
				}
				inClause.append(username);
				inClause.append("%')");
				newStudentCount++;
				if(newStudentCount % 50 == 0){
					firstValue = true;
					inClause.append("#");
				}
		}
		studentManagement.populateActualStudentUserName(newStdRecordCacheImpl, inClause.toString(), new Integer(newStudentCount));
		studentManagement.populateActualStudentIds(newStdRecordCacheImpl, new Integer(newStudentCount) ,studentIdExtPinMap);
		
		
		
		
		studentManagement.insertStudentDetails(newStdRecordCacheImpl);
		studentManagement.createOrgnodeStudent(newStdRecordCacheImpl);
		studentManagement.createStudentAccommodations(newStdRecordCacheImpl);
		studentManagement.createStudentDemographicData(newStdRecordCacheImpl);
		
		
	}
	
	public void executeStudentUpdate(StudentUpdateRecordCacheImpl updateStdRecordCacheImpl, Integer customerId , Map<String,Integer> studentIdExtPinMap ) throws Exception{
		
		studentManagement.updateStudent(updateStdRecordCacheImpl, studentIdExtPinMap);
		updateOrgNodeStudent(updateStdRecordCacheImpl,  customerId);
		updateAccommodation(updateStdRecordCacheImpl);
		updateStudentDemographicData(updateStdRecordCacheImpl);
		System.gc();
		
	}
	
	private void updateOrgNodeStudent(StudentUpdateRecordCacheImpl updateStdRecordCacheImpl,  Integer customerId) throws Exception{
		
		try{
			
			studentManagement.createOrgnodeStudentDuringUpdate(updateStdRecordCacheImpl);
		} 
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw e;
		}
		finally
		{
			logger.info("updateOrgNodeStudent() completed");
		}
		
	}
	
	private boolean hasOrgNodePresentInDB(Integer orgNodeId, ArrayList<OrganizationNode> existingOrgNodeList) {
		Iterator<OrganizationNode> it = existingOrgNodeList.iterator();
		while(it.hasNext()){
			OrganizationNode newONode = it.next();
			if(newONode.getOrgNodeId().intValue() == orgNodeId.intValue()){
				return true;
			}
		}
		return false;
	}

	private StringBuilder generateDemographicIds(StringBuilder demographicIds, StudentDemoGraphics[] studentDemographic, Set<Integer> demoIdSet, boolean firstValue) {
		
		boolean value = firstValue;
		for(int indx=0; indx<studentDemographic.length;indx++){
			Integer customerDemographicId = studentDemographic[indx].getId();
			if(!demoIdSet.contains(customerDemographicId)){
				demoIdSet.add(customerDemographicId);
				if(value){
					demographicIds = new StringBuilder();
					value = false;
				}else{
					demographicIds.append(",");
				}
				demographicIds.append(customerDemographicId);
			}
		}
		return demographicIds;
	}

	private void updateAccommodation(StudentUpdateRecordCacheImpl updateStdRecordCacheImpl ) throws Exception{
	
		studentManagement.updateAccommodation(updateStdRecordCacheImpl);
		
	}
	
	private  void updateStudentDemographicData(StudentUpdateRecordCacheImpl updateStdRecordCacheImpl) throws Exception
	{
		StringBuilder studentIds =  new StringBuilder();
		StringBuilder studentAndDemoIds = new StringBuilder();
		boolean firstValue = true;
		int counter = 0;
		List<String> keys = updateStdRecordCacheImpl.getKeys();
   		for(String key : keys){
   			UploadStudent upload = ((UploadStudent)updateStdRecordCacheImpl.getUpdatedStudent(key));

			Integer studentId = upload.getManageStudent().getId();
			/*StudentDemoGraphics[] studentDemographics = upload.getStudentDemographic();
			
			for (int i=0; studentDemographics!= null && i<studentDemographics.length; i++) {
				StudentDemographicValue [] studentDemographicValues = studentDemographics[i].getStudentDemographicValues();
				
				for (int j=0; studentDemographicValues!=null && j<studentDemographicValues.length; j++) {
					
					if (studentDemographicValues[j] != null && "true".equals(studentDemographicValues[j].getSelectedFlag())) {
						StudentDemographicData studentDemographicData = new StudentDemographicData();
						studentDemographicData.setStudentId(studentId);
						studentDemographicData.setCustomerDemographicId(studentDemographics[i].getId());
						studentDemographicData.setValueName(studentDemographicValues[j].getValueName());
						studentDemographicData.setValue(studentDemographicValues[j].getValueCode());
						studentDemographicData.setCreatedBy(1);
						studentDemographicData.setCreatedDateTime(new Date());
						insertStudentDemoList.add(studentDemographicData);
					}
				}
			}*/
			if(firstValue){
				firstValue = false;
			}else{
				studentIds.append(",");
			}
			studentIds.append(studentId);
			counter++;
			if(counter % BATCH_SIZE == 0){
				studentIds.append("#");
				studentAndDemoIds.append("#");
				firstValue = true;
			}
		}
		
		if(null != studentIds && studentIds.length() >0)
			studentManagement.deleteVisibleStudentDemographicDataForStudent(studentIds.toString());
		
		if(updateStdRecordCacheImpl.getCacheSize() > 0)
			studentManagement.createStudentDemographicDataDuringUpdate(updateStdRecordCacheImpl);

		logger.info("updateStudentDemographicData() done executing");
	}
}
