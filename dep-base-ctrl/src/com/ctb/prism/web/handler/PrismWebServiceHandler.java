/**
 * 
 */
package com.ctb.prism.web.handler;

import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import com.ctb.prism.web.constant.PrismWebServiceConstant;
import com.ctb.prism.web.controller.ContentDetailsTO;
import com.ctb.prism.web.controller.ContentScoreDetailsTO;
import com.ctb.prism.web.controller.ContentScoreTO;
import com.ctb.prism.web.controller.CustHierarchyDetailsTO;
import com.ctb.prism.web.controller.DemoTO;
import com.ctb.prism.web.controller.ItemResponseTO;
import com.ctb.prism.web.controller.ItemResponsesDetailsTO;
import com.ctb.prism.web.controller.ObjectiveScoreDetailsTO;
import com.ctb.prism.web.controller.ObjectiveScoreTO;
import com.ctb.prism.web.controller.OrgDetailsTO;
import com.ctb.prism.web.controller.RosterDetailsTO;
import com.ctb.prism.web.controller.SampleWebservice;
import com.ctb.prism.web.controller.StudentBioTO;
import com.ctb.prism.web.controller.StudentDemoTO;
import com.ctb.prism.web.controller.StudentDetailsTO;
import com.ctb.prism.web.controller.StudentListTO;
import com.ctb.prism.web.controller.StudentSurveyBioTO;
import com.ctb.prism.web.controller.SubtestAccommodationTO;
import com.ctb.prism.web.controller.SubtestAccommodationsTO;
import com.ctb.prism.web.controller.SurveyBioTO;
import com.ctb.prism.web.dbutility.PrismWebServiceDBUtility;
import com.ctb.util.OASLogger;
import com.sun.xml.internal.ws.client.BindingProviderProperties;
import com.thoughtworks.xstream.XStream;

/**
 * @author TCS
 * 
 */
public class PrismWebServiceHandler {

	private static SampleWebservice service = null;

