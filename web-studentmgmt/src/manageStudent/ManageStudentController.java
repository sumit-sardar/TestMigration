
package manageStudent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

/**
 * @jpf:controller
**/

/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** ManageStudentController ************* ///////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////

@Jpf.Controller()
public class ManageStudentController extends PageFlowController
{
	static final long serialVersionUID = 1L;

	/**
	 * @common:control
	 */

	@Control()
	private com.ctb.control.studentManagement.StudentManagement studentManagement;


	private static final String ACTION_DEFAULT           = "defaultAction";
	private static final String ACTION_FIND_STUDENT      = "findStudent";
	private static final String ACTION_VIEW_STUDENT      = "viewStudent";
	private static final String ACTION_EDIT_STUDENT      = "editStudent";
	private static final String ACTION_ADD_STUDENT       = "addStudent";
	private static final String ACTION_DELETE_STUDENT    = "deleteStudent";

	private static final String MODULE_HIERARCHY               = "moduleHierarchy";
	private static final String MODULE_STUDENT_PROFILE         = "moduleStudentProfile";
	private static final String MODULE_STUDENT_DEMOGRAPHIC     = "moduleStudentDemographic";
	private static final String MODULE_STUDENT_ACCOMMODATION   = "moduleStudentAccommodation";
	private static final String MODULE_NONE                    = "moduleNone";

	private static final String ACTION_APPLY_SEARCH   = "applySearch";
	private static final String ACTION_CLEAR_SEARCH   = "clearSearch";

	public String[] gradeOptions = null;
	public String[] genderOptions = null;
	public String[] monthOptions = null;
	public String[] dayOptions = null;
	public String[] yearOptions = null;

	private String userName = null;
	private Integer customerId = null;
	private User user = null;

	private String selectedModuleFind = null;
	private boolean searchApplied = false;
	private boolean viewStudentFromSearch = false;

	private List orgNodePath = null;
	private Integer selectedOrgNodeId = null;    
	private Integer copyOfOrgNodeId = null;    

	private ManageStudentForm savedForm = null;
	private StudentProfileInformation studentSearch = null;
	
	private HashMap currentOrgNodesInPathList = null;
	public List selectedOrgNodes = null;
	public Integer[] currentOrgNodeIds = null;

	// customer configuration
	CustomerConfiguration[] customerConfigurations = null;
	CustomerConfigurationValue[] customerConfigurationsValue = null;
	
	//GACRCT2010CR007- Disable_Mandatory_Birth_Date according to customer cofiguration
	private boolean disableMandatoryBirthdate = false;
	
	private boolean isMandatoryStudentId = false; // Change For CR - GA2011CR001
	private String studentIdLabelName = "Student ID";
	private String studentId2LabelName = "Student ID 2";

	// student demographics
	List demographics = null;


	// student accommodations
	public StudentAccommodationsDetail accommodations = null;

	// misc
	public String pageTitle = null;
	public String pageMessage = null;
	public String studentName = null;     

/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** begin controller ************* ///////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////


