package dataExportPageFlow;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import utils.DataExportSearchUtils;
import utils.FilterSortPageUtils;
import utils.Message;
import utils.MessageResourceBundle;

import com.ctb.bean.dataExportManagement.CustomerConfigurationValue;
import com.ctb.bean.dataExportManagement.ManageJobData;
import com.ctb.bean.dataExportManagement.ManageStudent;
import com.ctb.bean.dataExportManagement.ManageStudentData;
import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.ManageTestSessionData;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.jmsutils.ExportDataJMSUtil;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.ctb.widgets.bean.PagerSummary;

@Jpf.Controller()
public class DataExportPageFlowController extends PageFlowController {
	private static final long serialVersionUID = 1L;
	private User user = null;
	private String userName = null;
	private Integer customerId = null;
	
	public String pageTitle = null;
	public String pageMessage = null;
	
	CustomerConfiguration[] customerConfigurations = null;
	CustomerConfigurationValue[] customerConfigurationsValue = null;

	private static final String ACTION_DEFAULT = "defaultAction";
	private static final String ACTION_FIND_STUDENT = "findStudent";
	private static final String ACTION_VIEW_STATUS = "getExportStatus";
	
	private static final String ACTION_APPLY_SEARCH = "applySearch";
	private static final String ACTION_CLEAR_SEARCH = "clearSearch";
	
	private static final String ACTION_VALIDATE_SEARCH = "validateStudent";
	private static final String ACTION_CURRENT_ELEMENT = "{actionForm.currentAction}";
	private boolean searchApplied = false;
	private Integer totalStudentCount = 0;
	private Integer unscoredStudentCount = 0;
	private Integer scheduledStudentCount = 0;
	private Integer notTakenStudentCount = 0;
	private Integer notCompletedStudentCount = 0;
	private List<Integer> toBeExportedStudentRosterList;
	private String  previousPage = "StudentForExport";   
	private String  pageId = "1";
	private boolean islaslinkCustomer = false;
	ArrayList toBeExportedRosterList = new ArrayList();

