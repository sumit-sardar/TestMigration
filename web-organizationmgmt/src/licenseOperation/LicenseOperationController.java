package licenseOperation;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import utils.LicenseBase;
import utils.Organization;
import utils.OrganizationPathListUtils;
import utils.OrgnizationComparator;
import utils.PermissionsUtils;
import utils.TreeData;

import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.CustomerLicense;
import com.ctb.bean.testAdmin.LicenseNodeData;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.NodeData;
import com.ctb.bean.testAdmin.OrgNodeLicenseInfo;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.exception.CTBBusinessException;
import com.google.gson.Gson;

import dto.LicenseNode;
import dto.OrganizationProfileInformation;

@Jpf.Controller()
public class LicenseOperationController extends PageFlowController {
	private static final long serialVersionUID = 1L;

    @Control()
    private com.ctb.control.testAdmin.TestSessionStatus testSessionStatus;

    @Control()
    private com.ctb.control.licensing.Licensing licensing;
    
    @Control()
    private com.ctb.control.userManagement.UserManagement userManagement;
    
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.BroadcastMessageLog message;
    
    @Control()
    private com.ctb.control.organizationManagement.OrganizationManagement organizationManagement;
    
	private String userName = null;
	private Integer customerId = null;
    private User user = null;

    private CustomerLicense[] customerLicenses = null;
    