	/**
	 * This method represents the point of entry into the pageflow
	 * @jpf:action
	 * @jpf:forward name="success" path="beginFindStudent.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "beginFindStudent.do")
	})
	protected Forward begin()
	{
		return new Forward("success");
	}

	/**
	 * initialize
	 */
	private ManageStudentForm initialize(String action)
	{        
		getUserDetails();

		this.orgNodePath = new ArrayList();
		this.currentOrgNodesInPathList = new HashMap();
		this.currentOrgNodeIds = new Integer[0];
		this.selectedOrgNodes = new ArrayList();

		this.monthOptions = DateUtils.getMonthOptions();
		this.dayOptions = DateUtils.getDayOptions();
		this.yearOptions = DateUtils.getYearOptions();

		this.savedForm = new ManageStudentForm();
		this.savedForm.init( action );

		this.getSession().setAttribute("userHasReports", userHasReports());

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


/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** ADD - EDIT STUDENT ************* ////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This method represents the point of entry into the pageflow
	 * @jpf:action
	 * @jpf:forward name="success" path="beginEditStudent.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "beginEditStudent.do")
	})
	protected Forward editStudent()
	{                        
		ManageStudentForm form = initialize(ACTION_ADD_STUDENT);     
		String studentId = (String)getRequest().getParameter("studentId");
		form.setSelectedStudentId(new Integer(studentId)); 

		return new Forward("success", form);
	}

	/**
	 * This method represents the point of entry into the pageflow
	 * @jpf:action
	 * @jpf:forward name="success" path="addEditStudent.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "addEditStudent.do")
	})
	protected Forward beginAddStudent()
	{                
		ManageStudentForm form = initialize(ACTION_ADD_STUDENT);     
		form.setSelectedStudentId(null); 

		form.setCurrentAction(MODULE_STUDENT_PROFILE);
		form.clearSectionVisibility();
		form.setByStudentProfileVisible(Boolean.TRUE);

		initGradeGenderOptions(ACTION_ADD_STUDENT, form, null, null);

		form.getStudentProfile().setMonth(this.monthOptions[0]);
		form.getStudentProfile().setDay(this.dayOptions[0]);
		form.getStudentProfile().setYear(this.yearOptions[0]);

		this.accommodations = null;
		this.demographics = null;

		this.studentName = null;
		this.searchApplied = false;

		return new Forward("success", form);
	}

	/**
	 * This method represents the point of entry into the pageflow
	 * @jpf:action
	 * @jpf:forward name="success" path="addEditStudent.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "addEditStudent.do"),
			@Jpf.Forward(name = "error",
							path = "findStudent.do")
	})
	protected Forward beginEditStudent(ManageStudentForm form)
	{                
		Integer studentId = form.getSelectedStudentId();
		StudentProfileInformation studentProfile = StudentSearchUtils.getStudentProfileInformation(this.studentManagement, this.userName, studentId);
		if (studentProfile == null) {						//Changes for Defect 60478
            form.setCurrentAction(ACTION_DEFAULT);
           return new Forward("error", form);
       }
		
		form.setStudentProfile(studentProfile);
		this.studentName = studentProfile.getFirstName() + " " + studentProfile.getLastName();

		String selectedTab = form.getSelectedTab();
		selectedTab = JavaScriptSanitizer.sanitizeString(selectedTab);
		if ((selectedTab != null) && (selectedTab.length() > 0))
		{        
			form.setCurrentAction(MODULE_STUDENT_PROFILE);
			form.clearSectionVisibility();
			form.setByStudentProfileVisible(Boolean.TRUE);            
		}
		else
		{
			form.setCurrentAction(ACTION_DEFAULT);
		}

		this.currentOrgNodesInPathList = new HashMap();
		this.currentOrgNodeIds = new Integer[0];

		this.selectedOrgNodes = StudentPathListUtils.getOrgNodeAssignment(studentProfile);

		// initialize hierarchy control
		Integer orgNodeId = null;
		String orgNodeName = null;       

		PathNode node = (PathNode)this.selectedOrgNodes.get(0);     // get first node
		orgNodeId = node.getId();
		orgNodeName = node.getName();
		this.orgNodePath = initHierarchyControl(orgNodeId, form);

		// init demographics and accommodations
		this.accommodations = null;
		this.demographics = null;

		this.gradeOptions = getGradeOptions(ACTION_EDIT_STUDENT);
		this.genderOptions = getGenderOptions(ACTION_EDIT_STUDENT);
		getCustomerConfigurations();  
		return new Forward("success", form);
	}

	/**
	 * @jpf:action
	 * @jpf:forward name="addStudent" path="addStudent.do"
	 * @jpf:forward name="editStudent" path="editStudent.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "addStudent",
					path = "addStudent.do"), 
					@Jpf.Forward(name = "editStudent",
							path = "editStudent.do")
	})
	protected Forward addEditStudent(ManageStudentForm form)
	{      
		isGeorgiaCustomer(form); //Change For CR - GA2011CR001
		String stringAction = form.getStringAction();
		setFormInfoOnRequest(form);
		saveToken(this.getRequest());
		return new Forward(stringAction, form);
	}

	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="add_edit_student.jsp"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "add_edit_student.jsp")
	})
	protected Forward addStudent(ManageStudentForm form)
	{      
		Boolean profileEditable = Boolean.TRUE;

		handleAddEdit(form, profileEditable);
		//GACRCT2010CR007- retrieve value for Disable_Mandatory_Birth_Date 
		isMandatoryBirthDate();

		this.getRequest().setAttribute("isAddStudent", Boolean.TRUE);

		this.getRequest().setAttribute("profileEditable", profileEditable);

		this.pageTitle = buildPageTitle(ACTION_ADD_STUDENT, form);

		return new Forward("success");
	}

	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="add_edit_student.jsp"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "add_edit_student.jsp")
	})
	protected Forward editStudent(ManageStudentForm form)
	{        
		Boolean profileEditable = isProfileEditable(form.getStudentProfile().getCreateBy());

		handleAddEdit(form, profileEditable);
		
		//GACRCT2010CR007- retrieve value for Disable_Mandatory_Birth_Date 
		isMandatoryBirthDate();
		
		this.getRequest().setAttribute("isEditStudent", Boolean.TRUE);

		this.getRequest().setAttribute("profileEditable", profileEditable);

		this.pageTitle = buildPageTitle(ACTION_EDIT_STUDENT, form);

		return new Forward("success");
	}

	/**
	 * handleAddOrEdit
	 */
	private void handleAddEdit(ManageStudentForm form, Boolean profileEditable)
	{        
		if (! profileEditable.booleanValue())
		{
			// reload student profile since nothing in the request
			Integer studentId = form.getSelectedStudentId();
			StudentProfileInformation studentProfile = StudentSearchUtils.getStudentProfileInformation(this.studentManagement, this.userName, studentId);
			form.setStudentProfile(studentProfile);
			this.studentName = studentProfile.getFirstName() + " " + studentProfile.getLastName();
		}

		addEditStudentProfile(form, profileEditable);    

		addEditDemographics(form);    

		addEditAccommodations(form);    

		Boolean disableColorSelection = new Boolean(! this.accommodations.getColorFont().booleanValue()); 
		this.getRequest().setAttribute("disableColorSelection", disableColorSelection);       

		setCustomerSettings();

		if (this.demographics.size() == 0)
		{
			this.getRequest().setAttribute("demographicVisible", "F");       
		}

		this.getRequest().setAttribute("viewOnly", Boolean.FALSE);       

		form.setCurrentAction(ACTION_DEFAULT);
	}

	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="viewStudent.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "viewStudent.do")
	})
	protected Forward goToViewStudent()
	{   
		saveToken(this.getRequest());
		return new Forward("success", this.savedForm);
	}

	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="goToViewStudent.do" 
	 * @jpf:forward name="error" path="addEditStudent.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success", 
					path = "goToViewStudent.do"), 
					@Jpf.Forward(name = "error",
							path = "addEditStudent.do")
	})
	protected Forward saveAddEditStudent(ManageStudentForm form)
	{   
		Boolean isTokenValid = isTokenValid();
		
		
		Integer studentId = form.getSelectedStudentId();
		
		if ( studentId == null) {
			 
			//System.out.println( studentId );
			studentId = (Integer)this.getSession().getAttribute("selectStudentIdInView");
			form.setSelectedStudentId(studentId);
		}
		
		boolean isCreateNew = studentId == null ? true : false;
		
		if ( isTokenValid ) {
			
				if (! isCreateNew)
				{        
					Boolean profileEditable = isProfileEditable(form.getStudentProfile().getCreateBy());
					if (! profileEditable.booleanValue())
					{
						// reload student profile since nothing in the request
						StudentProfileInformation studentProfile = StudentSearchUtils.getStudentProfileInformation(this.studentManagement, this.userName, studentId);
						form.setStudentProfile(studentProfile);
					}                
				}
		
				this.selectedOrgNodes = StudentPathListUtils.buildSelectedOrgNodes(this.currentOrgNodesInPathList, this.currentOrgNodeIds, this.selectedOrgNodes);
				
				//GACRCT2010CR007- set value for disableMandatoryBirthdate in  form. 
				form.setDisableMandatoryBirthdate(disableMandatoryBirthdate);
				
				//CR - GA2011CR001 - set value for FTE Mandatory Field
				form.setMandatoryStudentId(isMandatoryStudentId);
				
				boolean result = form.verifyStudentInformation(this.selectedOrgNodes);
				if (! result)
				{           
					form.setActionElement(ACTION_DEFAULT);
					form.setCurrentAction(ACTION_DEFAULT);                 
					return new Forward("error", form);
				}        
				//START- Added for CR  ISTEP2011CR017
				 Boolean isMultiOrgAssociationValid = isMultiOrgAssociationValid();
				if(result && !isMultiOrgAssociationValid){
					if ( this.selectedOrgNodes.size() > 1 ) {
						
						if (isCreateNew)
							form.setMessage(Message.ADD_TITLE, Message.STUDENT_ASSIGNMENT_ERROR, Message.ERROR);
						else
							form.setMessage(Message.EDIT_TITLE, Message.STUDENT_ASSIGNMENT_ERROR, Message.ERROR);
						
						form.setActionElement(ACTION_DEFAULT);
						form.setCurrentAction(ACTION_DEFAULT);                 
						return new Forward("error", form);
					}  
				}	
				//END- Added for CR  ISTEP2011CR017
				studentId = saveStudentProfileInformation(isCreateNew, form, studentId, this.selectedOrgNodes);
				
		
				String demographicVisible = this.user.getCustomer().getDemographicVisible();
				if ((studentId != null) && demographicVisible.equalsIgnoreCase("T"))
				{
					result = saveStudentDemographic(isCreateNew, form, studentId);
				}
		
				if (studentId != null)
				{
					result = saveStudentAccommodations(isCreateNew, form, studentId);
				}
		
				form.setSelectedStudentId(studentId);
		
				this.viewStudentFromSearch = false;             
			
			if (isCreateNew)
			{
				if (studentId != null) 
					form.setMessage(Message.ADD_TITLE, Message.ADD_SUCCESSFUL, Message.INFORMATION);
				else 
					form.setMessage(Message.ADD_TITLE, Message.ADD_ERROR, Message.INFORMATION);
			}
			else
			{
				if (studentId != null) 
					form.setMessage(Message.EDIT_TITLE, Message.EDIT_SUCCESSFUL, Message.INFORMATION);
				else 
					form.setMessage(Message.EDIT_TITLE, Message.EDIT_ERROR, Message.INFORMATION);
			}
			
			this.savedForm = form.createClone();    
		}
		return new Forward("success");
	}

	/**
	 * initHierarchyControl
	 */
	private List initHierarchyControl(Integer orgNodeId, ManageStudentForm form)
	{
		List orgNodePath = new ArrayList();

		List nodeAncestors = new ArrayList();

		OrganizationNode[] orgNodes = StudentPathListUtils.getAncestorOrganizationNodesForOrgNode(this.userName, orgNodeId, this.studentManagement);

		for (int i=0; i < (orgNodes.length - 1); i++)
		{
			OrganizationNode orgNode = (OrganizationNode)orgNodes[i];
			Integer orgId = orgNode.getOrgNodeId();
			String orgName = orgNode.getOrgNodeName();                
			if (orgId.intValue() >= 2)
			{    // ignore Root
				PathNode node = new PathNode();
			node.setId(orgId);
			node.setName(orgName);
			nodeAncestors.add(node);                
			form.setOrgNodeId(orgId);
			form.setOrgNodeName(orgName);
			form.resetValuesForPathList();
			}
		}    

		orgNodePath = StudentPathListUtils.setupOrgNodePath(nodeAncestors);       

		return orgNodePath;
	}

	/**
	 * isProfileEditable
	 */
	private Boolean isProfileEditable(Integer createBy)
	{
		Boolean editable = Boolean.TRUE;

		String importStudentEditable = this.user.getCustomer().getImportStudentEditable();
		if ((importStudentEditable != null) && importStudentEditable.equals("F") && (createBy.intValue() == 1))
		{
			editable = Boolean.FALSE;            
		}

		return editable;
	}

	/**
	 * setCustomerSettings
	 */
	private void setCustomerSettings()
	{
		String demographicVisible = this.user.getCustomer().getDemographicVisible();
		this.getRequest().setAttribute("demographicVisible", demographicVisible);       

		String hideAccommodations = this.user.getCustomer().getHideAccommodations();
		this.getRequest().setAttribute("hideAccommodations", hideAccommodations);       

		String importStudentEditable = this.user.getCustomer().getImportStudentEditable();
		this.getRequest().setAttribute("importStudentEditable", importStudentEditable);       

		this.getRequest().setAttribute("customerConfigurations", this.customerConfigurations);       

	}


	/**
	 * saveStudentProfileInformation
	 */
	private Integer saveStudentProfileInformation(boolean isCreateNew, ManageStudentForm form, Integer studentId, List selectedOrgNodes)
	{
		StudentProfileInformation studentProfile = form.getStudentProfile();
		ManageStudent student = studentProfile.makeCopy(studentId, selectedOrgNodes);

		try
		{                    
			if (isCreateNew)
			{
				studentId = this.studentManagement.createNewStudent(this.userName, student);
			}
			else
			{
				this.studentManagement.updateStudent(this.userName, student);
			}
		}
		catch (StudentDataCreationException sde)
		{
			sde.printStackTrace();
			studentId = null;
		}        
		catch (CTBBusinessException be)
		{
			be.printStackTrace();
			studentId = null;
		}                    

		return studentId;
	}



	/**
	 * addEditStudentProfile
	 */
	private void addEditStudentProfile(ManageStudentForm form, Boolean profileEditable)
	{
		form.validateValues();

		String actionElement = form.getActionElement();
		String currentAction = form.getCurrentAction();

		form.resetValuesForAction(actionElement, ACTION_EDIT_STUDENT);        

		String orgNodeName = form.getOrgNodeName();
		Integer orgNodeId = form.getOrgNodeId();   
		boolean nodeChanged = StudentPathListUtils.adjustOrgNodePath(this.orgNodePath, orgNodeId, orgNodeName);

		if (nodeChanged)
		{
			form.resetValuesForPathList();
		}

		if (actionElement.equals("setupOrgNodePath"))
		{
			String tempOrgNodeId = form.getCurrentAction();  
			if (tempOrgNodeId != null)
			{
				try
				{
					orgNodeId = new Integer(tempOrgNodeId);
					this.orgNodePath = initHierarchyControl(orgNodeId, form);      
					orgNodeName = form.getOrgNodeName();
					orgNodeId = form.getOrgNodeId();   
				} 
				catch (Exception e)
				{
				}
			}
		}

		FilterParams filter = null;
		PageParams page = FilterSortPageUtils.buildPageParams(form.getOrgPageRequested(), FilterSortPageUtils.PAGESIZE_8);
		SortParams sort = FilterSortPageUtils.buildSortParams(form.getOrgSortColumn(), form.getOrgSortOrderBy(), null, null);

		OrganizationNodeData ond = StudentPathListUtils.getOrganizationNodes(this.userName, this.studentManagement, orgNodeId, filter, page, sort);

		//START - Added for CR017
		 Boolean isClassReassignable = isClassReassignable(profileEditable);
		 List orgNodes = StudentPathListUtils.buildOrgNodeList(ond, profileEditable, ACTION_ADD_STUDENT, isClassReassignable);
		//END - Added for CR017

		String orgCategoryName = StudentPathListUtils.getOrgCategoryName(orgNodes);

		PagerSummary orgPagerSummary = StudentPathListUtils.buildOrgNodePagerSummary(ond, form.getOrgPageRequested());        
		form.setOrgMaxPage(ond.getFilteredPages());

		if (actionElement.equals("actionElement")) //Changes for Find_Student_Hierarchy Display Message
		{
			PathNode node = StudentPathListUtils.findOrgNode(orgNodes, form.getSelectedOrgNodeId());
			if (node != null)
			{
				form.setSelectedOrgNodeId(node.getId());
				form.setSelectedOrgNodeName(node.getName());                
			}
		}

		// compute selected orgnodes from pathlist
		this.selectedOrgNodes = StudentPathListUtils.buildSelectedOrgNodes(this.currentOrgNodesInPathList, this.currentOrgNodeIds, this.selectedOrgNodes);

		this.currentOrgNodeIds = StudentPathListUtils.retrieveCurrentOrgNodeIds(this.selectedOrgNodes);
		this.currentOrgNodesInPathList = StudentPathListUtils.buildOrgNodeHashMap(orgNodes);        

		List selectableOrgNodes = StudentPathListUtils.buildSelectableOrgNodes(this.currentOrgNodesInPathList, this.selectedOrgNodes);

		List orgNodesForSelector = buildOrgNodesForSelector(this.selectedOrgNodes, selectableOrgNodes, form.getOrgSortOrderBy());
		
		this.getRequest().setAttribute("orgNodePath", this.orgNodePath);
		this.getRequest().setAttribute("orgNodes", orgNodes);        
		this.getRequest().setAttribute("orgPagerSummary", orgPagerSummary);
		this.getRequest().setAttribute("orgCategoryName", orgCategoryName);      

		this.getRequest().setAttribute("selectedOrgNodes", this.selectedOrgNodes);
		this.getRequest().setAttribute("orgNodesForSelector", orgNodesForSelector);
	}


	/**
	 * setFullPathNodeName
	 */
	private void setFullPathNodeName(List orgNodes)    
	{
		for (int i=0; i < orgNodes.size(); i++)
		{
			PathNode node = (PathNode)orgNodes.get(i);
			Integer orgNodeId = node.getId();
			String fullPathNodeName = StudentPathListUtils.getFullPathNodeName(this.userName, orgNodeId, this.studentManagement);
			node.setFullPathName(fullPathNodeName);            
		}    
	}


	/**
	 * buildOrgNodesForSelector
	 */
	private List buildOrgNodesForSelector(List selectedOrgNodes, List selectableOrgNodes, String sortOrderBy)    
	{
		List orgNodesForSelector = new ArrayList();

		for (int i=0; i < selectedOrgNodes.size(); i++)
		{
			PathNode node = (PathNode)selectedOrgNodes.get(i);
			node.setSelectable("false");
			orgNodesForSelector.add(node);
		}
		for (int i=0; i < selectableOrgNodes.size(); i++)
		{
			PathNode node = (PathNode)selectableOrgNodes.get(i);
			node.setSelectable("true");
			orgNodesForSelector.add(node);
		}

		setFullPathNodeName(orgNodesForSelector);    

		OrgNodeUtils.sortList(orgNodesForSelector, sortOrderBy); 

		return orgNodesForSelector; 
	}


