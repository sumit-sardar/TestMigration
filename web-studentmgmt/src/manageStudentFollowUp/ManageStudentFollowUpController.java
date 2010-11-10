package manageStudentFollowUp;

import java.util.ArrayList;
//Changes for CA-ABE student intake
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;



import manageStudent.ManageStudentController.ManageStudentForm;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import utils.DateUtils;
import utils.FilterSortPageUtils;
import utils.MessageResourceBundle;
import utils.OrgNodeUtils;
import utils.PermissionsUtils;
import utils.StudentPathListUtils;
import utils.StudentSearchUtils;
import utils.WebUtils;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.studentManagement.CustomerConfiguration;
import com.ctb.bean.studentManagement.CustomerConfigurationValue;
import com.ctb.bean.studentManagement.ManageStudent;
import com.ctb.bean.studentManagement.ManageStudentData;
import com.ctb.bean.studentManagement.OrganizationNode;
import com.ctb.bean.studentManagement.OrganizationNodeData;
import com.ctb.bean.studentManagement.StudentDemographic;
import com.ctb.bean.studentManagement.StudentDemographicValue;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.StudentAccommodations;
import com.ctb.bean.testAdmin.USState;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.studentManagement.StudentDataCreationException;
import com.ctb.exception.studentManagement.StudentDataDeletionException;
import com.ctb.util.studentManagement.DeleteStudentStatus;
import com.ctb.util.web.sanitizer.JavaScriptSanitizer;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.ctb.widgets.bean.PagerSummary;

import dto.Message;
import dto.PathNode;
import dto.StudentAccommodationsDetail;
import dto.StudentProfileInformation;
import manageStudent.ManageStudentController;

/*
 * Manage Student Follow Up Controller Main Class
 */
@Jpf.Controller()
public class ManageStudentFollowUpController extends PageFlowController {
	
	
	

	/**
	 * @common:control
	 */

	@Control()
	private com.ctb.control.studentManagement.StudentManagement studentManagement;


	private static final String ACTION_DEFAULT           = "defaultAction";
	private static final String ACTION_FIND_STUDENT      = "findStudent";
	private static final String ACTION_VIEW_STUDENT      = "viewStudent";
	private String userName = null;
	public String[] gradeOptions = null;
	public String[] genderOptions = null;
	public String[] monthOptions = null;
	public String[] dayOptions = null;
	public String[] yearOptions = null;
	public String pageTitle = null;
	public String pageMessage = null;
	public String studentName = null; 
	private ManageStudentFollowUpForm savedForm = null;
	//Changes for CA-ABE student intake
	public LinkedHashMap stateOptions = null;
	
	private Integer customerId = null;
	private User user = null;
	
	
	
	/**
	 * This method represents the point of entry into the pageflow
	 * @jpf:action
	 * @jpf:forward name="success" path="beginFindStudent.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "followUpStudent.do")
	})
	protected Forward begin()
	{		
		
		return new Forward("success");
	}
	
	//Changes for CA_ABE Follow-Up
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "follow_up_student.jsp"),
					@Jpf.Forward(name = "viewFollowUp",
							path = "viewFollowUpStudent.do")
					
	})
	protected Forward followUpStudent(ManageStudentController.ManageStudentForm form)
	{   
		
		ManageStudentFollowUpForm followUpform = initialize(ACTION_FIND_STUDENT);
		
		followUpform.byStudentProfileVisible = true;
		
		followUpform.setStudentProfile(form.getStudentProfile());
		
		String followUpStatus= form.getStudentProfile().getStudentFollowUpStatus();
		
		if (followUpStatus.equalsIgnoreCase("In Complete")) {
			return new Forward("success",followUpform);
		} else {
			return new Forward("viewFollowUp",followUpform);
		}
		
		
	}
	
	//Changes for CA_ABE Follow-Up
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "viewFollowUpStudent.do")
	})
	protected Forward saveFollowUpData(ManageStudentFollowUpForm form)
	{
		
		form.byStudentProfileVisible = true;
		return new Forward("success",form);
	}
	

	//Changes for CA_ABE Follow-Up
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "view_follow_up_student.jsp")
	})
	protected Forward viewFollowUpStudent(ManageStudentFollowUpForm form)
	{  
		form.clearSectionVisibility();
		form.setByStudentProfileVisible(Boolean.TRUE);
		return new Forward("success",form);
	}
	
	
	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="findStudent.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "/manageStudent/beginFindStudent.do")
	})
	protected Forward returnToFindStudent(ManageStudentFollowUpForm form)
	{   
		
		return new Forward("success");
	}
	

	
	
	/**
	 * initialize
	 */
	private ManageStudentFollowUpForm initialize(String action)
	{        
		getUserDetails();

		this.monthOptions = DateUtils.getMonthOptions();
		this.dayOptions = DateUtils.getDayOptions();
		this.yearOptions = DateUtils.getYearOptions();
		initStateOptions(ACTION_FIND_STUDENT);		
		this.savedForm = new ManageStudentFollowUpForm();
		this.savedForm.init( action );

		return this.savedForm;
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

		//getCustomerConfigurations();             
	}
	