	/**
	 * Connect with the Prism Web Service
	 * @return
	 * @throws Exception
	 */
	private static void getService() throws Exception {
		try {
			if (service == null) {
				String urlLocation = PrismWebServiceConstant.resourceBundler.getString("url");
				System.out.println("PrismWebServiceHandler.getService : Prism Web Service URL Location : -> " + urlLocation);
				OASLogger.getLogger(PrismWebServiceConstant.loggerName).info("PrismWebServiceHandler.getService : Prism Web Service URL Location : " + urlLocation);
				URL url = new URL(urlLocation);
				QName qname = new QName("http://controller.web.prism.ctb.com/",
						"StudentDataloadService");

				Service prxyService = Service.create(url, qname);

				service = prxyService.getPort(SampleWebservice.class);
				BindingProvider provider = (BindingProvider) service;
				provider.getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, PrismWebServiceConstant.REQUEST_TIMEOUT);
				provider.getRequestContext().put("com.sun.xml.internal.ws.connect.timeout", PrismWebServiceConstant.CONNECT_TIMEOUT);	
			}
			OASLogger.getLogger(PrismWebServiceConstant.loggerName).info("PrismWebServiceHandler.getService : Prism Web Service successfully connected.");
		} catch (Exception e) {
			OASLogger.getLogger(PrismWebServiceConstant.loggerName).error("PrismWebServiceHandler.getService : Unable to connect with Prism Web Service.");
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * Invoke the Prism Web Service
	 * @param studentListTO
	 * @throws Exception
	 */
	private static void invokePrismWebService(StudentListTO studentListTO) throws Exception{
		try{
			//printXMLFromVO(studentListTO);
			/*XStream xstream = new XStream();
			System.out.println(xstream.toXML(studentListTO));*/
			getService();
			if(service != null){
				service.loadStudentData(studentListTO);
				OASLogger.getLogger(PrismWebServiceConstant.loggerName).info("PrismWebServiceHandler.invokePrismWebService : Prism Web Service successfully invoked.");
			}else{
				OASLogger.getLogger(PrismWebServiceConstant.loggerName).error("PrismWebServiceHandler.invokePrismWebService : Unable to invoke Prism Web Service.");
				throw new Exception("Prism Web Service is null.");
			}
		}catch(Exception e){
			OASLogger.getLogger(PrismWebServiceConstant.loggerName).error("PrismWebServiceHandler.invokePrismWebService : Unable to invoke Prism Web Service.");
			e.printStackTrace();
			for(int hitCnt = 0 ; hitCnt < PrismWebServiceConstant.numberOfFailedHitCnt ; hitCnt++){
				OASLogger.getLogger(PrismWebServiceConstant.loggerName).info("PrismWebServiceHandler.invokePrismWebService : Retry to invoke Prism Web Service. Count - " + (hitCnt+1));
				try{
					getService();
					service.loadStudentData(studentListTO);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
			throw e;
		}
	}
	

	/**
	 * Web Service call for Edit Student
	 * @param userName
	 * @param studentId
	 * @throws Exception
	 */
	public static StudentListTO editStudent(Integer studentId) throws Exception{
		OASLogger.getLogger(PrismWebServiceConstant.loggerName).info("PrismWebServiceHandler.editStudent : Prism Web Service Edit Student started for student id - " + studentId);
		StudentListTO studentListTO = new StudentListTO();
		
		List<RosterDetailsTO> rosterDetailsList = studentListTO.getRosterDetailsTO();
		
		StudentDetailsTO studentDetailsTO = getStdentBio(studentId);
		CustHierarchyDetailsTO custHierarchyDetailsTO = getCustHierarchy(studentId);
		
		List<Long> rosterIds = PrismWebServiceDBUtility.getRosterListForStudent(studentId);
		
		for(long rosterID : rosterIds){
			RosterDetailsTO rosterDetailsTO = new RosterDetailsTO();
			studentDetailsTO.setStudentDemoTO(PrismWebServiceDBUtility.getStudentDemo(rosterID));
			//TODO - get the student survey details and put it in the studentDetailsTO
			rosterDetailsTO.setStudentDetailsTO(studentDetailsTO);
			rosterDetailsTO.setCustHierarchyDetailsTO(custHierarchyDetailsTO);
			rosterDetailsTO.setRosterId(String.valueOf(rosterID));
			rosterDetailsList.add(rosterDetailsTO);
		}
		
		invokePrismWebService(studentListTO);
		OASLogger.getLogger(PrismWebServiceConstant.loggerName).info("PrismWebServiceHandler.editStudent : Prism Web Service Edit Student ended for student id - " + studentId);
		
		return studentListTO;
		
		/*List<ContentDetailsTO> list = PrismWebServiceDBUtility.getContentDetailsTO(6808083, new SubtestAccommodationsTO(), 12097455, 187196);
		System.out.println(list);*/
	}
	
	/**
	 * Web Service call for Scoring
	 * @param rosterId
	 * @param studentId
	 * @param sessionId
	 * @throws Exception
	 */
	public static StudentListTO scoring(long rosterId, Integer studentId, long sessionId) throws Exception{
		OASLogger.getLogger(PrismWebServiceConstant.loggerName).info("PrismWebServiceHandler.scoring : Prism Web Service Scoring started for student id - " + studentId + " rosterId - " + rosterId + " sessionId - " + sessionId);
		StudentListTO studentListTO = new StudentListTO();
		
		List<RosterDetailsTO> rosterDetailsList = studentListTO.getRosterDetailsTO();
		
		RosterDetailsTO rosterDetailsTO = new RosterDetailsTO();
		

		CustHierarchyDetailsTO custHierarchyDetailsTO = getCustHierarchy(studentId);
		rosterDetailsTO.setCustHierarchyDetailsTO(custHierarchyDetailsTO);
		
		StudentDetailsTO studentDetailsTO = getStdentBio(studentId);
		studentDetailsTO.setStudentDemoTO(PrismWebServiceDBUtility.getStudentDemo(rosterId));
		studentDetailsTO.setStudentSurveyBioTO(PrismWebServiceDBUtility.getStudentSurveyBio(rosterId));
		rosterDetailsTO.setStudentDetailsTO(studentDetailsTO);
		
		List<ContentDetailsTO> contentDetailsTOList =  PrismWebServiceDBUtility.getContentDetailsTO(rosterId, studentId, sessionId);
		rosterDetailsTO.getCollContentDetailsTO().addAll(contentDetailsTOList);
		
		rosterDetailsTO.setRosterId(String.valueOf(rosterId));
		rosterDetailsList.add(rosterDetailsTO);
		
		invokePrismWebService(studentListTO);
		OASLogger.getLogger(PrismWebServiceConstant.loggerName).info("PrismWebServiceHandler.scoring : Prism Web Service Scoring ended for student id - " + studentId + " rosterId - " + rosterId + " sessionId - " + sessionId);
		
		return studentListTO;
		
	}

	/**
	 * Populate the StudentBioTO and CustHierarchyDetailsTO
	 * @param studentDetailsTO
	 * @param studentId
	 * @throws Exception
	 */
	private static StudentDetailsTO getStdentBio(Integer studentId) throws Exception{
		StudentDetailsTO studentDetailsTO = new StudentDetailsTO();
		StudentBioTO studentBioTO =  PrismWebServiceDBUtility.getStudentBio(studentId);
		studentDetailsTO.setStudentBioTO(studentBioTO);
		return studentDetailsTO;
	}
	
	/**
	 * Get the customer hierarchy
	 * @param studentId
	 * @throws Exception 
	 */
	private static CustHierarchyDetailsTO getCustHierarchy(Integer studentId)
			throws Exception {
		return PrismWebServiceDBUtility.getCustomerHigherarchy(studentId);
	}
	
	

	/**
	 * Print the XML from VO
	 * @param studentListTO
	 */
	private static void printXMLFromVO(StudentListTO studentListTO) {
		XStream xstream = new XStream();
		xstream.alias("Student_List", StudentListTO.class);
		xstream.alias("Roster_Details", RosterDetailsTO.class);
		xstream.aliasAttribute(RosterDetailsTO.class, "rosterId",  "roster_id"  );
		xstream.alias("Cust_Hierarchy_Details", CustHierarchyDetailsTO.class);
		xstream.aliasAttribute(CustHierarchyDetailsTO.class, "customerId",  "Customer_ID");
		xstream.aliasAttribute(CustHierarchyDetailsTO.class, "maxHierarchy",  "Max_Hierarchy");
		xstream.aliasAttribute(CustHierarchyDetailsTO.class, "dataChanged",  "IsDataChange");
		xstream.alias("Org_Details", OrgDetailsTO.class);
		xstream.aliasAttribute(OrgDetailsTO.class, "orgName",  "Org_Name");
		xstream.aliasAttribute(OrgDetailsTO.class, "orgLabel",  "Org_Label");
		xstream.aliasAttribute(OrgDetailsTO.class, "orgLevel", "Org_Level");
		xstream.aliasAttribute(OrgDetailsTO.class, "orgNodeId",  "Org_Node_Id");
		xstream.aliasAttribute(OrgDetailsTO.class, "orgCode",  "Org_Code");
		xstream.aliasAttribute(OrgDetailsTO.class, "parentOrgCode",  "Pr_Org_Code");
		xstream.alias("Student_Details", StudentDetailsTO.class);
		xstream.alias("Student_Bio", StudentBioTO.class);
		xstream.aliasAttribute(StudentBioTO.class, "dataChanged",  "IsDataChange");
		xstream.aliasAttribute(StudentBioTO.class, "oasStudentId",  "OAS_Stnt_ID");
		xstream.aliasAttribute(StudentBioTO.class, "lastName",  "Last_Name");
		xstream.aliasAttribute(StudentBioTO.class, "firstName",  "First_Name");
		xstream.aliasAttribute(StudentBioTO.class, "middleInit",  "Mdle_Initial");
		xstream.aliasAttribute(StudentBioTO.class, "gender",  "Gender");
		xstream.aliasAttribute(StudentBioTO.class, "grade",  "Grade");
		xstream.aliasAttribute(StudentBioTO.class, "examineeId",  "Examinee_ID");
		xstream.alias("Student_Demo ", StudentDemoTO.class);
		xstream.aliasAttribute(StudentDemoTO.class, "dataChanged",  "IsDataChange");
		xstream.alias("Demo", DemoTO.class);
		xstream.aliasAttribute(DemoTO.class, "demoName",  "demo_name");
		xstream.aliasAttribute(DemoTO.class, "demovalue",  "demo_value");
		xstream.alias("Student_Survey_Bio", StudentSurveyBioTO.class);
		xstream.aliasAttribute(StudentSurveyBioTO.class, "dataChanged",  "IsDataChange");
		xstream.alias("Survey_Bio", SurveyBioTO.class);
		xstream.aliasAttribute(SurveyBioTO.class, "surveyName",  "Survey_name");
		xstream.aliasAttribute(SurveyBioTO.class, "surveyValue",  "survey_value");
		xstream.alias("Content_Details ", ContentDetailsTO.class);
		xstream.aliasAttribute(ContentDetailsTO.class, "contentCode",  "Content_Code");
		xstream.aliasAttribute(ContentDetailsTO.class, "scoringMethod",  "Scrng_Method");
		xstream.aliasAttribute(ContentDetailsTO.class, "statusCode",  "Status_code");
		xstream.aliasAttribute(ContentDetailsTO.class, "dateTestTaken",  "Dt_Tst_Taken");
		xstream.aliasAttribute(ContentDetailsTO.class, "dataChanged",  "IsDataChange");
		xstream.alias("Subtest_Accommodations", SubtestAccommodationsTO.class);
		xstream.alias("Subtest_Accommodation ", SubtestAccommodationTO.class);
		xstream.aliasAttribute(SubtestAccommodationTO.class, "name",  "name");
		xstream.aliasAttribute(SubtestAccommodationTO.class, "value",  "value");
		xstream.alias("Item_Responses_Details", ItemResponsesDetailsTO.class);
		xstream.alias("Item_Response", ItemResponseTO.class);
		xstream.aliasAttribute(ItemResponseTO.class, "itemCode",  "item_code");
		xstream.aliasAttribute(ItemResponseTO.class, "itemSetType",  "item_set_type");
		xstream.aliasAttribute(ItemResponseTO.class, "scoreValue",  "score_value");
		xstream.alias("Content_Score_Details", ContentScoreDetailsTO.class);
		xstream.alias("Content_Score ", ContentScoreTO.class);
		xstream.aliasAttribute(ContentScoreTO.class, "scoreType",  "Score_Type");
		xstream.aliasAttribute(ContentScoreTO.class, "scoreValue",  "score_value");
		xstream.alias("Objective_Score_Details", ObjectiveScoreDetailsTO.class);
		xstream.aliasAttribute(ObjectiveScoreDetailsTO.class, "objectiveCode",  "Obj_Code");
		xstream.aliasAttribute(ObjectiveScoreDetailsTO.class, "objectiveName",  "Obj_Name");
		xstream.alias("Objective_Score", ObjectiveScoreTO.class);
		xstream.aliasAttribute(ObjectiveScoreTO.class, "scoreType",  "Score_Type");
		xstream.aliasAttribute(ObjectiveScoreTO.class, "value",  "value");
				
		System.out.println("XML forwarded to Prism Web Service >>>>>>>>>>>>>> \n" + xstream.toXML(studentListTO));
	}
	
}
