package com.ctb.control.testAdmin; 

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.beehive.controls.api.bean.ControlImplementation;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.request.FilterParams.FilterParam;
import com.ctb.bean.request.FilterParams.FilterType;
import com.ctb.bean.testAdmin.ActiveSession;
import com.ctb.bean.testAdmin.ActiveSessionData;
import com.ctb.bean.testAdmin.ActiveTest;
import com.ctb.bean.testAdmin.ActiveTestData;
import com.ctb.bean.testAdmin.BroadcastMessageData;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.CustomerConfigurationValue;
import com.ctb.bean.testAdmin.CustomerReport;
import com.ctb.bean.testAdmin.CustomerReportData;
import com.ctb.bean.testAdmin.CustomerSDSData;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.NodeData;
import com.ctb.bean.testAdmin.OrganizationNode;
import com.ctb.bean.testAdmin.Program;
import com.ctb.bean.testAdmin.ProgramData;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.RosterElementData;
import com.ctb.bean.testAdmin.SessionNode;
import com.ctb.bean.testAdmin.SessionNodeData;
import com.ctb.bean.testAdmin.StudentSessionStatus;
import com.ctb.bean.testAdmin.StudentSessionStatusData;
import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestElementData;
import com.ctb.bean.testAdmin.TestProduct;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.TestSessionData;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserNode;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.testAdmin.CustomerConfigurationDataNotFoundException;
import com.ctb.exception.testAdmin.CustomerReportDataNotFoundException;
import com.ctb.exception.testAdmin.OrgNodeDataNotFoundException;
import com.ctb.exception.testAdmin.RosterDataNotFoundException;
import com.ctb.exception.testAdmin.StudentSessionStatusNotFoundException;
import com.ctb.exception.testAdmin.TestAdminDataNotFoundException;
import com.ctb.exception.testAdmin.TestElementDataNotFoundException;
import com.ctb.exception.testAdmin.UserDataNotFoundException;
import com.ctb.exception.testAdmin.UserProgramsNotFoundException;
import com.ctb.exception.validation.ValidationException;
import com.ctb.util.DESUtils;
import com.ctb.util.DateUtils;
import com.ctb.util.SQLutils;
import com.ctb.util.testAdmin.TestAdminStatusComputer;

/**
 * Platform control provides functions related to test session
 * monitoring and status, including methods to obtain lists of
 * test sessions of interest to a particular user.
 * 
 * @author Nate_Cohen, John_Wang
 * @editor-info:code-gen control-interface="true"
 */
@ControlImplementation()
public class TestSessionStatusImpl implements TestSessionStatus, Serializable
{ 
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.Product product;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.testAdmin.CustomerConfigurations customerConfiguration;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.TestAdmin testAdmin;
    
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.CustomerReportBridge reportBridge;
    
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.BroadcastMessageLog message;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.OrgNode orgNode;
    
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.TestRoster roster;
    
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.testAdmin.FormAssignment formAssignments;
    
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.ItemSet itemSet;
    
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.Users users;
    
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.testAdmin.ADS ads;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.validation.Validator validator;
    
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.jms.ScoreStudent scorer;
    
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.CustomerReportBridge customerReports;
    
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.StudentItemSetStatus studentItemSetStatus;

    static final long serialVersionUID = 1L;
    
    private static final int CTB_CUSTOMER_ID =2;
    
    private static final String CUSTOMER_CONFIG_ALLOW_SUBTEST_INVALIDATION = "Allow_Subtest_Invalidation";
    private static final String CUSTOMER_CONFIG_PARTIALLY ="Partially ";

