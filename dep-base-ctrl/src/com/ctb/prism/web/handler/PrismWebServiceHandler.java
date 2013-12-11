/**
 * 
 */
package com.ctb.prism.web.handler;

import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.*;
import weblogic.utils.StringUtils;

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
import com.ctb.prism.web.controller.StudentDataLoadTO;
import com.ctb.prism.web.controller.StudentDemoTO;
import com.ctb.prism.web.controller.StudentDetailsTO;
import com.ctb.prism.web.controller.StudentListTO;
import com.ctb.prism.web.controller.StudentSurveyBioTO;
import com.ctb.prism.web.controller.SubtestAccommodationTO;
import com.ctb.prism.web.controller.SubtestAccommodationsTO;
import com.ctb.prism.web.controller.SurveyBioTO;
import com.ctb.prism.web.dbutility.PrismWebServiceDBUtility;
import com.ctb.util.HMACQueryStringEncrypter;
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
	private static void getService(String customerId, String orgNodeCode, String heirarchyLevel) throws Exception {
		try {
			if (service == null) {
				String urlLocation = PrismWebServiceConstant.resourceBundler.getString("url");
				System.out.println("PrismWebServiceHandler.getService : Prism Web Service URL Location : -> " + urlLocation);
				System.out.println("PrismWebServiceHandler.getService : Prism Web Service URL Location : " + urlLocation);
				URL url = new URL(urlLocation);
				QName qname = new QName("http://controller.web.prism.ctb.com/",
						"StudentDataloadService");
				
				Service prxyService = Service.create(url, qname);
				service = prxyService.getPort(SampleWebservice.class);
				BindingProvider provider = (BindingProvider) service;				
				provider.getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, PrismWebServiceConstant.REQUEST_TIMEOUT);
				provider.getRequestContext().put("com.sun.xml.internal.ws.connect.timeout", PrismWebServiceConstant.CONNECT_TIMEOUT);	
			}
			
			if (true)
			{
				//**[IAA] Create SSO parameters with signature
				String requestParam = "";
        		String customerKey = PrismWebServiceDBUtility.getCustomerKey(Integer.parseInt(customerId));
        		HMACQueryStringEncrypter HMACEncrypter = new HMACQueryStringEncrypter(customerKey, Integer.parseInt(customerId), orgNodeCode, Integer.parseInt(heirarchyLevel));
            	requestParam = HMACEncrypter.encrypt()+"&clienttype=SOAP";
            	System.out.println("WS_SSOparams=" + requestParam);
            	OASLogger.getLogger(PrismWebServiceConstant.loggerName).info("PrismWebServiceHandler.getService : Prism Web Service WS_SSOparams : " + requestParam);

				BindingProvider provider = (BindingProvider) service;
				
				//**[IAA]: append SOAP header to SOAP envelope
				if (false)
				{
					Binding binding = provider.getBinding();
					List<Handler> handlers = provider.getBinding().getHandlerChain();
					handlers.add(new PrismSOAPHandler(requestParam));
					binding.setHandlerChain(handlers);
				}
				else
				{
					Map<String, List<String>> requestHeaders = new HashMap<String, List<String>>();
					String[] pairs = requestParam.split("&");
					for (String pair: pairs)
					{
						int idx = pair.indexOf("=");
						requestHeaders.put(pair.substring(0, idx), Collections.singletonList(pair.substring(idx+1)));
						System.out.println(pair.substring(0, idx)+"="+ pair.substring(idx+1));
					}
					provider.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, requestHeaders);
				}
				//**[IAA]
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
	private static void invokePrismWebService(StudentListTO studentListTO, String customerId, String orgNodeCode, String heirarchyLevel, Integer studentId, long rosterId, long sessionId, String wsType) throws Exception{
		long errorLogKey = 0;
		String errorMessage = "";
		StudentDataLoadTO responseTO = null;
		String additionalInfo = "";
		try{
			getService(customerId, orgNodeCode, heirarchyLevel);
			if(service != null){
				responseTO = service.loadStudentData(studentListTO);
				System.out.println("Prism Process Id : " + responseTO.getProcessId());
				System.out.println("Prism Partition Name : " + responseTO.getPartitionName());
				if(responseTO.getStatusCode() == 1){ //Success
					System.out.println("PrismWebServiceHandler.invokePrismWebService : Prism Web Service successfully invoked.");
				}else{ //Failure
					OASLogger.getLogger(PrismWebServiceConstant.loggerName).error("PrismWebServiceHandler.invokePrismWebService : Prism Web Service call failed and error message is ::::: " + StringUtils.join(responseTO.getErrorMessages().toArray(new String[0]) , "------------------------------- ********************* --------------------------\n"));
					throw new Exception(StringUtils.join(responseTO.getErrorMessages().toArray(new String[0]) , "------------------------------- ********************* --------------------------\n"));
				}
			}else{
				OASLogger.getLogger(PrismWebServiceConstant.loggerName).error("PrismWebServiceHandler.invokePrismWebService : Unable to invoke Prism Web Service.");
				throw new Exception("Prism Web Service is null.");
			}
		}catch(Exception e){
			OASLogger.getLogger(PrismWebServiceConstant.loggerName).error("PrismWebServiceHandler.invokePrismWebService : Unable to invoke Prism Web Service.");
			e.printStackTrace();
			errorMessage =  e.getMessage();
			additionalInfo = createAdditionalInfo(responseTO);
			errorLogKey = PrismWebServiceDBUtility.insertWSErrorLog(studentId, rosterId, sessionId, wsType, errorMessage, additionalInfo);
			System.out.println("Prism web service error log key : " + errorLogKey);
			boolean success = false;
			for(int hitCnt = 2 ; hitCnt <= PrismWebServiceConstant.numberOfFailedHitCnt ; hitCnt++){
				try{
					Thread.sleep(PrismWebServiceConstant.retryWaitTime);
					System.out.println("PrismWebServiceHandler.invokePrismWebService : Retry to invoke Prism Web Service. Count - " + hitCnt);
					getService(customerId, orgNodeCode, heirarchyLevel);
					responseTO = service.loadStudentData(studentListTO);
					System.out.println("Prism Process Id : " + responseTO.getProcessId());
					System.out.println("Prism Partition Name : " + responseTO.getPartitionName());
					if(responseTO.getStatusCode() == 1){ //Success
						PrismWebServiceDBUtility.deleteWSErrorLog(errorLogKey);
						System.out.println("PrismWebServiceHandler.invokePrismWebService : Prism Web Service successfully invoked.");
						success = true;
						break;
					}else{ //Failure
						OASLogger.getLogger(PrismWebServiceConstant.loggerName).error("PrismWebServiceHandler.invokePrismWebService : Prism Web Service call failed and error message is ::::: " + StringUtils.join(responseTO.getErrorMessages().toArray(new String[0]) , "------------------------------- ********************* --------------------------\n"));
						throw new Exception(StringUtils.join(responseTO.getErrorMessages().toArray(new String[0]) , "------------------------------- ********************* --------------------------\n"));
					}
				}catch(Exception ex){
					additionalInfo = createAdditionalInfo(responseTO);
					PrismWebServiceDBUtility.updateWSErrorLog(errorLogKey, hitCnt, errorMessage, "Progress", additionalInfo);
					errorMessage =  e.getMessage();
					ex.printStackTrace();
				}
			}
			if(!success){
				PrismWebServiceDBUtility.updateWSErrorLog(errorLogKey, PrismWebServiceConstant.numberOfFailedHitCnt , errorMessage, "Failed", createAdditionalInfo(responseTO));
				throw e;
			}
		}
	}
	

	private static String createAdditionalInfo(StudentDataLoadTO responseTO) {
		String additionalinfo = "";
		if(responseTO != null){
			additionalinfo = "Process Id : " + responseTO.getProcessId() + " Partition Name : " + responseTO.getPartitionName();
		}
		return additionalinfo;
	}

	/**
	 * Web Service call for Edit Student
	 * @param userName
	 * @param studentId
	 * @throws Exception
	 */
	public static StudentListTO editStudent(Integer studentId) throws Exception{
		long startTime = System.currentTimeMillis();
		System.out.println("PrismWebServiceHandler.editStudent : Prism Web Service Edit Student started for student id - " + studentId);
		StudentListTO studentListTO = new StudentListTO();
		
		List<RosterDetailsTO> rosterDetailsList = studentListTO.getRosterDetailsTO();
		
		
		List<Long> rosterIds = PrismWebServiceDBUtility.getRosterListForStudent(studentId);
		
		String customerId = null;
		String orgNodeCode = null;
		String heirarchyLevel = null;
		for(long rosterID : rosterIds){
			RosterDetailsTO rosterDetailsTO = new RosterDetailsTO();
			
			StudentDetailsTO studentDetailsTO = getStdentBio(studentId);
			studentDetailsTO.setStudentDemoTO(PrismWebServiceDBUtility.getStudentDemo(rosterID));
			//TODO - get the student survey details and put it in the studentDetailsTO
			rosterDetailsTO.setStudentDetailsTO(studentDetailsTO);
			//rosterDetailsTO.setCustHierarchyDetailsTO(getCustHierarchy(studentId,rosterID));
			
			CustHierarchyDetailsTO custHierarchyDetailsTO = getCustHierarchy(studentId,rosterID);
			customerId = custHierarchyDetailsTO.getCustomerId();
			OrgDetailsTO orgDetails = custHierarchyDetailsTO.getCollOrgDetailsTO().get(custHierarchyDetailsTO.getCollOrgDetailsTO().size()-1);
			orgNodeCode = (orgDetails.getOrgCode()==null)?"":orgDetails.getOrgCode();			
			heirarchyLevel = orgDetails.getOrgLevel();
			
			rosterDetailsTO.setCustHierarchyDetailsTO(custHierarchyDetailsTO);
			
			rosterDetailsTO.setRosterId(String.valueOf(rosterID));
			rosterDetailsList.add(rosterDetailsTO);
		}
		
		invokePrismWebService(studentListTO, customerId, orgNodeCode, heirarchyLevel, studentId, 0, 0, "Edit Student");
		System.out.println("PrismWebServiceHandler.editStudent : Prism Web Service Edit Student ended for student id - " + studentId);
		
		long stopTime = System.currentTimeMillis();
		System.out.println("Time taken to call the Edit Student Web Service : " + (stopTime - startTime) + "ms");
		
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
		long startTime = System.currentTimeMillis();
		System.out.println("PrismWebServiceHandler.scoring : Prism Web Service Scoring started for student id - " + studentId + " rosterId - " + rosterId + " sessionId - " + sessionId);
		StudentListTO studentListTO = new StudentListTO();
		
		List<RosterDetailsTO> rosterDetailsList = studentListTO.getRosterDetailsTO();
		
		RosterDetailsTO rosterDetailsTO = new RosterDetailsTO();
		
		String customerId = null;
		String orgNodeCode = null;
		String heirarchyLevel = null;
		CustHierarchyDetailsTO custHierarchyDetailsTO = getCustHierarchy(studentId,rosterId);
		customerId = custHierarchyDetailsTO.getCustomerId();
		OrgDetailsTO orgDetails = custHierarchyDetailsTO.getCollOrgDetailsTO().get(custHierarchyDetailsTO.getCollOrgDetailsTO().size()-1);
		orgNodeCode = (orgDetails.getOrgCode()==null)?"":orgDetails.getOrgCode();
		heirarchyLevel = orgDetails.getOrgLevel();
		rosterDetailsTO.setCustHierarchyDetailsTO(custHierarchyDetailsTO);
		
		StudentDetailsTO studentDetailsTO = getStdentBio(studentId);
		studentDetailsTO.setStudentDemoTO(PrismWebServiceDBUtility.getStudentDemo(rosterId));
		studentDetailsTO.setStudentSurveyBioTO(PrismWebServiceDBUtility.getStudentSurveyBio(rosterId));
		rosterDetailsTO.setStudentDetailsTO(studentDetailsTO);
		
		List<ContentDetailsTO> contentDetailsTOList =  PrismWebServiceDBUtility.getContentDetailsTO(rosterId, studentId, sessionId);
		rosterDetailsTO.getCollContentDetailsTO().addAll(contentDetailsTOList);
		
		rosterDetailsTO.setRosterId(String.valueOf(rosterId));
		rosterDetailsList.add(rosterDetailsTO);
		
		invokePrismWebService(studentListTO, customerId, orgNodeCode, heirarchyLevel, studentId, rosterId, sessionId, "Scoring");
		System.out.println("PrismWebServiceHandler.scoring : Prism Web Service Scoring ended for student id - " + studentId + " rosterId - " + rosterId + " sessionId - " + sessionId);
		
		long stopTime = System.currentTimeMillis();
		System.out.println("Time taken to call the Scoring Web Service : " + (stopTime - startTime) + "ms");
		
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
	 * @param rosterID 
	 * @throws Exception 
	 */
	private static CustHierarchyDetailsTO getCustHierarchy(Integer studentId, long rosterID)
			throws Exception {
		return PrismWebServiceDBUtility.getCustomerHigherarchy(studentId,rosterID);
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
	
	/**
	 * Test the web service standalone
	 * @param args
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public static void main(String[] args) throws NumberFormatException, Exception {
		//Edit Student
		if("1".equals(args[0])){
			editStudent(Integer.valueOf(args[1]));
		}else{//Scoring 
			scoring(Long.valueOf(args[1]), Integer.valueOf(args[2]), Long.valueOf(args[3]));
		}
	}
	
}