    private List licenseNodes = null;
    
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
	 * @jpf:forward name="success" path="services.do"
	 */
	@Jpf.Action(forwards = { 
			@Jpf.Forward(name = "success", path = "services.do")
	})
	protected Forward begin()
	{
		return new Forward("success");
	}
	
	
	
	
    /**
     * @jpf:action
     */
    @Jpf.Action()
	protected Forward loadOrgNodeTree(){

		String jsonTree = "";
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		String contentType = "application/json";
		
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
	    		//System.out.println("acceptEncoding..."+acceptEncoding.toString());

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
	
 private boolean isLeafNodeAdmin (Integer leafNodeCategoryId, ArrayList<Organization> selectedList) {
		
		for (Organization org : selectedList) {
			
			if (leafNodeCategoryId.intValue() == org.getOrgCategoryLevel().intValue()) {
				
				return true;
			}
		}
		
		return false;
	}
    

 	/**
 	 * @jpf:action
 	 */
 	@Jpf.Action()
	protected Forward loadChildrenOrgNodeLicense() {
		
		 HttpServletRequest req = getRequest();
		 HttpServletResponse resp = getResponse();
		 Integer treeOrgNodeId = Integer.parseInt(getRequest().getParameter("treeOrgNodeId"));
		 List orgNodeLicenses = new ArrayList(0);
		 OutputStream stream = null;
		 String json = "";
		 ObjectOutput output = null;
		 CustomerLicense cl = this.customerLicenses[0];
		 
		 try {
			 
				NodeData subOrganizationList = OrganizationPathListUtils.getOrganizationNodesForParentIncludingParentName(this.userName, this.organizationManagement, treeOrgNodeId);
				if (subOrganizationList != null) {
			    	orgNodeLicenses = buildOrgNodeLicenses(subOrganizationList, cl, treeOrgNodeId);
		        }
				
				LicenseBase base = new LicenseBase();
				base.setPage("1");
				base.setRecords("10");
				Gson gson = new Gson();
				base.setOrgNodeLicenses(orgNodeLicenses);
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
			e.printStackTrace();
		}
			
		return null;
			
	}
 
 	/**
 	 * @jpf:action
 	 */
 	@Jpf.Action()
	protected Forward loadOrgNodeLicense() {
		
		 HttpServletRequest req = getRequest();
		 HttpServletResponse resp = getResponse();
		 Integer treeOrgNodeId = Integer.parseInt(getRequest().getParameter("treeOrgNodeId"));
		 OutputStream stream = null;
		 String json = "";
		 ObjectOutput output = null;
		 CustomerLicense cl = this.customerLicenses[0];
		   
		 try {
			 
			 Node node = this.organizationManagement.getOrganization(this.userName, treeOrgNodeId);
			 
			 LicenseNode licenseNode = findCachedLicenseNode(treeOrgNodeId);
			 if (licenseNode == null) {
				 licenseNode = getOrgNodeLicense(node.getOrgNodeName(), treeOrgNodeId, cl, node.getParentOrgNodeId());
			 }
		 
			 List orgNodeLicenses = new ArrayList();
			 orgNodeLicenses.add(licenseNode);

			 LicenseBase base = new LicenseBase();
			 Gson gson = new Gson();
			 base.setOrgNodeLicenses(orgNodeLicenses);
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
			e.printStackTrace();
		}
			
		return null;
			
	}
 	
    /**
     * getOrgNodeLicense
     */    
    public LicenseNode getOrgNodeLicense(String orgNodeName, Integer orgNodeId, CustomerLicense cl, Integer parentId) 
    {
        OrgNodeLicenseInfo onli = getLicenseQuantitiesByOrg(orgNodeId, cl.getProductId(), cl.getSubtestModel());
        LicenseNode licenseNode = new LicenseNode();
        licenseNode.setName(orgNodeName);
        licenseNode.setId(orgNodeId);   
        
        licenseNode.setProductId(cl.getProductId());
        licenseNode.setSubtestModel(cl.getSubtestModel());
       	licenseNode.setReserved(licenseNode.ConvertIntegerToString(onli.getLicReserved()));
       	licenseNode.setConsumed(licenseNode.ConvertIntegerToString(onli.getLicUsed()));
        licenseNode.setAvailable(licenseNode.ConvertIntegerToString(onli.getLicPurchased()));
                                        
        licenseNode.setParentId(parentId);
		storeLicenseNode(licenseNode);
        
        return licenseNode;
    }
 	
    /**
     * buildOrgNodeLicenses
     */    
    public List buildOrgNodeLicenses(NodeData nData, CustomerLicense cl, Integer parentId) 
    {
        ArrayList organizationList = new ArrayList();
        if (nData != null) {
            Node[] nodes = nData.getNodes();
            if(nodes != null){
                for (int i=0 ; i<nodes.length ; i++) {
                	Node node = nodes[i];
                    if (node != null) {
                    	
           			 	LicenseNode licenseNode = findCachedLicenseNode(node.getOrgNodeId());
                    	if (licenseNode == null) {
                    		licenseNode = getOrgNodeLicense(node.getOrgNodeName(), node.getOrgNodeId(), cl, parentId); 
                    	}
                        
                        organizationList.add(licenseNode);
                    }
                }
            }
        }
        return organizationList;
    }
 	
    /**
     * getLicenseQuantitiesByOrg
     */    
    private OrgNodeLicenseInfo getLicenseQuantitiesByOrg(Integer orgNodeId, Integer productId, String subtestModel) {
        OrgNodeLicenseInfo onli = null;
        try {
            onli = this.licensing.getLicenseQuantitiesByOrgNodeIdAndProductId(this.userName, 
										                    orgNodeId, 
										                    productId, 
										                    subtestModel);
        }    
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return onli;
    }
    
 	/**
 	 * @jpf:action
 	 */
 	@Jpf.Action()
	protected Forward updateCellValue(){

 		HttpServletRequest req = getRequest();
 		String orgNodeId = req.getParameter("id");
		if (orgNodeId == null) 
			orgNodeId = "0";
 		String available = req.getParameter("available");
		if (available == null) 
			available = "0";
		String result = "ERROR";
 		
		HttpServletResponse resp = getResponse();
		OutputStream stream = null;
		 
		 if (! "ERROR".equalsIgnoreCase(available)) {
			 
			 result = updateLicenseNode(new Integer(orgNodeId), new Integer(available));
			 
		 }
		 
		 try {
			 try {
				 resp.setContentType("text/html");
				 stream = resp.getOutputStream();
				 resp.flushBuffer();
				 stream.write(result.getBytes());
			 }
			 finally{
				if (stream!=null){
					stream.close();
				}
			}
		 } catch (IOException e) {
			e.printStackTrace();
		 }
 		
		return null;
	}
 		
 	
 	private void storeLicenseNode(LicenseNode srcNode) {

 		boolean found = false;
 		for (int i=0 ; i<this.licenseNodes.size() ; i++) {
 			LicenseNode node = (LicenseNode)this.licenseNodes.get(i);
 			if (node.getId().intValue() == srcNode.getId().intValue()) {
 	 			found = true;
 	 			break;
 	 		}
 		}
 		if (! found) {
 			this.licenseNodes.add(srcNode);
 		}
 	}

 	private LicenseNode findCachedLicenseNode(Integer orgNodeId) {

 		for (int i=0 ; i<this.licenseNodes.size() ; i++) {
 			LicenseNode node = (LicenseNode)this.licenseNodes.get(i);
 			if (node.getId().intValue() == orgNodeId.intValue()) {
 				return node;
 	 		}
 		}
 		return null;
 	}
 	
 	private String updateLicenseNode(Integer orgNodeId, Integer available) {
 		
		String result = "OK";
 		
 	 	LicenseNode srcNode = findCachedLicenseNode(orgNodeId);
 	 	if (srcNode != null) {
 	 	 	LicenseNode parentNode = findCachedLicenseNode(srcNode.getParentId());
 	 	 	Integer parentAvailable = new Integer(parentNode.getAvailable());
 	 	 	Integer srcAvailable = new Integer(srcNode.getAvailable());
 	 	 	
 	 	 	int diff = available.intValue() - srcAvailable.intValue();
 	 	 	int amount = parentAvailable.intValue() - diff; 
 	 	 	if (amount < 0) {
 	 	 		result = "You do not have enough available licenses to allocate.";
 	 	 	}
 	 	 	else {
 	 	 		srcNode.setAvailable(available.toString());
 	 	 		parentNode.setAvailable(String.valueOf(amount));
 	 	 	}
 	 	}
 	 	
 		
 		/*
 		for (int i=0 ; i<this.licenseNodes.size() ; i++) {
 			LicenseNode node = (LicenseNode)this.licenseNodes.get(i);
 			System.out.println(node.getName() + " - " + node.getAvailable());
 		}
 		*/
 	 	
 	 	
 		return result;
 	}
 	
 	
 	/**
 	 * @jpf:action
 	 */
 	@Jpf.Action()
	protected Forward saveLicenses() {
		
		Integer customerId = this.user.getCustomer().getCustomerId();       
        int numberNodes = this.licenseNodes.size();
        LicenseNodeData [] licenseNodeDataList = new LicenseNodeData[numberNodes];
        
        for (int i = 0 ; i < numberNodes ; i++) {           
        	LicenseNode licenseNode = (LicenseNode)this.licenseNodes.get(i);
        	Integer orgNodeId = licenseNode.getId();
        	String name = licenseNode.getName();
        	String available = licenseNode.getAvailable();
        	Integer productId = licenseNode.getProductId();
        	String subtestModel = licenseNode.getSubtestModel();
        	
        	LicenseNodeData licenseNodeData = new LicenseNodeData();
        	licenseNodeData.setProductId(productId);
        	licenseNodeData.setAvailable(available);
        	licenseNodeData.setOrgNodeId(orgNodeId);
        	licenseNodeData.setSubtestModel(subtestModel);
        	licenseNodeData.setCustomerId(customerId);
        	licenseNodeDataList[i] = licenseNodeData;
      	
System.out.println("orgNodeId=" + orgNodeId + "    name=" + name + "    productId=" + productId + "    subtestModel=" + subtestModel + "    customerId=" + customerId + "    available=" + available);
        }

        /*
        try {
			result = this.licensing.saveOrUpdateOrgNodeLicenseDetail(licenseNodeDataList);
		} catch (CTBBusinessException e) {
			e.printStackTrace();
		}
        */
        
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
	        @Jpf.Forward(name = "bulkMoveLink", path = "organizations_manageBulkMove.do"),
	        @Jpf.Forward(name = "OOSLink", path = "organizations_manageOutOfSchool.do")
	}) 
	protected Forward organizations()
	{
		String menuId = (String)this.getRequest().getParameter("menuId");    	
		String forwardName = (menuId != null) ? menuId : "studentsLink";

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

		 return new Forward(forwardName);
	 }

