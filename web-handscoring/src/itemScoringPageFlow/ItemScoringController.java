package itemScoringPageFlow;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import itemScoringPageFlow.ItemScoringController.ItemScoringForm;

import utils.FilterSortPageUtils;
import utils.ItemScoringUtils;
import utils.JsonUtils;
import utils.MessageResourceBundle;
import utils.ScoringPopupUtil;
import utils.StudentProfileInformation;
import utils.StudentSearchUtils;
import utils.WebUtils;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.studentManagement.CustomerConfiguration;
import com.ctb.bean.studentManagement.CustomerConfigurationValue;
import com.ctb.bean.studentManagement.ManageStudent;
import com.ctb.bean.studentManagement.ManageStudentData;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.RosterElementData;
import com.ctb.bean.testAdmin.RubricViewData;
import com.ctb.bean.testAdmin.ScorableCRAnswerContent;
import com.ctb.bean.testAdmin.ScorableItem;
import com.ctb.bean.testAdmin.ScorableItemData;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.ctb.widgets.bean.PagerSummary;

import utils.Message;



@Jpf.Controller
public class ItemScoringController extends PageFlowController {
	private static final long serialVersionUID = 1L;

	@Control()
	private com.ctb.control.studentManagement.StudentManagement studentManagement;

	@Control()
	private com.ctb.control.crscoring.TestScoring testScoring;

	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.CRScoring scoring;
	
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.OrgNode orgnode;

	private static final String ACTION_DEFAULT         = "defaultAction";
	private static final String ACTION_SCORE_BY_ITEM   = "scoreByItem";
	private static final String ACTION_STUDENT_LIST    = "getStudentsByItem";


	private String userName = null;
	private User user = null;
	private Integer customerId = null;
	public String pageTitle = null;
	public String pageMessage = null;
	private Integer testAdminId = null;
	private Integer itemSetId = null;
	private String  testAccessCode= null;
	private String testSessionName=null;
	private String studentIdLabelName = "Student ID";

