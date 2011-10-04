package studentOperation;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import manageStudent.ManageStudentController.ManageStudentForm;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import utils.Base;
import utils.BaseTree;
import utils.DateUtils;
import utils.FilterSortPageUtils;
import utils.MessageInfo;
import utils.OptionList;
import utils.Organization;
import utils.PermissionsUtils;
import utils.Row;
import utils.StudentPathListUtils;
import utils.StudentSearchUtils;
import utils.TreeData;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.studentManagement.CustomerConfiguration;
import com.ctb.bean.studentManagement.CustomerConfigurationValue;
import com.ctb.bean.studentManagement.ManageStudent;
import com.ctb.bean.studentManagement.ManageStudentData;
import com.ctb.bean.studentManagement.MusicFiles;
import com.ctb.bean.studentManagement.StudentDemographic;
import com.ctb.bean.studentManagement.StudentDemographicValue;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.StudentAccommodations;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.studentManagement.StudentDataCreationException;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.google.gson.Gson;

import dto.Message;
import dto.StudentAccommodationsDetail;
import dto.StudentProfileInformation;

@Jpf.Controller()
public class StudentOperationController extends PageFlowController {
	private static final long serialVersionUID = 1L;

	@Control()
	private com.ctb.control.studentManagement.StudentManagement studentManagement;

	@Control()
	private com.ctb.control.db.OrgNode orgnode;

	private String userName = null;
	private Integer customerId = null;
	private User user = null;
	List demographics = null;
	// student accommodations
	public StudentAccommodationsDetail accommodations = null;




