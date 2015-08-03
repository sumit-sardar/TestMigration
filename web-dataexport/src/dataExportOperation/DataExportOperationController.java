package dataExportOperation;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import utils.Base;
import utils.DataExportSearchUtils;
import utils.FilterSortPageUtils;
import utils.JsonUtils;
import utils.MessageResourceBundle;
import utils.PermissionsUtils;
import utils.BroadcastUtils;

import com.ctb.bean.dataExportManagement.ManageStudent;
import com.ctb.bean.dataExportManagement.ManageStudentData;
import com.ctb.bean.dataExportManagement.ManageJobData;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.request.FilterParams;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.CustomerConfigurationValue;
import com.ctb.bean.testAdmin.CustomerExportStudentData;
import com.ctb.bean.testAdmin.CustomerLicense;
import com.ctb.bean.testAdmin.CustomerReport;
import com.ctb.bean.testAdmin.CustomerReportData;
import com.ctb.bean.testAdmin.ManageTestSession;
import com.ctb.bean.testAdmin.ManageTestSessionData;
import com.ctb.bean.testAdmin.ProgramData;
import com.ctb.bean.testAdmin.QuestionAnswerData;
import com.ctb.bean.testAdmin.RubricViewData;
import com.ctb.bean.testAdmin.ScheduleElementData;
import com.ctb.bean.testAdmin.ScorableCRAnswerContent;
import com.ctb.bean.testAdmin.ScorableItem;
import com.ctb.bean.testAdmin.ScorableItemData;
import com.ctb.bean.testAdmin.Student;
import com.ctb.bean.testAdmin.StudentData;
import com.ctb.bean.testAdmin.StudentSessionStatusData;
import com.ctb.bean.testAdmin.TestSessionData;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.control.crscoring.TestScoring;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.SuccessInfo;
import com.ctb.util.jmsutils.ExportDataJMSUtil;
import com.ctb.widgets.bean.PagerSummary;
import com.google.gson.Gson;

import dto.DataExportVO;





@Jpf.Controller()
public class DataExportOperationController extends PageFlowController {
	private static final long serialVersionUID = 1L;
	private static final String ACTION_DEFAULT = "defaultAction";
	private static final String ACTION_FIND_STUDENT = "findStudent";
	private static final String ACTION_VIEW_STATUS = "getExportStatus";
	private static final String STUDENT_STATUS = "Incomplete";
	private CustomerLicense[] customerLicenses = null;
	CustomerConfiguration[] customerConfigurations = null;
	CustomerConfigurationValue[] customerConfigurationsValue = null;
	private String userName = null;
	private Integer customerId = null;
    private User user = null;
    private String userTimeZone = null;
    public String pageTitle = null;
	public String pageMessage = null;
	private Integer totalStudentCount = 0;
	private boolean islaslinkCustomer = false;
	private boolean isEngradeCustomer = false;
	
	private List<Integer> toBeExportedRosterListLLEAB = null;
	private Integer totalExportedStudentCountLLEAB = null;
	private List<Integer> toBeExportedRosterListLL2ND = null;
	private Integer totalExportedStudentCountLL2ND = null;
	
	private List<Integer> toBeExportedRosterListForSessionsLLEAB = null;
	private List<Integer> toBeExportedRosterListForSessionsLL2ND = null;
    
	/*[IAA]: Defect#75509 Rename Esp A/B*/
	private static final String FORMATITLE = "Student Data Export - Forms A/B Espa�ol A";
	private static final String FORMBTITLE = "Student Data Export - Enhanced - Forms C/D Espa�ol B";
	private static final String FORMADESCRIPTION = "Export the student data obtained by the student in standard format for Laslink A/B/Espa�ol A.";
	private static final String FORMBDESCRIPTION = "Export the student data obtained by the student in standard format for Laslink Enhanced C/D/Espa�ol B.";
	
    public static String CONTENT_TYPE_JSON = "application/json";

	/**
	 * Callback that is invoked when this controller instance is created.
	 */
	@Override
	protected void onCreate() {
	}

	/**
	 * Callback that is invoked when this controller instance is destroyed.
	 */
	@Override
	protected void onDestroy(HttpSession session) {
	}
	/**
	 * @common:control
	 */
	@Control()
	private com.ctb.control.testAdmin.TestSessionStatus testSessionStatus;
	/**
	 * @common:control
	 */
	@Control()
	private com.ctb.control.dataExportManagement.DataExportManagement dataexportManagement;
	
	@Control()
	private com.ctb.control.crscoring.TestScoring testScoring;
	
	@Control()
    private com.ctb.control.db.CRScoring scoring;
	
	@Control()
    private com.ctb.control.licensing.Licensing licensing;
	
