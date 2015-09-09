package bulkOperation;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import utils.BroadcastUtils;
import utils.Base;
import utils.BaseTree;
import utils.FilterSortPageUtils;
import utils.MessageInfo;
import utils.Organization;
import utils.OrgnizationComparator;
import utils.PermissionsUtils;
import utils.Row;
import utils.StudentPathListUtils;
import utils.StudentSearchUtils;
import utils.TreeData;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.request.FilterParams.FilterParam;
import com.ctb.bean.request.FilterParams.FilterType;
import com.ctb.bean.studentManagement.CustomerConfiguration;
import com.ctb.bean.studentManagement.CustomerConfigurationValue;
import com.ctb.bean.studentManagement.CustomerDemographic;
import com.ctb.bean.studentManagement.ManageBulkStudentData;
import com.ctb.bean.studentManagement.MusicFiles;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.SessionStudent;
import com.ctb.bean.testAdmin.StudentAccommodations;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.exception.CTBBusinessException;
import com.google.gson.Gson;

import dto.Message;
import dto.StudentAccommodationsDetail;

@Jpf.Controller(simpleActions = { @Jpf.SimpleAction(name = "begin", path = "index.jsp") })
public class BulkOperationController extends PageFlowController {
	private static final long serialVersionUID = 1L;
	
	@Control()
	private com.ctb.control.studentManagement.StudentManagement studentManagement;

	@Control()
	private com.ctb.control.db.OrgNode orgnode;

    @Control()
    private com.ctb.control.db.BroadcastMessageLog message;
    
	
	private String userName = null;
	private Integer customerId = null;
	private User user = null;
	List demographics = null;
	// student accommodations
	public StudentAccommodationsDetail accommodations = null;
	CustomerConfiguration[] customerConfigurations = null;
	CustomerDemographic [] customerDemographic = null;
	public String[] realDemographicOptions = null;
	public static String CONTENT_TYPE_JSON = "application/json";
	public static String UNDER_SCORE = "_";
	
	/* Changes for DEX Story - Add intermediate screen : Start */
    private boolean isEOIUser = false;
	private boolean isMappedWith3_8User = false;
	private boolean is3to8Selected = false;
	private boolean isEOISelected = false;
	private boolean isUserLinkSelected = false;
   /* Changes for DEX Story - Add intermediate screen : End */
	
	private boolean isEngradeCustomer = false;
	
