package orgOperation;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import utils.Base;
import utils.BaseTree;
import utils.DateUtils;
import utils.MessageInfo;
import utils.Organization;
import utils.OrganizationPathListUtils;
import utils.OrgnizationComparator;
import utils.PermissionsUtils;
import utils.TreeData;

import com.ctb.bean.testAdmin.BroadcastMessage;
import com.ctb.bean.testAdmin.BroadcastMessageData;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.NodeData;
import com.ctb.bean.testAdmin.OrgNodeCategory;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.SQLutils;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.google.gson.Gson;

import dto.Message;


@Jpf.Controller()
public class OrgOperationController extends PageFlowController {
	private static final long serialVersionUID = 1L;
	
    public LinkedHashMap orgLevelOptions = null;//Added on 09.11.2011 
	
	/**
     * @common:control
     */
    @Control()
    private com.ctb.control.userManagement.UserManagement userManagement;
    
    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.db.Users users;
    
    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.organizationManagement.OrganizationManagement organizationManagement;
    
    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.db.OrgNode orgNode;

    
    @org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.OrgNode orgnode;
	
    
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.BroadcastMessageLog message;
    
    
    public static String CONTENT_TYPE_JSON = "application/json";

    /**
     * @common:control
     */
    @Control()
    private com.ctb.control.testAdmin.TestSessionStatus testSessionStatus;
	
	private String userName = null;
	private Integer customerId = null;
    private User user = null;