/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** DEMOGRAPHICS ************* //////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * addEditDemographics
	 */
	private void addEditDemographics(ManageStudentForm form)
	{
		Integer studentId = form.getStudentProfile().getStudentId();
		boolean studentImported = (form.getStudentProfile().getCreateBy().intValue() == 1);

		if ((this.demographics == null) && (studentId != null))
		{
			this.demographics = getStudentDemographics(studentId);
			prepareOnNullRule();            
		}
		else
		{
			if (studentImported)
			{        
				prepareStudentDemographicForCustomerConfiguration();
			}
			getStudentDemographicsFromRequest();
		}

		this.getRequest().setAttribute("demographics", this.demographics);       
		this.getRequest().setAttribute("studentImported", new Boolean(studentImported));       
	}


	/**
	 * saveStudentDemographic
	 */
	private boolean saveStudentDemographic(boolean isCreateNew, ManageStudentForm form, Integer studentId)
	{
		boolean studentImported = (form.getStudentProfile().getCreateBy().intValue() == 1);                
		if (studentImported)
		{        
			prepareStudentDemographicForCustomerConfiguration();
		}        
		getStudentDemographicsFromRequest();        

		if (isCreateNew)
		{
			createStudentDemographics(studentId);
		}
		else
		{
			updateStudentDemographics(studentId);
		}
		this.demographics = null;

		return true;
	}

	/**
	 * createStudentDemographics
	 */
	private void createStudentDemographics(Integer studentId)
	{
		if ((studentId != null) && (studentId.intValue() > 0) && (this.demographics != null))
		{
			try
			{    
				StudentDemographic[] studentDemoList = (StudentDemographic[])this.demographics.toArray( new StudentDemographic[0] );
				this.studentManagement.createStudentDemographics(this.userName, studentId, studentDemoList);
			}
			catch (CTBBusinessException be)
			{
				be.printStackTrace();
			} 
		}
	}

	/**
	 * updateStudentDemographics
	 */
	private void updateStudentDemographics(Integer studentId)
	{
		if ((studentId != null) && (studentId.intValue() > 0) && (this.demographics != null))
		{
			try
			{    
				StudentDemographic[] studentDemoList = (StudentDemographic[])this.demographics.toArray( new StudentDemographic[0] );
				this.studentManagement.updateStudentDemographics(this.userName, studentId, studentDemoList);
			}
			catch (CTBBusinessException be)
			{
				be.printStackTrace();
			}    
		}
	}

	/**
	 * deleteStudentDemographics
	 */
	private void deleteStudentDemographics(Integer studentId)
	{
		if ((studentId != null) && (studentId.intValue() > 0))
		{
			try
			{    
				this.studentManagement.deleteStudentDemographics(this.userName, studentId);
			}
			catch (CTBBusinessException be)
			{
				be.printStackTrace();
			}        
		}
	}

	/**
	 * getStudentDemographics
	 */
	private List getStudentDemographics(Integer studentId)
	{
		this.demographics = new ArrayList();
		try
		{
			if ((studentId != null) && (studentId.intValue() == 0))
				studentId = null;

			StudentDemographic[] studentDemoList = this.studentManagement.getStudentDemographics(this.userName, this.customerId, studentId, false);

			if (studentDemoList != null)
			{
				for (int i=0; i < studentDemoList.length; i++)
				{
					StudentDemographic sd = studentDemoList[i];
					this.demographics.add(sd);                
				}                        
			}
		}
		catch (CTBBusinessException be)
		{
			be.printStackTrace();
		}

		return this.demographics;
	}

	/**
	 * prepareOnNullRule
	 */
	private void prepareOnNullRule() 
	{
		for (int i=0; i < this.demographics.size(); i++)
		{
			StudentDemographic sdd = (StudentDemographic)this.demographics.get(i);
			if (sdd.getImportEditable().equals("ON_NULL_RULE"))
			{
				StudentDemographicValue[] values = sdd.getStudentDemographicValues();		    
				boolean hasValue = false;
				for (int j=0; j < values.length; j++)
				{
					StudentDemographicValue value = values[j];
					if ((value.getSelectedFlag() != null) && value.getSelectedFlag().equals("true"))
						hasValue = true;
				}
				if (hasValue)
				{
					sdd.setImportEditable("UNEDITABLE_ON_NULL_RULE");
				}
			}
		}
	}


	/**
	 * prepareStudentDemographicForCustomerConfiguration
	 */
	private void prepareStudentDemographicForCustomerConfiguration() 
	{
		for (int i=0; i < this.demographics.size(); i++)
		{
			StudentDemographic sdd = (StudentDemographic)this.demographics.get(i);
			if (sdd.getImportEditable().equals("ON_NULL_RULE") || sdd.getImportEditable().equals("UNEDITABLE_ON_NULL_RULE") || sdd.getImportEditable().equals("F"))
			{            
				StudentDemographicValue[] values = sdd.getStudentDemographicValues();		    
				for (int j=0; j < values.length; j++)
				{
					StudentDemographicValue value = (StudentDemographicValue)values[j];
					if ((value.getSelectedFlag() != null) && value.getSelectedFlag().equals("true"))
					{
						value.setVisible("false");            
					}
				}
			}
		}
	}


	/**
	 * getStudentDemographicsFromRequest
	 */
	private void getStudentDemographicsFromRequest() 
	{
		String param = null, paramValue = null;

		for (int i=0; i < this.demographics.size(); i++)
		{
			StudentDemographic sdd = (StudentDemographic)this.demographics.get(i);
			StudentDemographicValue[] values = sdd.getStudentDemographicValues();

			for (int j=0; j < values.length; j++)
			{
				StudentDemographicValue sdv = (StudentDemographicValue)values[j];

				// Look up the parameter based on checkbox vs radio/select
				if (sdd.getMultipleAllowedFlag().equals("true"))
				{
					if (! sdv.getVisible().equals("false"))
						sdv.setSelectedFlag("false");
					param = sdd.getLabelName() + "_" + sdv.getValueName();
					if (getRequest().getParameter(param) != null)
					{
						paramValue = getRequest().getParameter(param);
						sdv.setSelectedFlag("true");
					}
				} 
				else
				{
					if (values.length == 1)
					{
						if (! sdv.getVisible().equals("false"))
							sdv.setSelectedFlag("false");
						param = sdd.getLabelName() + "_" + sdv.getValueName();
						if (getRequest().getParameter(param) != null)
						{
							paramValue = getRequest().getParameter(param);
							sdv.setSelectedFlag("true");
						}
					}
					else
					{
						param = sdd.getLabelName();
						if (getRequest().getParameter(param) != null)
						{
							paramValue = getRequest().getParameter(param);

							for (int k=0; k < values.length; k++)
							{
								StudentDemographicValue sdv1 = (StudentDemographicValue)values[k];
								if (! sdv1.getVisible().equals("false"))
									sdv1.setSelectedFlag("false");
								if (!paramValue.equalsIgnoreCase("None") && !paramValue.equalsIgnoreCase("Please Select"))
								{
									if (paramValue.equals(sdv1.getValueName()))
									{
										sdv1.setSelectedFlag("true");
									}
								}
							}

							break;
						}
					}
				}
				sdv.setVisible("T");
			}
		}
	}


