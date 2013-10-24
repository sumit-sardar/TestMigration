/**
 * 
 */
package com.ctb.prism.web.handler;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import com.ctb.prism.web.constant.PrismWebServiceConstant;
import com.ctb.prism.web.controller.CustHierarchyDetailsTO;
import com.ctb.prism.web.controller.RosterDetailsTO;
import com.ctb.prism.web.controller.SampleWebservice;
import com.ctb.prism.web.controller.StudentBioTO;
import com.ctb.prism.web.controller.StudentDetailsTO;
import com.ctb.prism.web.controller.StudentListTO;
import com.ctb.prism.web.dbutility.PrismWebServiceDBUtility;
import com.ctb.util.OASLogger;
import com.sun.xml.internal.ws.client.BindingProviderProperties;

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
				ResourceBundle rb = ResourceBundle.getBundle("PrismWebService");
				String urlLocation = rb.getString("url");
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
	public static void editStudent(Integer studentId) throws Exception{
		OASLogger.getLogger(PrismWebServiceConstant.loggerName).info("PrismWebServiceHandler.editStudent : Prism Web Service Edit Student started for student id - " + studentId);
		StudentListTO studentListTO = new StudentListTO();
		
		List<RosterDetailsTO> rosterDetailsList = studentListTO.getRosterDetailsTO();
		
		StudentDetailsTO studentDetailsTO = getStdentBio(studentId);
		CustHierarchyDetailsTO custHierarchyDetailsTO = getCustHierarchy(studentId);
		
		List<Long> rosterIds = PrismWebServiceDBUtility.getRosterListForStudent(studentId);
		
		for(long rosterID : rosterIds){
			RosterDetailsTO rosterDetailsTO = new RosterDetailsTO();
			studentDetailsTO.setStudentDemoTO(PrismWebServiceDBUtility.getStudentDemo(rosterID));
			rosterDetailsTO.setStudentDetailsTO(studentDetailsTO);
			rosterDetailsTO.setCustHierarchyDetailsTO(custHierarchyDetailsTO);
			rosterDetailsTO.setRosterId(String.valueOf(rosterID));
			rosterDetailsList.add(rosterDetailsTO);
		}
		
		invokePrismWebService(studentListTO);
		OASLogger.getLogger(PrismWebServiceConstant.loggerName).info("PrismWebServiceHandler.editStudent : Prism Web Service Edit Student ended for student id - " + studentId);
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
	
}
