/**
 * 
 */
package com.ctb.prism.web.handler;

import java.net.URL;
import java.sql.Connection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;

import weblogic.utils.StringUtils;

import com.ctb.prism.web.constant.PrismWebServiceConstant;
import com.ctb.prism.web.controller.ContentDetailsTO;
import com.ctb.prism.web.controller.CustHierarchyDetailsTO;
import com.ctb.prism.web.controller.OrgDetailsTO;
import com.ctb.prism.web.controller.RosterDetailsTO;
import com.ctb.prism.web.controller.SampleWebservice;
import com.ctb.prism.web.controller.StudentBioTO;
import com.ctb.prism.web.controller.StudentDataLoadTO;
import com.ctb.prism.web.controller.StudentDetailsTO;
import com.ctb.prism.web.controller.StudentListTO;
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
				String urlLocation = PrismWebServiceDBUtility.getPrismWSURL(Integer.parseInt(customerId));
				System.out.println("PrismWebServiceHandler.getService : Prism Web Service URL Location : -> " + urlLocation);
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
	 * @param logkey 
	 * @param con 
	 * @throws Exception
	 */
	private static void invokePrismWebService(StudentListTO studentListTO, String customerId, String orgNodeCode, String heirarchyLevel, Integer studentId, long rosterId, long sessionId, String wsType, int hitCount, long logkey, Connection con) throws Exception{
		XStream xstream = new XStream();
		System.out.println("Data forwarded XML structure for student id : " + studentId + " roster id : " + rosterId + " session id : " + sessionId  + "\n" + xstream.toXML(studentListTO));
		long errorLogKey = 0;
		String errorMessage = "";
		StudentDataLoadTO responseTO = null;
		String additionalInfo = "";
		try{
			service=null; // to build the service each time 
			getService(customerId, orgNodeCode, heirarchyLevel);
			if(service != null){
				responseTO = service.loadStudentData(studentListTO);
				System.out.println("Prism Process Id : " + responseTO.getProcessId());
				System.out.println("Prism Partition Name : " + responseTO.getPartitionName());
				if(responseTO.getStatusCode() == 1){ //Success
					System.out.println("PrismWebServiceHandler.invokePrismWebService : Prism Web Service successfully invoked.");
					if(logkey != 0){
						System.out.println("Prism Web Service Successfully invoked for ws error log key : " + logkey);
						PrismWebServiceDBUtility.delWSErrorLog(logkey, con);
					}
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
			if(hitCount == 0){
				errorLogKey = PrismWebServiceDBUtility.insertWSErrorLog(studentId, rosterId, sessionId, wsType, errorMessage, additionalInfo);
				System.out.println("Prism web service error log key : " + errorLogKey);
			}else if(hitCount  < PrismWebServiceConstant.numberOfFailedHitCnt){
				errorLogKey = logkey;
				PrismWebServiceDBUtility.updateWSErrorLog(errorLogKey, hitCount , errorMessage, "Progress", additionalInfo, con);
			}else{
				errorLogKey = logkey;
				PrismWebServiceDBUtility.updateWSErrorLog(errorLogKey, PrismWebServiceConstant.numberOfFailedHitCnt , errorMessage, "Failed", additionalInfo, con);
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
	 * @param logkey 
	 * @param con 
	 * @throws Exception
	 */
	public static void editStudent(Integer studentId, int hitCount, long logkey, Connection con) throws Exception{
		long startTime = System.currentTimeMillis();
		System.out.println("PrismWebServiceHandler.editStudent : Prism Web Service Edit Student started for student id - " + studentId);
		StudentListTO studentListTO = new StudentListTO();
		
		List<RosterDetailsTO> rosterDetailsList = studentListTO.getRosterDetailsTO();
		
		
		List<Long> rosterIds = PrismWebServiceDBUtility.getRosterListForStudent(studentId);
		
		String customerId = null;
		String orgNodeCode = null;
		String heirarchyLevel = null;
		
		boolean rosterAvailable = false;
		for(long rosterID : rosterIds){
			rosterAvailable = true;
			RosterDetailsTO rosterDetailsTO = new RosterDetailsTO();
			
			StudentDetailsTO studentDetailsTO = getStdentBio(studentId);
			studentDetailsTO.setStudentDemoTO(PrismWebServiceDBUtility.getStudentDemo(rosterID));
			//TODO - get the student survey details and put it in the studentDetailsTO
			rosterDetailsTO.setStudentDetailsTO(studentDetailsTO);
			
			CustHierarchyDetailsTO custHierarchyDetailsTO = getCustHierarchy(studentId,rosterID);
			customerId = custHierarchyDetailsTO.getCustomerId();
			String orgNodeCodeList = "";
			for (int i=0;i<custHierarchyDetailsTO.getCollOrgDetailsTO().size();i++)
			{
				OrgDetailsTO orgDetails = custHierarchyDetailsTO.getCollOrgDetailsTO().get(i);
				if (i>0) orgNodeCodeList += "~";
            	orgNodeCodeList += ((orgDetails.getOrgCode()==null)?"":orgDetails.getOrgCode());
				heirarchyLevel = orgDetails.getOrgLevel();
			}
			orgNodeCode = orgNodeCodeList;
			System.out.println("WS/EditStudent orgNodeCodeList: "+orgNodeCodeList);
			
			rosterDetailsTO.setCustHierarchyDetailsTO(custHierarchyDetailsTO);
			
			rosterDetailsTO.setRosterId(String.valueOf(rosterID));
			rosterDetailsList.add(rosterDetailsTO);
		}
		if(rosterAvailable){
			invokePrismWebService(studentListTO, customerId, orgNodeCode, heirarchyLevel, studentId, 0, 0, "Edit Student", hitCount, logkey, con);
		}else{
			System.out.println("PrismWebServiceHandler.editStudent : No Roster availabe for the student id : " + studentId + ". Web Service not invoked.");
		}
		System.out.println("PrismWebServiceHandler.editStudent : Prism Web Service Edit Student ended for student id - " + studentId);
		
		long stopTime = System.currentTimeMillis();
		System.out.println("Time taken to call the Edit Student Web Service : " + (stopTime - startTime) + "ms");
		
	}
	
	/**
	 * Web Service call for Scoring
	 * @param rosterId
	 * @param studentId
	 * @param sessionId
	 * @param logkey 
	 * @param con 
	 * @throws Exception
	 */
	public static void scoring(long rosterId, Integer studentId, long sessionId, int hitCount, long logkey, Connection con) throws Exception{
		if(PrismWebServiceDBUtility.checkValidRosterStatus(rosterId)){
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
			String orgNodeCodeList = "";
			for (int i=0;i<custHierarchyDetailsTO.getCollOrgDetailsTO().size();i++)
			{
				OrgDetailsTO orgDetails = custHierarchyDetailsTO.getCollOrgDetailsTO().get(i);
				if (i>0) orgNodeCodeList += "~";
	        	orgNodeCodeList += ((orgDetails.getOrgCode()==null)?"":orgDetails.getOrgCode());
				heirarchyLevel = orgDetails.getOrgLevel();
			}
			orgNodeCode = orgNodeCodeList;
			System.out.println("WS/Scoring orgNodeCodeList: "+orgNodeCodeList);
			
			rosterDetailsTO.setCustHierarchyDetailsTO(custHierarchyDetailsTO);
			
			StudentDetailsTO studentDetailsTO = getStdentBio(studentId);
			studentDetailsTO.setStudentDemoTO(PrismWebServiceDBUtility.getStudentDemo(rosterId));
			studentDetailsTO.setStudentSurveyBioTO(PrismWebServiceDBUtility.getStudentSurveyBio(rosterId));
			rosterDetailsTO.setStudentDetailsTO(studentDetailsTO);
			
			List<ContentDetailsTO> contentDetailsTOList =  PrismWebServiceDBUtility.getContentDetailsTO(rosterId, studentId, sessionId);
			rosterDetailsTO.getCollContentDetailsTO().addAll(contentDetailsTOList);
			
			rosterDetailsTO.setRosterId(String.valueOf(rosterId));
			rosterDetailsList.add(rosterDetailsTO);
			invokePrismWebService(studentListTO, customerId, orgNodeCode, heirarchyLevel, studentId, rosterId, sessionId, "Scoring", hitCount, logkey, con);
			System.out.println("PrismWebServiceHandler.scoring : Prism Web Service Scoring ended for student id - " + studentId + " rosterId - " + rosterId + " sessionId - " + sessionId);
			
			long stopTime = System.currentTimeMillis();
			System.out.println("Time taken to call the Scoring Web Service : " + (stopTime - startTime) + "ms");
		}else{
			System.out.println("Web Service is not invoked. Roster is in SC or NT status. Roster Id : " + rosterId);
		}
		
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
	
}
