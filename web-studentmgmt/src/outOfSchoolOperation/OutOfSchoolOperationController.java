package outOfSchoolOperation;

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
import javax.servlet.http.HttpSession;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import utils.Base;
import utils.BaseTree;
import utils.BroadcastUtils;
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
import com.ctb.bean.studentManagement.CustomerConfiguration;
import com.ctb.bean.studentManagement.CustomerConfigurationValue;
import com.ctb.bean.studentManagement.ManageStudentData;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.exception.CTBBusinessException;
import com.google.gson.Gson;

@Jpf.Controller(simpleActions = { @Jpf.SimpleAction(name = "begin", path = "index.jsp") })
public class OutOfSchoolOperationController extends PageFlowController {
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
	CustomerConfiguration[] customerConfigurations = null;
	public static String CONTENT_TYPE_JSON = "application/json";
	
	/* Changes for DEX Story - Add intermediate screen : Start */
    private boolean isEOIUser = false;
	private boolean isMappedWith3_8User = false;
	private boolean is3to8Selected = false;
	private boolean isEOISelected = false;
	private boolean isUserLinkSelected = false;
   /* Changes for DEX Story - Add intermediate screen : End */
	
	private boolean isEngradeCustomer = false;
	
	/**
	 * @jpf:action
	 * @jpf:forward name="success" path="findStudent.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "out_of_school_container.jsp")
	}, 
	validationErrorForward = @Jpf.Forward(name = "failure",
			path = "logout.do"))
	protected Forward beginOutOfSchoolStudent()
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
						path ="")
		})
	    protected Forward getStudentForSelectedNode(){

			HttpServletRequest req = getRequest();
			HttpServletResponse resp = getResponse();
			OutputStream stream = null;
			String contentType = CONTENT_TYPE_JSON;
			String studentArray = "";
			String json = "";
			ObjectOutput output = null;
			Map<Integer, Map> accomodationMap = new HashMap<Integer, Map>();
			
			try {
				ManageStudentData msData = findStudentByHierarchy();
				List studentNodes = StudentSearchUtils.buildStudentList(msData,accomodationMap);
				
				Base base = new Base();
				base.setPage("1");
				base.setRecords("10");
				base.setTotal("2");
				List <Row> rows = new ArrayList<Row>();
				Gson gson = new Gson();
				base.setStudentProfileInformation(studentNodes);
				base.setStudentIdArray(studentArray);
				base.setAccomodationMap(accomodationMap);
				
				json = gson.toJson(base);
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
	
	 
	 @Jpf.Action(forwards = { 
				@Jpf.Forward(name = "success", 
						path = "")
		})
		protected Forward updateOutOfSchoolData()
		{   
			HttpServletRequest req = getRequest();
			HttpServletResponse resp = getResponse();
			OutputStream stream = null;
			MessageInfo messageInfo = new MessageInfo();
			String oosData = req.getParameter("oosSelected");			
			String[] oosDataArr = oosData.split(",");
			Integer[] studentOOSIds = new Integer[oosDataArr.length];
			for(int i = 0; i < oosDataArr.length; i++) {
				studentOOSIds[i] = Integer.parseInt(oosDataArr[i]);
			}
			if(studentOOSIds.length > 0) {
				try {
					this.studentManagement.updateOOSOperation(studentOOSIds);
					messageInfo.setSuccessFlag(true);
				} catch (CTBBusinessException e) {
					messageInfo.setSuccessFlag(false);
				}
			}
			creatGson(req, resp, stream, messageInfo);
			return null;
		}
	
	
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="")
	})
	protected Forward oosOrgNodeHierarchyList(){

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
			jsonTree = gson.toJson(baseTree);
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
	
	/**
	 * findByHierarchy
	 */
	private ManageStudentData findStudentByHierarchy()
	{      
		String treeOrgNodeId = getRequest().getParameter("stuForOrgNodeId");
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
			msData = StudentSearchUtils.searchOOSStudentsByOrgNode(this.userName, this.studentManagement, selectedOrgNodeId, filter, page, sort);

		}