	 /**
     * initStateOptions
     */
     //Changes for CA-ABE student intake
    private void initStateOptions(String action)
    {        
        this.stateOptions = new LinkedHashMap();
        
        TreeMap territoriesOptions = new TreeMap();
         

       this.stateOptions.put("", Message.SELECT_STATE);
            
       
        try {
            USState[] state = this.studentManagement.getStates();
            if (state != null) {
                for (int i = 0 ; i < state.length ; i++) {
                    if (!isContains (state[i].getStatePrDesc())) {
                        
                        this.stateOptions.put(state[i].getStatePr(), 
                                            state[i].getStatePrDesc());
                    } else {
                        
                        territoriesOptions.put(state[i].getStatePrDesc(),state[i]);
                        
                    }
                    
                }
            }
            orderedStateTerritories(territoriesOptions); 
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
 private boolean isContains (String stateDesc) {
        
        String []USterritories = {"Virgin Islands","Puerto Rico","Palau","North Mariana Islands","Marshall Islands","Guam","F.S. of Micronesia","American Samoa"};
        
        for (int i = 0; i < USterritories.length; i++) {
            
            if (USterritories[i].equals(stateDesc)) {
                return true;
            } 
        }
        return false;
        
    } 
    
    private void orderedStateTerritories (TreeMap territoriesOptions) {
        
        Collection territories = territoriesOptions.values();
        
        Iterator iterate = territories.iterator();
        while (iterate.hasNext()) {
            USState state = (USState) iterate.next();
            this.stateOptions.put(state.getStatePr(), 
                                            state.getStatePrDesc());
        }
    }


	
	
	
	
	
	
/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** MANAGESTUDENTFOLLOWUPFORM ************* /////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////    
	/**
	 * FormData get and set methods may be overwritten by the Form Bean editor.
	 */
	public static class ManageStudentFollowUpForm extends SanitizedFormData
	{
		private String actionElement;
		private String currentAction;
		private String selectedTab;

		private Boolean byStudentProfileVisible;
		private Boolean byStudentDemographicVisible;
		private Boolean byStudentAccommodationVisible;

		private String selectedOrgLevel;

		private Integer selectedStudentId;

		// student profile
		private StudentProfileInformation studentProfile;

		// find all students
		private String orgNodeName;
		private Integer orgNodeId;
		private Integer selectedOrgNodeId;
		private String selectedOrgNodeName;

		// org pager
		private String orgSortColumn;
		private String orgSortOrderBy;
		private Integer orgPageRequested;
		private Integer orgMaxPage;

		// student pager
		private String studentSortColumn;
		private String studentSortOrderBy;
		private Integer studentPageRequested;
		private Integer studentMaxPage;

		private Message message;
		private  boolean disableMandatoryBirthdate = false;  //GACRCT2010CR007 - Disable Mandatory Birth Date 
		private boolean isMandatoryStudentId = false;//GA2011CR001- GTID mandatory field
		private String studentIdLabelName = "Student ID";
		private String studentId2LabelName = "Student ID 2";
		private boolean isABECustomer = false;
		/**
		 * @return the isABECustomer
		 */
		public boolean isABECustomer() {
			return isABECustomer;
		}

		/**
		 * @param isABECustomer the isABECustomer to set
		 */
		public void setABECustomer(boolean isABECustomer) {
			this.isABECustomer = isABECustomer;
		}

		public ManageStudentFollowUpForm()
		{
		}

		public void init(String action)
		{
			

			clearSearch();
			clearSectionVisibility();

			this.selectedStudentId = null;

			this.orgNodeName = "Top";
			this.orgNodeId = new Integer(0);
			this.selectedOrgNodeId = null;
			this.selectedOrgNodeName = null;

			this.orgSortColumn = FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN;
			this.orgSortOrderBy = FilterSortPageUtils.ASCENDING;      
			this.orgPageRequested = new Integer(1);                
			this.orgMaxPage = new Integer(1);      

			this.studentSortColumn = FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN;
			this.studentSortOrderBy = FilterSortPageUtils.ASCENDING;      
			this.studentPageRequested = new Integer(1);       
			this.studentMaxPage = new Integer(1);      

			this.studentProfile = new StudentProfileInformation();
		}   

		public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
		{
			ActionErrors errs = super.validate(mapping, request);

			if (!errs.isEmpty()) {
				request.setAttribute("hasAlert", Boolean.TRUE);
			}
			return errs;
		}

		public void validateValues()
		{
			if (this.orgSortColumn == null)
				this.orgSortColumn = FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN;

			if (this.orgSortOrderBy == null)
				this.orgSortOrderBy = FilterSortPageUtils.ASCENDING;

			if (this.orgPageRequested == null)
				this.orgPageRequested = new Integer(1);

			if (this.orgPageRequested.intValue() <= 0) {
				this.orgPageRequested = new Integer(1);
				this.selectedOrgNodeId = null;
			}

			if (this.orgMaxPage == null)
				this.orgMaxPage = new Integer(1);

			if (this.orgPageRequested.intValue() > this.orgMaxPage.intValue()) {
				this.orgPageRequested = new Integer(this.orgMaxPage.intValue());
				this.selectedOrgNodeId = null;
			}

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
				this.selectedStudentId = null;
			}
		}     


		public void resetValuesForAction(String actionElement, String fromAction) 
		{
			
		}


		public void resetValuesForPathList()
		{
			this.orgSortColumn = FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN;
			this.orgSortOrderBy = FilterSortPageUtils.ASCENDING;      
			this.orgPageRequested = new Integer(1);    
			this.orgMaxPage = new Integer(1);      

			this.studentSortColumn = FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN;
			this.studentSortOrderBy = FilterSortPageUtils.ASCENDING;      
			this.studentPageRequested = new Integer(1);    
			this.studentMaxPage = new Integer(1);      
		}     


		public void clearSearch()
		{   
			this.studentProfile = new StudentProfileInformation();
			this.studentProfile.setGrade(FilterSortPageUtils.FILTERTYPE_ANY_GRADE);
			this.studentProfile.setGender(FilterSortPageUtils.FILTERTYPE_ANY_GENDER);                        
		}


		public ManageStudentFollowUpForm createClone()
		{
			ManageStudentFollowUpForm copied = new ManageStudentFollowUpForm();

			copied.setActionElement(this.actionElement);
			copied.setCurrentAction(this.currentAction);
			

			copied.setByStudentProfileVisible(this.byStudentProfileVisible);
			copied.setByStudentDemographicVisible(this.byStudentDemographicVisible);
			copied.setByStudentAccommodationVisible(this.byStudentAccommodationVisible);

			copied.setSelectedOrgLevel(this.selectedOrgLevel);            
			copied.setSelectedStudentId(this.selectedStudentId);

			copied.setOrgNodeName(this.orgNodeName);
			copied.setOrgNodeId(this.orgNodeId);
			copied.setSelectedOrgNodeId(this.selectedOrgNodeId);
			copied.setSelectedOrgNodeName(this.selectedOrgNodeName);

			copied.setOrgSortColumn(this.orgSortColumn);
			copied.setOrgSortOrderBy(this.orgSortOrderBy);
			copied.setOrgPageRequested(this.orgPageRequested);
			copied.setOrgMaxPage(this.orgMaxPage);

			copied.setStudentSortColumn(this.studentSortColumn);
			copied.setStudentSortOrderBy(this.studentSortOrderBy);
			copied.setStudentPageRequested(this.studentPageRequested);      
			copied.setStudentMaxPage(this.studentMaxPage);

			copied.setStudentProfile(this.studentProfile);
			copied.setABECustomer(this.isABECustomer);	//added for CA-ABE
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
		
		public void setSelectedStudentId(Integer selectedStudentId)
		{
			this.selectedStudentId = selectedStudentId;
		}
		public Integer getSelectedStudentId()
		{
			return this.selectedStudentId;
		}
		public void setOrgNodeName(String orgNodeName)
		{
			this.orgNodeName = orgNodeName;
		}
		public String getOrgNodeName()
		{
			return this.orgNodeName;
		}
		public void setOrgNodeId(Integer orgNodeId)
		{
			this.orgNodeId = orgNodeId;
		}
		public Integer getOrgNodeId()
		{
			return this.orgNodeId;
		}
		public void setSelectedOrgNodeId(Integer selectedOrgNodeId)
		{
			this.selectedOrgNodeId = selectedOrgNodeId;
		}
		public Integer getSelectedOrgNodeId()
		{
			return this.selectedOrgNodeId;
		}
		public void setSelectedOrgNodeName(String selectedOrgNodeName)
		{
			this.selectedOrgNodeName = selectedOrgNodeName;
		}
		public String getSelectedOrgNodeName()
		{
			return this.selectedOrgNodeName;
		}        


		public void setOrgSortColumn(String orgSortColumn)
		{
			this.orgSortColumn = orgSortColumn;
		}
		public String getOrgSortColumn()
		{
			return this.orgSortColumn != null ? this.orgSortColumn : FilterSortPageUtils.ORGNODE_DEFAULT_SORT_COLUMN;
		}       
		public void setOrgSortOrderBy(String orgSortOrderBy)
		{
			this.orgSortOrderBy = orgSortOrderBy;
		}
		public String getOrgSortOrderBy()
		{
			return this.orgSortOrderBy != null ? this.orgSortOrderBy : FilterSortPageUtils.ASCENDING;
		}       
		public void setOrgPageRequested(Integer orgPageRequested)
		{
			this.orgPageRequested = orgPageRequested;
		}
		public Integer getOrgPageRequested()
		{
			return this.orgPageRequested != null ? this.orgPageRequested : new Integer(1);
		}        
		public void setOrgMaxPage(Integer orgMaxPage)
		{
			this.orgMaxPage = orgMaxPage;
		}
		public Integer getOrgMaxPage()
		{
			return this.orgMaxPage != null ? this.orgMaxPage : new Integer(1);
		}        


		public void setStudentSortColumn(String studentSortColumn)
		{
			this.studentSortColumn = studentSortColumn;
		}
		public String getStudentSortColumn()
		{
			return this.studentSortColumn != null ? this.studentSortColumn : FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN;
		}       
		public void setStudentSortOrderBy(String studentSortOrderBy)
		{
			this.studentSortOrderBy = studentSortOrderBy;
		}
		public String getStudentSortOrderBy()
		{
			return this.studentSortOrderBy != null ? this.studentSortOrderBy : FilterSortPageUtils.ASCENDING;
		}       
		public void setStudentPageRequested(Integer studentPageRequested)
		{
			this.studentPageRequested = studentPageRequested;
		}
		public Integer getStudentPageRequested()
		{
			return this.studentPageRequested != null ? this.studentPageRequested : new Integer(1);
		}        
		public void setStudentMaxPage(Integer studentMaxPage)
		{
			this.studentMaxPage = studentMaxPage;
		}
		public Integer getStudentMaxPage()
		{
			return this.studentMaxPage != null ? this.studentMaxPage : new Integer(1);
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

		public String getStringAction()
		{
			String action = "addStudent";
			if ((this.selectedStudentId != null) && (this.selectedStudentId.intValue() > 0))
				action = "editStudent";
			return action; 
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



		public void setByStudentDemographicVisible(Boolean byStudentDemographicVisible)
		{
			this.byStudentDemographicVisible = byStudentDemographicVisible;
		}
		public Boolean getByStudentDemographicVisible()
		{
			return this.byStudentDemographicVisible;
		}        
		public void setByStudentAccommodationVisible(Boolean byStudentAccommodationVisible)
		{
			this.byStudentAccommodationVisible = byStudentAccommodationVisible;
		}
		public Boolean getByStudentAccommodationVisible()
		{
			return this.byStudentAccommodationVisible;
		}        
		public void setByStudentProfileVisible(Boolean byStudentProfileVisible)
		{
			this.byStudentProfileVisible = byStudentProfileVisible;
		}
		public Boolean getByStudentProfileVisible()
		{
			return this.byStudentProfileVisible;
		}        

		public void setSelectedOrgLevel(String selectedOrgLevel)
		{
			this.selectedOrgLevel = selectedOrgLevel;
		}
		public String getSelectedOrgLevel()
		{
			return this.selectedOrgLevel;
		}       

		public void clearSectionVisibility()
		{
			this.byStudentProfileVisible = Boolean.FALSE;
			this.byStudentDemographicVisible = Boolean.FALSE;
			this.byStudentAccommodationVisible = Boolean.FALSE;
		}

		/**
		 * @return the disableMandatoryBirthdate
		 */
		public boolean isDisableMandatoryBirthdate() {
			return disableMandatoryBirthdate;
		}

		/**
		 * @param disableMandatoryBirthdate the disableMandatoryBirthdate to set
		 */
		public void setDisableMandatoryBirthdate(boolean disableMandatoryBirthdate) {
			this.disableMandatoryBirthdate = disableMandatoryBirthdate;
		}  
		
		public boolean verifyStudentInformation(List selectedOrgNodes)
		{
			// check for required fields
			String requiredFields = "";
			int requiredFieldCount = 0;

			String firstName = this.studentProfile.getFirstName().trim();
			if ( firstName.length() == 0 ) {
				requiredFieldCount += 1;            
				requiredFields = Message.buildErrorString("First Name", requiredFieldCount, requiredFields);       
			}

			String lastName = this.studentProfile.getLastName().trim();
			if ( lastName.length() == 0 ) {
				requiredFieldCount += 1;            
				requiredFields = Message.buildErrorString("Last Name", requiredFieldCount, requiredFields);       
			}
			
			String month = this.studentProfile.getMonth();
			String day = this.studentProfile.getDay();
			String year = this.studentProfile.getYear();
			//GACRCT2010CR007 - validate required date of birth  according to customer configuartion 
			if(!isDisableMandatoryBirthdate()) {
				if (! DateUtils.allSelected(month, day, year)) {
					requiredFieldCount += 1;            
					requiredFields = Message.buildErrorString("Date of Birth", requiredFieldCount, requiredFields);       
				}
			}
			String studentGrade = this.studentProfile.getGrade();
			if ( studentGrade.equals(FilterSortPageUtils.FILTERTYPE_SELECT_A_GRADE)) {
				requiredFieldCount += 1;            
				requiredFields = Message.buildErrorString("Grade", requiredFieldCount, requiredFields);       
			}

			String studentGender = this.studentProfile.getGender();
			if ( studentGender.equals(FilterSortPageUtils.FILTERTYPE_SELECT_A_GENDER) ) {
				requiredFieldCount += 1;            
				requiredFields = Message.buildErrorString("Gender", requiredFieldCount, requiredFields);       
			}
			
			//CR - GA2011CR001 - validation For GTID
			if(isMandatoryStudentId){
				String externalStudentNumber = this.studentProfile.getStudentNumber().trim();
				if ( externalStudentNumber.length()==0) {
					requiredFieldCount += 1;     
					requiredFields = Message.buildErrorString(this.studentIdLabelName, requiredFieldCount, requiredFields);   
				}
			}

			if ( selectedOrgNodes.size() == 0 ) {
				requiredFieldCount += 1;      
				requiredFields = Message.buildErrorString("Organization Assignment", requiredFieldCount, requiredFields);       
			}        
			
			
			if (requiredFieldCount > 0) {
				if (requiredFieldCount == 1) {
					requiredFields += ("<br/>" + Message.REQUIRED_TEXT);
					setMessage("Missing required field", requiredFields, Message.ERROR);
				}
				else {
					requiredFields += ("<br/>" + Message.REQUIRED_TEXT_MULTIPLE);
					setMessage("Missing required fields", requiredFields, Message.ERROR);
				}
				return false;
			}

			String middleName = this.studentProfile.getMiddleName().trim();
			String studentNumber = this.studentProfile.getStudentNumber().trim();
			String studentSecondNumber = this.studentProfile.getStudentSecondNumber().trim();

			String invalidCharFields = WebUtils.verifyCreateStudentName(firstName, lastName, middleName);                
			if (invalidCharFields.length() > 0) {
				invalidCharFields += ("<br/>" + Message.INVALID_NAME_CHARS);
				setMessage(MessageResourceBundle.getMessage("invalid_char_message"), invalidCharFields, Message.ERROR);
				return false;
			}

			invalidCharFields = WebUtils.verifyCreateStudentNumber(studentNumber, studentSecondNumber, this.studentIdLabelName, this.studentId2LabelName );                
			if (invalidCharFields.length() > 0) {
				invalidCharFields += ("<br/>" + Message.INVALID_NUMBER_CHARS);
				setMessage(MessageResourceBundle.getMessage("invalid_char_message"), invalidCharFields, Message.ERROR);
				return false;
			}
			//GACRCT2010CR007 - validate  date of birth  when date value is provided.
			
			if(isDisableMandatoryBirthdate() && !DateUtils.allSelected(month, day, year)) {
				if (!DateUtils.noneSelected(month, day, year)) {
					invalidCharFields += Message.INVALID_DATE;
					setMessage(MessageResourceBundle.getMessage("invalid_birthdate"), invalidCharFields, Message.ERROR);
					return false;
					      
				}
			}
						
			if (DateUtils.allSelected(month, day, year)) {
				int isDateValid = DateUtils.validateDateValues(year, month, day);
				if (isDateValid != DateUtils.DATE_VALID) {
					invalidCharFields += Message.INVALID_DATE;
					setMessage(MessageResourceBundle.getMessage("invalid_birthdate"), invalidCharFields, Message.ERROR);
					return false;
				}
			}
			return true;
		}
		
		
		// start Change For CR - GA2011CR001
		/**
		 * @return the isMandatoryStudentId
		 */
		public boolean isMandatoryStudentId() {
			return isMandatoryStudentId;
		}

		/**
		 * @param isMandatoryStudentId the isMandatoryStudentId to set
		 */
		public void setMandatoryStudentId(boolean isMandatoryStudentId) {
			this.isMandatoryStudentId = isMandatoryStudentId;
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
		 * @return the studentId2LabelName
		 */
		public String getStudentId2LabelName() {
			return studentId2LabelName;
		}

		/**
		 * @param studentId2LabelName the studentId2LabelName to set
		 */
		public void setStudentId2LabelName(String studentId2LabelName) {
			this.studentId2LabelName = studentId2LabelName;
		}
		// End  Change For CR - GA2011CR001
		
	}








	public String[] getGradeOptions() {
		return gradeOptions;
	}

	public void setGradeOptions(String[] gradeOptions) {
		this.gradeOptions = gradeOptions;
	}

	public String[] getGenderOptions() {
		return genderOptions;
	}

	public void setGenderOptions(String[] genderOptions) {
		this.genderOptions = genderOptions;
	}

	public String[] getMonthOptions() {
		return monthOptions;
	}

	public void setMonthOptions(String[] monthOptions) {
		this.monthOptions = monthOptions;
	}

	public String[] getDayOptions() {
		return dayOptions;
	}

	public void setDayOptions(String[] dayOptions) {
		this.dayOptions = dayOptions;
	}

	public String[] getYearOptions() {
		return yearOptions;
	}

	public void setYearOptions(String[] yearOptions) {
		this.yearOptions = yearOptions;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public LinkedHashMap getStateOptions() {
		return stateOptions;
	}

	public void setStateOptions(LinkedHashMap stateOptions) {
		this.stateOptions = stateOptions;
	}
	
}
