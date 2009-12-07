package com.ctb.util; 

import com.ctb.bean.testAdmin.Address;
import com.ctb.bean.testAdmin.Customer;
//import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfig;
import com.ctb.bean.testAdmin.CustomerEmail;
import com.ctb.control.db.Users;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import com.ctb.control.db.UploadDataFile;
import com.ctb.bean.testAdmin.DataFileAudit;
import com.ctb.bean.testAdmin.DataFileRowError;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.Role;
import com.ctb.bean.testAdmin.TimeZones;
import com.ctb.bean.testAdmin.USState;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserData;
import com.ctb.bean.testAdmin.UserFile;
import com.ctb.bean.testAdmin.UserFileRow;
import com.ctb.bean.testAdmin.UserNode;
import com.ctb.control.db.OrgNode;
import com.ctb.control.db.Roles;
import com.ctb.control.organizationManagement.OrganizationManagement;
import com.ctb.control.userManagement.UserManagement;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.uploadDownloadManagement.FileNotUploadedException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.poi.hssf.record.formula.functions.Char;
import com.ctb.util.CTBConstants;
import com.ctb.util.UploadErrorData;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.util.HashSet;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.util.HSSFColor;

/*
 * Userd for UploadProcess 
 * @author  Tata Consultancy Services
 * 
 */

public class UploadProcess extends BatchProcessor.Process
{ 
    
    private HashMap storedPhaseGradeMap;
    private int phaseId;
    private String username;
    private InputStream uploadedStream;
    private int custOeNodeId;
    private String email;
    private Date uploadDt;
    private int noOfUserColumn;
    private UploadDataFile uploadDataFile;
    private Integer customerId;
    private int failedRecordCount;
    private int uploadRecordCount;
    private HashMap commonHierarchyMap;
    public UserFile userFile;
    public DataFileAudit dataFileAudit = new DataFileAudit();
    public OrgNode orgNode;
    public Users users;
    public com.ctb.control.db.Customer customerdb;
    public OrganizationManagement organizationManagement;
    public UserManagement userManagement;
    public com.ctb.control.db.OrgNodeCategory orgNodeCate;
    public UserNode[] usernode = null;
    public com.ctb.bean.testAdmin.OrgNodeCategory orgNodeCategory[] = null;
    public com.ctb.control.db.Addresses addresses;
    public Roles roles;
    private UserFileRow []userFileRowHeader;
    private String serverFilePath;
    private HashMap roleMap = new HashMap();
    private HashMap stateMap = new HashMap();
    private HashMap timeZoneMap = new HashMap();
    private Node []userTopOrgNode = null;
    
    TimeZones[] timeZones;
    USState[] usState;
    Role[] role;
    public int traversCells = 0 ;
    HashMap orgCategory = new HashMap();
   
    //Changed 04/12/2008
    private Node [] detailNodeM = null;
    
    private HashMap visibleUsers = null;
    
    
    public UploadProcess (String serverFilePath, String username, 
            InputStream uploadedStream , 
            UserFileRow []userFileRowHeader,
            int noOfUserColumn , HashMap commonHierarchyMap,
            OrgNode orgNode , com.ctb.control.db.OrgNodeCategory orgNodeCate, 
            Users users,com.ctb.control.db.Customer customerdb,
            UploadDataFile uploadDataFile, 
            OrganizationManagement organizationManagement,
            UserManagement userManagement,com.ctb.control.db.Addresses addresses, 
            Roles roles,DataFileAudit dataFileAudit,
            Node []userTopOrgNode) {
                
               
        setPriority(BatchProcessor.PRIORITY_NORMAL);

        setUsername(username);
        setUploadedFilename(uploadedStream);
        setUploadDt(new Date());
        setNoOfHeaderRows(noOfUserColumn);     
        setCommonHierarchyMap(commonHierarchyMap);
        //set UserHeader
        this.userFileRowHeader = userFileRowHeader;
        this.orgNode = orgNode;
        this.users = users;
        this.customerdb = customerdb;
        this.organizationManagement = organizationManagement;
        this.userManagement = userManagement;
        this.orgNodeCate = orgNodeCate;
        this.uploadDataFile = uploadDataFile;
        this.dataFileAudit = dataFileAudit;
        this.serverFilePath = serverFilePath;
        this.addresses = addresses;
        this.roles = roles;
        this.userTopOrgNode = userTopOrgNode;
        initList(); 
        
        run();
    }
    
    public void run () {
    
        //POI details Initialize
        POIFSFileSystem pfs = null;
        HSSFSheet sheet = null;
        
        //User File Row Initialize
        
        UserFileRow userFileRow = null;
        //ErrorMap initilization
        HashMap requiredMap = new HashMap();
        HashMap maxLengthMap = new HashMap();
        HashMap invalidCharMap = new HashMap();
        HashMap logicalErrorMap = new HashMap();
        HashMap hierarchyErrorMap = new HashMap();
        HashMap userDataMap = new HashMap();
        HashMap blankRowMap = new HashMap();
        boolean isMatchUploadOrgIds = false;
        boolean isBlankRow = true;
        User user = null;
        
        String strCellName = "";
        String strCellId ="";
        String strCellHeaderName = "";
        String strCellHeaderId = "";
        
        int loginUserPosition = 0;
        
        try {
            
            
            
            // retrive login userDetail
            user = userManagement.getUserUpload(this.username,this.username);  
            
            visibleUsers = new HashMap();
            UserData userData = userManagement.getUsersVisibleToUserUpload(
                    this.username, null, null, null);
                    
            if(userData != null) {
                User[] userArray = userData.getUsers();
                if(userArray != null) {
                    for(int i=0;i<userArray.length;i++) {
                        visibleUsers.put(userArray[i].getUserName(), userArray[i]);
                    }
                }
            }
            
            //CUSTOMER CONFIGURATION CHECK
            
            //Changed for performance on 18/12/08
            //Customer customer = this.users.getCustomer(this.username);
            Customer customer = user.getCustomer();
           
            isMatchUploadOrgIds = 
                    this.uploadDataFile.checkCustomerConfigurationEntries(
                    customer.getCustomerId(),CTBConstants.CUSTOMER_CONF_NAME);
            
            
            // Read UploaderFile through POI     
            pfs = new POIFSFileSystem( uploadedStream );
            HSSFWorkbook wb = new HSSFWorkbook(pfs);
            sheet = wb.getSheetAt(0);
            int totalRows = 0;
            if ( sheet != null ) {
                
                totalRows =  sheet.getPhysicalNumberOfRows();
                        
            }
            //retrive each row from uploaded excel sheet for validation
            HSSFRow rowHeader = sheet.getRow(0);
            for ( int i = 1; i < totalRows; i++ ) {
                
                //Initialize
                
                System.out.println("    ***** Upload Control: Processing row " + i); 
                
                HSSFRow row = sheet.getRow(i);
                if (row == null) {
                    
                    totalRows++;
                    continue;
                    
                } else {
                     
                     int totalCells = rowHeader.getPhysicalNumberOfCells();         
                    // retrive each cell value for user
                     for (int k = 0; k < totalCells; k++) {
            
                     //   HSSFCell cellHeader = rowHeader.getCell((short)k);
                     //   Cell type 3 means it is a blank cell
                        HSSFCell cell = row.getCell((short)k);
                         
                        if ( cell != null && (!getCellValue(cell).trim().equals("") 
                                && !(cell.getCellType() == 3))) {
                             
                             isBlankRow = false;                  
                             
                        } 
                        
                       
                    }
                    
                    if (isBlankRow) {
                    
                        blankRowMap.put(new Integer(i),"BlankRow");
                        continue;
                    
                    }
                    
                }
                getEachRowUserDetail(i, row,rowHeader, requiredMap,
                        maxLengthMap, invalidCharMap, logicalErrorMap, user, isMatchUploadOrgIds);
                
                //check if any required fieldmissing, invalid char, 
                //maxlength exceed, logical error has been occured        
                if (!(requiredMap.containsKey(new Integer(i)) 
                        || invalidCharMap.containsKey(new Integer(i)) 
                        || maxLengthMap.containsKey(new Integer(i)) 
                        || logicalErrorMap.containsKey(new Integer(i)))) {
                    
                    
                    if ( isCommonPathValid (i, row, rowHeader, hierarchyErrorMap, isMatchUploadOrgIds , this.userTopOrgNode) ) {
                        
                                            
                        loginUserPosition = getLoginUserOrgPosition(row, rowHeader, this.userTopOrgNode);
                        //Admin validation
                        if ( !hasOrganization(loginUserPosition, row) ) {
                            
                            if (getRoleName(row, rowHeader).equalsIgnoreCase(
                                    CTBConstants.ROLE_NAME_ADMIN)) {
                                
                                if( !isLoginUserDetails(row,rowHeader,user,this.users))  {                                       
                                    //excel write process admin validation fail    
                                    ArrayList logicalErrorList  = new ArrayList();
                                    logicalErrorList.add(CTBConstants.REQUIREDFIELD_ROLE); 
                                    logicalErrorMap.put(new Integer(i),logicalErrorList);     
                                }
                            }
                        } else {
                         
                               // create Organization process
                               Node []node = this.userFileRowHeader[0].
                                                    getOrganizationNodes();
                               int orgHeaderLastPosition = node.length * 2 ;
                               
                               for ( int j = loginUserPosition + 2; 
                                            j < orgHeaderLastPosition; j = j + 2 ) {
                                    
                                    HSSFCell cellHeaderName = rowHeader.getCell(j);
                                    HSSFCell cellHeaderId = rowHeader.getCell(j + 1);
                                    HSSFCell cellName = row.getCell(j);
                                    HSSFCell cellId = row.getCell(j + 1);
                                    
                                    strCellName = getCellValue(cellName);
                                    strCellId = getCellValue(cellId);
                                    strCellHeaderName = getCellValue(cellHeaderName);
                                    strCellHeaderId = getCellValue(cellHeaderId);
                                    
                                    // OrgName required check
                                    if ( strCellName.equals("")  && hasOrganization(j, row) 
                                            && !strCellId.equals("") ) {
                                        
                                        // write excel  required  with the help of cellHeaderName
                                        ArrayList requiredList = new ArrayList();
                                        requiredList.add(strCellHeaderName); 
                                        requiredMap.put(new Integer(i), requiredList);
                                        
                                                                              
                                        break;
                                        
                                    
                                    } else if (strCellName.equals("") && hasOrganization(j - 2, row) 
                                                    && !strCellId.equals("")) {
                                        
                                        // write excel  required  with the help of cellHeaderName
                                        ArrayList requiredList = new ArrayList();
                                        requiredList.add(strCellHeaderName); 
                                        requiredMap.put(new Integer(i), requiredList);
                                        
                                                                              
                                        break;
                                        
                                    } else { 
                                    
                                        //OrgName invalid char check
                                        if ( validString(strCellName) ) {
                                            
                                            //OrgCode invalid char check
                                            if ( !validString(strCellId) ) {
                                                
                                                // write excel  invalid  with the help of cellHeaderID
                                                ArrayList invalidList = new ArrayList ();
                                                invalidList.add(strCellHeaderId);
                                                invalidCharMap.put(new Integer(i), invalidList);
                                                break;
                                                
                                            } else {
                                                
                                                // maxlength checking
                                                boolean flag = false;
                                                                                           
                                                if ( !isMaxLength50(strCellName) ) {
                                                   
                                                    ArrayList maxLengthList = new ArrayList();
                                                    maxLengthList.add(strCellHeaderName);
                                                    maxLengthMap.put(new Integer(i), maxLengthList);
                                                    flag = true;
                                                    
                                                }
                                                
                                                if ( !isMaxLength32(strCellHeaderId) ) {
                                                    
                                                    ArrayList maxLengthList = new ArrayList();
                                                    maxLengthList.add(strCellHeaderId);
                                                    maxLengthMap.put(new Integer(i), maxLengthList);
                                                    flag = true;
                                                    
                                                }
                                                
                                                if (flag) {
                                                    
                                                    break;
                                                    
                                                }
                                                
                                            }
                                              
                                        } else {
                                            
                                            // write excel  invalid  with the help of cellHeaderName
                                            ArrayList invalidList = new ArrayList ();
                                            invalidList.add(strCellHeaderName);
                                            invalidCharMap.put(new Integer(i), invalidList);
                                            break;
                                        }
                                      
                                    }
                    
                                } //end for 
                          
                        } // end else (Organization validation)
                    
                          
                    } // end if (commonpath validation)
                    
                } //end if (User Validation)
                
               isBlankRow = true; 
                
            } //end outer for 
            
           if (requiredMap.size() > 0 || maxLengthMap.size() > 0 
                    || invalidCharMap.size() > 0 || logicalErrorMap.size() > 0 
                    || hierarchyErrorMap.size() > 0) {
                
                errorExcelCreation (requiredMap, maxLengthMap, invalidCharMap, 
                        logicalErrorMap, hierarchyErrorMap); 
           }
           //create user and organization
           createOrganizationAndUser (requiredMap, maxLengthMap, 
                                            invalidCharMap, logicalErrorMap, 
                                            hierarchyErrorMap, userDataMap, 
                                            blankRowMap, isMatchUploadOrgIds, user,
                                            this.userTopOrgNode);
          
                                            
                                                    
        
        } catch (Exception e) {
            
            e.printStackTrace();    
        }
    
    } 
    
    
 ///////////////////////////////private methods////////////////////////////////////////////////////////   
    
    /*
     * initList ()
     * Initialize RoleMap,TimeZoneMap,StateMap
    */ 

