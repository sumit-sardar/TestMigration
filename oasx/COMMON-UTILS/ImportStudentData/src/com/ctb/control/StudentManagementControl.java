package com.ctb.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

public class StudentManagementControl {
	
	final int BATCH_SIZE = 998;
	private IStudentManagementDAO studentManagement=new StudentManagementDAO();
	
	public void executeStudentCreation(List<UploadStudent> finalStudentList, Set<String> studentUserNames ,
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
		studentManagement.populateActualStudentUserName(finalStudentList, inClause.toString(), new Integer(newStudentCount));
		finalStudentList = studentManagement.populateActualStudentIds(finalStudentList, new Integer(newStudentCount) ,studentIdExtPinMap);
		
		
		
		
		studentManagement.insertStudentDetails(finalStudentList);
		studentManagement.createOrgnodeStudent(finalStudentList);
		studentManagement.createStudentAccommodations(finalStudentList);
		studentManagement.createStudentDemographicData(finalStudentList);
		
		
	}
	
	public void executeStudentUpdate(List<UploadStudent> finalStudentUpdateList, Integer customerId , Map<String,Integer> studentIdExtPinMap ) throws Exception{
		
		Map<Integer, ArrayList<OrganizationNode>> studentOrgMap = new HashMap<Integer, ArrayList<OrganizationNode>>();
		Map<Integer, StudentAccommodations> studentAccomMap = new HashMap<Integer, StudentAccommodations>();
		Map<Integer, HashMap<Integer, ArrayList<StudentDemographicValue>>> studentDemoMap = new HashMap<Integer, HashMap<Integer, ArrayList<StudentDemographicValue>>>();
		int counter = 0;
		boolean firstValue = true;
		StringBuilder inClause = new StringBuilder();
		
		for(UploadStudent upStd : finalStudentUpdateList){
			Integer studentId = 0;
			if (upStd.getManageStudent().getId()==null){
				studentId = studentIdExtPinMap.get(upStd.getManageStudent().getStudentIdNumber().trim());
				upStd.getManageStudent().setId(studentId);
				upStd.getStudentAccommodations().setStudentId(studentId);
				//upStd.getStudent().setStudentId(studentId);
			}else{
				studentId = upStd.getManageStudent().getId();
			}
			
			if(firstValue){
				firstValue = false;
			}else{
				inClause.append(',');
			}
			inClause.append(studentId);
			counter++;
			if(counter % BATCH_SIZE == 0){
				studentManagement.populateStudentOrgNodes(inClause.toString(), studentOrgMap, customerId);
				studentManagement.populateStudentAccommodation(inClause.toString(), studentAccomMap);
				//studentManagement.populateStudentDemoValue(inClause.toString(),demographicIds.toString(),studentDemoMap);
				studentManagement.setRosterUpdateFlag(inClause.toString());
				firstValue = true;
				counter = 0;
				inClause = new StringBuilder();
			}
		}
		
		// Populate org node and accommodation details and set updated date time for roster.
		studentManagement.populateStudentOrgNodes(inClause.toString(), studentOrgMap, customerId);
		studentManagement.populateStudentAccommodation(inClause.toString(), studentAccomMap);
		//studentManagement.populateStudentDemoValue(inClause.toString(),demographicIds.toString(),studentDemoMap);
		studentManagement.setRosterUpdateFlag(inClause.toString());
		
		
		
		// Update student details, accommodation, demographic 
		studentManagement.updateStudent(finalStudentUpdateList, studentOrgMap);
		updateOrgNodeStudent(finalStudentUpdateList, studentOrgMap, customerId);
		updateAccommodation(finalStudentUpdateList, studentAccomMap);
		updateStudentDemographicData(finalStudentUpdateList, studentDemoMap);
		
	}
	