	@org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.BroadcastMessageLog message;
	
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.OrgNode orgnode;
	
	
	
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success", path = "services.do")
	})
	protected Forward begin()
	{
		return new Forward("success");
	}
	
	
	
   
    
    /**
     * STUDENT REGISTRATION actions
     */
    @Jpf.Action()
    protected Forward assessments_studentRegistrationLink()
    {
        try
        {
        	String url = "/RegistrationWeb/registrationOperation/beginStudentRegistration.do";
        	getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    }
    
    


    /**Services 
     * Actions
     */
   
	@Jpf.Action(forwards = { 
			 @Jpf.Forward(name = "exportDataLink", path = "services_dataExport.do"),
			 @Jpf.Forward(name = "viewStatusLink", path="beginViewStatus.do"),
			 @Jpf.Forward(name = "resetTestSessionLink", path = "services_resetTestSession.do"),
			 @Jpf.Forward(name = "manageLicensesLink", path = "services_manageLicenses.do"),
			 @Jpf.Forward(name = "installSoftwareLink", path = "services_installSoftware.do"),
			 @Jpf.Forward(name = "downloadTestLink", path = "services_downloadTest.do"),
			 @Jpf.Forward(name = "uploadDataLink", path = "services_uploadData.do"),
			 @Jpf.Forward(name = "downloadDataLink", path = "services_downloadData.do"),
			 @Jpf.Forward(name = "showAccountFileDownloadLink", path = "eMetric_user_accounts_detail.do")
	 })
	protected Forward services()
	 {
		 String menuId = (String)this.getRequest().getParameter("menuId");    	
		 String forwardName = (menuId != null) ? menuId : "exportDataLink";

		 return new Forward(forwardName);
	 }

	 @Jpf.Action()
		protected Forward eMetric_user_accounts_detail()
		{
	        try
	        {
	            String url = "/SessionWeb/userAccountFileOperation/accountFiles.do";
	            getResponse().sendRedirect(url);
	        } 
	        catch (IOException ioe)
	        {
	            System.err.print(ioe.getStackTrace());
	        }
	        return null;
		} 

	@Jpf.Action()
	protected Forward services_resetTestSession()
	{
		try
		{
			String url = "/OrganizationWeb/resetOperation/services_resetTestSession.do";
			getResponse().sendRedirect(url);
		} 
	    catch (IOException ioe)
	    {
	    	System.err.print(ioe.getStackTrace());
	    }
	    return null;
	}
	
	@Jpf.Action()
	 protected Forward services_manageLicenses()
	 {
	        try
	        {
	            String url = "/OrganizationWeb/licenseOperation/services_manageLicenses.do";
	            getResponse().sendRedirect(url);
	        } 
	        catch (IOException ioe)
	        {
	            System.err.print(ioe.getStackTrace());
	        }
	        return null;
	 }
	 

	 @Jpf.Action()
	 protected Forward services_installSoftware()
	 {
		 try
		 {
			 String url = "/SessionWeb/softwareOperation/services_installSoftware.do";
			 getResponse().sendRedirect(url);
		 } 
		 catch (IOException ioe)
		 {
			 System.err.print(ioe.getStackTrace());
		 }
		 return null;
	 }

	 @Jpf.Action()
	 protected Forward services_downloadTest()
	 {
		 try
		 {
			 String url = "/SessionWeb/testContentOperation/services_downloadTest.do";
			 getResponse().sendRedirect(url);
		 } 
		 catch (IOException ioe)
		 {
			 System.err.print(ioe.getStackTrace());
		 }
		 return null;
	 }

	 @Jpf.Action()
	 protected Forward services_uploadData()
	 {
		 try
		 {
			 String url = "/OrganizationWeb/uploadOperation/services_uploadData.do";
			 getResponse().sendRedirect(url);
		 } 
		 catch (IOException ioe)
		 {
			 System.err.print(ioe.getStackTrace());
		 }
		 return null;
	 }

	 @Jpf.Action()
	 protected Forward services_downloadData()
	 {
		 try
		 {
			 String url = "/OrganizationWeb/downloadOperation/services_downloadData.do";
			 getResponse().sendRedirect(url);
		 } 
		 catch (IOException ioe)
		 {
			 System.err.print(ioe.getStackTrace());
		 }
		 return null;
	 }  
  
	 /**
	     * ASSESSMENTS actions
	     */    
	    @Jpf.Action(forwards = { 
	    		@Jpf.Forward(name = "sessionsLink", path = "assessments_sessionsLink.do"),
	    		@Jpf.Forward(name = "studentScoringLink", path = "assessments_studentScoringLink.do"),
	    		@Jpf.Forward(name = "programStatusLink", path = "assessments_programStatusLink.do"),
	    		@Jpf.Forward(name = "studentRegistrationLink", path = "assessments_studentRegistrationLink.do")
	    })   
	    protected Forward assessments()
	    {

	    	String menuId = (String)this.getRequest().getParameter("menuId");    	
	    	String forwardName = (menuId != null) ? menuId : "sessionsLink";

	    	return new Forward(forwardName);	    
	    }


	    @Jpf.Action()
	    protected Forward assessments_sessionsLink()
	    {
	    	try
	    	{
	    		String url = "/SessionWeb/sessionOperation/assessments_sessions.do";
	    		getResponse().sendRedirect(url);
	    	} 
	    	catch (IOException ioe)
	    	{
	    		System.err.print(ioe.getStackTrace());
	    	}
	    	return null;
	    }

	    @Jpf.Action()
	    protected Forward assessments_studentScoringLink()
	    {
	    	try
	    	{
	    		String url = "/SessionWeb/sessionOperation/assessments_studentScoring.do";
	    		getResponse().sendRedirect(url);
	    	} 
	    	catch (IOException ioe)
	    	{
	    		System.err.print(ioe.getStackTrace());
	    	}
	    	return null;
	    }

	    @Jpf.Action()
	    protected Forward assessments_programStatusLink()
	    {
	    	try
	    	{
	    		String url = "/SessionWeb/programOperation/assessments_programStatus.do";
	    		getResponse().sendRedirect(url);
	    	} 
	    	catch (IOException ioe)
	    	{
	    		System.err.print(ioe.getStackTrace());
	    	}
	    	return null;
	    }
	/**
    * STUDENT SCORING actions
    */    
   @Jpf.Action(forwards = { 
           @Jpf.Forward(name = "studentScoringLink", path = "scoring_studentScoring.do")
       }) 
   protected Forward studentScoring()
   {
   	String menuId = (String)this.getRequest().getParameter("menuId");    	
   	String forwardName = (menuId != null) ? menuId : "studentScoringLink";
   	
       return new Forward(forwardName);
   }
   
   @Jpf.Action()
	protected Forward scoring_studentScoring()
	{
       try
       {
           String url = "/ScoringWeb/studentScoringOperation/beginStudentScoring.do";
           getResponse().sendRedirect(url);
       } 
       catch (IOException ioe)
       {
           System.err.print(ioe.getStackTrace());
       }
       return null;
	}
   
   /**
	 * ORGANIZATIONS actions
	 */    
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "studentsLink", path = "organizations_manageStudents.do"),
	        @Jpf.Forward(name = "usersLink", path = "organizations_manageUsers.do"),
	        @Jpf.Forward(name = "organizationsLink", path = "organizations_manageOrganizations.do"),
	        @Jpf.Forward(name = "bulkAccomLink", path = "organizations_manageBulkAccommodation.do"),
	        @Jpf.Forward(name = "bulkMoveLink", path = "organizations_manageBulkMove.do"),
	        @Jpf.Forward(name = "OOSLink", path = "organizations_manageOutOfSchool.do")
	    }) 
	protected Forward organizations()
	{
		String menuId = (String)this.getRequest().getParameter("menuId");
		String forwardName = (menuId != null) ? menuId : "OOSLink";
		
	    return new Forward(forwardName);
	}
	
   @Jpf.Action()
	protected Forward organizations_manageOrganizations()
	{
       try
       {
           String url = "/OrganizationWeb/orgOperation/organizations_manageOrganizations.do";
           getResponse().sendRedirect(url);
       } 
       catch (IOException ioe)
       {
           System.err.print(ioe.getStackTrace());
       }
       return null;
	}
	
   @Jpf.Action()
	protected Forward organizations_manageStudents()
	{
       try
       {
           String url = "/StudentWeb/studentOperation/organizations_manageStudents.do";
           getResponse().sendRedirect(url);
       } 
       catch (IOException ioe)
       {
           System.err.print(ioe.getStackTrace());
       }
       return null;
	}
	
   @Jpf.Action()
	protected Forward organizations_manageUsers()
	{
       try
       {
           String url = "/UserWeb/userOperation/organizations_manageUsers.do";
           getResponse().sendRedirect(url);
       } 
       catch (IOException ioe)
       {
           System.err.print(ioe.getStackTrace());
       }
       return null;
	}
   
   @Jpf.Action()
	protected Forward organizations_manageBulkAccommodation()
	{
       try
       {
           String url = "/StudentWeb/bulkOperation/organizations_manageBulkAccommodation.do";
           getResponse().sendRedirect(url);
       } 
       catch (IOException ioe)
       {
           System.err.print(ioe.getStackTrace());
       }
       return null;
	}
   
   @Jpf.Action() 
	protected Forward organizations_manageBulkMove()
	{
       try
       {
           String url = "/StudentWeb/bulkMoveOperation/organizations_manageBulkMove.do";
           getResponse().sendRedirect(url);
       } 
       catch (IOException ioe)
       {
           System.err.print(ioe.getStackTrace());
       }
       return null;
	}
   
   @Jpf.Action() 
	protected Forward organizations_manageOutOfSchool()
	{
   	 try
        {
            String url = "/StudentWeb/outOfSchoolOperation/organizations_manageOutOfSchool.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
	}

   /**
    * REPORTS actions
    */    
   @Jpf.Action()
   protected Forward reports()
   {
       try
       {
           String url = "/SessionWeb/sessionOperation/reports.do";
           getResponse().sendRedirect(url);
       } 
       catch (IOException ioe)
       {
           System.err.print(ioe.getStackTrace());
       }
       return null;
   }
   
   /**
    * Data Export
    * 
    */
   
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success", path = "data_export.jsp"),
			@Jpf.Forward(name = "linkPage", path = "dataExportHome.jsp") 
	 }) 
	 protected Forward services_dataExport()
	 {
		 getLoggedInUserPrincipal();

		 getUserDetails();
		 
		 this.customerLicenses = getCustomerLicenses();
		 
		 setupUserPermission();
		 
		 Integer[] frameProducts = getFrameWorkProductIds(this.customerId);
		 
		 List broadcastMessages = BroadcastUtils.getBroadcastMessages(this.message, this.userName);
		 this.getSession().setAttribute("broadcastMessages", new Integer(broadcastMessages.size()));
		 
		 List<CustomerExportStudentData> exportList = new ArrayList<CustomerExportStudentData>();
		 if(frameProducts.length > 1){
			 for(int ii = 0; ii < frameProducts.length; ii++){
				 CustomerExportStudentData export = new CustomerExportStudentData();
				 if(frameProducts[ii] == 7000){
					 String url = "/ExportWeb/dataExportOperation/dataExport.do?frameworkId="+frameProducts[ii];
					 export.setProductId(frameProducts[ii]);
					 export.setExportURL(url);
					 export.setExportName(FORMATITLE);
					 export.setExportDescription(FORMADESCRIPTION);
					 exportList.add(export);
				 }else if (frameProducts[ii] == 7500){
					 String url = "/ExportWeb/dataExportOperation/dataExport.do?frameworkId="+frameProducts[ii];
					 export.setProductId(frameProducts[ii]);
					 export.setExportURL(url);
					 export.setExportName(FORMBTITLE);
					 export.setExportDescription(FORMBDESCRIPTION);
					 exportList.add(export);
				 }
			 }
			 this.getRequest().setAttribute("exportList", exportList);
			 return new Forward("linkPage");
			 
		 }else{
			 checkLasProductIds(frameProducts[0]);
			 this.getRequest().setAttribute("frameworkProductId", frameProducts[0]);
			 return new Forward("success");
		 }
		 
		 
	 }
	
	
	
	private Integer[] getFrameWorkProductIds(Integer customerId) {
		
		Integer[] frameworkProductIds =null;
		try {
			frameworkProductIds = dataexportManagement.getFrameWorkProductIds(customerId);
		} catch (CTBBusinessException e) {
			e.printStackTrace();
		}
		return frameworkProductIds;
	}
	
	private Integer[] getProductIds(Integer customerId, Integer frameworkProductId) {
		
		Integer[] productIds =null;
		try {
			productIds = dataexportManagement.getProductIds(customerId, frameworkProductId);
		} catch (CTBBusinessException e) {
			e.printStackTrace();
		}
		return productIds;
	}
	
	private void checkLasProductIds(Integer frameworkProductId) {
		Integer[] prodIds = getProductIds(this.customerId, frameworkProductId);
		
		boolean isFormA = false;
		boolean isFormB = false;
		boolean isEspA = false;
		boolean isFormC = false;
		boolean isFormD = false;
		boolean isEspB = false;
		
		if(frameworkProductId == 7000) {
			for(Integer x:prodIds) {
				if(x == 7001 ) {
					isFormA = true;
				} else if(x == 7002) {
					isFormB = true;
				}else if(x == 7003) {
					isEspA = true;
				}
			}
		} else if(frameworkProductId == 7500) {
			for(Integer x:prodIds) {
				if(x == 7501) {
					isFormC = true;
				} else if(x == 7502) {
					isEspB = true;
				} else if(x == 7505) {
					isFormD = true;
				}
			}
		}
		
		this.getRequest().setAttribute("frameworkProductId", frameworkProductId);
		this.getRequest().setAttribute("isFormA", new Boolean(isFormA));
		this.getRequest().setAttribute("isFormB", new Boolean(isFormB));
		this.getRequest().setAttribute("isEspA", new Boolean(isEspA));
		this.getRequest().setAttribute("isFormC", new Boolean(isFormC));
		this.getRequest().setAttribute("isFormD", new Boolean(isFormD));
		this.getRequest().setAttribute("isEspB", new Boolean(isEspB));
	}

	@Jpf.Action(forwards = { 
			 @Jpf.Forward(name = "success", path = "data_export.jsp") 
	 }) 
	 protected Forward dataExport()
	 {
		Integer frameworkProductId = Integer.parseInt(this.getRequest().getParameter("frameworkId"));
		checkLasProductIds(frameworkProductId);
		return new Forward("success");
	 }
	@Jpf.Action()
	public Forward getStudentForExport() {

		HttpServletResponse resp = getResponse();
		//resp.setCharacterEncoding("UTF-8"); 
		OutputStream stream = null;
		ManageTestSessionData mtsData = null;
		DataExportVO vo = new DataExportVO();
		Integer frameworkProductId = Integer.parseInt(this.getRequest().getParameter("frameworkProductId").toString());
		Integer productId = Integer.parseInt(this.getRequest().getParameter("productId").toString());
		if (this.userName == null) {
			getLoggedInUserPrincipal();
			getUserDetails();
		}
		try
		{	
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
			Date date = new Date();  
			mtsData = DataExportSearchUtils.getTestSessionsWithUnexportedStudents(this.dataexportManagement, customerId, null, null, null, null, this.userName, frameworkProductId, productId);
			Date date1 = new Date();  
			if ((mtsData != null)) {
				if( (mtsData.getFilteredCount().intValue() > 0)) {
					List<ManageTestSession> testSessionList = DataExportSearchUtils.buildTestSessionsWithStudentToBeExportedList(mtsData);
					if(frameworkProductId != -1 && frameworkProductId == 7000){
						this.toBeExportedRosterListLLEAB = mtsData.getToBeExportedStudentRosterList();
						this.totalExportedStudentCountLLEAB = mtsData.getTotalExportedStudentCount();
					}else if(frameworkProductId != -1 && frameworkProductId == 7500){
						this.toBeExportedRosterListLL2ND = mtsData.getToBeExportedStudentRosterList();
						this.totalExportedStudentCountLL2ND = mtsData.getTotalExportedStudentCount();
					}
					vo.setTestSessionList(testSessionList);
				}
				vo.setNotCompletedStudentCount(mtsData.getNotCompletedStudentCount());
				vo.setNotTakenStudentCount(mtsData.getNotTakenStudentCount());
				vo.setScheduledStudentCount(mtsData.getScheduledStudentCount());
				vo.setStudentBeingExportCount(mtsData.getTotalExportedStudentCount());
			}
			else {
				vo.setTestSessionList(null);
			}
			
			try {
				Gson gson = new Gson();
				String json = gson.toJson(vo);
				System.out.println("Jason value"+ json );
				//resp.setContentType(CONTENT_TYPE_JSON);
				resp.flushBuffer();
				stream = resp.getOutputStream();
				stream.write(json.getBytes("UTF-8"));

			}catch(IOException ioe){
				ioe.printStackTrace();
			}
			finally{
				if (stream!=null){
					stream.close();
				}
			}
		
		}catch(Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			System.err.println("Exception while findSessionStudentToBeExport");
			e.printStackTrace();
		}
	
		return null;
	}
	
	
	
	/**
	 * View Status
	 * 
	 */

	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "view_status.jsp") })
	protected Forward beginViewStatus() {
		initialize();
		return new Forward("success");
	}
	
	@Jpf.Action()
	public Forward getUnscoredStudentDetails() {
		
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		ManageStudentData msData = null;
		ManageStudent student = null;
		List<ManageStudent> studentList=new ArrayList<ManageStudent>();
		DataExportVO vo = new DataExportVO();
		ManageTestSessionData mtsData = null;
		List<Integer> rosterListForSelectedSessions = null;
		Integer totalExportedStudentCount = null;
		String [] selectedTestSessionIdArr = null;
		Integer [] selectedTestSessionIds = null;
		String selectedTestSessionIdStr = (String) getRequest().getParameter("selectedTestSessionIds");
		Integer frameworkId = (getRequest().getParameter("frameworkProductId") == null)?-1:Integer.parseInt(getRequest().getParameter("frameworkProductId").toString());
		Integer productId = (getRequest().getParameter("productId") == null)?-1:Integer.parseInt(getRequest().getParameter("productId").toString());
		if(selectedTestSessionIdStr != null && !selectedTestSessionIdStr.equalsIgnoreCase("")){			
			selectedTestSessionIdArr =  selectedTestSessionIdStr.split(",");
			selectedTestSessionIds  = new Integer[selectedTestSessionIdArr.length];
			for (int i = 0; i < selectedTestSessionIdArr.length; i++) {
				selectedTestSessionIds[i] = Integer.parseInt(selectedTestSessionIdArr[i]);
			}
		}
		try {
			if(selectedTestSessionIds != null && selectedTestSessionIds.length > 0){
				mtsData = DataExportSearchUtils.getTestSessionsWithUnexportedStudents(this.dataexportManagement, customerId, null, null, null, selectedTestSessionIds, this.userName, frameworkId, productId);
				if (mtsData != null) {
					if( (mtsData.getFilteredCount().intValue() > 0)) {
						rosterListForSelectedSessions = mtsData.getToBeExportedStudentRosterList();
						if(frameworkId == 7000){
							this.toBeExportedRosterListForSessionsLLEAB = rosterListForSelectedSessions;
						}else if(frameworkId == 7500){
							this.toBeExportedRosterListForSessionsLL2ND = rosterListForSelectedSessions;
						}
						totalExportedStudentCount = mtsData.getTotalExportedStudentCount();
					}
					vo.setNotCompletedStudentCount(mtsData.getNotCompletedStudentCount());
					vo.setNotTakenStudentCount(mtsData.getNotTakenStudentCount());
					vo.setScheduledStudentCount(mtsData.getScheduledStudentCount());
					vo.setStudentBeingExportCount(mtsData.getTotalExportedStudentCount());
				}
			}else{
				if(frameworkId == 7000){
					rosterListForSelectedSessions = this.toBeExportedRosterListLLEAB;
					totalExportedStudentCount = this.totalExportedStudentCountLLEAB;
				}else if (frameworkId == 7500){
					rosterListForSelectedSessions = this.toBeExportedRosterListLL2ND;
					totalExportedStudentCount = this.totalExportedStudentCountLL2ND;
				}
			}
			System.out.println(" Total student count to exported ::"+ totalExportedStudentCount);
			msData = DataExportSearchUtils.getAllUnscoredUnexportedStudentsDetail(rosterListForSelectedSessions,this.dataexportManagement, customerId, null, null, null);
			if (msData != null && (msData.getFilteredCount() !=null && msData.getFilteredCount().intValue() > 0)) {
				studentList = DataExportSearchUtils.buildExportStudentList(msData);
				for (int index = 0; index <studentList.size(); index++ ){
					student = (ManageStudent)studentList.get(index);
					student.setScoringStatus(STUDENT_STATUS);
				}
				vo.setUnscoredStudentCount(msData.getTotalCount());
			}
			vo.setStudentBeingExportCount(totalExportedStudentCount);
			vo.setStudentList(studentList);
			
		
			try {
				Gson gson = new Gson();
				String json = gson.toJson(vo);
				//System.out.println("Json value"+ json );
				resp.setContentType(CONTENT_TYPE_JSON);
				resp.flushBuffer();
				stream = resp.getOutputStream();
				stream.write(json.getBytes());

			}catch(IOException ioe){
				ioe.printStackTrace();
			}
			finally{
				if (stream!=null){
					stream.close();
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Jpf.Action()
	protected Forward beginDisplayStudItemList()
	{ 

		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String json = "";
		Integer rosterId = Integer.parseInt(getRequest().getParameter("rosterId"));
		Integer itemSetIdTC = Integer.parseInt(getRequest().getParameter("itemSetIdTC"));
		HashMap<Integer,ScorableItem> incompleteItemSetIdMap = new HashMap<Integer, ScorableItem>();
		HashMap<Integer,ScorableItem> totalItemSetIdMap = new HashMap<Integer, ScorableItem>();
		DataExportVO vo = new DataExportVO();
		try {

			//can be used for immediate report button
			ScorableItemData sid = getTestItemForStudent(rosterId, itemSetIdTC, null, null);
			List<ScorableItem> itemList = buildItemList(sid);
			String status = "T";
			for (ScorableItem si : itemList){

				if(si.getScoreStatus().equalsIgnoreCase("incomplete") && si.getAnswered().equalsIgnoreCase("answered")){
					incompleteItemSetIdMap.put(si.getItemSetId(),si);
				}
				totalItemSetIdMap.put(si.getItemSetId(),si);
			}

			for (Integer id : totalItemSetIdMap.keySet()){
				if(incompleteItemSetIdMap.get(id) == null){
					System.out.println("status true");
					status = "T";
					break;
				}else{
					status ="F";
					System.out.println("status false");
				}

			}


			
			try{
				
				String completionStatus = scoring.getScoringStatus(rosterId,itemSetIdTC);
				Boolean scoringButton = false;
				if(completionStatus.equals("CO")){
					 scoringButton = true;
				}else{
					 scoringButton = false;
				}
				
				vo.setScorableItems(itemList);
				
				vo.setPage("1");
				vo.setRecords("10");
				vo.setTotal("2");
				vo.setProcessScoreBtn(scoringButton.toString());
				
				Gson gson = new Gson();
				json = gson.toJson(vo);

				resp.setContentType("application/json");
				stream = resp.getOutputStream();
				resp.flushBuffer();
				stream.write(json.getBytes("UTF-8"));

			}

			finally{
				if (stream!=null){
					stream.close();
				}
			}
		} catch (Exception e) {
			System.err.println("Exception while processing populateGridDropDowns.");
			e.printStackTrace();
		}

		return null;
	}
	
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="")
	})
	protected Forward showQuestionAnswer(){
			
		String jsonResponse = "";
		String itemId = getRequest().getParameter("itemId");
		Integer testRosterId = new Integer(getRequest().getParameter("testRosterId"));
		Integer itemSetId = new Integer(getRequest().getParameter("itemSetId"));
		String itemType = getRequest().getParameter("itemType");

		System.out.println("item id" + itemId);
		System.out.println("testRosterId id" + testRosterId);
		System.out.println("itemSetId id" + itemSetId);
		System.out.println("itemType id" + itemType);

	//	itemId = "0155662";//Had to make it static, since only 2 items are present in database now
		RubricViewData[] scr =  getRubricDetails(itemId);
		ScorableCRAnswerContent scrArea = getIndividualCRResponse(testScoring,
				userName, testRosterId, itemSetId, itemId, itemType);
		QuestionAnswerData qad = new QuestionAnswerData();
		qad.setRubricData(scr);
		qad.setScrContent(scrArea);
		
		try {
			jsonResponse = JsonUtils.getJson(qad, "questionAnswer",qad.getClass());

			//System.out.println("jsonresponse" + jsonResponse);
		   HttpServletResponse resp = this.getResponse();     
		   resp.setContentType("application/json");
           resp.flushBuffer();
	        OutputStream stream = resp.getOutputStream();
	        stream.write(jsonResponse.getBytes());
	        stream.close();
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return null;
		
	}
	
	
	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "") })
	protected Forward saveDetails() {
		System.out.println("Save details");
		String jsonMessageResponse = "";
		if (user == null) {
			getUserDetails();
		}
		String itemId = getRequest().getParameter("itemId");
		Integer itemSetId = Integer.valueOf(getRequest().getParameter(
		"itemSetId"));
		Integer testRosterId = Integer.valueOf(getRequest().getParameter(
		"rosterId"));
		Integer score = Integer.valueOf(getRequest().getParameter("score"));
		Integer itemSetIdTC = Integer.valueOf(getRequest().getParameter("itemSetIdTC"));
		try {
			String completeTD = null;
			Boolean isSuccess = this.testScoring.saveOrUpdateScore(user
					.getUserId(), itemId, itemSetId, testRosterId, score);
			// start- added for  Process Scores   button changes
			String completionStatus = scoring.getScoringStatus(testRosterId,itemSetIdTC);
			if (completionStatus.equals("CO")) {
				this.testSessionStatus.rescoreStudent(testRosterId);
			} else { // Change for immediate reporting requirements
				String completionStatusRosterAndTD = scoring.getStatusForRosterAndTD(testRosterId,itemSetId);
				if (completionStatusRosterAndTD.equals("CO")) {
					this.testSessionStatus.rescoreStudent(testRosterId);
					completeTD = "CO";
				} else {
					completeTD = "IN";
				}
			}


			ManageStudent ms = new ManageStudent();
			ms.setIsSuccess(isSuccess);
			ms.setCompletionStatus(completionStatus);
			ms.setCompletionStatusTD(completeTD);
			jsonMessageResponse = JsonUtils.getJson(ms, "SaveStatus",ms.getClass());
			// end - added for  Process Scores   button changes

			HttpServletResponse resp = this.getResponse();
			resp.setContentType("application/json");
			resp.flushBuffer();
			OutputStream stream = resp.getOutputStream();
			stream.write(jsonMessageResponse.getBytes());
			stream.close();

		} catch (CTBBusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "")
	})