/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** ACCOMODATIONS ************* //////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * addEditAccommodations
	 */
	private void addEditAccommodations(ManageStudentForm form)
	{
		if (this.accommodations == null)
		{
			Integer studentId = form.getStudentProfile().getStudentId();
			this.accommodations = getStudentAccommodations(studentId);         
		}
		else
		{
			getStudentAccommodationsFromRequest();
		}

		this.getRequest().setAttribute("accommodations", this.accommodations);       
	}


	/**
	 * getStudentAccommodations
	 */
	private StudentAccommodationsDetail getStudentAccommodations(Integer studentId)
	{
		StudentAccommodationsDetail studentAcc = new StudentAccommodationsDetail();

		if ((studentId != null) && (studentId.intValue() > 0))
		{
			try
			{    
				StudentAccommodations sa = this.studentManagement.getStudentAccommodations(this.userName, studentId);
				studentAcc = new StudentAccommodationsDetail(sa);
			}
			catch (CTBBusinessException be)
			{
				be.printStackTrace();
			}  
		}
		else
		{
			setCustomerAccommodations(studentAcc, true);
		}

		studentAcc.convertHexToText();


		return studentAcc;
	}


	/**
	 * saveStudentAccommodation
	 */
	private boolean saveStudentAccommodations(boolean isCreateNew, ManageStudentForm form, Integer studentId)
	{
		String hideAccommodations = this.user.getCustomer().getHideAccommodations();

		if (hideAccommodations.equalsIgnoreCase("T"))         
			getStudentDefaultAccommodations();
		else
			getStudentAccommodationsFromRequest();

		StudentAccommodations sa = this.accommodations.makeCopy(studentId);

		if (isCreateNew)
		{
			if (sa != null)
			{
				createStudentAccommodations(studentId, sa);
			}
		}
		else
		{
			if (sa != null)
				updateStudentAccommodations(studentId, sa);
			else
				deleteStudentAccommodations(studentId);
		}
		this.accommodations = null;

		return true;
	}


	/**
	 * createStudentAccommodations
	 */
	private void createStudentAccommodations(Integer studentId, StudentAccommodations sa)
	{
		if ((studentId != null) && (studentId.intValue() > 0))
		{
			try
			{    
				this.studentManagement.createStudentAccommodations(this.userName, sa);
			}
			catch (CTBBusinessException be)
			{
				be.printStackTrace();
			}        
		}
	}

	/**
	 * updateStudentAccommodations
	 */
	private void updateStudentAccommodations(Integer studentId, StudentAccommodations sa)
	{
		if ((studentId != null) && (studentId.intValue() > 0))
		{
			try
			{    
				this.studentManagement.updateStudentAccommodations(this.userName, sa);
			}
			catch (CTBBusinessException be)
			{
				be.printStackTrace();
			}        
		}
	}

	/**
	 * deleteStudentAccommodations
	 */
	private void deleteStudentAccommodations(Integer studentId)
	{
		if ((studentId != null) && (studentId.intValue() > 0))
		{
			try
			{    
				this.studentManagement.deleteStudentAccommodations(this.userName, studentId);
			}
			catch (CTBBusinessException be)
			{
				be.printStackTrace();
			}        
		}
	}

	/**
	 * getStudentAccommodationsFromRequest
	 */
	private void getStudentAccommodationsFromRequest() 
	{
		// first get values from request
		String screenReader = getRequest().getParameter("screen_reader");
		String calculator = getRequest().getParameter("calculator");
		String highlighter = getRequest().getParameter("highlighter");
		String testPause = getRequest().getParameter("test_pause");
		String untimedTest = getRequest().getParameter("untimed_test");
		String colorFont = getRequest().getParameter("colorFont");

		this.accommodations.setScreenReader(new Boolean(screenReader != null));
		this.accommodations.setCalculator(new Boolean(calculator != null));
		this.accommodations.setHighlighter(new Boolean(highlighter != null));
		this.accommodations.setTestPause(new Boolean(testPause != null));
		this.accommodations.setUntimedTest(new Boolean(untimedTest != null));
		this.accommodations.setColorFont(new Boolean(colorFont != null));        

		setCustomerAccommodations(this.accommodations, false);

		String question_bgrdColor = this.getRequest().getParameter("question_bgrdColor");
		if (question_bgrdColor != null)
		{
			this.accommodations.setQuestion_bgrdColor(question_bgrdColor);
		}

		String question_fontColor = this.getRequest().getParameter("question_fontColor");
		if (question_fontColor != null)
		{
			this.accommodations.setQuestion_fontColor(question_fontColor);
		}

		String answer_bgrdColor = this.getRequest().getParameter("answer_bgrdColor");
		if (answer_bgrdColor != null)
		{
			this.accommodations.setAnswer_bgrdColor(answer_bgrdColor);
		}

		String answer_fontColor = this.getRequest().getParameter("answer_fontColor");
		if (answer_fontColor != null)
		{
			this.accommodations.setAnswer_fontColor(answer_fontColor);
		}

		String fontSize = this.getRequest().getParameter("fontSize");
		if (fontSize != null)
		{
			this.accommodations.setFontSize(fontSize);
		}
	}

	/**
	 * getStudentDefaultAccommodations
	 */
	private void getStudentDefaultAccommodations() 
	{
		this.accommodations.setScreenReader(Boolean.FALSE);
		this.accommodations.setCalculator(Boolean.FALSE);
		this.accommodations.setTestPause(Boolean.FALSE);
		this.accommodations.setUntimedTest(Boolean.FALSE);
		this.accommodations.setHighlighter(Boolean.TRUE);
		this.accommodations.setColorFont(Boolean.FALSE);        

		setCustomerAccommodations(this.accommodations, true);
	}



	/**
	 * setCustomerAccommodations
	 */
	private void setCustomerAccommodations(StudentAccommodationsDetail sad, boolean isSetDefaultValue) 
	{        
		// set checked value if there is configuration for this customer
		for (int i=0; i < this.customerConfigurations.length; i++)
		{
			CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
			String ccName = cc.getCustomerConfigurationName();
			String defaultValue = cc.getDefaultValue() != null ? cc.getDefaultValue() : "F";
			String editable = cc.getEditable() != null ? cc.getEditable() : "F";

			if (isSetDefaultValue)
				editable = "F";

			if (defaultValue.equalsIgnoreCase("T") && editable.equalsIgnoreCase("F"))
			{

				if (ccName.equalsIgnoreCase("screen_reader"))
				{
					sad.setScreenReader(Boolean.TRUE);
				}

				if (ccName.equalsIgnoreCase("calculator"))
				{
					sad.setCalculator(Boolean.TRUE);
				}

				if (ccName.equalsIgnoreCase("test_pause"))
				{
					sad.setTestPause(Boolean.TRUE);
				}

				if (ccName.equalsIgnoreCase("untimed_test"))
				{
					sad.setUntimedTest(Boolean.TRUE);
				}

				if (ccName.equalsIgnoreCase("highlighter"))
				{
					sad.setHighlighter(Boolean.TRUE);
				}
			}
		}
	}