	private ItemScoringForm savedForm = null;
	private boolean studentIdConfigurable = false;
	// customer configuration
	CustomerConfiguration[] customerConfigurations = null;
	CustomerConfigurationValue[] customerConfigurationsValue = null;
	private boolean islaslinkCustomer = false;

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
	 * This method represents the point of entry into the pageflow
	 * @jpf:action
	 * @jpf:forward name="success" path="beginFindStudent.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "beginIndivItemScoring.do")
	})
	protected Forward begin()
	{
		return new Forward("success");
	}

	/**
	 * initialize
	 */
	private ItemScoringForm initialize(String action)
	{        
		getUserDetails();
		this.savedForm = new ItemScoringForm();
		this.savedForm.init();
		return this.savedForm;
	}

	/**
	 * This method represents the point of entry into the pageflow
	 * @jpf:action
	 * @jpf:forward name="success" path="findStudent.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",path = "findItemDetails.do")
	})

	protected Forward beginIndivItemScoring()
	{
		ItemScoringForm form = initialize(ACTION_SCORE_BY_ITEM);
		isGeorgiaCustomer(form);
		customerHasBulkAccommodation();
		customerHasScoring();
		isTopLevelUser();
		return new Forward("success", form);
	}

	/**
	 * This method represents the point of entry into the pageflow
	 * @jpf:action
	 * @jpf:forward name="findItem" path="find_item_scoring.do"
	 * */
	@Jpf.Action(forwards = {
			@Jpf.Forward(name = "findItem",	path = "find_item_scoring.jsp")
	})
	protected Forward findItemDetails(ItemScoringForm form)
	{

		form.validateValues();
		String currentAction = form.getCurrentAction();
		String actionElement = form.getActionElement();
		TestSession ts = null;
		form.resetValuesForAction(actionElement, ACTION_SCORE_BY_ITEM);
		if (currentAction.equals(ACTION_DEFAULT)) {

			if(form.testAdminId == null) {
				form.testAdminId = Integer.valueOf(getRequest().getParameter("testAdminId"));
			}
			form.setTestAdminId(form.testAdminId);
			actionElement = ACTION_SCORE_BY_ITEM;
			form.setActionElement(ACTION_SCORE_BY_ITEM);
			form.setCurrentAction(ACTION_SCORE_BY_ITEM);
		}
		initPagingSorting(form);
		FilterParams filter = null;
		PageParams page = FilterSortPageUtils.buildPageParams(form.getItemPageRequested(), FilterSortPageUtils.PAGESIZE_10);
		SortParams sort = FilterSortPageUtils.buildSortParams(form.getItemSortColumn(), form.getItemSortOrderBy(), null, null);
		ScorableItemData siData = null;
		siData = ItemScoringUtils.getItemsByTestSession(testScoring, null, page, sort, form.testAdminId);
		List itemList = ItemScoringUtils.buildItemList(siData);

		try {
			ts = testScoring.getTestAdminDetails(form.testAdminId);
		}  catch (CTBBusinessException be){
			be.printStackTrace();
			String msg = MessageResourceBundle.getMessage(be.getMessage());
			form.setMessage(Message.TEST_SESSION_TITLE, msg, Message.ERROR);
		}

		form.setTestAccessCode(ts.getAccessCode());
		form.setTestSessionName(ts.getTestAdminName());
		form.setTestAdminId(form.testAdminId);
		form.setItemMaxPage(siData.getFilteredPages());
		PagerSummary itemPagerSummary = ItemScoringUtils.buildItemPagerSummary(siData, form.getItemPageRequested());  
		getRequest().setAttribute("itemList", itemList);
		this.getRequest().setAttribute("itemPagerSummary", itemPagerSummary);

		this.pageTitle  = "List Of Items";
		customerHasBulkAccommodation();
		if(itemList.isEmpty())
		{	
			this.getRequest().setAttribute("itemSearchEmpty", MessageResourceBundle.getMessage("itemSearchEmpty"));        
			return new Forward("findItem");
		}
		this.savedForm = form.createClone();

		return new Forward("findItem",form);
	} 



	/**
	 * This method represents the point of entry into the pageflow
	 * @jpf:action
	 * @jpf:forward name="getStudentsByItem" path="goto_student_list.do"
	 * */
	@Jpf.Action(forwards = {
			@Jpf.Forward(name = "getStudentsByItem", path = "item_scoring_student_list.jsp")
	})
	protected Forward goto_student_list(ItemScoringForm form)
	{	
		String currentAction = form.getCurrentAction();
		String actionElement = form.getActionElement();
		if(form.getActionElement().equals(ACTION_DEFAULT)) 
		{
			String testAdminId = getRequest().getParameter("testAdminId");
			String itemSetId = getRequest().getParameter("itemSetId");
			String itemId = getRequest().getParameter("itemId");
			String itemType =getRequest().getParameter("itemType");
			String itemSetName = getRequest().getParameter("itemSetName");
			String itemSetOrder = getRequest().getParameter("itemSetOrder");
			String maxPoints = getRequest().getParameter("maxPoints");
			
		    form.setTestAdminId(new Integer(testAdminId));
			form.setItemSetId(Integer.valueOf(itemSetId));
			form.setItemId(itemId);
			form.setSelectedItemType(itemType);
			form.setSelectedItemSetName(itemSetName);
			form.setSelectedItemNo(Integer.valueOf(itemSetOrder));
			form.setSelectedMaxPoints(Integer.valueOf(maxPoints));
		}
		if(form.getActionElement().equals(ACTION_DEFAULT)) {
			form.setActionElement(ACTION_STUDENT_LIST);
			form.setCurrentAction(ACTION_STUDENT_LIST);
		}
		currentAction = form.getCurrentAction();
		actionElement = form.getActionElement();
		form.resetValuesForAction(actionElement, ACTION_STUDENT_LIST);
		TestSession ts = null;
		String sortColoum = null;
		RosterElementData reData = null;
		form.validateValues();
		initPagingSorting(form);

		FilterParams filter = null;
		PageParams page = FilterSortPageUtils.buildPageParams(form.getStudentPageRequested(), FilterSortPageUtils.PAGESIZE_5);
		SortParams sort = FilterSortPageUtils.buildSortParams(form.getStudentSortColumn(), form.getStudentSortOrderBy(), null, null);

		reData = ItemScoringUtils.getStudentsByItem(testScoring, filter, page, sort, form.testAdminId,form.itemSetId, form.itemId);
		List studentList = ItemScoringUtils.buildStudentListByItem(reData);
		PagerSummary studentPagerSummary = ItemScoringUtils.buildStudentPagerSummary(reData, form.getStudentPageRequested()); 

		try {
			ts = testScoring.getTestAdminDetails(form.testAdminId);
		}  catch (CTBBusinessException be){
			be.printStackTrace();
			String msg = MessageResourceBundle.getMessage(be.getMessage());
			form.setMessage(Message.TEST_SESSION_TITLE, msg, Message.ERROR);
		}

		form.setTestAccessCode(ts.getAccessCode());
		form.setTestSessionName(ts.getTestAdminName());
		form.setStudentMaxPage(reData.getFilteredPages());

		getRequest().setAttribute("studentList", studentList);	

		this.getRequest().setAttribute("studentPagerSummary",studentPagerSummary);
		this.getRequest().setAttribute("testSessionName", form.getTestSessionName());
		this.getRequest().setAttribute("testAccessCode", form.getTestAccessCode());
		this.getRequest().setAttribute("itemNo", form.getSelectedItemNo());
		this.getRequest().setAttribute("itemId", form.getItemId());
		this.getRequest().setAttribute("itemType", form.getSelectedItemType());
		this.getRequest().setAttribute("itemSetName", form.getSelectedItemSetName());
		this.getRequest().setAttribute("maxPoints", form.getSelectedMaxPoints());
		String title="Scoring : " +form.testSessionName;
		this.pageTitle  = title;
		if(studentList.isEmpty())
		{	
			this.getRequest().setAttribute("studentSearchResultEmpty", MessageResourceBundle.getMessage("studentSearchResultEmpty"));        
			return new Forward("getStudentsByItem");
		}

		return new Forward("getStudentsByItem",form);
	}


	/**
	 * getUserDetails
	 */
	private void getUserDetails()
	{
		java.security.Principal principal = getRequest().getUserPrincipal();
		if (principal != null) 
			this.userName = principal.toString();
		else            
			this.userName = (String)getSession().getAttribute("userName");

		try
		{
			this.user = this.studentManagement.getUserDetails(this.userName, this.userName);     
			this.customerId = user.getCustomer().getCustomerId();
		}
		catch (CTBBusinessException be)
		{
			be.printStackTrace();
		}        
		getSession().setAttribute("userName", this.userName);

		getCustomerConfigurations();             
	}

	/**
	 * userHasReports
	 */
	private Boolean userHasReports() 
	{
		boolean hasReports = false;
		try
		{      
			Customer customer = this.user.getCustomer();
			Integer customerId = customer.getCustomerId();   
			hasReports = this.studentManagement.userHasReports(this.userName, customerId);
		}
		catch (CTBBusinessException be)
		{
			be.printStackTrace();
		}        
		return new Boolean(hasReports);           
	}

	/**
	 * Bulk Accommodation
	 */
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
	
	/**
	 * customerHasScoring
	 */
	private Boolean customerHasScoring() {

		boolean hasScoringConfigurable = false;
		 boolean isLaslinkCustomer = false;
		for (CustomerConfiguration cc : customerConfigurations) {
			if (cc.getCustomerConfigurationName().equalsIgnoreCase(
					"Configurable_Hand_Scoring")
					&& cc.getDefaultValue().equals("T")) {
				hasScoringConfigurable = true;
				break;
			}
			 if (cc.getCustomerConfigurationName().equalsIgnoreCase("Laslink_Customer")
						&& cc.getDefaultValue().equals("T")) {
					isLaslinkCustomer = true;
					break;
	            }
		}
		 this.setIslaslinkCustomer(isLaslinkCustomer);
		getSession()
				.setAttribute("isScoringConfigured", hasScoringConfigurable);
		return new Boolean(hasScoringConfigurable);
	}
	
	 //LLO- 118 - Change for Ematrix UI
    private void isTopLevelUser(){
		
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
	 * getCustomerConfigurations
	 */
	private void getCustomerConfigurations()
	{
		try {
			this.customerConfigurations = this.studentManagement.getCustomerConfigurations(this.userName, 

					this.customerId);
		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}
	}

	/*
	 * this method retrieve CustomerConfigurationsValue for provided customer configuration Id.
	 */
	private void customerConfigurationValues(Integer configId)
	{
		try {
			this.customerConfigurationsValue = 

				this.studentManagement.getCustomerConfigurationsValue(configId);

		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}
	}

	/*
	 * //START -changes for defect # 65980
	 * This method retrieve  the value of provide two customer configuration and their corresponding data in customer configuration value.
	 */
	private void isGeorgiaCustomer(ItemScoringForm form) 
    {     
		 boolean isStudentIdConfigurable = false;
		 Integer configId=0;
		
		String []valueForStudentId = new String[8] ;
		
		for (int i=0; i < this.customerConfigurations.length; i++)
	        {
	            CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
	           
	            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Student_ID") && cc.getDefaultValue().equalsIgnoreCase("T"))
				{
					isStudentIdConfigurable = true; 
					configId = cc.getId();
					customerConfigurationValues(configId);
					//By default there should be 3 entries for customer configurations
				valueForStudentId = new String[8];
					for(int j=0; j<this.customerConfigurationsValue.length; j++){
						int sortOrder = this.customerConfigurationsValue[j].getSortOrder();
						valueForStudentId[sortOrder-1] = this.customerConfigurationsValue[j].getCustomerConfigurationValue();
					}	
				
				this.studentIdConfigurable = isStudentIdConfigurable;
				form.setStudentIdConfigurable(this.studentIdConfigurable);
				
					valueForStudentId = getDefaultValue(valueForStudentId,"Student ID", form);
					
				}
	            
	         }
		
		this.getRequest().setAttribute("studentIdArrValue",valueForStudentId);
        this.getRequest().setAttribute("isStudentIdConfigurable",isStudentIdConfigurable);
        
    } 
	/*
	 * //START -changes for defect # 65980
	 * this method retrieve CustomerConfigurationsValue for provided customer configuration Id.
	 */
	private String[] getDefaultValue(String [] arrValue, String labelName, ItemScoringForm form)
	{
		arrValue[0] = arrValue[0] != null ? arrValue[0]   : labelName ;
		arrValue[1] = arrValue[1] != null ? arrValue[1]   : "32" ;
		
		
		if(labelName.equals("Student ID")){
			arrValue[2] = arrValue[2] != null ? arrValue[2]   : "F" ;
			if(!arrValue[2].equals("T") && !arrValue[2].equals("F"))
				{ 
					arrValue[2]  = "F";
				}
			this.studentIdLabelName = arrValue[0];
			form.setStudentIdLabelName(this.studentIdLabelName );
			
		}
		return arrValue;
	}
	/**
	 * initPagingSorting
	 */
	private void initPagingSorting(ItemScoringForm form)
	{
		String actionElement = form.getActionElement();


		if ((actionElement.indexOf("itemPageRequested") > 0) || (actionElement.indexOf("itemSortOrderBy") > 0))
		{
			form.setSelectedItemId(null);
		}
	}
	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="findItemDetails.do"
	 */
	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "findItemDetails.do") })
	protected Forward returnToFindItem(ItemScoringForm form) {
		savedForm.setCurrentAction(ACTION_SCORE_BY_ITEM);
		savedForm.setActionElement(ACTION_SCORE_BY_ITEM);
		savedForm.setTestAdminId(form.getTestAdminId());
		return new Forward("success", this.savedForm);
	}

	 /**
	 * Included for rubric View
	 */
	
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="item_scoring_student_list.jsp")
	})
	protected Forward rubricViewDisplay(ItemScoringForm form){
			
		String jsonResponse = "";
		String itemId = getRequest().getParameter("itemId");
		
	//	itemId = "0155662";//Had to make it static, since only 2 items are present in database now
		RubricViewData[] scr =  getRubricDetails(itemId);
		
		try {
			jsonResponse = JsonUtils.getJson(scr, "rubricData",scr.getClass());

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
	 /**
     *getRubricDetails() for rubricView
     */
    private RubricViewData[] getRubricDetails(String itemId){

    	RubricViewData[] rubricDetailsData = null;
    	try {	
    		rubricDetailsData =  this.testScoring.getRubricDetailsData(itemId);
    	}
    	catch(CTBBusinessException be){
    		be.printStackTrace();
    	}
    	return rubricDetailsData;
    }
    

	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "item_scoring_student_list.jsp") })
	protected Forward beginCRResponseDisplay(ItemScoringForm form){
		try {
			ScoringPopupUtil.processCRResponseDisplay(getRequest(), getResponse(), this.userName, 
					this.testScoring, ScoringPopupUtil.CONTENT_TYPE_JSON) ;
		} catch (Exception e) {
			System.err.println("Exception while processing CR response.");
			e.printStackTrace();
		}

		return null;
		
	}
 private ScorableCRAnswerContent getIndividualCRResponse(String userName,
			Integer testRosterId, Integer deliverableItemSetId, String itemId,
			String itemType){
    	ScorableCRAnswerContent answerArea = new ScorableCRAnswerContent();
    	try{
    		
    		answerArea =  this.testScoring.getCRItemResponseForScoring(userName, testRosterId, deliverableItemSetId, itemId, itemType);
    	}
    	catch(CTBBusinessException be){
    		be.printStackTrace();
    	}
    	
    	
    	return answerArea;
    }
	
