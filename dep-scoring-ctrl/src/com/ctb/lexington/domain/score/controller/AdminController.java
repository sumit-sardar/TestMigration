package com.ctb.lexington.domain.score.controller;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.data.AdminData;
import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.irsdata.IrsSessionDimData;
import com.ctb.lexington.db.irsdata.IrsAssessmentDimData;
import com.ctb.lexington.db.irsdata.IrsProductDimData;
import com.ctb.lexington.db.irsdata.IrsCustomerDimData;
import com.ctb.lexington.db.irsdata.IrsProgramDimData;
import com.ctb.lexington.db.irsdata.IrsSchedulerDimData;

import com.ctb.lexington.db.mapper.IrsSessionDimMapper;
import com.ctb.lexington.db.mapper.IrsAssessmentDimMapper;
import com.ctb.lexington.db.mapper.IrsProductDimMapper;
import com.ctb.lexington.db.mapper.IrsCustomerDimMapper;
import com.ctb.lexington.db.mapper.IrsProgramDimMapper;
import com.ctb.lexington.db.mapper.IrsSchedulerDimMapper;


/**
 * @author Rama_Rao
 *
 */

public class AdminController {
    private Connection conn;
    private AdminData data;
    private CurriculumData currData;
    private Long studentGradeId;
  
    private IrsSchedulerDimMapper irsSchedulerDimMapper;
	private IrsSessionDimMapper irsSessionDimMapper;
	private IrsAssessmentDimMapper irsAssessmentDimMapper;
	private IrsProductDimMapper irsProductDimMapper;
	private IrsCustomerDimMapper irsCustomerDimMapper;
	private IrsProgramDimMapper irsProgramDimMapper;
	
	public AdminController(Connection conn, AdminData adminData, CurriculumData currData, Long studentGradeId){
		this.data = adminData;
        this.currData = currData;
        this.studentGradeId = studentGradeId;
		this.conn = conn;
	}
	  
    public void run() throws SQLException {
    	this.irsCustomerDimMapper = new IrsCustomerDimMapper(conn);
    	IrsCustomerDimData newCustomer = getIrsCustomerBean(data);
    	
    	IrsCustomerDimData customer = irsCustomerDimMapper.findByCustomerId(newCustomer.getCustomerid());
    	if(customer == null){
    		irsCustomerDimMapper.insert(newCustomer);
    	}else{
    		if(!customer.equals(newCustomer)){
    			newCustomer.setCustomerid(customer.getCustomerid());
    			irsCustomerDimMapper.update(newCustomer);
    		}
    	}
        
        this.irsProgramDimMapper = new IrsProgramDimMapper(conn);
    	IrsProgramDimData newProgram = getIrsProgramBean(data);
        
    	IrsProgramDimData program = irsProgramDimMapper.findByProgramId(newProgram.getProgramid());
    	if(program == null){
    			irsProgramDimMapper.insert(newProgram);
    	}else{
    		if(!program.equals(newProgram)){
                if(!program.getCustomerid().equals(newProgram.getCustomerid())) {
                    throw new RuntimeException("Critical Error in scoring! Same program id used for different customers.");
                }
    			newProgram.setProgramid(program.getProgramid());
    			irsProgramDimMapper.update(newProgram);
    		}
    	}
    	
    	this.irsProductDimMapper = new IrsProductDimMapper(conn);
    	IrsProductDimData newProduct = getIrsProductBean(data);
    	
    	IrsProductDimData product = irsProductDimMapper.findByProductId(newProduct.getProductid());
    	if(product == null){
    		irsProductDimMapper.insert(newProduct);
    	}else{
    		if(!product.equals(newProduct)){
    			newProduct.setProductid(product.getProductid());
    			irsProductDimMapper.update(newProduct);
    		}
    	}
        
        this.irsAssessmentDimMapper = new IrsAssessmentDimMapper(conn);
    	IrsAssessmentDimData newAssessment = getIrsAssessmentBean(data);
        
    	IrsAssessmentDimData assessment = irsAssessmentDimMapper.findByAssessmentId(newAssessment.getAssessmentid());
    	if(assessment == null){
    		irsAssessmentDimMapper.insert(newAssessment);
    	}else{
    		if(!assessment.equals(newAssessment)){
    			newAssessment.setAssessmentid(assessment.getAssessmentid());
    			irsAssessmentDimMapper.update(newAssessment);
    		}
    	}
    	
    	this.irsSchedulerDimMapper = new IrsSchedulerDimMapper(conn);
    	IrsSchedulerDimData newSchedulerData = getIrsSchedulerBean(data);
    	
    	IrsSchedulerDimData schedulerData = irsSchedulerDimMapper.findBySchedulerId(newSchedulerData.getSchedulerid());
    	if(schedulerData == null){
    		irsSchedulerDimMapper.insert(newSchedulerData);
    	}else{
    		if(!schedulerData.equals(newSchedulerData)){
    			newSchedulerData.setSchedulerid(schedulerData.getSchedulerid());
    			irsSchedulerDimMapper.update(newSchedulerData);
    		}
    	}
    	
        this.irsSessionDimMapper = new IrsSessionDimMapper(conn);
    	IrsSessionDimData newSessionData = getIrsSessionBean(data);
        
    	IrsSessionDimData sessionData = irsSessionDimMapper.findBySessionId(newSessionData.getSessionid());
    	if(sessionData == null){
            try {
                irsSessionDimMapper.insert(newSessionData);
            } catch (SQLException sqe) {
                // contentious session inserts are common, try to handle here
                sessionData = irsSessionDimMapper.findBySessionId(newSessionData.getSessionid());
                if(sessionData == null) {
                    throw sqe;
                } else if(!sessionData.equals(newSessionData)){
                    newSessionData.setSessionid(sessionData.getSessionid());
                    irsSessionDimMapper.update(newSessionData);
                }
            }
    	} else{
    		if(!sessionData.equals(newSessionData)){
    			newSessionData.setSessionid(sessionData.getSessionid());
    			irsSessionDimMapper.update(newSessionData);
    		}
    	}
    }	