	private DataExportForm savedForm;
	

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
	
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.OrgNode orgnode;

	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "beginDataExport.do") })
	protected Forward begin() {
		retrieveInfoFromSession();
		return new Forward("success");
	}

	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "getStudentForExport.do")})
	public Forward beginDataExport() {	
		
		DataExportForm form = initialize(ACTION_FIND_STUDENT);
		customerHasScoring();
		isTopLevelLaslinkUser();
		return new Forward("success", form);
	}

	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "session_student_for_export.jsp") }, 
			validationErrorForward = @Jpf.Forward(name = "failure", path = "logout.do"))
	public Forward getStudentForExport(DataExportForm form) {

		form.validateValues();

		String currentAction = form.getCurrentAction();
		String actionElement = form.getActionElement();
		form.resetValuesForAction(actionElement); 
		ManageTestSessionData mtsData = null;
		int emptyTestSessionList = 0;
		mtsData = getTestSessionsWithStudentsToBeExported(form);
		
		if ((mtsData != null) && (mtsData.getFilteredCount().intValue() == 0)) {
			this.getRequest().setAttribute("searchResultEmpty",	MessageResourceBundle.getMessage("exportStudentForTestSessionSearchResultEmpty"));
		}else{
			this.getRequest().removeAttribute("searchResultEmpty");
			
		}
		
		if ((mtsData != null) && (mtsData.getFilteredCount().intValue() > 0)) {
			
			List testSessionList = DataExportSearchUtils.buildTestSessionsWithStudentToBeExportedList(mtsData);
			int testSessionlistSize = testSessionList.size();
			PagerSummary testSessionPagerSummary = DataExportSearchUtils.buildTestSessionPagerSummary(mtsData, form.getTestSessionStudentPageRequested());
			form.setTestSessionStudentMaxPage(mtsData.getFilteredPages());
			this.getRequest().setAttribute("testSessionList", testSessionList);
			this.getRequest().setAttribute("testSessionlistSize", testSessionlistSize);
			this.getRequest().setAttribute("testSessionPagerSummary",testSessionPagerSummary);
			this.setTotalStudentCount(mtsData.getTotalExportedStudentCount());
			this.scheduledStudentCount = mtsData.getScheduledStudentCount();
			this.notTakenStudentCount = mtsData.getNotTakenStudentCount();
			this.notCompletedStudentCount = mtsData.getNotCompletedStudentCount();
			this.toBeExportedStudentRosterList = mtsData.getToBeExportedStudentRosterList();
			
		}
		this.pageTitle = "Data Export: Test Session With Students";
		this.savedForm = form.createClone();
		form.setCurrentAction(ACTION_DEFAULT);
		return new Forward("success",form);
	}
	
	
	
	@Jpf.Action(forwards = {
			@Jpf.Forward(name = "validate", path = "unscored_student_list.jsp"),
			@Jpf.Forward(name = "confirm", path = "dataexport_summary.jsp")})
	public Forward validateAndConfirm(DataExportForm form) {
		Forward forward;
		//form.init();
		form.validateValues();
		
		String currentAction = form.getCurrentAction();
		String actionElement = form.getActionElement();
		
		form.resetValuesForAction(actionElement); 		

		ManageStudentData msData = null;
		msData = getAllUnscoredUnexportedStudents(form);	
		
		if ((msData != null) && (msData.getFilteredCount() !=null && msData.getFilteredCount().intValue() == 0)) {
			this.getRequest().setAttribute("searchResultEmpty",	MessageResourceBundle.getMessage("unscoredStudentSearchResultEmpty"));
		}else{
			this.getRequest().removeAttribute("searchResultEmpty");
			
		}
		
		if ((msData != null) && (msData.getFilteredCount() !=null && msData.getFilteredCount().intValue() > 0)) {
			//3
			
			List studentList = DataExportSearchUtils.buildExportStudentList(msData);
			for (int i = 0; i <studentList.size(); i++ ){
				ManageStudent student = (ManageStudent)studentList.get(i);
				student.setScoringStatus("Incomplete");
			}
			
			PagerSummary unscoredStudentPagerSummary = DataExportSearchUtils.buildStudentPagerSummary(msData, form.getUnscoredStudentPageRequested());
			form.setUnscoredStudentMaxPage(msData.getFilteredPages());
			
			this.pageMessage = MessageResourceBundle.getMessage("dataExportSearchResultFound");
			this.getRequest().setAttribute("studentList", studentList);
			this.getRequest().setAttribute("unscoredStudentPagerSummary",unscoredStudentPagerSummary);
			this.setUnscoredStudentCount(msData.getTotalCount());
			
			this.pageTitle = "Data Export: Student Scoring Incomplete";
			
			if(previousPage == "UnscoredStudent"){
				if(pageId == "1"){
					previousPage = "StudentForExport";
				}
				/*else if(pageId == "2"){
					previousPage = "StudentNotToBeExported";
				}*/
			}			
			
			forward = new Forward("validate",form);
			
		} else {		
			
			this.setUnscoredStudentCount(0);
			this.pageTitle = "Data Export: Summary";
			
			this.savedForm = form.createClone();
			form.setCurrentAction(ACTION_DEFAULT);				
			forward = new Forward("confirm",form);
			
		}	
		
		return forward;
		
	}


	@Jpf.Action(forwards = {
			@Jpf.Forward(name = "success", path = "dataexport_summary.jsp")})
	public Forward gotoSummary(DataExportForm form) {
		previousPage = "UnscoredStudent";
		
		this.pageTitle = "Data Export: Summary";
		return new Forward("success",form);
		
	}

	
	/**
     * @jpf:action
     */
    @Jpf.Action()
    protected Forward goto_item_list(DataExportForm form)
    {
        try
        {
            String contextPath = "/HandScoringWeb/scorebystudent/beginDisplayStudItemList.do";
            String loginName = this.getRequest().getParameter("loginName");
            String testAdminId = this.getRequest().getParameter("testAdminId");
            String testRosterId = this.getRequest().getParameter("testRosterId");
            String itemSetIdTC = this.getRequest().getParameter("itemSetIdTC");
            
            String url = contextPath + "?loginName=" + loginName;
            url = url + "&testAdminId=" + testAdminId;
            url = url + "&testRosterId=" + testRosterId;
            url = url + "&itemSetIdTC=" + itemSetIdTC;
            url = url + "&dataExport=T";
            
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    } 
  
    
    @Jpf.Action(forwards = {@Jpf.Forward(name = "unscoredStudentList", path = "validateAndConfirm.do")})
    protected Forward returnToFindStudent() {
    	//String param = this.getRequest().getParameter("itemBack");

    	//if(param == "T"){
    		//pageId = "3";
    		return new Forward("unscoredStudentList", this.savedForm);
    	/*}
    	else{
    		
    		return new Forward("success", this.savedForm);
    	}*/

    	
    }
	
    @Jpf.Action(forwards = { @Jpf.Forward(name = "StudentForExport", path = "getStudentForExport.do"),
    							@Jpf.Forward(name = "validateAndConfirm", path = "validateAndConfirm.do")})
    protected Forward backToPreviousPage(DataExportForm form) {
    	
    	if(previousPage.equals("StudentForExport"))
    		return new Forward("StudentForExport", form);
    	else if(previousPage.equals("UnscoredStudent"))
    		return new Forward("validateAndConfirm", form);
    	
    	return null;
    	
    }
    
    @Jpf.Action(forwards = {
			@Jpf.Forward(name = "success", path = "dataexport_summary.jsp")})
	protected Forward submitJob(DataExportForm form) {
	   
	   Integer userId = user.getUserId();
	   Integer studentCount = this.toBeExportedStudentRosterList.size();
	   Integer jobId = DataExportSearchUtils.getSubmitJobIdAndStartExport(this.dataexportManagement,userId,studentCount);
	   
	   ExportDataJMSUtil exportDataJMSUtil = null;
		 try {
			 exportDataJMSUtil = new ExportDataJMSUtil ();
		     System.out.println("Exported Roster List:"+this.toBeExportedStudentRosterList);
			 exportDataJMSUtil.initGenerateReportTask (userName, customerId, userId, jobId, this.toBeExportedStudentRosterList);
		} catch (CTBBusinessException e) {
			e.printStackTrace();
		}

	   this.getRequest().setAttribute("jobId", jobId);
	   this.getRequest().setAttribute("submitJobResult", MessageResourceBundle.getMessage("submitJobResult"));     
       
	   return new Forward("success",form);
	
	}
    
    @Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "getExportStatus.do") })
	protected Forward beginViewStatus() {
		retrieveInfoFromSession();
		DataExportForm form = initialize(ACTION_FIND_STUDENT);
		customerHasScoring();
		isTopLevelLaslinkUser();
		return new Forward("success",form);
	}
    
    @Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "view_export_status.jsp") }, 
			validationErrorForward = @Jpf.Forward(name = "failure", path = "logout.do"))
	public Forward getExportStatus(DataExportForm form) {
    	
    	form.validateValues();

		String currentAction = form.getCurrentAction();
		String actionElement = form.getActionElement();
		form.resetValuesForAction(actionElement); 
		ManageJobData msData = null;
		
		msData = getDataExportJobStatus(form);
		
		if ((msData != null) && (msData.getFilteredCount().intValue() == 0)) {
			this.getRequest().setAttribute("searchResultEmpty",	MessageResourceBundle.getMessage("jobSearchResultEmpty"));
		}else{
			this.getRequest().removeAttribute("searchResultEmpty");
			
		}
		
		if ((msData != null) && (msData.getFilteredCount().intValue() > 0)) {
			//1
			List jobList = DataExportSearchUtils.buildExportJobList(msData);
			
			PagerSummary jobPagerSummary = DataExportSearchUtils.buildJobPagerSummary(msData, form.getJobPageRequested());
			form.setJobMaxPage(msData.getFilteredPages());
			this.pageMessage = MessageResourceBundle.getMessage("viewStatusPageMessage");
			this.getRequest().setAttribute("jobList", jobList);
			this.getRequest().setAttribute("jobPagerSummary",jobPagerSummary);
			this.setTotalStudentCount(msData.getTotalCount());
		}
		this.pageTitle = "Data Export: View Status";
		this.savedForm = form.createClone();
		form.setCurrentAction(ACTION_DEFAULT);
		return new Forward("success",form);
	}
    
    /**
     * @jpf:action
     */
    @Jpf.Action()
    protected Forward goto_home()
    {
        try
        {
            String contextPath = "/TestSessionInfoWeb/homepage/HomePageController.jpf";
            
            
            String url = contextPath ;
            
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    } 
    
    
	private ManageTestSessionData getTestSessionsWithStudentsToBeExported(DataExportForm form) {
		String actionElement = form.getActionElement();
		
		PageParams page = FilterSortPageUtils.buildPageParams(form.getTestSessionStudentPageRequested(), FilterSortPageUtils.PAGESIZE_10);
		SortParams sort = FilterSortPageUtils.buildTestSessionStudentSortParams(form.getTestSessionStudentSortColumn(), form.getTestSessionStudentSortOrderBy());
		FilterParams filter = FilterSortPageUtils.buildFilterParams(null);

		ManageTestSessionData mtsData = null;		
		
		mtsData = DataExportSearchUtils.getTestSessionsWithUnexportedStudents(
				this.dataexportManagement, this.customerId, filter,
				page, sort);
		
		return mtsData;
	}
	
	
	private ManageStudentData getAllUnscoredUnexportedStudents(DataExportForm form) {
		String actionElement = form.getActionElement();
		
		PageParams page = FilterSortPageUtils.buildPageParams(form.getTestSessionStudentPageRequested(), FilterSortPageUtils.PAGESIZE_10);
		SortParams sort = FilterSortPageUtils.buildStudentSortParams(form.getTestSessionStudentSortColumn(), form.getTestSessionStudentSortOrderBy());
		FilterParams filter = FilterSortPageUtils.buildFilterParams(null);

		ManageStudentData msData = null;
		List toBeExportedStudentRosterList = this.toBeExportedStudentRosterList;
		msData = DataExportSearchUtils.getAllUnscoredUnexportedStudentsDetail(toBeExportedStudentRosterList,this.dataexportManagement, customerId, filter, page, sort);
		
		
		return msData;
	}
	
	private ManageJobData getDataExportJobStatus(DataExportForm form) {
		String actionElement = form.getActionElement();
		Integer userId = user.getUserId();
		PageParams page = FilterSortPageUtils.buildPageParams(form.getJobPageRequested(), FilterSortPageUtils.PAGESIZE_10);
		SortParams sort = FilterSortPageUtils.buildJobSortParams(form.getJobSortColumn(), form.getJobSortOrderBy());
		FilterParams filter = FilterSortPageUtils.buildFilterParams(null);

		ManageJobData msData = null;		
		
		msData = DataExportSearchUtils.getDataExportJobStatus(
				this.dataexportManagement, userId, filter,
				page, sort);
		
		return msData;
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

	private boolean retrieveInfoFromSession() {
		boolean success = true;
		this.userName = (String) getSession().getAttribute("userName");
		if (this.userName == null)
			success = false;
		return success;
	}

	public DataExportForm initialize(String action) {
		getUserDetails();
		this.savedForm = new DataExportForm();
		this.savedForm.init();
		this.getSession().setAttribute("userHasReports", userHasReports());

		return this.savedForm;

	}

	/**
	 * getUserDetails
	 */
	private void getUserDetails() {
		java.security.Principal principal = getRequest().getUserPrincipal();
		if (principal != null)
			this.userName = principal.toString();
		else
			this.userName = (String) getSession().getAttribute("userName");

		try {
			this.user = this.dataexportManagement.getUserDetails(this.userName,
					this.userName);
			this.customerId = user.getCustomer().getCustomerId();
			
		} catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		getSession().setAttribute("userName", this.userName);

		getCustomerConfigurations();
	}

	/**
	 * userHasReports
	 */
	private Boolean userHasReports() {
		boolean hasReports = false;
		try {
			Customer customer = this.user.getCustomer();
			Integer customerId = customer.getCustomerId();
			hasReports = this.dataexportManagement.userHasReports(this.userName,
					customerId);
		} catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		return new Boolean(hasReports);
	}

	
		
	/**
	 * initPagingSorting
	 */
	private void initPagingSorting(DataExportForm form) {
		String actionElement = form.getActionElement();

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
	 * FormData get and set methods may be overwritten by the Form Bean editor.
	 */
	public static class DataExportForm extends SanitizedFormData {
		private static final long serialVersionUID = 1L;
		private String actionElement;
		private String currentAction;
		private Message message;

		// student pager
		private String studentSortColumn;
		private String studentSortOrderBy;
		private Integer studentPageRequested;
		private Integer studentMaxPage;
		
		private String testSessionStudentSortColumn;
		private String testSessionStudentSortOrderBy;
		private Integer testSessionStudentPageRequested;
		private Integer testSessionStudentMaxPage;
		
		private String unscoredStudentSortColumn;
		private String unscoredStudentSortOrderBy;
		private Integer unscoredStudentPageRequested;
		private Integer unscoredStudentMaxPage;

		private String jobSortColumn;
		private String jobSortOrderBy;
		private Integer jobPageRequested;
		private Integer jobMaxPage;
		
				
		/**
		 * @return the unscoredStudentSortColumn
		 */
		public String getUnscoredStudentSortColumn() {
			return unscoredStudentSortColumn;
		}


		/**
		 * @param unscoredStudentSortColumn the unscoredStudentSortColumn to set
		 */
		public void setUnscoredStudentSortColumn(String unscoredStudentSortColumn) {
			this.unscoredStudentSortColumn = unscoredStudentSortColumn;
		}


		/**
		 * @return the unscoredStudentSortOrderBy
		 */
		public String getUnscoredStudentSortOrderBy() {
			return unscoredStudentSortOrderBy;
		}


		/**
		 * @param unscoredStudentSortOrderBy the unscoredStudentSortOrderBy to set
		 */
		public void setUnscoredStudentSortOrderBy(String unscoredStudentSortOrderBy) {
			this.unscoredStudentSortOrderBy = unscoredStudentSortOrderBy;
		}


		/**
		 * @return the unscoredStudentPageRequested
		 */
		public Integer getUnscoredStudentPageRequested() {
			return unscoredStudentPageRequested;
		}


		/**
		 * @param unscoredStudentPageRequested the unscoredStudentPageRequested to set
		 */
		public void setUnscoredStudentPageRequested(Integer unscoredStudentPageRequested) {
			this.unscoredStudentPageRequested = unscoredStudentPageRequested;
		}




		public void init()
		{
			this.actionElement = ACTION_DEFAULT;
			this.currentAction = ACTION_DEFAULT;
			//clearSearch();
			
			this.studentSortColumn = FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN;
			this.studentSortOrderBy = FilterSortPageUtils.ASCENDING;      
			this.studentPageRequested = new Integer(1);       
			this.studentMaxPage = new Integer(1);
			
			this.testSessionStudentSortColumn = FilterSortPageUtils.TEST_SESSION_DEFAULT_SORT_COLUMN;
			this.testSessionStudentSortOrderBy = FilterSortPageUtils.ASCENDING;      
			this.testSessionStudentPageRequested = new Integer(1);       
			this.testSessionStudentMaxPage = new Integer(1);
			
			this.unscoredStudentSortColumn = FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN;
			this.unscoredStudentSortOrderBy = FilterSortPageUtils.ASCENDING;      
			this.unscoredStudentPageRequested = new Integer(1);       
			this.unscoredStudentMaxPage = new Integer(1);
			
			this.jobSortColumn = FilterSortPageUtils.JOB_DEFAULT_SORT_COLUMN;
			this.jobSortOrderBy = FilterSortPageUtils.DESCENDING;      
			this.jobPageRequested = new Integer(1);       
			this.jobMaxPage = new Integer(1);
		} 
		
		
		public DataExportForm createClone() {
			DataExportForm copied = new DataExportForm();

			copied.setActionElement(this.actionElement);
			copied.setCurrentAction(this.currentAction);

			copied.setStudentSortColumn(this.studentSortColumn);
			copied.setStudentSortOrderBy(this.studentSortOrderBy);
			copied.setStudentPageRequested(this.studentPageRequested);
			copied.setStudentMaxPage(this.studentMaxPage);
			
			copied.setTestSessionStudentSortColumn(this.testSessionStudentSortColumn);
			copied.setTestSessionStudentSortOrderBy(this.testSessionStudentSortOrderBy);
			copied.setTestSessionStudentPageRequested(this.testSessionStudentPageRequested);
			copied.setTestSessionStudentMaxPage(this.testSessionStudentMaxPage);
			
			copied.setUnscoredStudentSortColumn(this.unscoredStudentSortColumn);
			copied.setUnscoredStudentSortOrderBy(this.unscoredStudentSortOrderBy);
			copied.setUnscoredStudentPageRequested(this.unscoredStudentPageRequested);
			copied.setUnscoredStudentMaxPage(this.unscoredStudentMaxPage);
			
			// copied.setStudentProfile(this.studentProfile);

			return copied;
		}
		
		public void validateValues()
		{
			if (this.studentSortColumn == null)
				this.studentSortColumn = FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN;

			if (this.studentSortOrderBy == null)
				this.studentSortOrderBy = FilterSortPageUtils.ASCENDING;

			if (this.studentPageRequested == null) {
				this.studentPageRequested = new Integer(1);
			}

			if (this.studentPageRequested.intValue() <= 0)            
				this.studentPageRequested = new Integer(1);

			if (this.studentMaxPage == null)
				this.studentMaxPage = new Integer(1);
			if (this.studentPageRequested.intValue() > this.studentMaxPage.intValue()) {
				this.studentPageRequested = new Integer(this.studentMaxPage.intValue());                
				
			}
			
			if (this.testSessionStudentSortColumn == null)
				this.testSessionStudentSortColumn = FilterSortPageUtils.TEST_SESSION_DEFAULT_SORT_COLUMN;

			if (this.testSessionStudentSortOrderBy == null)
				this.testSessionStudentSortOrderBy = FilterSortPageUtils.ASCENDING;

			if (this.testSessionStudentPageRequested == null) {
				this.testSessionStudentPageRequested = new Integer(1);
			}

			if (this.testSessionStudentPageRequested.intValue() <= 0)            
				this.testSessionStudentPageRequested = new Integer(1);

			if (this.testSessionStudentMaxPage == null)
				this.testSessionStudentMaxPage = new Integer(1);
			if (this.testSessionStudentPageRequested.intValue() > this.testSessionStudentMaxPage.intValue()) {
				this.testSessionStudentPageRequested = new Integer(this.testSessionStudentMaxPage.intValue());                
				
			}
			
			
			if (this.unscoredStudentSortColumn == null)
				this.unscoredStudentSortColumn = FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN;

			if (this.unscoredStudentSortOrderBy == null)
				this.unscoredStudentSortOrderBy = FilterSortPageUtils.ASCENDING;

			if (this.unscoredStudentPageRequested == null) {
				this.unscoredStudentPageRequested = new Integer(1);
			}

			if (this.unscoredStudentPageRequested.intValue() <= 0)            
				this.unscoredStudentPageRequested = new Integer(1);

			if (this.unscoredStudentMaxPage == null)
				this.unscoredStudentMaxPage = new Integer(1);
			if (this.unscoredStudentPageRequested.intValue() > this.unscoredStudentMaxPage.intValue()) {
				this.unscoredStudentPageRequested = new Integer(this.unscoredStudentMaxPage.intValue());                
				
			}
			
			if (this.jobSortColumn == null)
				this.jobSortColumn = FilterSortPageUtils.JOB_DEFAULT_SORT_COLUMN;

			if (this.jobSortOrderBy == null)
				this.jobSortOrderBy = FilterSortPageUtils.DESCENDING;

			if (this.jobPageRequested == null) {
				this.jobPageRequested = new Integer(1);
			}

			if (this.jobPageRequested.intValue() <= 0)            
				this.jobPageRequested = new Integer(1);

			if (this.jobMaxPage == null)
				this.jobMaxPage = new Integer(1);
			if (this.jobPageRequested.intValue() > this.jobMaxPage.intValue()) {
				this.jobPageRequested = new Integer(this.jobMaxPage.intValue());                
				
			}
			
			
			
		}
		
		/**
		 * @param studentSortColumn
		 *            the studentSortColumn to set
		 */
		public void setStudentSortColumn(String studentSortColumn) {
			this.studentSortColumn = studentSortColumn;
		}

		/**
		 * @return the studentSortColumn
		 */
		public String getStudentSortColumn() {
			return studentSortColumn;
		}
		
		/**
		 * @return the studentSortOrderBy
		 */
		public String getStudentSortOrderBy() {
			return studentSortOrderBy;
		}

		/**
		 * @param studentSortOrderBy
		 *            the studentSortOrderBy to set
		 */
		public void setStudentSortOrderBy(String studentSortOrderBy) {
			this.studentSortOrderBy = studentSortOrderBy;
		}

		/**
		 * @return the studentPageRequested
		 */
		public Integer getStudentPageRequested() {
			return studentPageRequested;
		}

		/**
		 * @param studentPageRequested
		 *            the studentPageRequested to set
		 */
		public void setStudentPageRequested(Integer studentPageRequested) {
			this.studentPageRequested = studentPageRequested;
		}

		/**
		 * @return the studentMaxPage
		 */
		public Integer getStudentMaxPage() {
			return studentMaxPage;
		}

		/**
		 * @param studentMaxPage
		 *            the studentMaxPage to set
		 */
		public void setStudentMaxPage(Integer studentMaxPage) {
			this.studentMaxPage = studentMaxPage;
		}


		
		/**
		 * @param studentSortColumn
		 *            the studentSortColumn to set
		 */
		public void setTestSessionStudentSortColumn(String testSessionStudentSortColumn) {
			this.testSessionStudentSortColumn = testSessionStudentSortColumn;
		}

		/**
		 * @return the studentSortColumn
		 */
		public String getTestSessionStudentSortColumn() {
			return testSessionStudentSortColumn;
		}
		
		/**
		 * @return the studentSortOrderBy
		 */
		public String getTestSessionStudentSortOrderBy() {
			return testSessionStudentSortOrderBy;
		}

		/**
		 * @param studentSortOrderBy
		 *            the studentSortOrderBy to set
		 */
		public void setTestSessionStudentSortOrderBy(String testSessionStudentSortOrderBy) {
			this.testSessionStudentSortOrderBy = testSessionStudentSortOrderBy;
		}

		/**
		 * @return the studentPageRequested
		 */
		public Integer getTestSessionStudentPageRequested() {
			return testSessionStudentPageRequested;
		}

		/**
		 * @param studentPageRequested
		 *            the studentPageRequested to set
		 */
		public void setTestSessionStudentPageRequested(Integer testSessionStudentPageRequested) {
			this.testSessionStudentPageRequested = testSessionStudentPageRequested;
		}

		/**
		 * @return the studentMaxPage
		 */
		public Integer getTestSessionStudentMaxPage() {
			return testSessionStudentMaxPage;
		}

		/**
		 * @param studentMaxPage
		 *            the studentMaxPage to set
		 */
		public void setTestSessionStudentMaxPage(Integer testSessionStudentMaxPage) {
			this.testSessionStudentMaxPage = testSessionStudentMaxPage;
		}


		/**
		 * @return the actionElement
		 */
		public String getActionElement() {
			return actionElement;
		}

		/**
		 * @param actionElement
		 *            the actionElement to set
		 */
		public void setActionElement(String actionElement) {
			this.actionElement = actionElement;
		}

		/**
		 * @return the currentAction
		 */
		public String getCurrentAction() {
			return currentAction;
		}

		/**
		 * @param currentAction
		 *            the currentAction to set
		 */
		public void setCurrentAction(String currentAction) {
			this.currentAction = currentAction;
		}

		/**
		 * @return the message
		 */
		public Message getMessage() {
			return message;
		}

		/**
		 * @param message
		 *            the message to set
		 */
		public void setMessage(Message message) {
			this.message = message;
		}

		public void setMessage(String title, String content, String type) {
			this.message = new Message(title, content, type);
		}
		
		public void resetValuesForAction(String actionElement) 
		{
			
			if (actionElement.equals("{actionForm.testSessionStudentSortOrderBy}")) {
				this.testSessionStudentPageRequested = new Integer(1);
			}
			
			if (actionElement.equals("{actionForm.unscoredStudentSortOrderBy}")) {
				this.unscoredStudentPageRequested = new Integer(1);
			}
			
			if (actionElement.equals("{actionForm.jobSortOrderBy}")) {
				this.jobPageRequested = new Integer(1);
			}
		
		}


		/**
		 * @return the unscoredStudentMaxPage
		 */
		public Integer getUnscoredStudentMaxPage() {
			return unscoredStudentMaxPage;
		}


		/**
		 * @param unscoredStudentMaxPage the unscoredStudentMaxPage to set
		 */
		public void setUnscoredStudentMaxPage(Integer unscoredStudentMaxPage) {
			this.unscoredStudentMaxPage = unscoredStudentMaxPage;
		}


		/**
		 * @return the jobSortColumn
		 */
		public String getJobSortColumn() {
			return jobSortColumn;
		}


		/**
		 * @param jobSortColumn the jobSortColumn to set
		 */
		public void setJobSortColumn(String jobSortColumn) {
			this.jobSortColumn = jobSortColumn;
		}


		/**
		 * @return the jobSortOrderBy
		 */
		public String getJobSortOrderBy() {
			return jobSortOrderBy;
		}


		/**
		 * @param jobSortOrderBy the jobSortOrderBy to set
		 */
		public void setJobSortOrderBy(String jobSortOrderBy) {
			this.jobSortOrderBy = jobSortOrderBy;
		}


		/**
		 * @return the jobPageRequested
		 */
		public Integer getJobPageRequested() {
			return jobPageRequested;
		}


		/**
		 * @param jobPageRequested the jobPageRequested to set
		 */
		public void setJobPageRequested(Integer jobPageRequested) {
			this.jobPageRequested = jobPageRequested;
		}


		/**
		 * @return the jobMaxPage
		 */
		public Integer getJobMaxPage() {
			return jobMaxPage;
		}


		/**
		 * @param jobMaxPage the jobMaxPage to set
		 */
		public void setJobMaxPage(Integer jobMaxPage) {
			this.jobMaxPage = jobMaxPage;
		}


		
	}

	/**
	 * @return the pageTitle
	 */
	public String getPageTitle() {
		return pageTitle;
	}

	/**
	 * @param pageTitle
	 *            the pageTitle to set
	 */
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	/**
	 * @return the pageMessage
	 */
	public String getPageMessage() {
		return pageMessage;
	}

	/**
	 * @param pageMessage
	 *            the pageMessage to set
	 */
	public void setPageMessage(String pageMessage) {
		this.pageMessage = pageMessage;
	}

	
	/**
	 * @return the savedForm
	 */
	public DataExportForm getSavedForm() {
		return savedForm;
	}

	/**
	 * @param savedForm the savedForm to set
	 */
	public void setSavedForm(DataExportForm savedForm) {
		this.savedForm = savedForm;
	}

	/**
	 * @return the totalStudentCount
	 */
	public Integer getTotalStudentCount() {
		return totalStudentCount;
	}

	/**
	 * @param totalStudentCount the totalStudentCount to set
	 */
	public void setTotalStudentCount(Integer totalStudentCount) {
		this.totalStudentCount = totalStudentCount;
	}

	/**
	 * @return the unscoredStudentCount
	 */
	public Integer getUnscoredStudentCount() {
		return unscoredStudentCount;
	}

	/**
	 * @param unscoredStudentCount the unscoredStudentCount to set
	 */
	public void setUnscoredStudentCount(Integer unscoredStudentCount) {
		this.unscoredStudentCount = unscoredStudentCount;
	}

	/**
	 * @return the scheduledStudentCount
	 */
	public Integer getScheduledStudentCount() {
		return scheduledStudentCount;
	}

	/**
	 * @param scheduledStudentCount the scheduledStudentCount to set
	 */
	public void setScheduledStudentCount(Integer scheduledStudentCount) {
		this.scheduledStudentCount = scheduledStudentCount;
	}

	/**
	 * @return the notTakenStudentCount
	 */
	public Integer getNotTakenStudentCount() {
		return notTakenStudentCount;
	}

	/**
	 * @param notTakenStudentCount the notTakenStudentCount to set
	 */
	public void setNotTakenStudentCount(Integer notTakenStudentCount) {
		this.notTakenStudentCount = notTakenStudentCount;
	}

	/**
	 * @return the notCompletedStudentCount
	 */
	public Integer getNotCompletedStudentCount() {
		return notCompletedStudentCount;
	}

	/**
	 * @param notCompletedStudentCount the notCompletedStudentCount to set
	 */
	public void setNotCompletedStudentCount(Integer notCompletedStudentCount) {
		this.notCompletedStudentCount = notCompletedStudentCount;
	}

	/**
	 * @return the previousPage
	 */
	public String getPreviousPage() {
		return previousPage;
	}

	/**
	 * @param previousPage the previousPage to set
	 */
	public void setPreviousPage(String previousPage) {
		this.previousPage = previousPage;
	}

	/**
	 * @return the islaslinkCustomer
	 */
	public boolean isIslaslinkCustomer() {
		return islaslinkCustomer;
	}

	/**
	 * @param islaslinkCustomer the islaslinkCustomer to set
	 */
	public void setIslaslinkCustomer(boolean islaslinkCustomer) {
		this.islaslinkCustomer = islaslinkCustomer;
	}

	/**
	 * @return the toBeExportedStudentRosterList
	 */
	public List getToBeExportedStudentRosterList() {
		return toBeExportedStudentRosterList;
	}

	/**
	 * @param toBeExportedStudentRosterList the toBeExportedStudentRosterList to set
	 */
	public void setToBeExportedStudentRosterList(List toBeExportedStudentRosterList) {
		this.toBeExportedStudentRosterList = toBeExportedStudentRosterList;
	}

	


}