//changes for defect #66003
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path ="/itemPlayer/ItemPlayerController.jpf")}, 
	validationErrorForward = @Jpf.Forward(name = "failure",
			path = "logout.do"))
	protected Forward viewQuestionWindow()
	{      
		String param = getRequest().getParameter("param");
		String itemSortNumber = getRequest().getParameter("itemSortNumber");
		getSession().setAttribute("param", param);
		getSession().setAttribute("itemSortNumber", itemSortNumber);

		return new Forward("success");
	}
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "beginDisplayStudItemList.do")
	})
	protected Forward saveDetails(ItemScoringForm form)
	{      
		 String jsonMessageResponse = "";
		 if(user==null){
				getUserDetails();
			}
	 
		String itemId = getRequest().getParameter("itemId");
		Integer itemSetId = Integer.valueOf(getRequest().getParameter("itemSetId"));
		Integer testRosterId =  Integer.valueOf(getRequest().getParameter("rosterId"));
		Integer score = Integer.valueOf(getRequest().getParameter("score"));
		//System.out.println("user.getUserId(), itemId, itemSetId, testRosterId, score :: "+user.getUserId() + itemId +  itemSetId +  testRosterId +  score);
	try {
		 Boolean isSuccess = this.testScoring.saveOrUpdateScore(user.getUserId(), itemId, itemSetId, testRosterId, score);
		 //Start- added for  Process Scores  button
		 String completionStatus = new String("FromItem");
		 ManageStudent ms = new ManageStudent();
		 ms.setIsSuccess(isSuccess);
		 ms.setCompletionStatus(completionStatus);
		 jsonMessageResponse = JsonUtils.getJson(ms, "SaveStatus",ms.getClass());
	     //End - added for  Process Scores  button		
			//System.out.println("jsonResponse:==>"+jsonMessageResponse);
			//	getCRItemResponseForScoring
			
			   HttpServletResponse resp = this.getResponse();     
			   resp.setContentType("application/json");
	           resp.flushBuffer();
		        OutputStream stream = resp.getOutputStream();
		        stream.write(jsonMessageResponse.getBytes());
		        stream.close();
	
	} catch (CTBBusinessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		return null;
	}
	/**
	 * @jpf:action
	 */
	@Jpf.Action()
	protected Forward goToHomePage(ItemScoringForm form) {
		try {

			getResponse().sendRedirect(
					"/TestSessionInfoWeb/homepage/HomePageController.jpf");
		} catch (IOException ioe) {
			System.err.print(ioe.getStackTrace());
		}

		return null;
	}


	public static class ItemScoringForm extends SanitizedFormData
	{

		private String studentIdLabelName = "Student ID";
		private Message message;

		// student profile
		private StudentProfileInformation studentProfile;
		private String actionElement;
		private String currentAction;	


		// item pager
		private Integer selectedItemId;
		private Integer selectedTestAdminId;
		private String itemSortColumn;
		private String itemSortOrderBy;
		private Integer itemPageRequested;
		private Integer itemMaxPage;
		private String testAccessCode;
		private String testSessionName;
		private String itemId;
		private Integer itemSetId;
		private Integer testAdminId;
		private boolean studentIdConfigurable = false;

		// student pager
		private Integer selectedStudentId;
		private Integer selectedItemNo;
		private String selectedItemSetName;
		private Integer selectedMaxPoints;
		private String selectedItemType;
		private String studentSortColumn;
		private String studentSortOrderBy;
		private Integer studentPageRequested;
		private Integer studentMaxPage;
		private Integer scorePoints = null;

		/**
		 * @return the scorePoints
		 */
		public Integer getScorePoints() {
			return scorePoints;
		}

		/**
		 * @param scorePoints the scorePoints to set
		 */
		public void setScorePoints(Integer scorePoints) {
			this.scorePoints = scorePoints;
		}

		/**
		 * @param studentIdConfigurable the studentIdConfigurable to set
		 */
		public void setStudentIdConfigurable(boolean studentIdConfigurable) {
			this.studentIdConfigurable = studentIdConfigurable;
		}

		public ItemScoringForm()
		{
		}

		public void init()
		{
			this.actionElement = ACTION_DEFAULT;
			this.currentAction = ACTION_DEFAULT;

			this.selectedStudentId = null;

			this.selectedItemId = null;
			this.selectedItemNo = null;
			this.selectedItemType = null;
			this.selectedItemSetName= null;
			this.selectedMaxPoints=null;
			this.itemMaxPage = new Integer(1);
			this.itemPageRequested = new Integer(1);
			this.itemSortColumn = FilterSortPageUtils.TESTITEM_DEFAULT_SORT_COLUMN;
			this.itemSortOrderBy = FilterSortPageUtils.ASCENDING;

			this.studentProfile = new StudentProfileInformation();
		} 

		public void validateValues()
		{
			if (this.studentSortColumn == null)
				this.studentSortColumn = FilterSortPageUtils.SCORE_STUDENT_DEFAULT_SORT_COLUMN;

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

			if (this.itemSortColumn == null)
				this.itemSortColumn = FilterSortPageUtils.TESTITEM_DEFAULT_SORT_COLUMN;

			if (this.itemSortOrderBy == null)
				this.itemSortOrderBy = FilterSortPageUtils.ASCENDING;

			if (this.itemPageRequested == null)
				this.itemPageRequested = new Integer(1);

			if (this.itemPageRequested.intValue() <= 0)
				this.itemPageRequested = new Integer(1);

			if (this.itemMaxPage == null)
				this.itemMaxPage = new Integer(1);
			if (this.itemPageRequested.intValue() > this.itemMaxPage.intValue()) {
				this.itemPageRequested = new Integer(this.itemMaxPage.intValue());                

			}	
		}

		public ItemScoringForm createClone()
		{
			ItemScoringForm copied = new ItemScoringForm();

			copied.setActionElement(this.actionElement);
			copied.setCurrentAction(this.currentAction);

			copied.setTestAdminId(this.testAdminId);
			copied.setItemId(this.itemId);
			copied.setItemSetId(this.itemSetId);
			copied.setTestAccessCode(this.testAccessCode);
			copied.setTestSessionName(this.testSessionName);

			copied.setItemSortColumn(this.itemSortColumn);
			copied.setItemSortOrderBy(this.itemSortOrderBy);
			copied.setItemPageRequested(this.itemPageRequested);      
			copied.setItemMaxPage(this.itemMaxPage);


			return copied;                    
		} 

		public void setActionElement(String actionElement)
		{
			this.actionElement = actionElement;
		}        
		public String getActionElement()
		{
			return this.actionElement != null ? this.actionElement : ACTION_DEFAULT;
		}
		public void setCurrentAction(String currentAction)
		{
			this.currentAction = currentAction;
		}
		public String getCurrentAction()
		{
			return this.currentAction != null ? this.currentAction : ACTION_DEFAULT;
		}
		// student profile
		public void setStudentProfile(StudentProfileInformation studentProfile)
		{
			this.studentProfile = studentProfile;
		}
		public StudentProfileInformation getStudentProfile()
		{
			if (this.studentProfile == null) this.studentProfile = new StudentProfileInformation();

			return this.studentProfile;
		}
		public Message getMessage()
		{
			return this.message != null ? this.message : new Message();
		}       
		public void setMessage(Message message)
		{
			this.message = message;
		}
		public void setMessage(String title, String content, String type)
		{
			this.message = new Message(title, content, type);
		}


		public void resetValuesForAction(String actionElement, String fromAction) 
		{

			if (actionElement.equals("{actionForm.itemSortOrderBy}")) {
				this.itemPageRequested = new Integer(1);
			}
			if (actionElement.equals("{actionForm.studentSortOrderBy}")) {
				this.studentPageRequested = new Integer(1);
			}
			if (actionElement.equals("ButtonGoInvoked_studentSearchResult") ||
					actionElement.equals("EnterKeyInvoked_studentSearchResult")) {
				this.selectedItemId = null;
			}
			if (actionElement.equals("ButtonGoInvoked_tablePathListAnchor") ||
					actionElement.equals("EnterKeyInvoked_tablePathListAnchor")) {

				if (fromAction.equals(ACTION_SCORE_BY_ITEM)){
					this.selectedItemId = null;
				}
			}
		}



		public String getItemSortColumn() {
			return itemSortColumn;
		}

		public void setItemSortColumn(String itemSortColumn) {
			this.itemSortColumn = itemSortColumn;
		}

		public String getItemSortOrderBy() {
			return itemSortOrderBy;
		}

		public void setItemSortOrderBy(String itemSortOrderBy) {
			this.itemSortOrderBy = itemSortOrderBy;
		}

		public void setItemPageRequested(Integer itemPageRequested) {
			this.itemPageRequested = itemPageRequested;
		}

		public Integer getItemMaxPage() {
			return itemMaxPage;
		}
		public void setItemMaxPage(Integer itemMaxPage) {
			this.itemMaxPage = itemMaxPage;
		}

		public Integer getSelectedStudentId() {
			return selectedStudentId;
		}

		public void setSelectedStudentId(Integer selectedStudentId) {
			this.selectedStudentId = selectedStudentId;
		}

		public String getStudentIdLabelName() {
			return studentIdLabelName;
		}

		public void setStudentIdLabelName(String studentIdLabelName) {
			this.studentIdLabelName = studentIdLabelName;
		}

		/**
		 * @return the testAccessCode
		 */
		public String getTestAccessCode() {
			return testAccessCode;
		}

		/**
		 * @param testAccessCode the testAccessCode to set
		 */
		public void setTestAccessCode(String testAccessCode) {
			this.testAccessCode = testAccessCode;
		}

		/**
		 * @return the testSessionName
		 */
		public String getTestSessionName() {
			return testSessionName;
		}

		/**
		 * @param testSessionName the testSessionName to set
		 */
		public void setTestSessionName(String testSessionName) {
			this.testSessionName = testSessionName;
		}

		/**
		 * @return the selectedItemId
		 */
		public Integer getSelectedItemId() {
			return selectedItemId;
		}

		/**
		 * @param selectedItemId the selectedItemId to set
		 */
		public void setSelectedItemId(Integer selectedItemId) {
			this.selectedItemId = selectedItemId;
		}

		/**
		 * @return the selectedTestAdminId
		 */
		public Integer getSelectedTestAdminId() {
			return selectedTestAdminId;
		}

		/**
		 * @param selectedTestAdminId the selectedTestAdminId to set
		 */
		public void setSelectedTestAdminId(Integer selectedTestAdminId) {
			this.selectedTestAdminId = selectedTestAdminId;
		}


		/**
		 * @return the itemId
		 */
		public String getItemId() {
			return itemId;
		}

		/**
		 * @param itemId the itemId to set
		 */
		public void setItemId(String itemId) {
			this.itemId = itemId;
		}

		/**
		 * @return the itemSetId
		 */
		public Integer getItemSetId() {
			return itemSetId;
		}

		/**
		 * @param itemSetId the itemSetId to set
		 */
		public void setItemSetId(Integer itemSetId) {
			this.itemSetId = itemSetId;
		}

		
		/**
		 * @return the testAdminId
		 */
		public Integer getTestAdminId() {
			return testAdminId;
		}

		/**
		 * @param testAdminId the testAdminId to set
		 */
		public void setTestAdminId(Integer testAdminId) {
			this.testAdminId = testAdminId;
		}

		/**
		 * @return the itemPageRequested
		 */
		public Integer getItemPageRequested() {
			return itemPageRequested;
		}


		/**
		 * @return the studentSortColumn
		 */
		public String getStudentSortColumn() {
			return studentSortColumn;
		}

		/**
		 * @param studentSortColumn the studentSortColumn to set
		 */
		public void setStudentSortColumn(String studentSortColumn) {
			this.studentSortColumn = studentSortColumn;
		}

		/**
		 * @return the studentSortOrderBy
		 */
		public String getStudentSortOrderBy() {
			return studentSortOrderBy;
		}

		/**
		 * @param studentSortOrderBy the studentSortOrderBy to set
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
		 * @param studentPageRequested the studentPageRequested to set
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
		 * @param studentMaxPage the studentMaxPage to set
		 */
		public void setStudentMaxPage(Integer studentMaxPage) {
			this.studentMaxPage = studentMaxPage;
		}

		/**
		 * @return the selectedItemNo
		 */
		public Integer getSelectedItemNo() {
			return selectedItemNo;
		}

		/**
		 * @param selectedItemNo the selectedItemNo to set
		 */
		public void setSelectedItemNo(Integer selectedItemNo) {
			this.selectedItemNo = selectedItemNo;
		}

		/**
		 * @return the selecteditemSetName
		 */
		public String getSelectedItemSetName() {
			return selectedItemSetName;
		}

		/**
		 * @param selecteditemSetName the selecteditemSetName to set
		 */
		public void setSelectedItemSetName(String selectedItemSetName) {
			this.selectedItemSetName = selectedItemSetName;
		}

		/**
		 * @return the selectedmaxPoints
		 */
		public Integer getSelectedMaxPoints() {
			return selectedMaxPoints;
		}

		/**
		 * @param selectedmaxPoints the selectedmaxPoints to set
		 */
		public void setSelectedMaxPoints(Integer selectedMaxPoints) {
			this.selectedMaxPoints = selectedMaxPoints;
		}
		/**
		 * @return the selectedmaxPoints
		 */
		public String getSelectedItemType() {
			return selectedItemType;
		}

		/**
		 * @param selectedmaxPoints the selectedmaxPoints to set
		 */
		public void setSelectedItemType(String selectedItemType) {
			this.selectedItemType = selectedItemType;
		}


	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}


	public CustomerConfigurationValue[] getCustomerConfigurationsValue() {
		return customerConfigurationsValue;
	}

	public void setCustomerConfigurationsValue(
			CustomerConfigurationValue[] customerConfigurationsValue) {
		this.customerConfigurationsValue = customerConfigurationsValue;
	}

	public void setCustomerConfigurations(
			CustomerConfiguration[] customerConfigurations) {
		this.customerConfigurations = customerConfigurations;
	}

	public String getPageMessage() {
		return pageMessage;
	}

	public void setPageMessage(String pageMessage) {
		this.pageMessage = pageMessage;
	}
	/**
	 * @return the testAdminId
	 */
	public Integer getTestAdminId() {
		return testAdminId;
	}

	/**
	 * @param testAdminId the testAdminId to set
	 */
	public void setTestAdminId(Integer testAdminId) {
		this.testAdminId = testAdminId;
	}
	/**
	 * @return the itemSetId
	 */
	public Integer getItemSetId() {
		return itemSetId;
	}

	/**
	 * @param itemSetId the itemSetId to set
	 */
	public void setItemSetId(Integer itemSetId) {
		this.itemSetId = itemSetId;
	}

	/**
	 * @return the testAccessCode
	 */
	public String getTestAccessCode() {
		return testAccessCode;
	}

	/**
	 * @param testAccessCode the testAccessCode to set
	 */
	public void setTestAccessCode(String testAccessCode) {
		this.testAccessCode = testAccessCode;
	}

	/**
	 * @return the studentIdLabelName
	 */
	public String getStudentIdLabelName() {
		return studentIdLabelName;
	}

	/**
	 * @param studentIdLabelName the studentIdLabelName to set
	 */
	public void setStudentIdLabelName(String studentIdLabelName) {
		this.studentIdLabelName = studentIdLabelName;
	}

	/**
	 * @return the savedForm
	 */
	public ItemScoringForm getSavedForm() {
		return savedForm;
	}

	/**
	 * @param savedForm the savedForm to set
	 */
	public void setSavedForm(ItemScoringForm savedForm) {
		this.savedForm = savedForm;
	}

	/**
	 * @return the studentIdConfigurable
	 */
	public boolean isStudentIdConfigurable() {
		return studentIdConfigurable;
	}

	/**
	 * @param studentIdConfigurable the studentIdConfigurable to set
	 */
	public void setStudentIdConfigurable(boolean studentIdConfigurable) {
		this.studentIdConfigurable = studentIdConfigurable;
	}
}