	//Constants
	public static String CONTENT_TYPE_JSON = "application/json";
	private static final String ACTION_FIND_STUDENT      = "findStudent";
	private static final String ACTION_EDIT_STUDENT      = "editStudent";
	private static final String ACTION_ADD_STUDENT       = "addStudent";
	private static final String ACTION_DELETE_STUDENT    = "deleteStudent";

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

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

/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** FIND STUDENT ************* //////////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This method represents the point of entry into the pageflow
	 * @jpf:action
	 * @jpf:forward name="success" path="findStudentHierarchy.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "findStudentHierarchy.do")
	})
	protected Forward beginFindStudent()
	{
		initialize();
		return new Forward("success");
	}


	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="findStudent.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "student_hierarchy.jsp")
	}, 
	validationErrorForward = @Jpf.Forward(name = "failure",
			path = "logout.do"))
			protected Forward findStudentHierarchy(StudentOperationForm form)
	{   
		this.getRequest().setAttribute("isFindStudent", Boolean.TRUE);
		return new Forward("success");
	}


	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="find_student_hierarchy.jsp")
	})
	protected Forward userOrgNodeHierarchyList(StudentOperationForm form){

		String jsonTree = "";
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String contentType = CONTENT_TYPE_JSON;
		try {
			BaseTree baseTree = new BaseTree ();

			ArrayList<Organization> completeOrgNodeList = new ArrayList<Organization>();
			UserNodeData associateNode = StudentPathListUtils.populateAssociateNode(this.userName,this.studentManagement);
			ArrayList<Organization> selectedList  = StudentPathListUtils.buildassoOrgNodehierarchyList(associateNode);	
			Integer leafNodeCategoryId = StudentPathListUtils.getLeafNodeCategoryId(this.userName,this.customerId, this.studentManagement);
			ArrayList <Integer> orgIDList = new ArrayList <Integer>();
			ArrayList<TreeData> data = new ArrayList<TreeData>();

			UserNodeData und = StudentPathListUtils.OrgNodehierarchy(this.userName, 
					this.studentManagement, selectedList.get(0).getOrgNodeId()); 
			ArrayList<Organization> orgNodesList = StudentPathListUtils.buildOrgNodehierarchyList(und, orgIDList,completeOrgNodeList);	

			jsonTree = generateTree(orgNodesList);

			for (int i= 0; i < selectedList.size(); i++) {

				if (i == 0) {

					preTreeProcess (data,orgNodesList);

				} else {

					Integer nodeId = selectedList.get (i).getOrgNodeId();
					if (orgIDList.contains(nodeId)) {
						continue;
					} else {

						orgIDList = new ArrayList <Integer>();
						UserNodeData undloop = StudentPathListUtils.OrgNodehierarchy(this.userName, 
								this.studentManagement,nodeId);   
						ArrayList<Organization> orgNodesListloop = StudentPathListUtils.buildOrgNodehierarchyList(undloop, orgIDList, completeOrgNodeList);	
						preTreeProcess (data,orgNodesListloop);
					}
				}


			}

			Gson gson = new Gson();
			baseTree.setData(data);
			baseTree.setLeafNodeCategoryId(leafNodeCategoryId);
			jsonTree = gson.toJson(baseTree);
			//System.out.println(jsonTree);


			try {

				resp.setContentType(contentType);
				resp.flushBuffer();
				stream = resp.getOutputStream();
				stream.write(jsonTree.getBytes());
			} finally{
				if (stream!=null){
					stream.close();
				}
			}
		} catch (Exception e) {
			System.err.println("Exception while processing CR response.");
			e.printStackTrace();
		}

		return null;

	}


	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="find_user_by_hierarchy.jsp")
	})
	protected Forward getStudentForSelectedOrgNodeGrid(StudentOperationForm form){

		String jsonTree = "";
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		String treeOrgNodeId = getRequest().getParameter("treeOrgNodeId");
		OutputStream stream = null;
		String contentType = CONTENT_TYPE_JSON;
		List studentList = new ArrayList(0);
		String json = "";
		ObjectOutput output = null;
		try {
			System.out.println ("db process time Start:"+new Date());
			ManageStudentData msData = findStudentByHierarchy();
			System.out.println ("db process time End:"+new Date());
			/*try{
			  System.out.println("List serialization start.......");
				OutputStream file = new FileOutputStream( "C:/studentList.ser" );
	    		 OutputStream buffer = new BufferedOutputStream( file );
	    	      output = new ObjectOutputStream( buffer );
	    	      output.writeObject(studentList);
	    	  System.out.println("List serialization end.......");
			} finally {

				output.close();
			}*/
			if ((msData != null) && (msData.getFilteredCount().intValue() > 0))
			{
				System.out.println ("List process time Start:"+new Date());
				studentList = StudentSearchUtils.buildStudentList(msData);
				System.out.println ("List process time End:"+new Date());
			}
			Base base = new Base();
			base.setPage("1");
			base.setRecords("10");
			base.setTotal("2");
			List <Row> rows = new ArrayList<Row>();
			String fName=null,lName=null,address=null ,email= null,role= null;

			System.out.println("just b4 gson");	
			Gson gson = new Gson();
			System.out.println ("Json process time Start:"+new Date());
			base.setStudentProfileInformation(studentList);
			json = gson.toJson(base);
			System.out.println ("Json process time End:"+new Date());


			/*InputStream file = new FileInputStream( "C:/studentList.ser" );
		    	      InputStream buffer = new BufferedInputStream( file );
		    	      ObjectInput input = new ObjectInputStream ( buffer );

		    	      try{

		    	    	  studentList = (List)input.readObject();
		    	    	  if (studentList.size() > 0) {

		    	    		System.out.println("Deserialize list......");
		    	    	  }
		    	    	  base.setStudentProfileInformation(studentList);
		    	    	  System.out.println ("Json process time Start:"+new Date());
				    	  json = gson.toJson(base);
				    	  System.out.println ("Json process time End:"+new Date());
		    	      }
		    	      finally{
		    	    	  input.close();
		    	      }*/

			//System.out.println(json);
			try{
				resp.setContentType("application/json");
				stream = resp.getOutputStream();
				resp.flushBuffer();
				stream.write(json.getBytes());

			}

			finally{
				if (stream!=null){
					stream.close();
				}
			}



		} catch (Exception e) {
			System.err.println("Exception while processing CR response.");
			e.printStackTrace();
		}

		return null;

	}

	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="find_user_by_hierarchy.jsp")
	})
	protected Forward getOptionList(StudentOperationForm form){
		String jsonResponse = "";
		OutputStream stream = null;
		Boolean isLasLinkCustomer = new Boolean(getRequest().getParameter("isLasLinkCustomer"));
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			if(isLasLinkCustomer) {
				getTestPurposeOptions(ACTION_ADD_STUDENT);
		     }
			OptionList optionList = new OptionList();
			optionList.setGradeOptions(getGradeOptions(ACTION_ADD_STUDENT));
			optionList.setGenderOptions(getGenderOptions(ACTION_ADD_STUDENT));
			optionList.setMonthOptions( DateUtils.getMonthOptions());
			optionList.setDayOptions(DateUtils.getDayOptions());
			optionList.setYearOptions(DateUtils.getYearOptions());
			optionList.setTestPurposeOptions(getTestPurposeOptions(ACTION_ADD_STUDENT));
			optionList.setProfileEditable(true);
			
			try {
				Gson gson = new Gson();
				String json = gson.toJson(optionList);
				resp.setContentType("application/json");
				resp.flushBuffer();
				stream = resp.getOutputStream();
				stream.write(json.getBytes());

			} finally{
				if (stream!=null){
					stream.close();
				}
			}
		}
		catch (Exception e) {
			System.err.println("Exception while retrieving optionList.");
			e.printStackTrace();
		}
		return null;
	}


	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="find_user_by_hierarchy.jsp")
	})
	protected Forward saveAddEditStudent(ManageStudentForm form)
	{   
		String jsonResponse = "";
		OutputStream stream = null;
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		
		StudentProfileInformation studentProfile = null;
		studentProfile = new StudentProfileInformation();
		studentProfile.setFirstName(getRequest().getParameter("studentFirstName"));
		studentProfile.setMiddleName(getRequest().getParameter("studentMiddleName"));
		studentProfile.setLastName(getRequest().getParameter("studentLastName"));
		studentProfile.setMonth(getRequest().getParameter("monthOptions"));
		studentProfile.setDay(getRequest().getParameter("dayOptions"));
		studentProfile.setYear(getRequest().getParameter("yearOptions"));
		studentProfile.setGender(getRequest().getParameter("genderOptions"));
		studentProfile.setGrade(getRequest().getParameter("gradeOptions"));
		studentProfile.setTestPurpose(getRequest().getParameter("testPurposeOptions"));
		studentProfile.setStudentNumber(getRequest().getParameter("studentExternalId"));
		studentProfile.setStudentSecondNumber(getRequest().getParameter("studentExternalId2"));
		boolean studentIdConfigurable = new Boolean(getRequest().getParameter("studentIdConfigurable"));
		String studentIdLabelName = getRequest().getParameter("studentIdLabelName");
		String assignedOrgNodeIds = getRequest().getParameter("assignedOrgNodeIds");
		String[] assignedOrgNodeId = assignedOrgNodeIds.split(",");
		List <Integer> selectedOrgNodes = new ArrayList <Integer>(assignedOrgNodeId.length);
		for (int i = assignedOrgNodeId.length - 1; i >= 0; i--) {
			selectedOrgNodes.add( new Integer(assignedOrgNodeId[i].trim()));
		}
		//selectedOrgNodes.add(118641);
		MessageInfo messageInfo = new MessageInfo();
		Integer studentId = form.getSelectedStudentId();

		/*if ( studentId == null) {

			studentId = (Integer)this.getSession().getAttribute("selectStudentIdInView");
			form.setSelectedStudentId(studentId);
		}*/

		boolean isCreateNew = studentId == null ? true : false;


		//this.selectedOrgNodes = StudentPathListUtils.buildSelectedOrgNodes(this.currentOrgNodesInPathList, this.currentOrgNodeIds, this.selectedOrgNodes);
		boolean result = true;
		try {
			CustomerConfiguration[]  customerConfigurations = this.studentManagement.getCustomerConfigurations(this.userName, this.customerId);

			if (result) {
				if (isValidationForUniqueStudentIDRequired(studentProfile, customerConfigurations)) {
					result = validateUniqueStudentId(isCreateNew, null, studentProfile);
					if (!result) {
						String messageTitle = studentIdConfigurable ? Message.VALIDATE_STUDENT_ID_TITLE
								.replace("<#studentId#>", studentIdLabelName)
								: Message.VALIDATE_STUDENT_ID_TITLE.replace(
										"<#studentId#>",
										Message.DEFAULT_STUDENT_ID_LABEL);
						String content = studentIdConfigurable ? Message.STUDENT_ID_UNUNIQUE_ERROR
								.replace("<#studentId#>", studentIdLabelName)
								: Message.STUDENT_ID_UNUNIQUE_ERROR.replace(
										"<#studentId#>",
										Message.DEFAULT_STUDENT_ID_LABEL);
								messageInfo = createMessageInfo(messageInfo, messageTitle, content, Message.ERROR, true, false );
					}

				}
			}
			if (! result)
			{           
				creatGson( req, resp, stream, messageInfo );
				return null;
			}        
			Boolean isMultiOrgAssociationValid = isMultiOrgAssociationValid(customerConfigurations);
			if(result && !isMultiOrgAssociationValid){
				if ( selectedOrgNodes.size() > 1 ) {
					if (isCreateNew) {
						messageInfo = createMessageInfo(messageInfo, Message.ADD_TITLE, Message.STUDENT_ASSIGNMENT_ERROR, Message.ERROR, true, false );
						//form.setMessage(Message.ADD_TITLE, Message.STUDENT_ASSIGNMENT_ERROR, Message.ERROR);
					}
					else {
						messageInfo = createMessageInfo(messageInfo, Message.EDIT_TITLE, Message.STUDENT_ASSIGNMENT_ERROR, Message.ERROR, true, false );
						//form.setMessage(Message.EDIT_TITLE, Message.STUDENT_ASSIGNMENT_ERROR, Message.ERROR);
					}
					creatGson( req, resp, stream, messageInfo );
					return null;
				}  
			}
			if(result) {
				studentId = saveStudentProfileInformation(isCreateNew, studentProfile, studentId, selectedOrgNodes);
	
				System.out.println("studentId==>"+studentId + "studentProfile.setFirstName" + studentProfile.getFirstName());	
				String demographicVisible = this.user.getCustomer().getDemographicVisible();
				if ((studentId != null) && demographicVisible.equalsIgnoreCase("T"))
				{
					result = saveStudentDemographic(isCreateNew, studentProfile, studentId);
				}
	
				if (studentId != null)
				{
					result = saveStudentAccommodations(isCreateNew, studentProfile, studentId, customerConfigurations);
				}

			}


			
			if (isCreateNew)
			{
				if (studentId != null)  {
					
					messageInfo = createMessageInfo(messageInfo, Message.ADD_TITLE, Message.ADD_SUCCESSFUL, Message.INFORMATION, false, true );
					//form.setMessage(Message.ADD_TITLE, Message.ADD_SUCCESSFUL, Message.INFORMATION);
				}
				else  {
					
					messageInfo = createMessageInfo(messageInfo, Message.ADD_TITLE, Message.ADD_ERROR, Message.INFORMATION, true, false );
					//form.setMessage(Message.ADD_TITLE, Message.ADD_ERROR, Message.INFORMATION);
				}
				
				
			}
			else
			{
				if (studentId != null) {
					messageInfo = createMessageInfo(messageInfo, Message.EDIT_TITLE, Message.EDIT_SUCCESSFUL, Message.INFORMATION, false, true );
					//form.setMessage(Message.EDIT_TITLE, Message.EDIT_SUCCESSFUL, Message.INFORMATION);
				}
				else  {
					messageInfo = createMessageInfo(messageInfo, Message.EDIT_TITLE, Message.EDIT_ERROR, Message.INFORMATION, true, false );
					//form.setMessage(Message.EDIT_TITLE, Message.EDIT_ERROR, Message.INFORMATION);
				}
			}
		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}	
			creatGson( req, resp, stream, messageInfo );
			return null;
		
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
	 * initialize
	 */
	private void initialize()
	{        
		getUserDetails();
		CustomerConfiguration[] customerConfigurations = getCustomerConfigurations();  
		StudentOperationForm  form = new StudentOperationForm();
		addEditDemographics();
		addEditAccommodations(customerConfigurations); 
		this.getRequest().setAttribute("viewOnly", Boolean.FALSE);  
		try{
			MusicFiles[] musicList = this.studentManagement.getMusicFiles();	
			this.getRequest().setAttribute("musicList", musicList);
		}catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		//initGradeGenderOptions(ACTION_ADD_STUDENT, savedForm, null, null);
		//this.savedForm.gradeOptions = getGradeOptions(ACTION_EDIT_STUDENT);
		//this.savedForm.genderOptions = getGenderOptions(ACTION_EDIT_STUDENT);
	}
	
	private void creatGson(HttpServletRequest req, HttpServletResponse resp, OutputStream stream, MessageInfo messageInfo ){
		
		try {
			try {
				Gson gson = new Gson();
				String json = gson.toJson(messageInfo);
				resp.setContentType("application/json");
				resp.flushBuffer();
				stream = resp.getOutputStream();
				stream.write(json.getBytes());

			} finally{
				if (stream!=null){
					stream.close();
				}
			}
			
		}
		catch (Exception e) {
			System.err.println("Exception while retrieving optionList.");
			e.printStackTrace();
		}
	}
	
	private MessageInfo createMessageInfo(MessageInfo messageInfo, String messageTitle, String content, String type, boolean errorflag, boolean successFlag){
		messageInfo.setTitle(messageTitle);
		messageInfo.setContent(content);
		messageInfo.setType(type);
		messageInfo.setErrorFlag(errorflag);
		messageInfo.setSuccessFlag(successFlag);
		return messageInfo;
	}

	/**
	 * saveStudentProfileInformation
	 */
	private Integer saveStudentProfileInformation(boolean isCreateNew, StudentProfileInformation studentProfile, Integer studentId, List selectedOrgNodes)
	{

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
	 * saveStudentDemographic
	 */
	private boolean saveStudentDemographic(boolean isCreateNew, StudentProfileInformation studentProfile, Integer studentId)
	{
		boolean studentImported = (studentProfile.getCreateBy().intValue() == 1);                
		if (studentImported)
		{        
			//prepareStudentDemographicForCustomerConfiguration();
		}        
		getStudentDemographicsFromRequest();        

		if (isCreateNew)
		{
			createStudentDemographics(studentId);
		}
		else
		{
			//updateStudentDemographics(studentId);
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
	 * getStudentDemographicsFromRequest
	 */
	private void getStudentDemographicsFromRequest() 
	{
		String param = null, paramValue = null;
		if(this.demographics == null)
			addEditDemographics();
		
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



	/*
	 * New method added for CR  ISTEP2011CR023.
	 */
	private boolean isMultiOrgAssociationValid(CustomerConfiguration[]  customerConfigurations) 
	{     
		boolean multiOrgAssociationValid = true;



		for (int i=0; i < customerConfigurations.length; i++)
		{
			CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Class_Reassignment") && cc.getDefaultValue().equalsIgnoreCase("T"))
			{
				multiOrgAssociationValid = false; 
				break;
			}
		}



		return multiOrgAssociationValid;
	}



	/*
	 * Added for TABE-BAUM 060: Unique Student ID. 
	 * This method checks unique student ID validation is required or not. 
	 * @param form RegistrationForm
	 * @return true if Student ID is unique
	 */
	private boolean isValidationForUniqueStudentIDRequired(StudentProfileInformation studentProfile, CustomerConfiguration[]  customerConfigurations ) {

		boolean validateUniqueStudentID = false;
		if (studentProfile==null || studentProfile.getStudentNumber() == null
				|| studentProfile.getStudentNumber().trim().length() == 0) {
			return false;
		}
		if (customerConfigurations != null) {
			for (CustomerConfiguration customerConfiguration : customerConfigurations) {
				if (customerConfiguration.getCustomerConfigurationName().trim()
						.equalsIgnoreCase("Unique_Student_ID")) {
					if (customerConfiguration.getDefaultValue() != null
							&& customerConfiguration.getDefaultValue().trim()
							.equalsIgnoreCase("T")) {
						validateUniqueStudentID = true;
						break;
					}
				}

			}
		}



		return validateUniqueStudentID;
	}


	/*
	 * Added for TABE-BAUM 060: Unique Student ID. 
	 * This method validate unique student ID. 
	 * @param isCreateNew boolean new student
	 * @param form RegistrationForm
	 * @return boolean isStudentIdUnique
	 */
	private boolean validateUniqueStudentId(boolean isCreateNew,Integer selectedStudentId,StudentProfileInformation studentProfile) {

		boolean isStudentIdUnique = false;
		try {
			isStudentIdUnique = this.studentManagement.validateUniqueStudentId(
					isCreateNew, customerId, selectedStudentId , studentProfile.getStudentNumber());
		} catch (CTBBusinessException e) {
			e.printStackTrace();
		}
		return isStudentIdUnique;
	}


	/**
	 * addEditDemographics
	 */
	private void addEditDemographics()
	{	
		List demographics = null;
		//Integer studentId = form.getStudentProfile().getStudentId();

		Integer studentId = 0;
		//boolean studentImported = (form.getStudentProfile().getCreateBy().intValue() == 1);
		boolean studentImported  = false;
		if ((demographics == null) && (studentId != null))
		{
			demographics = getStudentDemographics(studentId, demographics);
			prepareOnNullRule(demographics);            
		}
		else
		{
			/*if (studentImported)
			{        
				prepareStudentDemographicForCustomerConfiguration();
			}
			getStudentDemographicsFromRequest();*/
		}
		this.demographics = demographics;
		this.getRequest().setAttribute("demographics", demographics);       
		this.getRequest().setAttribute("studentImported", new Boolean(studentImported));       
	}

	/**
	 * getStudentDemographics
	 */
	private List getStudentDemographics(Integer studentId, List demographics)
	{
		demographics = new ArrayList();
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
					demographics.add(sd);                
				}                        
			}
		}
		catch (CTBBusinessException be)
		{
			be.printStackTrace();
		}

		return demographics;
	}

	/**
	 * prepareOnNullRule
	 */
	private void prepareOnNullRule( List demographics) 
	{
		for (int i=0; i < demographics.size(); i++)
		{
			StudentDemographic sdd = (StudentDemographic)demographics.get(i);
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
	 * getTestPurposeOptions
	 * // (LLO82) StudentManagement Changes For LasLink product
	 */
	private String [] getTestPurposeOptions(String action)
	{
		List options = new ArrayList();
		
		if ( action.equals(ACTION_ADD_STUDENT) || action.equals(ACTION_EDIT_STUDENT) )
			options.add(FilterSortPageUtils.FILTERTYPE_SELECT_A_TESTPURPOSE);

		options.add("Initial Placement");
		options.add("Annual Assessment");
		
		return (String [])options.toArray(new String[0]);        
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
	 * findByHierarchy
	 */
	private ManageStudentData findStudentByHierarchy()
	{      
		String treeOrgNodeId = getRequest().getParameter("treeOrgNodeId");
		Integer selectedOrgNodeId = null;
		if(treeOrgNodeId != null)
			selectedOrgNodeId = Integer.parseInt(treeOrgNodeId);
		ManageStudentData msData = null;

		FilterParams filter = null;
		PageParams page = null;
		SortParams sort = null;

		if (selectedOrgNodeId != null)
		{
			sort = FilterSortPageUtils.buildStudentSortParams(FilterSortPageUtils.LAST_NAME_SORT, FilterSortPageUtils.ASCENDING);
			msData = StudentSearchUtils.searchStudentsByOrgNode(this.userName, this.studentManagement, selectedOrgNodeId, filter, page, sort);

		}

		return msData;
	}



	/**
	 * initGradeGenderOptions
	 *//*
	private void initGradeGenderOptions(String action, StudentOperationForm form, String grade, String gender)
	{      
		String[] gradeOptions = null;
		String[] genderOptions = null;
		gradeOptions = getGradeOptions(action);

		this.getRequest().setAttribute("gradeOptions",gradeOptions);
		form.setGradeOptions(gradeOptions);
		if (grade != null)
			form.getStudentProfile().setGrade(grade);
		else
			form.getStudentProfile().setGrade(gradeOptions[0]);

		genderOptions = getGenderOptions( action );

		this.getRequest().setAttribute("genderOptions",genderOptions);
		form.setGenderOptions(genderOptions);
		if (gender != null)
			form.getStudentProfile().setGender(gender);
		else
			form.getStudentProfile().setGender(genderOptions[0]);
	}
*/

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
		{	if(this.userName != null ) {
			this.user = this.studentManagement.getUserDetails(this.userName, this.userName);     
			this.customerId = user.getCustomer().getCustomerId();
			Customer customer = this.user.getCustomer();
			Boolean supportAccommodations = Boolean.TRUE;   
            String hideAccommodations = customer.getHideAccommodations();
            if ((hideAccommodations != null) && hideAccommodations.equalsIgnoreCase("T"))
            {
                supportAccommodations = Boolean.FALSE;
            }
            this.getRequest().setAttribute("supportAccommodations", supportAccommodations); 
            
            
		}
		}
		catch (CTBBusinessException be)
		{
			be.printStackTrace();
		}        
		getSession().setAttribute("userName", this.userName);

		           
	}

	/**
	 * getCustomerConfigurations
	 */
	private CustomerConfiguration[] getCustomerConfigurations()
	{
		CustomerConfiguration[] customerConfigurations = null;
		try {
			customerConfigurations = this.studentManagement.getCustomerConfigurations(this.userName, this.customerId);

			this.getRequest().setAttribute("isBulkAccommodationConfigured",customerHasBulkAccommodation(customerConfigurations));
			this.getRequest().setAttribute("isScoringConfigured", customerHasScoring(customerConfigurations));
			this.getRequest().setAttribute("canRegisterStudent",canRegisterStudent(customerConfigurations));
			boolean isLasLinkCustomer = isLasLinkCustomer(customerConfigurations);
			this.getRequest().setAttribute("isLasLinkCustomer", isLasLinkCustomer);  
			this.getRequest().setAttribute("isTopLevelUser",isTopLevelUser(isLasLinkCustomer));
			this.getRequest().setAttribute("userHasReports", userHasReports());
			this.getRequest().setAttribute("isMandatoryBirthDate", isMandatoryBirthDate(customerConfigurations));
			isGeorgiaCustomer(customerConfigurations);
			
		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		return customerConfigurations;
	}


	/*
	 * GACRCT2010CR007- retrieve value for disableMandatoryBirthdate set  Value in request. 
	 */
	private boolean isMandatoryBirthDate(CustomerConfiguration[] customerConfigurations) 
	{     
		boolean disableMandatoryBirthdateValue = false;
		for (int i=0; i < customerConfigurations.length; i++)
		{
			CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Disable_Mandatory_Birth_Date") && cc.getDefaultValue().equalsIgnoreCase("T"))
			{
				disableMandatoryBirthdateValue = true; 
			}
		}
		return disableMandatoryBirthdateValue;


	}


	/*
	 * New method added for CR - GA2011CR001
	 * This method retrieve  the value of provide two customer configuration and their corresponding data in customer configuration value.
	 */
	private void isGeorgiaCustomer(CustomerConfiguration[] customerConfigurations) 
	{     
		boolean isStudentIdConfigurable = false;
		boolean isStudentId2Configurable = false;
		boolean isMandatoryStudentId = false;
		Integer configId=0;
		//START- GACR005 
		String []valueForStudentId = new String[8] ;
		String []valueForStudentId2 = new String[8] ;
		valueForStudentId[0] = "Student ID";
		valueForStudentId2[0] = "Student ID 2";
		//END- GACR005 
		for (int i=0; i < customerConfigurations.length; i++)
		{
			CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Student_ID_2") && cc.getDefaultValue().equalsIgnoreCase("T"))
			{
				isStudentId2Configurable = true; 
				configId = cc.getId();
				CustomerConfigurationValue[] customerConfigurationsValue = customerConfigurationValues(configId);
				valueForStudentId2 = new String[8];

				for(int j=0; j<customerConfigurationsValue.length; j++){

					int sortOrder = customerConfigurationsValue[j].getSortOrder();
					valueForStudentId2[sortOrder-1] = customerConfigurationsValue[j].getCustomerConfigurationValue();

				}
				//START- GACR005 
				//this.studentId2Configurable = isStudentId2Configurable;
				//form.setStudentId2Configurable(isStudentId2Configurable);
				//END- GACR005 
				valueForStudentId2 = getDefaultValue(valueForStudentId2,"Student ID 2");
			}
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Student_ID") && cc.getDefaultValue().equalsIgnoreCase("T"))
			{
				isStudentIdConfigurable = true; 
				configId = cc.getId();
				CustomerConfigurationValue[] customerConfigurationsValue = customerConfigurationValues(configId);
				//By default there should be 3 entries for customer configurations
				valueForStudentId = new String[8];
				for(int j=0; j<customerConfigurationsValue.length; j++){
					int sortOrder = customerConfigurationsValue[j].getSortOrder();
					valueForStudentId[sortOrder-1] = customerConfigurationsValue[j].getCustomerConfigurationValue();
				}	
				//START- GACR005 
				//this.studentIdConfigurable = isStudentIdConfigurable;
				//form.setStudentIdConfigurable(isStudentIdConfigurable);
				//END- GACR005 
				valueForStudentId = getDefaultValue(valueForStudentId,"Student ID");

			}

		}
		if(valueForStudentId.length == 8) {
			isMandatoryStudentId = valueForStudentId !=null &&  valueForStudentId[2] != null && valueForStudentId[2].equals("T") ?  true : false ;

		}
		this.getRequest().setAttribute("studentIdArrValue",valueForStudentId);
		this.getRequest().setAttribute("isStudentIdConfigurable",isStudentIdConfigurable);
		this.getRequest().setAttribute("isStudentId2Configurable",isStudentId2Configurable);
		this.getRequest().setAttribute("studentId2ArrValue",valueForStudentId2);
		this.getRequest().setAttribute("isMandatoryStudentId",isMandatoryStudentId);
	}


	/*
	 * 
	 * this method retrieve CustomerConfigurationsValue for provided customer configuration Id.
	 */
	private String[] getDefaultValue(String [] arrValue, String labelName)
	{
		arrValue[0] = arrValue[0] != null ? arrValue[0]   : labelName ;
		arrValue[1] = arrValue[1] != null ? arrValue[1]   : "32" ;

		if(labelName.equals("Student ID 2")){
			//this.studentId2LabelName = arrValue[0];
			try {
				arrValue[2] = (arrValue[2] != null && new Integer(arrValue[2]).intValue() > 0)? arrValue[2]   : "0" ;
				int minLength = Integer.valueOf(arrValue[2]);
			} catch (NumberFormatException nfe){
				arrValue[2] = "0" ;
			}
			//this.studentId2MinLength = arrValue[2];
			arrValue[3] = arrValue[3] != null ? arrValue[3]   : "AN" ;
			if(!arrValue[3].equals("NU") && !arrValue[3].equals("AN"))
			{ 
				arrValue[3]  = "AN";
			}
			//this.isStudentId2Numeric = arrValue[3];


		}
		if(labelName.equals("Student ID")){
			arrValue[2] = arrValue[2] != null ? arrValue[2]   : "F" ;
			if(!arrValue[2].equals("T") && !arrValue[2].equals("F"))
			{ 
				arrValue[2]  = "F";
			}
			//this.studentIdLabelName = arrValue[0];

			try {
				arrValue[3] = (arrValue[3] != null && new Integer(arrValue[3]).intValue() > 0)? arrValue[3]   : "0" ;
				int minLength = Integer.valueOf(arrValue[3]);
			} catch (NumberFormatException nfe){
				arrValue[3] = "0" ;
			}
			//this.studentIdMinLength = arrValue[3];
			arrValue[4] = arrValue[4] != null ? arrValue[4]   : "AN" ;
			if(!arrValue[4].equals("NU") && !arrValue[4].equals("AN"))
			{ 
				arrValue[4]  = "AN";
			}
			//this.isStudentIdNumeric = arrValue[4];

		}

		// check for numeric conversion of maxlength

		try {
			int maxLength = Integer.valueOf(arrValue[1]);
		} catch (NumberFormatException nfe){
			arrValue[1] = "32" ;
		}



		return arrValue;
	}


	/*
	 * 
	 * this method retrieve CustomerConfigurationsValue for provided customer configuration Id.
	 */
	private CustomerConfigurationValue[] customerConfigurationValues(Integer configId)
	{	
		CustomerConfigurationValue[] customerConfigurationsValue = null;
		try {
			customerConfigurationsValue = this.studentManagement.getCustomerConfigurationsValue(configId);

		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		return customerConfigurationsValue;
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
	 * This method checks whether customer is configured to access the scoring feature or not.
	 * @return Return Boolean 
	 */


	private Boolean customerHasScoring(CustomerConfiguration[] customerConfigurations)
	{               
		boolean hasScoringConfigurable = false;
		if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++)
			{
				CustomerConfiguration cc = (CustomerConfiguration) customerConfigurations[i];
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Hand_Scoring") && 
						cc.getDefaultValue().equals("T")	) {
					hasScoringConfigurable = true;
					break;
				} 
			}
		}

		return new Boolean(hasScoringConfigurable);
	}

	private Boolean canRegisterStudent(CustomerConfiguration[] customerConfigurations) 
	{               
		String roleName = this.user.getRole().getRoleName();        
		boolean validCustomer = false; 
		if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++)
			{
				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("TABE_Customer"))
				{
					validCustomer = true; 
				}               
			}
		}
		boolean validUser = (roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR) || roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR));
		return new Boolean(validCustomer && validUser);
	}
	private boolean isTopLevelUser(boolean isLasLinkCustomerVal){

		boolean isUserTopLevel = false;
		boolean isLaslinkUserTopLevel = false;
		boolean isLaslinkUser = false;
		isLaslinkUser = isLasLinkCustomerVal;
		try {
			if(isLaslinkUser) {
				isUserTopLevel = orgnode.checkTopOrgNodeUser(this.userName);	
				if(isUserTopLevel){
					isLaslinkUserTopLevel = true;				
				}
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return isLaslinkUserTopLevel;
	}


	private boolean isLasLinkCustomer(CustomerConfiguration[] customerConfigurations)
	{               
		boolean isLasLinkCustomer = false;
		if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++)
			{
				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
				//isLasLink customer
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("LASLINK_Customer") && cc.getDefaultValue().equals("T")	)
				{
					isLasLinkCustomer = true;
					break;
				} 
			}
		}
		return isLasLinkCustomer;
	}


	private String generateTree (ArrayList<Organization> orgNodesList) throws Exception{	

		Organization org = orgNodesList.get(0);
		TreeData td = new TreeData ();
		td.setData(org.getOrgName());
		td.getAttr().setId(org.getOrgNodeId().toString());
		td.getAttr().setCategoryID(org.getOrgCategoryLevel().toString());
		treeProcess (org,orgNodesList,td);
		BaseTree baseTree = new BaseTree ();
		baseTree.getData().add(td);
		Gson gson = new Gson();

		String json = gson.toJson(baseTree);

		return json;
	}

	private static void treeProcess (Organization org,List<Organization> list,TreeData td) {

		for (Organization tempOrg : list) {
			if (org.getOrgNodeId().equals(tempOrg.getOrgParentNodeId())) {
				TreeData tempData = new TreeData ();
				tempData.setData(tempOrg.getOrgName());
				tempData.getAttr().setId(tempOrg.getOrgNodeId().toString());
				tempData.getAttr().setCategoryID(tempOrg.getOrgCategoryLevel().toString());
				td.getChildren().add(tempData);
				treeProcess (tempOrg,list,tempData);
			}
		}
	}

	private static void preTreeProcess (ArrayList<TreeData> data,ArrayList<Organization> orgList) {

		Organization org = orgList.get(0);
		TreeData td = new TreeData ();
		td.setData(org.getOrgName());
		td.getAttr().setId(org.getOrgNodeId().toString());
		treeProcess (org,orgList,td);
		data.add(td);
	}

    /////////////////////////////////////////////////////////////////////////////////////////////    
	///////////////////////////// ACCOMODATION ///////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////    
	
	/**
	 * addEditAccommodations
	 */
	private void addEditAccommodations(CustomerConfiguration[] customerConfigurations)
	{
		Integer studentId = 0;
		boolean studentImported  = false;
		 
		if (accommodations == null)
		{
			accommodations = getStudentAccommodations(studentId, customerConfigurations);
		}
		else
		{
			//getStudentAccommodationsFromRequest();
		}
		this.getRequest().setAttribute("accommodations", this.accommodations);   
        
		
	}

	/**
	 * getStudentAccommodations
	 */
	private StudentAccommodationsDetail getStudentAccommodations(Integer studentId, CustomerConfiguration[] customerConfigurations)
	{
		StudentAccommodationsDetail accommodations = new StudentAccommodationsDetail();

		 if ((studentId != null) && (studentId.intValue() > 0))
		{
			/*try
			{    
				StudentAccommodations sa = this.studentManagement.getStudentAccommodations(this.userName, studentId);
				accommodations = new StudentAccommodationsDetail(sa);
			}
			catch (CTBBusinessException be)
			{
				be.printStackTrace();
			} */
		}
		else
		{
			setCustomerAccommodations(accommodations, true, customerConfigurations);
		}

	  accommodations.convertHexToText();


		return accommodations;
	}
	/**
	 * setCustomerAccommodations
	 */
	private void setCustomerAccommodations(StudentAccommodationsDetail sad, boolean isSetDefaultValue,  CustomerConfiguration[] customerConfigurations) 
	{        
		
		   
		// set checked value if there is configuration for this customer
		  for (int i=0; i < customerConfigurations.length; i++)
		  {
			CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
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
				//Added for Masking Ruler
				if (ccName.equalsIgnoreCase("Masking_Ruler"))
				{
					sad.setMaskingRuler(Boolean.TRUE);
				}
				//Added for Auditory Calming
				if (ccName.equalsIgnoreCase("Auditory_Calming"))
				{
					sad.setAuditoryCalming(Boolean.TRUE);
				}
				//Added for Magnifying Glass
				if (ccName.equalsIgnoreCase("Magnifying_Glass"))
				{
					sad.setMagnifyingGlass(Boolean.TRUE);
				}
				//Added for student pacing
				if (ccName.equalsIgnoreCase("Extended_Time"))
				{
					sad.setExtendedTime(Boolean.TRUE);
				}
				//Added for Masking Answers
				if (ccName.equalsIgnoreCase("Masking_Tool"))
				{
					sad.setMaskingTool(Boolean.TRUE);
				}
			}
		}
		  this.getRequest().setAttribute("customerConfigurations", customerConfigurations);    
			
	
}
	/**
	 * saveStudentAccommodation
	 */

	private boolean saveStudentAccommodations(boolean isCreateNew, StudentProfileInformation studentProfile, Integer studentId, CustomerConfiguration[]  customerConfigurations)
	{
			String hideAccommodations = this.user.getCustomer().getHideAccommodations();

		if (hideAccommodations.equalsIgnoreCase("T"))         
			getStudentDefaultAccommodations(customerConfigurations);
		else
			getStudentAccommodationsFromRequest(customerConfigurations);

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
			/*if (sa != null)
				updateStudentAccommodations(studentId, sa);
			else
				deleteStudentAccommodations(studentId);*/
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
	 * getStudentAccommodationsFromRequest
	 */
	private void getStudentAccommodationsFromRequest(CustomerConfiguration[]  customerConfigurations) 
	{
		// first get values from request
		String screenReader = getRequest().getParameter("screen_reader");
		String calculator = getRequest().getParameter("calculator");
		String highlighter = getRequest().getParameter("highlighter");
		String testPause = getRequest().getParameter("test_pause");
		String untimedTest = getRequest().getParameter("untimed_test");
		String colorFont = getRequest().getParameter("colorFont");
		String maskingRuler = getRequest().getParameter("Masking_Ruler"); //Added for Masking Ruler
		String auditoryCalming = getRequest().getParameter("Auditory_Calming"); //Added for Auditory Calming
		String magnifyingGlass = getRequest().getParameter("Magnifying_Glass"); //Added for Magnifying Glass
		String extendedTime = getRequest().getParameter("Extended_Time"); //Added for Student Pacing
		String maskingTool = getRequest().getParameter("Masking_Tool"); // Added for Masking Answers

		this.accommodations.setScreenReader(new Boolean(screenReader != null));
		this.accommodations.setCalculator(new Boolean(calculator != null));
		this.accommodations.setHighlighter(new Boolean(highlighter != null));
		this.accommodations.setTestPause(new Boolean(testPause != null));
		this.accommodations.setUntimedTest(new Boolean(untimedTest != null));
		this.accommodations.setColorFont(new Boolean(colorFont != null));
		this.accommodations.setAuditoryCalming(new Boolean(auditoryCalming != null));//Added for Auditory Calming
		this.accommodations.setMaskingRuler(new Boolean(maskingRuler != null));//Added for Masking Ruler
		this.accommodations.setMagnifyingGlass(new Boolean(magnifyingGlass != null));//Added for Magnifying Glass
		this.accommodations.setExtendedTime(new Boolean(extendedTime != null)); //Added for Student Pacing
		this.accommodations.setMaskingTool(new Boolean(maskingTool != null)); // Added for Masking Answers
		if(this.accommodations == null)
			addEditAccommodations(customerConfigurations);
		
		setCustomerAccommodations(this.accommodations, false, customerConfigurations);

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
		//Added for music files of Auditory Calming
		if(this.accommodations.getAuditoryCalming()){
			Integer musicFiles = Integer.parseInt(this.getRequest().getParameter("music_files"));
			if (musicFiles != null)
			{
				this.accommodations.setMusic_files(musicFiles);
			}
		}
	}

	/**
	 * getStudentDefaultAccommodations
	 */
	private void getStudentDefaultAccommodations(CustomerConfiguration[]  customerConfigurations) 
	{
		this.accommodations.setScreenReader(Boolean.FALSE);
		this.accommodations.setCalculator(Boolean.FALSE);
		this.accommodations.setTestPause(Boolean.FALSE);
		this.accommodations.setUntimedTest(Boolean.FALSE);
		this.accommodations.setHighlighter(Boolean.TRUE);
		this.accommodations.setColorFont(Boolean.FALSE);
		this.accommodations.setAuditoryCalming(Boolean.FALSE);//Added for Auditory Calming
		this.accommodations.setMaskingRuler(Boolean.FALSE);//Added for Masking Ruler
		this.accommodations.setMagnifyingGlass(Boolean.FALSE);//Added for Magnifying Glass
		this.accommodations.setExtendedTime(Boolean.FALSE); //Added for Student Pacing
		this.accommodations.setMaskingTool(Boolean.FALSE); // Added for Masking Answers

		setCustomerAccommodations(this.accommodations, true, customerConfigurations);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////    
	///////////////////////////// BEGIN OF NEW NAVIGATION ACTIONS ///////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////    
	
	/**
	 * ASSESSMENTS actions
	 */    
    @Jpf.Action()
	protected Forward assessments()
	{
        try
        {
            String url = "/TestSessionInfoWeb/homepage/assessments.do";
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
	        @Jpf.Forward(name = "organizationsLink", path = "organizations_manageOrganizations.do"),
	        @Jpf.Forward(name = "studentsLink", path = "organizations_manageStudents.do"),
	        @Jpf.Forward(name = "usersLink", path = "organizations_manageUsers.do")
	    }) 
	protected Forward organizations()
	{
		String menuId = (String)this.getRequest().getParameter("menuId");    	
		String forwardName = (menuId != null) ? menuId : "organizationsLink";
		System.out.println(forwardName);
		
	    return new Forward(forwardName);
	}
	
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success", path = "organizations_manageOrganizations.jsp") 
	    }) 
	protected Forward organizations_manageOrganizations()
	{
		initialize();
		this.getRequest().setAttribute("isFindStudent", Boolean.FALSE);

		return new Forward("success");
	}
	
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success", path = "beginFindStudent.do") 
	    }) 
	protected Forward organizations_manageStudents()
	{
	    return new Forward("success");
	}
	
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success", path = "organizations_manageUsers.jsp") 
	    }) 
	protected Forward organizations_manageUsers()
	{
		initialize();
		this.getRequest().setAttribute("isFindStudent", Boolean.FALSE);
		
	    return new Forward("success");
	}

    /**
     * REPORTS actions
     */    
    @Jpf.Action()
    protected Forward reports()
    {
        try
        {
            String url = "/TestSessionInfoWeb/homepage/reports.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
    }
	
	/**
	 * SERVICES actions
	 */    
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "manageLicensesLink", path = "services_manageLicenses.do"),
	        @Jpf.Forward(name = "installSoftwareLink", path = "services_installSoftware.do"),
	        @Jpf.Forward(name = "downloadTestLink", path = "services_downloadTest.do"),
	        @Jpf.Forward(name = "uploadDataLink", path = "services_uploadData.do"),
	        @Jpf.Forward(name = "downloadDataLink", path = "services_downloadData.do")
	    }) 
	protected Forward services()
	{
		String menuId = (String)this.getRequest().getParameter("menuId");    	
		String forwardName = (menuId != null) ? menuId : "manageLicensesLink";
		System.out.println(forwardName);
		
	    return new Forward(forwardName);
	}
	
	/***
    @Jpf.Action()
	protected Forward services_manageLicenses()
	{
        try
        {
            String url = "/OrganizationManagementWeb/manageLicense/services.do";
            getResponse().sendRedirect(url);
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
	}
	***/
    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "success", path = "blankPage.jsp") 
        }) 
    protected Forward services_manageLicenses()
    {
        return new Forward("success");
    }
	
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success", path = "blankPage.jsp") 
	    }) 
	protected Forward services_installSoftware()
	{
	    return new Forward("success");
	}
	
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success", path = "blankPage.jsp") 
	    }) 
	protected Forward services_downloadTest()
	{
	    return new Forward("success");
	}
	
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success", path = "blankPage.jsp") 
	    }) 
	protected Forward services_uploadData()
	{
	    return new Forward("success");
	}
	
	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success", path = "blankPage.jsp") 
	    }) 
	protected Forward services_downloadData()
	{
	    return new Forward("success");
	}

	/**
	 * BROADCAST MESSAGE actions
	 */    
	/**
	 * @jpf:action
	 */
	@Jpf.Action()
	protected Forward broadcastMessage()
	{
	    return null;
	}
	
	
	/**
	 * MYPROFILE actions
	 */    
	@Jpf.Action()
	protected Forward myProfile()
	{
	    return null;
	}

	

/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** MANAGESTUDENTFORM ************* /////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////    
	/**
	 * FormData get and set methods may be overwritten by the Form Bean editor.
	 */
	public static class StudentOperationForm extends SanitizedFormData
	{

	}	
}

