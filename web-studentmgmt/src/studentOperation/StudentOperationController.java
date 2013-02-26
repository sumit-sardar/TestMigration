package studentOperation;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import utils.Base;
import utils.BaseTree;
import utils.BroadcastUtils;
import utils.DateUtils;
import utils.FilterSortPageUtils;
import utils.MessageInfo;
import utils.OptionList;
import utils.Organization;
import utils.OrgnizationComparator;
import utils.PermissionsUtils;
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
import com.ctb.bean.testAdmin.BroadcastMessage;
import com.ctb.bean.testAdmin.BroadcastMessageData;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.Student;
import com.ctb.bean.testAdmin.StudentAccommodations;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.control.db.Students;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.studentManagement.StudentDataCreationException;
import com.ctb.exception.studentManagement.StudentDataDeletionException;
import com.ctb.util.SQLutils;
import com.ctb.util.studentManagement.DeleteStudentStatus;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.ctb.widgets.bean.ColumnSortEntry;
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

    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.BroadcastMessageLog message;

    
	private String userName = null;
	private Integer customerId = null;
	private User user = null;
	List demographics = null;
	Map<String, List> demographicMap = null;
	// student accommodations
	public StudentAccommodationsDetail accommodations = null;
	CustomerConfiguration[] customerConfigurations = null;
	boolean hasBioUneditable = false;
	Set<String> demoGraphicCategoryNames = null;




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
		CustomerConfiguration[] customerConfigurations = getCustomerConfigurations();
		List<String> demoCategoryNames = new ArrayList<String>();
		
		try {
			BaseTree baseTree = new BaseTree ();

			ArrayList<Organization> completeOrgNodeList = new ArrayList<Organization>();
			UserNodeData associateNode = StudentPathListUtils.populateAssociateNode(this.userName,this.studentManagement);
			ArrayList<Organization> selectedList  = StudentPathListUtils.buildassoOrgNodehierarchyList(associateNode);
			Collections.sort(selectedList, new OrgnizationComparator());
			Integer leafNodeCategoryId = StudentPathListUtils.getLeafNodeCategoryId(this.userName,this.customerId, this.studentManagement);
			ArrayList <Integer> orgIDList = new ArrayList <Integer>();
			ArrayList<TreeData> data = new ArrayList<TreeData>();

			UserNodeData und = StudentPathListUtils.OrgNodehierarchy(this.userName, 
					this.studentManagement, selectedList.get(0).getOrgNodeId()); 
			ArrayList<Organization> orgNodesList = StudentPathListUtils.buildOrgNodehierarchyList(und, orgIDList,completeOrgNodeList);	

			//jsonTree = generateTree(orgNodesList);

			for (int i= 0; i < selectedList.size(); i++) {

				if (i == 0) {

					preTreeProcess (data, orgNodesList, selectedList);

				} else {

					Integer nodeId = selectedList.get (i).getOrgNodeId();
					if (orgIDList.contains(nodeId)) {
						continue;
					} else if (!selectedList.get (i).getIsAssociate()) {
						
						continue;
						
					} else {

						orgIDList = new ArrayList <Integer>();
						UserNodeData undloop = StudentPathListUtils.OrgNodehierarchy(this.userName, 
								this.studentManagement,nodeId);   
						ArrayList<Organization> orgNodesListloop = StudentPathListUtils.buildOrgNodehierarchyList(undloop, orgIDList, completeOrgNodeList);	
						preTreeProcess (data, orgNodesListloop, selectedList);
					}
				}


			}

			Gson gson = new Gson();
			baseTree.setData(data);
			baseTree.setCustomerConfiguration(customerConfigurations);
			Boolean hasOOSConfigured = customerHasOOS(customerConfigurations);
			baseTree.setIsOOSConfigured(hasOOSConfigured);
			Collections.sort(baseTree.getData(), new Comparator<TreeData>(){

				public int compare(TreeData t1, TreeData t2) {
					return (t1.getData().toUpperCase().compareTo(t2.getData().toUpperCase()));
				}
					
			});
			baseTree.setLeafNodeCategoryId(leafNodeCategoryId);
			if(this.demoGraphicCategoryNames != null){
				for (Iterator iterator = this.demoGraphicCategoryNames.iterator(); iterator
						.hasNext();) {
					demoCategoryNames.add(iterator.next().toString());
					
				}
			}
			baseTree.setDemoCategory(demoCategoryNames);
			jsonTree = gson.toJson(baseTree);
			//System.out.println(jsonTree);
			String pattern = ",\"children\":[]";
			jsonTree = jsonTree.replace(pattern, "");

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

		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		List<StudentProfileInformation> studentList = new ArrayList<StudentProfileInformation>(0);
		String studentArray = "";
		StringBuffer  buffer= new StringBuffer("");
		String json = "";
		Map<Integer,Map> accomodationMap = new HashMap<Integer, Map>();
		
		try {
			System.out.println ("db process time Start:"+new Date());
			ManageStudentData msData = findStudentByHierarchy();
			System.out.println ("db process time End:"+new Date());
			
			if ((msData != null) && (msData.getFilteredCount().intValue() > 0))
			{
				System.out.println ("List process time Start:"+new Date());
				studentList = StudentSearchUtils.buildStudentList(msData, buffer,accomodationMap);
				studentArray = buffer.toString();
				System.out.println ("List process time End:"+new Date());
			}
			
			Base base = new Base();
			base.setPage("1");
			base.setRecords("10");
			base.setTotal("2");

			System.out.println("just b4 gson");	
			Gson gson = new Gson();
			
			base.setStudentProfileInformation(studentList);
			base.setStudentIdArray(studentArray);
			base.setAccomodationMap(accomodationMap);
			json = gson.toJson(base);
			


			
			try{
/*				resp.setContentType("application/json");
				stream = resp.getOutputStream();
				resp.flushBuffer();
				stream.write(json.getBytes());
*/
				resp.setContentType(CONTENT_TYPE_JSON);
	    		stream = resp.getOutputStream();

	    		String acceptEncoding = req.getHeader("Accept-Encoding");
	    		System.out.println("acceptEncoding..."+acceptEncoding.toString());

	    		if (acceptEncoding != null && acceptEncoding.contains("gzip")) {
	    		    resp.setHeader("Content-Encoding", "gzip");
	    		    stream = new GZIPOutputStream(stream);
	    		}
	    		
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
	protected Forward getStudentCountForOrgNode(StudentOperationForm form){

		String jsonResponse = "";
		OutputStream stream = null;
		Integer treeOrgNodeId = Integer.parseInt(getRequest().getParameter("treeOrgNodeId"));
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			Integer studentCount = 0;
			studentCount = StudentSearchUtils.getStudentsCountForOrgNode(this.userName, this.studentManagement, treeOrgNodeId);
			System.out.println("treeOrgNodeId:"+treeOrgNodeId);
			System.out.println("studentCount:"+studentCount);
			try {
				Gson gson = new Gson();
				String json = gson.toJson(studentCount);
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
			optionList.setNotTestingOption(getNotTestingOptions(ACTION_ADD_STUDENT));
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
	protected Forward saveAddEditStudent(StudentOperationForm form)
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
		studentProfile.setUserName(getRequest().getParameter("loginId"));
		studentProfile.setMonth(getRequest().getParameter("monthOptions"));
		studentProfile.setDay(getRequest().getParameter("dayOptions"));
		studentProfile.setYear(getRequest().getParameter("yearOptions"));
		studentProfile.setGender(getRequest().getParameter("genderOptions"));
		studentProfile.setNotTesting(getRequest().getParameter("notTestingOptions"));
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
		Integer studentId = new Integer(0);
		String studentIdVal = getRequest().getParameter("selectedStudentId");
		if(studentIdVal != "" && studentIdVal !=null)
			studentId = Integer.parseInt(studentIdVal);
		String isAddStudent = getRequest().getParameter("isAddStudent");
		boolean isCreateNew = (isAddStudent == null || isAddStudent.equals("") || isAddStudent.equals("true")) ? true : false;
		Integer createBy = new Integer(0);
		String createByVal = getRequest().getParameter("createBy");
		if(!createByVal.equals("") && createByVal != null)
			createBy = Integer.parseInt(createByVal);
		studentProfile.setCreateBy(createBy);
		
		boolean result = true;
		
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
				studentId = saveStudentProfileInformation(isCreateNew, studentProfile, studentId, selectedOrgNodes, messageInfo);
				
				System.out.println("studentId==>"+studentId + "studentProfile.setFirstName" + studentProfile.getFirstName());	
				String demographicVisible = this.user.getCustomer().getDemographicVisible();
				if ((studentId != null) && demographicVisible.equalsIgnoreCase("T"))
				{
					result = saveStudentDemographic(isCreateNew, studentProfile, studentId);
				}
	
				if (studentId != null)
				{
					result = saveStudentAccommodations(isCreateNew, studentProfile, studentId, customerConfigurations, messageInfo);
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
		
			creatGson( req, resp, stream, messageInfo );
			return null;
		
	}

	//Added for color font preview button
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
	
	//Added for edit student
	
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="find_user_by_hierarchy.jsp")
	})
	protected Forward getStudentDataForSelectedStudent(){
		String jsonResponse = "";
		OutputStream stream = null;
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		int studentId = Integer.parseInt(req.getParameter("studentID"));
		int createBy = Integer.parseInt(req.getParameter("createBy"));
		System.out.println(studentId);
		//Boolean profileEditable = isProfileEditable(createBy);
		//StudentProfileInformation studentProfileData = handleAddEdit(studentId, profileEditable, createBy);
		StudentProfileInformation studentProfileData = StudentSearchUtils.getStudentProfileInformation(this.studentManagement, this.userName, studentId);
		Boolean profileEditable = isProfileEditable(studentProfileData.getCreateBy());
		List demographics = null;
		studentProfileData.setStuDemographic(getStudentDemographics(studentId, demographics));
		//this.getRequest().setAttribute("demographics", this.demographics);       
		//this.getRequest().setAttribute("studentImported", new Boolean(studentImported));       
		StudentAccommodationsDetail accommodations = null;
		studentProfileData.setStuAccommodation(getStudentAccommodations(studentId, customerConfigurations));
		try {
			
			OptionList optionList = new OptionList();
			optionList.setGradeOptions(getGradeOptions(ACTION_ADD_STUDENT));
			optionList.setGenderOptions(getGenderOptions(ACTION_ADD_STUDENT));
			optionList.setNotTestingOption(getNotTestingOptions(ACTION_ADD_STUDENT));
			optionList.setMonthOptions( DateUtils.getMonthOptions());
			optionList.setDayOptions(DateUtils.getDayOptions());
			optionList.setYearOptions(DateUtils.getYearOptions());
			optionList.setTestPurposeOptions(getTestPurposeOptions(ACTION_EDIT_STUDENT));
		    
			optionList.setProfileEditable(profileEditable);
			
			studentProfileData.setOptionList(optionList);
			if(studentProfileData.getCreateBy() != null && studentProfileData.getCreateBy() == 1) {
				studentProfileData.setStudentImported(true);
			} else {
				studentProfileData.setStudentImported(false);
			}
			
			if(this.hasBioUneditable) {
				boolean tobeDisabled = isStudentExtracted(studentId);
				//System.out.println("org_node_id -->" + tobeDisabled);
				studentProfileData.setStudentExtracted(tobeDisabled);
			}
			
			try {
				Gson gson = new Gson();
				String json = gson.toJson(studentProfileData);
				System.out.println(json);
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
	protected Forward getViewStudentData(){
		String jsonResponse = "";
		OutputStream stream = null;
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		int studentId = Integer.parseInt(req.getParameter("studentID"));
		String stuCreatedBy = req.getParameter("createBy");
		int createBy = 0;
		if(stuCreatedBy != null && !("".equals(stuCreatedBy)))
			createBy = Integer.parseInt(req.getParameter("createBy"));
		//System.out.println(studentId);
		boolean studentImported = (createBy == 1);
		StudentProfileInformation studentProfile = StudentSearchUtils.getStudentProfileInformation(this.studentManagement, this.userName, studentId);
		
		
		try {
			
			List demographics = null;
			studentProfile.setStuDemographic(getStudentDemographics(studentId, demographics));
			//this.getRequest().setAttribute("demographics", this.demographics);       
			//this.getRequest().setAttribute("studentImported", new Boolean(studentImported));       
			StudentAccommodationsDetail accommodations = null;
			studentProfile.setStuAccommodation(getStudentAccommodations(studentId, customerConfigurations));
			//this.getRequest().setAttribute("accommodations", this.accommodations);       

			//this.getRequest().setAttribute("viewOnly", Boolean.TRUE);       

			//this.getRequest().setAttribute("disableColorSelection", Boolean.TRUE);       
			if (this.demographics.size() == 0) {
				//this.getRequest().setAttribute("demographicVisible", "F");       
			}
			try {
				Gson gson = new Gson();
				String json = gson.toJson(studentProfile);
				System.out.println(json);
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
		getLoggedInUserPrincipal();
		
		getUserDetails();
		
		setupUserPermission();
		
		StudentOperationForm  form = new StudentOperationForm();
		demographics = null;
		demographicMap = null;
		accommodations = null;
		addEditDemographics(new Integer(0), null);
		addEditAccommodations(customerConfigurations, null); 
		this.getRequest().setAttribute("viewOnly", Boolean.FALSE); 
		
		String roleName = this.user.getRole().getRoleName();
		this.getRequest().setAttribute("showEditButton", PermissionsUtils.showEditButton(roleName));
		this.getRequest().setAttribute("showDeleteButton", PermissionsUtils.showDeleteButton(roleName));
		
		List broadcastMessages = BroadcastUtils.getBroadcastMessages(this.message, this.userName);
        this.getSession().setAttribute("broadcastMessages", new Integer(broadcastMessages.size()));
				
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
	private Integer saveStudentProfileInformation(boolean isCreateNew, StudentProfileInformation studentProfile, Integer studentId, List selectedOrgNodes, MessageInfo messageInfo)
	{

		ManageStudent student = studentProfile.makeCopy(studentId, selectedOrgNodes);

		try
		{                    
			if (isCreateNew)
			{
				Student studentdetail = this.studentManagement.createNewStudent(this.userName, student);
				studentId = studentdetail.getStudentId();
				messageInfo.setStudentLoginId(studentdetail.getUserName());
				messageInfo.setStudentId(studentId);
				
			}
			else
			{
				this.studentManagement.updateStudent(this.userName, student);
				messageInfo.setStudentLoginId(student.getLoginId());
				messageInfo.setStudentId(student.getId());
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
		
		getStudentDemographicsFromRequest();
		
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
		this.demographicMap = null;
		return true;
	}
	
	
	/**
	 * createStudentDemographics
	 */
	private void createStudentDemographics(Integer studentId)
	{	
		String valueName=null;
		String value=null;
		String demoLabel = null;
		String selected = null;
		int demoCount=0;
		String subEthnicity=null;
		if ((studentId != null) && (studentId.intValue() > 0) && (this.demographics != null))
		{
			
			try
			{ 
				for (int j=0; j < demographics.size(); j++)
				{
					StudentDemographic sdd1 = (StudentDemographic)demographics.get(j);
					StudentDemographicValue[] sdv0 = sdd1.getStudentDemographicValues();
					//demographics.
					demoLabel = sdd1.getLabelName();
					for (int l=0; l < sdv0.length; l++){
						StudentDemographicValue sdv3 = (StudentDemographicValue)sdv0[l];
						 valueName = sdv3.getValueName().trim();
						 //value =sdv2.getValueCode().trim();
						 selected = sdv3.getSelectedFlag();
						if ((demoLabel.equals("Sub_Ethnicity") || demoLabel.equals("Ethnicity")) && selected.equals("true") )
						{
							demoCount++;
						}
						if (demoLabel.equals("Sub_Ethnicity")&& selected.equals("true"))
						 {
							 subEthnicity = sdv3.getValueName(); 
							 sdv3.setSelectedFlag("false");
						 }
					}
					
				}
				if (demoCount == 2)
				{
					for (int i=0; i < demographics.size(); i++)
					{
						StudentDemographic sdd2 = (StudentDemographic)demographics.get(i);
						StudentDemographicValue[] sdv = sdd2.getStudentDemographicValues();
						for (int k=0; k < sdv.length; k++){
							 StudentDemographicValue sdv2 = (StudentDemographicValue)sdv[k];
							 valueName = sdv2.getValueName().trim();
							 
							 //value =sdv2.getValueCode().trim();
							 //selected = sdv2.getSelectedFlag();
							 if (valueName.equals("Hispanic or Latino") && selected.equals("true"))
							 {
								 //sdv[k].setSelectedFlag("false");
								 sdv[k].setValueName(subEthnicity);
								 sdv[k].setValueCode(subEthnicity);
							 }
						}
						
					}
				}
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
			String valueName=null;
			String value=null;
			String demoLabel = null;
			String selected = null;
			int demoCount=0;
			String subEthnicity = null;
			try
			{  
				for (int j=0; j < demographics.size(); j++)
				{
					StudentDemographic sdd1 = (StudentDemographic)demographics.get(j);
					StudentDemographicValue[] sdv0 = sdd1.getStudentDemographicValues();
					//demographics.
					demoLabel = sdd1.getLabelName();
					for (int l=0; l < sdv0.length; l++){
						StudentDemographicValue sdv3 = (StudentDemographicValue)sdv0[l];
						 valueName = sdv3.getValueName().trim();
						 //value =sdv2.getValueCode().trim();
						 selected = sdv3.getSelectedFlag();
						if ((demoLabel.equals("Sub_Ethnicity") || demoLabel.equals("Ethnicity")) && selected.equals("true") )
						{
							demoCount++;
						}
						if (demoLabel.equals("Sub_Ethnicity")&& selected.equals("true"))
						 {
							 subEthnicity = sdv3.getValueName(); 
							 sdv3.setSelectedFlag("false");
						 }
					}
					
				}
				if (demoCount == 2)
				{
					for (int i=0; i < demographics.size(); i++)
					{
						StudentDemographic sdd2 = (StudentDemographic)demographics.get(i);
						StudentDemographicValue[] sdv = sdd2.getStudentDemographicValues();
						for (int k=0; k < sdv.length; k++){
							 StudentDemographicValue sdv2 = (StudentDemographicValue)sdv[k];
							 valueName = sdv2.getValueName().trim();
							 //value =sdv2.getValueCode().trim();
							 selected = sdv2.getSelectedFlag();
							 if (valueName.equals("Hispanic or Latino") && selected.equals("true"))
							 {
								 //sdv[k].setSelectedFlag("false");
								 sdv[k].setValueName(subEthnicity);
								 sdv[k].setValueCode(subEthnicity);
							 }
						}
						
					}
				}
				
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
	 * getStudentDemographicsFromRequest
	 */
	private void getStudentDemographicsFromRequest() 
	{
		String param = null, paramValue = null;
		String groupName = "";
		if(this.demographics == null)
			addEditDemographics(new Integer(0), null);
		
		for (int i=0; i < this.demographics.size(); i++)
		{
			StudentDemographic sdd = (StudentDemographic)this.demographics.get(i);
			if(null != sdd.getDemoCategory())
				groupName = "_" + sdd.getDemoCategory();
			else
				groupName = "";
			StudentDemographicValue[] values = sdd.getStudentDemographicValues();
			
			for (int j=0; j < values.length; j++)
			{
				StudentDemographicValue sdv = (StudentDemographicValue)values[j];

				// Look up the parameter based on checkbox vs radio/select
				if (sdd.getMultipleAllowedFlag().equals("true"))
				{
					if (! sdv.getVisible().equals("false"))
						sdv.setSelectedFlag("false");
					if(sdv.getValueName().equals("puertorriqueno"))
						sdv.setValueName("puertorriqueño");
					if(sdv.getValueCode().equals("puertorriqueno"))
						sdv.setValueCode("puertorriqueño");
					param = sdd.getLabelName() + "_" + sdv.getValueName() + groupName ;
					if (getRequest().getParameter(param) != null)
					{
						paramValue = getRequest().getParameter(param);
						sdv.setSelectedFlag("true");
					}
				} 
				else
				{
					if(sdd.getLabelName().equals("Sub_Ethnicity")) {
						if (! sdv.getVisible().equals("false"))
							sdv.setSelectedFlag("false");
						if(sdv.getValueName().equals("puertorriqueno"))
							sdv.setValueName("puertorriqueño");
						if(sdv.getValueCode().equals("puertorriqueno"))
							sdv.setValueCode("puertorriqueño");
						param = sdd.getLabelName() + "_" + sdv.getValueName() + groupName;
						if (getRequest().getParameter(param) != null)
						{
							paramValue = getRequest().getParameter(param);
							sdv.setSelectedFlag("true");
						}
					} else {
						if (values.length == 1)
						{
							if (! sdv.getVisible().equals("false"))
								sdv.setSelectedFlag("false");
							if(sdv.getValueName().equals("puertorriqueno"))
								sdv.setValueName("puertorriqueño");
							if(sdv.getValueCode().equals("puertorriqueno"))
								sdv.setValueCode("puertorriqueño");
							param = sdd.getLabelName() + "_" + sdv.getValueName() + groupName;
							if (getRequest().getParameter(param) != null)
							{
								paramValue = getRequest().getParameter(param);
								sdv.setSelectedFlag("true");
							}
						}
						else
						{
							param = sdd.getLabelName() + groupName;
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
	private List addEditDemographics(Integer studentId, Integer createBy)
	{	
		//List demographics = null;
		boolean studentImported = false;
		boolean customerDemographicGroupingEnable = false;
		 if(createBy != null)
			 studentImported = (createBy.intValue() == 1);
		
		
		if ((this.demographics == null) && (studentId != null))
		{
			demographics = getStudentDemographics(studentId, demographics);
			prepareOnNullRule(demographics);            
		}
		else
		{
			if (studentImported)
			{        
				prepareStudentDemographicForCustomerConfiguration();
			}
			getStudentDemographicsFromRequest();
		}
		//this.demographics = demographics;
		this.getRequest().setAttribute("demographics", demographics);       
		this.getRequest().setAttribute("studentImported", new Boolean(studentImported)); 
		customerDemographicGroupingEnable = isCustomerDemographicGroupingEnable();
		this.getRequest().setAttribute("customerDemographicGroupingEnable", customerDemographicGroupingEnable); 
		if(customerDemographicGroupingEnable){
			this.getRequest().setAttribute("okDemographicMap", populateOKDemographics(demographics));
		}
		return demographics;
	}
	
	private void populateDemoCategory(String key){
		String divID = "";
		if(key != ""|| key != null){
			divID = key;
			divID = divID.trim();
			divID = divID.replaceAll(" ","_");  
		}		
		demoGraphicCategoryNames.add(divID);
	}
	
	
	private Map<String,List> populateOKDemographics(List<StudentDemographic> demographics) {
		
		Map<String,List> tempDemographicMap = new HashMap<String,List>();
		demographicMap = new HashMap<String,List>();
		demoGraphicCategoryNames = new TreeSet<String>();
		String demographicCategoryName = null;
		for(StudentDemographic demographic : demographics) {
			demographicCategoryName = demographic.getDemoCategory();
			if(null == tempDemographicMap.get(demographicCategoryName)){
				List dempgraphicsList = new ArrayList();
				dempgraphicsList.add(demographic);
				tempDemographicMap.put(demographicCategoryName, dempgraphicsList);
			}
			else {
				tempDemographicMap.get(demographicCategoryName).add(demographic);
			}
	
			List dempgraphicsList = null;
			for(String studentDemographicCategoryName : tempDemographicMap.keySet()){
				dempgraphicsList = tempDemographicMap.get(studentDemographicCategoryName);				
				demographicMap.put(studentDemographicCategoryName, dempgraphicsList);
				populateDemoCategory(studentDemographicCategoryName);
			}
			
		}
//		for(StudentDemographic demographic : demographics) {
//			if("Ethnicity".equals(demographic.getLabelName())
//					||"Race".equals(demographic.getLabelName())
//					||"NFAY".equals(demographic.getLabelName())
//					||"Alt Ed Academy".equals(demographic.getLabelName())
//					||"Migrant".equals(demographic.getLabelName())
//					||"Title X, Part C".equals(demographic.getLabelName())
//					||"Free/Reduced Lunch".equals(demographic.getLabelName())
//					||"Distance Learning".equals(demographic.getLabelName())
//					||"Grade Level Repeat Test Taker".equals(demographic.getLabelName())
//					||"504".equals(demographic.getLabelName())
//					||"IEP".equals(demographic.getLabelName())
//					||"ELL 1st or 2nd year proficient".equals(demographic.getLabelName())
//					||"ELL".equals(demographic.getLabelName())){
//				additionalInfoList.add(demographic);
//			}
//			if("ELL Accommodation - Translator".equals(demographic.getLabelName())
//					||"ELL Accommodation - Transcribe".equals(demographic.getLabelName())
//					||"ELL Accommodation – Clarification/Read Aloud".equals(demographic.getLabelName())
//					||"ELL Accommodation – Grouping/Sessions".equals(demographic.getLabelName())
//					||"ELL Accommodation - W/W Dictionary".equals(demographic.getLabelName())
//					||"Paper Tester Gr 6-8 OCCT READING".equals(demographic.getLabelName())
//					||"OMAAP Reading".equals(demographic.getLabelName())
//					||"IEP or 504 Accommodation - Setting".equals(demographic.getLabelName())
//					||"IEP or 504 Accommodation – Timing/Scheduling".equals(demographic.getLabelName())
//					||"IEP or 504 Accommodation – Response".equals(demographic.getLabelName())
//					||"IEP".equals(demographic.getLabelName())
//					||"ELL 1st or 2nd year proficient".equals(demographic.getLabelName())
//					||"ELL".equals(demographic.getLabelName())){
//				readingInfoList.add(demographic);
//			}
//			if("ELL Accommodation - Translator".equals(demographic.getLabelName())
//					||"ELL Accommodation - Transcribe".equals(demographic.getLabelName())
//					||"ELL Accommodation – Clarification/Read Aloud".equals(demographic.getLabelName())
//					||"ELL Accommodation – Grouping/Sessions".equals(demographic.getLabelName())
//					||"ELL Accommodation - W/W Dictionary".equals(demographic.getLabelName())
//					||"Paper Tester Gr 6-8 OCCT MATH".equals(demographic.getLabelName())
//					||"OMAAP Mathematics".equals(demographic.getLabelName())
//					||"IEP or 504 Accommodation - Setting".equals(demographic.getLabelName())
//					||"IEP or 504 Accommodation – Timing/Scheduling".equals(demographic.getLabelName())
//					||"IEP or 504 Accommodation – Response".equals(demographic.getLabelName())
//					||"IEP".equals(demographic.getLabelName())
//					||"ELL 1st or 2nd year proficient".equals(demographic.getLabelName())
//					||"ELL".equals(demographic.getLabelName())){
//				mathematicsInfoList.add(demographic);
//			}
//			
//		}
//		demographicMap.put("Additional Info", additionalInfoList);
//		demographicMap.put("Reading", readingInfoList);
//		demographicMap.put("Mathematics", mathematicsInfoList);
		
		return demographicMap;
	}
	//is demographic group config enable for customer
	private boolean isCustomerDemographicGroupingEnable()
    {    
        boolean demographicGrouping = false;
		for (int i=0; i < this.customerConfigurations.length; i++)
        {
        	CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];        	
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("demographic_grouping") && cc.getDefaultValue().equals("T")	)
            {
            	demographicGrouping = true;
                break;
            } 
        }
       
       return demographicGrouping;
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
					
					if(sd.getStudentDemographicValues() != null && sd.getStudentDemographicValues().length > 0){
						for (int j = 0; j < sd.getStudentDemographicValues().length; j++) {
							if("puertorriqueño".equals(sd.getStudentDemographicValues()[j].getValueCode())){
								sd.getStudentDemographicValues()[j].setValueCode("puertorriqueno");
							}
							if("puertorriqueño".equals(sd.getStudentDemographicValues()[j].getValueName())){
								sd.getStudentDemographicValues()[j].setValueName("puertorriqueno");
							}
						}
					}
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
   	 	SortParams sort = FilterSortPageUtils.buildSortParams("LastName",ColumnSortEntry.ASCENDING, null, null);

		if (selectedOrgNodeId != null)
		{
			msData = StudentSearchUtils.getStudentsMinimalInfoForSelectedOrgNode(this.userName, this.studentManagement, selectedOrgNodeId, filter, page, sort);

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
	 * getNotTestingOptions
	 */
	private String [] getNotTestingOptions(String action)
	{
		List options = new ArrayList();
		options.add("Please Select");
		options.add("No");
		options.add("Yes");

		return (String [])options.toArray(new String[0]);        
	}


	/**
	 * getUserDetails
	 */
	private void getUserDetails()
	{
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
		getSession().setAttribute("createdBy", this.user.getUserId());
	}

	/**
	 * getCustomerConfigurations
	 */
	private CustomerConfiguration[] getCustomerConfigurations()
	{
		CustomerConfiguration[] customerConfigurations = null;
		try {
			customerConfigurations = this.studentManagement.getCustomerConfigurations(this.userName, this.customerId);
		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		return customerConfigurations;
	}


	/*
	 * GACRCT2010CR007- retrieve value for disableMandatoryBirthdate set  Value in request. 
	 */
	/*private boolean isMandatoryBirthDate(CustomerConfiguration[] customerConfigurations) 
	{     
		boolean mandatoryBirthdateValue = true;
		for (int i=0; i < customerConfigurations.length; i++)
		{
			CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
			if (cc.getCustomerConfigurationName().equalsIgnoreCase("Disable_Mandatory_Birth_Date") && cc.getDefaultValue().equalsIgnoreCase("T"))
			{
				mandatoryBirthdateValue = false; 
			}
		}
		return mandatoryBirthdateValue;


	}*/


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
		String []valueForStudentId = new String[8] ;
		String []valueForStudentId2 = new String[8] ;
		valueForStudentId[0] = "Student ID";
		valueForStudentId2[0] = "Student ID 2";
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
	/*private Boolean customerHasBulkAccommodation(CustomerConfiguration[] customerConfigurations) 
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
	}*/
	
	/**
	 * Bulk Move
	 */
	/*private Boolean customerHasBulkMove(CustomerConfiguration[] customerConfigurations) 
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
	}*/
	
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
		return hasOOSConfigurable;           
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


	/*private String generateTree (ArrayList<Organization> orgNodesList, ArrayList<Organization> selectedList) throws Exception{	

		Organization org = orgNodesList.get(0);
		TreeData td = new TreeData ();
		td.setData(org.getOrgName());
		td.getAttr().setId(org.getOrgNodeId().toString());
		td.getAttr().setCid(org.getOrgCategoryLevel().toString());
		//treeProcess (org,orgNodesList,td, selectedList);
		BaseTree baseTree = new BaseTree ();
		baseTree.getData().add(td);
		Gson gson = new Gson();

		String json = gson.toJson(baseTree);

		return json;
	}*/

	private static void treeProcess (Organization org,List<Organization> list,TreeData td, 
    		ArrayList<Organization> selectedList, Integer rootCategoryLevel, 
    		Map<Integer, Organization> orgMap) {

		Integer treeLevel = 0;
		Organization parentOrg = null;
		for (Organization tempOrg : list) {
			if (org.getOrgNodeId().equals(tempOrg.getOrgParentNodeId())) {
				
				if (selectedList.contains(tempOrg)) {
					
					int index = selectedList.indexOf(tempOrg);
					if (index != -1) {
						
						Organization selectedOrg = selectedList.get(index);
						selectedOrg.setIsAssociate(false);
					}
					
				}
				TreeData tempData = new TreeData ();
				tempData.setData(tempOrg.getOrgName());
				tempData.getAttr().setId(tempOrg.getOrgNodeId().toString());
				tempData.getAttr().setCid(tempOrg.getOrgCategoryLevel().toString());
				parentOrg = orgMap.get(tempOrg.getOrgParentNodeId());
				treeLevel = parentOrg.getTreeLevel() + 1;
				tempOrg.setTreeLevel(treeLevel);
				tempData.getAttr().setTcl(treeLevel.toString());
				td.getChildren().add(tempData);
				orgMap.put(tempOrg.getOrgNodeId(), tempOrg);
				treeProcess (tempOrg, list, tempData, selectedList, rootCategoryLevel, orgMap);
			}
		}
	}

	private static void preTreeProcess (ArrayList<TreeData> data,ArrayList<Organization> orgList, ArrayList<Organization> selectedList) {

		Organization org = orgList.get(0);
		Integer rootCategoryLevel = 0;
		TreeData td = new TreeData ();
		td.setData(org.getOrgName());
		td.getAttr().setId(org.getOrgNodeId().toString());
		td.getAttr().setCid(org.getOrgCategoryLevel().toString());
		rootCategoryLevel = org.getOrgCategoryLevel();
		td.getAttr().setTcl("1");
		org.setTreeLevel(1);
		Map<Integer, Organization> orgMap = new HashMap<Integer, Organization>();
		orgMap.put(org.getOrgNodeId(), org);
		treeProcess (org, orgList, td, selectedList, rootCategoryLevel, orgMap);
		data.add(td);
	}

    /////////////////////////////////////////////////////////////////////////////////////////////    
	///////////////////////////// ACCOMODATION ///////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////    
	
	/**
	 * addEditAccommodations
	 */
	private StudentAccommodationsDetail addEditAccommodations(CustomerConfiguration[] customerConfigurations, Integer createBy)
	{
		Integer studentId = 0;
		boolean studentImported  = false;
		 if(createBy != null)
			 studentImported = (createBy.intValue() == 1);
		 
		if (accommodations == null)
		{
			accommodations = getStudentAccommodations(studentId, customerConfigurations);
		}
		else
		{
			getStudentAccommodationsFromRequest(customerConfigurations, createBy);
		}
		this.getRequest().setAttribute("accommodations", this.accommodations);   
        
		
		return accommodations;
		
		
		
	}

	/**
	 * getStudentAccommodations
	 */
	private StudentAccommodationsDetail getStudentAccommodations(Integer studentId, CustomerConfiguration[] customerConfigurations)
	{
		StudentAccommodationsDetail accommodations = new StudentAccommodationsDetail();

		 if ((studentId != null) && (studentId.intValue() > 0))
		{
			try
			{    
				StudentAccommodations sa = this.studentManagement.getStudentAccommodations(this.userName, studentId);
				accommodations = new StudentAccommodationsDetail(sa);
			}
			catch (CTBBusinessException be)
			{
				be.printStackTrace();
			} 
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
				//Added for microphone and headphone
				if (ccName.equalsIgnoreCase("Microphone_Headphone"))
				{
					sad.setMicrophoneHeadphone(Boolean.TRUE);
				}
			}
		}
		  
			
	
}
	/**
	 * saveStudentAccommodation
	 */

	private boolean saveStudentAccommodations(boolean isCreateNew, StudentProfileInformation studentProfile, Integer studentId, CustomerConfiguration[]  customerConfigurations, MessageInfo messageInfo)
	{
			String hideAccommodations = this.user.getCustomer().getHideAccommodations();

		if (hideAccommodations.equalsIgnoreCase("T"))         
			getStudentDefaultAccommodations(customerConfigurations);
		else
			getStudentAccommodationsFromRequest(customerConfigurations, studentProfile.getCreateBy());

		StudentAccommodations sa = this.accommodations.makeCopy(studentId);
		messageInfo.setHasAccommodation(studentHasAccommodation(sa));
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
	
	public String studentHasAccommodation(StudentAccommodations sa){
		 String hasAccommodations = "No";
	        if( "T".equals(sa.getScreenMagnifier()) ||
	            "T".equals(sa.getScreenReader()) ||
	            "T".equals(sa.getCalculator()) ||
	            "T".equals(sa.getTestPause()) ||
	            "T".equals(sa.getUntimedTest()) ||
	            "T".equals(sa.getHighlighter()) ||
	            "T".equals(sa.getExtendedTime()) ||
	            (sa.getMaskingRuler() != null && !sa.getMaskingRuler().equals("") && !sa.getMaskingRuler().equals("F"))||
	            (sa.getExtendedTime() != null && !sa.getExtendedTime().equals("") && !sa.getExtendedTime().equals("F")) || 
	            (sa.getAuditoryCalming() != null && !sa.getAuditoryCalming().equals("") && !sa.getAuditoryCalming().equals("F")) || 
	            (sa.getMagnifyingGlass() != null && !sa.getMagnifyingGlass().equals("") && !sa.getMagnifyingGlass().equals("F")) || 
	            (sa.getMaskingTool() != null && !sa.getMaskingTool().equals("") && !sa.getMaskingTool().equals("F")) ||
	            (sa.getMicrophoneHeadphone() != null && !sa.getMicrophoneHeadphone().equals("") && !sa.getMicrophoneHeadphone().equals("F")) || 
	            sa.getQuestionBackgroundColor() != null ||
	            sa.getQuestionFontColor() != null ||
	            sa.getQuestionFontSize() != null ||
	            sa.getAnswerBackgroundColor() != null ||
	            sa.getAnswerFontColor() != null ||
	            sa.getAnswerFontSize() != null)
	        	hasAccommodations = "Yes";
	       
	       
	   return hasAccommodations;
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
	private void getStudentAccommodationsFromRequest(CustomerConfiguration[]  customerConfigurations , Integer createBy) 
	{
		if(this.accommodations == null)
			addEditAccommodations(customerConfigurations, createBy);
		
		// first get values from request
		String screenReader = getRequest().getParameter("screenReader");
		String calculator = getRequest().getParameter("calculator");
		String highlighter = getRequest().getParameter("highlighter");
		String testPause = getRequest().getParameter("testPause");
		String untimedTest = getRequest().getParameter("untimedTest");
		String colorFont = getRequest().getParameter("colorFont");
		String maskingRuler = getRequest().getParameter("MaskingRuler"); //Added for Masking Ruler
		String auditoryCalming = getRequest().getParameter("AuditoryCalming"); //Added for Auditory Calming
		String magnifyingGlass = getRequest().getParameter("MagnifyingGlass"); //Added for Magnifying Glass
		String extendedTime = getRequest().getParameter("ExtendedTime"); //Added for Student Pacing
		String maskingTool = getRequest().getParameter("MaskingTool"); // Added for Masking Answers
		String microphoneHeadphone = getRequest().getParameter("MicrophoneHeadphone");//Added for Microphone and Headphone

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
		this.accommodations.setMicrophoneHeadphone(new Boolean(microphoneHeadphone != null));
		
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
			String selecteMusicFile = this.getRequest().getParameter("music_files");
			if((selecteMusicFile == null) || selecteMusicFile.equals("")) {
				selecteMusicFile = "1";
			}
			Integer musicFiles = Integer.parseInt(selecteMusicFile);
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
		this.accommodations.setMicrophoneHeadphone(Boolean.FALSE); // Added for microphone and headphone
		
		setCustomerAccommodations(this.accommodations, true, customerConfigurations);
	}

	/**
	 * @jpf:action
	 */
	@Jpf.Action()
	protected Forward deleteStudent() throws Exception
	{
		String studentIdStr = (String)this.getRequest().getParameter("studentID");
		if(this.userName == null)
			throw new Exception();
		
		if (studentIdStr != null) {
			Integer studentId = new Integer(studentIdStr);
			String deleteStatus = "Student has been deleted successfully.";
						
			try {                    
				DeleteStudentStatus status = this.studentManagement.deleteStudent(this.userName, studentId);
			}
			catch (StudentDataDeletionException sde) {
				deleteStatus = sde.getMessage();
			}        
			catch (CTBBusinessException be) {
				deleteStatus = be.getMessage();
			}  
			catch (Exception e) {
				deleteStatus = "Failed to delete this student.";
			}  
			
			
			HttpServletResponse resp = getResponse();
			OutputStream stream = null;

			try {
				Gson gson = new Gson();
				String json = gson.toJson(deleteStatus);
				resp.setContentType("application/json");
				resp.flushBuffer();
				stream = resp.getOutputStream();
				stream.write(json.getBytes());

				/*resp.setContentType(CONTENT_TYPE_JSON);
				resp.flushBuffer();
				stream = resp.getOutputStream();
				stream.write(deleteStatus.getBytes());*/
				if (stream != null) {
					stream.close();
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}			

		}    
		return null;
	}
	
	private boolean isStudentExtracted (Integer studentId) {
		boolean flag = false;
		
		try {
			flag = this.studentManagement.getIsStudentExtracted(studentId);
		} catch (CTBBusinessException e) {
			e.printStackTrace();
		}
		
		return flag;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////    
	///////////////////////////// BEGIN OF NEW NAVIGATION ACTIONS ///////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////    
	
	/**
	 * ASSESSMENTS actions
	 */    
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "sessionsLink", path = "assessments_sessionsLink.do"),
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
		String forwardName = (menuId != null) ? menuId : "studentsLink";
		
	    return new Forward(forwardName);
	}
	

	@Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success", path = "beginFindStudent.do") 
	    }) 
	protected Forward organizations_manageStudents()
	{
	    return new Forward("success");
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
     * SERVICES actions
     */    
    @Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "resetTestSessionLink", path = "services_resetTestSession.do"),
    		@Jpf.Forward(name = "manageLicensesLink", path = "services_manageLicenses.do"),
    		@Jpf.Forward(name = "installSoftwareLink", path = "services_installSoftware.do"),
    		@Jpf.Forward(name = "downloadTestLink", path = "services_downloadTest.do"),
    		@Jpf.Forward(name = "uploadDataLink", path = "services_uploadData.do"),
    		@Jpf.Forward(name = "downloadDataLink", path = "services_downloadData.do"),
    		@Jpf.Forward(name = "viewStatusLink", path = "services_viewStatus.do"),
    		@Jpf.Forward(name = "exportDataLink", path = "services_dataExport.do"),
    		@Jpf.Forward(name = "showAccountFileDownloadLink", path = "eMetric_user_accounts_detail.do")
    }) 
    protected Forward services()
    {
    	String menuId = (String)this.getRequest().getParameter("menuId");    	
    	String forwardName = (menuId != null) ? menuId : "installSoftwareLink";

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
    protected Forward services_dataExport()
    {
    	try
    	{
    		String url = "/ExportWeb/dataExportOperation/services_dataExport.do";
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
	protected Forward services_viewStatus()
	{
        try
        {
            String url = "/ExportWeb/dataExportOperation/beginViewStatus.do";
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

	
	@Jpf.Action()
	protected Forward myProfile()
	{
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
    
private void setUpAllUserPermission(CustomerConfiguration [] customerConfigurations) {
    	
    	boolean hasBulkStudentConfigurable = false;
    	boolean hasBulkStudentMoveConfigurable = false;
    	boolean hasOOSConfigurable = false;
    	boolean adminUser = isAdminUser();
    	boolean hasUploadConfig = false;
    	boolean hasDownloadConfig = false;
    	boolean hasUploadDownloadConfig = false;
    	boolean hasProgramStatusConfig = false;
    	boolean hasScoringConfigurable = false;
    	boolean hasLicenseConfig = false;
    	String roleName = this.user.getRole().getRoleName();
    	boolean TABECustomer = false;
    	boolean mandatoryBirthdateValue = true;
    	boolean multiOrgAssociationValid = true;
    	boolean adminCoordinatorUser = isAdminCoordinatotUser(); //For Student Registration
    	boolean hasResetTestSession = false;
    	boolean hasResetTestSessionForAdmin = false;
    	boolean isOKCustomer = false;
    	boolean isGACustomer = false;
    	boolean isTopLevelAdmin = new Boolean(isTopLevelUser() && isAdminUser());
    	boolean laslinkCustomer = false;
    	boolean hasLockHierarchyEdit = false;
    	String hierarchyLockLevel = "-1";
    	
		if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++) {

				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
				// For Bulk Accommodation
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Bulk_Accommodation") && 
						cc.getDefaultValue().equals("T")) {
					hasBulkStudentConfigurable = true;
					continue;
				}
				// For Bulk Student Move
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Bulk_Move_Students") && 
						cc.getDefaultValue().equals("T")) {
					hasBulkStudentMoveConfigurable = true;
					continue;
				}
				// For Out Of School Student
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("OOS_Configurable") && 
						cc.getDefaultValue().equals("T")) {
					hasOOSConfigurable = true;
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
				// For Program Status
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Program_Status") && 
						cc.getDefaultValue().equals("T")) {
					hasProgramStatusConfig = true;
					continue;
				}
				// For Hand Scoring
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Hand_Scoring") && 
	            		cc.getDefaultValue().equals("T")	) {
					hasScoringConfigurable = true;
					continue;
	            }
				// For License
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Subscription") && 
	            		cc.getDefaultValue().equals("T")	) {
					hasLicenseConfig = true;
					continue;
	            }
				// For TABE Customer
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("TABE_Customer")) {
	            	TABECustomer = true;
	            	continue;
	            }
				//For Date Of Birth
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Disable_Mandatory_Birth_Date") && 
						cc.getDefaultValue().equalsIgnoreCase("T")) {
					mandatoryBirthdateValue = false;
					continue;
				}
				//For multi org node association
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Class_Reassignment") && 
						cc.getDefaultValue().equalsIgnoreCase("T")) {
					multiOrgAssociationValid = false;
					continue;
				}
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Reopen_Subtest") && 
	            		cc.getDefaultValue().equals("T")	) {
					hasResetTestSession = true;
					continue;
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
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Laslink_Customer")) {
	            	laslinkCustomer = true;
	            	continue;
	            }
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Lock_Hierarchy_Edit")) {
	            	hasLockHierarchyEdit = true;
	            	hierarchyLockLevel = cc.getDefaultValue();
	            }
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Disable_StudentBio_On_Export") 
						&& cc.getDefaultValue().equalsIgnoreCase("T")) {
					this.hasBioUneditable = true;
					continue;
				}
			}
			
		}
		
		if (hasUploadConfig && hasDownloadConfig) {
			hasUploadDownloadConfig = true;
		}
		if (hasUploadDownloadConfig) {
			hasUploadConfig = false;
			hasDownloadConfig = false;
		}
		
		this.getSession().setAttribute("isBulkAccommodationConfigured",new Boolean(hasBulkStudentConfigurable));
		this.getSession().setAttribute("isBulkMoveConfigured",new Boolean(hasBulkStudentMoveConfigurable));
		this.getSession().setAttribute("isOOSConfigured",new Boolean(hasOOSConfigurable));
		this.getSession().setAttribute("hasUploadConfigured",new Boolean(hasUploadConfig && adminUser));
		this.getSession().setAttribute("hasDownloadConfigured",new Boolean(hasDownloadConfig && adminUser));
		this.getSession().setAttribute("hasUploadDownloadConfigured",new Boolean(hasUploadDownloadConfig && adminUser));
		this.getSession().setAttribute("hasProgramStatusConfigured",new Boolean(hasProgramStatusConfig && adminUser));
		this.getSession().setAttribute("hasScoringConfigured",new Boolean(hasScoringConfigurable));
		this.getSession().setAttribute("hasLicenseConfigured",new Boolean(hasLicenseConfig));
		this.getSession().setAttribute("adminUser", new Boolean(adminUser));
		boolean validUser = (roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR) || 
        		roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR));
		//this.getSession().setAttribute("canRegisterStudent", new Boolean(TABECustomer && validUser));
        this.getSession().setAttribute("canRegisterStudent", false);//Temporary change to hide register student button
		
		this.getRequest().setAttribute("isMandatoryBirthDate", mandatoryBirthdateValue);
		boolean addDeleteStudentEnabled = addDeleteStudentEnable(roleName);
		this.getSession().setAttribute("addStudentEnable", addDeleteStudentEnabled);
		this.getSession().setAttribute("deleteStudentEnable", addDeleteStudentEnabled);
		this.getSession().setAttribute("isClassReassignable",multiOrgAssociationValid);	// Changes for defect #67959 imported student's organization
		this.getSession().setAttribute("hasRapidRagistrationConfigured", new Boolean(TABECustomer && (adminUser || adminCoordinatorUser) ));//For Student Registration
		
		this.getRequest().setAttribute("isLasLinkCustomer", laslinkCustomer);
		//System.out.println(laslinkCustomer);
     	this.getSession().setAttribute("showDataExportTab",laslinkCustomer);
     	this.getSession().setAttribute("hasResetTestSession", new Boolean((hasResetTestSession && hasResetTestSessionForAdmin) && ((isOKCustomer && isTopLevelAdmin)||(laslinkCustomer && isTopLevelAdmin)||(isGACustomer && adminUser))));
		
     	//show Account file download link      	
     	this.getSession().setAttribute("isAccountFileDownloadVisible", new Boolean(laslinkCustomer && isTopLevelAdmin));
     	this.getRequest().setAttribute("hasLockHierarchyEditConfigured", hasLockHierarchyEdit);
     	this.getRequest().setAttribute("hierarchyLockLevel", hierarchyLockLevel);
     	
     	//Disable student bio if exported once from a org
     	this.getSession().setAttribute("hasBioUneditable", this.hasBioUneditable);

}

	private boolean isAdminCoordinatotUser() //For Student Registration
	{               
		String roleName = this.user.getRole().getRoleName();        
		return roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR); 
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
    
	private void setupUserPermission()
	{
		customerConfigurations = getCustomerConfigurations();  

        boolean laslinkCustomer = isLaslinkCustomer(customerConfigurations);
        
        setUpAllUserPermission(customerConfigurations);
        
        this.getSession().setAttribute("showReportTab", 
        		new Boolean(userHasReports().booleanValue() || laslinkCustomer));
    	
    	this.getRequest().setAttribute("isLasLinkCustomer", laslinkCustomer);  
    	
    	this.getRequest().setAttribute("isTopLevelUser",isTopLevelUser(laslinkCustomer));

		isGeorgiaCustomer(customerConfigurations);
		
		this.getRequest().setAttribute("customerConfigurations", customerConfigurations);


	
		this.getSession().setAttribute("showDataExportTab",laslinkCustomer);

	}


    private boolean isAdminUser() 
    {               
        String roleName = this.user.getRole().getRoleName();        
        return roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR); 
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
    
    private boolean addDeleteStudentEnable(String roleName) 
    {        
        return (roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR) ||
                roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR) ||
                roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOUNT_MANAGER));
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////    
    ///////////////////////////// END OF SETUP USER PERMISSION ///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////    

    
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
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
        
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