    private IrsCustomerDimData getIrsCustomerBean(AdminData data) {
            IrsCustomerDimData customer = new IrsCustomerDimData();
            customer.setCustomerid(data.getCustomerId());
            customer.setName(data.getCustomerName());
            customer.setKey(data.getCustomerKey());
            return customer;
    }
    
    private IrsProgramDimData getIrsProgramBean(AdminData data){
    	IrsProgramDimData programData = new IrsProgramDimData();
    	programData.setProgramid(data.getProgramId());
    	programData.setName(data.getProgramName());
    	programData.setProgStartDate(data.getProgStartDate());
    	programData.setProgEndDate(data.getProgEndDate());
    	programData.setCustomerid(data.getCustomerId());
        programData.setAgeCategory(studentGradeId.longValue()==1?"AD":"JV");
    	programData.setProductTypeid(new Long(
            "TB".equals(data.getAssessmentType())?1:
            "TL".equals(data.getAssessmentType())?1:
            "TN".equals(data.getAssessmentType())?2:
            "TV".equals(data.getAssessmentType())?2:
            "LL".equals(data.getAssessmentType())?4: // For Laslink Scoring
            "TA".equals(data.getAssessmentType())?5:3)); // For Tabe Adaptive Scoring
        programData.setNormsGroup(
            "6".equals(data.getNormsGroup())?"Fall":
            "18".equals(data.getNormsGroup())?"Winter":
            "19".equals(data.getNormsGroup())?"Winter":
            "30".equals(data.getNormsGroup())?"Spring":
            "31".equals(data.getNormsGroup())?"Spring":
            data.getNormsGroup());
        if(programData.getNormsGroup() == null) programData.setNormsGroup("N/A");
        programData.setNormsYear(data.getNormsYear());
        return programData;
    }
    
    private IrsSchedulerDimData getIrsSchedulerBean(AdminData data){
    	IrsSchedulerDimData schedulerData = new IrsSchedulerDimData();
    	schedulerData.setSchedulerid(data.getSchedulerId());
    	schedulerData.setName(data.getSchedulerName());
    	return schedulerData;
    }
    
