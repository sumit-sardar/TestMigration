package com.ctb.control;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.ctb.bean.ManageStudent;
import com.ctb.bean.Node;
import com.ctb.bean.OrgNodeStudent;
import com.ctb.bean.OrganizationNode;
import com.ctb.bean.Student;
import com.ctb.bean.StudentAccommodations;
import com.ctb.bean.StudentDemoGraphics;
import com.ctb.bean.StudentDemographicData;
import com.ctb.bean.StudentDemographicValue;
import com.ctb.bean.UploadStudent;
import com.ctb.bean.User;
import com.ctb.dao.IOrgNodeDAO;
import com.ctb.dao.IOrgNodeStudentDAO;
import com.ctb.dao.IStudentManagementDAO;
import com.ctb.dao.ITestRosterDAO;
import com.ctb.dao.OrgNodeDAO;
import com.ctb.dao.OrgNodeStudentDAO;
import com.ctb.dao.SQLutils;
import com.ctb.dao.StudentManagementDAO;
import com.ctb.dao.TestRosterDAO;
import com.ctb.exception.CTBBusinessException;
import com.ctb.utils.StudentUtils;

public class StudentManagementControl {
	
	private IOrgNodeStudentDAO orgNodeStudents=new OrgNodeStudentDAO();
	private IStudentManagementDAO studentManagement=new StudentManagementDAO();
	private IOrgNodeDAO orgNode=new OrgNodeDAO();
	private ITestRosterDAO testRosters=new TestRosterDAO();
	private String findInColumn = "ona.ancestor_org_node_id in ";
	
	public OrganizationNode[]  getStudentsOrganizationUpload(Integer studentId) throws Exception 
	{

//		validator.validateStudent(userName, studentId, "StudentManagementImpl.getManageStudent");

		try {
			OrganizationNode [] orgNodes = studentManagement.getAssignedOrganizationNodesForStudentAtAndBelowUserTopNodes(studentId.intValue());
			return orgNodes;
		} catch (SQLException se) {
			
			se.printStackTrace();
			
		}
		return null;

	}
	
	public void updateStudentUpload(ManageStudent manageStudent) throws CTBBusinessException
	{
		Integer studentId = manageStudent.getId();
		OrganizationNode [] organizationNodes = manageStudent.getOrganizationNodes();

		try {
			//User user = getUserDetails(userName, userName);
//			Integer userId = user.getUserId();
//			Integer [] topOrgNodeIds = studentManagement.getTopOrgNodeIdsForUser(user.getUserName());

			studentManagement.updateStudent(manageStudent,new Date());

			Hashtable newOrgNodeHash = new Hashtable();

			for (int i=0; organizationNodes!=null && i< organizationNodes.length; i++) {
				newOrgNodeHash.put(organizationNodes[i].getOrgNodeId(), organizationNodes[i]);
			}

			OrgNodeStudent [] orgNodeStus = orgNodeStudents.getOrgNodeStudentForStudentAtAndBelowOrgNodes(studentId, SQLutils.generateSQLCriteria(findInColumn,null));
			for (int i=0; orgNodeStus!=null && i< orgNodeStus.length; i++) {
				OrgNodeStudent oldOrgNodeInDB = orgNodeStus[i];
				boolean foundInNewOrgNodes = newOrgNodeHash.containsKey(oldOrgNodeInDB.getOrgNodeId());
				if (foundInNewOrgNodes) { //activate 
					orgNodeStudents.activateOrgNodeStudentForStudentAndOrgNode(oldOrgNodeInDB.getStudentId(), oldOrgNodeInDB.getOrgNodeId());    
					//remove from hash so that the remaining will be new org nodes
					newOrgNodeHash.remove(oldOrgNodeInDB.getOrgNodeId());                              
				}
				else { //delete or deactivate
					Integer rosterCount = testRosters.getRosterCountForStudentAndOrgNode(studentId, oldOrgNodeInDB.getOrgNodeId());
					if (rosterCount.intValue() >0) {
						orgNodeStudents.deactivateOrgNodeStudentForStudentAndOrgNode(studentId, oldOrgNodeInDB.getOrgNodeId());
					}
					else {
						orgNodeStudents.deleteOrgNodeStudentForStudentAndOrgNode(studentId, oldOrgNodeInDB.getOrgNodeId());
					}
				}
			}

			//insert new org nodes remaining in hash                
			Iterator iterator = newOrgNodeHash.values().iterator();
			while (iterator.hasNext()) {
				OrganizationNode newOrganizationNode = (OrganizationNode) iterator.next();
				Node node = orgNode.getOrgNodeById(newOrganizationNode.getOrgNodeId());                
				OrgNodeStudent orgNodeStudent = new OrgNodeStudent();
				orgNodeStudent.setActivationStatus("AC");
				orgNodeStudent.setCreatedBy(new Integer(1));
				orgNodeStudent.setCreatedDateTime(new Date());
				orgNodeStudent.setCustomerId(node.getCustomerId());
				orgNodeStudent.setDataImportHistoryId(node.getDataImportHistoryId());
				orgNodeStudent.setOrgNodeId(node.getOrgNodeId());
				orgNodeStudent.setStudentId(studentId);
				orgNodeStudents.createOrgNodeStudent(orgNodeStudent);                                
			}

		} catch (SQLException se) {
			se.printStackTrace();
		}
	}
	
	

	
	public void updateStudentAccommodations(StudentAccommodations studentAccommodations) throws CTBBusinessException
	{
		Integer studentId = studentAccommodations.getStudentId();
//		validator.validateStudent(userName, studentId, "StudentManagementImpl.updateStudentAccommodations");

		try {
			StudentAccommodations accommo = studentManagement.getStudentAccommodations(studentId);
			if (accommo == null) {
//				accommodation.createStudentAccommodations(studentAccommodations);
			} else {    
//				accommodation.updateStudentAccommodations(studentAccommodations);
			}
			studentManagement.setRosterUpdateFlag(studentId);
		} catch (SQLException se) {
			
			se.printStackTrace();
			
		}
	} 
	