    private void initList () {

         try{
            String[] roleNames = {CTBConstants.ROLE_NAME_ADMIN
                                    , CTBConstants.ROLE_NAME_ACCOMMODATIONS_COORDINATOR
                                    , CTBConstants.ROLE_NAME_COORDINATOR
                                    , CTBConstants.ROLE_NAME_PROCTOR};
            Role[] roles = this.roles.getRoles(SQLutils.convertArraytoString(roleNames)); 
            for ( int i = 0; i < roles.length ; i++ ) {

                roleMap.put(roles[i].getRoleName(), roles[i].getRoleId());

            }



            TimeZones[] timezones = this.users.getTimeZones();
            for ( int i = 0; i < timezones.length ; i++ ) {

                timeZoneMap.put(timezones[i].getTimeZoneDesc(), 
                        timezones[i].getTimeZone());

            }

            USState[] states = this.addresses.getStates();
            
            //Solution for defect 55851
            for ( int i = 0; i < states.length ; i++ ) {

                stateMap.put(initCap(states[i].getStatePrDesc()), states[i].getStatePr());

            }
            
            //Changed 04/12/2008
           this.detailNodeM = this.uploadDataFile.
                                        getUserDataTemplate(this.username); 

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    
   
   
   /*
   * create Organization and User
   */ 
   private void createOrganizationAndUser(HashMap requiredMap, 
                                          HashMap maxLengthMap,
                                          HashMap invalidCharMap, 
                                          HashMap logicalErrorMap,
                                          HashMap hierarchyErrorMap, 
                                          HashMap userDataMap,
                                          HashMap blankRowMap, 
                                          boolean isMatchUploadOrgIds,
                                          User loginUser,
                                          Node []loginUserNodes) 
                                          throws SQLException,CTBBusinessException {
    
        POIFSFileSystem pfs = null;
        HSSFSheet sheet = null; 
        int loginUserOrgPosition = 0; 
        Node organization = null;
        Integer orgNodeId = null;
        int uploadRecordCount = 0;
        boolean isBlankRow = true;
        try {
            //changed for perofrmance on 18/12/2008
            //Customer customer = users.getCustomer(this.username);
            Customer customer = loginUser.getCustomer();
            
            pfs = new POIFSFileSystem( new FileInputStream(this.serverFilePath) );
                
            HSSFWorkbook wb = new HSSFWorkbook(pfs);
            sheet = wb.getSheetAt(0);
            int totalRows = 0;
                
            if ( sheet != null ) {
                
                totalRows =  sheet.getPhysicalNumberOfRows();
                        
            }
            
            HSSFRow rowHeader = sheet.getRow(0);
            //find the statr position of the user details header
            Node []nodeCategory = this.userFileRowHeader[0].getOrganizationNodes();
            int orgHeaderLastPosition = nodeCategory.length * 2;    
        //    Node []loginUserNodes =  orgNode.getTopNodesForUser(this.username);
            //travers the entire sheet to update the db for user insertion 
            for ( int i = 1; i < totalRows; i++ ) {
                
                System.out.println("    ***** Upload Control: 2nd loop: Processing row " + i);
               
                HSSFRow bodyRow = sheet.getRow(i);
                
                if (bodyRow == null) {
                    
                    totalRows++;
                    continue;
                    
                } else {
                      
                     int totalCells = rowHeader.getPhysicalNumberOfCells();         
                    // retrive each cell value for user
                    for (int k = 0; k < totalCells; k++) {
            
                     //   HSSFCell cellHeader = rowHeader.getCell((short)k);
                        HSSFCell cell = bodyRow.getCell((short)k);
                         
                        if ( cell != null && (!getCellValue(cell).trim().equals("") 
                                && !(cell.getCellType() == 3)) ) {
                             
                             isBlankRow = false;                  
                             
                        } 
                    }
                    
                    if (isBlankRow) {
                    
                        continue;
                    
                    }
                    
                }
                
                if ( !(requiredMap.containsKey(new Integer(i)) 
                        || invalidCharMap.containsKey(new Integer(i)) 
                        || maxLengthMap.containsKey(new Integer(i)) 
                        || logicalErrorMap.containsKey(new Integer(i)) 
                        || hierarchyErrorMap.containsKey(new Integer(i))
                        || blankRowMap.containsKey(new Integer(i)))) {
                            
                            
                    //OrganizationCreation or Existency check process
                    loginUserOrgPosition = getLoginUserOrgPosition(
                                                    bodyRow, rowHeader, loginUserNodes);
                                                    
                    HSSFCell loginUserOrgCell = bodyRow.getCell(
                                                        (short)loginUserOrgPosition);
                                                        
                    String loginUserOrgName = getCellValue(loginUserOrgCell);
                    Node loginUserNode = getLoginUserOrgDetail(loginUserNodes, 
                                                loginUserOrgName);
                    
                    //orgNodeId and parentId initialization process
                    Integer parentOrgId = loginUserNode.getOrgNodeId();
                    orgNodeId = loginUserNode.getOrgNodeId();
                    int lastOrganization = 0; 
                    for (int ii = loginUserOrgPosition + 2; ii < orgHeaderLastPosition; ii = ii + 2 ) {
                        
                            HSSFCell OrgCellName = bodyRow.getCell((short)ii);
                            HSSFCell OrgCellId = bodyRow.getCell((short)ii + 1);
                            HSSFCell orgCellHeaderName = rowHeader.getCell((short)ii);
                            String orgCode = getCellValue(OrgCellId);
                            String orgName = getCellValue(OrgCellName);
                            HSSFCell OrgCellHeaderName = rowHeader.getCell((short)ii);
                            String headerName = getCellValue(OrgCellHeaderName);
                            Integer categoryId = getCategoryId (headerName, nodeCategory);
                            
                           if ( !hasOrganization(ii,bodyRow) && orgName.equals("")
                                    && orgCode.equals("")) {
                                
                                lastOrganization = ii;
                                break;
                           
                           } else if (hasOrganization(ii,bodyRow) && orgName.equals("") 
                                            && orgCode.equals("")) {
                                continue;
                            
                           } else {
                                
                                // Search Organization by OrgCode    
                                if ( isMatchUploadOrgIds ) {
                                    
                                    boolean isOrgExist = isOrganizationExist (orgCode, parentOrgId, categoryId, isMatchUploadOrgIds);
                                    if (!orgCode.trim().equals("") || !orgName.trim().equals("") ) {
                                        
                                         // Search Organization by OrgName
                                        if ( !isOrgExist ) {
                                            
                                            isOrgExist = isOrganizationExist (orgName, parentOrgId, categoryId, !isMatchUploadOrgIds);
                                            
                                            // No organization Exist
                                            if ( !isOrgExist ) {
                                                
                                                organization = new Node();
                                               
                                                organization.setCustomerId(customer.getCustomerId());
                                                organization.setOrgNodeName(orgName);
                                                organization.setOrgNodeCode(orgCode); 
                                                organization.setOrgNodeCategoryId(categoryId);
                                                organization.setParentOrgNodeId(parentOrgId);
                                                //create Organization
                                                organization = this.organizationManagement.
                                                        createOrganization(this.username, organization);
                                                
                                                //parentId and orgNodeId updated
                                                parentOrgId = organization.getOrgNodeId();
                                                orgNodeId = organization.getOrgNodeId();
                                                ArrayList tempList = new ArrayList( Arrays.asList(this.detailNodeM));
                                                tempList.add(organization);
                                                this.detailNodeM = (Node[])tempList.toArray(new Node[0]);
                                                
                                                
                                            } else {
                                                
                                                // retrive existing organization by passing orgName
                                                organization = getOrgNodeDetail(orgName, parentOrgId, categoryId, false);
                                                
                                                
                                                //Is Organization Exist
                                                    if (organization != null) {
                                                            
                                                            parentOrgId = organization.getOrgNodeId();
                                                            orgNodeId = organization.getOrgNodeId();
                                                            continue;    
                                                    
                                                    }  else {
                                                    
                                                        //new Organization creation
                                                        organization = new Node();
                                                                                                          
                                                        organization.setCustomerId(customer.getCustomerId());
                                                        organization.setOrgNodeName(orgName);
                                                        organization.setOrgNodeCode(orgCode); 
                                                        organization.setOrgNodeCategoryId(categoryId);
                                                        organization.setParentOrgNodeId(parentOrgId);
                                                        //create Organization
                                                        organization = this.organizationManagement.
                                                                createOrganization(this.username, organization);
                                                        
                                                        //parentId and orgNodeId updated
                                                        parentOrgId = organization.getOrgNodeId();
                                                        orgNodeId = organization.getOrgNodeId();
                                                        ArrayList tempList = new ArrayList( Arrays.asList(this.detailNodeM));
                                                        tempList.add(organization);
                                                        this.detailNodeM = (Node[])tempList.toArray(new Node[0]);
                                                 
                                                    }
                                                    
                                                }
                                            
                                            } else {
                                                
                                                // retrive existing organization by passing orgCode
                                                    organization = getOrgNodeDetail(orgCode, parentOrgId, categoryId, isMatchUploadOrgIds);
                                                    
                                                    //Is Organization Exist
                                                    if (organization != null) {
                                                            
                                                            parentOrgId = organization.getOrgNodeId();
                                                            orgNodeId = organization.getOrgNodeId();
                                                            continue;    
                                                    
                                                    } else {
                                                    
                                                        //new Organization creation
                                                                                                                
                                                        organization.setCustomerId(customer.getCustomerId());
                                                        organization.setOrgNodeName(orgName);
                                                        organization.setOrgNodeCode(orgCode); 
                                                        organization.setOrgNodeCategoryId(categoryId);
                                                        organization.setParentOrgNodeId(parentOrgId);
                                                        //create Organization
                                                        organization = this.organizationManagement.
                                                                createOrganization(this.username, organization);
                                                        
                                                         //parentId and orgNodeId updated
                                                        parentOrgId = organization.getOrgNodeId();
                                                        orgNodeId = organization.getOrgNodeId();
                                                        ArrayList tempList = new ArrayList( Arrays.asList(this.detailNodeM));
                                                        tempList.add(organization);
                                                        this.detailNodeM = (Node[])tempList.toArray(new Node[0]);
                                                    
                                                    }
                                                
                                            }
                                        
                                        
                                    }// End if ""
                                   
                                    
                                    
                                    
                                } else { // if no MatchUploadOrgIds present in customer configuration 
                                    
                                                                       
                                        boolean isOrgExist = isOrganizationExist (orgName, parentOrgId, categoryId, false);
                                   
                                       if (!orgName.trim().equals("")) {   
                                         // if no organization exist
                                            if (!isOrgExist) {
                                            
                                                //new Organization creation
                                                organization = new Node();
                                                
                                                organization.setCustomerId(customer.getCustomerId());
                                                organization.setOrgNodeName(orgName);
                                                organization.setOrgNodeCode(orgCode); 
                                                organization.setOrgNodeCategoryId(categoryId);
                                                organization.setParentOrgNodeId(parentOrgId);
                                                //create Organization
                                                organization = this.organizationManagement.
                                                        createOrganization(this.username, organization);
                                                
                                                 //parentId and orgNodeId updated
                                                parentOrgId = organization.getOrgNodeId();
                                                orgNodeId = organization.getOrgNodeId();
                                                ArrayList tempList = new ArrayList( Arrays.asList(this.detailNodeM));
                                                tempList.add(organization);
                                                this.detailNodeM = (Node[])tempList.toArray(new Node[0]);
                                            
                                            } else {
                                            
                                                // retrive existing organization by passing orgName
                                                organization = getOrgNodeDetail(orgName, parentOrgId, categoryId, false);
                                                
                                                //Is Organization Exist
                                                if (organization != null) {
                                                        
                                                        parentOrgId = organization.getOrgNodeId();
                                                        orgNodeId = organization.getOrgNodeId();
                                                        continue;    
                                                
                                                } else {
                                            
                                                    //new Organization creation
                                                    organization = new Node();
                                                    
                                                    organization.setCustomerId(customer.getCustomerId());
                                                    organization.setOrgNodeName(orgName);
                                                    organization.setOrgNodeCode(orgCode); 
                                                    organization.setOrgNodeCategoryId(categoryId);
                                                    organization.setParentOrgNodeId(parentOrgId);
                                                    //create Organization
                                                    organization = this.organizationManagement.
                                                            createOrganization(this.username, organization);
                                                    
                                                    //parentId and orgNodeId updated
                                                    parentOrgId = organization.getOrgNodeId();
                                                    orgNodeId = organization.getOrgNodeId();
                                                    ArrayList tempList = new ArrayList( Arrays.asList(this.detailNodeM));
                                                    tempList.add(organization);
                                                    this.detailNodeM = (Node[])tempList.toArray(new Node[0]);
                                             
                                                }
                                            
                                            }
                                        }// End of cheking orgName not ""
                                    
                                } // Else block end
                            
                           } // Else block (Organization creation process)
                        
                    }        
                    
                    Node []orgDetail = new Node[1];
                    orgDetail[0] = new Node();
                    orgDetail[0].setOrgNodeId(orgNodeId);
                    // User creation or User Existency check process
                    for ( int j = orgHeaderLastPosition ; j < rowHeader.
                            getPhysicalNumberOfCells() ; j++ ) {
                      
                        HSSFCell headerCell = rowHeader.getCell(j);
                        HSSFCell bodyCell  = bodyRow.getCell(j);

                        String strHeaderValue = getCellValue(headerCell);
                        String strBodyValue = getCellValue(bodyCell);
                        
                        
                        if ( strHeaderValue.equals(CTBConstants.REQUIREDFIELD_ROLE) ) {
                            
                            userDataMap.put(strHeaderValue, initCap(strBodyValue));
                            
                        } else {
                            
                            userDataMap.put(strHeaderValue, strBodyValue);
                        }                           
                        
                     
                    }    
                    
                    //actual user creation using usermanagement
                    createUpdateUser(userDataMap, orgDetail, 
                               this.username, this.userManagement , 
                               this.uploadDataFile, loginUser); 
                               
                    
                    uploadRecordCount++;                                                                

                } 
                isBlankRow = true;
            }
            
             
            if ( this.dataFileAudit.getFailedRecordCount() == null || 
                    this.dataFileAudit.getFailedRecordCount().intValue() == 0 )  {
            
                this.dataFileAudit.setStatus("SC");
                this.dataFileAudit.setFaildRec(null);
            
            
            } else {
            	
            	this.dataFileAudit.setStatus("FL");
            }
            
            this.dataFileAudit.setUploadFileRecordCount(new Integer(uploadRecordCount));
            uploadDataFile.upDateAuditTable(this.dataFileAudit);
            
            String loginUserMail = users.getUserDetails( this.username).getEmail();
            
            //send email        
            if ( loginUserMail != null) {   
                
                sendMail( this.username, CTBConstants.EMAIL_TYPE_WELCOME, loginUserMail);
                
            }
            
        } catch (SQLException se) {
             dataFileAudit.setFaildRec(null);
             dataFileAudit.setStatus("FL");
             dataFileAudit.setFailedRecordCount(new Integer(0));
             dataFileAudit.setUploadFileRecordCount(new Integer(0));
             try{
                uploadDataFile.upDateAuditTable(dataFileAudit);
             } catch (SQLException ex) {
                ex.printStackTrace();
             }
            FileNotUploadedException dataNotFoundException = 
                                        new FileNotUploadedException
                                                ("UploadDownloadManagement.Failed");
            dataNotFoundException.setStackTrace(se.getStackTrace());
            throw dataNotFoundException;
            
        } catch (Exception e) {
            dataFileAudit.setFaildRec(null);
             dataFileAudit.setStatus("FL");
             dataFileAudit.setFailedRecordCount(new Integer(0));
             dataFileAudit.setUploadFileRecordCount(new Integer(0));
             try{
                uploadDataFile.upDateAuditTable(dataFileAudit);
             } catch (SQLException ex) {
                ex.printStackTrace();
             }
            FileNotUploadedException dataNotFoundException = 
                                        new FileNotUploadedException
                                                ("UploadDownloadManagement.Failed");
            dataNotFoundException.setStackTrace(e.getStackTrace());
            throw dataNotFoundException;
            
        }
    
   }
    
   /*
   *
   */ 
   
   private Integer getCategoryId ( String categoryName, Node []categoryNode ) {
    
        Integer categoryId = null;
        
        for ( int i = 0; i < categoryNode.length; i++ ) {
            
            Node tempNode = categoryNode[i];
            
            if ( tempNode.getOrgNodeCategoryName().equalsIgnoreCase(categoryName) ) {
                
                    categoryId = tempNode.getOrgNodeCategoryId();     
            }
        }
        
        return categoryId;
   }
   
   /*
   * retrive login user org detail
   */ 
   private Node getLoginUserOrgDetail ( Node []loginUserNodes, String organizationName ) {
    
        Node orgDetail = null;
        for ( int i = 0; i < loginUserNodes.length; i++ ) {
            
            orgDetail = loginUserNodes[i];
            
            if ( orgDetail.getOrgNodeName().equalsIgnoreCase(organizationName) ) {
                
                break;
            }
            
        }
        
        return orgDetail;
    
   }
   
   /*
   *
   */ 
    private boolean isOrganizationExist ( String searchString, Integer parentId,
                                            Integer categoryId, boolean isMatchUploadOrgIds) {
    
        boolean hasOrganization = false;
        try {
            
            //Changed 04/12/2008 
            
            //Node [] detailNode = this.uploadDataFile.getUserDataTemplate(this.username);
            Node [] detailNode = this.detailNodeM;
            
            for ( int i = 0; i < detailNode.length; i++ ) {
                
                Node tempNode = detailNode[i];
                
                if ( isMatchUploadOrgIds ) {
                    
                    if (tempNode.getOrgNodeCode() != null) {
                        
                        if ( !searchString.trim().equals("")
                             && tempNode.getOrgNodeCode().equalsIgnoreCase(searchString) 
                                && tempNode.getParentOrgNodeId().intValue() 
                                == parentId.intValue()
                                && categoryId.intValue() == tempNode.getOrgNodeCategoryId().intValue()) {
                        
                            hasOrganization = true;
                            break;
                        }
                        
                    }
                    
                    
                } else {
                    
                    if ( !searchString.trim().equals("") 
                         && tempNode.getOrgNodeName().equalsIgnoreCase(searchString) 
                            && tempNode.getParentOrgNodeId().intValue() 
                            == parentId.intValue()
                            && categoryId.intValue() == tempNode.getOrgNodeCategoryId().intValue()) {
                        
                        hasOrganization = true;  
                        break; 
                    }
                }
                
            } 
            
        } /*catch (SQLException se) {
            
            se.printStackTrace();
            
        }*/ catch (Exception e) {
            
            e.printStackTrace();
            
        }
        
        return hasOrganization;
    
   }
   /*
   *
   */ 
   private Node getOrgNodeDetail ( String orgName, Integer parentId, Integer categoryId, 
        boolean isMatchUploadOrgIds ) {
    
        Node orgNode = null;
        try {
            
             //Changed on 04/12/2008
                
             //Node [] detailNode = this.uploadDataFile.getUserDataTemplate(this.username);
             Node [] detailNode = this.detailNodeM;                                        
            
            for ( int i = 0; i < detailNode.length; i++ ) {
                
                Node tempNode = detailNode[i];
                if ( isMatchUploadOrgIds ) {
                    
                    if (tempNode.getOrgNodeCode() != null) {
                       
                       if ( tempNode.getOrgNodeCode().equalsIgnoreCase(orgName) 
                                && tempNode.getParentOrgNodeId().intValue() 
                                == parentId.intValue()
                                && tempNode.getOrgNodeCategoryId().intValue() == categoryId.intValue()) {
                        
                            orgNode = tempNode;
                            break;
                        
                        } 
                        
                    }
                    
                    
                    
                } else {
                    
                    if ( tempNode.getOrgNodeName().equalsIgnoreCase(orgName) 
                            && tempNode.getParentOrgNodeId().intValue() 
                            == parentId.intValue()
                            && tempNode.getOrgNodeCategoryId().intValue() == categoryId.intValue()) {
                    
                        orgNode = tempNode;
                        break;
                    
                    }
                    
                }
                
                
            }
            
        } /*catch (SQLException se) {
            
            se.printStackTrace();
            
        }*/ catch (Exception e) {
            
            e.printStackTrace();
        }
    
        return orgNode;
   }
   
   /*
     * createUser()
     * this method will call the user management 
     * createUser function to user data insertion
    */ 

    private void createUpdateUser(HashMap userDataMap, 
                            Node[] userNode,        
                            String userName, 
                            UserManagement userManagement,
                            UploadDataFile uploadDataFile, User loginUser) 
                            throws SQLException, CTBBusinessException{

        User user = new User();
        boolean isNewUser = true;
        boolean isLoginUser = false;
        Node orgNodeForUpdateUser = null;
        // set address details
        Address address = new Address();
        address.setAddressLine1((String)userDataMap.
                                        get(CTBConstants.ADDRESS_LINE_1));
                                        
        address.setAddressLine2((String)userDataMap.
                                        get(CTBConstants.ADDRESS_LINE_2));
                                        
        address.setCity((String)userDataMap.get(CTBConstants.CITY));
        
        if( userDataMap.get(CTBConstants.STATE_NAME) != null ) {
            
            address.setStatePr((String)stateMap.
                    get(initCap((String)userDataMap.get(
                    CTBConstants.STATE_NAME))));
                    
        }
                                        
        String zipCode = (String)userDataMap.get(CTBConstants.ZIP);

        if ( zipCode != null && !"".equals(zipCode)) {

            address.setZipCode(zipCode.substring(0,5).trim());

            if ( zipCode.length() > 6 ) {
                int extStartPos =  zipCode.indexOf("-") + 1;
                address.setZipCodeExt(zipCode.substring(extStartPos, zipCode.length()).trim());

            }

        }

        String primaryPhoneNumber = (String)userDataMap.get(CTBConstants.PRIMARY_PHONE);
        primaryPhoneNumber = getPhoneFax(primaryPhoneNumber);
        address.setPrimaryPhone(primaryPhoneNumber);
      
        String secondaryPhoneNumber = (String)userDataMap.get(CTBConstants.SECONDARY_PHONE);
        secondaryPhoneNumber = getPhoneFax(secondaryPhoneNumber);
        address.setSecondaryPhone(secondaryPhoneNumber);
                
        String faxNumber = (String)userDataMap.get(CTBConstants.FAX);
        faxNumber = getPhoneFax(faxNumber);
        address.setFaxNumber(faxNumber);

        user.setAddress(address);

        //user personal details
        user.setFirstName(initStringCap((String)userDataMap.get(
                CTBConstants.REQUIREDFIELD_FIRST_NAME)));
                
        user.setMiddleName(initStringCap((String)userDataMap.get(
                CTBConstants.MIDDLE_NAME)));
                
        user.setLastName(initStringCap((String)userDataMap.get(
                CTBConstants.REQUIREDFIELD_LAST_NAME)));
                
        user.setEmail((String)userDataMap.get(
                CTBConstants.EMAIL));
                
        user.setTimeZone((String)timeZoneMap.get(initCap((String)userDataMap.get(
                CTBConstants.REQUIREDFIELD_TIME_ZONE))));

        //set role Details

        Role role = new Role();
        role.setRoleId((Integer)roleMap.get((String)userDataMap.get(
                CTBConstants.REQUIREDFIELD_ROLE)));
        
        role.setRoleName((String)userDataMap.get(
                CTBConstants.REQUIREDFIELD_ROLE));
                                
        user.setRole(role);
        
        //set External User Id for CR
        
        
        user.setExtPin1((String)userDataMap.get(
                CTBConstants.EXT_PIN1));
        
        //need to check if the user is already exits or not?
       // UserFileRow[] userFileRows = uploadDataFile.getUserData(userName);
       //Changed on 15-Dec-2008 for Performance Tuning
       User[] userRows = (User[])new ArrayList(this.visibleUsers.values()).
                                    toArray(new User[0]);
                                    
        //UserFileRow []userFileRows = new UserFileRow[userDetailRows.length];
        
        //copyUserToUserFileRow(userFileRows, userDetailRows);
        
        for( int i = 0; i < userRows.length; i++ ) {
            
            if ( userRows[i].getFirstName().equalsIgnoreCase(user.getFirstName())
                    && userRows[i].getLastName().equalsIgnoreCase(user.getLastName())) {
                
                isNewUser = false;
                                
                if( user.getMiddleName() != null && !user.getMiddleName().equals("")) {
                    
                    if ( userRows[i].getMiddleName() !=null
                                && userRows[i].getMiddleName().
                                equalsIgnoreCase(user.getMiddleName())) {
                        
                        isNewUser = false;
                                            
                    } else {
                        
                         isNewUser = true;
                        
                    }
                    
                } else {
                    
                    if ( userRows[i].getMiddleName() == null 
                            || userRows[i].getMiddleName().trim().equals("")) {
                        
                        isNewUser = false;
                        
                    } else {
                        
                        isNewUser = true;
                        
                    }
                    
                }
                
                if (!isNewUser) {
                    
                    isLoginUser = checkLoginUser(loginUser, userRows[i].getFirstName(),
                                    userRows[i].getMiddleName(), userRows[i].getLastName());
                    user.setUserId(userRows[i].getUserId());
                    user.setUserName(userRows[i].getUserName());
                    
                    if ( userRows[i].getAddressId() != null ) {
                    
                        user.setAddressId(userRows[i].getAddressId());
                        address.setAddressId(userRows[i].getAddressId());
                        user.setAddress(address);
                        
                    }
     
                 break;  
                }
            }
            
        }
        
        // make the first, middle and last name init caps
        user.setFirstName(StringUtils.upperCaseFirstLetter(user.getFirstName()));
        user.setMiddleName(StringUtils.upperCaseFirstLetter(user.getMiddleName()));
        user.setLastName(StringUtils.upperCaseFirstLetter(user.getLastName()));
        
        
        if ( isNewUser ) {
            
            user.setOrganizationNodes(userNode);
            String newUserName = userManagement.createUserUpload(loginUser, user);
            user.setUserName(newUserName);
            visibleUsers.put(user.getUserName(), user);
            
        } else {
            
            User existingUserDetails = new User();
            existingUserDetails = (User) visibleUsers.get(user.getUserName());
            Node[] orgNodes = existingUserDetails.getOrganizationNodes();
            
            if (isLoginUser) {
                
                user.setOrganizationNodes(orgNodes);
                
            } else {
                
                int size = orgNodes.length;    
                Node []updateNodes = new Node[size + 1];
                if (!isOrganizationPresent(userNode[0].getOrgNodeId(), orgNodes)) {
                                
                    for (int i = 0; i < orgNodes.length; i++) {
                        
                        updateNodes[i] = orgNodes[i];
                    }
                    
                    updateNodes[size] = userNode[0];
                    user.setOrganizationNodes(updateNodes);
                    
                } else {
                
                    user.setOrganizationNodes(orgNodes);
                
                }
                
            }
            
            userManagement.updateUserUpload(loginUser, user);
            visibleUsers.put(user.getUserName(), user);
            
        
        }
        
             
    }
    
    private void copyUserToUserFileRow (UserFileRow []userFileRow, User []user) {
        
        for (int i = 0; i < user.length; i++) {
            
            userFileRow[i].setUserId(user[i].getUserId());
            userFileRow[i].setUserName(user[i].getUserName());
            userFileRow[i].setFirstName(user[i].getFirstName());
            userFileRow[i].setMiddleName(user[i].getMiddleName());
            userFileRow[i].setLastName(user[i].getLastName());
            userFileRow[i].setEmail(user[i].getEmail());
            userFileRow[i].setRoleName(user[i].getRole().getRoleName());
            userFileRow[i].setRoleId(user[i].getRole().getRoleId());
            userFileRow[i].setExtPin1(user[i].getExtPin1());
            userFileRow[i].setExtPin2(user[i].getExtPin2());
            userFileRow[i].setAddressId(user[i].getAddressId());
            userFileRow[i].setTimeZone(user[i].getTimeZone());
            
            
            
        }
        
        
    }
    
    private boolean checkLoginUser (User loginUser, String firstName, String middleName, 
            String lastName) {
                
        if (loginUser.getFirstName().equalsIgnoreCase(firstName) 
                && loginUser.getLastName().equalsIgnoreCase(lastName)) {
            
            if (loginUser.getMiddleName() != null && middleName != null) {
                
                    if (loginUser.getMiddleName().equalsIgnoreCase(middleName)) {
                        
                        return true;
                        
                    }
            }
            
            if (loginUser.getMiddleName() == null && middleName == null) {
                
                return true;
            }
            
        }
        
        return false;
        
    }
    /*
    * Is user alresdy associate with orgNodeId
    */ 
   private static boolean isOrganizationPresent (Integer orgNodeId, Node []orgNodes) {
    
                
        for (int i = 0; i < orgNodes.length ; i++) {
            
            Node tempNode = orgNodes[i];
            
            if (orgNodeId.intValue() == tempNode.getOrgNodeId().intValue()) {
                
                return true;
            }
            
        }
        
        return false;
    
   }
   
     /*
   * Error Excel Creation
   */ 
   private void errorExcelCreation (HashMap requiredMap,  
            HashMap maxLengthMap, HashMap invalidCharMap, 
            HashMap logicalErrorMap, HashMap hierarchyErrorMap) {
        
        //POI details Initialize
        POIFSFileSystem pfs = null;
        HSSFSheet sheet = null;  
        int errorCount = 0;
        byte[] errorData  = null;
        boolean isBlankRow = true;
        String strUploadCell = "";   
        String rowHeaderCellValue = "";      
        
        try {
           
           //Error Excel file create Object Initializa
            HSSFWorkbook ewb = new HSSFWorkbook ();
            HSSFSheet esheet = ewb.createSheet("ErrorSheet");
            HSSFRow erowHeader = esheet.createRow(0);
            HSSFCellStyle style = null;
            
            HSSFCellStyle requiredStyle = ewb.createCellStyle();
            HSSFCellStyle invalidStyle = ewb.createCellStyle();
            HSSFCellStyle maxlengthStyle = ewb.createCellStyle();
            HSSFCellStyle logicalErrorStyle = ewb.createCellStyle();
            
            //set required field color
            requiredStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		    requiredStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            requiredStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
            
            //set invalid field color
            invalidStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		    invalidStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            invalidStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
            
            //set maxlength field color
            maxlengthStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		    maxlengthStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            maxlengthStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
            
            //set logical field color
            logicalErrorStyle.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
		    logicalErrorStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            logicalErrorStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
           
           //Upload Excel read object initialize 
           
            pfs = new POIFSFileSystem( new FileInputStream(this.serverFilePath) );
            HSSFWorkbook wb = new HSSFWorkbook(pfs);
            sheet = wb.getSheetAt(0);
            int totalRows = 0;
            
            if ( sheet != null) {
                
                totalRows =  sheet.getPhysicalNumberOfRows();
                        
            }
            
            HSSFRow rowHeader = sheet.getRow(0);
            
            // Excel Header Creation
            for ( int i = 0; i < rowHeader.getPhysicalNumberOfCells(); i++ ) {
                
                HSSFCell cell = erowHeader.createCell((short)i);
                style = cell.getCellStyle();
                style.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
                esheet.setDefaultColumnStyle((short)i,style);                      
                
                cell.setCellValue(getCellValue(rowHeader.getCell((short)i)));
                
            }
            
            // Excel Body creation
            for (int i = 1; i < totalRows; i++) {
                
              HSSFRow uploadRow = sheet.getRow((short)i);
              if (uploadRow == null) {
                    
                    totalRows++;
                    continue;
                    
                } else {
                   
                    int totalCells = rowHeader.getPhysicalNumberOfCells();         
                    // retrive each cell value for user
                    for (int k = 0; k < totalCells; k++) {
                    
                        //   HSSFCell cellHeader = rowHeader.getCell((short)k);
                        HSSFCell cell = uploadRow.getCell((short)k);
                         
                        if ( cell != null && (!getCellValue(cell).trim().equals("") 
                            && !(cell.getCellType() == 3)) ) {
                             
                             isBlankRow = false;                  
                             
                        } 
                    }
                    
                    if (isBlankRow) {
                    
                    continue;
                    
                    }
                    
                }   
                
                if ((requiredMap.containsKey(new Integer(i)) 
                        || invalidCharMap.containsKey(new Integer(i)) 
                        || maxLengthMap.containsKey(new Integer(i)) 
                        || logicalErrorMap.containsKey(new Integer(i))
                        || hierarchyErrorMap.containsKey(new Integer(i)))) {
                    
                    errorCount++;
                    
                   
                    HSSFRow errorRow = esheet.createRow((short)errorCount);
                    
                    for ( int j = 0; j < rowHeader.getPhysicalNumberOfCells(); j++ ) {
                        
                        HSSFCell errorCell = errorRow.createCell((short)j);
                        HSSFCell uploadCell = uploadRow.getCell((short)j);
                        
                        strUploadCell = getCellValue(uploadCell);
                        rowHeaderCellValue =  getCellValue
                                                 (rowHeader.getCell((short)j));
                        
                        //checking for required field
                        if ( requiredMap.size() > 0 ) {
                            
                            if ( requiredMap.containsKey(new Integer(i)) ) {
                                
                                ArrayList requiredList = (ArrayList)requiredMap.
                                                                get(new Integer(i));
                               
                                if ( requiredList.contains(rowHeaderCellValue) ) {
                                    
                                    if ( !strUploadCell.equals("") ) {
                                        
                                        errorCell.setCellValue(strUploadCell);
                                        errorCell.setCellStyle(requiredStyle);
                                        
                                    } else {
                                        
                                        errorCell.setCellStyle(requiredStyle); 
                                        
                                    }
                               
                                }    
                                
                            }
                            
                        }
                        
                        //checking for invalid field
                        if ( invalidCharMap.size() > 0 ) {
                            
                            if ( invalidCharMap.containsKey(new Integer(i)) ) {
                            
                                ArrayList invalidCharList 
                                        = (ArrayList)invalidCharMap.get(new Integer(i));
                               
                                if ( invalidCharList.contains(rowHeaderCellValue) ) {
                                    
                                    if ( !strUploadCell.equals("") ) {
                                     
                                        errorCell.setCellValue(strUploadCell);
                                        errorCell.setCellStyle(invalidStyle);         
                                        
                                    } else {
                                        
                                        errorCell.setCellStyle(invalidStyle); 
                                        
                                    }
                            
                                }
                          
                            }
                      
                        }
                        
                        //checking for maxlength field
                        if ( maxLengthMap.size() > 0 ) {
                            
                            if ( maxLengthMap.containsKey(new Integer(i)) ) {
                            
                                ArrayList maxlengthList 
                                        = (ArrayList)maxLengthMap.get(new Integer(i));
                               
                                if ( maxlengthList.contains(rowHeaderCellValue) ) {
                                    
                                    if ( !strUploadCell.equals("") ) {
                                        
                                        errorCell.setCellValue(strUploadCell);
                                        errorCell.setCellStyle(maxlengthStyle);      
                                        
                                    } else {
                                        
                                        errorCell.setCellStyle(maxlengthStyle); 
                                        
                                    }
                                 
                                }    
                             
                            }
                          
                        }
                        
                        //checking for logical error field 
                        if ( logicalErrorMap.size() > 0 ) {
                            
                            if ( logicalErrorMap.containsKey(new Integer(i)) ) {
                             
                                ArrayList logicalErrorList 
                                        = (ArrayList)logicalErrorMap.get(new Integer(i));
                               
                                if ( logicalErrorList.contains(rowHeaderCellValue) ) {
                                    
                                    if ( !strUploadCell.equals("") ) {
                                        
                                        errorCell.setCellValue(strUploadCell);
                                        errorCell.setCellStyle(logicalErrorStyle);     
                                        
                                    } else {
                                        
                                        errorCell.setCellStyle(logicalErrorStyle);
                                        
                                    }
                                  
                                }
                                
                            }
                          
                        }
                        
                        //checking for logical error field 
                        if ( hierarchyErrorMap.size() > 0 ) {
                            
                            if ( hierarchyErrorMap.containsKey(new Integer(i)) ) {
                            
                                ArrayList commonPathErrorList = 
                                        (ArrayList)hierarchyErrorMap.get(new Integer(i));
                               
                                if ( commonPathErrorList.contains(rowHeaderCellValue) ) {
                                    
                                    if ( !strUploadCell.equals("") ) {
                                        
                                        errorCell.setCellValue(strUploadCell);
                                        errorCell.setCellStyle(logicalErrorStyle);    
                                        
                                    } else {
                                        
                                        errorCell.setCellStyle(logicalErrorStyle);
                                        
                                    }
                                   
                                }    
                            
                            }
                         
                        }
                        
                        if ( !strUploadCell.equals("") ) {
                            
                            errorCell.setCellValue(strUploadCell);
                            
                        }
                      
                    }
                    
                                        
                }// If block(Error value weite) End
                
                isBlankRow = true;
            
            } // End for loop Excel body processing
            
            String uploadFileName = this.dataFileAudit.getUploadFileName();
            uploadFileName = uploadFileName.substring(0, uploadFileName.length()-4 );
            String errorFileName = CTBConstants.SERVER_FOLDER_NAME + "/" + uploadFileName + "_" + "Error" + ".xls";
            
            FileOutputStream mfileOut = new FileOutputStream(errorFileName);
            ewb.write(mfileOut);
            
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();             
        
            InputStream in = new FileInputStream(errorFileName);
    
            boolean moreData = true;
            
            while( moreData ) {
    
                byte [] buffer = new byte[1024];
                int read = in.read(buffer);
                moreData = read > 0;
                
                if ( moreData ) {
                    
                   baos.write(buffer, 0, read);
                   
                }
             }
           
           errorData  = baos.toByteArray(); 
           
           dataFileAudit.setFaildRec(errorData);
           //dataFileAudit.setStatus("FL");
           dataFileAudit.setFailedRecordCount(new Integer(errorCount));
           dataFileAudit.setUploadFileRecordCount(new Integer(0));
           uploadDataFile.upDateAuditTable(dataFileAudit);
             
           mfileOut.close();
           baos.flush();
           baos.close();
           
        } catch (Exception e) {
             dataFileAudit.setFaildRec(errorData);
             dataFileAudit.setStatus("FL");
             dataFileAudit.setFailedRecordCount(new Integer(errorCount));
             dataFileAudit.setUploadFileRecordCount(new Integer(0));
             try{
                uploadDataFile.upDateAuditTable(dataFileAudit);
             } catch (SQLException se) {
                se.printStackTrace();
             }
           
            e.printStackTrace();
            
        }     
    
   } 

  
   /*
   * get RoleName from Excel
   */ 
   private String getRoleName ( HSSFRow row, HSSFRow rowHeader ) {
    
        int totalCells = rowHeader.getPhysicalNumberOfCells();
        String roleName = "";
         // retrive header category Array
        Node []node = this.userFileRowHeader[0].getOrganizationNodes();
        int userHeaderStartPosition = node.length * 2;
        
        for ( int i = userHeaderStartPosition; i < totalCells; i++ ) {
            
            HSSFCell cellHeader = rowHeader.getCell((short)i);
            
            if ( getCellValue(cellHeader).equalsIgnoreCase(CTBConstants.
                    REQUIREDFIELD_ROLE) ) {
                        
               HSSFCell cellVal =  row.getCell((short)i);
               roleName = getCellValue(cellVal); 
               
            }
            
        }
        
        return roleName;
    
   }
   
   /*
    * get first name,middle name and last name from Excel
   */
   private boolean isLoginUserDetails ( HSSFRow row, HSSFRow rowHeader,
                                        User loginUserDetails , 
                                        Users users) throws SQLException{

        int totalCells = rowHeader.getPhysicalNumberOfCells();
        String firstName = "";
        String middleName = "";
        String lastName= ""; 
        Node []node = this.userFileRowHeader[0].getOrganizationNodes();
        int userHeaderStartPosition = node.length * 2;
        boolean isloginUser = false;
        String strCellHeader = "";
        String strCellValue = "";
        
        for ( int i = userHeaderStartPosition; i < totalCells; i++ ) {
            
            HSSFCell cellHeader = rowHeader.getCell((short)i);
            HSSFCell cellVal =  row.getCell((short)i);
            strCellHeader = getCellValue(cellHeader);
            strCellValue = getCellValue(cellVal);
            
            if ( strCellHeader.equalsIgnoreCase(CTBConstants.
                    REQUIREDFIELD_FIRST_NAME) ) {
                        
             
               firstName = strCellValue; 
               
            }
           
            if ( strCellHeader.equalsIgnoreCase(CTBConstants.
                    MIDDLE_NAME) ) {
                        
               
               middleName = strCellValue; 
               
            }
            
            if ( strCellHeader.equalsIgnoreCase(CTBConstants.
                    REQUIREDFIELD_LAST_NAME) ) {
                        
               
               lastName = strCellValue; 
               
            }
        }
            
          // User loginUserDetails = users.getUserDetails(loinUserName);
           if ( firstName != null && 
               loginUserDetails.getFirstName().equalsIgnoreCase(firstName)
               && lastName != null && loginUserDetails.getLastName().equalsIgnoreCase(lastName)) {
           
                    if ( loginUserDetails.getMiddleName() != null ) {
                        
                        if ( middleName != null && !middleName.equals("")
                                    && loginUserDetails.getMiddleName().
                                    equalsIgnoreCase(middleName)) {
                        
                             isloginUser = true;   
                        
                        } else {
                        
                             isloginUser = false;
                        
                        } 
                        
                    } else {
                        
                        if ( middleName != null && !middleName.equals("")) {
                            
                            isloginUser = false;
                        
                        } else {
                            
                            isloginUser = true;
                        
                        }
                    
                    }
            }  //check for my profile edit
            
         
      return isloginUser;
         
    
   }
   
   
   /*
   * has any organization in excel from login user organization
   */ 
   private boolean hasOrganization (int currentPosition, HSSFRow row) {
    
        Node []node = this.userFileRowHeader[0].getOrganizationNodes();
        int OrgHeaderLastPosition = node.length * 2;
        String leafOrgName = "";
        for ( int j = currentPosition + 2 ; j < OrgHeaderLastPosition; j = j + 2 ) {
                        
            HSSFCell cellName = row.getCell(j);
            HSSFCell cellId = row.getCell(j + 1);
            
              if( !getCellValue(cellName).equals("") || !getCellValue(cellId).equals("") ){  
                
                return true;
            
            }
                
        } //end for 
       
        return false;    
   }
   
   /*
   * retrive login user organization position in Excel
   */ 
   
   private int getLoginUserOrgPosition ( HSSFRow row, HSSFRow rowHeader, Node []loginUserNode ) {
   
        int loginUserPosition = 0;
        try {
            
            Node []node = this.userFileRowHeader[0].getOrganizationNodes();
            int OrgHeaderLastPosition = node.length ;
            String leafOrgName = "";
            //Node []loginUserNode =  orgNode.getTopNodesForUser(this.username);
            for ( int i = 0, j = 0; i < OrgHeaderLastPosition; i++, j = j + 2 ) {
                    
                    HSSFCell cell = row.getCell(j);
                    
                    if ( !getCellValue(cell).equals("") ) {
                        
                        leafOrgName = cell.getStringCellValue();
                                                
                        if ( isLoginUserOrganization(leafOrgName,loginUserNode,false) ) {
                            
                            loginUserPosition = j;
                            break;
        
                        } 
                        
                    }
                   
                } //end for 
       
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
     return  loginUserPosition;   
   }
   
   /*
   * check wheather cutomer's organization to 
   * loginUserOrganization path is valid or not
   */
   
   private boolean isCommonPathValid ( int rowPosition,HSSFRow row, 
                                       HSSFRow rowHeader, 
                                       HashMap hierarchyErrorMap, 
                                       boolean isMatchUploadOrgIds,
                                       Node  []loginUserNode) {
    
        try {
            
            Node []node = this.userFileRowHeader[0].getOrganizationNodes();
            HashMap commonPathMapDB = this.commonHierarchyMap;
            int OrgHeaderLastPosition = node.length ;
          //  Node []loginUserNode =  orgNode.getTopNodesForUser(this.userName);
            String leafOrgName = "";
            String loginOrgName = "";
            String leafOrgCode = "";
            String loginOrgCode = "";
            ArrayList excelCommonPathList = new ArrayList();
            String strCellId = "";
            String strCellName = "";
            
            int loginUserPosition = -1;
            
            for ( int i = 0, j = 0; i < OrgHeaderLastPosition; i++, j = j + 2 ) {
                
                HSSFCell cellName = row.getCell(j);
                HSSFCell cellId = row.getCell(j + 1);
                
                strCellName = getCellValue(cellName);
                strCellId = getCellValue(cellId);
                
                if (isMatchUploadOrgIds) {
                      
                    if (!strCellId.equals("")) {
                              
                         leafOrgCode = strCellId;
                         loginOrgCode = strCellId;
                        
                         if ( !strCellName.equals("") ) {
                            
                            leafOrgName = strCellName;
                            loginOrgName = strCellName;
                            leafOrgCode = leafOrgCode+"|"+leafOrgName;
                            
                         }
                          
                         excelCommonPathList.add(leafOrgCode);
                         
                         if ( isLoginUserOrganization(loginOrgCode+"|"+loginOrgName, loginUserNode, true) ) {
                            
                            loginUserPosition = j;
                            break;
        
                        } 
                           
                     }// End CellId checking
                    // if cellId is blank then check cellName
                  
                    else {
                    
                        if ( !strCellName.equals("") ) {
                            
                            leafOrgName = strCellName;
                            loginOrgName = strCellName;
                             
                            if (!strCellId.equals("")) {
                              
                              leafOrgCode = strCellId;
                              loginOrgCode = strCellId;
                              leafOrgName = leafOrgCode+"|"+leafOrgName;  
                                
                            }
                           
                            excelCommonPathList.add(leafOrgName);
                            
                            if ( isLoginUserOrganization(loginOrgCode+"|"+loginOrgName,loginUserNode, true) ) {
                                
                                loginUserPosition = j;
                                break;
            
                            } 
                            
                        }
              
                    }
                    
                //End isMatchUploadOrgIds checking
                } else {
                    
                    if ( !strCellName.equals("") ) {
                        
                        leafOrgName = strCellName;
                        loginOrgName = strCellName;
                        
                        if (!strCellId.equals("")) {
                          
                          leafOrgCode = strCellId;
                          
                          leafOrgName = leafOrgName+"|"+leafOrgCode;  
                            
                        }
                         
                        excelCommonPathList.add(leafOrgName);
                        
                        if ( isLoginUserOrganization(loginOrgName,loginUserNode, false) ) {
                            
                            loginUserPosition = j;
                            break;
        
                        } 
                        
                    }
                    
                   
                } //End Else(isMatchUploadOrgIds == false)
                
                
            } //end for
            
            if (loginUserPosition == -1) {
                
               String orgName = loginUserNode[0].getOrgNodeCategoryName();
               String orgHeader = orgName +" Name";
               ArrayList errorHierarchyList = new ArrayList();
               String strCellNameHeader = "";
               
               for ( int i = 0, j = 0; i < OrgHeaderLastPosition; i++, j = j + 2 ) {
                
                    HSSFCell cellNameHeader = rowHeader.getCell(j);
                    HSSFCell cellIdHeader = rowHeader.getCell(j + 1);
                    strCellNameHeader = getCellValue(cellNameHeader);
                    
                    errorHierarchyList.add(strCellNameHeader);
                    errorHierarchyList.add(getCellValue(cellIdHeader));
                    
                    if (strCellNameHeader.equals(orgHeader)) {
                        
                        break;   
                    }
                
                }
            
                hierarchyErrorMap.put(new Integer(rowPosition), errorHierarchyList);
                
            } else {
                
                if ( excelCommonPathList.size() > 0 ) {
                
                    Set key = commonPathMapDB.keySet();
                    ArrayList commonPathListDB = null;
                    Iterator it = key.iterator();
                    Integer orgNodeId = null;
            
                    while ( it.hasNext() ) {
                        
                        orgNodeId = (Integer)it.next();
                        commonPathListDB = (ArrayList)commonPathMapDB.get(orgNodeId);
                        
                        //if ( commonPathListDB.size() == excelCommonPathList.size() ) {
                            
                            if (isCommonPathSame (rowPosition, loginUserPosition, 
                                    rowHeader, excelCommonPathList, 
                                    commonPathListDB, hierarchyErrorMap, isMatchUploadOrgIds)) {
                                
                               // hierarchyErrorMap.clear();
                                break;
                                
                                
                            }
                            
                        //}
                        
                    }
                
                } else {
                    
                    return false;
                    
                }// End if-else
                
            }
            
            
            
            if (hierarchyErrorMap.size() > 0) {
                
                return false;
            
            } else {
                
                return true;   
            } 
                   
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        return true;
    }
    
    /*
    * Is Excel common Path match to actual common path which
    *  is comes from database
    */ 
    private boolean isCommonPathSame (int rowPosition, int loginUserPosition,
            HSSFRow rowHeader, ArrayList excelCommonPathList, 
            ArrayList commonPathListDB, HashMap hierarchyErrorMap, boolean isMatchUploadOrgIds) {
        
        int currentPosition = -1;
        for ( int i = 0, j = commonPathListDB.size() - 1;
                     i < excelCommonPathList.size(); i++, j-- ) {
            
            Node node = (Node)commonPathListDB.get(j);
            String orgNameDB = node.getOrgNodeName();
            String orgCodeDB = node.getOrgNodeCode();
            String orgNameExcel = (String)excelCommonPathList.get(i);
            
            String []orgNameCode = orgNameExcel.split("\\|");
            
            if (isMatchUploadOrgIds) {
                
                if (orgNameCode.length > 1 ) {
                    
                    if (!orgNameCode[0].equalsIgnoreCase("")) {
                        
                            if (!orgNameCode[0].equalsIgnoreCase(orgCodeDB)) {
                              
                                if (!orgNameCode[1].equalsIgnoreCase(orgNameDB)) {
                                    
                                    currentPosition = i;
                                    break; 
                                    
                                }
                                
                            
                            } 
                        }
                    
                    } else {
                        
                        if (!orgNameCode[0].equalsIgnoreCase(orgNameDB)) {
                            
                            currentPosition = i;
                            break;
                            
                        }
                        
                    }
                
            } else {
                
                if (orgNameCode[0] != null) {
                
                    if ( !orgNameDB.equalsIgnoreCase(orgNameCode[0]) ) {
                    
                        currentPosition = i;
                        break;
                    
                    } 
                    
                }
            
                if (orgNameCode.length > 1) {
                    
                    if (orgNameCode[1] !=null) {
                        
                        if (!orgNameCode[1].equalsIgnoreCase(orgCodeDB)) {
                        
                            currentPosition = i;
                            break;
                        
                        }
                        
                    }
                
                }
                
            } // End Else(isMatchUploadOrgIds == false)
            
            
            
        } // End For
        
        if ( currentPosition == -1 ) {
            
            if (hierarchyErrorMap.containsKey(new Integer(rowPosition))) {

                hierarchyErrorMap.remove(new Integer(rowPosition));

            }
            return true;
            
            
            
        } else {
            
            Node []node = this.userFileRowHeader[0].getOrganizationNodes();
            ArrayList errorHierarchyList = new ArrayList();
            currentPosition = currentPosition * 2;
            for ( int j = currentPosition ; j < loginUserPosition + 2; j = j + 2 ) {
                
                HSSFCell cellNameHeader = rowHeader.getCell(j);
                HSSFCell cellIdHeader = rowHeader.getCell(j + 1);
                
                errorHierarchyList.add(getCellValue(cellNameHeader));
                errorHierarchyList.add(getCellValue(cellIdHeader));
                
            }
            
            hierarchyErrorMap.put(new Integer(rowPosition), errorHierarchyList);
            return false;
        }
        
        
    }
   
   /*
   * retrive user detail for each row
   */ 
    private void getEachRowUserDetail( int rowPosition,HSSFRow row, HSSFRow rowHeader, 
            HashMap requiredMap, HashMap maxLengthMap, 
            HashMap invalidCharMap, HashMap logicalErrorMap,
            User user, boolean isMatchUploadOrgIds) throws Exception {
        
        //Initialize reruired,invalid,maxlength and logical error arraylist
        ArrayList requiredList = new ArrayList();
        ArrayList invalidList = new ArrayList();
        ArrayList maxLengthList = new ArrayList();
        ArrayList logicalErrorList = new ArrayList ();
        
        // retrive header category Array
        Node []node = this.userFileRowHeader[0].getOrganizationNodes();
        int userHeaderStartPosition = node.length * 2;
        
        // checking for required field,invalid charecter,maxlength,logical error
        if ( isRequired (userHeaderStartPosition, row, 
                    rowHeader, requiredList) ) {
            
            requiredMap.put(new Integer(rowPosition), requiredList);
        
        } else if ( isInvalidChar(userHeaderStartPosition, 
                    row, rowHeader, invalidList) ) {
            
            invalidCharMap.put(new Integer(rowPosition), invalidList);
            
        } else if ( isMaxlengthExceed (userHeaderStartPosition, row, 
                    rowHeader, maxLengthList) ) {
            
            maxLengthMap.put(new Integer (rowPosition), maxLengthList);
        
        } else if ( isLogicalError(userHeaderStartPosition, 
                    row, rowHeader, logicalErrorList, user, isMatchUploadOrgIds) ) {
            
            logicalErrorMap.put(new Integer (rowPosition), logicalErrorList);
            
        }
        
    }
    
     /**
     * This is a generic method to send mail. It retrieves the content of the body
     * from database. value should be an empty string even If for some email_type, 
     * there is no replacement in the body. Caller should ensure that to_address 
     * is not null. This method suppresses any exception occured. 
     * 
     */
    private void sendMail(String userName, Integer emailType, String to) {
        try {
            CustomerEmail emailData = new CustomerEmail();
            if(userName != null){ 
              emailData = users.getCustomerEmailByUserName(userName, emailType);
            }
            String content = CTBConstants.USER_MAIL_BODY;
                                           
            InitialContext ic = new InitialContext();
            
            //the properties were configured in WebLogic through the console
            Session session = (Session) ic.lookup("UserManagementMail");
            
            //contruct the actual message
            Message msg =  new MimeMessage(session);
            msg.setFrom(new InternetAddress(CTBConstants.EMAIL_FROM));
            
            //emailTo could be a comma separated list of addresses
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(CTBConstants.EMAIL_SUBJECT);
            msg.setText(content);
            msg.setSentDate(new Date());
            
            //send the message
            Transport.send(msg);
            
        } catch (Exception e) {
            e.printStackTrace();
            OASLogger.getLogger("UploadManagement").error(e.getMessage());
            OASLogger.getLogger("UploadManagement").error("sendMail failed for emailType: " + emailType);
        }
    }
    
    /*
    * Is role name, Time_Zone, State Name are same
    */ 
    private boolean isLogicalError (int userHeaderStartPosition, HSSFRow row, 
            HSSFRow rowHeader, ArrayList logicalErrorList, User user, boolean isMatchUploadOrgIds) {
       
        int totalCells = rowHeader.getPhysicalNumberOfCells();
        String strCell = "";        
        
        //check for peer
            
            if (isLoginUser(userHeaderStartPosition, row, rowHeader, user)) {
                
                checkLoginUserOrganization (row, rowHeader, isMatchUploadOrgIds, 
                    user, logicalErrorList); 
                    
            } else {
                
                checkPeerUser (userHeaderStartPosition, row, 
                    rowHeader, user, logicalErrorList);
                
            } 
        // retrive each cell value for user
        
        if (logicalErrorList.size() == 0) {
            
            for ( int i = userHeaderStartPosition; i < totalCells; i++ ) {
            
                HSSFCell cellHeader = rowHeader.getCell((short)i);
                HSSFCell cell = row.getCell((short)i);
                
                strCell = getCellValue(cell);
                
                if ( !strCell.equals("")){
                
                    if ( cellHeader.getStringCellValue().
                            equals(CTBConstants.REQUIREDFIELD_TIME_ZONE) 
                            && !isTimeZoneSame(strCell) ) {
                        
                        logicalErrorList.add(CTBConstants.REQUIREDFIELD_TIME_ZONE);    
                    }
                    
                    else if ( cellHeader.getStringCellValue().
                            equals(CTBConstants.REQUIREDFIELD_ROLE) 
                            && !isRoleNameSame(strCell) ) {
                                
                        logicalErrorList.add(CTBConstants.REQUIREDFIELD_ROLE);    
                        
                    } 
                    
                    //Check for Myprofile 
                    else if ( cellHeader.getStringCellValue().
                            equals(CTBConstants.REQUIREDFIELD_ROLE)
                            && isLoginUser(userHeaderStartPosition, row, rowHeader, user) 
                            && isLoginUserRoleChange(userHeaderStartPosition, row, rowHeader, user)) {
                       
                        logicalErrorList.add(CTBConstants.REQUIREDFIELD_ROLE);  
                    }
                    
                    else if ( cellHeader.getStringCellValue().
                            equals(CTBConstants.STATE_NAME) 
                            && !isStateSame(strCell) ) {
                                
                        logicalErrorList.add(CTBConstants.STATE_NAME);    
                    }
                }
            
            }
            
        }
        
        
        
        if ( logicalErrorList.size() == 0 ) {
            
            return false;
            
        } else {
            
            return true;   
        }
                
        
    }
    /*
    * Is Peer User 
    */ 
    private void checkPeerUser (int userHeaderStartPosition, HSSFRow row, 
            HSSFRow rowHeader, User user, ArrayList logicalErrorList) {
        
        User[] userRows = null;
        //UserFileRow []userFileRows = null;
        
        try {
            // Changed on 18-Dec-2008 for performance tuning
              userRows = (User[])new ArrayList(this.visibleUsers.values()).
                                    toArray(new User[0]);
            //userFileRows = uploadDataFile.getUserData(this.username);
            
        }  catch (Exception e) {
            
            e.printStackTrace();
            
        }
        
         int totalCells = rowHeader.getPhysicalNumberOfCells();
         Node []loginUserOrg = user.getOrganizationNodes();
         String fristName = null;
         String middleName = null;
         String lastName = null;
         Node []userOrganization = null;
         String searchUserName = null;
         Role userRole = null;
         boolean flag = false;
         String strCell = "";
         
         for ( int i = userHeaderStartPosition; i < totalCells; i++ ) {
            HSSFCell cellHeader = rowHeader.getCell((short)i);
            HSSFCell cell = row.getCell((short)i);
            strCell = getCellValue(cell);
                 
            if ( !strCell.equals("")){     
                 
                if ( cellHeader.getStringCellValue().
                    equals(CTBConstants.REQUIREDFIELD_FIRST_NAME)) {
                        
                        fristName = strCell;
                }
                
                if ( cellHeader.getStringCellValue().
                    equals(CTBConstants.REQUIREDFIELD_LAST_NAME)) {
                        
                        lastName = strCell;
                }
                
                 if ( cellHeader.getStringCellValue().
                            equals(CTBConstants.MIDDLE_NAME)) {
                        
                        middleName = strCell;
                        
                        if (cell.getCellType() == 3) {
                            
                            middleName = null;
                            
                            
                        } 
                        
                        if (cell.getCellType() == 1 && cell.getStringCellValue().trim().equals("")) {
                            
                            middleName = null;
                        }
                     
                }
            }
            
         }
       
         int totalUserCount = userRows.length;
         
         for (int i = 0; i < totalUserCount; i++) {
            
            String tempFristName = userRows[i].getFirstName();
            String tempLastName = userRows[i].getLastName();
            String tempMiddleName = userRows[i].getMiddleName();
            
            // Changed on 18-Dec-2008 for performance tuning
            
            if (tempFristName.equalsIgnoreCase(fristName) 
                && tempLastName.equalsIgnoreCase(lastName)) {
            
                if (tempMiddleName != null && middleName != null 
                        && !tempMiddleName.equals("")) {
                    
                    if (tempMiddleName.equalsIgnoreCase(middleName)) {
                        
                            flag = true;
                            searchUserName = userRows[i].getUserName();
                            break;
                    }
                    
                } else {
                    
                    if ((tempMiddleName == null || tempMiddleName.equals("")) 
                            && middleName == null) {
                        
                        
                            flag = true;
                            searchUserName = userRows[i].getUserName();
                            break;
                    }
                    
                }
            
            }
            
            
         }
         
         if (flag) {
            
            try {
                
                User searchUser = (User) visibleUsers.get(searchUserName);
                userOrganization = searchUser.getOrganizationNodes();
                userRole = searchUser.getRole();
                
            } catch (Exception e) {
            
                    e.printStackTrace();
            
            }
            
            if (isSameOrg (loginUserOrg, userOrganization) && isAdmin(userRole) ) {
                
                logicalErrorList.add(CTBConstants.REQUIREDFIELD_FIRST_NAME);
                logicalErrorList.add(CTBConstants.MIDDLE_NAME);
                logicalErrorList.add(CTBConstants.REQUIREDFIELD_LAST_NAME);
                logicalErrorList.add(CTBConstants.EMAIL);
                logicalErrorList.add(CTBConstants.REQUIREDFIELD_TIME_ZONE);
                logicalErrorList.add(CTBConstants.REQUIREDFIELD_ROLE);
                logicalErrorList.add(CTBConstants.EXT_PIN1);
                logicalErrorList.add(CTBConstants.ADDRESS_LINE_1);
                logicalErrorList.add(CTBConstants.ADDRESS_LINE_2);
                logicalErrorList.add(CTBConstants.CITY);
                logicalErrorList.add(CTBConstants.STATE_NAME);
                logicalErrorList.add(CTBConstants.ZIP);
                logicalErrorList.add(CTBConstants.PRIMARY_PHONE);
                logicalErrorList.add(CTBConstants.SECONDARY_PHONE);
                logicalErrorList.add(CTBConstants.FAX);
                
            }
            
            
         } 
         
        
    }
    
    /**
     * check for admin role of user
     */
    private boolean isAdmin(Role userRole) {
        
        String roleName = userRole.getRoleName();
        
        if(roleName.equalsIgnoreCase(CTBConstants.ROLE_NAME_ADMIN) ){
            
            return true;
        }
        
        return false;
    }
    
    
    /*
    * Is Same Organization
    */ 
    private boolean isSameOrg (Node []loginUserOrg, Node []userOrganization) {
        
        for (int i = 0 ; i < userOrganization.length; i++) {
            
            Node userNode = userOrganization[i];
            
            for (int j = 0; j < loginUserOrg.length; j++) {
                
                Node loginUserNode = loginUserOrg[j];
                
                if (userNode.getOrgNodeName().trim().equalsIgnoreCase(loginUserNode.
                        getOrgNodeName().trim())) {
                   
                    if (userNode.getOrgNodeCategoryId().
                            equals(loginUserNode.getOrgNodeCategoryId())) { 
                    
                        return true; 
                    }
                                        
                }
                
            }
            
        }
        
        return false;
        
    }
    
    /*
    * check for login userOrganization
    */ 
    private void checkLoginUserOrganization (HSSFRow row, HSSFRow rowHeader, boolean isMatchUploadOrgIds, 
            User user, ArrayList logicalErrorList) {
        
       Node []node = this.userFileRowHeader[0].getOrganizationNodes();
       Node [] loginUserNode = user.getOrganizationNodes();
       int lastOrgPosition = node.length * 2;
       String orgCode = "";
       String orgName = "";
       String orgHeaderCode = "";
       String orgHeaderName = "";
       
       for (int i = 0; i < lastOrgPosition; i = i + 2) {
        
            //Header
            HSSFCell orgCellHeaderName = rowHeader.getCell((short)i);
            HSSFCell orgCellHeaderId = rowHeader.getCell((short)i+1);
            orgHeaderName = getCellValue(orgCellHeaderName);
            orgHeaderCode = getCellValue(orgCellHeaderId);
            
            HSSFCell OrgCellName = row.getCell((short)i);
            HSSFCell OrgCellId = row.getCell((short)i + 1);
            orgCode = getCellValue(OrgCellId);
            orgName = getCellValue(OrgCellName);
            
            if (!hasOrganization(i, row)) {
                
                break;
                
            }
        
       }
       
       if (isMatchUploadOrgIds) {
        
            //check for orgCode
            
            if (!checkForOrgCode(loginUserNode, orgCode)) {
                
                if (!checkForOrgName(loginUserNode, orgName)) {
                    
                    logicalErrorList.add(orgHeaderName);
                    logicalErrorList.add(orgHeaderCode);
                }
                
            }
        
       } else {
        
            if (!checkForOrgName(loginUserNode, orgName)) {
                
                    logicalErrorList.add(orgHeaderName);
                    logicalErrorList.add(orgHeaderCode);
            }
       }
        
    }
    /*
    * Check for orgCode
    */ 
    private boolean checkForOrgCode (Node []loginUserNode, String orgCode) {
        
        for (int i = 0; i < loginUserNode.length; i++) {
            
            Node tempNode = loginUserNode[i];
            
            if (tempNode.getOrgNodeCode() != null) {
                
                
                if (tempNode.getOrgNodeCode().trim().
                        equalsIgnoreCase(orgCode.trim())) {
                
                    return true;
                
                }
                
            }
           
            
        }
        
        return false;
        
    }
    /*
    * Check for orgName
    */ 
    private boolean checkForOrgName (Node []loginUserNode, String orgName) {
        
        for (int i = 0; i < loginUserNode.length; i++) {
            
            Node tempNode = loginUserNode[i];
            
            if (tempNode.getOrgNodeName().trim().
                    equalsIgnoreCase(orgName.trim())) {
                
                return true;
                
            }
            
        }
        
        return false;
        
    }
    
    private boolean isLoginUserRoleChange (int userHeaderStartPosition, HSSFRow row, 
            HSSFRow rowHeader, User user) {
         int totalCells = rowHeader.getPhysicalNumberOfCells();
         String role = "";
         String strCell = "";
          for ( int i = userHeaderStartPosition; i < totalCells; i++ ) {
            
                HSSFCell cellHeader = rowHeader.getCell((short)i);
                HSSFCell cell = row.getCell((short)i);
                strCell = getCellValue(cell);
                
                if ( !strCell.equals("") && cellHeader.getStringCellValue().
                        equals(CTBConstants.REQUIREDFIELD_ROLE)) {
                    
                        role = initCap(strCell);
                }
            
          }
          
          if (user.getRole().getRoleName().equalsIgnoreCase(role)) {
            
                return false;
          }
          
          return true;
        
    }
    
    private boolean isLoginUser (int userHeaderStartPosition, HSSFRow row, 
            HSSFRow rowHeader, User user ) {
        
         int totalCells = rowHeader.getPhysicalNumberOfCells();
         
         String fristName = null;
         String middleName = null;
         String lastName = null;
         String strCell = "";
         
         for ( int i = userHeaderStartPosition; i < totalCells; i++ ) {
            HSSFCell cellHeader = rowHeader.getCell((short)i);
            HSSFCell cell = row.getCell((short)i);
            strCell = getCellValue(cell);
                 
            if ( !strCell.equals("") && cellHeader.getStringCellValue().
                equals(CTBConstants.REQUIREDFIELD_FIRST_NAME)) {
                    
                    fristName = strCell;
            }
            
            if ( !strCell.equals("") && cellHeader.getStringCellValue().
                equals(CTBConstants.REQUIREDFIELD_LAST_NAME)) {
                    
                    lastName = strCell;
            }
            
             if ( !strCell.equals("") && cellHeader.getStringCellValue().
                        equals(CTBConstants.MIDDLE_NAME)) {
                    
                    middleName = strCell;
                    
                    if (cell.getCellType() == 3) {
                        
                        middleName = null;
                        
                    } 
                    
                    if (cell.getCellType() == 1 && cell.getStringCellValue().trim().equals("")) {
                        
                        middleName = null;
                    }
                 
            }
            
            
         }
         
         if (user.getFirstName().equalsIgnoreCase(fristName) 
                && user.getLastName().equalsIgnoreCase(lastName)) {
            
                if (user.getMiddleName() != null && middleName != null) {
                    
                    if (user.getMiddleName().equalsIgnoreCase(middleName)) {
                        
                            return true;
                    }
                    
                } else {
                    
                    if (user.getMiddleName() == null && middleName == null) {
                        
                        
                            return true;
                    }
                    
                }
            
         }
         
         return false;
         
    } 
    
    /*
    * Is Upload Excel rolename and database role name are same
    */ 
    private boolean isRoleNameSame ( String value ) {
        
        if ( roleMap.containsKey(initCap(value)) ) {
            
            return true;
            
        } else {
            
            return false;
            
        }
        
    }
    
    /*
    * Is Upload Excel timezone and database timezone name are same
    */
    private boolean isTimeZoneSame ( String value ) {
        
        
        if ( timeZoneMap.containsKey(initCap(value)) ) {
            
            return true;
            
        } else {
            
            return false;
            
        }
        
    }  
    /*
    * Solution for Defect 55851
    */ 
    private String initCap ( String value ) {

        if ( value != null && !value.trim().equals("")) {

            String str[] = value.trim().split(" ");
            String initString = "";
            if (str.length > 1) {

            	for (int i = 0; i < str.length; i++) {

                    if(str[i].length()>0){
                        String firstValue = str[i].trim().toLowerCase();
                        char ch = firstValue.charAt(0);
                        String newStringChar = (new Character(ch)).toString().toUpperCase();
                        firstValue = firstValue.substring(1,firstValue.length());
                        if (i == 0) {
    
                            initString = initString + newStringChar+firstValue;
    
                        } else {
    
    
                            initString = initString +" "+ newStringChar+firstValue;
                        }
                
                    }

            	}
            	return initString;
           } else {

                String firstValue = str[0].toLowerCase();
                char ch = firstValue.charAt(0);
                String newStringChar = (new Character(ch)).toString().toUpperCase();
                firstValue = firstValue.substring(1,firstValue.length());
                firstValue = newStringChar+firstValue;
                return firstValue;

            } 

        } else {

            return null;

        }

    }

    
     /*
     * initStringCap
    */
    private static String initStringCap ( String value ) {

	    if ( value != null && !value.trim().equals("")) {

            char ch = value.charAt(0);

            if ((ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122)) {

            	String newStringChar = (new Character(ch)).toString().toUpperCase();
            	value = value.substring(1, value.length());
            	value = newStringChar + value;


            }

	    }

	    return value;
	}
    
    /*
    * Is Upload Excel state and database state name are same
    */ 
    private boolean isStateSame ( String value ) {
        
                
        if ( stateMap.containsKey(initCap(value)) ) {
            
            return true;
            
        } else {
            
            return false; 
            
        }
        
    }
    
   
    /*
    * Is admin belonging organization is login admin organization
    */ 
    
    private boolean isLoginUserOrganization ( String orgName, Node[] loginUserNode, boolean isMatchUploadOrgIds ) {
        
        String []orgDetail = null;
        
        if (isMatchUploadOrgIds) {
            
            orgDetail = orgName.split("\\|");
            
        }
        
        for ( int i = 0; i < loginUserNode.length; i++ ) {
            
            Node tempNode = loginUserNode[i];
            
            if (isMatchUploadOrgIds) {
                
                
                if (!orgDetail[0].equalsIgnoreCase("")) {
                    
                    //Check for orgCode
                    if (orgDetail[0].equalsIgnoreCase(tempNode.getOrgNodeCode())) {
                        
                        return true;
                    
                    } else {
                        
                        //Check for orgName
                        if (orgDetail[1].equalsIgnoreCase(tempNode.getOrgNodeName())) {
                        
                            return true;
                    
                         }
                        
                    }
                    
                } else {
                    
                //Check for orgName
                    if (orgDetail[1].equalsIgnoreCase(tempNode.getOrgNodeName())) {
                    
                        return true;
                
                    }
                
                }
                
                
            } else {
                
                    //Check for orgName
                    if ( tempNode.getOrgNodeName().equalsIgnoreCase(orgName) ) {
                
                        return true;
                    }
              }
         
        }
        
        return false;
        
    }
    
    
    
    /*
    * Check for required field for each row
    */ 
    private boolean isRequired ( int userHeaderStartPosition, HSSFRow row, 
            HSSFRow rowHeader, ArrayList requiredList ) {
        
        int totalCells = rowHeader.getPhysicalNumberOfCells(); 
        String strCellValue = "";        
        // retrive each cell value for user
        for (int i = userHeaderStartPosition; i < totalCells; i++) {
            
            HSSFCell cellHeader = rowHeader.getCell((short)i);
            HSSFCell cell = row.getCell((short)i);
            strCellValue = getCellValue(cell);
            
            //Required field checking
            if ( strCellValue.equals("") || cell.getCellType() == 3 || (cell.getCellType() == 1 
                    && cell.getStringCellValue().trim().equals(""))) {
                
                if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.REQUIREDFIELD_FIRST_NAME) ) {
                        
                        requiredList.add(CTBConstants.REQUIREDFIELD_FIRST_NAME);    
                } 
           
       /*     }
            
            if ( strCellValue.equals("") || cell.getCellType() == 3 || (cell.getCellType() == 1 
                    && cell.getStringCellValue().trim().equals(""))) {
       */          
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.REQUIREDFIELD_LAST_NAME) ) {
                        
                    requiredList.add(CTBConstants.REQUIREDFIELD_LAST_NAME);   
                } 
                        
                        
     /*       }
            
            if ( strCellValue.equals("")|| cell.getCellType() == 3 || (cell.getCellType() == 1 
                    && cell.getStringCellValue().trim().equals(""))) {
     */        
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.REQUIREDFIELD_ROLE) ) {
                        
                    requiredList.add(CTBConstants.REQUIREDFIELD_ROLE);           
                }            
                        
   /*         }
            
            if ( strCellValue.equals("") || cell.getCellType() == 3 || (cell.getCellType() == 1 
                    && cell.getStringCellValue().trim().equals(""))) {
       */       
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.REQUIREDFIELD_TIME_ZONE) ) {
                        
                    requiredList.add(CTBConstants.REQUIREDFIELD_TIME_ZONE);          
                
                }
                        
                        
            }
       
        }
        
        if ( requiredList.size() == 0 ) {
            
            return false;
            
        } else {
            
            return true;
            
        }                   
       
    }
    /*
    * Is cell contains invalid charecter
    */
    
    private boolean isInvalidChar ( int userHeaderStartPosition, HSSFRow row, 
            HSSFRow rowHeader, ArrayList invalidList ) {
            
        int totalCells = rowHeader.getPhysicalNumberOfCells(); 
        String strCell = "";      
        for (int i = userHeaderStartPosition; i < totalCells; i++) {
        
            HSSFCell cellHeader = rowHeader.getCell((short)i);
            HSSFCell cell = row.getCell((short)i);
            strCell = getCellValue(cell);
            
            if(!strCell.equals("")){
            
                if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.REQUIREDFIELD_FIRST_NAME) 
                        && !validNameString(strCell) ) {
                            
                    invalidList.add(CTBConstants.REQUIREDFIELD_FIRST_NAME);
                }
            
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.MIDDLE_NAME)
                        && !strCell.trim().equals("")
                        && !validNameString(strCell) ) {
                    
                    invalidList.add(CTBConstants.MIDDLE_NAME);
                    
                }
                
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.REQUIREDFIELD_LAST_NAME)
                        && !validNameString(strCell) ) {
                            
                    invalidList.add(CTBConstants.REQUIREDFIELD_LAST_NAME);
                    
                }
                
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.EMAIL)
                        && !strCell.trim().equals("")
                        && !validEmail(strCell) ) {
                    
                    invalidList.add(CTBConstants.EMAIL);
                    
                }
                
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.ADDRESS_LINE_1)
                        && !strCell.trim().equals("")
                        && !validAddressString(strCell) ) {
                            
                    invalidList.add(CTBConstants.ADDRESS_LINE_1);
                    
                }
                
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.ADDRESS_LINE_2)
                        && !strCell.trim().equals("")
                        && !validAddressString(strCell) ) {
                            
                    invalidList.add(CTBConstants.ADDRESS_LINE_2);
                    
                }
                
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.CITY)
                        && !strCell.trim().equals("")
                        && !validNameString(strCell) ) {
                            
                    invalidList.add(CTBConstants.CITY);
                    
                }
                
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.ZIP)
                        && !strCell.trim().equals("")
                        && !isValidZipFormat(strCell) ) {
                            
                    invalidList.add(CTBConstants.ZIP);
                    
                }
                
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.PRIMARY_PHONE)
                        && !strCell.trim().equals("")
                        && !isValidPhone(strCell) ) {
                            
                    invalidList.add(CTBConstants.PRIMARY_PHONE);
                    
                }
                
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.SECONDARY_PHONE)
                        && !strCell.trim().equals("")
                        && !isValidPhone(strCell) ) {
                            
                    invalidList.add(CTBConstants.SECONDARY_PHONE);
                    
                }
                
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.FAX)
                        && !strCell.trim().equals("")
                        && !isValidFax(strCell) ) {
                            
                    invalidList.add(CTBConstants.FAX);
                    
                }
            }
        
        }
        
        if ( invalidList.size() == 0 ) {
            
            return false;
        
        } else {
            
            return true;
            
        }

    }
    
    /*
    * return Excel cell value as a String
    */
    private String getCellValue(HSSFCell cell) {
        
        String cellValue = "";
        int cellType = 0;
        
        if ( cell != null ) {
            
            cellType = cell.getCellType();
            
            if ( cellType == 0 ) {
            
            cellValue = String.valueOf((new Double (cell.getNumericCellValue())).intValue());
            
            } else if ( cellType == 1 ) {
                
                cellValue = cell.getStringCellValue();
            }
        }
        
        
        return cellValue.trim();
        
    }
    /*
    *
    */
    
    private String getCellHeaderValue(HSSFCell cell) {
        
        String cellValue = "";
        
        if ( cell != null ) {
            
            if ( cell.getCellType() == 1 ) {
                
                cellValue = cell.getStringCellValue();
                
                cellValue = cellValue.split(" ")[0].trim();
            }
        }
        
        
        return cellValue.trim();
        
    }  
    
    /*
    * Is cell value exceed Maxlength
    */
    
    private boolean isMaxlengthExceed (int userHeaderStartPosition, HSSFRow row, 
            HSSFRow rowHeader, ArrayList maxLengthList) {
                
        int totalCells = rowHeader.getPhysicalNumberOfCells();
        String strCell = "";       
        for ( int i = userHeaderStartPosition; i < totalCells; i++ ) {
        
            HSSFCell cellHeader = rowHeader.getCell((short)i);
            HSSFCell cell = row.getCell((short)i);
            strCell = getCellValue(cell);
            
            if ( !strCell.equals("")){
            
                if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.REQUIREDFIELD_FIRST_NAME) 
                        && !isMaxLength32(strCell) ) {
                            
                    maxLengthList.add(CTBConstants.REQUIREDFIELD_FIRST_NAME);
                }
                
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.MIDDLE_NAME)
                        && !isMaxLength32(strCell) ) {
                            
                    maxLengthList.add(CTBConstants.MIDDLE_NAME);
                    
                }
                
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.REQUIREDFIELD_LAST_NAME)
                        && !isMaxLength32(strCell) ) {
                            
                    maxLengthList.add(CTBConstants.REQUIREDFIELD_LAST_NAME);
                    
                }
                
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.EMAIL)
                        && !strCell.trim().equals("")
                        && !isMaxLength64(strCell) ) {
                    
                        maxLengthList.add(CTBConstants.EMAIL);
                    
                }
                
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.EXT_PIN1)
                        && !strCell.trim().equals("")
                        && !isMaxLength20(strCell) ) {
                    
                        maxLengthList.add(CTBConstants.EXT_PIN1);
                    
                }
                
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.REQUIREDFIELD_TIME_ZONE)
                        && !isMaxLength255(strCell) ) {
                    
                    maxLengthList.add(CTBConstants.REQUIREDFIELD_TIME_ZONE);
                    
                }
                
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.ADDRESS_LINE_1)
                        && !strCell.trim().equals("")
                        && !isMaxLength64(strCell) ) {
                    
                    maxLengthList.add(CTBConstants.ADDRESS_LINE_1);
                    
                }
                
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.ADDRESS_LINE_2)
                        && !strCell.trim().equals("")
                        && !isMaxLength64(strCell) ) {
                    
                    maxLengthList.add(CTBConstants.ADDRESS_LINE_2);
                    
                }
                
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.CITY)
                        && !strCell.trim().equals("")
                        && !isMaxLength64(strCell) ) {
                    
                    maxLengthList.add(CTBConstants.CITY);
                    
                }
                
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.ZIP)
                        && !strCell.trim().equals("")
                        && !isMaxLength15(strCell) ) {
                    
                    maxLengthList.add(CTBConstants.ZIP);
                    
                }
                
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.PRIMARY_PHONE)
                        && !strCell.trim().equals("")
                        && !isMaxLength32(strCell) ) {
                     
                    maxLengthList.add(CTBConstants.PRIMARY_PHONE);
                    
                }
                
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.SECONDARY_PHONE)
                        && !strCell.trim().equals("")
                        && !isMaxLength32(strCell) ) {
                     
                    maxLengthList.add(CTBConstants.SECONDARY_PHONE);
                    
                }
                
                else if ( cellHeader.getStringCellValue().
                        equals(CTBConstants.FAX)
                        && !strCell.trim().equals("")
                        && !isMaxLength32(strCell) ) {
                     
                    maxLengthList.add(CTBConstants.FAX);
                    
                }
            }
  
        }
        
        if ( maxLengthList.size() == 0 ) {
            
            return false;
            
        } else {
            
            return true;   
        }
        
    }
    
    /*
    * check wheather Maxlength is 32
    */ 
    
    private boolean isMaxLength15 ( String value) {
        
        if ( value.length() <= 15 ) {
            
            return true;
        
        } else {
            
            return false;
            
        }
        
    }
    
    /*
    * check wheather Maxlength is 32
    */ 
    
    private boolean isMaxLength32 ( String value) {
        
        if ( value.length() <= 32 ) {
            
            return true;
        
        } else {
            
            return false;
            
        }
        
    }
    
    /*
    * check wheather Maxlength is 64
    */
    
    private boolean isMaxLength64 ( String value ) {
        
        if ( value.length() <= 64 ) {
            
            return true;
            
        } else {
            
            return false;   
        }
      
    }
    
    /*
    * check wheather Maxlength is 255
    */
    
    private boolean isMaxLength255 ( String value ) {
        
        if ( value.length() <= 255 ) {
            
            return true;
        
        } else {
            
            return false;   
        }
        
    }
    
    /*
    * check wheather Maxlength is 50
    */
    
    private boolean isMaxLength50 ( String value ) {
        
        if ( value.length() <= 50 ) {
            
            return true;
        
        } else {
            
            return false;   
        }
        
    }
    
    /*
    * check wheather Maxlength is 50
    */
    
    private boolean isMaxLength20 ( String value ) {
        
        if ( value.length() <= 20 ) {
            
            return true;
        
        } else {
            
            return false;   
        }
        
    }
    
  /*
  *
  */ 
    private boolean isValidHierarchy (ArrayList orgNodeNames, 
                            LinkedHashMap orgDetails,StringBuffer sb) {
        
        Set OrganizationNameSet = orgDetails.keySet();
        
        Iterator it = OrganizationNameSet.iterator();
        
        
        boolean commonPath = false;
        while ( it.hasNext() ) {
            
            String keys  = (String) it.next();
            String  orgNodeName =  (String)  orgDetails.get( keys ) ;     
            if ( sb.indexOf(orgNodeName) != -1) {
            
                commonPath = true;
            
            } else {
             
                commonPath = false;
            
            }
            
        }      
        
        return commonPath;
    }
    
    // valid Address
    
    private static boolean validAddressString(String str) {
        str = str.trim();
        char[] characters = str.toCharArray();
        for ( int i=0 ; i<characters.length ; i++ ) {
            
            char character = characters[i];
            if (! validAddressCharacter(character) ) {
             
                return false;
            
            }
        }
        return true;
    }

    //
    private static boolean validAddressCharacter(char ch) {
        boolean valid = validNameCharacter(ch);
    	if (ch == '#') {
            
           valid = true; 
        
        }
    
        return valid;
    }
     
    /*
     * validate invalid /valid  Field
    */
    
    
     private  boolean validNameString(String str) {
        str = str.trim();
        char[] characters = str.toCharArray();
        for (int i=0 ; i<characters.length ; i++) {
            
            char character = characters[i];
            if (! validNameCharacter(character)) {
            
                return false;
            
            }
        }
        return true;
    } 
    
    
     private static boolean validNameCharacter(char ch) {
        
        boolean A_Z = ((ch >= 65) && (ch <= 90));
        boolean a_z = ((ch >= 97) && (ch <= 122));
        boolean zero_nine = ((ch >= 48) && (ch <= 57));
        boolean validChar = ((ch == '/') || 
                             (ch == '\'') || 
                             (ch == '-') || 
                             (ch == '_') ||
                             (ch == '\\') || 
                             (ch == '.') || 
                             (ch == '(') || 
                             (ch == ')') || 
                             (ch == '&') || 
                             (ch == '+') || 
                             (ch == ',') || 
                             (ch == ' '));
        
        return (zero_nine || A_Z || a_z || validChar);
    }
    
    private  boolean validString(String str) {
        str = str.trim();
        char[] characters = str.toCharArray();
        for (int i=0 ; i<characters.length ; i++) {
            char character = characters[i];
            if (! validOrgNameCharacter(character)) {
                
                return false;
                
            }
        }
        return true;
    } 
    
    private static boolean validOrgNameCharacter(char ch) {
        boolean A_Z = ((ch >= 65) && (ch <= 90));
        boolean a_z = ((ch >= 97) && (ch <= 122));
        boolean zero_nine = ((ch >= 48) && (ch <= 57));
         // !, @, #, -, _, ', :, /, comma, period, and space will be allowed in these fields.
        boolean validChar = ((ch == '/') ||
                             (ch == '\\') ||
                             (ch == '-') ||
                             (ch == '\'') ||
                             (ch == '(') ||
                             (ch == ')') ||
                             (ch == '&') ||
                             (ch == '+') ||
                             (ch == ',') ||
                             (ch == '.') ||
                             (ch == ' '));
         
        return (zero_nine || A_Z || a_z || validChar);
    }
    
     private static boolean validEmail(String email) {  
              
        email = email.trim();
        boolean matchFound = true;
        
        if (email != null && email.length()>0) {
                     
           Pattern p = Pattern.compile("[a-z|A-Z|0-9|_|\\-|.]+@[a-z|A-Z|0-9|_|\\-|.]+\\.[a-z|A-Z|0-9|_|\\-|.]+");               
           //Match the given string with the pattern
           Matcher m = p.matcher(email);
           //check whether match is found 
           matchFound = m.matches();
        }        
        return matchFound;        
    }
    
    private static boolean isValidPhone(String str) {
        
        String[] pieces = tokenize(str, " ()-eExt.:,");
        if(pieces== null || pieces.length<3 || pieces.length>4){
            return false;
        }
        
        for(int i=0;i<pieces.length;i++) {
            String phonePart = pieces[i];
            if(i == 0){
                if (phonePart.length() != 3 || !isValidNumber(phonePart)){
                    return false;
                }
            }
            
            else if(i == 1){
                if (phonePart.length() != 3 || !isValidNumber(phonePart)){
                    return false;
                }
            }
            
            else if(i == 2){
                if (phonePart.length() != 4 || !isValidNumber(phonePart)){
                    return false;
                }
            }
            
            else if(i == 3){
                if (phonePart.length() > 4 || !isValidNumber(phonePart)){
                    return false;
                }
            }
        }

        return true;
    }
    
     private static boolean isValidFax(String str) {
        
        String[] pieces = tokenize(str, " ()-eExt.:,");
        if(pieces== null || pieces.length != 3){
            return false;
        }
        
        for(int i=0;i<pieces.length;i++) {
            String phonePart = pieces[i];
            if(i == 0){
                if (phonePart.length() != 3 || !isValidNumber(phonePart)){
                    return false;
                }
            }
            
            else if(i == 1){
                if (phonePart.length() != 3 || !isValidNumber(phonePart)){
                    return false;
                }
            }
            
            else if(i == 2){
                if (phonePart.length() != 4 || !isValidNumber(phonePart)){
                    return false;
                }
            }
        }

        return true;
    }
    
    private String getPhoneFax(String str) {
        
        String[] pieces = tokenize(str, " ()-eExt.:,");
        String result = "";
        for(int i=0;i<pieces.length;i++) {
            String phonePart = pieces[i];
            if(i == 0){
               result +="(" + phonePart + ") ";
            }
            
            else if(i == 1){
                result +=phonePart;
            }
            
            else if(i == 2){
                result += "-" + phonePart;
            }
            
            else if(i == 3){
                result += " x " + phonePart;
            }
        }

        return result;
    }
    
    final public static String[] tokenize(String parameter, String delimiter){
        String tokens[];
        int nextItem=0;
        StringTokenizer stoke=new StringTokenizer(parameter,delimiter);
        tokens=new String[stoke.countTokens()];
        while(stoke.hasMoreTokens()){
            tokens[nextItem]=stoke.nextToken();
            nextItem=(nextItem+1)%tokens.length;
        }
      return tokens;
    }      
     
    private static boolean isValidFormat (String str) {
		
		String []splitNumber = str.split("\\)");
		String firstNumber = splitNumber[0].trim().substring(1);
		boolean isValid = false;
		
		//If block for first part
		if (firstNumber.trim().length() == 3 
				&& isValidNumber(firstNumber.trim())) {
			
			splitNumber = splitNumber[1].split("-");
			isValid = true;
					
			//If block for second part		
			if (splitNumber[0].trim().length() == 3 
					&& isValidNumber(splitNumber[0].trim())) {
				
				splitNumber = splitNumber[1].split("x");
				isValid = true;
				
				//If block for third part		
				if (splitNumber[0].trim().length() == 4 
						&& isValidNumber(splitNumber[0].trim())) {
					
					if (splitNumber.length == 2) {
						
						//If block for extension
						if (splitNumber[1].trim().length() < 5 
								&& isValidNumber(splitNumber[1].trim())) {
							
							isValid = true;
							
						} else {
							
							return false;
							
						} //End If-else block for extension
						
					}
					
					isValid = true;
					
				} else {
					
					return false;
					
				} // End If-else for third part
			} else {
				
				return false;
				
			} // End If-else for second part
		} else {
			
				return false;
			
		} // End If-else for first part
		
		if (isValid) {
			
			return true;
			
		} else {
			
			return false;
		}
	}   
    
    // valid numeric field
    private static boolean isValidNumber (String number) {
		
		for (int i = 0; i < number.length(); i++ ) {
			char ch = number.charAt(i);
			if (!((ch >= 48) && (ch <= 57))) {
                return false;
            }
		}
		return true;
	}
    
    // valid zip field
    private static boolean isValidZipFormat (String zip) {
		
		String []splitNumber = zip.split("-");
		boolean isValid = false;
		
		if (splitNumber[0].trim().length() == 5 
				&& isValidNumber(splitNumber[0].trim())) {
			
			isValid = true;
			
			if (splitNumber.length > 1) {
				
				if (splitNumber[1].trim().length() <= 5 
						&& isValidNumber(splitNumber[1].trim())) {
					
					isValid = true;
					
				} else {
					
					return false;
					
				}
				
			}
			
		} else {
			
			return false;
			
		}
		
		if (isValid) {
			
			return true;
			
		} else {
			
			return false;
			
		}
	}      

     
     /**
	 * @return Returns the serverFilePath.
	 */
	public String getServerFilePath() {
		return serverFilePath;
	}
	/**
	 * @param serverFilePath The serverFilePath to set.
	 */
	public void setServerFilePath(String serverFilePath) {
		this.serverFilePath = serverFilePath;
	}
          
    /**
	 * @return Returns the userFileRowHeader.
	 */
	public UserFileRow[] getUserFileRowHeader() {
		return userFileRowHeader;
	}

	/**
	 * @param userFileRowHeader The userFileRowHeader to set.
	 */
	public void setUserFileRowHeader(UserFileRow[] userFileRowHeader) {
		this.userFileRowHeader = userFileRowHeader;
	}
          
    public void setUsername(String username) {
        this.username = username;
    }

    public void setUploadedFilename(InputStream uploadedStream) {
        this.uploadedStream = uploadedStream;
    }
    public void setUploadDt(Date uploadDt) {
        this.uploadDt = uploadDt;
    }
    public void setNoOfHeaderRows(int noOfUserColumn) {
        this.noOfUserColumn = noOfUserColumn;
    }
    
    public int getFailedRecordCount() {
        return failedRecordCount;
    }

    public void setFailedRecordCount(int failedRecordCount) {
        this.failedRecordCount = failedRecordCount;
    }

    public int getUploadRecordCount() {
        return uploadRecordCount;
    }

    public void setUploadRecordCount(int uploadRecordCount) {
        this.uploadRecordCount = uploadRecordCount;
    }
    
    public void setCommonHierarchyMap(HashMap commonHierarchyMap) {
        this.commonHierarchyMap = commonHierarchyMap;
    }
       
}    
   
    
    
    
    
    
 
