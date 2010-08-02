package com.ctb.control.testAdmin; 

import com.bea.control.*;
import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.Program;
import com.ctb.bean.testAdmin.ProgramData;
import com.ctb.bean.testAdmin.ProgramStatusSession;
import com.ctb.bean.testAdmin.ProgramStatusSessionData;
import com.ctb.bean.testAdmin.SubtestStatusCount;
import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestElementData;
import com.ctb.bean.testAdmin.TestStatus;
import com.ctb.control.db.testAdmin.CustomerConfigurations;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.testAdmin.OrgNodeDataNotFoundException;
import com.ctb.exception.testAdmin.ProgramStatusNotFoundException;
import com.ctb.exception.testAdmin.TestElementDataNotFoundException;
import com.ctb.exception.testAdmin.UserProgramsNotFoundException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Hashtable;
import org.apache.beehive.controls.api.bean.ControlImplementation;

/**
 * Platform control provides functions related to program status.
 * 
 * @author John_Wang
 * @editor-info:code-gen control-interface="true"
 */
@ControlImplementation()
public class ProgramStatusImpl implements ProgramStatus, Serializable
{ 
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.TestAdmin testAdmin;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.testAdmin.CustomerConfigurations customerConfigurations;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.Users users;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.Customer customer;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.OrgNode orgNode;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.TestRoster testRoster;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.StudentItemSetStatus studentItemSetStatus;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.ItemSet itemSet;

    static final long serialVersionUID = 1L;
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.validation.Validator validator;
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.CustomerReportBridge reportBridge;
    
    private static final String CUSTOMER_CONFIG_PROGRAM_STATUS = "Program_Status";

    /**
     * Retrieves the set of active programs available to a user's customer
     * @common:operation
     * @param userName - identifies the user
     * @return ProgramData
	 * @throws com.ctb.exception.CTBBusinessException
     */
    public ProgramData getActiveProgramsForUser(String userName, FilterParams filter, PageParams page, SortParams sort)throws CTBBusinessException{
        validator.validate(userName, null, "testAdmin.getCustomerProgramsForOrg");
        try{
            ProgramData progsData = new ProgramData();
            Program [] programs = reportBridge.getActiveProgramsForUser(userName);
            Integer pageSize = null;
            if(page != null) {
                pageSize = new Integer(page.getPageSize());
            }
            progsData.setPrograms(programs,pageSize) ;
            if(filter != null) progsData.applyFiltering(filter);
            if(sort != null) progsData.applySorting(sort);
            if(page != null) progsData.applyPaging(page);
            return progsData;
        }catch(SQLException se){
            UserProgramsNotFoundException upnf = new UserProgramsNotFoundException("ProgramStatusImpl: getCustomerProgramsForOrg: " + se.getMessage());
            upnf.setStackTrace(se.getStackTrace());
            throw upnf;
        }
    }
    