/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** COLOR FONT SETTINGS ************* ////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="/previewer/PreviewerController.jpf"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "/previewer/PreviewerController.jpf")
	})
	protected Forward colorFontPreview()
	{      
		String param = getRequest().getParameter("param");
		getSession().setAttribute("param", param);

		return new Forward("success");
	}

	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="addEditStudent.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "addEditStudent.do")
	})
	protected Forward colorFontPreviewDone()
	{      
		return new Forward("success");
	}

/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** FIND STUDENT ************* //////////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This method represents the point of entry into the pageflow
	 * @jpf:action
	 * @jpf:forward name="success" path="findStudent.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "findStudent.do")
	})
	protected Forward beginFindStudent()
	{
		ManageStudentForm form = initialize(ACTION_FIND_STUDENT);
		form.setSelectedStudentId(null); 

		form.setSelectedTab(MODULE_STUDENT_PROFILE);        

		this.searchApplied = false;

		initGradeGenderOptions(ACTION_FIND_STUDENT, form, null, null);

		form.setSelectedOrgNodeId(null);

		return new Forward("success", form);
	}



	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="find_student.jsp"
	 * @jpf:forward name="viewStudent" path="beginViewStudent.do"
	 * @jpf:forward name="editStudent" path="beginEditStudent.do"
	 * @jpf:forward name="deleteStudent" path="beginDeleteStudent.do"
	 * @jpf:validation-error-forward name="failure" path="logout.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "find_student.jsp"), 
					@Jpf.Forward(name = "viewStudent",
							path = "beginViewStudent.do"), 
							@Jpf.Forward(name = "editStudent",
									path = "beginEditStudent.do"), 
									@Jpf.Forward(name = "deleteStudent",
											path = "beginDeleteStudent.do")
	}, 
	validationErrorForward = @Jpf.Forward(name = "failure",
			path = "logout.do"))
			protected Forward findStudent(ManageStudentForm form)
	{    
		isGeorgiaCustomer(form);// Change For CR - GA2011CR001
		form.validateValues();

		String currentAction = form.getCurrentAction();
		String actionElement = form.getActionElement();

		form.resetValuesForAction(actionElement, ACTION_FIND_STUDENT); 
		if (currentAction.equals(ACTION_VIEW_STUDENT) || currentAction.equals(ACTION_EDIT_STUDENT) || currentAction.equals(ACTION_DELETE_STUDENT))
		{
			this.viewStudentFromSearch = true;             
			this.savedForm = form.createClone();             
			this.studentSearch = form.getStudentProfile().createClone();          
			return new Forward(currentAction, form);
		}

		String selectedTab = form.getSelectedTab();
		selectedTab = JavaScriptSanitizer.sanitizeString(selectedTab);
		if (! selectedTab.equals(this.selectedModuleFind))
		{
			initFindStudent(selectedTab, form);                
		}

		initPagingSorting(form);

		boolean applySearch = initSearch(form);


		ManageStudentData msData = null;

		if (this.selectedModuleFind.equals( MODULE_STUDENT_PROFILE ))
		{
			if (applySearch)
			{
				msData = findByStudentProfile(form);    
				if ((msData != null) && (msData.getFilteredCount().intValue() == 0))
				{
					this.getRequest().setAttribute("searchResultEmpty", MessageResourceBundle.getMessage("searchResultEmpty"));        
				}
			}
		}

		if (this.selectedModuleFind.equals( MODULE_HIERARCHY ))
		{
			msData = findByHierarchy(form);                
		}


		this.searchApplied = false;
		if ((msData != null) && (msData.getFilteredCount().intValue() > 0))
		{

			this.searchApplied = true;        
			List studentList = StudentSearchUtils.buildStudentList(msData);
			PagerSummary studentPagerSummary = StudentSearchUtils.buildStudentPagerSummary(msData, form.getStudentPageRequested());        
			form.setStudentMaxPage(msData.getFilteredPages());
			 
			this.getRequest().setAttribute("studentList", studentList);        
			this.getRequest().setAttribute("studentPagerSummary", studentPagerSummary);

			if (form.getSelectedStudentId() != null){
				this.getRequest().setAttribute("disableButtons", Boolean.FALSE);

			} else {
				this.getRequest().setAttribute("disableButtons", Boolean.TRUE);      
				
			}   
			String roleName = this.user.getRole().getRoleName();
			this.getRequest().setAttribute("showEditButton", PermissionsUtils.showEditButton(roleName));
			this.getRequest().setAttribute("showDeleteButton", PermissionsUtils.showDeleteButton(roleName));
		}

		form.setCurrentAction(ACTION_DEFAULT);
		this.getRequest().setAttribute("isFindStudent", Boolean.TRUE);
		this.getRequest().setAttribute("selectedModule", this.selectedModuleFind);

		this.pageTitle = buildPageTitle(ACTION_FIND_STUDENT, form);

		setFormInfoOnRequest(form);
		return new Forward("success");
	}


	/**
	 * This method represents the point of entry into the pageflow
	 * @jpf:action
	 */
	@Jpf.Action()
	protected Forward findSuggestions()
	{                
		String controlId = this.getRequest().getHeader("controlId");        
		String controlValue = this.getRequest().getHeader("controlValue");        

		String suggestions = StudentSearchUtils.findStudentSuggestions(this.studentManagement, this.userName, controlId, controlValue.toLowerCase());   
		WebUtils.writeResponseContent(this.getResponse(), suggestions);        

		return null;
	}

	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="findStudent.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "findStudent.do")
	})
	protected Forward returnToFindStudent(ManageStudentForm form)
	{   
		this.savedForm.setStudentProfile(this.studentSearch);
		this.savedForm.setSelectedTab( this.selectedModuleFind );
		this.savedForm.setCurrentAction( ACTION_DEFAULT );
		this.savedForm.setSelectedOrgNodeName(form.getSelectedOrgNodeName());                

		this.savedForm.setMessage(form.getMessage());

		String grade = (this.studentSearch != null) ? this.studentSearch.getGrade() : null;
		String gender = (this.studentSearch != null) ? this.studentSearch.getGender() : null;

		initGradeGenderOptions(ACTION_FIND_STUDENT, this.savedForm, grade, gender);

		this.copyOfOrgNodeId = this.savedForm.getSelectedOrgNodeId();
		setFormInfoOnRequest(this.savedForm);
		return new Forward("success", this.savedForm);
	}

	/**
	 * initFindStudent
	 */
	private void initFindStudent(String selectedTab, ManageStudentForm form)
	{
		this.selectedModuleFind = selectedTab;
		// Solution for Deferred Defect 51520
		this.copyOfOrgNodeId = null;

		if (this.selectedModuleFind.equals( MODULE_STUDENT_PROFILE ))
		{
			clearStudentProfileSearch(form);    
		}

		if (this.selectedModuleFind.equals( MODULE_HIERARCHY ))
		{
			clearHierarchySearch(form);    
		}
	}



	/**
	 * findByHierarchy
	 */
	private ManageStudentData findByHierarchy(ManageStudentForm form)
	{      
		form.validateValues();

		ManageStudentData msData = null;
		String actionElement = form.getActionElement();
		String currentAction = form.getCurrentAction();

		form.resetValuesForAction(actionElement, ACTION_FIND_STUDENT);        

		String orgNodeName = form.getOrgNodeName();
		Integer orgNodeId = form.getOrgNodeId();   
		boolean nodeChanged = StudentPathListUtils.adjustOrgNodePath(this.orgNodePath, orgNodeId, orgNodeName);

		if (nodeChanged)
		{
			form.resetValuesForPathList();
			form.setSelectedOrgNodeId(null);
			if (this.copyOfOrgNodeId != null)
			{
				form.setSelectedOrgNodeId(this.copyOfOrgNodeId);
				this.copyOfOrgNodeId = null;
			}

		}

		FilterParams filter = null;
		PageParams page = FilterSortPageUtils.buildPageParams(form.getOrgPageRequested(), FilterSortPageUtils.PAGESIZE_5);
		SortParams sort = FilterSortPageUtils.buildSortParams(form.getOrgSortColumn(), form.getOrgSortOrderBy(), null, null);

		OrganizationNodeData ond = StudentPathListUtils.getOrganizationNodes(this.userName, this.studentManagement, orgNodeId, filter, page, sort);        

		if (form.getOrgPageRequested().intValue() > ond.getFilteredPages().intValue())
		{
			form.setOrgPageRequested(ond.getFilteredPages());
		}
		List orgNodes = StudentPathListUtils.buildOrgNodeList(ond, Boolean.TRUE, ACTION_FIND_STUDENT,Boolean.TRUE );  //Added for CR017
		String orgCategoryName = StudentPathListUtils.getOrgCategoryName(orgNodes);

		PagerSummary orgPagerSummary = StudentPathListUtils.buildOrgNodePagerSummary(ond, form.getOrgPageRequested());        
		form.setOrgMaxPage(ond.getFilteredPages());

		if (actionElement.equals("actionElement"))  //Changes for Find_Student_Hierarchy Display Message
		{
			PathNode node = StudentPathListUtils.findOrgNode(orgNodes, form.getSelectedOrgNodeId());
			if (node != null)
			{
				form.setSelectedOrgNodeId(node.getId());
				form.setSelectedOrgNodeName(node.getName());                
				form.setSelectedStudentId(null);
			}
		}
		this.getRequest().setAttribute("orgNodePath", this.orgNodePath);
		this.getRequest().setAttribute("orgNodes", orgNodes);        
		this.getRequest().setAttribute("orgPagerSummary", orgPagerSummary);
		this.getRequest().setAttribute("orgCategoryName", orgCategoryName);        

		this.pageMessage = "";
		Integer selectedOrgNodeId = form.getSelectedOrgNodeId();
		if (selectedOrgNodeId != null)
		{
			filter = null;
			page = FilterSortPageUtils.buildPageParams(form.getStudentPageRequested(), FilterSortPageUtils.PAGESIZE_10);
			sort = FilterSortPageUtils.buildStudentSortParams(form.getStudentSortColumn(), form.getStudentSortOrderBy());

			msData = StudentSearchUtils.searchStudentsByOrgNode(this.userName, this.studentManagement, selectedOrgNodeId, filter, page, sort);
			if (msData.getFilteredCount().intValue() > 0)
			{
				this.pageMessage = "The following students are at <b>" +
				form.getSelectedOrgNodeName() +
				"</b>";                                    
			}
			else
			{
				form.setSelectedOrgNodeId(null);
			}
		}

		return msData;
	}



	/**
	 * findByStudentProfile
	 */
	private ManageStudentData findByStudentProfile(ManageStudentForm form)
	{
		String actionElement = form.getActionElement();
		form.resetValuesForAction(actionElement, ACTION_FIND_STUDENT);        

		String firstName = form.getStudentProfile().getFirstName().trim();
		String middleName = form.getStudentProfile().getMiddleName().trim();
		String lastName = form.getStudentProfile().getLastName().trim();
		String loginId = form.getStudentProfile().getUserName().trim();
		String studentNumber = form.getStudentProfile().getStudentNumber().trim();
		String grade = form.getStudentProfile().getGrade().trim();
		String gender = form.getStudentProfile().getGender().trim();

		if (! gender.equals(FilterSortPageUtils.FILTERTYPE_ANY_GENDER))
		{
			if (gender.equals("Male"))
				gender = "M";
			else if (gender.equals("Female"))
				gender = "F";
			else
				gender = "U";
		}

		String invalidCharFields = WebUtils.verifyFindStudentInfo(firstName, lastName, middleName, studentNumber, loginId, form.studentIdLabelName);                

		if (invalidCharFields.length() > 0)
		{
			invalidCharFields += ("<br/>" +
					Message.INVALID_CHARS);
			form.setMessage(MessageResourceBundle.getMessage("invalid_char_message"), invalidCharFields, Message.ERROR);
			return null;
		}


		PageParams page = FilterSortPageUtils.buildPageParams(form.getStudentPageRequested(), FilterSortPageUtils.PAGESIZE_10);
		SortParams sort = FilterSortPageUtils.buildStudentSortParams(form.getStudentSortColumn(), form.getStudentSortOrderBy());
		FilterParams filter = FilterSortPageUtils.buildFilterParams(firstName, middleName, lastName, loginId, studentNumber, grade, gender);

		ManageStudentData msData = null;

		if (filter == null)
		{
			msData = StudentSearchUtils.searchAllStudentsAtAndBelow(this.userName, this.studentManagement, page, sort);   
			this.pageMessage = MessageResourceBundle.getMessage("searchResultFound");
		}
		else
		{
			msData = StudentSearchUtils.searchStudentsByProfile(this.userName, this.studentManagement, filter, page, sort);   
			this.pageMessage = MessageResourceBundle.getMessage("searchProfileFound");
		}

		return msData;
	}




	/**
	 * initSearch
	 */
	private boolean initSearch(ManageStudentForm form)
	{
		boolean applySearch = false;
		String currentAction = form.getCurrentAction();

		if ((currentAction != null) && currentAction.equals(ACTION_APPLY_SEARCH))
		{
			applySearch = true;
			this.searchApplied = false;
			form.setStudentSortColumn(FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN);
			form.setStudentSortOrderBy(FilterSortPageUtils.ASCENDING);      
			form.setStudentPageRequested(new Integer(1));    
			form.setStudentMaxPage(new Integer(1));                  
		}

		if ((currentAction != null) && currentAction.equals(ACTION_CLEAR_SEARCH))
		{
			applySearch = false;
			this.searchApplied = false;
			form.clearSearch();
		}

		if (this.searchApplied)
		{
			applySearch = true;
		}
		else
		{
			form.setSelectedStudentId(null);
		}

		return applySearch;
	}


	/**
	 * initPagingSorting
	 */
	private void initPagingSorting(ManageStudentForm form)
	{
		String actionElement = form.getActionElement();

		if ((actionElement.indexOf("orgPageRequested") > 0) || (actionElement.indexOf("orgSortOrderBy") > 0))
		{
			this.searchApplied = false;
			form.setSelectedStudentId(null);
			form.setSelectedOrgNodeId(null);
		}
		if ((actionElement.indexOf("studentPageRequested") > 0) || (actionElement.indexOf("studentSortOrderBy") > 0))
		{
			form.setSelectedStudentId(null);
		}
	}