public Forward rescoreStudent() {
		String jsonMessageResponse = "";
		if (user == null) {
			getUserDetails();
		}
		Integer testRosterId = Integer.valueOf(getRequest().getParameter(
		"rosterId"));

		System.out.println("rescore Student" + testRosterId);
        try {    
            this.testSessionStatus.rescoreStudent(testRosterId);
        	
        	ManageStudent ms = new ManageStudent();
			ms.setIsSuccess(true);
			jsonMessageResponse = JsonUtils.getJson(ms, "SaveStatus",ms.getClass());
            HttpServletResponse resp = this.getResponse();
			resp.setContentType("application/json");
			resp.flushBuffer();
			OutputStream stream = resp.getOutputStream();
			stream.write(jsonMessageResponse.getBytes());
			stream.close();
        }
        catch (Exception e) {
            e.printStackTrace();

			try {
	        	ManageStudent ms = new ManageStudent();
				ms.setIsSuccess(false);
				jsonMessageResponse = JsonUtils.getJson(ms, "SaveStatus",ms.getClass());
	            HttpServletResponse resp = this.getResponse();
				resp.setContentType("application/json");
				resp.flushBuffer();
				OutputStream stream = resp.getOutputStream();
				stream.write(jsonMessageResponse.getBytes());
				stream.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
        }                
        return null;
}
	
	private RubricViewData[] getRubricDetails(String itemId){

    	RubricViewData[] rubricDetailsData = null;
    	try {	
    		rubricDetailsData =  this.testScoring.getRubricDetailsData(itemId);
    		System.out.println("rubricdetails data");
    	}
    	catch(CTBBusinessException be){
    		be.printStackTrace();
    	}
    	return rubricDetailsData;
    }
	
	private static ScorableCRAnswerContent getIndividualCRResponse(
			TestScoring testScoring, String userName, Integer testRosterId,
			Integer deliverableItemSetId, String itemId, String itemType) {
		ScorableCRAnswerContent answerArea = new ScorableCRAnswerContent();
		try {
			// Hard Coding to match getCRItemResponseForScoring() method signature.
			answerArea = testScoring.getCRItemResponseForScoring(userName,
					testRosterId, deliverableItemSetId, itemId, itemType , "1");
		} catch (CTBBusinessException be) {
			be.printStackTrace();
		}

		return answerArea;
	}
	
	private ScorableItemData getTestItemForStudent(Integer testRostorId,Integer itemSetId, PageParams page, SortParams sort) 
    {
	 ScorableItemData sid = new ScorableItemData();                        
        try
        {      
            sid = this.testScoring.getAllScorableCRItemsForTestRoster(testRostorId, itemSetId, page, sort);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
        return sid;
    }
	
	private List<ScorableItem> buildItemList(ScorableItemData tid) 
    {
        ArrayList<ScorableItem> itemList = new ArrayList<ScorableItem>();
        if (tid != null) {
        	 ScorableItem[] testItemDetails = tid.getScorableItems();    
            for (int i=0 ; i<testItemDetails.length ; i++) {
            	ScorableItem itemDetail = (ScorableItem)testItemDetails[i];
                if (itemDetail != null) {
                	if("A".equals(itemDetail.getAnswered())){
                		itemDetail.setAnswered("Answered");
                		if("Incomplete".equalsIgnoreCase(itemDetail.getScoreStatus())) {
                			itemDetail.setScorePoint("-");
                		}
                	} else {
                		itemDetail.setAnswered("Not Answered");
                	}
                    itemList.add(itemDetail);
                }
            }
        }
        return itemList;
    }
	
	@Jpf.Action()
	protected Forward submitJob() {
	   
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String isDataExportBySession = getRequest().getParameter("isDataExportBySession");
		Integer frameworkId = (getRequest().getParameter("frameworkId") == null)?-1:Integer.parseInt(getRequest().getParameter("frameworkId").toString());
		DataExportVO vo = new DataExportVO();
		Integer userId = user.getUserId();		
		Integer studentCount = null;
		List<Integer> finalExportedRosterList = null;
		
		if(frameworkId != -1){
			if(isDataExportBySession != null && !isDataExportBySession.equalsIgnoreCase("") && isDataExportBySession.equalsIgnoreCase("true")){
				if(frameworkId == 7000){
					studentCount = this.toBeExportedRosterListForSessionsLLEAB.size();
					finalExportedRosterList = this.toBeExportedRosterListForSessionsLLEAB;
				} else if (frameworkId == 7500){
					studentCount = this.toBeExportedRosterListForSessionsLL2ND.size();
					finalExportedRosterList = this.toBeExportedRosterListForSessionsLL2ND;
				}
			} else {
				if(frameworkId == 7000){
					studentCount = this.toBeExportedRosterListLLEAB.size();
					finalExportedRosterList = this.toBeExportedRosterListLLEAB;
				} else if (frameworkId == 7500){
					studentCount = this.toBeExportedRosterListLL2ND.size();
					finalExportedRosterList = this.toBeExportedRosterListLL2ND;
				}
			}
		}
		Integer jobId = DataExportSearchUtils.getSubmitJobIdAndStartExport(this.dataexportManagement,userId,studentCount);
		ExportDataJMSUtil exportDataJMSUtil = null;
		try {
			 exportDataJMSUtil = new ExportDataJMSUtil ();
		     exportDataJMSUtil.initGenerateReportTask (userName, customerId, userId, jobId, finalExportedRosterList);
		} catch (CTBBusinessException e) {
			e.printStackTrace();
		}
	
		vo.setJobId(jobId);

		try {
			Gson gson = new Gson();
			String json = gson.toJson(vo);
			//System.out.println("Json value"+ json );
			resp.setContentType(CONTENT_TYPE_JSON);
			resp.flushBuffer();
			stream = resp.getOutputStream();
			stream.write(json.getBytes());

		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		finally{
			if (stream!=null){
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	   
	}
	 private void initialize()
		{     
			getLoggedInUserPrincipal();
			getUserDetails();
			setupUserPermission();
			this.getRequest().setAttribute("viewOnly", Boolean.FALSE); 
			String roleName = this.user.getRole().getRoleName();
			
			List broadcastMessages = BroadcastUtils.getBroadcastMessages(this.message, this.userName);
	        this.getSession().setAttribute("broadcastMessages", new Integer(broadcastMessages.size()));
			
		}
	 
	 
	 @Jpf.Action() 
				
		public Forward getExportStatus() throws IOException {
	    	String json ="";
	    	HttpServletResponse resp = getResponse();
			OutputStream stream = null;
			retrieveInfoFromSession();
			//customerHasBulkAccommodation(); //added for defect #66784
			//customerHasResetTestSessions();
			//customerHasScoring();
			isTopLevelLaslinkUser();
			ManageJobData msData = null;
			
			msData = getDataExportJobStatus();
			
			if ((msData != null) && (msData.getFilteredCount().intValue() == 0)) {
				this.getRequest().setAttribute("searchResultEmpty",	MessageResourceBundle.getMessage("jobSearchResultEmpty"));
			}else{
				this.getRequest().removeAttribute("searchResultEmpty");
				
			}
			List jobList = null;
			
			if ((msData != null) && (msData.getFilteredCount().intValue() > 0)) {
				//1
				jobList = DataExportSearchUtils.buildExportJobList(msData);
				
				this.pageMessage = MessageResourceBundle.getMessage("viewStatusPageMessage");
				this.setTotalStudentCount(msData.getTotalCount());
			}
			/*
			 * this.pageTitle = "Export Data: View Status";
			this.savedForm = form.createClone();
			form.setCurrentAction(ACTION_DEFAULT);
			*/
			Base base = new Base();
			base.setPage("1");
			base.setRecords("10");
			base.setTotal("2");
			Gson gson = new Gson();
			base.setManageJobData(jobList);
			json = gson.toJson(base);
//			System.out.println("json -> " + json);
			//return new Forward("success",json);
			
			try{
				resp.setContentType("application/json");
				stream = resp.getOutputStream();
				resp.flushBuffer();
				stream.write(json.getBytes("UTF-8"));

			}

			finally{
				if (stream!=null){
					stream.close();
				}
			}

			
		return null;
			
			
		}
	 
	 
	 
	 private boolean retrieveInfoFromSession() {
			boolean success = true;
			this.userName = (String) getSession().getAttribute("userName");
			if (this.userName == null)
				success = false;
			return success;
		}

	
	private ManageJobData getDataExportJobStatus() {
			Integer userId = user.getUserId();

			ManageJobData msData = null;		
			
			msData = DataExportSearchUtils.getDataExportJobStatus(
					this.dataexportManagement, userId, null,
					null, null);
			
			return msData;
		}
	
	
	
	
	 /**
		 * @param totalStudentCount the totalStudentCount to set
		 */
		public void setTotalStudentCount(Integer totalStudentCount) {
			this.totalStudentCount = totalStudentCount;
		}
	
	
		private Boolean customerHasBulkAccommodation() 
		{
			boolean hasBulkStudentConfigurable = false;
				 //Bulk Accommodation
			 for (int i=0; i < this.customerConfigurations.length; i++) {
				 
		           CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
		            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Bulk_Accommodation") && 
		    	        		cc.getDefaultValue().equals("T")) {
		                	hasBulkStudentConfigurable = true; 
		                	break;
		             }
		      }
				
		    getSession().setAttribute("isBulkAccommodationConfigured", hasBulkStudentConfigurable);
		            
		 	
			return new Boolean(hasBulkStudentConfigurable);           
		}
		
		private Boolean customerHasScoring() {
			getCustomerConfigurations();
			boolean hasScoringConfigurable = false;
			boolean isLaslinkCustomer = false;
			for (CustomerConfiguration cc : customerConfigurations) {
				if (cc.getCustomerConfigurationName().equalsIgnoreCase(
						"Configurable_Hand_Scoring")
						&& cc.getDefaultValue().equals("T")) {
					hasScoringConfigurable = true;
					//break;
				}
				if (cc.getCustomerConfigurationName().equalsIgnoreCase(
						"Laslink_Customer")
						&& cc.getDefaultValue().equals("T")) {
					isLaslinkCustomer = true;
					//break;
				}
			}

			getSession().setAttribute("isScoringConfigured", hasScoringConfigurable);
			this.setIslaslinkCustomer(isLaslinkCustomer);
			getSession().setAttribute("isLaslinkCustomer", isLaslinkCustomer);
			return new Boolean(hasScoringConfigurable);
		}
		
		
		private void isTopLevelLaslinkUser(){
			
			boolean isUserTopLevel = false;
			boolean isLaslinkUserTopLevel = false;
			boolean isLaslinkUser = false;
			isLaslinkUser = this.islaslinkCustomer;
			try {
				if(isLaslinkUser) {
					isUserTopLevel = orgnode.checkTopOrgNodeUser(this.userName);	
					if(isUserTopLevel){
						isLaslinkUserTopLevel = true;				
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			getSession().setAttribute("isTopLevelUser",isLaslinkUserTopLevel);	
			
		}
		
		
		
		/**
		 * Changes For cr hand scoring score by student getCustomerConfigurations
		 */
		private void getCustomerConfigurations() {
			try {
				User user = this.testSessionStatus.getUserDetails(this.userName,
						this.userName);
				Customer customer = user.getCustomer();
				Integer customerId = customer.getCustomerId();
				this.customerConfigurations = this.testSessionStatus
						.getCustomerConfigurations(this.userName, customerId);
			} catch (CTBBusinessException be) {
				be.printStackTrace();
			}
		}
		
		/**
		 * @param islaslinkCustomer the islaslinkCustomer to set
		 */
		public void setIslaslinkCustomer(boolean islaslinkCustomer) {
			this.islaslinkCustomer = islaslinkCustomer;
		}
	
	
	
	
	
	
	
	
	
/////////////////////////////////////////////////////////////////////////////////////////////    
    ///////////////////////////// SETUP USER PERMISSION ///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////    
    private void getLoggedInUserPrincipal()
    {
        java.security.Principal principal = getRequest().getUserPrincipal();
        if (principal != null) {
            this.userName = principal.toString();
        }        
        getSession().setAttribute("userName", this.userName);
    }
    
    private void getUserDetails()
    {
        Boolean supportAccommodations = Boolean.TRUE;
        String userTimeZone = "GMT";
             	
        try
        {
            this.user = this.testSessionStatus.getUserDetails(this.userName, this.userName);
            Customer customer = this.user.getCustomer();
            this.customerId = customer.getCustomerId();
            getSession().setAttribute("customerId", customerId); 
            userTimeZone = this.user.getTimeZone();
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
        this.userTimeZone = userTimeZone;
    }
    
	private void setupUserPermission()
	{
        CustomerConfiguration [] customerConfigs = getCustomerConfigurations(this.customerId);
        boolean adminUser = isAdminUser();
        boolean adminCoordinatorUser = isAdminCoordinatotUser();
        boolean TABECustomer = isTABECustomer(customerConfigs);
        boolean laslinkCustomer = isLaslinkCustomer(customerConfigs);
    	boolean hasResetTestSession = false;
    	boolean hasResetTestSessionForAdmin = false;
    	boolean isOKCustomer = false;
    	boolean isGACustomer = false;
    	boolean hasLicenseConfigured = false;
    	boolean isTopLevelAdmin = new Boolean(isTopLevelUser() && isAdminUser());
    	boolean hasDataExportVisibilityConfig = false;
    	Integer dataExportVisibilityLevel = 1;
    	boolean hasUploadConfig = false;
    	boolean hasDownloadConfig = false;
    	boolean hasUploadDownloadConfig = false;
    	boolean hasSSOHideUserProfile = false;
    	boolean hasSSOBlockUserModifications = false;
    	
    	if( customerConfigs != null ) {
    	for (int i=0; i < customerConfigs.length; i++) {
			CustomerConfiguration cc = (CustomerConfiguration)customerConfigs[i];
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Reopen_Subtest") && 
            		cc.getDefaultValue().equals("T")	) {
				hasResetTestSession = true;
            }
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Reopen_Subtest_For_Admin") && 
            		cc.getDefaultValue().equals("T")	) {
				hasResetTestSessionForAdmin = true;
				continue;
            }
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("OK_Customer")
					&& cc.getDefaultValue().equals("T")) {
            	isOKCustomer = true;
            }
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("GA_Customer") 
					&& cc.getDefaultValue().equalsIgnoreCase("T")) {
				isGACustomer = true;
				continue;
			}
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Subscription") && 
	        		cc.getDefaultValue().equals("T")	) {
				hasLicenseConfigured = true;
	        }
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Data_Export_Visibility")) {
				hasDataExportVisibilityConfig = true;
				dataExportVisibilityLevel = Integer.parseInt(cc.getDefaultValue());
				continue;
            }
            
			// For Upload Download
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Upload")
					&& cc.getDefaultValue().equals("T")) {
				hasUploadConfig = true;
				continue;
            }
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Download")
					&& cc.getDefaultValue().equals("T")) {
				hasDownloadConfig = true;
				continue;
            }
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Upload_Download")
					&& cc.getDefaultValue().equals("T")) {
				hasUploadDownloadConfig = true;
				continue;
            }
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("SSO_Hide_User_Profile") && 
            		cc.getDefaultValue().equals("T")) {
				hasSSOHideUserProfile = Boolean.TRUE;
            }
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("SSO_Block_User_Modifications") && 
            		cc.getDefaultValue().equals("T")) {
				hasSSOBlockUserModifications = Boolean.TRUE;
            }
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("ENGRADE_Customer") && 
            		cc.getDefaultValue().equals("T")) {
        		this.isEngradeCustomer = true;
        		continue;
            }
		}
    	getConfigStudentLabel(customerConfigs);
    	}
    	
		if (hasUploadConfig && hasDownloadConfig) {
			hasUploadDownloadConfig = true;
		}
		if (hasUploadDownloadConfig) {
			hasUploadConfig = false;
			hasDownloadConfig = false;
		}
    	
		this.getSession().setAttribute("hasUploadConfigured",new Boolean(hasUploadConfig && adminUser));
		this.getSession().setAttribute("hasDownloadConfigured",new Boolean(hasDownloadConfig && adminUser));
		this.getSession().setAttribute("hasUploadDownloadConfigured",new Boolean(hasUploadDownloadConfig && adminUser));
		
        this.getSession().setAttribute("showReportTab", 
        		new Boolean(userHasReports().booleanValue() || laslinkCustomer));

        this.getSession().setAttribute("isBulkAccommodationConfigured",customerHasBulkAccommodation(customerConfigs));    	
    	
        
        this.getSession().setAttribute("hasProgramStatusConfigured", 
        		new Boolean( hasProgramStatusConfig().booleanValue() && adminUser));
        
        this.getSession().setAttribute("hasScoringConfigured", 
        		new Boolean( customerHasScoring(customerConfigs).booleanValue()));
        
        this.getSession().setAttribute("canRegisterStudent", canRegisterStudent(customerConfigs));
        
     	this.getSession().setAttribute("hasLicenseConfigured", new Boolean(hasLicenseConfigured && adminUser));

		this.getSession().setAttribute("isBulkMoveConfigured",customerHasBulkMove(customerConfigs));
		
     	this.getSession().setAttribute("adminUser", new Boolean(adminUser));
     	
    	this.getSession().setAttribute("isOOSConfigured",customerHasOOS(customerConfigs));	// Changes for Out Of School
     	
     	this.getSession().setAttribute("hasRapidRagistrationConfigured", new Boolean(TABECustomer && (adminUser || adminCoordinatorUser) ));
     	
    
     	this.getSession().setAttribute("showDataExportTab",laslinkCustomer);
     
		this.getSession().setAttribute("hasResetTestSession", new Boolean((hasResetTestSession && hasResetTestSessionForAdmin) && ((isOKCustomer && isTopLevelAdmin)||(laslinkCustomer && (adminUser||adminCoordinatorUser))||(isGACustomer && adminUser))));
		
		//this.getSession().setAttribute("hasDataExportConfigured", new Boolean(laslinkCustomer)); // add for Data Export
		this.getSession().setAttribute("hasDataExportConfigured", new Boolean((laslinkCustomer && isTopLevelUser()) || (hasDataExportVisibilityConfig && checkUserLevel(dataExportVisibilityLevel))));
		//show Account file download link      	
     	this.getSession().setAttribute("isAccountFileDownloadVisible", new Boolean(laslinkCustomer && isTopLevelAdmin));
     	//Done for Engrade customer to block admin users from adding/editing/deleting users
     	this.getSession().setAttribute("hasSSOHideUserProfile", new Boolean(hasSSOHideUserProfile));
     	this.getSession().setAttribute("hasSSOBlockUserModifications", new Boolean(hasSSOBlockUserModifications));
     	this.getSession().setAttribute("isEngradeCustomer", new Boolean(this.isEngradeCustomer)); 
		
	}
	
	private boolean checkUserLevel(Integer defaultVisibilityLevel){
			boolean isUserLevelMatched = false;
			try {
				isUserLevelMatched = orgnode.matchUserLevelWithDefault(this.userName, defaultVisibilityLevel);	
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return isUserLevelMatched;
		}
	
	private boolean isTopLevelUser(){
		boolean isUserTopLevel = false;
		try {
			isUserTopLevel = orgnode.checkTopOrgNodeUser(this.userName);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isUserTopLevel;
	}
	
    private Boolean userHasReports() 
    {
        boolean hasReports = false;
        try
        {      
            Customer customer = this.user.getCustomer();
            Integer customerId = customer.getCustomerId();   
            hasReports = this.testSessionStatus.userHasReports(this.userName, customerId);
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }        
        return new Boolean(hasReports);           
    }

    private boolean isAdminUser() 
    {               
        String roleName = this.user.getRole().getRoleName();        
        return roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR); 
    }
    
    private boolean isAdminCoordinatotUser() //For Student Registration
	{               
		String roleName = this.user.getRole().getRoleName();        
		return roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR); 
	}
    
    private Boolean canRegisterStudent(CustomerConfiguration [] customerConfigs) 
    {               
        String roleName = this.user.getRole().getRoleName();        
        boolean validCustomer = false; 

        for (int i=0; i < customerConfigs.length; i++)
        {
            CustomerConfiguration cc = (CustomerConfiguration)customerConfigs[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("TABE_Customer"))
            {
                validCustomer = true; 
            }               
        }
        
        boolean validUser = (roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR) || 
        		roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR));
        
        return new Boolean(validCustomer && validUser);
    }
	
    /**
     * getCustomerLicenses
     */
    private CustomerLicense[] getCustomerLicenses()
    {
        CustomerLicense[] cls = null;

        try
        {
            cls = this.licensing.getCustomerOrgNodeLicenseData(this.userName, null);
        }    
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
     
        return cls;
    }
    
    private Boolean hasLicenseConfiguration()
    {               
    	this.customerLicenses =  getCustomerLicenses();
		return new Boolean(this.customerLicenses.length > 0);
    }
    
    private Boolean customerHasScoring(CustomerConfiguration [] customerConfigs)
    {               
        Integer customerId = this.user.getCustomer().getCustomerId();
        boolean hasScoringConfigurable = false;
        
        for (int i=0; i < customerConfigs.length; i++)
        {
        	 CustomerConfiguration cc = (CustomerConfiguration)customerConfigs[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Hand_Scoring") && 
            		cc.getDefaultValue().equals("T")	) {
            	hasScoringConfigurable = true;
            } 
        }
        return new Boolean(hasScoringConfigurable);
    }

    private boolean isLaslinkCustomer(CustomerConfiguration [] customerConfigs)
    {               
        boolean laslinkCustomer = false;
        
        for (int i=0; i < customerConfigs.length; i++)
        {
        	 CustomerConfiguration cc = (CustomerConfiguration)customerConfigs[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Laslink_Customer")
					&& cc.getDefaultValue().equals("T")) {
            	laslinkCustomer = true;
            }
        }
        return laslinkCustomer;
    }
    
    private boolean isTABECustomer(CustomerConfiguration [] customerConfigs)
    {               
        boolean TABECustomer = false;
        
        for (int i=0; i < customerConfigs.length; i++)
        {
        	CustomerConfiguration cc = (CustomerConfiguration)customerConfigs[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("TABE_Customer")) {
            	TABECustomer = true;
            }
        }
        return TABECustomer;
    }
    
    private CustomerConfiguration [] getCustomerConfigurations(Integer customerId)
    {               
        CustomerConfiguration [] ccArray = null;
        try
        {      
            ccArray = this.testSessionStatus.getCustomerConfigurations(this.userName, customerId);    
           
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }        
        return ccArray;
    }
    
    private Boolean hasUploadDownloadConfig()
    {
        Boolean hasUploadDownloadConfig = Boolean.FALSE;
        try {   
            hasUploadDownloadConfig = this.testSessionStatus.hasUploadDownloadConfig(this.userName);
        } 
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return hasUploadDownloadConfig;
    }

    private Boolean hasProgramStatusConfig()
    {	
        Boolean hasProgramStatusConfig = Boolean.FALSE;
        try {   
            hasProgramStatusConfig = this.testSessionStatus.hasProgramStatusConfig(this.userName);
        } 
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return hasProgramStatusConfig;
    }
    
    /**
	 * Bulk Accommodation
	 */
	private Boolean customerHasBulkAccommodation(CustomerConfiguration[] customerConfigurations) 
	{
		boolean hasBulkStudentConfigurable = false;
		if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++) {

				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Bulk_Accommodation") && 
						cc.getDefaultValue().equals("T")) {
					hasBulkStudentConfigurable = true; 
					break;
				}
			}
		}
		return new Boolean(hasBulkStudentConfigurable);           
	}
	
	/**
	 * Bulk Move
	 */
	private Boolean customerHasBulkMove(CustomerConfiguration[] customerConfigurations) 
	{
		boolean hasBulkStudentConfigurable = false;
		if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++) {

				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Bulk_Move_Students") && 
						cc.getDefaultValue().equals("T")) {
					hasBulkStudentConfigurable = true; 
					break;
				}
			}
		}
		return new Boolean(hasBulkStudentConfigurable);           
	}
	// Changes for Out Of School
	/**
	 * Out Of School
	 */
	private Boolean customerHasOOS(CustomerConfiguration[] customerConfigurations) 
	{
		boolean hasOOSConfigurable = false;
		if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++) {

				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("OOS_Configurable") && 
						cc.getDefaultValue().equals("T")) {
					hasOOSConfigurable = true; 
					break;
				}
			}
		}
		return new Boolean(hasOOSConfigurable);           
	}

	
	private void getConfigStudentLabel(CustomerConfiguration[] customerConfigurations) 
	{     
		boolean isStudentIdConfigurable = false;
		Integer configId=0;
		String []valueForStudentId = new String[8] ;
		valueForStudentId[0] = "Student ID";
		for (int i=0; i < customerConfigurations.length; i++)
		{
			CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Student_ID") && cc.getDefaultValue().equalsIgnoreCase("T"))
			{
				isStudentIdConfigurable = true; 
				configId = cc.getId();
				CustomerConfigurationValue[] customerConfigurationsValue = null;
				if(cc.getCustomerConfigurationValues()== null) {
					customerConfigurationsValue = customerConfigurationValues(configId);
				} else {
					customerConfigurationsValue = cc.getCustomerConfigurationValues();
				}
				
				//By default there should be 3 entries for customer configurations
				valueForStudentId = new String[8];
				for(int j=0; j<customerConfigurationsValue.length; j++){
					int sortOrder = customerConfigurationsValue[j].getSortOrder();
					valueForStudentId[sortOrder-1] = customerConfigurationsValue[j].getCustomerConfigurationValue();
				}	
				valueForStudentId[0] = valueForStudentId[0]!= null ? valueForStudentId[0] : "Student ID" ;

			}

		}
		this.getRequest().setAttribute("studentIdLabelName",valueForStudentId[0]);
		
	}
	
	private CustomerConfigurationValue[] customerConfigurationValues(Integer configId)
	{	
		CustomerConfigurationValue[] customerConfigurationsValue = null;
		try {
			customerConfigurationsValue = this.testSessionStatus.getCustomerConfigurationsValue(configId);

		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		return customerConfigurationsValue;
	}
	
	@Jpf.Action()
    protected Forward broadcastMessage()
    {
        HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		
		if (this.userName == null) {
			getLoggedInUserPrincipal();
			this.userName = (String)getSession().getAttribute("userName");
		}
		
		List broadcastMessages = BroadcastUtils.getBroadcastMessages(this.message, this.userName);
        String bcmString = BroadcastUtils.buildBroadcastMessages(broadcastMessages);
		
		try{
    		resp.setContentType(CONTENT_TYPE_JSON);
			try {
				stream = resp.getOutputStream();
	    		resp.flushBuffer();
	    		stream.write(bcmString.getBytes());
			} 
			finally {
				if (stream!=null){
					stream.close();
				}
			}
			
			try {
				Gson gson = new Gson();
				String json = gson.toJson("");
				resp.setContentType(CONTENT_TYPE_JSON);
				resp.flushBuffer();
				stream = resp.getOutputStream();
				stream.write(json.getBytes("UTF-8"));

			} finally{
				if (stream!=null){
					stream.close();
				}
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
        
        return null;
    }
	
}
