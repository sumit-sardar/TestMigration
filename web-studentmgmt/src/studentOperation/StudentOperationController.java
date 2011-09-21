package studentOperation;

import java.io.ObjectOutput;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import utils.Base;
import utils.BaseTree;
import utils.FilterSortPageUtils;
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
import com.ctb.bean.studentManagement.ManageStudentData;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserNodeData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.web.sanitizer.SanitizedFormData;
import com.google.gson.Gson;

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
	// customer configuration
	CustomerConfiguration[] customerConfigurations = null;
	CustomerConfigurationValue[] customerConfigurationsValue = null;
	
	
	//Constants
	public static String CONTENT_TYPE_JSON = "application/json";
	
	/**
	 * @return the customerConfigurationsValue
	 */
	public CustomerConfigurationValue[] getCustomerConfigurationsValue() {
		return customerConfigurationsValue;
	}

	/**
	 * @param customerConfigurationsValue the customerConfigurationsValue to set
	 */
	public void setCustomerConfigurationsValue(
			CustomerConfigurationValue[] customerConfigurationsValue) {
		this.customerConfigurationsValue = customerConfigurationsValue;
	}

	/**
	 * @param customerConfigurations the customerConfigurations to set
	 */
	public void setCustomerConfigurations(
			CustomerConfiguration[] customerConfigurations) {
		this.customerConfigurations = customerConfigurations;
	}

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
		this.getRequest().setAttribute("isBulkAccommodationConfigured",customerHasBulkAccommodation());
		this.getRequest().setAttribute("isScoringConfigured", customerHasScoring());
		this.getRequest().setAttribute("canRegisterStudent",canRegisterStudent());
		boolean isLasLinkCustomer = isLasLinkCustomer();
		this.getRequest().setAttribute("isLasLinkCustomer", isLasLinkCustomer);  
		this.getRequest().setAttribute("isTopLevelUser",isTopLevelUser(isLasLinkCustomer));
		this.getRequest().setAttribute("userHasReports", userHasReports());
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
			sort = FilterSortPageUtils.buildStudentSortParams(FilterSortPageUtils.STUDENT_DEFAULT_SORT_COLUMN, FilterSortPageUtils.ASCENDING);
			msData = StudentSearchUtils.searchStudentsByOrgNode(this.userName, this.studentManagement, selectedOrgNodeId, filter, page, sort);
			
		}

		return msData;
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
	 * getCustomerConfigurations
	 */
	private void getCustomerConfigurations()
	{
		try {
				this.customerConfigurations = this.studentManagement.getCustomerConfigurations(this.userName, this.customerId);
		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}
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
		 for (int i=0; i < this.customerConfigurations.length; i++) {
			 
	           CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
	            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Bulk_Accommodation") && 
	    	        		cc.getDefaultValue().equals("T")) {
	                	hasBulkStudentConfigurable = true; 
	                	break;
	             }
	      }
		return new Boolean(hasBulkStudentConfigurable);           
	}

		/**
		 * This method checks whether customer is configured to access the scoring feature or not.
		 * @return Return Boolean 
		 */
	
	
	private Boolean customerHasScoring()
    {               
        boolean hasScoringConfigurable = false;

        for (int i=0; i < customerConfigurations.length; i++)
        {
        	 CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
            if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Hand_Scoring") && 
            		cc.getDefaultValue().equals("T")	) {
            	hasScoringConfigurable = true;
                break;
            } 
        }
        
        return new Boolean(hasScoringConfigurable);
    }
	
	 private Boolean canRegisterStudent() 
	    {               
	        String roleName = this.user.getRole().getRoleName();        
	        boolean validCustomer = false; 

	        for (int i=0; i < customerConfigurations.length; i++)
	        {
	            CustomerConfiguration cc = (CustomerConfiguration)customerConfigurations[i];
	            if (cc.getCustomerConfigurationName().equalsIgnoreCase("TABE_Customer"))
	            {
	                validCustomer = true; 
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
	    
	  
		private boolean isLasLinkCustomer()
	    {               
	        boolean isLasLinkCustomer = false;
				 for (int i=0; i < this.customerConfigurations.length; i++)
	            {
	            	 CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
	            	//isLasLink customer
	                if (cc.getCustomerConfigurationName().equalsIgnoreCase("LASLINK_Customer") && cc.getDefaultValue().equals("T")	)
	                {
	                	isLasLinkCustomer = true;
	                    break;
	                } 
	            }
	        return isLasLinkCustomer;
	    }
		
		
		 private String generateTree (ArrayList<Organization> orgNodesList) throws Exception{	
		    	
				Organization org = orgNodesList.get(0);
				TreeData td = new TreeData ();
				td.setData(org.getOrgName());
				td.getAttr().setId(org.getOrgNodeId().toString());
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
/////// *********************** MANAGESTUDENTFORM ************* /////////////////////////////    
/////////////////////////////////////////////////////////////////////////////////////////////    
	/**
	 * FormData get and set methods may be overwritten by the Form Bean editor.
	 */
	public static class StudentOperationForm extends SanitizedFormData
	{
	}
}