/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** VIEW STUDENT ************* //////////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="viewStudent.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "viewStudent.do")
	})
	protected Forward beginViewStudent(ManageStudentForm form)
	{         
		form.setCurrentAction(MODULE_STUDENT_PROFILE);
		form.clearSectionVisibility();
		form.setByStudentProfileVisible(Boolean.TRUE);

		return new Forward("success", form);
	}

	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="view_student.jsp"
	 * @jpf:validation-error-forward name="failure" path="viewStudent.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "view_student.jsp"),
			@Jpf.Forward(name = "error",
					path = "findStudent.do")
	}, 
	validationErrorForward = @Jpf.Forward(name = "failure",
			path = "viewStudent.do"))
			protected Forward viewStudent(ManageStudentForm form)
	{   
		//Save the token to remove F5 problem
		saveToken(this.getRequest());   
		Integer studentId = form.getSelectedStudentId(); 
		
		boolean studentImported = (form.getStudentProfile().getCreateBy().intValue() == 1);

		StudentProfileInformation studentProfile = StudentSearchUtils.getStudentProfileInformation(this.studentManagement, this.userName, studentId);
		if (studentProfile == null) {							//Changed for Defect 60478
            form.setCurrentAction(ACTION_DEFAULT);
           return new Forward("error", form);
        }
		//this.getRequest().setAttribute("studentProfileForView",studentProfile);
		form.setStudentProfile(studentProfile);

		this.studentName = studentProfile.getFirstName() + " " + studentProfile.getLastName();        

		form.setCurrentAction(ACTION_DEFAULT);
		this.getRequest().setAttribute("selectedModule", MODULE_STUDENT_PROFILE);
		if (this.viewStudentFromSearch) 
			this.getRequest().setAttribute("showBackButton", "returnToFindStudent");
		else
			this.getRequest().setAttribute("showBackButton", "beginAddStudent");

		this.getRequest().setAttribute("organizationNodes", studentProfile.getOrganizationNodes());       

		this.demographics = getStudentDemographics(studentId);
		this.getRequest().setAttribute("demographics", this.demographics);       
		this.getRequest().setAttribute("studentImported", new Boolean(studentImported));       

		this.accommodations = getStudentAccommodations(studentId);
		this.getRequest().setAttribute("accommodations", this.accommodations);       

		this.getRequest().setAttribute("viewOnly", Boolean.TRUE);       

		this.getRequest().setAttribute("disableColorSelection", Boolean.TRUE);       

		this.pageTitle = buildPageTitle(ACTION_VIEW_STUDENT, form);

		String roleName = this.user.getRole().getRoleName();
		this.getRequest().setAttribute("showEditButton", PermissionsUtils.showEditButton(roleName));
		this.getRequest().setAttribute("showDeleteButton", PermissionsUtils.showDeleteButton(roleName));

		setCustomerSettings();

		if (this.demographics.size() == 0) {
			this.getRequest().setAttribute("demographicVisible", "F");       
		}
		isGeorgiaCustomer(form); //Change For CR - GA2011CR001
		setFormInfoOnRequest(form);
		return new Forward("success", form);                                                                                                                                                                                                    
	}