	/**
	 * This method represents the point of entry into the pageflow
	 * @jpf:action
	 * @jpf:forward name="success" path="findStudent.do"
	 */
	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="findStudent.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "add_bulk_student_accommodation.jsp")
	}, 
	validationErrorForward = @Jpf.Forward(name = "failure",
			path = "logout.do"))
	protected Forward beginAddBulkStudent()
	{
		if(getSession().getAttribute("is3to8Selected") == null)
			this.is3to8Selected = (getRequest().getParameter("is3to8Selected") != null && "true".equalsIgnoreCase(getRequest().getParameter("is3to8Selected").toString()))? true: false; 
    	if(getSession().getAttribute("isEOISelected") == null)
    		this.isEOISelected = (getRequest().getParameter("isEOISelected") != null && "true".equalsIgnoreCase(getRequest().getParameter("isEOISelected").toString()))? true: false;
    	if(getSession().getAttribute("isUserLinkSelected") == null)
    		this.isUserLinkSelected = (getRequest().getParameter("isUserLinkSelected") != null && "true".equalsIgnoreCase(getRequest().getParameter("isUserLinkSelected").toString()))? true: false;

		initialize();
		return new Forward("success");
	}
	
	 @Jpf.Action(forwards={
	    		@Jpf.Forward(name = "success", 
						path ="assessments_sessions.jsp")
		})
	    protected Forward getStudentForSelectedNode(){
	    	
			String jsonTree = "";
			HttpServletRequest req = getRequest();
			HttpServletResponse resp = getResponse();
			OutputStream stream = null;
			String contentType = CONTENT_TYPE_JSON;
			String json = "";
			ObjectOutput output = null;
			String studentArray = "";
			FilterParams demoFilter = null;
			PageParams studentPage = null;
	        SortParams studentSort = null;
			String treeOrgNodeId = getRequest().getParameter("stuForOrgNodeId");
			Integer selectedOrgNodeId = null;
			if(treeOrgNodeId != null)
				selectedOrgNodeId = Integer.parseInt(treeOrgNodeId);
			String demoFilter1 = getRequest().getParameter("demoFilter1");
			String demoFilter2 = getRequest().getParameter("demoFilter2");
			String demoFilter3 = getRequest().getParameter("demoFilter3");
			try {
				if(demoFilter1 != null && demoFilter2 != null && demoFilter3 != null  
						&& !(demoFilter1.equals("") && demoFilter2.equals("") && demoFilter3.equals(""))) {
					
				String selectedDemo1 = (demoFilter1.equals("")?  FilterSortPageUtils.FILTERTYPE_SHOWALL : (demoFilter1.split(UNDER_SCORE))[0].trim());
				String selectedDemoValue1 = (demoFilter1.equals("")?  FilterSortPageUtils.FILTERTYPE_SHOWALL : ((demoFilter1.split(UNDER_SCORE))[1].trim().equals("All")?  FilterSortPageUtils.FILTERTYPE_SHOWALL :(demoFilter1.split(UNDER_SCORE))[1].trim()));
				String selectedDemo2 = (demoFilter2.equals("")?  FilterSortPageUtils.FILTERTYPE_SHOWALL : (demoFilter2.split(UNDER_SCORE))[0].trim());
				String selectedDemoValue2 = (demoFilter2.equals("")?  FilterSortPageUtils.FILTERTYPE_SHOWALL : ((demoFilter2.split(UNDER_SCORE))[1].trim().equals("All")?  FilterSortPageUtils.FILTERTYPE_SHOWALL :(demoFilter2.split(UNDER_SCORE))[1].trim()));
				String selectedDemo3 = (demoFilter3.equals("")?  FilterSortPageUtils.FILTERTYPE_SHOWALL : (demoFilter3.split(UNDER_SCORE))[0].trim());
				String selectedDemoValue3 = (demoFilter3.equals("")?  FilterSortPageUtils.FILTERTYPE_SHOWALL : ((demoFilter3.split(UNDER_SCORE))[1].trim().equals("All")?  FilterSortPageUtils.FILTERTYPE_SHOWALL :(demoFilter3.split(UNDER_SCORE))[1].trim()));
				
				demoFilter = new FilterParams();
				ArrayList demoFilters = new ArrayList();
				if (selectedDemo1 != null && !selectedDemo1.equals(FilterSortPageUtils.FILTERTYPE_SHOWALL))
				{
					String [] arg = new String[1];
					arg[0] = selectedDemo1;
					if (selectedDemoValue1 != null && !selectedDemoValue1.equals(FilterSortPageUtils.FILTERTYPE_SHOWALL))
					{
						String [] valueArg = new String[1];
						valueArg[0] = arg[0]+ "_" + selectedDemoValue1;
						demoFilters.add(new FilterParam("ValueName", valueArg, FilterType.EQUALS));
					} else {
						demoFilters.add(new FilterParam("LabelName", arg, FilterType.EQUALS));
					}

				}

				if (selectedDemo2 != null && !selectedDemo2.equals(FilterSortPageUtils.FILTERTYPE_SHOWALL))
				{
					String [] arg = new String[1];
					arg[0] = selectedDemo2;
					if (selectedDemoValue2 != null && !selectedDemoValue2.equals(FilterSortPageUtils.FILTERTYPE_SHOWALL))
					{
						String [] valueArg = new String[1];
						valueArg[0] = arg[0]+ "_" + selectedDemoValue2;
						demoFilters.add(new FilterParam("ValueName", valueArg, FilterType.EQUALS));
					} else {
						demoFilters.add(new FilterParam("LabelName", arg, FilterType.EQUALS));
					}

				}

				if (selectedDemo3 != null && !selectedDemo3.equals(FilterSortPageUtils.FILTERTYPE_SHOWALL))
				{
					String [] arg = new String[1];
					arg[0] = selectedDemo3;
					if (selectedDemoValue3 != null && !selectedDemoValue3.equals(FilterSortPageUtils.FILTERTYPE_SHOWALL))
					{
						String [] valueArg = new String[1];
						valueArg[0] = arg[0]+ "_" + selectedDemoValue3;
						demoFilters.add(new FilterParam("ValueName", valueArg, FilterType.EQUALS));
					} else {
						demoFilters.add(new FilterParam("LabelName", arg, FilterType.EQUALS));
					}
				}
				demoFilter.setFilterParams((FilterParam[])demoFilters.toArray(new FilterParam[0]));
			}
	        studentSort = FilterSortPageUtils.buildStudentSortParams(FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN_LAST_NAME, FilterSortPageUtils.ASCENDING);
	        // get students - getSessionStudents
	        ManageBulkStudentData msData = StudentSearchUtils.searchBulkStudentsByOrgNode(this.userName, this.studentManagement, selectedOrgNodeId, null, demoFilter, studentPage, studentSort);

	        Map studentMap = buildStudentList(msData);
	        List studentNodes = (List)studentMap.get("studentList");
	        Map<Integer,Integer> studentIdIndexer = (Map<Integer,Integer>)studentMap.get("studentIdIndexer");
	        
	        studentArray = StudentSearchUtils.buildStudentListString(msData);
			Base base = new Base();
			base.setPage("1");
			base.setRecords("10");
			base.setTotal("2");
			List <Row> rows = new ArrayList<Row>();
			base.setStudentNode(studentNodes);
			base.setStudentIdArray(studentArray);
			base.setStudentIdIndexer(studentIdIndexer);
			
			Gson gson = new Gson();
			//System.out.println ("Json process time Start:"+new Date());
			json = gson.toJson(base);
			//System.out.println ("Json process time End:"+new Date() +".."+json);
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
		} catch (Exception e) {
			System.err.println("Exception while processing CR response.");
			e.printStackTrace();
		}

		return null;

	}
	
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="find_student_hierarchy.jsp")
	})
	protected Forward userOrgNodeHierarchyList(){

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
			Collections.sort(baseTree.getData(), new Comparator<TreeData>(){

				public int compare(TreeData t1, TreeData t2) {
					return (t1.getData().toUpperCase().compareTo(t2.getData().toUpperCase()));
				}
					
			});
			baseTree.setLeafNodeCategoryId(leafNodeCategoryId);
			baseTree.setGradeOptions(getGradeOptions());
			baseTree.setCustomerDemographicList(buildCustomerDemographicList());
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
	

	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="goToViewStudent.do" 
	 * @jpf:forward name="error" path="addEditStudent.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success", 
					path = "find_student_hierarchy.jsp")
	})
	protected Forward saveBulkStudentData()
	{   
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String studentIds = getRequest().getParameter("studentIds");
		String[] selectedStudents = null;
		Integer[] studentId = null;
		
		if(studentIds != null) {
			studentIds = studentIds.substring(studentIds.indexOf("{") + 1,studentIds.lastIndexOf("}"));
			selectedStudents = studentIds.split(",");
			if(selectedStudents != null && selectedStudents.length > 0 ) {
				studentId = new Integer[selectedStudents.length];
				for(int i=0;i<selectedStudents.length; i++){
					String[] tempSelectedStudent = selectedStudents[i].split(":");
					selectedStudents[i] = tempSelectedStudent[1];
					studentId[i] = Integer.parseInt(selectedStudents[i]);
				}
			}
			
		}
			
		
		 MessageInfo messageInfo = new MessageInfo();
		
			boolean successFlag=true;
			if(selectedStudents != null && selectedStudents.length > 0 ) {
				/*Integer[] studentId = new Integer[selectedStudents.length];
				//studentId = Arrays.copyOf(selectedStudents, selectedStudents.length, Integer[].class);
				
				for(int i=0;i< selectedStudents.length;i++)
				{
					studentId[i] = Integer.parseInt(selectedStudents[i]);
					

				}*/
				if(studentId != null && studentId.length > 0) {
					StudentAccommodations sa = saveAccommodationsSelected();
					if (sa != null) {
						try {
							
							this.studentManagement.updateBulkStudentAccommodations(this.userName,sa,studentId);
						} catch (CTBBusinessException e) {
							// TODO Auto-generated catch block
							successFlag=false;
						}
						

						if(successFlag) {
							messageInfo = createMessageInfo(messageInfo, Message.BULK_ADD_TITLE, Message.BULK_ADD_SUCCESSFUL, Message.INFORMATION, false, true );
							messageInfo = createMessageInfo(messageInfo, Message.SELECTED_STUDENT_COUNT, String.valueOf(studentId.length), Message.ADDITIONAL_INFORMATION, false, true );
						}
						else {
							messageInfo = createMessageInfo(messageInfo, Message.BULK_ADD_TITLE, Message.BULK_ADD_ERROR, Message.INFORMATION, true, false );
						}
						
					} else {
						
							messageInfo = createMessageInfo(messageInfo, Message.BULK_ADD_TITLE, Message.BULK_ACCOM_NOTSELECTED, Message.ALERT, true, false );
					}
					
				}
			}
		

			creatGson( req, resp, stream, messageInfo );
			return null;

	}

	public StudentAccommodations saveAccommodationsSelected()
	{	
		boolean isAccommodationSelected = false;
		String screenReader = getRequest().getParameter("screen_reader");
		String calculator = getRequest().getParameter("calculator");
		String testPause = getRequest().getParameter("test_pause");
		String untimedTest = getRequest().getParameter("untimed_test");
		String highLighter = getRequest().getParameter("highlighter");
		String colorFont = getRequest().getParameter("colorFont");
		String questionBgrdColor = this.getRequest().getParameter("question_bgrdColor");

		String questionFontColor = this.getRequest().getParameter("question_fontColor");

		String answerBgrdColor = this.getRequest().getParameter("answer_bgrdColor");


		String answerFontColor = this.getRequest().getParameter("answer_fontColor");


		String fontSize = this.getRequest().getParameter("fontSize");

		StudentAccommodations stuAommodations = new StudentAccommodations();
		StudentAccommodationsDetail stdDetail = new StudentAccommodationsDetail();

		//generate dynamic sql query for update
		StringBuffer result = new StringBuffer();
		if (screenReader != null ) {
			stuAommodations.setScreenReader(screenReader);
			isAccommodationSelected = true;
		}

		if (calculator != null) {
			stuAommodations.setCalculator(calculator);
			isAccommodationSelected = true;
		}	 

		if (testPause != null ) {
			stuAommodations.setTestPause(testPause);
			isAccommodationSelected = true;
		}

		if (untimedTest != null ) {
			stuAommodations.setUntimedTest(untimedTest);
			isAccommodationSelected = true;
		}

		if (highLighter != null) {
			stuAommodations.setHighlighter(highLighter);
			isAccommodationSelected = true;
		}

		if (colorFont != null) {
			stuAommodations.setColorFont(colorFont);
			isAccommodationSelected = true;//Change for defect -# 65698 
		}
		if (questionBgrdColor != null) {
			
			stuAommodations.setQuestionBackgroundColor
			(stdDetail.getColorHexMapping(questionBgrdColor));
			isAccommodationSelected = true;
		}

		if (questionFontColor != null) {
			stuAommodations.setQuestionFontColor(
					stdDetail.getColorHexMapping(questionFontColor));
			isAccommodationSelected = true;
			
		}
		if (answerBgrdColor != null) {
			stuAommodations.setAnswerBackgroundColor(
					stdDetail.getColorHexMapping(answerBgrdColor));
			isAccommodationSelected = true;
		}

		if (answerFontColor != null) {
			stuAommodations.setAnswerFontColor(
					stdDetail.getColorHexMapping(answerFontColor));
			isAccommodationSelected = true;
		}

		if (fontSize != null) {
			stuAommodations.setAnswerFontSize(fontSize);
			isAccommodationSelected = true;
		}
		if (fontSize != null) {
			stuAommodations.setQuestionFontSize(fontSize);
			isAccommodationSelected = true;
		}

		if (colorFont != null && colorFont.equalsIgnoreCase("F")) {
			stuAommodations.setColorFont(colorFont);       //Change for defect -# 65698 
			stuAommodations.setAnswerBackgroundColor(null);
			stuAommodations.setAnswerFontColor(null);
			stuAommodations.setAnswerFontSize(null);
			stuAommodations.setQuestionBackgroundColor(null);
			stuAommodations.setQuestionFontColor(null);
			stuAommodations.setQuestionFontSize(null);
		}
		if(isAccommodationSelected)
			return stuAommodations;
		else
			return null;
	}


	private MessageInfo createMessageInfo(MessageInfo messageInfo, String messageTitle, String content, String type, boolean errorflag, boolean successFlag){
		messageInfo.setType(type);
		messageInfo.setErrorFlag(errorflag);
		messageInfo.setSuccessFlag(successFlag);
		if(Message.ADDITIONAL_INFORMATION.equals(type)){
			Map additionalInfo = new HashMap();
			additionalInfo.put(messageTitle, content);
			messageInfo.setAdditionalInfoMap(additionalInfo);
		}else {
			messageInfo.setContent(content);
			messageInfo.setTitle(messageTitle);		
		}
		return messageInfo;
	}

	private void creatGson(HttpServletRequest req, HttpServletResponse resp, OutputStream stream, MessageInfo messageInfo){
		
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
	private Map buildStudentList(ManageBulkStudentData ssd) 
	{
		Map studentMap = new HashMap();
		Map<Integer,Integer> studentIdIndexer = new HashMap<Integer,Integer>();
		List studentList = new ArrayList();
		SessionStudent [] sessionStudents = ssd.getManageStudents();    

		for (int i=0; i < sessionStudents.length; i++)
		{
			SessionStudent ss = (SessionStudent)sessionStudents[i];
			ss.setHasColorFontAccommodations(getHasColorFontAccommodations(ss));
            ss.setHasAccommodations(studentHasAccommodation(ss));
			if (ss != null)
			{
				String middleName = ss.getMiddleName();
				if ((middleName != null) && (middleName.length() > 0)) {
					ss.setMiddleName(String.valueOf(middleName.charAt(0)));
				}
				studentList.add(ss);
				studentIdIndexer.put(sessionStudents[i].getStudentId(), i);

			}
		}
		studentMap.put("studentList", studentList);
		studentMap.put("studentIdIndexer", studentIdIndexer);
		return studentMap;
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
	private void initialize()
	{   
		/* Changes for DEX Story - Add intermediate screen : Start */
    	//System.out.println("userName from session in user module >> "+getSession().getAttribute("userName"));
    	//System.out.println("isDexLogin from session [user module] >> "+getSession().getAttribute("isDexLogin"));
    	try {
			this.isEOIUser = this.studentManagement.isOKEOIUser(getRequest().getUserPrincipal().toString()); //need to check and populate this flag
			this.isMappedWith3_8User = this.studentManagement.isMappedWith3_8User(getRequest().getUserPrincipal().toString()); //need to check and populate this flag

			if((getRequest().getParameter("is3to8Selected") != null && "true".equalsIgnoreCase(getRequest().getParameter("is3to8Selected").toString()))) {
				this.is3to8Selected = true;
				this.isEOISelected = false;
				this.isUserLinkSelected = false;
				
				getSession().setAttribute("is3to8Selected", this.is3to8Selected);
				getSession().setAttribute("isEOISelected", this.isEOISelected);
				getSession().setAttribute("isUserLinkSelected", this.isUserLinkSelected);
			}
			if((getRequest().getParameter("isEOISelected") != null && "true".equalsIgnoreCase(getRequest().getParameter("isEOISelected").toString()))) {
				this.is3to8Selected = false;
				this.isEOISelected = true;
				this.isUserLinkSelected = false;
				
				getSession().setAttribute("is3to8Selected", this.is3to8Selected);
				getSession().setAttribute("isEOISelected", this.isEOISelected);
				getSession().setAttribute("isUserLinkSelected", this.isUserLinkSelected);
			}
			if((getRequest().getParameter("isUserLinkSelected") != null && "true".equalsIgnoreCase(getRequest().getParameter("isUserLinkSelected").toString()))) {
				this.is3to8Selected = false;
				this.isEOISelected = false;
				this.isUserLinkSelected = true;
				
				getSession().setAttribute("is3to8Selected", this.is3to8Selected);
				getSession().setAttribute("isEOISelected", this.isEOISelected);
				getSession().setAttribute("isUserLinkSelected", this.isUserLinkSelected);
			}
			
		} catch (CTBBusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* Changes for DEX Story - Add intermediate screen : End */
		getLoggedInUserPrincipal();
		getUserDetails();
		setupUserPermission();
		getCustomerDemographicOptions();
		demographics = null;
		accommodations = null;
		this.accommodations = getStudentAccommodations();
		this.getRequest().setAttribute("accommodations",accommodations);
		this.getRequest().setAttribute("viewOnly", Boolean.FALSE); 
		String roleName = this.user.getRole().getRoleName();
		
		List broadcastMessages = BroadcastUtils.getBroadcastMessages(this.message, this.userName);
        this.getSession().setAttribute("broadcastMessages", new Integer(broadcastMessages.size()));
		
		try{
			MusicFiles[] musicList = this.studentManagement.getMusicFiles();	
			this.getRequest().setAttribute("musicList", musicList);
		}catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		
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
	 * getCustomerDemographicOptions
	 */
	private  void getCustomerDemographicOptions()
	{	
		try {
			customerDemographic =  this.studentManagement.getCustomerDemographics(this.userName, this.customerId);
		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}
		if (customerDemographic != null)
		{
			/*realDemographicOptions = new String[customerDemographic.length+1];
			realDemographicOptions[0] = FilterSortPageUtils.FILTERTYPE_SHOWALL;

			for (int i=0 ; i<customerDemographic.length ; i++) { 
				if(customerDemographic[i].getVisible() != null 
						&& !(customerDemographic[i].getVisible().equals("")) 
						&& customerDemographic[i].getVisible().equals("T")) {
					realDemographicOptions[i+1] = customerDemographic[i].getLabelName();
				}
			}*/
		}
		if(realDemographicOptions != null) {
			//demographic1 = realDemographicOptions;
			//demographic2 = realDemographicOptions;
			//demographic3 = realDemographicOptions;
			
		}

	}
	
	private List buildCustomerDemographicList() 
	{
		List CustomerDemographicList = new ArrayList();
		for (int i=0; i < customerDemographic.length; i++)
		{	
			if(customerDemographic[i].getVisible() != null 
					&& !(customerDemographic[i].getVisible().equals("")) 
					&& customerDemographic[i].getVisible().equals("T")) {
					CustomerDemographicList.add(customerDemographic[i]);
			}
		}

		return CustomerDemographicList;
	}
	
	/**
	 * getGradeOptions
	 */
	private String [] getGradeOptions()
	{
		String[] grades = null;
		try {
			grades =  this.studentManagement.getGradesForCustomer(this.userName, this.customerId);
		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}

		List options = new ArrayList();
		for (int i=0 ; i<grades.length ; i++) {        
			options.add(grades[i]);
		}

		return (String [])options.toArray(new String[0]);        
	}

	private StudentAccommodationsDetail getStudentAccommodations()
	{


		this.accommodations = new StudentAccommodationsDetail();
		setCustomerAccommodations(this.accommodations, true);
		this.accommodations.convertHexToText();      

		return this.accommodations;
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

	//Bulk Accommodation Changes
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
	
	 public String getHasColorFontAccommodations(SessionStudent ss) {
	        String result = "F";
	        if( ss.getQuestionBackgroundColor() != null ||
	        	ss.getQuestionFontColor() != null ||
	        	ss.getQuestionFontSize() != null ||
	        	ss.getAnswerBackgroundColor() != null ||
	        	ss.getAnswerFontColor() != null ||
	        	ss.getAnswerFontSize() != null)
	            result = "T";
	        return result;
	    }
	    
	    
	    public String studentHasAccommodation(SessionStudent  sa){
			 String hasAccommodations = "No";
		        if( "T".equals(sa.getScreenMagnifier()) ||
		            "T".equals(sa.getScreenReader()) ||
		            "T".equals(sa.getCalculator()) ||
		            "T".equals(sa.getTestPause()) ||
		            "T".equals(sa.getUntimedTest()) ||
		            "T".equals(sa.getHighLighter()) ||
		            "T".equals(sa.getExtendedTimeAccom()) ||
		           // (sa.getMaskingRuler() != null && !sa.getMaskingRuler().equals("") && !sa.getMaskingRuler().equals("F"))||
		            (sa.getExtendedTimeAccom() != null && !sa.getExtendedTimeAccom().equals("") && !sa.getExtendedTimeAccom().equals("F")) || 
		           // (sa.getAuditoryCalming() != null && !sa.getAuditoryCalming().equals("") && !sa.getAuditoryCalming().equals("F")) || 
		            //(sa.getMagnifyingGlass() != null && !sa.getMagnifyingGlass().equals("") && !sa.getMagnifyingGlass().equals("F")) || 
		           // (sa.getMaskingTool() != null && !sa.getMaskingTool().equals("") && !sa.getMaskingTool().equals("F")) || 
		            sa.getQuestionBackgroundColor() != null ||
		            sa.getQuestionFontColor() != null ||
		            sa.getQuestionFontSize() != null ||
		            sa.getAnswerBackgroundColor() != null ||
		            sa.getAnswerFontColor() != null ||
		            sa.getAnswerFontSize() != null)
		        	hasAccommodations = "Yes";
		   return hasAccommodations;
		}
	    
	    
	 /////////////////////////////////////////////////////////////////////////////////////////////    
    ///////////////////////////// SETUP USER PERMISSION ///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////    
    private void getLoggedInUserPrincipal()
    {
    	/* Changes for DEX Story - Add intermediate screen : Start */
    	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	if(this.isEOIUser && this.isMappedWith3_8User){
    		//if(getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString())){
    		if(this.is3to8Selected){
    			try {
					this.userName = this.studentManagement.fetchMapped3to8User(getRequest().getUserPrincipal().toString());
				} catch (CTBBusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}	
    		else
    			this.userName = getRequest().getUserPrincipal().toString();//principle object will always contain EOI user
    		
    	}else{
	        java.security.Principal principal = getRequest().getUserPrincipal();
	        if (principal != null) {
	            this.userName = principal.toString();
	        }  
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
    	boolean hasLicenseConfiguration= false;
    	boolean TABECustomer = false;
    	boolean adminCoordinatorUser = isAdminCoordinatorUser(); //For Student Registration
    	String roleName = this.user.getRole().getRoleName();
    	boolean hasResetTestSession = false;
    	boolean hasResetTestSessionForAdmin = false;
    	boolean isOKCustomer = false;
    	boolean isGACustomer = false;
    	boolean isTascCustomer = false;
    	boolean isTASCReadinessCustomer = false; //Added for story OAS-1542 TASC Readiness - Enable/Test 'Test Reset'
    	boolean isTopLevelAdmin = new Boolean(isTopLevelUser() && isAdminUser());
    	boolean laslinkCustomer = false;
    	boolean hasDataExportVisibilityConfig = false;
    	Integer dataExportVisibilityLevel = 1;
    	boolean hasBlockUserManagement = false;
    	boolean hasSSOHideUserProfile = false;
    	boolean hasSSOBlockUserModifications = false;
    	boolean isWVCustomer = false;
    	boolean lloRPCustomer = false;
    	
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
				//For License
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Subscription") && 
	            		cc.getDefaultValue().equals("T")	) {
					hasLicenseConfiguration = true;
					continue;
	            }
				// For TABE Customer
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("TABE_Customer")) {
	            	TABECustomer = true;
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
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Data_Export_Visibility")) {
					hasDataExportVisibilityConfig = true;
					dataExportVisibilityLevel = Integer.parseInt(cc.getDefaultValue());
					continue;
	            }
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Block_User_Management_3to8") && 
	            		cc.getDefaultValue().equals("T")) {
	        		hasBlockUserManagement = Boolean.TRUE;
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
				//Added for story Config Reset Test for TASC top level admin
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("TASC_Customer")
						//[IAA]&& cc.getDefaultValue().equals("T")) {
	            		){
					isTascCustomer = true;
					continue;
	            }
	 			//Added for story OAS-1542 TASC Readiness - Enable/Test 'Test Reset'
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("TASCReadiness_Customer")){
					isTASCReadinessCustomer = true;
					continue;
	            }
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("WV_Customer")
						//[IAA]&& cc.getDefaultValue().equals("T")) {
	            		){
					isWVCustomer = true;
					continue;
	            }
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("LLO_RP_Customer")
						&& cc.getDefaultValue().equals("T")) {
	            	lloRPCustomer = true;
					continue;
	            }
				
			}
			
		}
		if (isWVCustomer)
		{
			if(!isWVCustomerTopLevelAdminAndAdminCO())
			{
			hasUploadConfig=false;
			hasUploadDownloadConfig=false;
			}
		}
		if (hasUploadConfig && hasDownloadConfig) {
			hasUploadDownloadConfig = true;
		}
		if (hasUploadDownloadConfig) {
			hasUploadConfig = false;
			hasDownloadConfig = false;
		}
		if(isWVCustomer)
		{
			this.getSession().setAttribute("hasUploadConfigured",new Boolean(hasUploadConfig));
			this.getSession().setAttribute("hasUploadDownloadConfigured",new Boolean(hasUploadDownloadConfig));
		}
		else
		{
			this.getSession().setAttribute("hasUploadConfigured",new Boolean(hasUploadConfig && adminUser));
			this.getSession().setAttribute("hasUploadDownloadConfigured",new Boolean(hasUploadDownloadConfig && adminUser));
		}
		this.getSession().setAttribute("isBulkAccommodationConfigured",new Boolean(hasBulkStudentConfigurable));
		this.getSession().setAttribute("isBulkMoveConfigured",new Boolean(hasBulkStudentMoveConfigurable));
		this.getSession().setAttribute("isOOSConfigured",new Boolean(hasOOSConfigurable));
		this.getSession().setAttribute("hasDownloadConfigured",new Boolean(hasDownloadConfig && adminUser));
		this.getSession().setAttribute("hasProgramStatusConfigured",new Boolean(hasProgramStatusConfig && adminUser));
		this.getSession().setAttribute("hasScoringConfigured",new Boolean(hasScoringConfigurable));
		this.getSession().setAttribute("hasLicenseConfigured",new Boolean(hasLicenseConfiguration && adminUser));
		this.getSession().setAttribute("adminUser", new Boolean(adminUser));
		boolean validUser = (roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR) || 
        		roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR));
		this.getSession().setAttribute("canRegisterStudent", new Boolean(TABECustomer && validUser));
		this.getSession().setAttribute("hasRapidRagistrationConfigured", new Boolean(TABECustomer && (adminUser || adminCoordinatorUser) ));//For Student Registration
		this.getSession().setAttribute("hasResetTestSession", new Boolean((hasResetTestSession && hasResetTestSessionForAdmin) && ((isOKCustomer && isTopLevelAdmin)||(laslinkCustomer && (adminUser||adminCoordinatorUser))||(isGACustomer && adminUser)||(isTascCustomer && isTopLevelAdmin) || (isTASCReadinessCustomer && isTopLevelAdmin))));
    
		
		this.getRequest().setAttribute("isLasLinkCustomer", laslinkCustomer);
		System.out.println(laslinkCustomer);
     	//this.getSession().setAttribute("showDataExportTab",laslinkCustomer);
     	this.getSession().setAttribute("showDataExportTab", new Boolean((laslinkCustomer && isTopLevelUser()) || (hasDataExportVisibilityConfig && checkUserLevel(dataExportVisibilityLevel))));
     	//show Account file download link      	
     	this.getSession().setAttribute("isAccountFileDownloadVisible", new Boolean(laslinkCustomer && isTopLevelAdmin));
     	//Done for 3to8 customer to block user module
     	this.getSession().setAttribute("hasBlockUserManagement", new Boolean(hasBlockUserManagement));
     	//Done for Engrade customer to block admin users from adding/editing/deleting users
     	this.getSession().setAttribute("hasSSOHideUserProfile", new Boolean(hasSSOHideUserProfile));
     	this.getSession().setAttribute("hasSSOBlockUserModifications", new Boolean(hasSSOBlockUserModifications));
     	this.getSession().setAttribute("isEngradeCustomer", new Boolean(this.isEngradeCustomer));
     	this.getSession().setAttribute("isLLORPCustomer", new Boolean(lloRPCustomer));
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

	private boolean isAdminCoordinatorUser() //For Student Registration
	{               
		String roleName = this.user.getRole().getRoleName();        
		return roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR); 
	}
	private boolean isWVCustomerTopLevelAdminAndAdminCO(){
		boolean isWVCustomerTopLevelAdminAndAdminCO = false;
		boolean isUserTopLevel =false;
		try {
			isUserTopLevel = orgnode.checkTopOrgNodeUser(this.userName);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (isUserTopLevel &&(isAdminUser() || isAdminCoordinatorUser()))
			isWVCustomerTopLevelAdminAndAdminCO = true;
		return isWVCustomerTopLevelAdminAndAdminCO;
	}
	private void setupUserPermission()
	{
		customerConfigurations = getCustomerConfigurations();  

        boolean laslinkCustomer = isLaslinkCustomer(customerConfigurations);
        
        this.getSession().setAttribute("showReportTab", 
        		new Boolean(userHasReports().booleanValue() || laslinkCustomer));
     	
     	this.getRequest().setAttribute("isLasLinkCustomer", laslinkCustomer);  
    	
    	this.getRequest().setAttribute("isTopLevelUser",isTopLevelUser(laslinkCustomer));
    	
    	getConfigStudentLabel(customerConfigurations);
		
		this.getRequest().setAttribute("customerConfigurations", customerConfigurations);
		
		setUpAllUserPermission(customerConfigurations);
		



		//this.getSession().setAttribute("showDataExportTab",laslinkCustomer);

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
	
	 private boolean isAdminUser() 
	    {               
	        String roleName = this.user.getRole().getRoleName();        
	        return roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ADMINISTRATOR); 
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
				CustomerConfigurationValue[] customerConfigurationsValue = customerConfigurationValues(configId);
				//By default there should be 3 entries for customer configurations
				valueForStudentId = new String[8];
				for(int j=0; j<customerConfigurationsValue.length; j++){
					int sortOrder = customerConfigurationsValue[j].getSortOrder();
					valueForStudentId[sortOrder-1] = customerConfigurationsValue[j].getCustomerConfigurationValue();
				}	
				valueForStudentId[0] = valueForStudentId[0]!= null ? valueForStudentId[0] : "Student ID" ;

			}

		}
		this.getSession().setAttribute("studentIdLabelName",valueForStudentId[0]);
		
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
	    	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
	    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
	    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
			try
	        {	if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
		        	String url = "/SessionWeb/sessionOperation/assessments_sessions.do?is3to8Selected="+this.is3to8Selected;
		        	getResponse().sendRedirect(url);
		        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
		    		String url = "/SessionWeb/sessionOperation/assessments_sessions.do?isEOISelected="+this.isEOISelected;
		    		getResponse().sendRedirect(url);
		    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
		    		String url = "/SessionWeb/sessionOperation/assessments_sessions.do?isUserLinkSelected="+this.isUserLinkSelected;
		    		getResponse().sendRedirect(url);
		    	}else{
					 String url = "/SessionWeb/sessionOperation/assessments_sessions.do";
					 getResponse().sendRedirect(url);
		    	}
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
	    	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
			this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
			this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
			try
		    {	
		    	if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
		        	String url = "/SessionWeb/programOperation/assessments_programStatus.do?is3to8Selected="+this.is3to8Selected;
		        	getResponse().sendRedirect(url);
		        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
		    		String url = "/SessionWeb/programOperation/assessments_programStatus.do?isEOISelected="+this.isEOISelected;
		    		getResponse().sendRedirect(url);
		    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
		    		String url = "/SessionWeb/programOperation/assessments_programStatus.do?isUserLinkSelected="+this.isUserLinkSelected;
		    		getResponse().sendRedirect(url);
		    	}else{
					 String url = "/SessionWeb/programOperation/assessments_programStatus.do";
					 getResponse().sendRedirect(url);
		    	}
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
	    	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
	    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
	    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
			try
	        {	
				if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
		        	String url = "/RegistrationWeb/registrationOperation/beginStudentRegistration.do?is3to8Selected="+this.is3to8Selected;
		        	getResponse().sendRedirect(url);
		        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
		    		String url = "/RegistrationWeb/registrationOperation/beginStudentRegistration.do?isEOISelected="+this.isEOISelected;
		    		getResponse().sendRedirect(url);
		    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
		    		String url = "/RegistrationWeb/registrationOperation/beginStudentRegistration.do?isUserLinkSelected="+this.isUserLinkSelected;
		    		getResponse().sendRedirect(url);
		    	}else{
		        	String url = "/RegistrationWeb/registrationOperation/beginStudentRegistration.do";
		        	getResponse().sendRedirect(url);
		    	}
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
	        @Jpf.Forward(name = "OOSLink", path = "organizations_manageOutOfSchool.do"),
	        @Jpf.Forward(name = "showAccountFileDownloadLink", path = "eMetric_user_accounts_detail.do")
	    }) 
	protected Forward organizations()
	{
		String menuId = (String)this.getRequest().getParameter("menuId");    	
		String forwardName = (menuId != null) ? menuId : "bulkAccomLink";
		
	    return new Forward(forwardName);
	}
	
	@Jpf.Action()
	protected Forward eMetric_user_accounts_detail()
	{
		this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/SessionWeb/userAccountFileOperation/accountFiles.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/SessionWeb/userAccountFileOperation/accountFiles.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/SessionWeb/userAccountFileOperation/accountFiles.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
               String url = "/SessionWeb/userAccountFileOperation/accountFiles.do";
               getResponse().sendRedirect(url);
	    	}
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
	} 


    @Jpf.Action()
	protected Forward organizations_manageOrganizations()
	{
    	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
				String url = "/OrganizationWeb/orgOperation/organizations_manageOrganizations.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	        	String url = "/OrganizationWeb/orgOperation/organizations_manageOrganizations.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/OrganizationWeb/orgOperation/organizations_manageOrganizations.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
	    		String url = "/OrganizationWeb/orgOperation/organizations_manageOrganizations.do";
				 getResponse().sendRedirect(url);
	    	}
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
    	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/StudentWeb/studentOperation/organizations_manageStudents.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/StudentWeb/studentOperation/organizations_manageStudents.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/StudentWeb/studentOperation/organizations_manageStudents.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
				 String url = "/StudentWeb/studentOperation/organizations_manageStudents.do";
				 getResponse().sendRedirect(url);
	    	}
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
	}
    
    @Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success", path = "beginAddBulkStudent.do") 
	    }) 
	protected Forward organizations_manageBulkAccommodation()
	{
    	if(getSession().getAttribute("is3to8Selected") == null)
			this.is3to8Selected = (getRequest().getParameter("is3to8Selected") != null && "true".equalsIgnoreCase(getRequest().getParameter("is3to8Selected").toString()))? true: false; 
    	if(getSession().getAttribute("isEOISelected") == null)
    		this.isEOISelected = (getRequest().getParameter("isEOISelected") != null && "true".equalsIgnoreCase(getRequest().getParameter("isEOISelected").toString()))? true: false;
    	if(getSession().getAttribute("isUserLinkSelected") == null)
    		this.isUserLinkSelected = (getRequest().getParameter("isUserLinkSelected") != null && "true".equalsIgnoreCase(getRequest().getParameter("isUserLinkSelected").toString()))? true: false;

	    return new Forward("success");
	}
	
    @Jpf.Action()
	protected Forward organizations_manageUsers()
	{
    	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/UserWeb/userOperation/organizations_manageUsers.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/UserWeb/userOperation/organizations_manageUsers.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/UserWeb/userOperation/organizations_manageUsers.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
				String url = "/UserWeb/userOperation/organizations_manageUsers.do";
				getResponse().sendRedirect(url);
	    	}
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
    	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/StudentWeb/bulkMoveOperation/organizations_manageBulkMove.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/StudentWeb/bulkMoveOperation/organizations_manageBulkMove.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/StudentWeb/bulkMoveOperation/organizations_manageBulkMove.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
				 String url = "/StudentWeb/bulkMoveOperation/organizations_manageBulkMove.do";
				 getResponse().sendRedirect(url);
	    	}
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
    	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/StudentWeb/outOfSchoolOperation/organizations_manageOutOfSchool.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/StudentWeb/outOfSchoolOperation/organizations_manageOutOfSchool.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/StudentWeb/outOfSchoolOperation/organizations_manageOutOfSchool.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
	            String url = "/StudentWeb/outOfSchoolOperation/organizations_manageOutOfSchool.do";
	            getResponse().sendRedirect(url);
	    	}
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
    	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/SessionWeb/sessionOperation/reports.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/SessionWeb/sessionOperation/reports.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/SessionWeb/sessionOperation/reports.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
	            String url = "/SessionWeb/sessionOperation/reports.do";
	            getResponse().sendRedirect(url);
	    	}
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
	        @Jpf.Forward(name = "exportDataLink", path = "services_dataExport.do"),
	        @Jpf.Forward(name = "viewStatusLink", path = "services_viewStatus.do")
	    }) 
	protected Forward services()
	{
		String menuId = (String)this.getRequest().getParameter("menuId");    	
		String forwardName = (menuId != null) ? menuId : "installSoftwareLink";
		
	    return new Forward(forwardName);
	}
	
	@Jpf.Action()
    protected Forward services_dataExport()
    {
		this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/ExportWeb/dataExportOperation/services_dataExport.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/ExportWeb/dataExportOperation/services_dataExport.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/ExportWeb/dataExportOperation/services_dataExport.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
	    		String url = "/ExportWeb/dataExportOperation/services_dataExport.do";
	    		getResponse().sendRedirect(url);
	    	}
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
		this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/OrganizationWeb/resetOperation/services_resetTestSession.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/OrganizationWeb/resetOperation/services_resetTestSession.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/OrganizationWeb/resetOperation/services_resetTestSession.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
	            String url = "/OrganizationWeb/resetOperation/services_resetTestSession.do";
	            getResponse().sendRedirect(url);
	    	}
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
    	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/ExportWeb/dataExportOperation/beginViewStatus.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/ExportWeb/dataExportOperation/beginViewStatus.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/ExportWeb/dataExportOperation/beginViewStatus.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
	            String url = "/ExportWeb/dataExportOperation/beginViewStatus.do";
	            getResponse().sendRedirect(url);
	    	}
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
    	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/OrganizationWeb/licenseOperation/services_manageLicenses.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/OrganizationWeb/licenseOperation/services_manageLicenses.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/OrganizationWeb/licenseOperation/services_manageLicenses.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
	            String url = "/OrganizationWeb/licenseOperation/services_manageLicenses.do";
	            getResponse().sendRedirect(url);
	    	}
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
    	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/SessionWeb/softwareOperation/services_installSoftware.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/SessionWeb/softwareOperation/services_installSoftware.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/SessionWeb/softwareOperation/services_installSoftware.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
				 String url = "/SessionWeb/softwareOperation/services_installSoftware.do";
				 getResponse().sendRedirect(url);
	    	}
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
		this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/SessionWeb/testContentOperation/services_downloadTest.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/SessionWeb/testContentOperation/services_downloadTest.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/SessionWeb/testContentOperation/services_downloadTest.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
	            String url = "/SessionWeb/testContentOperation/services_downloadTest.do";
	            getResponse().sendRedirect(url);
	    	}
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
    	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/OrganizationWeb/uploadOperation/services_uploadData.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/OrganizationWeb/uploadOperation/services_uploadData.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/OrganizationWeb/uploadOperation/services_uploadData.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
	            String url = "/OrganizationWeb/uploadOperation/services_uploadData.do";
	            getResponse().sendRedirect(url);
	    	}
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
    	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/OrganizationWeb/downloadOperation/services_downloadData.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/OrganizationWeb/downloadOperation/services_downloadData.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/OrganizationWeb/downloadOperation/services_downloadData.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
	            String url = "/OrganizationWeb/downloadOperation/services_downloadData.do";
	            getResponse().sendRedirect(url);
	    	}
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
	}
    

	/**
	 * @jpf:action
	 */
	@Jpf.Action()
	protected Forward broadcastMessage()
	{
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
    	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {
        	if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/ScoringWeb/studentScoringOperation/beginStudentScoring.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/ScoringWeb/studentScoringOperation/beginStudentScoring.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/ScoringWeb/studentScoringOperation/beginStudentScoring.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
	            String url = "/ScoringWeb/studentScoringOperation/beginStudentScoring.do";
	            getResponse().sendRedirect(url);
	    	}
        } 
        catch (IOException ioe)
        {
            System.err.print(ioe.getStackTrace());
        }
        return null;
	}
	
}