    private IrsSessionDimData getIrsSessionBean(AdminData data){
    	IrsSessionDimData sessionData = new IrsSessionDimData();
    	sessionData.setSessionid(data.getSessionId());
    	sessionData.setProgramid(data.getProgramId());
    	sessionData.setNumberOfStudents(data.getNumberOfStudents());
    	sessionData.setSessionid(data.getSessionId());
    	sessionData.setWindowStartDate(data.getWindowStartDate());
    	sessionData.setWindowEndDate(data.getWindowEndDate());
        sessionData.setSchedulerid(data.getSchedulerId());
        sessionData.setShowGE(data.getShowGE());
    	return sessionData;
    } 
    
    private IrsProductDimData getIrsProductBean(AdminData data){
    	IrsProductDimData productData = new IrsProductDimData();
    	productData.setProductid(data.getProductId());
    	productData.setName(data.getproductName());
        productData.setProductTypeid(new Long(
            "TB".equals(data.getAssessmentType())?1:
            "TL".equals(data.getAssessmentType())?1:
            "TN".equals(data.getAssessmentType())?2:
            "TV".equals(data.getAssessmentType())?2:
            "LL".equals(data.getAssessmentType())?4:
            "TA".equals(data.getAssessmentType())?5:3)); // For Laslink Scoring
    	return productData;
    }
    
    private IrsAssessmentDimData getIrsAssessmentBean(AdminData data){
    	IrsAssessmentDimData assessmentData = new IrsAssessmentDimData();
    	assessmentData.setAssessmentid(data.getAssessmentId());
    	assessmentData.setName(data.getAssessmentName());
    	assessmentData.setProductid(data.getProductId());
    	assessmentData.setType(data.getAssessmentType());
        assessmentData.setProductid(data.getProductId());
        assessmentData.setProductTypeid(new Long(
            "TB".equals(data.getAssessmentType())?1:
            "TL".equals(data.getAssessmentType())?1:
            "TN".equals(data.getAssessmentType())?2:
            "TV".equals(data.getAssessmentType())?2:
            "LL".equals(data.getAssessmentType())?4:
            "TA".equals(data.getAssessmentType())?5:3)); // For Laslink Scoring
        
        // this is WRONG!!! what if they didn't complete any content area?
        String form = "N/A";
        String level = "N/A";
        if(currData != null && currData.getContentAreas() != null && currData.getContentAreas().length > 0) {
            form = currData.getContentAreas()[0].getSubtestForm();
            level = currData.getContentAreas()[0].getSubtestLevel();
        }
        assessmentData.setFormid(new Long("9".equals(form)?1:
                                          "10".equals(form)?2:
                                          "B".equals(form)?4:
                                          "G".equals(form)?5:
                                          "1".equals(form)?4:
                                          "A".equals(form)?7:
                                          "B".equals(form)?8:
                                          ("Espa?ol".equals(form)|| "Espanol".equals(form) || "Español".equals(form))?9: // For Laslink Scoring
                                          "CAT".equals(form)?13:14)); 
        if("TV".equals(data.getAssessmentType())) {
            assessmentData.setLevelid(                                  
                                          new Long(
                                            "13".equals(level)?7:
                                            "14".equals(level)?8:
                                            "15".equals(level)?9:
                                            "16".equals(level)?10:
                                            "17".equals(level)?11:
                                            "18".equals(level)?12:
                                            "19".equals(level)?13:
                                            "19-20".equals(level)?13:
                                            "19/20".equals(level)?13:
                                            "20".equals(level)?14:15));
        } 
         // For Laslink Scoring
       else if("LL".equals(data.getAssessmentType())) {
            assessmentData.setLevelid(                                  
                                          new Long(
                                        	"K".equals(level)?16:
                                        	"1".equals(level)?17:
                                        	"2-3".equals(level)?18:
                                        	"4-5".equals(level)?19:
                                        	"6-8".equals(level)?20:
                                        	"9-12".equals(level)?21:22 ));
        } 
     // For Tabe Adaptive Scoring
       else if("TA".equals(data.getAssessmentType())) {
            assessmentData.setLevelid(                                  
                                          new Long(
                                        	"Adaptive".equals(level)?23:24 ));
        } else {
            assessmentData.setLevelid(new Long(6));
        }
    	return assessmentData;
    }
}