	public void updateStudentDemographicsUpload(Integer studentId, StudentDemoGraphics [] studentDemographics) throws Exception
	{
//		validator.validateStudent(user.getUserName(), studentId, "StudentManagementImpl.updateStudentDemographics");

		try {
			//User user = getUserDetails(userName, userName);
//			Integer userId = user.getUserId();
			Date now = new Date();
			// delete all visible for student then insert new ones
			studentManagement.deleteVisibleStudentDemographicDataForStudent(studentId);

			for (int i=0; studentDemographics!= null && i<studentDemographics.length; i++) {
				StudentDemographicValue [] studentDemographicValues = studentDemographics[i].getStudentDemographicValues();
				boolean isSingle = false;
				if ("SINGLE".equals(studentDemographics[i].getValueCardinality())) {
					boolean foundSelectedValue = false;
					for (int j=0; studentDemographicValues!=null && j<studentDemographicValues.length; j++) 
						if (studentDemographicValues[j] != null && "true".equals(studentDemographicValues[j].getSelectedFlag())) 
							foundSelectedValue = true;                 

					if (foundSelectedValue)  
						studentManagement.deleteStudentDemographicDataForStudentAndCustomerDemographic(studentId, studentDemographics[i].getId());
					else {
						boolean foundInvisibleValue = false;
						StudentDemographicValue [] oldStudentDemographicValues = studentManagement.getStudentDemographicValues(studentDemographics[i].getId().intValue(), studentId.intValue());
						for (int j=0; oldStudentDemographicValues!=null && j<oldStudentDemographicValues.length; j++) 
							if (oldStudentDemographicValues[j] != null && "true".equals(oldStudentDemographicValues[j].getSelectedFlag()) && "F".equals(oldStudentDemographicValues[j].getVisible())) 
								foundInvisibleValue = true; 

						if (!foundInvisibleValue)
							studentManagement.deleteStudentDemographicDataForStudentAndCustomerDemographic(studentId, studentDemographics[i].getId());
					}                                              
				}
				for (int j=0; studentDemographicValues!=null && j<studentDemographicValues.length; j++) {
					if (studentDemographicValues[j] != null && "true".equals(studentDemographicValues[j].getSelectedFlag())) {
						StudentDemographicData studentDemographicData = new StudentDemographicData();
						studentDemographicData.setStudentDemographicDataId(studentManagement.getNextPKForStudentDemographicData());                        
						studentDemographicData.setStudentId(studentId);
						studentDemographicData.setCustomerDemographicId(studentDemographics[i].getId());
						studentDemographicData.setValueName(studentDemographicValues[j].getValueName());
						studentDemographicData.setValue(studentDemographicValues[j].getValueCode());
						studentDemographicData.setCreatedBy(new Integer(1));
						studentDemographicData.setCreatedDateTime(now);
					}
				}
			}
		}catch(Exception se) {
			se.printStackTrace();
		}
	} 
	
	public void executeStudentCreation(List<UploadStudent> finalStudentList, Set<String> studentUserNames) throws Exception {
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
		finalStudentList = studentManagement.populateActualStudentIds(finalStudentList, new Integer(newStudentCount));
		
		
		
		
		studentManagement.insertStudentDetails(finalStudentList);
		studentManagement.createOrgnodeStudent(finalStudentList);
		studentManagement.createStudentAccommodations(finalStudentList);
		studentManagement.createStudentDemographicData(finalStudentList);
	}
}