	private void updateOrgNodeStudent(List<UploadStudent> finalStudentList, Map<Integer, ArrayList<OrganizationNode>> studentOrgMap, Integer customerId) throws Exception{
		
		List<OrgNodeStudent> newOrgStudentList = new ArrayList<OrgNodeStudent>();
		for(UploadStudent updateStudent :  finalStudentList){
			List<OrganizationNode> newOrgList = new ArrayList<OrganizationNode>();
			Integer studentId = updateStudent.getManageStudent().getId();
			OrganizationNode [] organizationNodes = updateStudent.getManageStudent().getOrganizationNodes();
			for (int i=0; organizationNodes!=null && i< organizationNodes.length; i++) {
				boolean foundInNewOrgNodes = hasOrgNodePresentInDB(organizationNodes[i].getOrgNodeId(),studentOrgMap.get(studentId));
				if (foundInNewOrgNodes) {
					//do nothing
				}else{
					newOrgList.add(organizationNodes[i]);
				}
			}
			if(studentOrgMap.get(studentId).size() > 0){
				Iterator<OrganizationNode> it = studentOrgMap.get(studentId).iterator();
				while(it.hasNext()){
					OrganizationNode newONode = it.next();
					if("true".equalsIgnoreCase(newONode.getHasRoster())){
						//deactivated but as per existing logic this block will never executed 
					}else{
						//delete but as per existing logic this block will never executed 
					}
				}
			}
			
			for(OrganizationNode orgNode: newOrgList){
				OrgNodeStudent orgNodeStudent = new OrgNodeStudent();
				orgNodeStudent.setActivationStatus("AC");
				orgNodeStudent.setCreatedBy(Constants.USER_ID);
				orgNodeStudent.setCreatedDateTime(new Date());
				orgNodeStudent.setCustomerId(customerId);
				orgNodeStudent.setOrgNodeId(orgNode.getOrgNodeId());
				orgNodeStudent.setStudentId(studentId);
				newOrgStudentList.add(orgNodeStudent);                                
			}
			
		}
		if(newOrgStudentList.size() > 0) {
			studentManagement.createOrgnodeStudentDuringUpdate(newOrgStudentList);
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

	private void updateAccommodation(List<UploadStudent> finalStudentList, Map<Integer, StudentAccommodations> studentAccomMap) throws Exception{
	
		List<UploadStudent> createList = new ArrayList<UploadStudent>();
		List<UploadStudent> updateList = new ArrayList<UploadStudent>();
		Iterator<UploadStudent> it = finalStudentList.iterator();
		while(it.hasNext()){
			UploadStudent upload = it.next();
			StudentAccommodations newAccom = upload.getStudentAccommodations();
			if (!studentAccomMap.containsKey(newAccom.getStudentId())) {
				createList.add(upload);
			} else {    
				updateList.add(upload);
			}
		}
		if(createList.size() > 0)
			studentManagement.createStudentAccommodations(createList);
		if(updateList.size() > 0)
			studentManagement.updateAccommodation(updateList);
		
	}
	
	private  void updateStudentDemographicData(List<UploadStudent> finalStudentList, Map<Integer, HashMap<Integer, ArrayList<StudentDemographicValue>>> studentDemoMap) throws Exception
	{
		List<StudentDemographicData> insertStudentDemoList = new ArrayList<StudentDemographicData>();
		StringBuilder studentIds =  new StringBuilder();
		StringBuilder studentAndDemoIds = new StringBuilder();
		boolean firstValue = true;
		int counter = 0;
		for(UploadStudent upload : finalStudentList){
			Integer studentId = upload.getManageStudent().getId();
			StudentDemoGraphics[] studentDemographics = upload.getStudentDemographic();
			
			for (int i=0; studentDemographics!= null && i<studentDemographics.length; i++) {
				StudentDemographicValue [] studentDemographicValues = studentDemographics[i].getStudentDemographicValues();
				
				//Not needed for LAUAD yet :: Souvik
				/*if ("SINGLE".equals(studentDemographics[i].getValueCardinality())) {
					boolean foundSelectedValue = false;
					for (int j=0; studentDemographicValues!=null && j<studentDemographicValues.length; j++){ 
						if (studentDemographicValues[j] != null && "true".equals(studentDemographicValues[j].getSelectedFlag())){ 
							foundSelectedValue = true;
							break;
						}
					}

					if (foundSelectedValue){
						if(!firstValue)
							studentAndDemoIds.append(",");
						String str = "("+studentId+","+studentDemographics[i].getId()+")";
						studentAndDemoIds.append(str);
					}
					else {
						boolean foundInvisibleValue = false;
						StudentDemographicValue[] oldStudentDemographicValues = (studentDemoMap.get(studentId).get(studentDemographics[i]
								.getCustomerDemographicId())).toArray(new StudentDemographicValue[studentDemoMap.get(studentId)
										.get(studentDemographics[i].getCustomerDemographicId()).size()]);
						
						for (int j=0; oldStudentDemographicValues!=null && j<oldStudentDemographicValues.length; j++) 
							if (oldStudentDemographicValues[j] != null && "true".equals(oldStudentDemographicValues[j].getSelectedFlag()) && "F".equals(oldStudentDemographicValues[j].getVisible())) 
								foundInvisibleValue = true; 

						if (!foundInvisibleValue){
							if(!firstValue)
								studentAndDemoIds.append(",");
							String str = "("+studentId+","+studentDemographics[i].getId()+")";
							studentAndDemoIds.append(str);
						}
					}                                              
				}*/
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
			}
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
		//if(null != studentAndDemoIds && studentAndDemoIds.length() >0)
		//	studentManagement.deleteStudentDemographicDataForStudentAndCustomerDemographic(studentAndDemoIds.toString());
		if(insertStudentDemoList.size() > 0)
			studentManagement.createStudentDemographicDataDuringUpdate(insertStudentDemoList);
	}
}