	 @Jpf.Action(forwards = { 
			 @Jpf.Forward(name = "success", path = "services_manageLicenses.jsp") 
	 }) 
	 protected Forward services_manageLicenses()
	 {
		 getLoggedInUserPrincipal();

		 getUserDetails();

		 setupUserPermission();

		 List broadcastMessages = BroadcastUtils.getBroadcastMessages(this.message, this.userName);
		 this.getSession().setAttribute("broadcastMessages", new Integer(broadcastMessages.size()));
		 
		 this.customerLicenses = getCustomerLicenses();
		 
		 this.licenseNodes = new ArrayList();
		 
		 return new Forward("success");
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

        this.getSession().setAttribute("isBulkAccommodationConfigured",customerHasBulkAccommodation(customerConfigs));    	
    	
        this.getSession().setAttribute("hasUploadDownloadConfigured", 
        		new Boolean( hasUploadDownloadConfig().booleanValue() && adminUser));
        
        this.getSession().setAttribute("hasProgramStatusConfigured", 
        		new Boolean( hasProgramStatusConfig().booleanValue() && adminUser));
        
        this.getSession().setAttribute("hasScoringConfigured", 
        		new Boolean( customerHasScoring(customerConfigs).booleanValue() && adminUser));
        
        this.getSession().setAttribute("canRegisterStudent", canRegisterStudent(customerConfigs));
        
     	this.getSession().setAttribute("hasLicenseConfigured", hasLicenseConfiguration() && adminUser);

		this.getSession().setAttribute("isBulkMoveConfigured",customerHasBulkMove(customerConfigs));
		
     	this.getSession().setAttribute("adminUser", new Boolean(adminUser));
     	
     	this.getSession().setAttribute("isOOSConfigured",customerHasOOS(customerConfigs));	// Changes for Out Of School
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

    /////////////////////////////////////////////////////////////////////////////////////////////    
    ///////////////////////////// END OF SETUP USER PERMISSION ///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////    

	
}