		return msData;
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
		
		List broadcastMessages = BroadcastUtils.getBroadcastMessages(this.message, this.userName);
        this.getSession().setAttribute("broadcastMessages", new Integer(broadcastMessages.size()));		
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
			System.err.println("Exception while updating oos status.");
			e.printStackTrace();
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
    	String roleName = this.user.getRole().getRoleName();
    	boolean adminCoordinatorUser = isAdminCoordinatorUser();
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
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("LLO_RP_Customer") &&
						cc.getDefaultValue().equals("T")) {
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
		this.getSession().setAttribute("hasRapidRagistrationConfigured", new Boolean(TABECustomer && (adminUser || adminCoordinatorUser) ));
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
	 private boolean isAdminCoordinatorUser() 
	 {               
	        String roleName = this.user.getRole().getRoleName();        
	        return roleName.equalsIgnoreCase(PermissionsUtils.ROLE_NAME_ACCOMMODATIONS_COORDINATOR); 
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
		this.getRequest().setAttribute("studentIdLabelName",valueForStudentId[0]);
		
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
		String forwardName = (menuId != null) ? menuId : "OOSLink";
		
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
	protected Forward organizations_manageBulkAccommodation()
	{
    	this.is3to8Selected = (getSession().getAttribute("is3to8Selected") != null && "true".equalsIgnoreCase(getSession().getAttribute("is3to8Selected").toString()))? true: false;
    	this.isEOISelected = (getSession().getAttribute("isEOISelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isEOISelected").toString()))? true: false;
    	this.isUserLinkSelected = (getSession().getAttribute("isUserLinkSelected") != null && "true".equalsIgnoreCase(getSession().getAttribute("isUserLinkSelected").toString()))? true: false;
		try
        {	
			if(this.isEOIUser && this.isMappedWith3_8User && this.is3to8Selected){
	        	String url = "/StudentWeb/bulkOperation/organizations_manageBulkAccommodation.do?is3to8Selected="+this.is3to8Selected;
	        	getResponse().sendRedirect(url);
	        }else if(this.isEOIUser && this.isMappedWith3_8User && this.isEOISelected){
	    		String url = "/StudentWeb/bulkOperation/organizations_manageBulkAccommodation.do?isEOISelected="+this.isEOISelected;
	    		getResponse().sendRedirect(url);
	    	}else if(this.isEOIUser && this.isMappedWith3_8User && this.isUserLinkSelected){
	    		String url = "/StudentWeb/bulkOperation/organizations_manageBulkAccommodation.do?isUserLinkSelected="+this.isUserLinkSelected;
	    		getResponse().sendRedirect(url);
	    	}else{
				String url = "/StudentWeb/bulkOperation/organizations_manageBulkAccommodation.do";
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
    
    @Jpf.Action(forwards = { 
	        @Jpf.Forward(name = "success", path = "beginOutOfSchoolStudent.do") 
	    }) 
	protected Forward organizations_manageOutOfSchool()
	{
    	if(getSession().getAttribute("is3to8Selected") == null)
			this.is3to8Selected = (getRequest().getParameter("is3to8Selected") != null && "true".equalsIgnoreCase(getRequest().getParameter("is3to8Selected").toString()))? true: false; 
    	if(getSession().getAttribute("isEOISelected") == null)
    		this.isEOISelected = (getRequest().getParameter("isEOISelected") != null && "true".equalsIgnoreCase(getRequest().getParameter("isEOISelected").toString()))? true: false;
    	if(getSession().getAttribute("isUserLinkSelected") == null)
    		this.isUserLinkSelected = (getRequest().getParameter("isUserLinkSelected") != null && "true".equalsIgnoreCase(getRequest().getParameter("isUserLinkSelected").toString()))? true: false;

	    return new Forward("success");
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