   /**
     * Retrieves the set of online reports available to a user's customer
     * @common:operation
     * @param userName - identifies the user
     * @param orgNodeId - identifies the org node
     * @param  programId - identifies the program
     * @return CustomerReportData
	 * @throws com.ctb.exception.CTBBusinessException
     */
    public CustomerReportData getCustomerReportData(String userName,Integer orgNodeId, Integer programId, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException {
        try {
            validator.validate(userName, null, "testAdmin.getCustomerReportData");
            CustomerReportData crd = new CustomerReportData();
            Integer pageSize = null;
            if(page != null) {
                pageSize = new Integer(page.getPageSize());
            }
//            Integer [] topOrgNodeIds = orgNode.getTopOrgNodeIdsForUser(userName);
//            Integer orgNodeId = topOrgNodeIds[0];
            CustomerReport [] cr = reportBridge.getReportAssignmentsForUser(userName, programId, orgNodeId);
            for (int i=0; i < cr.length; i++) {
                String reportURL = cr[i].getReportUrl();
//              String encryptedProgramId = DESUtils.encrypt(String.valueOf(cr[i].getActiveProgramId()), cr[i].getSystemKey());
                String encryptedProgramId = DESUtils.encrypt(String.valueOf(programId), cr[i].getSystemKey());
//              String paramsPlainText = "NodeInstanceId="+cr[i].getOrgNodeId()
//                  +"&LevelId="+cr[i].getCategoryLevel()+"&Timestamp="+(new Date()).toString();
                String paramsPlainText = "NodeInstanceId="+orgNodeId
                    +"&LevelId="+cr[i].getCategoryLevel()+"&Timestamp="+(new Date()).toString();
                String encryptedParams = DESUtils.encrypt(paramsPlainText, cr[i].getCustomerKey());
                reportURL = reportURL +"?sys="+encryptedProgramId+"&parms="+encryptedParams;
                cr[i].setReportUrl(reportURL);
            }
            crd.setCustomerReports(cr, pageSize);
            if(filter != null) crd.applyFiltering(filter);
            if(sort != null) crd.applySorting(sort);
            if(page != null) crd.applyPaging(page);
            return crd;
        } catch (SQLException se) {
            CustomerReportDataNotFoundException tee = new CustomerReportDataNotFoundException("ScheduleTestImpl: getCustomerReportData: " + se.getMessage());
            tee.setStackTrace(se.getStackTrace());
            throw tee;
        }
    }
    
    /**
     * Retrieves the set of online reports available to a user's customer
     * @common:operation
     * @param userName - identifies the user
     * @param testRosterId - identifies the test roster
     * @return String
	 * @throws com.ctb.exception.CTBBusinessException
     */
    public String getIndividualReportUrl(String userName, Integer testRosterId) throws CTBBusinessException {
        System.out.println("*****  Generating Ind. report URL: ");
        String reportURL = null;
        try {
            RosterElement re = roster.getRosterElement(testRosterId);
            TestSession session = testAdmin.getTestAdminDetails(re.getTestAdminId());
            
            validator.validateAdmin(userName, re.getTestAdminId(), "testAdmin.getTestSessionDetails");

            String orgNodeId = String.valueOf(session.getCreatorOrgNodeId());
            System.out.println("*****  orgNodeId: " + orgNodeId);
            String sessionId = String.valueOf(re.getTestAdminId());
            System.out.println("*****  sessionId: " + sessionId);
            String programId = String.valueOf(session.getProgramId());
            System.out.println("*****  programId: " + programId);
            String studentId = String.valueOf(re.getStudentId());
            System.out.println("*****  studentId: " + studentId);
            String studentName = re.getLastName() + ", " + re.getFirstName();
            System.out.println("*****  studentName: " + studentName);
            String testId = String.valueOf(session.getTestCatalogId());
            System.out.println("*****  testId: " + testId);
            String systemKey = null;
            String customerKey = null;
            String orgCategoryLevel = null;
            
            CustomerReport [] cr = reportBridge.getReportAssignmentsForProgram(session.getProgramId(), session.getCreatorOrgNodeId());
            for (int i=0; i < cr.length; i++) {
                String report = cr[i].getReportUrl();
                if(cr[i].getReportName().indexOf("IndividualProfile") >= 0) {
                    reportURL = report;
                    systemKey = cr[i].getSystemKey();
                    customerKey = cr[i].getCustomerKey();
                    orgCategoryLevel = String.valueOf(cr[i].getCategoryLevel());
                }
            }
            System.out.println("*****  clear sys: " + programId);
            String encryptedProgramId = DESUtils.encrypt(String.valueOf(programId), systemKey);
            System.out.println("*****  enc sys: " + encryptedProgramId);
            String paramsPlainText = 
                "Timestamp="+(new Date()).toString()+
                "&LevelId="+orgCategoryLevel+
                "&NodeInstanceId="+orgNodeId+
                "&CurrentTestSessionId="+sessionId+
                "&CurrentStudentId="+studentId+
                "&CurrentStudentName="+studentName;
            System.out.println("*****  clear parms: " + paramsPlainText);
            String encryptedParams = DESUtils.encrypt(paramsPlainText, customerKey);
            System.out.println("*****  enc parms: " + encryptedParams);
            reportURL = reportURL +"?TestID="+testId+"&sys="+encryptedProgramId+"&parms="+encryptedParams+"&RunReport=1";
        } catch (SQLException se) {
            CustomerReportDataNotFoundException tee = new CustomerReportDataNotFoundException("ScheduleTestImpl: getIndividualReportUrl: " + se.getMessage());
            tee.setStackTrace(se.getStackTrace());
            throw tee;
        }
        System.out.println("*****  final URL: " + reportURL);
        return reportURL;
    }
    
    /**
     * Retrieves the set of programs available to a user's customer
     * @common:operation
     * @param userName - identifies the user
     * @return ProgramData
	 * @throws com.ctb.exception.CTBBusinessException
     */
    public ProgramData getProgramsForUser(String userName, FilterParams filter, PageParams page, SortParams sort)throws CTBBusinessException{
        validator.validate(userName, null, "getCustomerProgramsForOrg");
        try{
            ProgramData progsData = new ProgramData();
            Program [] programs = reportBridge.getProgramsForUser(userName);
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
            UserProgramsNotFoundException upnf = new UserProgramsNotFoundException("TestSessionStatusImpl: getCustomerProgramsForOrg: " + se.getMessage());
            upnf.setStackTrace(se.getStackTrace());
            throw upnf;
        }
    }
    /**
     * Retrieves the no of reports  available to a user's customer
     * @common:operation
     * @param userName - identifies the user
     * @param customerId - identifies the customer
     * @return boolean value 
	 * @throws com.ctb.exception.CTBBusinessException
     */
    public boolean userHasReports(String userName,Integer customerId) throws CTBBusinessException{
        validator.validate(userName, customerId, "userHasReports");
        try{            
            Integer noOfReports = reportBridge.getCustomerReports(customerId);            
            if(noOfReports.intValue() > 0){
                 return true;
            }
            return false;
        }catch(SQLException se){
            CustomerReportDataNotFoundException tee = new CustomerReportDataNotFoundException("ScheduleTestImpl: userHasReports: " + se.getMessage());
            tee.setStackTrace(se.getStackTrace());
            throw tee;
        }
    }

    /**
     * Retrieves a filtered, sorted, paged list of Customer SDS names and
     * corresponding tokens for the provided user's customer.
     * @param userName - identifies the user
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return CustomerSDSData
	 * @throws com.ctb.exception.CTBBusinessException
     * @common:operation
     */
    public CustomerSDSData getCustomerSDSListForUser(String userName, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
    {
        validator.validate(userName, null, "testAdmin.getCustomerSDSListForUser");
        try {
            CustomerSDSData csd = new CustomerSDSData();
            Integer pageSize = null;
            if(page != null) {
                pageSize = new Integer(page.getPageSize());
            }
            Integer customerId = users.getCustomerIdForName(userName);
            csd.setCustomerSDSs(ads.getCustomerSDSList(customerId), pageSize);
            if(filter != null) csd.applyFiltering(filter);
            if(sort != null) csd.applySorting(sort);
            if(page != null) csd.applyPaging(page);
            return csd;
        } catch (SQLException se) {
            TestAdminDataNotFoundException tae = new TestAdminDataNotFoundException("TestSessionStatusImpl: getCustomerSDSListForUser: " + se.getMessage());
            tae.setStackTrace(se.getStackTrace());
            throw tae;
        }
    }

    /**
     * Retrieves a filtered, sorted, paged list of test sessions to which the specified user
     * is assigned as a proctor.
     * @param userName - identifies the user
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return TestSessionData
	 * @throws com.ctb.exception.CTBBusinessException
     * @common:operation
     */
    public TestSessionData getTestSessionsForUser(String userName, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
    {
        validator.validate(userName, null, "testAdmin.getTestSessionsForUser");
        try {
            TestSessionData tsd = new TestSessionData();
            Integer pageSize = null;
            if(page != null) {
                pageSize = new Integer(page.getPageSize());
            }
            TestSession [] sessions = testAdmin.getTestAdminsForUser(userName);
            for(int i=0;i<sessions.length;i++) {
                TestAdminStatusComputer.adjustSessionTimesToLocalTimeZone(sessions[i]);
            }
            tsd.setTestSessions(sessions, pageSize);
            if(filter != null) tsd.applyFiltering(filter);
            if(sort != null) tsd.applySorting(sort);
            if(page != null) tsd.applyPaging(page);
            return tsd;
        } catch (SQLException se) {
            TestAdminDataNotFoundException tae = new TestAdminDataNotFoundException("TestSessionStatusImpl: getTestSessionsForUser: " + se.getMessage());
            tae.setStackTrace(se.getStackTrace());
            throw tae;
        }
    }
    
    /**
     * Retrieves a filtered, sorted, paged list of test sessions to which the specified user is assigned as a proctor
	 * @param userName - identifies the user
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return TestSessionData
	 * @throws com.ctb.exception.CTBBusinessException
     * @common:operation
     */
    public TestSessionData getProctorAssignmentsForUser(String userName, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
    {
        validator.validate(userName, null, "testAdmin.getProctorAssignmentsForUser");
        try {
            TestSessionData tsd = new TestSessionData();
            Integer pageSize = null;
            if(page != null) {
                pageSize = new Integer(page.getPageSize());
            }
            TestSession [] sessions = testAdmin.getProctorAssignmentsForUser(userName);
            for(int i=0;i<sessions.length;i++) {
                TestAdminStatusComputer.adjustSessionTimesToLocalTimeZone(sessions[i]);
            }
            tsd.setTestSessions(sessions, pageSize);
            if(filter != null) tsd.applyFiltering(filter);
            if(sort != null) tsd.applySorting(sort);
            if(page != null) tsd.applyPaging(page);
            return tsd;
        } catch (SQLException se) {
            TestAdminDataNotFoundException tae = new TestAdminDataNotFoundException("TestSessionStatusImpl: getProctorAssignmentsForUser: " + se.getMessage());
            tae.setStackTrace(se.getStackTrace());
            throw tae;        
        }
    }
    
    /**
     * Retrieves details for a particular test session
     * @param userName - identifies the user
     * @param testAdminId - identifies the test session of interest
     * @return TestSessionData
     * @throws com.ctb.exception.CTBBusinessException
     * @common:operation
     */
    public TestSessionData getTestSessionDetails(String userName, Integer testAdminId) throws CTBBusinessException
    {
        validator.validateAdmin(userName, testAdminId, "testAdmin.getTestSessionDetails");
        try {
            TestSession [] sessions = new TestSession[1];
            sessions[0] = testAdmin.getTestAdminDetails(testAdminId);
            TestAdminStatusComputer.adjustSessionTimesToLocalTimeZone(sessions[0]);
            TestSessionData tsd = new TestSessionData();
            tsd.setTestSessions(sessions, null);
            return tsd;
        } catch (SQLException se) {
            TestAdminDataNotFoundException tae = new TestAdminDataNotFoundException("TestSessionStatusImpl: getTestSessionDetails: " + se.getMessage());
            tae.setStackTrace(se.getStackTrace());
            throw tae; 
        }
    }
    
    
    /**
     * Retrieves the list of currently active customer specific broadcast messages
     * @param userName - identifies the user
     * @return BroadcastMessageData
     * @throws com.ctb.exception.CTBBusinessException
     * @common:operation
     */
    public BroadcastMessageData getBroadcastMessages(String userName) throws CTBBusinessException
    {
        validator.validate(userName, null, "broadcastMessage.getBroadcastMessages");
        try {
           BroadcastMessageData bmd = new BroadcastMessageData();
           Integer [] prodId = message.getFrameworkProductForUser(userName);
           //System.out.println("getBroadcastMessages productId" + prodId[0].intValue());
           Integer pageSize = null;
           String qString = SQLutils.convertArraytoString(prodId);
           //System.out.println("qString====>"+qString);
           bmd.setBroadcastMessages(message.getProductSpecificBroadcastMsg(qString), null);
           return bmd;
        } catch (SQLException se) {
            TestAdminDataNotFoundException tae = new TestAdminDataNotFoundException("TestSessionStatusImpl: getBroadcastMessages: " + se.getMessage());
            tae.setStackTrace(se.getStackTrace());
            throw tae;         
        }
    }
    
    /**
     * Retrieves the list of all currently active broadcast messages for account manager
     * @param userName - identifies the user
     * @return BroadcastMessageData
     * @throws com.ctb.exception.CTBBusinessException
     * @common:operation
     */
    public BroadcastMessageData getBroadcastMessagesForActManager(String userName) throws CTBBusinessException
    {
        validator.validate(userName, null, "broadcastMessage.getBroadcastMessages");
        try {
           BroadcastMessageData bmd = new BroadcastMessageData();
           Integer pageSize = null;
           bmd.setBroadcastMessages(message.getBroadcastMessages(), null);
           return bmd;
        } catch (SQLException se) {
            TestAdminDataNotFoundException tae = new TestAdminDataNotFoundException("TestSessionStatusImpl: getBroadcastMessages: " + se.getMessage());
            tae.setStackTrace(se.getStackTrace());
            throw tae;         
        }
    }
    
    /**
     * Retrieves a filtered, sorted, paged list of test sessions created at the specified org node
	 * @param userName - identifies the user
     * @param orgNodeId - identifies the org node
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return TestSessionData
	 * @throws com.ctb.exception.CTBBusinessException
     * @common:operation
     */
    public TestSessionData getTestSessionsForOrgNode(String userName, Integer orgNodeId, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
    {
        validator.validateNode(userName, orgNodeId, "testAdmin.getTestSessionsForOrgNode");
        try {
            TestSessionData tsd = new TestSessionData();
            Integer pageSize = null;
            if(page != null) {
                pageSize = new Integer(page.getPageSize());
            }
            TestSession [] sessions = testAdmin.getTestAdminsForOrgNode(orgNodeId);
            tsd.setTestSessions(sessions, pageSize);
            if(filter != null) tsd.applyFiltering(filter);
            if(sort != null) tsd.applySorting(sort);
            if(page != null) tsd.applyPaging(page);
            sessions = tsd.getTestSessions();
            for(int i=0;i<sessions.length;i++) {
                if(sessions[i] != null) {
                    TestAdminStatusComputer.adjustSessionTimesToLocalTimeZone(sessions[i]);
                }
            }
            
            return tsd;
        } catch (SQLException se) {
            TestAdminDataNotFoundException tae = new TestAdminDataNotFoundException("TestSessionStatusImpl: getTestSessionsForOrgNode: " + se.getMessage());
            tae.setStackTrace(se.getStackTrace());
            throw tae;         
        }
    }
    
    /**
     * Retrieves a filtered, sorted, paged list of org nodes that are children of the specified org node
	 * @param userName - identifies the user
     * @param orgNodeId - identifies the org node
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return NodeData
	 * @throws com.ctb.exception.CTBBusinessException
     * @common:operation
     */
    public NodeData getOrgNodesForParent(String userName, Integer orgNodeId, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
    {
        validator.validateNode(userName, orgNodeId, "testAdmin.getOrgNodesForParent");
        try {
            NodeData ond = new NodeData();
            Integer pageSize = null;
            if(page != null) {
                pageSize = new Integer(page.getPageSize());
            }
            ond.setNodes(orgNode.getOrgNodesByParent(orgNodeId), pageSize);
            if(filter != null) ond.applyFiltering(filter);
            if(sort != null) ond.applySorting(sort);
            if(page != null) ond.applyPaging(page);
            return ond;
        } catch (SQLException se) {
            OrgNodeDataNotFoundException one = new OrgNodeDataNotFoundException("TestSessionStatusImpl: getOrgNodesForParent: " + se.getMessage());
            one.setStackTrace(se.getStackTrace());
            throw one;
        }
    }
    
    /**
     * Retrieves a filtered, sorted, paged list of org nodes that are children of the specified org node,
     * plus a count of all active test sessions scheduled at/below each node in the list.
	 * @param userName - identifies the user
     * @param orgNodeId - identifies the org node
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return SessionNodeData
	 * @throws com.ctb.exception.CTBBusinessException
     * @common:operation
     */
    public SessionNodeData getSessionNodesForParent(String userName, Integer orgNodeId, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
    {
        validator.validateNode(userName, orgNodeId, "testAdmin.getSessionNodesForParent");
        try {
            SessionNodeData ond = new SessionNodeData();
            Integer pageSize = null;
            if(page != null) {
                pageSize = new Integer(page.getPageSize());
            }
            Node [] nodes = orgNode.getOrgNodesByParent(orgNodeId);
            SessionNode [] sessionNodes = new SessionNode [nodes.length];
            for(int i=0;i<nodes.length;i++) {
                sessionNodes[i] = new SessionNode(nodes[i]);                     
            }
            ond.setSessionNodes(sessionNodes, pageSize);
            if(filter != null) ond.applyFiltering(filter);
            if(sort != null) ond.applySorting(sort);
            if(page != null) ond.applyPaging(page);
            sessionNodes = ond.getSessionNodes();
            for(int i=0;i<sessionNodes.length && sessionNodes[i] != null;i++) {
                sessionNodes[i].setSessionCount(orgNode.getSessionCountForAncestorNode(sessionNodes[i].getOrgNodeId()));
                sessionNodes[i].setChildNodeCount(orgNode.getOrgNodeCountByParent(sessionNodes[i].getOrgNodeId()));
            }
            return ond;
        } catch (SQLException se) {
            OrgNodeDataNotFoundException one = new OrgNodeDataNotFoundException("TestSessionStatusImpl: getSessionNodesForParent: " + se.getMessage());
            one.setStackTrace(se.getStackTrace());
            throw one;
        }
    }
    
    /**
     * Retrieves a filtered, sorted, paged list of org nodes at which the specified user has a role defined
	 * @param userName - identifies the user
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return NodeData
	 * @throws com.ctb.exception.CTBBusinessException
     * @common:operation
     */
    public NodeData getTopNodesForUser(String userName, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
    {
        validator.validate(userName, null, "testAdmin.getTopNodesForUser");
        try {
            NodeData ond = new NodeData();
            Integer pageSize = null;
            if(page != null) {
                pageSize = new Integer(page.getPageSize());
            }
            ond.setNodes(orgNode.getTopNodesForUser(userName), pageSize);
            if(filter != null) ond.applyFiltering(filter);
            if(sort != null) ond.applySorting(sort);
            if(page != null) ond.applyPaging(page);
            return ond;
        } catch (SQLException se) {
            OrgNodeDataNotFoundException one = new OrgNodeDataNotFoundException("TestSessionStatusImpl: getTopNodesForUser: " + se.getMessage());
            one.setStackTrace(se.getStackTrace());
            throw one;        
        }
    }
    
    /**
     * Retrieves a filtered, sorted, paged list of org nodes at which the specified user has a role defined,
     * plus a count of all active test sessions scheduled at/below each node in the list.
	 * @param userName - identifies the user
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return SessionNodeData
	 * @throws com.ctb.exception.CTBBusinessException
     * @common:operation
     */
    public SessionNodeData getTopSessionNodesForUser(String userName, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
    {
        validator.validate(userName, null, "testAdmin.getTopSessionNodesForUser");
        try {
            SessionNodeData ond = new SessionNodeData();
            Integer pageSize = null;
            if(page != null) {
                pageSize = new Integer(page.getPageSize());
            }
            Node [] nodes = orgNode.getTopNodesForUser(userName);
            SessionNode [] sessionNodes = new SessionNode [nodes.length];
            for(int i=0;i<nodes.length;i++) {
                sessionNodes[i] = new SessionNode(nodes[i]);                     
            }
            ond.setSessionNodes(sessionNodes, pageSize);
            if(filter != null) ond.applyFiltering(filter);
            if(sort != null) ond.applySorting(sort);
            if(page != null) ond.applyPaging(page);
            sessionNodes = ond.getSessionNodes();
            for(int i=0;i<sessionNodes.length && sessionNodes[i] != null;i++) {
                sessionNodes[i].setSessionCount(orgNode.getSessionCountForAncestorNode(sessionNodes[i].getOrgNodeId()));
                sessionNodes[i].setChildNodeCount(orgNode.getOrgNodeCountByParent(sessionNodes[i].getOrgNodeId()));
            }
            return ond;
        } catch (SQLException se) {
            OrgNodeDataNotFoundException one = new OrgNodeDataNotFoundException("TestSessionStatusImpl: getTopSessionNodesForUser: " + se.getMessage());
            one.setStackTrace(se.getStackTrace());
            throw one;
        }
    }
    
    /**
     * Retrieves a filtered, sorted, paged list of roster elements for the specified test session.
	 * @param userName - identifies the user
     * @param testAdminId - identifies the test session
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return RosterElementData
	 * @throws com.ctb.exception.CTBBusinessException
     * @common:operation
     */
    public RosterElementData getRosterForTestSession(String userName, Integer testAdminId, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
    {
        validator.validateAdmin(userName, testAdminId, "testAdmin.getRosterElementsForTestSession");
        try {
            RosterElementData red = new RosterElementData();
            Integer pageSize = null;
            if(page != null) {
                pageSize = new Integer(page.getPageSize());
            }
            red.setRosterElements(roster.getRosterForTestSession(testAdminId), pageSize);
            if(filter != null) red.applyFiltering(filter);
            if(sort != null) red.applySorting(sort);
            if(page != null) red.applyPaging(page);
            return red;
        } catch (SQLException se) {
            RosterDataNotFoundException rde = new RosterDataNotFoundException("TestSessionStatusImpl: getRosterElementsForTestSession: " + se.getMessage());
            rde.setStackTrace(se.getStackTrace());
            throw rde;  
        }
    }     

    /**
     * Retrieves a roster elements by testRosterId
     * @param testRosterId - identifies the test roster
     * @return RosterElement
     * @throws com.ctb.exception.CTBBusinessException
     * @common:operation
     */
    public RosterElement getRoster(Integer testRosterId) throws CTBBusinessException
    {
        try {
            RosterElement re = roster.getRosterElement(testRosterId);
            return re;
        } catch (SQLException se) {
            RosterDataNotFoundException rde = new RosterDataNotFoundException("TestSessionStatusImpl: getRosterElement: " + se.getMessage());
            rde.setStackTrace(se.getStackTrace());
            throw rde;  
        }
    }     
    
    /**
     * Retrieves a filtered, sorted, paged list of test elements which comprise the specified test session.
     * (presumably 'TS'-type item sets)
	 * @param userName - identifies the user
     * @param testAdminId - identifies the test session
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return TestElementData
	 * @throws com.ctb.exception.CTBBusinessException
     * @common:operation
     */
    public TestElementData getTestElementsForTestSession(String userName, Integer testAdminId, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
    {
        validator.validateAdmin(userName, testAdminId, "testAdmin.getTestElementsForTestSession");
        try {
            TestElementData ted = new TestElementData();
            Integer pageSize = null;
            if(page != null) {
                pageSize = new Integer(page.getPageSize());
            }
            ted.setTestElements(itemSet.getTestElementsForSession(testAdminId), pageSize);
            if(filter != null) ted.applyFiltering(filter);
            if(sort != null) ted.applySorting(sort);
            if(page != null) ted.applyPaging(page);
            return ted;
        } catch (SQLException se) {
            TestElementDataNotFoundException tee = new TestElementDataNotFoundException("TestSessionStatusImpl: getTestElementsForTestSession: " + se.getMessage());
            tee.setStackTrace(se.getStackTrace());
            throw tee;  
        }
    }    
    
    /**
     * Toggles the validation status of the specifed roster element
	 * @param userName - identifies the user
     * @param testRosterId - identifies the test session roster element (student)
	 * @throws com.ctb.exception.CTBBusinessException
     * @common:operation
     */
    public void toggleRosterValidationStatus(String userName, Integer testRosterId) throws CTBBusinessException{
        validator.validateRoster(userName, testRosterId, "testAdmin.toggleRosterValidationStatus");
        try {
            RosterElement rosterDetail = getRoster(testRosterId);
            String newValidationStatus = "VA";
            if (rosterDetail.getValidationStatus().equals("VA"))
                newValidationStatus = "IN";
            else if (rosterDetail.getValidationStatus().equals("IN"))
                newValidationStatus = "VA";   
            else if (rosterDetail.getValidationStatus().equals("PI"))
                //newValidationStatus = "PI";  
                throw new CTBBusinessException("Cannot toggle roster validation status 'Partially Invalid'.");

            rosterDetail.setValidationStatus(newValidationStatus);
            roster.updateTestRoster(rosterDetail);
            
            if (!newValidationStatus.equals("PI")) {
                StudentSessionStatus [] studentSessionStatuses =studentItemSetStatus.getStudentItemSetStatuses(testRosterId);
                for (int i=0; i < studentSessionStatuses.length; i++) {
                    studentSessionStatuses[i].setValidationStatus(newValidationStatus);
                    studentItemSetStatus.updateStudentItemSetStatus(studentSessionStatuses[i]);
                }                
            }
            
            scorer.sendObjectMessage(testRosterId);
        } catch (SQLException se) {
            RosterDataNotFoundException rde = new RosterDataNotFoundException("TestSessionStatusImpl: toggleRosterValidationStatus: " + se.getMessage());
            rde.setStackTrace(se.getStackTrace());
            throw rde;  
        }
    }
    
    /**
     * Toggles the validation status of the specifed subtests
	 * @param userName - identifies the user
     * @param testRosterId - identifies the test session roster element (student)
     * @param itemSetIds - identifies the TD item sets (subtests)
	 * @throws com.ctb.exception.CTBBusinessException
     * @common:operation
     */
    public void toggleSubtestValidationStatus(String userName, Integer testRosterId, Integer [] itemSetIds) throws CTBBusinessException{
        validator.validateRoster(userName, testRosterId, "testAdmin.toggleSubtestValidationStatus");
        try {
            if (itemSetIds == null || itemSetIds.length == 0)
                return;
                
            for (int i=0; i < itemSetIds.length; i++) {
                studentItemSetStatus.toggleSubtestValidationStatus(testRosterId, itemSetIds[i]);
            }
            
            String newRosterStatus =studentItemSetStatus.getRosterValidationStatusFromSubtests(testRosterId);
            RosterElement rosterDetail = getRoster(testRosterId);
            rosterDetail.setValidationStatus(newRosterStatus);
            roster.updateTestRoster(rosterDetail);
            
            scorer.sendObjectMessage(testRosterId);
        } catch (SQLException se) {
            RosterDataNotFoundException rde = new RosterDataNotFoundException("TestSessionStatusImpl: toggleSubtestValidationStatus: " + se.getMessage());
            rde.setStackTrace(se.getStackTrace());
            throw rde;  
        }
    }
    
        
    /**
     * Retrieves a filtered, sorted, paged list of active tests, meaning tests having
     * scheduled sessions whose login window opens within the next 60 days. Each ActiveTest
     * object contains a list of all subtests (TD item sets) belonging to the test.
	 * @param userName - identifies the user
     * @return ActiveTestData
	 * @throws com.ctb.exception.CTBBusinessException
     * @common:operation
     */
    public ActiveTestData getActiveTestsForUser(String userName, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException {
        validator.validate(userName, null, "testAdmin.getActiveTestsForUser");
        try {
            ActiveTestData atd = new ActiveTestData();
            Integer pageSize = null;
            if(page != null) {
                pageSize = new Integer(page.getPageSize());
            }
            ActiveTest [] tests = itemSet.getActiveTestsForUser(userName);
            for(int i=0;i<tests.length;i++) {
                tests[i].setContentSize(itemSet.getContentSizeForTest(tests[i].getItemSetId()));
                tests[i].setSubtests(itemSet.getTestElementsForAncestor(tests[i].getItemSetId(), "TD"));
            }
            atd.setActiveTests(tests, pageSize);
            if(filter != null) atd.applyFiltering(filter);
            if(sort != null) atd.applySorting(sort);
            if(page != null) atd.applyPaging(page);
            return atd;
        } catch (SQLException se) {
            TestElementDataNotFoundException rde = new TestElementDataNotFoundException("TestSessionStatusImpl: getActiveTestsForUser: " + se.getMessage());
            rde.setStackTrace(se.getStackTrace());
            throw rde;  
        }
    }
    
    /**
     * Retrieves a filtered, sorted, paged list of active sessions for a test, meaning
     * sessions of the specified test whose login window opens within the next 60 days, or any
     * sessions thereafter with login windows opening less than 60 days after the close
     * of the login window of any previous session of the specified test.
	 * @param userName - identifies the user
     * @param itemSetId - identifies the test
     * @return ActiveSessionData
	 * @throws com.ctb.exception.CTBBusinessException
     * @common:operation
     */
    public ActiveSessionData getActiveSessionsForTest(String userName, Integer itemSetId, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException {
        validator.validateItemSet(userName, itemSetId, "testAdmin.getActiveSessionsForTest");
        try {
            ActiveSessionData asd = new ActiveSessionData();
            Integer pageSize = null;
            if(page != null) {
                pageSize = new Integer(page.getPageSize());
            }
            ActiveSession [] sessions = testAdmin.getActiveSessionsForTest(itemSetId);
            // remove sessions with >60 day gap before window opens
            long millisForWindow = DateUtils.daysToMillis(60);
            ArrayList actives = new ArrayList();
            boolean active = true;
            long currTime = System.currentTimeMillis();
            long lastEnd = currTime;
            for(int i=0;i<sessions.length && active;i++) {
                 ActiveSession session = sessions[i];
                 boolean visible = "true".equals(testAdmin.checkVisibility(userName, session.getTestAdminId()));
                 if(visible) {
                     active = false;
                     long nextStart = session.getLoginStartDate().getTime();
                     if (nextStart <= currTime && session.getLoginEndDate().getTime() >= currTime && visible) {
                        actives.add(session);
                        active = true;
                     } else {
                        boolean activePred = false;
                        if ((nextStart - currTime < millisForWindow) && visible) {
                            activePred = true;
                            active = true;
                        }
                        
                        for(int j=0; j < actives.size() && !activePred; j++) {
                            if(((nextStart - ((ActiveSession)actives.get(j)).getLoginEndDate().getTime()) < millisForWindow) && visible) {
                                activePred = true;
                                active = true;
                            }
                        }
                        if(activePred) actives.add(session);
                     }
                     lastEnd = session.getLoginEndDate().getTime();
                     if(lastEnd < (currTime + millisForWindow)) {
                        active = true;
                     }
                } else {
                    active = true;
                }
                TestAdminStatusComputer.adjustSessionTimesToLocalTimeZone(session);
            }
            sessions = (ActiveSession []) actives.toArray(new ActiveSession[0]);
            asd.setActiveSessions(sessions, pageSize);
            if(filter != null) asd.applyFiltering(filter);
            if(sort != null) asd.applySorting(sort);
            if(page != null) asd.applyPaging(page);
            return asd;
        } catch (SQLException se) {
            TestAdminDataNotFoundException rde = new TestAdminDataNotFoundException("TestSessionStatusImpl: getActiveSessionsForTest: " + se.getMessage());
            rde.setStackTrace(se.getStackTrace());
            throw rde;  
        }
    }
    
    /**
     * Get user information including full name and system id for the specified user name.
     * If the specified user lies withing the requesting user's visible hierarchy,
     * all fields are returned - if not, only the first, last, and middle names
     * of the specified user are returned. Each user object contains a Customer object,
     * which contains information about the user's customer, including a flag, hideAccommodations,
     * which indicates whether accommodation-related UI elements should be hidden for
     * the specified user.
     * @common:operation
     * @param userName - identifies the calling user
     * @param detailUserName - identifies the user whose information is desired
     * @return User
     * @throws CTBBusinessException
     */
    public User getUserDetails(String userName, String detailUserName) throws CTBBusinessException{
        boolean hasPerms = true;
        try {
            validator.validateUser(userName, detailUserName, "testAdmin.getUserDetails");
        } catch (ValidationException ve) {
            hasPerms = false;
        }
        try {
            User user = users.getUserDetails(detailUserName);
            if(user != null) {
                user.setCustomer(users.getCustomer(detailUserName));
                user.setRole(users.getRole(detailUserName));
                
                if(!hasPerms) {
                    User secureUser = new User();
                    secureUser.setFirstName(user.getFirstName());
                    secureUser.setLastName(user.getLastName());
                    secureUser.setMiddleName(user.getMiddleName());
                    secureUser.setUserId(user.getUserId());
                    secureUser.setUserName(user.getUserName());
                    secureUser.setCustomer(user.getCustomer());
                    return secureUser;
                } else {
                    return user;
                }
            } else {
                return null;
            }
        } catch (SQLException se) {
            CTBBusinessException cbe = new UserDataNotFoundException("ScheduleTestImpl: getUserDetails: " + se.getMessage());
            cbe.setStackTrace(se.getStackTrace());
            throw cbe;
        }  
    }
    
    
   /**
     * Retrieves a filtered, sorted, paged list of roster elements for the specified test session.
     * @param userName - identifies the user
     * @param testAdminId - identifies the test session
     * @param orgNodeId - identifies the org node
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return RosterElementData
     * @throws com.ctb.exception.CTBBusinessException
     * @common:operation
     */

    public RosterElementData getRosterForTestSessionAndOrgNode(java.lang.String userName, java.lang.Integer testAdminId, java.lang.Integer orgNodeId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException
    {
        validator.validateAdmin(userName, testAdminId, "testAdmin.getRosterElementsForTestSession");

        try {

            RosterElementData red = new RosterElementData();

            Integer pageSize = null;

            if(page != null) {

                pageSize = new Integer(page.getPageSize());

            }

            red.setRosterElements(roster.getRosterForTestSessionAndOrgNode(testAdminId, orgNodeId), pageSize);

            if(filter != null) red.applyFiltering(filter);

            if(sort != null) red.applySorting(sort);

            if(page != null) red.applyPaging(page);

            return red;

        } catch (SQLException se) {

            RosterDataNotFoundException rde = new RosterDataNotFoundException("TestSessionStatusImpl: getRosterElementsForTestSession: " + se.getMessage());

            rde.setStackTrace(se.getStackTrace());

            throw rde;  

        }

    }  
    

    

    /**
     * Retrieves a list of child org nodes of the specified org node,
     * plus a count of users who have roles anywhere at/below each 
     * org node in the list (userCount). If testAdminId is not null, a count of users at
     * or below each node who are assigned as proctors to the specified session
     * is also attached to each node (proctorCount).
     * @common:operation
     * @param userName - identifies the user
     * @param testAdminId - identifies the test session
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return UserNodeData
	 * @throws com.ctb.exception.CTBBusinessException
     */
    public UserNodeData getTopUserNodesForUser(String userName, Integer testAdminId, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
    {
        validator.validateAdmin(userName, testAdminId, "testAdmin.getTopUserNodesForUser");
        try {
            UserNodeData ond = new UserNodeData();
            Integer pageSize = null;
            if(page != null) {
                pageSize = new Integer(page.getPageSize());
            }
            Node [] nodes = orgNode.getTopNodesForUser(userName);
            UserNode [] userNodes = new UserNode [nodes.length];
            for(int i=0;i<nodes.length;i++) {
                userNodes[i] = new UserNode(nodes[i]);                     
            }
            ond.setUserNodes(userNodes, pageSize);
            for(int i=0;i<userNodes.length && userNodes[i] != null;i++) {
                userNodes[i].setUserCount(orgNode.getUserCountForAncestorNode(userNodes[i].getOrgNodeId()));            
                if(testAdminId != null) {
                    userNodes[i].setProctorCount(orgNode.getProctorCountForAncestorNodeAndAdmin(userNodes[i].getOrgNodeId(), testAdminId));            
                }
            }
            FilterParams implicitFilter = new FilterParams();
            FilterParam [] newFilters = new FilterParam [1];
            Integer [] args = new Integer[1];
            args[0] = new Integer(0);
            FilterParam countFilter = new FilterParam("UserCount", args, FilterType.GREATERTHAN);
            newFilters[0] = countFilter;
            implicitFilter.setFilterParams(newFilters);
            ond.applyFiltering(implicitFilter);
            ond.setUserNodes(ond.getUserNodes(), pageSize);
            if(filter != null) ond.applyFiltering(filter);
            if(sort != null) ond.applySorting(sort);
            if(page != null) ond.applyPaging(page);
            return ond;
        } catch (SQLException se) {
            OrgNodeDataNotFoundException one = new OrgNodeDataNotFoundException("ScheduleTestImpl: getTopUserNodesForUser: " + se.getMessage());
            one.setStackTrace(se.getStackTrace());
            throw one;
        }
    }
    /**     
     * Retrieves the  student item set statuses for a session.
     * @common:operation
     * @param userName - identifies the user
     * @param testAdminId - identifies the test session
     * @param studentId  -  identifies the student 
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return StudentSessionStatusesData
	 * @throws com.ctb.exception.CTBBusinessException
     */ 
     public StudentSessionStatusData  getStudentItemSetStatusesForRoster(java.lang.String userName,java.lang.Integer studentId,java.lang.Integer testAdminId,FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
     {
        validator.validate(userName,testAdminId,"getStudentItemSetStatusesForRoster");
        
        try{
            StudentSessionStatusData sssd =  new StudentSessionStatusData();
            StudentSessionStatus [] sss = studentItemSetStatus.getStudentItemSetStatusesForRoster(studentId,testAdminId);
            Integer pageSize = null;
            if(page != null) {
                pageSize = new Integer(page.getPageSize());
            }
            sssd.setStudentSessionStatuses(sss,pageSize);
            if(filter != null) sssd.applyFiltering(filter);
            if(sort != null) sssd.applySorting(sort);
            if(page != null) sssd.applyPaging(page);
            return sssd;
        }catch (SQLException se) {
            CTBBusinessException cbe = new StudentSessionStatusNotFoundException("TestSessionStatusImpl: getStudentItemSetStatusesForRoster: " + se.getMessage());
            cbe.setStackTrace(se.getStackTrace());
            throw cbe;
        }        
     }
     
     /**     
     * Retrieves the  student item set statuses for a session.
     * @common:operation
     * @param userName - identifies the user
     * @param parentItemSetId - identifies the parent item set
     * @param itemSetTyp -  identifies the item set type  
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return TestElementData
	 * @throws com.ctb.exception.CTBBusinessException
     */ 
     public TestElementData  getTestElementsForParent(java.lang.String userName,Integer parentItemSetId, String itemSetType, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
     {
        validator.validate(userName,parentItemSetId,"getTestElementsForParent");
        
        try{
            TestElementData ted =  new TestElementData();
            TestElement [] te = itemSet.getTestElementsForParent(parentItemSetId,itemSetType);
            Integer pageSize = null;
            if(page != null) {
                pageSize = new Integer(page.getPageSize());
            }
            ted.setTestElements(te,pageSize);
            if(filter != null) ted.applyFiltering(filter);
            if(sort != null) ted.applySorting(sort);
            if(page != null) ted.applyPaging(page);
            return ted;
        }catch (SQLException se) {
            CTBBusinessException cbe = new TestElementDataNotFoundException("TestSessionStatusImpl: getTestElementsForParent: " + se.getMessage());
            cbe.setStackTrace(se.getStackTrace());
            throw cbe;
        }        
     }
     
    /**
     * Get customer configuration for the specified customer.
     * @common:operation
     * @param userName - identifies the calling user
     * @param customerId - identifies the customer whose information is desired
     * @return CustomerConfiguration []
     * @throws CTBBusinessException
     */
    public CustomerConfiguration [] getCustomerConfigurations(String userName, Integer customerId) throws CTBBusinessException
    {
        validator.validateCustomer(userName, customerId, "TestSessionStatusImpl.getCustomerConfigurations");
        try {
            CustomerConfiguration [] customerConfigurations = customerConfiguration.getCustomerConfigurations(customerId.intValue());
            if (customerConfigurations == null || customerConfigurations.length == 0) {
                customerConfigurations = customerConfiguration.getCustomerConfigurations(CTB_CUSTOMER_ID);
            }
            
            if (customerConfigurations != null && customerConfigurations.length > 0) 
            {
                for (int i = 0; i < customerConfigurations.length; i++) {
                    CustomerConfiguration cutomerConfig = customerConfigurations[i];
                    CustomerConfigurationValue [] customerConfigurationValues 
                        = customerConfiguration.getCustomerConfigurationValues(cutomerConfig.getId().intValue());
                    cutomerConfig.setCustomerConfigurationValues(customerConfigurationValues);              
                }
            }
            return customerConfigurations;
        } catch (SQLException se) {
            CustomerConfigurationDataNotFoundException tee = new CustomerConfigurationDataNotFoundException("TestSessionStatusImpl: getCustomerConfigurations: " + se.getMessage());
            tee.setStackTrace(se.getStackTrace());
            throw tee;
        }
    }
    
        
    /**
     * Retrieves the TestProduct for the given testAdminId
     * 
     *  @common:operation
     * @userName -  Identifies the user
     * @ testAdminId  -  Identifies the test admin
     */
    public TestProduct  getProductForTestAdmin(String userName, Integer testAdminId) throws com.ctb.exception.CTBBusinessException
    {
        validator.validateAdmin(userName, testAdminId, "TestSessionStatusImpl.getProductForTestAdmin");
            try{
                TestProduct tp = product.getProductForTestAdmin(testAdminId);
                return tp;                  
            }catch(SQLException se){
                CTBBusinessException cbe = new RosterDataNotFoundException("TestSessionStatusImpl: getProductForTestAdmin: " + se.getMessage());
                cbe.setStackTrace(se.getStackTrace());
                throw cbe;                
            }        
    }
    
    
    /**
     * Retrieves a list of ancestor org nodes of the specified org node
     * @common:operation
     * @param userName - identifies the user
     * @param orgNodeId - identifies the parent org node
	 * @return OrganizationNode []
	 * @throws com.ctb.exception.CTBBusinessException
     */
    public OrganizationNode [] getAncestorOrganizationNodesForOrgNode(String userName, Integer orgNodeId) throws com.ctb.exception.CTBBusinessException
    {
        validator.validateNode(userName, orgNodeId, "TestSessionStatusImpl.getAncestorOrganizationNodesForOrgNode");
        try {
            Integer [] topOrgNodeIds = customerConfiguration.getTopOrgNodeIdsForUser(userName);
            OrganizationNode [] orgNodes = customerConfiguration.getAncestorOrganizationNodesForOrgNodeAtAndBelowTopOrgNodes(orgNodeId, SQLutils.convertArraytoString(topOrgNodeIds));
            return orgNodes;
        } catch (SQLException se) {
            OrgNodeDataNotFoundException one = new OrgNodeDataNotFoundException("TestSessionStatusImpl: getAncestorOrganizationNodesForOrgNode: " + se.getMessage());
            one.setStackTrace(se.getStackTrace());
            throw one;
        }
    }
        
     

    /**
     * Toggle the customer flag status for the specified test roster.
     * @common:operation
     * @param testRosterId - identifies the test roster
	 * @throws com.ctb.exception.CTBBusinessException
     */
    public void toggleCustomerFlagStatus(String userName, Integer testRosterId) throws com.ctb.exception.CTBBusinessException
    {
         try {
            validator.validateRoster(userName, testRosterId, "testAdmin.toggleCustomerFlagStatus");
            User user = getUserDetails(userName, userName);
            Customer customer = user.getCustomer();
            Integer customerId = customer.getCustomerId();
            CustomerConfigurationValue[] customerConfigurationValue = null;
            CustomerConfiguration cutomerConfiguration = null;
            
            CustomerConfiguration [] ccArray = customerConfiguration.getCustomerConfigurations(customerId.intValue());
            if (ccArray != null && ccArray.length > 0) 
            {
                for (int i = 0; i < ccArray.length; i++) {
                    CustomerConfiguration cutomerConfig = ccArray[i];
                    if(cutomerConfig.getCustomerConfigurationName().equalsIgnoreCase("Roster_Status_Flag")){
                        cutomerConfiguration = ccArray[i];
                        customerConfigurationValue
                            = customerConfiguration.getCustomerConfigurationValues(cutomerConfig.getId().intValue());
                        cutomerConfiguration.setCustomerConfigurationValues(customerConfigurationValue);  
                        break; 
                    }           
                }
            }
            
            RosterElement rosterDetail = getRoster(testRosterId);
            Integer currentPosition = null;
            Integer expectedPosition = null;
            
            if (rosterDetail.getCustomerFlagStatus()!= null && rosterDetail.getCustomerFlagStatus().indexOf(CUSTOMER_CONFIG_PARTIALLY) >= 0)
                throw new CTBBusinessException("Cannot toggle roster customer flag status '"+rosterDetail.getCustomerFlagStatus()+"'.");
            
            if(rosterDetail.getCustomerFlagStatus()!=null) {
                expectedPosition = getExpectedPosition(customerConfigurationValue,rosterDetail.getCustomerFlagStatus());
            }
            else if (cutomerConfiguration != null){
                expectedPosition = getExpectedPosition(customerConfigurationValue,cutomerConfiguration.getDefaultValue());
            }
            if (expectedPosition != null){
                String newConfig = customerConfigurationValue[expectedPosition.intValue()].getCustomerConfigurationValue();
                rosterDetail.setCustomerFlagStatus(newConfig);
                roster.updateTestRoster(rosterDetail) ;
                
                StudentSessionStatus [] studentSessionStatuses =studentItemSetStatus.getStudentItemSetStatuses(testRosterId);
                for (int i=0; i < studentSessionStatuses.length; i++) {
                    studentSessionStatuses[i].setCustomerFlagStatus(newConfig);
                    studentItemSetStatus.updateStudentItemSetStatus(studentSessionStatuses[i]);
                }                
            }
            
         
         } catch (SQLException se) {
            RosterDataNotFoundException one = new RosterDataNotFoundException("TestSessionStatusImpl: toggleCustomerFlagStatus: " + se.getMessage());
            one.setStackTrace(se.getStackTrace());
            throw one;
         }
         
    }

    /**
     * Toggle the customer flag status for the specified subtests.
     * @common:operation
     * @param testRosterId - identifies the test session roster element (student)
     * @param itemSetIds - identifies the TD item sets (subtests)
	 * @throws com.ctb.exception.CTBBusinessException
     */
    public void toggleSubtestCustomerFlagStatus(String userName, Integer testRosterId, Integer [] itemSetIds) throws com.ctb.exception.CTBBusinessException
    {
         try {
            validator.validateRoster(userName, testRosterId, "testAdmin.toggleSubtestCustomerFlagStatus");
            if (itemSetIds == null || itemSetIds.length == 0)
                return;
                
            User user = getUserDetails(userName, userName);
            Customer customer = user.getCustomer();
            Integer customerId = customer.getCustomerId();
            CustomerConfigurationValue[] customerConfigurationValue = null;
            CustomerConfiguration cutomerConfiguration = null;
            
            CustomerConfiguration [] ccArray = customerConfiguration.getCustomerConfigurations(customerId.intValue());
            if (ccArray != null && ccArray.length > 0) 
            {
                for (int i = 0; i < ccArray.length; i++) {
                    CustomerConfiguration cutomerConfig = ccArray[i];
                    if(cutomerConfig.getCustomerConfigurationName().equalsIgnoreCase("Roster_Status_Flag")){
                        cutomerConfiguration = ccArray[i];
                        customerConfigurationValue
                            = customerConfiguration.getCustomerConfigurationValues(cutomerConfig.getId().intValue());
                        cutomerConfiguration.setCustomerConfigurationValues(customerConfigurationValue);  
                        break; 
                    }           
                }
            }
            
            if (cutomerConfiguration == null)
                throw new CTBBusinessException("Roster_Status_Flag is not found in customer configuration.");
                
            if (customerConfigurationValue == null || customerConfigurationValue.length == 0)
                throw new CTBBusinessException("No customer configuration values found for Roster_Status_Flag.");

            HashMap itemSetIdHash = new HashMap();
            for (int i=0; i < itemSetIds.length; i++) {
                itemSetIdHash.put(itemSetIds[i],itemSetIds[i]);
            }
            
            StudentSessionStatus [] studentSessionStatuses =studentItemSetStatus.getStudentItemSetStatuses(testRosterId);
            for (int i=0; i < studentSessionStatuses.length; i++) {
                if (itemSetIdHash.containsKey(studentSessionStatuses[i].getItemSetId())) {
                    Integer expectedPosition = null;
                    if(studentSessionStatuses[i].getCustomerFlagStatus()!=null) {
                        expectedPosition = getExpectedPosition(customerConfigurationValue,studentSessionStatuses[i].getCustomerFlagStatus());
                    }
                    else if (cutomerConfiguration != null){
                        expectedPosition = getExpectedPosition(customerConfigurationValue,cutomerConfiguration.getDefaultValue());
                    }
                    if (expectedPosition != null){
                        String newConfig = customerConfigurationValue[expectedPosition.intValue()].getCustomerConfigurationValue();
                        studentSessionStatuses[i].setCustomerFlagStatus(newConfig);
                        studentItemSetStatus.updateStudentItemSetStatus(studentSessionStatuses[i]);
                    }
                }
                else if (studentSessionStatuses[i].getCustomerFlagStatus() == null || studentSessionStatuses[i].getCustomerFlagStatus().equals("")) {
                    studentSessionStatuses[i].setCustomerFlagStatus(cutomerConfiguration.getDefaultValue());
                    studentItemSetStatus.updateStudentItemSetStatus(studentSessionStatuses[i]);
                }
            } 
            
            String newRosterStatus = studentItemSetStatus.getRosterCustomerFlagStatusFromSubtests(testRosterId);
            if (newRosterStatus == null || newRosterStatus.trim().equals(""))
                newRosterStatus = cutomerConfiguration.getDefaultValue();
            else if (newRosterStatus.equals("PI")) {
                Integer expectedPosition = getExpectedPosition(customerConfigurationValue,cutomerConfiguration.getDefaultValue());
                newRosterStatus = customerConfigurationValue[expectedPosition.intValue()].getCustomerConfigurationValue();
                newRosterStatus = CUSTOMER_CONFIG_PARTIALLY+newRosterStatus;
            }
            
            RosterElement rosterDetail = getRoster(testRosterId);
            rosterDetail.setCustomerFlagStatus(newRosterStatus);
            roster.updateTestRoster(rosterDetail) ;
         
         } catch (SQLException se) {
            RosterDataNotFoundException one = new RosterDataNotFoundException("TestSessionStatusImpl: toggleSubtestCustomerFlagStatus: " + se.getMessage());
            one.setStackTrace(se.getStackTrace());
            throw one;
        }
         
    }

    
    /**
     * To check if a user's customer is configured to allow subtest invalidation
     * @common:operation
     * @param userName - identifies the user
	 * @return Boolean
	 * @throws com.ctb.exception.CTBBusinessException
     */
    public Boolean allowSubtestInvalidation(String userName) throws CTBBusinessException{
        try {
            Boolean hasProgramStatusConfig = Boolean.FALSE;
            Integer customerId = users.getCustomerIdForName(userName);
            if(customerId != null){
                CustomerConfiguration[] customerConfigs = customerConfiguration.getCustomerConfigurations(customerId.intValue());
                if(customerConfigs != null && customerConfigs.length>0 ){
                    for (int i = 0; i<customerConfigs.length; i++){
                        CustomerConfiguration customerConfig = customerConfigs[i];
                        if((CUSTOMER_CONFIG_ALLOW_SUBTEST_INVALIDATION.equals(customerConfig.getCustomerConfigurationName())) 
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
    
    
    
     private Integer getExpectedPosition (CustomerConfigurationValue []customerConfigurationValue, String statusFlag) {
        
        LinkedHashMap rosterMap = new LinkedHashMap ();
        
        Integer expectedPosition = null;
        
        for (int i = 0; i < customerConfigurationValue.length; i++) {
            CustomerConfigurationValue tempC = (CustomerConfigurationValue)customerConfigurationValue[i];
            rosterMap.put(tempC.getCustomerConfigurationValue(),new Integer(i));
        }
        
        if (rosterMap.containsKey(statusFlag)){
            
            Integer currentPosition = (Integer)rosterMap.get(statusFlag);
            if (currentPosition.intValue() == (rosterMap.size() - 1)) {
                expectedPosition = new Integer(0);
            } else {
                expectedPosition = new Integer(currentPosition.intValue() + 1);
            }
        }
        
        return expectedPosition;        
    }
   
    
} 