/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** DELETE STUDENT ************* ////////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This method represents the point of entry into the pageflow
	 * @jpf:action
	 * @jpf:forward name="success" path="deleteStudent.do"
	 */
	@Jpf.Action(
			forwards = { 
					@Jpf.Forward(name = "success", path = "deleteStudent.do")
			}
	)
	protected Forward beginDeleteStudent(ManageStudentForm form)
	{        
		return new Forward("success", form);
	}

	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="returnToFindStudent.do"
	 */
	@Jpf.Action(
			forwards = { 
					@Jpf.Forward(name = "success", path = "returnToFindStudent.do")
			}
	)
	protected Forward deleteStudent(ManageStudentForm form)
	{
		Integer studentId = form.getSelectedStudentId(); 
		if (studentId != null) {
			try {                    
				DeleteStudentStatus status = this.studentManagement.deleteStudent(this.userName, studentId);
				this.savedForm.setMessage(Message.DELETE_TITLE, Message.DELETE_SUCCESSFUL, Message.INFORMATION);
				this.savedForm.setSelectedStudentId(null);
			}
			catch (StudentDataDeletionException sde) {
				sde.printStackTrace();
				this.savedForm.setMessage(Message.DELETE_TITLE, Message.DELETE_ERROR_TAS, Message.INFORMATION);
			}        
			catch (CTBBusinessException be) {
				be.printStackTrace();
				this.savedForm.setMessage(Message.DELETE_TITLE, be.getMessage(), Message.INFORMATION);
			}  
		}    
		return new Forward("success", this.savedForm);
	}