    /**
     * Retrieves a filtered, sorted, paged list of tests under the specified program.
     * @common:operation
     * @param userName - identifies the user
     * @param programId - identifies the program
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return TestElementData
	 * @throws com.ctb.exception.CTBBusinessException
     */
    public TestElementData getTestsForProgram(String userName, Integer programId, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
    {
//        validator.validateProduct(userName, programId, "testAdmin.getTestsForProgram");
        try {
            TestElementData ted = new TestElementData();
            Integer pageSize = null;
            if(page != null) {
                pageSize = new Integer(page.getPageSize());
            }
            ted.setTestElements(itemSet.getTestsForProgram(userName, programId), pageSize);
            if(filter != null) ted.applyFiltering(filter);
            if(sort != null) ted.applySorting(sort);
            if(page != null) ted.applyPaging(page);
            TestElement [] tests = ted.getTestElements();
            for(int i=0;i<tests.length && tests[i] != null;i++) {
                TestElement test = tests[i];
                test.setForms(itemSet.getFormsForTest(test.getItemSetId()));
            }
            return ted;
        } catch (SQLException se) {
            TestElementDataNotFoundException tee = new TestElementDataNotFoundException("ProgramStatusImpl: getTestsForProgram: " + se.getMessage());
            tee.setStackTrace(se.getStackTrace());
            throw tee;
        }
    }
    
    /**
     * Retrieves a list of subtest status for the specified item set id under the specified program 
     * at/below specified organization. 
     * @common:operation
     * @param userName - identifies the user
     * @param programId - identifies the program
     * @param orgNodeId - identifies the organization
     * @param itemSetId - identifies the item set (TC)
	 * @return TestStatus []
	 * @throws com.ctb.exception.CTBBusinessException
     */
    public TestStatus [] getSubtestStatusForProgram(String userName, Integer programId, Integer orgNodeId, Integer itemSetId) throws CTBBusinessException
    {
        validator.validateNode(userName, orgNodeId, "testAdmin.getSubtestStatusForProgram");
        validator.validateItemSet(userName, itemSetId, "testAdmin.getSubtestStatusForProgram");
        try {
            SubtestStatusCount [] subtestStatusCounts = studentItemSetStatus.getSubtestStatusCount(programId, orgNodeId, itemSetId);
            
            Hashtable countHash = new Hashtable();
            for(int i=0;i<subtestStatusCounts.length && subtestStatusCounts[i] != null;i++) {
                String key = subtestStatusCounts[i].getItemSetId().toString()+subtestStatusCounts[i].getStatus();
                if (countHash.containsKey(key)) {
                    Integer count = (Integer) countHash.get(key);
                    int newCount = count.intValue() + subtestStatusCounts[i].getRosterCount().intValue();
                    countHash.put(key, new Integer(newCount));
                }
                else {
                    int newCount = subtestStatusCounts[i].getRosterCount().intValue();
                    countHash.put(key, new Integer(newCount));
                }
            }
            
            
            TestElement [] tests =  itemSet.getTestElementsForParent(itemSetId, "TS");
            TestStatus [] testStatus = new TestStatus[tests.length];
            for(int i=0;i<tests.length && tests[i] != null;i++) {
                testStatus[i] = new TestStatus();
                testStatus[i].setItemSetId(tests[i].getItemSetId());
                testStatus[i].setItemSetName(tests[i].getItemSetName());
                testStatus[i].setItemSetType(tests[i].getItemSetType());
                testStatus[i].setItemSetCategoryId(tests[i].getItemSetCategoryId());
                
                Integer count = (Integer) countHash.get(testStatus[i].getItemSetId().toString()+"Scheduled");
                if (count == null)
                    count = new Integer(0);
                testStatus[i].setScheduledCount(count);
                
                count = (Integer) countHash.get(testStatus[i].getItemSetId().toString()+"Started");
                if (count == null)
                    count = new Integer(0);
                testStatus[i].setStartedCount(count);
                
                count = (Integer) countHash.get(testStatus[i].getItemSetId().toString()+"Completed");
                if (count == null)
                    count = new Integer(0);
                testStatus[i].setCompletedCount(count);
                
            }
            return testStatus;
        } catch (SQLException se) {
            ProgramStatusNotFoundException tee = new ProgramStatusNotFoundException("ProgramStatusImpl: getSubtestStatusForProgram: " + se.getMessage());
            tee.setStackTrace(se.getStackTrace());
            throw tee;
        }
    }
    
    
    /**
     * Retrieves test status for the specified item set id under the specified program 
     * at/below specified organization. 
     * @common:operation
     * @param userName - identifies the user
     * @param programId - identifies the program
     * @param orgNodeId - identifies the organization
     * @param itemSetId - identifies the item set (TC)
	 * @return TestStatus []
	 * @throws com.ctb.exception.CTBBusinessException
     */
    public TestStatus getTestStatusForProgram(String userName, Integer programId, Integer orgNodeId, Integer itemSetId) throws CTBBusinessException
    {
        validator.validateNode(userName, orgNodeId, "testAdmin.getTestStatusForProgram");
        validator.validateItemSet(userName, itemSetId, "testAdmin.getTestStatusForProgram");
        try {
            SubtestStatusCount [] subtestStatusCounts = studentItemSetStatus.getTestStatusCount(programId, orgNodeId, itemSetId);
            
            Hashtable countHash = new Hashtable();
            for(int i=0;i<subtestStatusCounts.length && subtestStatusCounts[i] != null;i++) {
                String key = subtestStatusCounts[i].getItemSetId().toString()+subtestStatusCounts[i].getStatus();
                if (countHash.containsKey(key)) {
                    Integer count = (Integer) countHash.get(key);
                    int newCount = count.intValue() + subtestStatusCounts[i].getRosterCount().intValue();
                    countHash.put(key, new Integer(newCount));
                }
                else {
                    int newCount = subtestStatusCounts[i].getRosterCount().intValue();
                    countHash.put(key, new Integer(newCount));
                }
            }
            
            
            TestElement te =  itemSet.getTestElementById(itemSetId);
            TestStatus testStatus = new TestStatus();
            testStatus.setItemSetId(te.getItemSetId());
            testStatus.setItemSetName(te.getItemSetName());
            testStatus.setItemSetType(te.getItemSetType());
            testStatus.setItemSetCategoryId(te.getItemSetCategoryId());
            
            Integer count = (Integer) countHash.get(te.getItemSetId().toString()+"Scheduled");
            if (count == null)
                count = new Integer(0);
            testStatus.setScheduledCount(count);
            
            count = (Integer) countHash.get(te.getItemSetId().toString()+"Started");
            if (count == null)
                count = new Integer(0);
            testStatus.setStartedCount(count);
            
            count = (Integer) countHash.get(te.getItemSetId().toString()+"Completed");
            if (count == null)
                count = new Integer(0);
            testStatus.setCompletedCount(count);
                
            return testStatus;
        } catch (SQLException se) {
            ProgramStatusNotFoundException tee = new ProgramStatusNotFoundException("ProgramStatusImpl: getTestStatusForProgram: " + se.getMessage());
            tee.setStackTrace(se.getStackTrace());
            throw tee;
        }
    }

    /**
     * Retrieves the set of sessions for the specified item set id under the specified program 
     * at/below specified organization. 
     * @common:operation
     * @param userName - identifies the user
     * @param programId - identifies the program
     * @param orgNodeId - identifies the organization
     * @param itemSetIdTC - identifies the item set (TC)
     * @param itemSetIdTS - identifies the item set (TS)
     * @param status - identifies completion status (Scheduled, Started, Completed)
     * @return ProgramStatusSessionData
	 * @throws com.ctb.exception.CTBBusinessException
     */
    public ProgramStatusSessionData getProgramStatusSessions(String userName, Integer programId, Integer orgNodeId, Integer itemSetIdTC, Integer itemSetIdTS, String status, FilterParams filter, PageParams page, SortParams sort)throws CTBBusinessException{
        validator.validate(userName, null, "testAdmin.getProgramStatusSessions");
        try{
            ProgramStatusSessionData progsData = new ProgramStatusSessionData();
            ProgramStatusSession [] programs = testRoster.getProgramStatusSessions(programId, orgNodeId, itemSetIdTC, itemSetIdTS, status);
            
            for (int i=0; i < programs.length; i++) {
                if (programs[i] != null) {
                    Integer testAdminId = programs[i].getTestAdminId();
                    String visible = testAdmin.checkVisibility(userName, testAdminId);
                    programs[i].setVisible(visible);
                }
            }
            
            Integer pageSize = null;
            if(page != null) {
                pageSize = new Integer(page.getPageSize());
            }
            progsData.setProgramStatusSessions(programs,pageSize) ;
            if(filter != null) progsData.applyFiltering(filter);
            if(sort != null) progsData.applySorting(sort);
            if(page != null) progsData.applyPaging(page);
            return progsData;
        }catch(SQLException se){
            ProgramStatusNotFoundException upnf = new ProgramStatusNotFoundException("ProgramStatusImpl: getProgramStatusSessions: " + se.getMessage());
            upnf.setStackTrace(se.getStackTrace());
            throw upnf;
        }
    }
    

    /**
     * Retrieves the bottom two org node category ids in an array
     * @common:operation
     * @param userName - identifies the user
     * @param customerId - identifies the customer
     * @return Integer []
	 * @throws com.ctb.exception.CTBBusinessException
     */
    public Integer [] getBottomTwoOrgNodeCategoryIdsForCustomer(String userName, Integer customerId)throws CTBBusinessException{
        validator.validate(userName, null, "testAdmin.getBottomTwoOrgNodeCategoryIdsForCustomer");
        validator.validateCustomer(userName, customerId, "testAdmin.getBottomTwoOrgNodeCategoryIdsForCustomer");
        try{
            Integer [] categoryIds = orgNode.getOrgNodeCategoryIdsForCustomerFromBottomUp(customerId);
            Integer [] returnIds;
            if (categoryIds.length >=2) 
                returnIds = new Integer[2];
            else
                returnIds = new Integer[categoryIds.length];
                
            for (int i=0; i < returnIds.length; i++) {
                returnIds[i] = categoryIds[i];
            }                
            return returnIds;
        }catch(SQLException se){
            OrgNodeDataNotFoundException upnf = new OrgNodeDataNotFoundException("ProgramStatusImpl: getBottomTwoOrgNodeCategoryIdsForCustomer: " + se.getMessage());
            upnf.setStackTrace(se.getStackTrace());
            throw upnf;
        }
    }

    

    /**
     * @common:operation
     */
    public Boolean hasProgramStatusConfig(String userName) throws CTBBusinessException{
        try {
            Boolean hasProgramStatusConfig = Boolean.FALSE;
            Integer customerId = users.getCustomerIdForName(userName);
            if(customerId != null){
                CustomerConfiguration[] customerConfigs = customerConfigurations.getCustomerConfigurations(customerId.intValue());
                if(customerConfigs != null && customerConfigs.length>0 ){
                    for (int i = 0; i<customerConfigs.length; i++){
                        CustomerConfiguration customerConfig = customerConfigs[i];
                        if((CUSTOMER_CONFIG_PROGRAM_STATUS.equals(customerConfig.getCustomerConfigurationName())) 
                                    && ("T".equals(customerConfig.getDefaultValue()))){
                            hasProgramStatusConfig = Boolean.TRUE;
                        }
                    }
                }
            }
            return hasProgramStatusConfig;
        } 
        catch (Exception e){ 
			e.printStackTrace();
            throw new CTBBusinessException("Program Status Config");
        }
    }
} 