	private List<BroadcastMessage> broadcastMessages = null;

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
     * @jpf:action
     * @jpf:forward name="success" path="beginFindOrganization.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "beginFindOrganization.do")
    })
    protected Forward begin()
    {        
        return new Forward("success");
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path = "beginFindOrganization.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "beginFindOrganization.do")
    })
    protected Forward defaultAction(ManageOrganizationForm form)
    {   
        return new Forward("success");
    }
    
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="findOrganizationHierarchy.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "findOrganizationHierarchy.do")
    })
    protected Forward beginFindOrganization()
    {                
    	getLoggedInUserPrincipal();
		
		getUserDetails();
		
		setupUserPermission();

		this.broadcastMessages = getBroadcastMessages();
        this.getSession().setAttribute("broadcastMessages", new Integer(this.broadcastMessages.size()));
		
    	return new Forward("success");
        
    }
    
    /**
	 * @jpf:action
	 * @jpf:forward name="success" path="organization_hierarchy.jsp"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success",
					path = "organization_hierarchy.jsp")
	}, 
	validationErrorForward = @Jpf.Forward(name = "failure",
			path = "logout.do"))
			protected Forward findOrganizationHierarchy(ManageOrganizationForm form)
	{   
		this.getRequest().setAttribute("isFindOrganization", Boolean.TRUE);
		return new Forward("success");
	}
	
	
	
	
	@Jpf.Action(forwards={
			@Jpf.Forward(name = "success", 
					path ="find_user_hierarchy.jsp")
	})
	protected Forward organizationOrgNodeHierarchyList(ManageOrganizationForm form){

		String jsonTree = "";
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String contentType = CONTENT_TYPE_JSON;
		
		try {
			
			BaseTree baseTree = new BaseTree ();
			ArrayList<Organization> completeOrgNodeList = new ArrayList<Organization>();
			UserNodeData associateNode = OrganizationPathListUtils.populateAssociateNode(this.userName,this.userManagement);
			ArrayList<Organization> selectedList  = OrganizationPathListUtils.buildassoOrgNodehierarchyList(associateNode);
			Integer leafNodeCategoryId = OrganizationPathListUtils.getLeafNodeCategoryId(this.userName,this.customerId, this.userManagement);
			Collections.sort(selectedList, new OrgnizationComparator());
			ArrayList <Integer> orgIDList = new ArrayList <Integer>();
			ArrayList<TreeData> data = new ArrayList<TreeData>();
			UserNodeData und = OrganizationPathListUtils.OrgNodehierarchy(this.userName, 
					this.userManagement, selectedList.get(0).getOrgNodeId()); 
			ArrayList<Organization> orgNodesList = OrganizationPathListUtils.buildOrgNodehierarchyList(und, orgIDList, completeOrgNodeList);	
			
					

			for (int i= 0; i < selectedList.size(); i++) {

				if (i == 0) {

					preTreeProcess (data,orgNodesList,selectedList);

				} else {

					Integer nodeId = selectedList.get (i).getOrgNodeId();
					if (orgIDList.contains(nodeId)) {
						continue;
					} else if (!selectedList.get (i).getIsAssociate()) {
						
						continue;
						
					} else {

						orgIDList = new ArrayList <Integer>();
						UserNodeData undloop = OrganizationPathListUtils.OrgNodehierarchy(this.userName, 
								this.userManagement,nodeId);   
						ArrayList<Organization> orgNodesListloop = OrganizationPathListUtils.buildOrgNodehierarchyList(undloop, orgIDList, completeOrgNodeList);	
						preTreeProcess (data,orgNodesListloop,selectedList);
					}
				}


			}

			Gson gson = new Gson();
			baseTree.setData(data);
			baseTree.setLeafNodeCategoryId(leafNodeCategoryId);
			baseTree.setIsLeafNodeAdmin(isLeafNodeAdmin (leafNodeCategoryId, selectedList));
			Collections.sort(baseTree.getData(), new Comparator<TreeData>(){

				public int compare(TreeData t1, TreeData t2) {
					return (t1.getData().toUpperCase().compareTo(t2.getData().toUpperCase()));
				}
					
			});
			jsonTree = gson.toJson(baseTree);
			String pattern = ",\"children\":[]";
			jsonTree = jsonTree.replace(pattern, "");

			try {
				resp.setContentType(contentType);
	    		stream = resp.getOutputStream();

	    		String acceptEncoding = req.getHeader("Accept-Encoding");
	    		System.out.println("acceptEncoding..."+acceptEncoding.toString());

	    		if (acceptEncoding != null && acceptEncoding.contains("gzip")) {
	    		    resp.setHeader("Content-Encoding", "gzip");
	    		    stream = new GZIPOutputStream(stream);
	    		}
	    		
				resp.flushBuffer();
	    		stream.write(jsonTree.getBytes());
	    		
			} finally{
				if (stream!=null){
					stream.close();
				}
			}
		} catch (Exception e) {
			System.err.println("Exception while processing userOrgNodeHierarchyList.");
			e.printStackTrace();
		}

		return null;

	}
	
	
	 @Jpf.Action(forwards={
				@Jpf.Forward(name = "success", 
						path ="find_user_hierarchy.jsp")
		})
		protected Forward orgNodeHierarchyGrid(ManageOrganizationForm form){
						
		 HttpServletRequest req = getRequest();
		 HttpServletResponse resp = getResponse();
		 Integer treeOrgNodeId = Integer.parseInt(getRequest().getParameter("treeOrgNodeId"));
		 List organizationList = new ArrayList(0);
		 OutputStream stream = null;
		 String contentType = CONTENT_TYPE_JSON;
		 String json = "";
		 ObjectOutput output = null;
			try {
				NodeData subOrganizationList  = new NodeData();
				subOrganizationList = OrganizationPathListUtils.getOrganizationNodesForParentIncludingParentName(this.userName, this.organizationManagement, treeOrgNodeId);
				if (subOrganizationList != null)
		        {
		            organizationList = OrganizationPathListUtils.buildOrganizationList(subOrganizationList);
		        }
				Base base = new Base();
				base.setPage("1");
				base.setRecords("10");
				base.setTotal("2");
				Gson gson = new Gson();
				base.setOrganizationProfileInformation(organizationList);
				json = gson.toJson(base);
				
					try{
			    		resp.setContentType("application/json");
			    		stream = resp.getOutputStream();
			    		resp.flushBuffer();
			    		stream.write(json.getBytes());
						
			    	  /*resp.setContentType(contentType);
			    		resp.setContentType("application/json");
			    		resp.setCharacterEncoding("UTF-8");
						
			    		stream = resp.getOutputStream();

			    		String acceptEncoding = req.getHeader("Accept-Encoding");
			    		System.out.println("acceptEncoding..."+acceptEncoding.toString());

			    		if (acceptEncoding != null && acceptEncoding.contains("gzip")) {
			    		    resp.setHeader("Content-Encoding", "gzip");
			    		    stream = new GZIPOutputStream(stream);
			    		}
			    		
						resp.flushBuffer();
			    		stream.write(json.getBytes());
			    	   	*/
			    		}
				
			    		 finally{
			 				if (stream!=null){
			 					stream.close();
			 				}
			 			}
					
					
				
			} catch (Exception e) {
				System.err.println("Exception while processing OrgNodeHierarchyGrid");
				e.printStackTrace();
			}
			
			return null;
			
		}
	 
	 @Jpf.Action(forwards={
				@Jpf.Forward(name = "success", 
						path ="find_user_hierarchy.jsp")
		})
		protected Forward populateLayer(ManageOrganizationForm form){
						
		 HttpServletRequest req = getRequest();
		 HttpServletResponse resp = getResponse();
		 Integer selectedParentNode = Integer.parseInt(getRequest().getParameter("selectedParentNode"));
		 OutputStream stream = null;
		 String contentType = CONTENT_TYPE_JSON;
		 OrgNodeCategory[] orgCatagories = null;
		 String json = "";
		 //added on 14.12.2011 for class with student is changed to school level issue
		 Boolean isAddOrganization = new Boolean(getRequest().getParameter("isAddOrganization"));
		 Integer orgNodeToEdit = null;
		 if(getRequest().getParameter("currentNodeIdForEdit") != null)
			 orgNodeToEdit = Integer.parseInt(getRequest().getParameter("currentNodeIdForEdit"));
		 
		 System.out.println("isAddOrganization ::"+isAddOrganization);
		 System.out.println("orgNodeToEdit ::"+orgNodeToEdit);
		 System.out.println("selectedParentNode ::"+selectedParentNode);
		 
			try {
				if(isAddOrganization){
					orgCatagories = organizationManagement.getFrameworkListForOrg(selectedParentNode, null, true); 
				}
				else{
					orgCatagories = organizationManagement.getFrameworkListForOrg(orgNodeToEdit, selectedParentNode, false);
				}

				Gson gson = new Gson();
				json = gson.toJson(orgCatagories);
				
					try{
			    		resp.setContentType("application/json");
			    		stream = resp.getOutputStream();
			    		resp.flushBuffer();

			    		
			    		//response.setContentType("application/json");
			    		//response.setCharacterEncoding("UTF-8");
			    		//OutputStream output = response.getOutputStream();

			    		String acceptEncoding = req.getHeader("Accept-Encoding");
			    		if (acceptEncoding != null && acceptEncoding.contains("gzip")) {
			    		    resp.setHeader("Content-Encoding", "gzip");
			    		    stream = new GZIPOutputStream(stream);
			    		}

			    		stream.write(json.getBytes("UTF-8"));
			    		
			    		
			    		//stream.write(json.getBytes());
				
			    		}
				
			    		 finally{
			 				if (stream!=null){
			 					stream.close();
			 				}
			 			}
					
					
				
			} catch (Exception e) {
				System.err.println("Exception while processing populateLayer");
				e.printStackTrace();
			}
			
			return null;
			
		}
	 /**
	  * Added on 08.11.2011 for getting organization details
	  */	 
	 @Jpf.Action(forwards={
	 			@Jpf.Forward(name = "success", 
	 					path ="find_user_by_hierarchy.jsp")
	 		})
	 		protected Forward getOrgDetailsForEdit(ManageOrganizationForm form)
	 		{
	 		 HttpServletRequest req = getRequest();
	 		 HttpServletResponse resp = getResponse();
	 		 OutputStream stream = null;
	 		 String contentType = CONTENT_TYPE_JSON;
	 		 String json = "";
	 		 MessageInfo messageInfo = new MessageInfo(); //added on 10.12.2011 for open node functionality in new jstree 
	 		 Node [] organizationNodes = null; //added on 10.12.2011 for open node functionality in new jstree 
	 		 Node orgNodeDetail = new Node();
	 		 Integer orgNodeId = Integer.parseInt(getRequest().getParameter("selectedOrgId").toString());
	 		 System.out.println("userName ::"+this.userName);
	 		 System.out.println("orgNodeId ::"+orgNodeId);
	 		
	 		 try{
	 			orgNodeDetail = this.organizationManagement.getOrganization(this.userName, orgNodeId);
	 			//added on 10.12.2011 for open node functionality in new jstree 
	 			organizationNodes = this.organizationManagement.getAncestorOrganizationNodesForOrgNode(this.userName, orgNodeId);
	 			
	 			messageInfo.setOrganizationDetail(orgNodeDetail);
	 			messageInfo.setOrganizationNodes(organizationNodes);
	 			//
	 			
	 			
	 			Gson gson = new Gson();
				//json = gson.toJson(orgNodeDetail);
	 			json = gson.toJson(messageInfo);
				
				System.out.println(json);
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
	 		 }
	 		 catch(Exception e){
	 			System.err.println("Exception while fetching organization details.");
				e.printStackTrace();
	 		 }
	 		 
	 		 return null;
	 		}
	 	 
	 //saving organization details
	 @Jpf.Action(forwards={
				@Jpf.Forward(name = "success", 
						path ="find_user_by_hierarchy.jsp")
		})
		protected Forward saveAddEditOrg(ManageOrganizationForm form)
		{
		 	
		 String json = "";
			OutputStream stream = null;
			HttpServletRequest req = getRequest();
			HttpServletResponse resp = getResponse();
			Node organizationDetail = new Node();
			Boolean isLaslinkCustomer = new Boolean(getRequest().getParameter("isLaslinkCustomer"));
			organizationDetail.setOrgNodeCategoryId(Integer.parseInt(getRequest().getParameter("layerOptions")));
			organizationDetail.setOrgNodeName(getRequest().getParameter("orgName").trim());
			organizationDetail.setCustomerId(Integer.parseInt((getSession().getAttribute("customerId")).toString()));
			organizationDetail.setOrgNodeCode(getRequest().getParameter("orgCode").trim());
			Integer selectedParentId = Integer.parseInt(getRequest().getParameter("assignedOrgNodeIds"));
			organizationDetail.setParentOrgNodeId(selectedParentId);
			//added on 08.11.2011
			MessageInfo messageInfo = new MessageInfo();
			//Message message = null;
			Integer orgNodeId = null;
			if(getRequest().getParameter("selectedOrgId")!=null){
				orgNodeId = Integer.parseInt(getRequest().getParameter("selectedOrgId").toString());
			}
			boolean isCreateNew = (orgNodeId == null || "".equals(orgNodeId)) ? true : false;
			//
			if(isLaslinkCustomer)
				organizationDetail.setMdrNumber(getRequest().getParameter("mdrNumber").trim());
			String userName = getSession().getAttribute("userName").toString();
			try {
				
				if(isCreateNew){
					organizationDetail = this.organizationManagement.createOrganization(userName, organizationDetail);
					//message = new Message(Message.ADD_TITLE, Message.ADD_SUCCESSFUL, Message.INFORMATION);
					messageInfo = createMessageInfo(messageInfo, Message.ADD_TITLE, Message.ADD_SUCCESSFUL, Message.INFORMATION, false, true );
					messageInfo.setOrganizationDetail(organizationDetail);
					
				}
				else{
					boolean validInfo = true;
					initOrgLevelOption(isCreateNew, orgNodeId, selectedParentId);
					if(isInvalidParent()){
						
						//message = new Message(Message.EDIT_ERROR, Message.INVALID_PARENT, Message.ERROR);
						messageInfo = createMessageInfo(messageInfo, Message.EDIT_ERROR, Message.INVALID_PARENT, Message.ERROR, true, false );
						messageInfo.setOrganizationDetail(organizationDetail);
						validInfo = false;
						
					}
					
					if(validInfo){
						organizationDetail.setOrgNodeId(orgNodeId);
						this.organizationManagement.updateOrganization(this.userName, organizationDetail);
						//message = new Message(Message.EDIT_TITLE, Message.EDIT_SUCCESSFUL, Message.INFORMATION);
						messageInfo = createMessageInfo(messageInfo, Message.EDIT_TITLE, Message.EDIT_SUCCESSFUL, Message.INFORMATION, false, true );
						messageInfo.setOrganizationDetail(organizationDetail);
						messageInfo.setIsEdit(Boolean.TRUE);
						BaseTree baseTree = new BaseTree ();
						ArrayList<Organization> completeOrgNodeList = new ArrayList<Organization>();
						ArrayList <Integer> orgIDList = new ArrayList <Integer>();
						ArrayList<TreeData> data = new ArrayList<TreeData>();
						ArrayList<Organization> selectedList = new ArrayList<Organization> ();
						UserNodeData und = OrganizationPathListUtils.OrgNodehierarchy(this.userName, 
						this.userManagement, orgNodeId); 
						ArrayList<Organization> orgNodesList = OrganizationPathListUtils.buildOrgNodehierarchyList(und, orgIDList, completeOrgNodeList);	
						preTreeProcess (data,orgNodesList,selectedList);
						baseTree.setData(data);
						/*Gson gson1 = new Gson();
						System.out.println("childdataset......:: "+gson1.toJson(baseTree));*/
						messageInfo.setBaseTree(baseTree);
						
					}
				}
			Gson gson = new Gson();
			json = gson.toJson(messageInfo);
			System.out.println("Json after saveOrg ::"+json);
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
			System.err.println("Exception while processing populateLayer");
			e.printStackTrace();
		}
		
		return null;
		 
		}
	 
	 
	 
	 
	 
	 @Jpf.Action(forwards={
				@Jpf.Forward(name = "success", 
						path ="find_user_by_hierarchy.jsp")
		})
		protected Forward uniqueMDRNumber(ManageOrganizationForm form)
		{
		 	
		 String json = "";
			OutputStream stream = null;
			HttpServletRequest req = getRequest();
			HttpServletResponse resp = getResponse();
			String MDRNumber = getRequest().getParameter("mdrNumber");

			try {
				String validMDRNumber = validMDRNumber(MDRNumber);
			Gson gson = new Gson();
			json = gson.toJson(validMDRNumber);
			
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
			System.err.println("Exception while processing populateLayer");
			e.printStackTrace();
		}
		
		return null;
		 
		}
	 
	 
	 
	
	/////////////////////////////////////////////////////////////////////////////////////////////    
	///////////////////////////// BEGIN OF NEW NAVIGATION ACTIONS ///////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////    
	
	 /**
	  * ASSESSMENTS actions
	  */    
	 @Jpf.Action(forwards = { 
			 @Jpf.Forward(name = "sessionsLink", path = "assessments_sessionsLink.do"),
			 @Jpf.Forward(name = "studentScoringLink", path = "assessments_studentScoringLink.do"),
			 @Jpf.Forward(name = "programStatusLink", path = "assessments_programStatusLink.do")
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
	  * ORGANIZATIONS actions
	  */    
	 @Jpf.Action(forwards = { 
			 @Jpf.Forward(name = "studentsLink", path = "organizations_manageStudents.do"),
			 @Jpf.Forward(name = "usersLink", path = "organizations_manageUsers.do"),
			 @Jpf.Forward(name = "organizationsLink", path = "organizations_manageOrganizations.do"),
			 @Jpf.Forward(name = "bulkAccomLink", path = "organizations_manageBulkAccommodation.do"),
		        @Jpf.Forward(name = "bulkMoveLink", path = "organizations_manageBulkMove.do")
	 }) 
	 protected Forward organizations()
	 {
		 String menuId = (String)this.getRequest().getParameter("menuId");    	
		 String forwardName = (menuId != null) ? menuId : "studentsLink";

		 return new Forward(forwardName);
	 }


	 @Jpf.Action(forwards = { 
			 @Jpf.Forward(name = "success", path = "beginFindOrganization.do") 
	 }) 
	 protected Forward organizations_manageOrganizations()
	 {		
		 return new Forward("success");
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
			 @Jpf.Forward(name = "manageLicensesLink", path = "services_manageLicenses.do"),
			 @Jpf.Forward(name = "installSoftwareLink", path = "services_installSoftware.do"),
			 @Jpf.Forward(name = "downloadTestLink", path = "services_downloadTest.do"),
			 @Jpf.Forward(name = "uploadDataLink", path = "services_uploadData.do"),
			 @Jpf.Forward(name = "downloadDataLink", path = "services_downloadData.do")
	 }) 
	 protected Forward services()
	 {
		 String menuId = (String)this.getRequest().getParameter("menuId");    	
		 String forwardName = (menuId != null) ? menuId : "installSoftwareLink";

		 return new Forward(forwardName);
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
	    protected Forward broadcastMessage()
	    {
	        HttpServletRequest req = getRequest();
			HttpServletResponse resp = getResponse();
			OutputStream stream = null;
			
			this.broadcastMessages = getBroadcastMessages();
	        String bcmString = buildBroadcastMessages();
			
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

	    private List getBroadcastMessages()
	    {      
	    	if (this.broadcastMessages == null) {
	    	
		    	if (this.userName == null) {
		    		this.userName = (String)getSession().getAttribute("userName");
		    	}
		    	
		    	this.broadcastMessages = new ArrayList();
		    	
	            try {
	               BroadcastMessageData bmd = new BroadcastMessageData();
	               Integer [] prodId = message.getFrameworkProductForUser(userName);
	               Integer pageSize = null;
	               String qString = "''";
	               
	               if (prodId != null && prodId.length > 0 ){
	            	   qString = SQLutils.convertArraytoString(prodId);
	               }
	              
	               bmd.setBroadcastMessages(message.getProductSpecificBroadcastMsg(qString), null);
	               
	               BroadcastMessage[] bcMessages = bmd.getBroadcastMessages();
		           if (bcMessages.length > 0) {
		                for (int i=0; i<bcMessages.length ; i++) {
		                	this.broadcastMessages.add(bcMessages[i]);
		                }
		           } 
	               
	            } catch (Exception e) {
		            e.printStackTrace();
	            }
	    	}
	    	
	        return this.broadcastMessages;
	    }
	    
	    private String buildBroadcastMessages()
	    {        
	        String html = "<table class='simpletable'>";        
			String messages = "You have no messages at this time. The Messages link will display a numbered red square <span class='messageheader'>&nbsp;</span> when you have active messages.";
			
	        if (this.broadcastMessages.size() > 0)
	        {
	            html += "<tr class='simpletable'>";
	            html += "<th class='simpletable alignLeft'>Message</th><th class='simpletable alignLeft'>Date</th></tr>";
	            html += "</tr>";
	            messages = "";
	            for (int i=0; i<this.broadcastMessages.size(); i++) {
	            	BroadcastMessage bm = (BroadcastMessage)this.broadcastMessages.get(i);
	                html += "<tr class='simpletable'>";
	            	html += "<td class='simpletable'>" + bm.getMessage() + "</td>";
	            	String dateStr = DateUtils.formatDateToDateString(bm.getCreatedDateTime());
	            	html += "<td class='simpletable'>" + dateStr + "</td>";
	                html += "</tr>";
	            }
	        }   
	        else {
	            html += "<tr class='simpletable'><td class='simpletable alignCenter'>";
	        	html += "<br/>";
	        	html += messages;
	        	html += "<br/><br/>";
	            html += "</td></tr>";
	        }
			
	        html += "</table>";
	        
	        return html;    
	    }
	    
	
	@Jpf.Action()
	protected Forward myProfile()
	{
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
        }
        catch (CTBBusinessException be)
        {
            be.printStackTrace();
        }
    }
    
	private void setupUserPermission()
	{
        CustomerConfiguration [] customerConfigs = getCustomerConfigurations(this.customerId);
        boolean adminUser = isAdminUser();
        boolean TABECustomer = isTABECustomer(customerConfigs);
        boolean laslinkCustomer = isLaslinkCustomer(customerConfigs);
        
        this.getSession().setAttribute("showReportTab", 
        		new Boolean(userHasReports().booleanValue() || laslinkCustomer));

        this.getSession().setAttribute("hasUploadDownloadConfigured", 
        		new Boolean( hasUploadDownloadConfig(customerConfigs).booleanValue() && adminUser));
        
        this.getSession().setAttribute("hasProgramStatusConfigured", 
        		new Boolean( hasProgramStatusConfig(customerConfigs).booleanValue() && adminUser));
        
        this.getSession().setAttribute("hasScoringConfigured", 
        		new Boolean( customerHasScoring(customerConfigs).booleanValue() && adminUser));
        
        this.getSession().setAttribute("canRegisterStudent", canRegisterStudent(customerConfigs));
        
     	this.getSession().setAttribute("hasLicenseConfigured", hasLicenseConfiguration(customerConfigs).booleanValue() && adminUser);

     	this.getSession().setAttribute("adminUser", new Boolean(adminUser));     
     	
     	this.getRequest().setAttribute("isLasLinkCustomer", laslinkCustomer);
     	
     	this.getSession().setAttribute("isBulkAccommodationConfigured",customerHasBulkAccommodation(customerConfigs));

		this.getSession().setAttribute("isBulkMoveConfigured",customerHasBulkMove(customerConfigs));
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
    
    private Boolean hasLicenseConfiguration(CustomerConfiguration [] customerConfigs)
    {               
    	 boolean hasLicenseConfiguration = false;

        for (int i=0; i < customerConfigs.length; i++)
        {
        	 CustomerConfiguration cc = (CustomerConfiguration)customerConfigs[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Subscription") && 
            		cc.getDefaultValue().equals("T")	) {
            	hasLicenseConfiguration = true;
                break;
            } 
        }
       
        return new Boolean(hasLicenseConfiguration);
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
    
    private Boolean hasUploadDownloadConfig(CustomerConfiguration[] customerConfigurations)
    {
    	Boolean hasUploadDownloadConfig = Boolean.FALSE;
        if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++) {

				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Upload_Download") && 
						cc.getDefaultValue().equals("T")) {
					hasUploadDownloadConfig = true; 
					break;
				}
			}
		}
        return new Boolean(hasUploadDownloadConfig);
    }

    private Boolean hasProgramStatusConfig(CustomerConfiguration[] customerConfigurations)
    {	
    	Boolean hasProgramStatusConfig = Boolean.FALSE;
        if( customerConfigurations != null ) {
			for (int i=0; i < customerConfigurations.length; i++) {

				CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Allow_Subtest_Invalidation") && 
						cc.getDefaultValue().equals("T")) {
					hasProgramStatusConfig = true; 
					break;
				}
			}
		}
        return new Boolean(hasProgramStatusConfig);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////    
    ///////////////////////////// END OF SETUP USER PERMISSION ///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////    

    
    private static void preTreeProcess (ArrayList<TreeData> data,ArrayList<Organization> orgList,ArrayList<Organization> selectedList) {

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
    
    
 private String validMDRNumber(String mdrNumber) {
        
    	mdrNumber = mdrNumber.trim();
        String mdrNumberFound ="F";
        try {
	        if ( mdrNumber != null && mdrNumber.length()>0 &&  !(mdrNumber.length()< 8)) {
	            
	        	mdrNumberFound = orgNode.checkUniqueMdrNumberForOrgNodes(mdrNumber);
	           
	        }         
        }
        catch (Exception e) {
        }
        
        return mdrNumberFound;  
        
    }
 
 /**
  * This method is responsible to check wheather admin user is belonging to leaf level node
  * @param leafNodeCategoryId
  * @param selectedList
  * @return boolean
  */
 
 private boolean isLeafNodeAdmin (Integer leafNodeCategoryId, ArrayList<Organization> selectedList) {
		
		for (Organization org : selectedList) {
			
			if (leafNodeCategoryId.intValue() == org.getOrgCategoryLevel().intValue()) {
				
				return true;
			}
		}
		
		return false;
	}
    
    
	
/////////////////////////////////////////////////////////////////////////////////////////////
/////// *********************** ManageOrganizationForm ************* /////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////    

  

  /**
   * FormData get and set methods may be overwritten by the Form Bean editor.
   */
  public static class ManageOrganizationForm extends SanitizedFormData
  {
	  
  }	
/////////////////////////////////////////////////////////////////////////////////////////
//////***********For server-side validation ::Added on 09.11.2011
////////////////////////////////////////////////////////////////////////////////////////
  
  /**
   * isInvalidParent
   */
  
  private boolean isInvalidParent() {
      boolean invalidParent = false;
      
      if(this.orgLevelOptions == null || this.orgLevelOptions.size()==0){
          invalidParent = true;
      }
      else if(this.orgLevelOptions.size()== 1 && this.orgLevelOptions.containsKey("")){
          invalidParent = true;
      }
       
      return invalidParent; 
  } 
  
  private void initOrgLevelOption(boolean isCreateNew ,Integer orgId,Integer selectedParentId)
  {        
      this.orgLevelOptions = new LinkedHashMap();
      OrgNodeCategory[] orgCatagories = null;
      boolean addFlag = true;
     
      if (isCreateNew) {
    	  
          this.orgLevelOptions.put("", Message.SELECT_ENTITY);
          
      }
      
      if (!isCreateNew) {
          
          addFlag = false;
      }
      
      try {
          orgCatagories = organizationManagement.
                                  getFrameworkListForOrg(orgId, 
                                  selectedParentId, addFlag);            
          if (orgCatagories != null) {
              
              for (int i = 0; i < orgCatagories.length ; i++) {
                  
                  this.orgLevelOptions.put(orgCatagories[i].getOrgNodeCategoryId(), 
                          orgCatagories[i].getCategoryName());
              }
          }
      }
      catch (Exception be) {
         be.printStackTrace();
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
////////////////////////////////////////////////////////////////////////////////////  
  
}

   