/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** PRIVATE METHODS ************* ///////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * buildPageTitle
	 */
	private String buildPageTitle(String action, ManageStudentForm form)
	{
		String title = "";
		StudentProfileInformation student = null;

		if (action.equals(ACTION_FIND_STUDENT)) {
			title = "Find Student";
		}
		else
			if (action.equals(ACTION_ADD_STUDENT)) {
				title = "Add Student";
			}
			else
				if (action.equals(ACTION_VIEW_STUDENT)) {
					title = "View Student: ";
					student = form.getStudentProfile();
				}
				else
					if (action.equals(ACTION_EDIT_STUDENT)) {
						title = "Edit Student: ";
						student = form.getStudentProfile();
					}

		if (student != null) {
			title += this.studentName;
		}

		return title;            
	}    


	/**
	 * getCustomerConfigurations
	 */
	private void getCustomerConfigurations()
	{
		try {
			//if (this.customerConfigurations == null) {   //Changes for Defect-60479
				this.customerConfigurations = this.studentManagement.getCustomerConfigurations(this.userName, this.customerId);
			//}
		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}
	}
	
	/*
	 * New method added for CR - GA2011CR001
	 * this method retrieve CustomerConfigurationsValue for provided customer configuration Id.
	 */
	private void customerConfigurationValues(Integer configId)
	{
		try {
				this.customerConfigurationsValue = this.studentManagement.getCustomerConfigurationsValue(configId);
			
		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}
	}
	
	/*
	 * New method added for CR - GA2011CR001
	 * this method retrieve CustomerConfigurationsValue for provided customer configuration Id.
	 */
	private String[] getDefaultValue(String [] arrValue, String labelName, ManageStudentForm form)
	{
		arrValue[0] = arrValue[0] != null ? arrValue[0]   : labelName ;
		arrValue[1] = arrValue[1] != null ? arrValue[1]   : "32" ;
		
		if(labelName.equals("Student ID 2")){
			this.studentId2LabelName = arrValue[0];
			form.setStudentId2LabelName(this.studentId2LabelName );
			
		}
		if(labelName.equals("Student ID")){
			arrValue[2] = arrValue[2] != null ? arrValue[2]   : "F" ;
			if(!arrValue[2].equals("T") && !arrValue[2].equals("F"))
				{ 
					arrValue[2]  = "F";
				}
			this.studentIdLabelName = arrValue[0];
			form.setStudentIdLabelName(this.studentIdLabelName );
			
		}
		
		// check for numeric conversion of maxlength

		try {
			int maxLength = Integer.valueOf(arrValue[1]);
		} catch (NumberFormatException nfe){
			arrValue[1] = "32" ;
		}
		
		
		
		return arrValue;
	}
	
	/**
	 * getGradeOptions
	 */
	private String [] getGradeOptions(String action)
	{
		String[] grades = null;
		try {
			grades =  this.studentManagement.getGradesForCustomer(this.userName, this.customerId);
		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}

		List options = new ArrayList();
		if ( action.equals(ACTION_FIND_STUDENT) )
			options.add(FilterSortPageUtils.FILTERTYPE_ANY_GRADE);
		if ( action.equals(ACTION_ADD_STUDENT) || action.equals(ACTION_EDIT_STUDENT) )
			options.add(FilterSortPageUtils.FILTERTYPE_SELECT_A_GRADE);

		for (int i=0 ; i<grades.length ; i++) {        
			options.add(grades[i]);
		}

		return (String [])options.toArray(new String[0]);        
	}

	/**
	 * getGenderOptions
	 */
	private String [] getGenderOptions(String action)
	{
		List options = new ArrayList();
		if ( action.equals(ACTION_FIND_STUDENT) )
			options.add(FilterSortPageUtils.FILTERTYPE_ANY_GENDER);
		if ( action.equals(ACTION_ADD_STUDENT) || action.equals(ACTION_EDIT_STUDENT) )
			options.add(FilterSortPageUtils.FILTERTYPE_SELECT_A_GENDER);

		options.add("Male");
		options.add("Female");
		options.add("Unknown");

		return (String [])options.toArray(new String[0]);        
	}


	/**
	 * initGradeGenderOptions
	 */
	private void initGradeGenderOptions(String action, ManageStudentForm form, String grade, String gender)
	{        
		this.gradeOptions = getGradeOptions(action);
		if (grade != null)
			form.getStudentProfile().setGrade(grade);
		else
			form.getStudentProfile().setGrade(this.gradeOptions[0]);

		this.genderOptions = getGenderOptions( action );
		if (gender != null)
			form.getStudentProfile().setGender(gender);
		else
			form.getStudentProfile().setGender(this.genderOptions[0]);
	}

	/**
	 * clearStudentProfileSearch
	 */
	private void clearStudentProfileSearch(ManageStudentForm form)
	{   
		this.searchApplied = false;

		form.clearSearch();    

		form.setSelectedStudentId(null);
		form.setSelectedOrgNodeId(null);
	}

	/**
	 * clearHierarchySearch
	 */
	private void clearHierarchySearch(ManageStudentForm form)
	{   
		this.searchApplied = false;
		this.orgNodePath = new ArrayList();

		form.setOrgNodeName("Top");
		form.setOrgNodeId(new Integer(0));

		form.setSelectedStudentId(null);
		form.setSelectedOrgNodeId(null);
	}

	/*
	 * set form Value in request
	 */
	private void setFormInfoOnRequest(ManageStudentForm form) {


		this.getRequest().setAttribute("pageMessage", form.getMessage());
		this.getRequest().setAttribute("studentProfileData", form.getStudentProfile());
		this.getSession().setAttribute("selectStudentIdInView",form.getSelectedStudentId());
		
	}
	/*
	 * GACRCT2010CR007- retrieve value for disableMandatoryBirthdate set  Value in request. 
	 */
	private void isMandatoryBirthDate() 
    {     
		boolean disableMandatoryBirthdateValue = false;
           for (int i=0; i < this.customerConfigurations.length; i++)
            {
                CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
                if (cc.getCustomerConfigurationName().equalsIgnoreCase("Disable_Mandatory_Birth_Date") && cc.getDefaultValue().equalsIgnoreCase("T"))
                {
                	disableMandatoryBirthdateValue = true; 
                }
             }
           disableMandatoryBirthdate = disableMandatoryBirthdateValue;
           this.getRequest().setAttribute("isMandatoryBirthDate", disableMandatoryBirthdate);
                
     }
               


	/*
	 * CR017- based on the value of customercofiguration and profileEditable flag set Value in profileEditable flag. 
	 */
	private boolean isClassReassignable(Boolean profileEditable) 
    {     
		boolean classReassignable = false;
		
		if(profileEditable)
			 return true ;
		else
		{
			for (int i=0; i < this.customerConfigurations.length; i++)
	        {
	            CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
	            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Class_Reassignment") && cc.getDefaultValue().equalsIgnoreCase("T"))
	            {
	            	classReassignable = true; 
	            	break;
	            }
	         }
		}
			
		 
		 return classReassignable;
     }
      
	/*
	 * New method added for CR  ISTEP2011CR017 . 
	 */
	private boolean isMultiOrgAssociationValid() 
    {     
		boolean multiOrgAssociationValid = true;
		
		
		
			for (int i=0; i < this.customerConfigurations.length; i++)
	        {
	            CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
	            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Class_Reassignment") && cc.getDefaultValue().equalsIgnoreCase("T"))
	            {
	            	multiOrgAssociationValid = false; 
	            	break;
	            }
	         }
		
			
		 
		 return multiOrgAssociationValid;
     }
        
    
	/*
	 * New method added for CR - GA2011CR001
	 * This method retrieve  the value of provide two customer configuration and their corresponding data in customer configuration value.
	 */
	private void isGeorgiaCustomer(ManageStudentForm form) 
    {     
		 boolean isStudentIdConfigurable = false;
		 boolean isStudentId2Configurable = false;
		 Integer configId=0;
		 String []valueForStudentId = new String[3] ;
		 String []valueForStudentId2 = new String[2] ;
		for (int i=0; i < this.customerConfigurations.length; i++)
	        {
	            CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
	            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Student_ID_2") && cc.getDefaultValue().equalsIgnoreCase("T"))
				{
					isStudentId2Configurable = true; 
					configId = cc.getId();
					customerConfigurationValues(configId);
					valueForStudentId2 = new String[2];

					for(int j=0; j<this.customerConfigurationsValue.length; j++){

						int sortOrder = this.customerConfigurationsValue[j].getSortOrder();
						valueForStudentId2[sortOrder-1] = this.customerConfigurationsValue[j].getCustomerConfigurationValue();

					}
					valueForStudentId2 = getDefaultValue(valueForStudentId2,"Student ID 2", form);
				}
	            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Student_ID") && cc.getDefaultValue().equalsIgnoreCase("T"))
				{
					isStudentIdConfigurable = true; 
					configId = cc.getId();
					customerConfigurationValues(configId);
					//By default there should be 3 entries for customer configurations
					valueForStudentId = new String[3];
					for(int j=0; j<this.customerConfigurationsValue.length; j++){
						int sortOrder = this.customerConfigurationsValue[j].getSortOrder();
						valueForStudentId[sortOrder-1] = this.customerConfigurationsValue[j].getCustomerConfigurationValue();
					}	
					valueForStudentId = getDefaultValue(valueForStudentId,"Student ID", form);
					
				}
	            
	         }
		if(valueForStudentId.length ==3) {
			this.isMandatoryStudentId = valueForStudentId !=null &&  valueForStudentId[2] != null && valueForStudentId[2].equals("T") ?  true : false ;
			
		}
		this.getRequest().setAttribute("studentIdArrValue",valueForStudentId);
        this.getRequest().setAttribute("isStudentIdConfigurable",isStudentIdConfigurable);
        this.getRequest().setAttribute("isStudentId2Configurable",isStudentId2Configurable);
        this.getRequest().setAttribute("studentId2ArrValue",valueForStudentId2);
    }
        

/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** MANAGESTUDENTFORM ************* /////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////    
	/**
	 * FormData get and set methods may be overwritten by the Form Bean editor.
	 */
	public static class ManageStudentForm extends SanitizedFormData
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
		public ManageStudentForm()
		{
		}

		public void init(String action)
		{
			this.actionElement = ACTION_DEFAULT;
			this.currentAction = ACTION_DEFAULT;

			this.selectedTab = MODULE_STUDENT_PROFILE;

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
			if (actionElement.equals("{actionForm.orgSortOrderBy}")) {
				this.orgPageRequested = new Integer(1);
			}
			if (actionElement.equals("{actionForm.studentSortOrderBy}")) {
				this.studentPageRequested = new Integer(1);
			}
			if (actionElement.equals("ButtonGoInvoked_studentSearchResult") ||
					actionElement.equals("EnterKeyInvoked_studentSearchResult")) {
				this.selectedStudentId = null;
			}
			if (actionElement.equals("ButtonGoInvoked_tablePathListAnchor") ||
					actionElement.equals("EnterKeyInvoked_tablePathListAnchor")) {
				this.selectedOrgNodeId = null;
				if (fromAction.equals(ACTION_FIND_STUDENT))
					this.selectedStudentId = null;
			}
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


		public ManageStudentForm createClone()
		{
			ManageStudentForm copied = new ManageStudentForm();

			copied.setActionElement(this.actionElement);
			copied.setCurrentAction(this.currentAction);
			copied.setSelectedTab(this.selectedTab);

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
		public void setSelectedTab(String selectedTab)
		{
			if ((selectedTab == null) || selectedTab.equals(MODULE_STUDENT_PROFILE) || selectedTab.equals(MODULE_HIERARCHY))
				this.selectedTab = selectedTab;
			else
				throw new RuntimeException("XSS attack detected!");
		}

		public String getSelectedTab()
		{
			return this.selectedTab != null ? this.selectedTab : MODULE_STUDENT_PROFILE;
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



	//Added getter method for all pageFlow attribute in weblogic 10.3
	public String getPageTitle() {
		return pageTitle;
	}

	public String[] getGradeOptions() {
		return gradeOptions;
	}

	public String[] getGenderOptions() {
		return genderOptions;
	}

	public String getPageMessage() {
		return pageMessage;
	}

	public String[] getMonthOptions() {
		return monthOptions;
	}

	public String[] getDayOptions() {
		return dayOptions;
	}

	public String[] getYearOptions() {
		return yearOptions;
	}
	
	// start Change For CR - GA2011CR001
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
	// End  Change For CR - GA2011CR001
}
