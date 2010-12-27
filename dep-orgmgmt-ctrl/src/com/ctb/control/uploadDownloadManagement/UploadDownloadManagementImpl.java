package com.ctb.control.uploadDownloadManagement; 

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.request.SortParams.SortParam;
import com.ctb.bean.request.SortParams.SortType;
import com.ctb.bean.studentManagement.CustomerConfiguration;
import com.ctb.bean.studentManagement.CustomerConfigurationValue;
import com.ctb.bean.testAdmin.Address;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfig;
import com.ctb.bean.testAdmin.DataFileAudit;
import com.ctb.bean.testAdmin.DataFileAuditData;
import com.ctb.bean.testAdmin.DataFileTemp;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.OrgNodeCategory;
import com.ctb.bean.testAdmin.StudentDemoGraphics;
import com.ctb.bean.testAdmin.StudentDemoGraphicsData;
import com.ctb.bean.testAdmin.StudentFile;
import com.ctb.bean.testAdmin.StudentFileRow;
import com.ctb.bean.testAdmin.StudentFileRowData;
import com.ctb.bean.testAdmin.User;
import com.ctb.bean.testAdmin.UserFile;
import com.ctb.bean.testAdmin.UserFileRow;
import com.ctb.bean.testAdmin.UserFileRowData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.uploadDownloadManagement.DeleteFileException;
import com.ctb.exception.uploadDownloadManagement.DownloadTemplateException;
import com.ctb.exception.uploadDownloadManagement.FileDownloadException;
import com.ctb.exception.uploadDownloadManagement.FileHeaderException;
import com.ctb.exception.uploadDownloadManagement.FileHistoryException;
import com.ctb.exception.uploadDownloadManagement.FileNotUploadedException;
import com.ctb.util.CTBConstants;
import com.ctb.util.DateUtils;
import com.ctb.util.HeaderOrder;
import com.ctb.util.StudentHeader;
import com.ctb.util.UploadProcess;
import com.ctb.util.UploadStudent;
import com.ctb.util.UserHeader;

/**
 * @editor-info:code-gen control-interface="true"
 */
@ControlImplementation(isTransient=true)
public class UploadDownloadManagementImpl implements UploadDownloadManagement
{ 
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.Roles roles;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.OrgNodeCategory orgNodeCate;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.Users users;
    
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.StudentAccommodation studentAccommodation;

    
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.Students students;
    
    
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.OrgNode orgNode;
    
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.UploadDataFile uploadDataFile;
    
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.Addresses address;
   
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.Customer customerdb;
    
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.userManagement.UserManagement userManagement;
   
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.organizationManagement.OrganizationManagement organizationManagement;
    
    /**
     * @common:control
     */
    
    @org.apache.beehive.controls.api.bean.Control()
     private com.ctb.control.studentManagement.StudentManagement studentManagement ;
    
     //Changes for GA2011CR001
    CustomerConfiguration[] customerConfigurations = null;
    CustomerConfigurationValue[] customerConfigurationsValue = null;
    private String loginUserName = null;
	private Integer customerId = null;
	private boolean isStudentIdConfigurable = false;
	private boolean isStudentId2Configurable = false;
	private boolean isGTIDMandatory = false;
	private Integer configId=0;
	private String []valueForStudentId = null ;
	private String []valueForStudentId2 = null ;
	//START- GACR005 
	private String studentIdMinLength = "0";
	private String studentId2MinLength = "0";
	private String isStudentIdNumeric = "AN";
	private String isStudentId2Numeric = "AN";
    //END- GACR005
    // END
    static final long serialVersionUID = 1L;

    /**
     * @common:operation
     */
    public UserFile getUserFileTemplate (String userName) 
                                        throws CTBBusinessException {
        
        UserFile userFile = new UserFile();
        try {
            StringBuffer orgNodeBuff = null;
            HashMap hierarchyMap = new HashMap();
            HashMap commonPathMap = new HashMap ();
            HashMap cellMap = new HashMap ();
            ArrayList topNodeList = new ArrayList();
            UserFileRow []userFileRow = null;
                        
            Node []userTopOrgNode =  orgNode.getTopNodesForUser(userName);
            Customer customer = users.getCustomer(userName);
            //Template Header Creation for Organization and User
            OrgNodeCategory []OrgNodeCategory = 
                    orgNodeCate.getOrgNodeCategories(customer.getCustomerId());

            Node [] sortedOrgNodes = new Node[OrgNodeCategory.length];                     
            //retrive userNode details
            Node [] detailNode = uploadDataFile.getUserDataTemplate(userName);
           
            /*login user is belonging those nodes which are in parent-child 
             relation ship, then we need to
            delete child nodes*/
            
             retriveTopNode(userTopOrgNode,detailNode,topNodeList);
             Node []topOrgNode =  (Node [])topNodeList.toArray(new Node[0]);
              
            //generate org hierarchy
            generateHiarchyFromLoginUserToLeafNode(
                    detailNode, topOrgNode, hierarchyMap);
            
            /* generate commonPath form customer Organization to 
               loginUser Organization 
            */
            retrivePathFromCustomerToLoginUser (
                    customer, topOrgNode, commonPathMap);
            
            generateFullUniquePath(hierarchyMap, commonPathMap);
            
            //we need to add 1 because userFileRow[0] contains header part.
            int totalSize = hierarchyMap.size() + 1; 
            
            userFileRow = new UserFileRow[totalSize];
            
            // Insert Header Part
            createTemplateHeader(customer,OrgNodeCategory, userFileRow, true);
            //Insert Template Body
            createTemplateBody(hierarchyMap,userFileRow, true);
            
            // Sorting User Template
            
             for ( int j = 1; j < userFileRow.length; j++ ) {
            
                Node []orgNodes = userFileRow[j].getOrganizationNodes();
                
                sortedOrgNodes = getOrgPathForSort(OrgNodeCategory, orgNodes);
                
                orgNodeBuff = new StringBuffer();
                
                for ( int k = 0 ; k < sortedOrgNodes.length ; k++ ) {
                
                    orgNodeBuff.append(sortedOrgNodes[k].getOrgNodeName());
                    
                }
                
                userFileRow[j].setPrimarySort(orgNodeBuff.toString());
                
             
            }
            
            UserFileRowData userFileRowData = new UserFileRowData();
            userFileRowData.setUserFileRows(userFileRow, null);
            
            SortParams sort = buildFileSortParams("PrimarySort", null);
            
            if ( sort != null ) {
            
                userFileRowData.applySorting(sort);
            
            }
            
            userFileRow = userFileRowData.getUserFileRows();
            
            
            
          /*  
            applyPrimarySortUser(userFileRow,0);
            Node []header = userFileRow[0].getOrganizationNodes();
                
                
            for (int lp = 0; lp < header.length; lp++) {
             
                for (int j = 1; j < userFileRow.length; j++) {
                    
                    Node []NparentNodes = userFileRow[j].getOrganizationNodes();
                    
                    if (lp < NparentNodes.length) {
                        
                        Node NparentNode = NparentNodes[lp];
                        applySecondarySortUser (userFileRow, NparentNode
                                ,lp + 1);
                        
                    }
            
                }
                
            }*/
                   
            
                   
         userFile.setUserFileRows(userFileRow);
             
         } catch(SQLException e){
            
           DownloadTemplateException downloadTemplateException = 
                                        new DownloadTemplateException
                                                ("DownloadTemplate.Failed");
            downloadTemplateException.setStackTrace(e.getStackTrace());
            throw downloadTemplateException;
         
         } catch(Exception e){
            
           DownloadTemplateException downloadTemplateException = 
                                        new DownloadTemplateException
                                                ("DownloadTemplate.Failed");
            downloadTemplateException.setStackTrace(e.getStackTrace());
            throw downloadTemplateException;
         
         }
        
        return userFile; 
        
    }
   
    
    
   
    /**
     * @common:operation
     */
    public StudentFile getStudentFileTemplate(String userName)
                                            throws CTBBusinessException{
        
        StudentFile studentFile = new StudentFile();
        
        try {
            StringBuffer orgNodeBuff = null;
            HashMap cellMap = new HashMap ();
            HashMap commonPathMap = new HashMap ();
            Node []userTopOrgNode =  orgNode.getTopNodesForUser(userName);
            Customer customer = users.getCustomer(userName);
            StudentFileRow []studentFileRow = null;
            ArrayList topNodeList = new ArrayList();
            HashMap hierarchyMap =  new HashMap();
            //Template Header Creation for Organization and Student
            OrgNodeCategory []OrgNodeCategory = 
                                orgNodeCate.getOrgNodeCategories(
                                customer.getCustomerId());
            
            Node [] sortedOrgNodes = new Node[OrgNodeCategory.length];                     
            //retrive StudentNode details
            Node [] detailNode = uploadDataFile.getUserDataTemplate(userName);
           
            /*login user is belonging those nodes which are 
             * in parent-child relation ship, then we need to
             * delete child nodes
             */
            
             retriveTopNode(userTopOrgNode,detailNode,topNodeList);
             Node []topOrgNode =  (Node [])topNodeList.toArray(new Node[0]);
            
            //generate org hierarchy
            generateHiarchyFromLoginUserToLeafNode(
                    detailNode, topOrgNode, hierarchyMap);
            
           /* generate commonPath form customer Organization
            *  to loginUser Organization
            */
            retrivePathFromCustomerToLoginUser (customer, 
                    topOrgNode, commonPathMap);
            
            generateFullUniquePath(hierarchyMap, commonPathMap);
            
            //we need to add 1 because userFileRow[0] contains header part.
            int totalSize = hierarchyMap.size() + 1; 
            
            studentFileRow = new StudentFileRow[totalSize];
            
            isStudentIDConfigurableCustomer(userName);
            // Insert Header Part
            createTemplateHeader(customer,OrgNodeCategory, 
                    studentFileRow, false);
                    
            //Insert Template Body
            createTemplateBody(hierarchyMap,studentFileRow, false);
            
            // Sorting Student Template
            
             for ( int j = 1; j < studentFileRow.length; j++ ) {
            
                Node []orgNodes = studentFileRow[j].getOrganizationNodes();
                
                sortedOrgNodes = getOrgPathForSort(OrgNodeCategory, orgNodes);
                
                orgNodeBuff = new StringBuffer();
                
                for ( int k = 0 ; k < sortedOrgNodes.length ; k++ ) {
                
                    orgNodeBuff.append(sortedOrgNodes[k].getOrgNodeName());
                    
                }
                
                studentFileRow[j].setPrimarySort(orgNodeBuff.toString());
                
             
            }
            
            StudentFileRowData studentFileRowData = new StudentFileRowData();
            studentFileRowData.setStudentFileRows(studentFileRow, null);
            
            SortParams sort = buildFileSortParams("PrimarySort", null);
            
            if ( sort != null ) {
            
                studentFileRowData.applySorting(sort);
            
            }
            
            studentFileRow = studentFileRowData.getStudentFileRows();
            /*applyPrimarySortStudent(studentFileRow,0);
            Node []header = studentFileRow[0].getOrganizationNodes();
                
                
            for (int lp = 0; lp < header.length; lp++) {
             
                for (int j = 1; j < studentFileRow.length; j++) {
                    
                    Node []NparentNodes = studentFileRow[j].getOrganizationNodes();
                    
                    if (lp < NparentNodes.length) {
                        
                        Node NparentNode = NparentNodes[lp];
                        applySecondarySortStudent (studentFileRow, NparentNode
                                ,lp + 1);
                        
                    }
            
                }
                
            }*/
            
           /* Generating Header row of Student Templet
            * createExcelHeaderForStudent(OrgNodeCategory,
            * StudentDemoGraphics, new StudentHeader(), cellMap);
            * Insert data in to Student Templet
            * insertContentIntoExcel(cellMap, hierarchyMap);
            */
            studentFile.setStudentFileRows(studentFileRow);
            
            
        } catch(SQLException se){
            DownloadTemplateException downloadTemplateException = 
                                        new DownloadTemplateException
                                                ("DownloadTemplate.Failed");
            downloadTemplateException.setStackTrace(se.getStackTrace());
            throw downloadTemplateException;
                                                          
        } catch (Exception e) {
            DownloadTemplateException downloadTemplateException = 
                                        new DownloadTemplateException
                                                ("DownloadTemplate.Failed");
            downloadTemplateException.setStackTrace(e.getStackTrace());
            throw downloadTemplateException;
            
        } 
        
        return studentFile;
        
        
    }

    /**
     * @common:operation
     */
    public UserFile getUserFile(String userName) throws CTBBusinessException {
        
        UserFile userFile = new UserFile();
        try {
            StringBuffer orgNodeBuff = null;
            String displayUserName = null;
            HashMap hierarchyMap = new HashMap();           
            HashMap commonPathMap = new HashMap();
            Node []userTopOrgNode =  orgNode.getTopNodesForUser(userName);
            Customer customer = users.getCustomer(userName);
            //Template Header Creation for Organization and User
            OrgNodeCategory []OrgNodeCategory = 
                    orgNodeCate.getOrgNodeCategories(customer.getCustomerId());
            UserFileRow [] userFileRow = uploadDataFile.getUserData(userName);
            //retrive userNode details
            Node [] detailNode = uploadDataFile.getUserDataTemplate(userName);
           
            Node [] sortedOrgNodes = new Node[OrgNodeCategory.length];   
            ArrayList topNodeList = new ArrayList();
            /*login user is belonging those nodes which 
             * are in parent-child relation ship, then we need to
             * delete child nodes
            */
            retriveTopNode(userTopOrgNode,detailNode,topNodeList);
            Node []topOrgNode =  (Node [])topNodeList.toArray(new Node[0]);
            
            int totalSize = userFileRow.length + 1;
            UserFileRow [] downLoadUserFileRow = new UserFileRow[totalSize];
             // Insert downLoad Header Part
            createTemplateHeader(customer,OrgNodeCategory, downLoadUserFileRow, true);
            //Insert downLoad Body Part
            retrivePathFromCustomerToLoginUser (customer, topOrgNode, commonPathMap);
            for (int i = 0, j = 1; i < userFileRow.length; i++, j++) {
                
                downLoadUserFileRow[j] = userFileRow[i];
                Integer addressId = userFileRow[i].getAddressId();
                if (addressId != null) {
                    
                    Address addressDetail = address.getAddress(addressId);
                    downLoadUserFileRow[j].setAddress1(addressDetail.getAddressLine1());
                    downLoadUserFileRow[j].setAddress2(addressDetail.getAddressLine2());
                    downLoadUserFileRow[j].setCity(addressDetail.getCity());
                    downLoadUserFileRow[j].setState(addressDetail.getStateDesc());
                    if (addressDetail.getZipCodeExt() != null) {
                        
                        downLoadUserFileRow[j].setZip(addressDetail.
                            getZipCode()+"-"+addressDetail.getZipCodeExt());
                        
                    } else {
                        
                        downLoadUserFileRow[j].setZip(addressDetail.
                            getZipCode());
                        
                    }
                    
                    downLoadUserFileRow[j].setPrimaryPhone(addressDetail.
                        getPrimaryPhone());
                    if(downLoadUserFileRow[j].getPrimaryPhone() != null 
                            && downLoadUserFileRow[j].getPrimaryPhone().length()>0){    
                       String phone =  downLoadUserFileRow[j].getPrimaryPhone().replaceAll("x","Ext:");
                       downLoadUserFileRow[j].setPrimaryPhone(phone);
                    }
                    
                    downLoadUserFileRow[j].setSecondaryPhone(
                            addressDetail.getSecondaryPhone());
                    if(downLoadUserFileRow[j].getSecondaryPhone() != null 
                            && downLoadUserFileRow[j].getSecondaryPhone().length()>0){    
                        String phone =  downLoadUserFileRow[j].getSecondaryPhone().replaceAll("x","Ext:");
                        downLoadUserFileRow[j].setSecondaryPhone(phone);
                    }
                            
                    downLoadUserFileRow[j].setFaxNumber(
                            addressDetail.getFaxNumber());        
                    
                    
                    
                }
                
                Integer orgNodeId = downLoadUserFileRow[j].getOrgNodeId();
                Node currentNode = new Node();
                currentNode.setOrgNodeId(orgNodeId);
                //retrive user orgNode path hierarchy
                retriveUniquepathForUser(customer, detailNode, currentNode, 
                        topOrgNode, hierarchyMap, commonPathMap);
                        
                ArrayList orgPath = (ArrayList)hierarchyMap.get(orgNodeId);
                Node []pathNode = new Node[orgPath.size()];
                int count = 0;
                for (int k = orgPath.size() - 1; k >= 0; k-- ) {
                    
                    Node node = (Node)orgPath.get(k);
                    pathNode[count++] = node;
                    
                 }
                
                 downLoadUserFileRow[j].setOrganizationNodes(pathNode);
                
            }
            
            // sort user template
            for ( int j = 1; j < downLoadUserFileRow.length; j++ ) {
            
                Node []orgNodes = downLoadUserFileRow[j].getOrganizationNodes();
                
                sortedOrgNodes = getOrgPathForSort(OrgNodeCategory, orgNodes);
                
                orgNodeBuff = new StringBuffer();
                
                for ( int k = 0 ; k < sortedOrgNodes.length ; k++ ) {
                
                    orgNodeBuff.append(sortedOrgNodes[k].getOrgNodeName());
                    
                }
                
                downLoadUserFileRow[j].setPrimarySort(orgNodeBuff.toString());
                
               
                displayUserName = downLoadUserFileRow[j].getDisplayUserName();
                                
                downLoadUserFileRow[j].setSecondarySort(displayUserName);
            }
            
            UserFileRowData userFileRowData = new UserFileRowData();
            userFileRowData.setUserFileRows(downLoadUserFileRow, null);
            
            SortParams sort = buildFileSortParams("PrimarySort", "SecondarySort");
            
            if ( sort != null ) {
            
                userFileRowData.applySorting(sort);
            
            }
            
            downLoadUserFileRow = userFileRowData.getUserFileRows();
            
    /*        applyPrimarySortUser(downLoadUserFileRow,0);
            Node []header = downLoadUserFileRow[0].getOrganizationNodes();
                
                
           for (int lp = 0; lp < header.length; lp++) {
             
                for (int j = 1; j < downLoadUserFileRow.length; j++) {
                    
                    Node []NparentNodes = downLoadUserFileRow[j].
                            getOrganizationNodes();
                    
                    if (lp < NparentNodes.length) {
                        
                        Node NparentNode = NparentNodes[lp];
                        applySecondarySortUser (downLoadUserFileRow, 
                                NparentNode, lp + 1);
                        
                    }
            
                }
                
            }*/
            
            userFile.setUserFileRows(downLoadUserFileRow);
            
        } catch (SQLException se){
            FileDownloadException fileDownloadException = 
                                        new FileDownloadException
                                                ("FileDownload.Failed");
            fileDownloadException.setStackTrace(se.getStackTrace());
            throw fileDownloadException;
        } catch (Exception e){
            FileDownloadException fileDownloadException = 
                                        new FileDownloadException
                                                ("FileDownload.Failed");
            fileDownloadException.setStackTrace(e.getStackTrace());
            throw fileDownloadException;
        }
        
        return userFile;
        
    }
    
  


    /**
     * @common:operation
     */
   public StudentFile getStudentFile(String userName)throws CTBBusinessException {
        
        StudentFile studentFile = new StudentFile();
       
        try {
            StringBuffer orgNodeBuff = null;
            String displayStudentName = null;
            HashMap hierarchyMap = new HashMap();
            HashMap commonPathMap = new HashMap ();
            Node []userTopOrgNode =  orgNode.getTopNodesForUser(userName);
            Customer customer = users.getCustomer(userName);
            //System.out.println("getStudentFile" + userName + customer+ customer.getCustomerId());
            //Template Header Creation for Organization and User
            OrgNodeCategory []OrgNodeCategory = orgNodeCate.getOrgNodeCategories(
                                                            customer.getCustomerId());
            Node [] sortedOrgNodes = new Node[OrgNodeCategory.length];                                                              
            //retrive userNode details
            Node [] detailNode = uploadDataFile.getUserDataTemplate(userName);
            ArrayList topNodeList = new ArrayList();
            /* login user is belonging those nodes which are in parent-child 
             *relation ship, then we need to
            delete child nodes*/
            retriveTopNode(userTopOrgNode,detailNode,topNodeList);
            Node []topOrgNode =  (Node [])topNodeList.toArray(new Node[0]);

            //retrive Student details

            StudentFileRow [] studentFileRow = 
                    uploadDataFile.getStudentData(userName);
            
            int totalSize = studentFileRow.length + 1; 
            StudentFileRow [] downloadStudentFileRow =
                    new StudentFileRow[totalSize];
            
            isStudentIDConfigurableCustomer(userName);
                        
             // Insert downLoad Header Part
            createTemplateHeader(customer,OrgNodeCategory, 
                    downloadStudentFileRow, false);
            
             //generate org hierarchy
            generateHiarchyFromLoginUserToLeafNode(detailNode, 
                    topOrgNode, hierarchyMap);
        
            // generate commonPath form customer Organization to loginUser Organization
        
            retrivePathFromCustomerToLoginUser (
                    customer, topOrgNode, commonPathMap);
        
            generateFullUniquePath(hierarchyMap, commonPathMap);  
         
        
            
            //Insert downLoad Body Part
            
            for(int i=0, j =1 ; i < studentFileRow.length; i++, j++) {
                
                
                downloadStudentFileRow[j] = studentFileRow[i];
                
                downloadStudentFileRow[j].setFirstName(
                        studentFileRow[i].getFirstName());
                        
                downloadStudentFileRow[j].setMiddleName(
                        studentFileRow[i].getMiddleName());
                        
                downloadStudentFileRow[j].setLastName(
                        studentFileRow[i].getLastName());
                        
                downloadStudentFileRow[j].setBirthdate(
                        studentFileRow[i].getBirthdate());
                        
                downloadStudentFileRow[j].setGrade(
                        studentFileRow[i].getGrade());
                        
                downloadStudentFileRow[j].setStudentId(
                        studentFileRow[i].getStudentId());
                        
                downloadStudentFileRow[j].setExtPin2(
                        studentFileRow[i].getExtPin2());
                        
                downloadStudentFileRow[j].setGender(
                        studentFileRow[i].getGender());
                
                
                // acoomodation values 'T' and 'F' should come as 'TRUE' and 'FALSE' respectively while downloading              
                
                if(studentFileRow[i].getScreenReader() != null 
                        && studentFileRow[i].getScreenReader().equals(CTBConstants.T)){
                    downloadStudentFileRow[j].setScreenReader(CTBConstants.ACOMODATION_TRUE); 
                }
                else if(studentFileRow[i].getScreenReader() != null 
                        && studentFileRow[i].getScreenReader().equals(CTBConstants.F)){
                    downloadStudentFileRow[j].setScreenReader(CTBConstants.ACOMODATION_FALSE);
                }
                else{
                    downloadStudentFileRow[j].setScreenReader(
                            studentFileRow[i].getScreenReader());
                }
                
                
                if(studentFileRow[i].getCalculator() != null 
                        && studentFileRow[i].getCalculator().equals(CTBConstants.T)){
                    downloadStudentFileRow[j].setCalculator(CTBConstants.ACOMODATION_TRUE); 
                }
                else if(studentFileRow[i].getCalculator() != null 
                        && studentFileRow[i].getCalculator().equals(CTBConstants.F)){
                    downloadStudentFileRow[j].setCalculator(CTBConstants.ACOMODATION_FALSE);
                }
                else{
                    downloadStudentFileRow[j].setCalculator(
                            studentFileRow[i].getCalculator());
                }
                  
                  
                if(studentFileRow[i].getTestPause() != null 
                        && studentFileRow[i].getTestPause().equals(CTBConstants.T)){
                    downloadStudentFileRow[j].setTestPause(CTBConstants.ACOMODATION_TRUE); 
                }
                else if(studentFileRow[i].getTestPause() != null 
                        && studentFileRow[i].getTestPause().equals(CTBConstants.F)){
                    downloadStudentFileRow[j].setTestPause(CTBConstants.ACOMODATION_FALSE);
                }
                else{
                    downloadStudentFileRow[j].setTestPause(
                            studentFileRow[i].getTestPause());
                } 
                
                if(studentFileRow[i].getUntimedTest() != null 
                        && studentFileRow[i].getUntimedTest().equals(CTBConstants.T)){
                    downloadStudentFileRow[j].setUntimedTest(CTBConstants.ACOMODATION_TRUE); 
                }
                else if(studentFileRow[i].getUntimedTest() != null 
                        && studentFileRow[i].getUntimedTest().equals(CTBConstants.F)){
                    downloadStudentFileRow[j].setUntimedTest(CTBConstants.ACOMODATION_FALSE);
                }
                else{
                    downloadStudentFileRow[j].setUntimedTest(
                            studentFileRow[i].getUntimedTest());
                }        
                
                if(studentFileRow[i].getHighlighter() != null 
                        && studentFileRow[i].getHighlighter().equals(CTBConstants.T)){
                    downloadStudentFileRow[j].setHighlighter(CTBConstants.ACOMODATION_TRUE); 
                }
                else if(studentFileRow[i].getHighlighter() != null 
                        && studentFileRow[i].getHighlighter().equals(CTBConstants.F)){
                    downloadStudentFileRow[j].setHighlighter(CTBConstants.ACOMODATION_FALSE);
                }
                else{
                    downloadStudentFileRow[j].setHighlighter(
                            studentFileRow[i].getHighlighter());
                }        
                        
                        
                downloadStudentFileRow[j].setQuestionBackgroundColor(
                        studentFileRow[i].getQuestionBackgroundColor());
                        
                downloadStudentFileRow[j].setQuestionFontColor(
                        studentFileRow[i].getQuestionFontColor());
                        
                downloadStudentFileRow[j].setQuestionFontSize(
                        studentFileRow[i].getQuestionFontSize());
                        
                downloadStudentFileRow[j].setAnswerBackgroundColor(
                        studentFileRow[i].getAnswerBackgroundColor());
                        
                downloadStudentFileRow[j].setAnswerFontColor(
                        studentFileRow[i].getAnswerFontColor());
                        
                downloadStudentFileRow[j].setAnswerFontSize(
                        studentFileRow[i].getAnswerFontSize());
                
                
                StudentDemoGraphicsData []studentDemoGraphicsData = 
                        uploadDataFile.getStudentDemoGraphicsData
                        (studentFileRow[i].getStudentId());
                                                                                
                downloadStudentFileRow[j].setStudentDemoGraphicsData(
                        studentDemoGraphicsData);
                   
                      
                if ( hierarchyMap.containsKey(
                        downloadStudentFileRow[j].getOrgNodeId()) ) {
                    
                    ArrayList orgPath = (ArrayList)hierarchyMap.
                            get(downloadStudentFileRow[j].getOrgNodeId());
                            
                    Node []pathNode = new Node[orgPath.size()];
                    int count = 0;
                    for ( int k = orgPath.size() - 1; k >= 0; k-- ) {
                        
                        Node node = (Node)orgPath.get(k);
                        pathNode[count++] = node;
                        
                     }
                     
                  downloadStudentFileRow[j].setOrganizationNodes(pathNode);   
                    
                }  
            }
            
            // Sorting Student Template
            
             for ( int j = 1; j < downloadStudentFileRow.length; j++ ) {
            
                Node []orgNodes = downloadStudentFileRow[j].getOrganizationNodes();
                
                sortedOrgNodes = getOrgPathForSort(OrgNodeCategory, orgNodes);
                
                orgNodeBuff = new StringBuffer();
                
                for ( int k = 0 ; k < sortedOrgNodes.length ; k++ ) {
                
                    orgNodeBuff.append(sortedOrgNodes[k].getOrgNodeName());
                    
                }
                
                downloadStudentFileRow[j].setPrimarySort(orgNodeBuff.toString());
                
                displayStudentName = downloadStudentFileRow[j].getDisplayStudentName();
                                
                downloadStudentFileRow[j].setSecondarySort(displayStudentName);
            }
            
            StudentFileRowData studentFileRowData = new StudentFileRowData();
            studentFileRowData.setStudentFileRows(downloadStudentFileRow, null);
            
            SortParams sort = buildFileSortParams("PrimarySort", "SecondarySort");
            
            if ( sort != null ) {
            
                studentFileRowData.applySorting(sort);
            
            }
            
            downloadStudentFileRow = studentFileRowData.getStudentFileRows();
            
            /*applyPrimarySortStudent(downloadStudentFileRow,0);
            Node []header = downloadStudentFileRow[0].getOrganizationNodes();
                
                
            for ( int lp = 0; lp < header.length; lp++ ) {
             
                for (int j = 1; j < downloadStudentFileRow.length; j++) {
                    
                    Node []NparentNodes = downloadStudentFileRow[j].
                            getOrganizationNodes();
                    
                    if (lp < NparentNodes.length) {
                        
                        Node NparentNode = NparentNodes[lp];
                        applySecondarySortStudent (downloadStudentFileRow,
                                 NparentNode, lp + 1);
                        
                    }
            
                }
                
            }*/
            
         studentFile.setStudentFileRows(downloadStudentFileRow);   
            
        } catch (SQLException se){
            FileDownloadException fileDownloadException = 
                                        new FileDownloadException
                                                ("FileDownload.Failed");
            fileDownloadException.setStackTrace(se.getStackTrace());
            throw fileDownloadException;
        } catch (Exception e){
            FileDownloadException fileDownloadException = 
                                        new FileDownloadException
                                                ("FileDownload.Failed");
            fileDownloadException.setStackTrace(e.getStackTrace());
            throw fileDownloadException;
        }
        
        return studentFile;
    }


    /**
     * @common:operation
     */
    public Integer createDataFileTemp(DataFileTemp dataFileTemp) 
                            throws CTBBusinessException {
                                
        Integer uploadDataFileId = null;
        
                         
        try {
              
            uploadDataFileId = uploadDataFile.getNextPK(); 
                        
            dataFileTemp.setDataFileAuditId(uploadDataFileId);
            uploadDataFile.createDataFileTemp(dataFileTemp);
            
            
        } catch (SQLException e) {
            
            FileNotUploadedException fileNotUploadedException = 
                                        new FileNotUploadedException
                                                ("Uploaded.Failed");
            fileNotUploadedException.setStackTrace(e.getStackTrace());
            throw fileNotUploadedException;
          
        } catch (Exception e ) {
        
            FileNotUploadedException fileNotUploadedException = 
                                        new FileNotUploadedException
                                                ("Uploaded.Failed");
            fileNotUploadedException.setStackTrace(e.getStackTrace());
            throw fileNotUploadedException;
        
        }
        
        return uploadDataFileId;
   }
    

    /**
     * @common:operation
     */
    public Integer addErrorDataFile(String userName, String serverFilePath, Integer uploadDataFileId) 
                            throws CTBBusinessException {
                                
 //       Integer uploadDataFileId = null;
        
       
    	int noOfUserColumn = 0;
        
       String excelFile = serverFilePath;

        // Used to read the file type
        InputStream fileInputStrean = null;
                        
        try {
            DataFileAudit dataFileAudit = new DataFileAudit();
            
            saveFileToDisk(serverFilePath, uploadDataFileId);
            
            // Get the user
            User user = users.getUserDetails(userName);
                
             // get user customer Id
            Customer customer = users.getCustomer(userName);
            Integer customerId = customer.getCustomerId();
            
            if ( serverFilePath.indexOf(String.valueOf(
                    customerId.intValue())) <=  -1  ) {
                
                CTBBusinessException be = 
                        new CTBBusinessException("Uploaded.Failed");
                         throw be;
            }
            
            // get the start column number of userdata
            noOfUserColumn = (orgNodeCate.
                    getOrgNodeCategories(customerId).length) * 2 + 1;
                    
            fileInputStrean = new FileInputStream(new File(excelFile));            
            
            OrgNodeCategory[] orgNodeCategory = 
                    orgNodeCate.getOrgNodeCategories(customer.getCustomerId());
            
            //Validating the excel sheet
            String fileType = getUploadFileType(
                    fileInputStrean,noOfUserColumn,customerId,
                    orgNodeCategory, customer, userName);            
            
            if ( fileType == "") {
             
                throw  new CTBBusinessException("Uploaded.Failed") ;
            
            }
             
    //        uploadDataFileId = uploadDataFile.getNextPK(); 
                        
            dataFileAudit.setDataFileAuditId(uploadDataFileId);
            dataFileAudit.setUploadFileName(getExactFileName(serverFilePath));
            dataFileAudit.setCreatedDateTime(new Date());
            dataFileAudit.setStatus("IN");
            dataFileAudit.setUserId(user.getUserId());
            dataFileAudit.setCustomerId(customerId);
            dataFileAudit.setCreatedBy(user.getUserId());
            uploadDataFile.createDataFileAudit(dataFileAudit);
            
            
         } catch (FileNotFoundException fn) {
        
            FileNotUploadedException fileNotUploadedException = 
                                        new FileNotUploadedException
                                                ("FileNotFound.Failed");
            fileNotUploadedException.setStackTrace(fn.getStackTrace());
            throw fileNotUploadedException;
        
        } catch (FileHeaderException fh) {
             FileHeaderException fileHeaderException = 
                                        new FileHeaderException
                                                ("FileHeader.Failed");
             fileHeaderException.setStackTrace(fh.getStackTrace());
             throw fileHeaderException;
        } catch (CTBBusinessException e) {

            FileHeaderException fileHeaderException = 
                                        new FileHeaderException
                                                ("Uploaded.Failed");
            fileHeaderException.setStackTrace(e.getStackTrace());
            throw fileHeaderException;
        
        } catch (SQLException e) {
            
            FileNotUploadedException fileNotUploadedException = 
                                        new FileNotUploadedException
                                                ("Uploaded.Failed");
            fileNotUploadedException.setStackTrace(e.getStackTrace());
            throw fileNotUploadedException;
          
        } catch (Exception e ) {
        
            FileNotUploadedException fileNotUploadedException = 
                                        new FileNotUploadedException
                                                ("Uploaded.Failed");
            fileNotUploadedException.setStackTrace(e.getStackTrace());
            throw fileNotUploadedException;
        
        }
        
        return uploadDataFileId;
   }
   
   
   /**
     * @common:operation
     */
    public void updateAuditFileStatus(Integer uploadFileId) 
                            throws CTBBusinessException {
                                
        try {
            
            DataFileAudit dataFileAudit = new DataFileAudit();
            dataFileAudit.setStatus("FL");
            dataFileAudit.setDataFileAuditId(uploadFileId);
            uploadDataFile.updateAuditFileStatus(dataFileAudit);
            
            
        } catch (SQLException e){
            FileNotUploadedException fileNotUploadedException = 
                                        new FileNotUploadedException
                                                ("Uploaded.Failed");
            fileNotUploadedException.setStackTrace(e.getStackTrace());
            throw fileNotUploadedException;
        }
         catch (Exception e) {
            
            FileNotUploadedException fileNotUploadedException = 
                                        new FileNotUploadedException
                                                ("Uploaded.Failed");
            fileNotUploadedException.setStackTrace(e.getStackTrace());
            throw fileNotUploadedException;
        } 
        
   }

    
    private boolean saveFileToDisk(String saveFileName, Integer uploadDataFileId) throws Exception {
        OutputStream baos = null; 
         
        try {
            Blob tempDataFile = uploadDataFile.getDataFileTemp(uploadDataFileId); 
         
            long fileSize = tempDataFile.length();                                 
            // guarantee path exists
            File file = new File(saveFileName);
            if (!file.exists()) {
                // if the path doesn't exist, create it
                file.createNewFile();
                
            }
        
            boolean moreData = true;
            InputStream is = tempDataFile.getBinaryStream();

            baos = new FileOutputStream(file);
            while(moreData) {
                byte [] buffer = new byte[1024];
                int read = is.read(buffer);
                moreData = read > 0;
                if(moreData) {
                    baos.write(buffer, 0, read);
                }
            }
            return true;          

		} catch (Exception e) {
			e.printStackTrace();
            throw e;
            
		} finally {
            baos.flush();
			baos.close();
		}
    }

   /**
     * @common:operation
     */
    public void uploadFile(String userName, String serverFilePath, Integer uploadDataFileId) 
                            throws CTBBusinessException {
        
       try {
         
           saveFileToDisk(serverFilePath, uploadDataFileId);
        
           Integer customerId = new Integer(0);
           StringBuffer container = new StringBuffer(); 
           int noOfUserColumn = 0;
         
           String excelFile = serverFilePath;
           InputStream inputStream = null;
           
           // Used to read the file type
           InputStream fileInputStrean = null;
           
           POIFSFileSystem pfs = null;
           HSSFSheet sheet = null;
           HashMap commonHierarchyMap = new HashMap ();
           DataFileAudit dataFileAudit = new DataFileAudit();
           boolean isValidHeader = false;
       
             // get user customer Id
            Customer customer = users.getCustomer(userName);
            customerId = customer.getCustomerId();
            
            // Get the user
            User user = users.getUserDetails(userName);
            
            // Get top Node
            
            Integer userId = user.getUserId();
            // Insert  the recorde into data_file_audit table
            if ( serverFilePath.indexOf(String.valueOf(
                    customerId.intValue())) <=  -1  ) {
                
                CTBBusinessException be = 
                        new CTBBusinessException("Uploaded.Failed");
                         throw be;
            }
            
           
            dataFileAudit = uploadDataFile.getUploadFile(uploadDataFileId);
          
    /*        dataFileAudit.setDataFileAuditId(uploadDataFile.getNextPK());
            dataFileAudit.setUploadFileName(getExactFileName(serverFilePath));
            dataFileAudit.setCreatedDateTime(new Date());
            dataFileAudit.setStatus("IN");
            dataFileAudit.setUserId(user.getUserId());
            dataFileAudit.setCustomerId(customerId);
            dataFileAudit.setCreatedBy(user.getUserId());
            uploadDataFile.createDataFileAudit(dataFileAudit);*/
          
            //Retrieve common path from customer to loginuser
            
            Node []userTopOrgNode =  orgNode.getTopNodesForUser(userName);
           
            //retrive common Path from customer organization to 
            //login user organization
            retrivePathFromCustomerToLoginUser (customer,
                     userTopOrgNode,commonHierarchyMap);
            
            
            // get the start column number of userdata
            noOfUserColumn = (orgNodeCate.
                    getOrgNodeCategories(customerId).length) * 2 + 1;
                    
			inputStream = new FileInputStream(new File(excelFile));
            fileInputStrean = new FileInputStream(new File(excelFile));
            
            // Determine file Type. If can not determine then throws exception
            
            OrgNodeCategory[] orgNodeCategory = 
                    orgNodeCate.getOrgNodeCategories(customer.getCustomerId());
            
            String fileType = getUploadFileType(
                    fileInputStrean,noOfUserColumn,customerId,
                    orgNodeCategory, customer, userName);
            
            
                                                            
            UserFileRow []userFileRowHeader = new UserFileRow[1];
            createTemplateHeader(customer,orgNodeCategory, 
                    userFileRowHeader, true);
            
            StudentFileRow [] studentFileRowHeader = 
                    new StudentFileRow[1];                                                            
                    
            createTemplateHeader(customer,orgNodeCategory, 
                    studentFileRowHeader, false);
            
            if ( fileType == "") {
             
                throw  new CTBBusinessException("Uploaded.Failed") ;
            
            }
                
            if ( fileType .equals("Upload_User_Data" ) ) {	
                
                uploadUserFile(serverFilePath, inputStream, userName ,
                        userFileRowHeader, noOfUserColumn, commonHierarchyMap,
                        fileInputStrean, dataFileAudit, userTopOrgNode);
           
            }
        
            if ( fileType .equals("Upload_Student_Data" ) ) {
                
                 uploadStudentFile(serverFilePath,inputStream, userName ,
                         studentFileRowHeader,noOfUserColumn,
                         commonHierarchyMap,fileInputStrean,dataFileAudit, userTopOrgNode);
                
            }
            
            
        
        } catch (FileNotFoundException fn) {
        
            FileNotUploadedException fileNotUploadedException = 
                                        new FileNotUploadedException
                                                ("FileNotFound.Failed");
            fileNotUploadedException.setStackTrace(fn.getStackTrace());
            throw fileNotUploadedException;
        
        } catch (FileHeaderException fh) {
             FileHeaderException fileHeaderException = 
                                        new FileHeaderException
                                                ("FileHeader.Failed");
             fileHeaderException.setStackTrace(fh.getStackTrace());
             throw fileHeaderException;
        } catch (CTBBusinessException e) {

            FileHeaderException fileHeaderException = 
                                        new FileHeaderException
                                                ("Uploaded.Failed");
            fileHeaderException.setStackTrace(e.getStackTrace());
            throw fileHeaderException;
        
        } catch (SQLException e) {
            
            FileNotUploadedException fileNotUploadedException = 
                                        new FileNotUploadedException
                                                ("Uploaded.Failed");
            fileNotUploadedException.setStackTrace(e.getStackTrace());
            throw fileNotUploadedException;
          
        } catch (Exception e ) {
        
            FileNotUploadedException fileNotUploadedException = 
                                        new FileNotUploadedException
                                                ("Uploaded.Failed");
            fileNotUploadedException.setStackTrace(e.getStackTrace());
            throw fileNotUploadedException;
        
        } 
       	
    }
     
    /**
     * @common:operation
     */
    public byte[]  getErrorDataFile(String userName, Integer auditFileId) 
                                    throws CTBBusinessException {
        
        byte[] errorRecodFile = null;
        InputStream inputStream = null;
        try{
            Blob blob = uploadDataFile.getErrorDataFile(auditFileId);
            errorRecodFile = blob.getBytes(1L,(int)blob.length());
            
        } catch(SQLException e) {
            FileDownloadException fileDownloadException = 
                                        new FileDownloadException
                                                ("FileDownload.Failed");
            fileDownloadException.setStackTrace(e.getStackTrace());
            throw fileDownloadException;
        } catch(Exception e){
            FileDownloadException fileDownloadException = 
                                        new FileDownloadException
                                                ("FileDownload.Failed");
            fileDownloadException.setStackTrace(e.getStackTrace());
            throw fileDownloadException;
        }
        return errorRecodFile;
    }
    
    
     /**
     * @common:operation
     */
   public DataFileAuditData getUploadHistory(String userName, 
                                              PageParams page , 
                                              SortParams sort)
                                              throws CTBBusinessException {
                                                
        DataFileAuditData dataFileAuditData = null;
        try {
             dataFileAuditData = getDataFileAuditData(userName, page, sort);

        } catch (SQLException e) {
            FileHistoryException fileHistoryException = 
                                        new FileHistoryException
                                                ("UploadHistory.Failed");
            fileHistoryException.setStackTrace(e.getStackTrace());
            throw fileHistoryException;
        } catch (CTBBusinessException be) {
            FileHistoryException fileHistoryException = 
                                        new FileHistoryException
                                                ("UploadHistory.Failed");
            fileHistoryException.setStackTrace(be.getStackTrace());
            throw fileHistoryException;
        }
        return dataFileAuditData;
    }
    
    

     /**
      * it will get the common path from customer to the login user node
      * @common:operation
      * @param userName
      * @param customer
      * @param userTopOrgNode
      * @param commonPathMap
     */
     
    public void getcommonPathFromCustomerToLoginUser(String userName,
                                                     Customer customer , 
                                                     Node []userTopOrgNode, 
                                                     HashMap commonPathMap)
                                                     throws CTBBusinessException {
        
        HashMap hierarchyMap = new HashMap();          
        ArrayList topNodeList = new ArrayList();
        UserFileRow []userFileRow = null;
        
        
            
         try {   
            
            
            Node []commonPath = uploadDataFile.
                                getOrgPathFromCustomerToLoginUser(
                                customer.getCustomerId());
                        
            //retrive userNode details
            Node [] detailNode = uploadDataFile.getUserDataTemplate(userName);
            
            /* login user is belonging those nodes which are
             * in parent-child relation ship, then we need to
             * delete child nodes*/
            
             retriveTopNode(userTopOrgNode,detailNode,topNodeList);
              
             Node []topOrgNode =  (Node [])topNodeList.toArray(new Node[0]);
             //generate org hierarchy
            generateHiarchyFromLoginUserToLeafNode(detailNode, 
                    topOrgNode, hierarchyMap);
                    
            retrivePathFromCustomerToLoginUser (customer, 
                    topOrgNode,commonPathMap);
            
         } catch (SQLException se ) {
             FileNotUploadedException 
                        exception = 
                            new FileNotUploadedException(
                                    "Uploaded.Failed");
            exception.setStackTrace(se.getStackTrace());
            throw exception;
         } catch (Exception e ) {
             FileNotUploadedException 
                        exception = 
                            new FileNotUploadedException(
                                    "Uploaded.Failed");
            exception.setStackTrace(e.getStackTrace());
            throw exception;
         }
         
         
    }
    
    
   /**
    * delete error file
     * @common:operation
     * @param auditFileId
     * @throws CTBBusinessException
     */
    public void deleteErrorDataFile(Integer auditFileId) 
                                    throws CTBBusinessException {
        try {
            
            uploadDataFile.deleteErrorDataFile(auditFileId);
            
        } catch (SQLException e){
            DeleteFileException deleteFileException = 
                                        new DeleteFileException
                                                ("DeleteFile.Failed");
            deleteFileException.setStackTrace(e.getStackTrace());
            throw deleteFileException;
        }
         catch (Exception e) {
            
            DeleteFileException deleteFileException = 
                                        new DeleteFileException
                                                ("DeleteFile.Failed");
            deleteFileException.setStackTrace(e.getStackTrace());
            throw deleteFileException;
        }
        
    }
    
    
    
    
    /**
     * Check whether user has the
     * @common:operation
     * @param userName - identifies the user
     * @param  customerId - identifies the customer
     * @return Boolean
	 * @throws CTBBusinessException
     */
    public Boolean hasUploadDownloadConfig(String userName) throws CTBBusinessException {
        try {
            Boolean hasUploadDownloadConfig = Boolean.FALSE;
            Integer customerId = users.getCustomerIdForName(userName);
            if(customerId != null){
                CustomerConfig[] cc = uploadDataFile.getCustomerConfigurationEntries(customerId);  
                if(cc != null && cc.length>0 ){
                    for (int i = 0; i<cc.length; i++){
                        CustomerConfig customerConfig = cc[i];
                        if((CTBConstants.CUSTOMER_CONFIG_UPLOAD_DOWNLOAD.
                                    equals(customerConfig.getCustomerConfigurationName())) 
                                    && (CTBConstants.T.equals(customerConfig.getDefaultValue()))){
                            hasUploadDownloadConfig = Boolean.TRUE;
                        }
                    }
                }
            }
            return hasUploadDownloadConfig;
        } catch (SQLException se) {
            FileNotUploadedException 
                        exception = 
                            new FileNotUploadedException(
                                    "Uploaded.Failed");
            exception.setStackTrace(se.getStackTrace());
            throw exception;
        } catch (Exception e) {
            FileNotUploadedException 
                        exception = 
                            new FileNotUploadedException(
                                    "Uploaded.Failed");
            exception.setStackTrace(e.getStackTrace());
            throw exception;
        }
    }
    
    
    
    ///////////////////////////////Private Methods//////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////
    
    
    /*
    *Sorting User Template
    */ 
    private void applyPrimarySortUser (UserFileRow[] userFileRow, int searchPosition) {
        
        
        UserFileRow tempUserFileRow = new UserFileRow();
        int val = 0;
        
        for (int i = 1; i < userFileRow.length; i++) {
            
            for (int j = i + 1; j < userFileRow.length ; j++) {
                
                Node []first = userFileRow[i].getOrganizationNodes();
                Node []second =  userFileRow[j].getOrganizationNodes();
                
                if (searchPosition >= first.length || searchPosition >= second.length){
                    
                    continue;
                    
                } 
                    
                val = first[searchPosition].getOrgNodeName().toUpperCase().
                        compareTo(second[searchPosition].getOrgNodeName().toUpperCase());
            
                if (val > 0) {
                    
                    tempUserFileRow = userFileRow[i];
                    userFileRow[i] = userFileRow[j];
                    userFileRow[j] = tempUserFileRow;
                    
                    
                }
              
            }
            
        }
     
    }
    /*
    *Sorting User Template
    */ 
    private void applySecondarySortUser (UserFileRow[] userFileRow, Node parentNode, int searchPosition) {
        
        
        UserFileRow tempUserFileRow = new UserFileRow();
        int val = 0;
        
        for (int i = 1; i < userFileRow.length; i++) {
            
            for (int j = i + 1; j < userFileRow.length ; j++) {
                
                Node []first = userFileRow[i].getOrganizationNodes();
                Node []second =  userFileRow[j].getOrganizationNodes();
                
                if (searchPosition >= first.length || searchPosition >= second.length){
                    
                    continue;
                    
                } else if ((parentNode.getOrgNodeId().intValue() == 
                                first[searchPosition].getParentOrgNodeId().intValue())  
                                && (parentNode.getOrgNodeId().intValue() == 
                                second[searchPosition].getParentOrgNodeId().intValue())) {
                    
                    val = first[searchPosition].getOrgNodeName().toUpperCase().
                            compareTo(second[searchPosition].getOrgNodeName().toUpperCase());
                
                    if (val > 0) {
                        
                        tempUserFileRow = userFileRow[i];
                        userFileRow[i] = userFileRow[j];
                        userFileRow[j] = tempUserFileRow;
                        
                        
                    }
                    
                } 
     
            }
            
        }
     
    }
    
    
    /*
    *Sorting Student Template
    */ 
    private void applyPrimarySortStudent (StudentFileRow[] studentFileRow, int searchPosition) {
        
        
        StudentFileRow tempStudentFileRow = new StudentFileRow();
        int val = 0;
        
        for (int i = 1; i < studentFileRow.length; i++) {
            
            for (int j = i + 1; j < studentFileRow.length ; j++) {
                
                Node []first = studentFileRow[i].getOrganizationNodes();
                Node []second =  studentFileRow[j].getOrganizationNodes();
                
                if (searchPosition >= first.length || searchPosition >= second.length){
                    
                    continue;
                    
                } 
                    
                val = first[searchPosition].getOrgNodeName().toUpperCase().
                        compareTo(second[searchPosition].getOrgNodeName().toUpperCase());
            
                if (val > 0) {
                    
                    tempStudentFileRow = studentFileRow[i];
                    studentFileRow[i] = studentFileRow[j];
                    studentFileRow[j] = tempStudentFileRow;
                    
                    
                }
              
            }
            
        }
     
    }
    /*
    *Sorting Student Template
    */ 
    private void applySecondarySortStudent (StudentFileRow[] studentFileRow, Node parentNode, int searchPosition) {
        
        
        StudentFileRow tempStudentFileRow = new StudentFileRow();
        int val = 0;
        
        for (int i = 1; i < studentFileRow.length; i++) {
            
            for (int j = i + 1; j < studentFileRow.length ; j++) {
                
                Node []first = studentFileRow[i].getOrganizationNodes();
                Node []second =  studentFileRow[j].getOrganizationNodes();
                
                if (searchPosition >= first.length || searchPosition >= second.length){
                    
                    continue;
                    
                } else if ((parentNode.getOrgNodeId().intValue() == 
                                first[searchPosition].getParentOrgNodeId().intValue())  
                                && (parentNode.getOrgNodeId().intValue() == 
                                second[searchPosition].getParentOrgNodeId().intValue())) {
                    
                    val = first[searchPosition].getOrgNodeName().toUpperCase().
                            compareTo(second[searchPosition].getOrgNodeName().toUpperCase());
                
                    if (val > 0) {
                        
                        tempStudentFileRow = studentFileRow[i];
                        studentFileRow[i] = studentFileRow[j];
                        studentFileRow[j] = tempStudentFileRow;
                        
                        
                    }
                    
                } 
     
            }
            
        }
     
    }
    
    /*
     * This method will uplad the user data file into db
     * uploadUserFile()
    */
    private void uploadUserFile(String serverFilePath, InputStream inputStream,
                                String userName , UserFileRow []userFileRowHeader,
                                int noOfUserColumn,HashMap commonHierarchyMap,
                                InputStream fileInputStream,DataFileAudit dataFileAudit,
                                Node []userTopOrgNode){
                                    
       System.out.println("  ***** Upload Control: Processing User file");                 
      
       /*    
        BatchProcessor.addProcessToQueue(new UploadProcess(serverFilePath, userName, 
                                            inputStream ,userFileRowHeader, 
                                            noOfUserColumn,commonHierarchyMap,
                                            this.orgNode,orgNodeCate,users,customerdb,
                                            uploadDataFile,organizationManagement,
                                            userManagement, address, roles, dataFileAudit));*/
   
        UploadProcess us =   new UploadProcess(serverFilePath, userName,
                                     inputStream ,userFileRowHeader, 
                                     noOfUserColumn,commonHierarchyMap,
                                     this.orgNode,orgNodeCate,users,customerdb,
                                     uploadDataFile,organizationManagement,
                                     userManagement, address, roles, dataFileAudit
                                     ,userTopOrgNode);  
		//us.run();    
          
        System.out.println("  ***** Upload Control: Done processing");                                                            
  
    }
   
    /*
     * it will retrieve the audit data 
     * getDataFileAuditData()
    */    
    private DataFileAuditData getDataFileAuditData(String userName, 
                                                   PageParams page , 
                                                   SortParams sort) 
                                                   throws SQLException, 
                                                   CTBBusinessException {

        DataFileAuditData dataFileAuditData = new DataFileAuditData();
   //     Integer customerId = new Integer(0);

         // get user info
        User user = users.getUserDetails(userName);
        Integer pageSize = null;
        
        if ( page != null ) {
            
            pageSize = new Integer(page.getPageSize());
        }

        DataFileAudit []dataFileAudit = uploadDataFile.getUploadHistory(userName);
        dataFileAuditData.setDataFileAudit(dataFileAudit,pageSize);

        
        //apply  paging, and sorting
        if(sort != null) {
        
            dataFileAuditData.applySorting(sort);
        
        }
        
        if(page != null) {
        
            dataFileAuditData.applyPaging(page);
        
        }
        
        for( int i = 0; i < dataFileAuditData.getDataFileAudit().length; i++ ) {
                
            if ( dataFileAuditData.getDataFileAudit()[i] != null ) {
                
                Date dt = dataFileAuditData.getDataFileAudit()[i].getCreatedDateTime();
                dt = DateUtils.getAdjustedDate(dt,"GMT",user.getTimeZone(),dt);
                dataFileAuditData.getDataFileAudit()[i].setCreatedDateTime(dt);
                dataFileAuditData.getDataFileAudit()[i].setEditable(
                    getPermisssion(userName,
                    dataFileAuditData.getDataFileAudit()[i].getCreatedBy(), 
                    dataFileAuditData.getDataFileAudit()[i].getFailedRecordCount(),
                    dataFileAuditData.getDataFileAudit()[i].getStatus()));   
                        
            }
        }
       
        return dataFileAuditData;
    }
     
     /*
      * It will insert content into excel file
      * insertContentIntoExcel()
     */ 
      private void insertContentIntoExcel (HashMap cellMap, 
                                           HashMap hierarchyMap) 
                                           throws Exception {
       
        POIFSFileSystem pfs = new POIFSFileSystem(
                                new FileInputStream("Template.xls"));
        HSSFWorkbook rwb = new HSSFWorkbook (pfs);
        HSSFSheet sheet = rwb.getSheet("FirstSheet");
        Set key = hierarchyMap.keySet();
        Iterator it = key.iterator();
         int rowPositiont = 1;
       
        while (it.hasNext()) {
           
            Integer leafOrgNodeId = (Integer)it.next();
            ArrayList orgPath = (ArrayList)hierarchyMap.get(leafOrgNodeId);
            HSSFRow row = sheet.createRow(rowPositiont);
            
            for ( int i = orgPath.size() - 1; i >= 0; i-- ) {
                
                Node node = (Node)orgPath.get(i);
                int cellPosition = retriveCellPosition(cellMap, node);
                
                if ( cellPosition != -1 ) {
                    //insert value for OrgNode
                    HSSFCell cell = row.createCell((short)cellPosition);
                    HSSFCellStyle style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue(node.getOrgNodeName());
                    
                    //insert value for OrgCode
                    cellPosition++;
                    cell = row.createCell((short)cellPosition);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue(node.getOrgNodeCode());
                    
                    
                }
            }
          
            rowPositiont ++;
            
        }
        
        FileOutputStream mfileOut = new FileOutputStream("Template.xls");
		rwb.write(mfileOut);
		
		mfileOut.close();
        
    }
    /*
    * create template Body
    * createTemplateBody()
    */ 
    
    private void createTemplateBody (HashMap hierarchyMap, Object []object, 
                                     boolean isUserHeader) {
        
        Set key = hierarchyMap.keySet();
        Iterator it = key.iterator();
        int rowPositiont = 1;
        while (it.hasNext()) {
            
            if (isUserHeader) {
                
                // Process for UserTemplate
                UserFileRow []userFileRow = (UserFileRow [])object;
                UserFileRow tempUserFileRow = new UserFileRow();
                Integer leafOrgNodeId = (Integer)it.next();
                ArrayList orgPath = (ArrayList)hierarchyMap.get(leafOrgNodeId);
                Node []pathNode = new Node[orgPath.size()];
                int count = 0;
                for ( int i = orgPath.size() - 1; i >= 0; i-- ) {
                    
                    Node node = (Node)orgPath.get(i);
                    pathNode[count++] = node;
                    
                 }
                 tempUserFileRow.setOrganizationNodes(pathNode);
                 userFileRow[rowPositiont] = tempUserFileRow;
                 rowPositiont++;
                
            } else {
                
                // Process for StudentTemplate
                StudentFileRow []studentFileRow = (StudentFileRow [])object;
                StudentFileRow tempStudentFileRow = new StudentFileRow();
                Integer leafOrgNodeId = (Integer)it.next();
                ArrayList orgPath = (ArrayList)hierarchyMap.get(leafOrgNodeId);
                Node []pathNode = new Node[orgPath.size()];
                int count = 0;
                 for (int i = orgPath.size() - 1; i >= 0; i-- ) {
                    
                    Node node = (Node)orgPath.get(i);
                    pathNode[count++] = node;
                    
                 }
                 tempStudentFileRow.setOrganizationNodes(pathNode);
                 studentFileRow[rowPositiont] = tempStudentFileRow;
                 rowPositiont++;
                
            }
            
            
        }
        
    }
    
    /*
    * Create Template header
    * createTemplateHeader()
    */
    
    private void createTemplateHeader (Customer customer,
                                       OrgNodeCategory []OrgNodeCategory, 
                                       Object []object, 
                                       boolean isUserHeader) {
    	//System.out.println("customer" + customer + "Customer Name"  + customer.getCustomerName() + "Customer Id" + customer.getCustomerId() );
        
        int userHeaderPosition = 0;
        int cellPosition = 0;
        
        if (isUserHeader) {
            
            UserFileRow []userFileRow = (UserFileRow [])object;
            UserFileRow tempUserFileRow = new UserFileRow();
            Node node[] = new Node[OrgNodeCategory.length];
            
            // create header for category
            for (int i = 0; i < OrgNodeCategory.length; i++) {
                
                Node currentHeader = new Node();
                currentHeader.setOrgNodeCategoryName(
                                OrgNodeCategory[i].getCategoryName()+ " Name");
                currentHeader.setOrgNodeCode(
                                OrgNodeCategory[i].getCategoryName()+ " Id");
                currentHeader.setOrgNodeCategoryId(
                                OrgNodeCategory[i].getOrgNodeCategoryId());
                node[i] = currentHeader;
                
            }
            
            tempUserFileRow.setOrganizationNodes(node);
            //User header                 
            UserHeader userHeader = new UserHeader();
            tempUserFileRow.setFirstName(userHeader.getFirstName());
            tempUserFileRow.setMiddleName(userHeader.getMiddleName());
            tempUserFileRow.setLastName(userHeader.getLastName());
            tempUserFileRow.setEmail(userHeader.getEmail());
            tempUserFileRow.setTimeZone(userHeader.getTimeZone());
            tempUserFileRow.setRole(userHeader.getRole());
            tempUserFileRow.setRoleName(userHeader.getRole());
            //CR
            tempUserFileRow.setExtPin1(userHeader.getExtPin1());
            tempUserFileRow.setAddress1(userHeader.getAddress1());
            tempUserFileRow.setAddress2(userHeader.getAddress2());
            tempUserFileRow.setCity(userHeader.getCity());
            tempUserFileRow.setState(userHeader.getState());
            tempUserFileRow.setZip(userHeader.getZip());
            tempUserFileRow.setPrimaryPhone(userHeader.getPrimaryPhone());
            tempUserFileRow.setSecondaryPhone(userHeader.getSecondaryPhone());
            tempUserFileRow.setFaxNumber(userHeader.getFaxNumber());
            // userFileRow's first index contains header part
            userFileRow[0] = tempUserFileRow;
                
        } else {
            
            StudentFileRow []studentFileRow = (StudentFileRow[])object;
            StudentFileRow tempStudentFileRow = new StudentFileRow();
            Node node[] = new Node[OrgNodeCategory.length];
            
            for ( int i = 0; i < OrgNodeCategory.length; i++ ) {
                
                Node currentHeader = new Node();
                currentHeader.setOrgNodeCategoryName(OrgNodeCategory[i].
                                                getCategoryName()+ " Name");
                                                
                currentHeader.setOrgNodeCode(
                        OrgNodeCategory[i].getCategoryName()+ " Id");
                        
                currentHeader.setOrgNodeCategoryId(
                        OrgNodeCategory[i].getOrgNodeCategoryId());
                        
                node[i] = currentHeader;
                
            }
            StudentDemoGraphics []StudentDemoGraphics = null;
            CustomerConfig[] customerConfig =  null;
            try {
                
                //Getting Student Demographic data
                StudentDemoGraphics = uploadDataFile.
                        getStudentDemoGraphics(customer.getCustomerId());
                
            } catch(SQLException se){
                
                se.printStackTrace();
                                      
            } catch (Exception e) {
                
                e.printStackTrace();
                
            } 
            
            tempStudentFileRow.setOrganizationNodes(node);
            tempStudentFileRow.setStudentDemoGraphics(StudentDemoGraphics);
            StudentHeader studentHeader = new StudentHeader();
            tempStudentFileRow.setFirstName(studentHeader.getFirstName());
            tempStudentFileRow.setMiddleName(studentHeader.getMiddleName());
            tempStudentFileRow.setLastName(studentHeader.getLastName());
            tempStudentFileRow.setHeaderDateOfBirth(
                    studentHeader.getHeaderDateOfBirthDate());
            tempStudentFileRow.setGrade(studentHeader.getGrade());
            //System.out.println("isStudentIdConfigurable"+"::"+isStudentIdConfigurable);
            if(isStudentIdConfigurable){
            tempStudentFileRow.setExtPin1(this.valueForStudentId[0]);
            }
            else{
            tempStudentFileRow.setExtPin1(studentHeader.getStudentId());
            }
            //System.out.println("isStudentId2Configurable"+"::"+isStudentId2Configurable);
            if(isStudentId2Configurable){
            tempStudentFileRow.setExtPin2(this.valueForStudentId2[0]);
            }
            else{
            tempStudentFileRow.setExtPin2(studentHeader.getStudentId2());
            }
            tempStudentFileRow.setGender(studentHeader.getGender());
            tempStudentFileRow.setScreenReader(studentHeader.getScreenReader());
            tempStudentFileRow.setCalculator(studentHeader.getCalculator());
            tempStudentFileRow.setTestPause(studentHeader.getTestPause());
            tempStudentFileRow.setUntimedTest(studentHeader.getUntimedTest());
            tempStudentFileRow.setHighlighter(studentHeader.getHighlighter());
            
            tempStudentFileRow.setQuestionBackgroundColor(
                    studentHeader.getQuestionBackgroundColor());
                    
            tempStudentFileRow.setQuestionFontColor(
                    studentHeader.getQuestionFontColor());
                    
            tempStudentFileRow.setQuestionFontSize(
                    studentHeader.getQuestionFontSize());
            
            tempStudentFileRow.setAnswerBackgroundColor(
                    studentHeader.getAnswerBackgroundColor());
                    
            tempStudentFileRow.setAnswerFontColor(
                    studentHeader.getAnswerFontColor());
                    
            tempStudentFileRow.setAnswerFontSize(
                    studentHeader.getAnswereFontSize());
            
            
            studentFileRow[0] = tempStudentFileRow;
            
        }
        
        
    }
    
    /*
    * To retrive uniquePath, if login user's topnodes belong to 
    * same hierarchy, then discard the childNode, though login user
    * belongs to that node
    * retriveTopNode()
    *    
    */
    
    private void retriveTopNode (Node []topOrgNode, Node [] detailNode,
                                 ArrayList topNodeList) {
        
     
        for (int i = 0; i < topOrgNode.length; i++) {
            
            Node currentTopNode = topOrgNode[i];
            int currentPosition = retriveNodePosition(currentTopNode, detailNode);
            boolean hasParent = false;
            
            if (currentPosition != -1) {
                
                for (int j = currentPosition; j >= 0; j--) {
                    
                    Node currentNode = detailNode[j];
                    
                    hasParent = hasParentTopNode(currentTopNode, 
                                                topOrgNode, currentNode);
                    /* if hasParent is true then we are not 
                     * consider currentTopNode as a top node
                    * we consider its parent as a top node
                    */ 
                    if (hasParent) {
                        
                        break;  
                        
                    }
                    
                    j = getParentPosition (j, currentNode, detailNode,false);
                    
                    if (j != -1) {
                        
                        j++;
                        
                    }
                    
                }
                
                if (!hasParent) {
                    
                    topNodeList.add(currentTopNode);
                    
                }
                
                
            }
            
        }
        
    } 
    
    /*
    * check wheather current topNode has any parent Node which is also user's top Node
    */ 
    
    private boolean hasParentTopNode (Node currentTopNode, 
                                      Node []topOrgNode, Node currentNode) {
        
        for (int i = 0; i < topOrgNode.length; i++) {
            
            Node node = topOrgNode[i];
            // Same node is processing
            if ( currentTopNode.getOrgNodeId().intValue() 
                    == node.getOrgNodeId().intValue() ) {
                
                continue;
                
            }
            
            if (currentNode.getParentOrgNodeId().intValue() 
                    == node.getOrgNodeId().intValue()) {
                
                return true;
                
            }
            
        }
        
        return false;
        
    }
    
    /*
    * retrive current Node Position from detailNode which contail 
    * all the nodes at and below
    * from login user's node
    * retriveNodePosition()
    */
    private int retriveNodePosition (Node currentNode, Node [] detailNode) {
        
        for (int i = 0; i < detailNode.length; i++) {
            
            Node tempNode = detailNode[i];
            
            if ( currentNode.getOrgNodeId().intValue() 
                    == tempNode.getOrgNodeId().intValue() ) {
                
                return i;
                
            }
            
        }
        
        return -1;
        
    }
    
    /*
    * Generate unique path from customer top node to login User leaf node
    */ 
    
    private void generateFullUniquePath (HashMap hierarchyMap, HashMap commonPathMap) {
        
        if (hierarchyMap.size() == 0) {
            
            Set keys = commonPathMap.keySet();
            Iterator it = keys.iterator();
            while ( it.hasNext() ) {
                
                Integer nodeId = (Integer)it.next();
                ArrayList hierarchyList = (ArrayList)commonPathMap.get(nodeId);
                hierarchyMap.put (nodeId, hierarchyList);
                
            }
            
        } else {
            
            Set keys = hierarchyMap.keySet();
            Iterator it = keys.iterator();
        
            while (it.hasNext()) {
                
                Integer nodeId = (Integer)it.next();
                ArrayList hierarchyDetail = (ArrayList)hierarchyMap.get(nodeId);
                
                Node node = (Node)hierarchyDetail.get(hierarchyDetail.size() - 1);
                
                if (commonPathMap.containsKey(node.getParentOrgNodeId())) {
                    
                    ArrayList pathDetail = (ArrayList)commonPathMap.
                                                get(node.getParentOrgNodeId());
                    
                    
                    for ( int i = 0, position = hierarchyDetail.size(); 
                            i < pathDetail.size(); i++, position++ ) {
                        
                        Node commonPathNode = (Node)pathDetail.get(i);
                        hierarchyDetail.add(position,commonPathNode);
                        hierarchyMap.put(nodeId,hierarchyDetail);
                        
                    }
                    
                } else {
                    /* used for downLoad user when selected user and 
                     * login user is belonging same organization
                     */
                    if (hierarchyDetail.size() == 1) {
                        
                        if (commonPathMap.containsKey(node.getOrgNodeId())) {
                    
                                ArrayList pathDetail = (ArrayList)commonPathMap.
                                                            get(node.getOrgNodeId());
                                
                                hierarchyMap.put(nodeId,pathDetail);    
                    
                        }
                    }   
                } 
            }
            
        }
        
        
    }
    
    /*
    * Generate all unique path from loginUser organization to leaf node
    * generateHiarchyFromLoginUserToLeafNode()
    */
    
    private void generateHiarchyFromLoginUserToLeafNode (Node [] detailNode, 
            Node []topOrgNode, HashMap hierarchyMap) {
                
        HashSet positionSet = new HashSet();
        int beginPosition = 0;
        
        if (topOrgNode.length > 1) {
            
            beginPosition = topOrgNode.length - 1;
            
        } 
        
        for ( int i = detailNode.length -1 ; i > beginPosition; i-- ) {
            
                        
                //retrive leafNode
            ArrayList hierarchyDetail = new ArrayList();
            if ( positionSet.contains(new Integer(i)) ) {
                
                continue;
                
            }
            
            // traverse through the node                
            for ( int k = i; k >= 0; k-- ) {
                //get childNode
                Node childNode = detailNode [k];
                
                
                positionSet.add(new Integer(k));
                
                if (isTopOrganization(childNode, topOrgNode)) {
                    
                    break;
                    
                } else {
                    
                    //add childNode 
                    hierarchyDetail.add(childNode);
                    k = getParentPosition (k, childNode, detailNode,false);
                    
                    if (k != -1) {
                        
                        k++;// we need to process current value of k
                    }
                }
                        
            }
            
            hierarchyMap.put(detailNode [i].getOrgNodeId(),hierarchyDetail);
            
        }
        
    }
    
    /*
    * Retrive commonPath from customer to loginUser
    */
    
    private void retrivePathFromCustomerToLoginUser (Customer customer, 
                                                            Node []topOrgNode,
                                                            HashMap cHierarchyMap)  {
        
        try {
            
            ArrayList hierarchyDetail = new ArrayList();
          
            Node customerNode = orgNode.getTopOrgNodeForCustomer (
                                        customer.getCustomerId());
                                        
            Node []commonPath = uploadDataFile.
                                    getOrgPathFromCustomerToLoginUser(
                                    customer.getCustomerId());
                                                
            // retrive common path Hierarchy
            
            for (int i = 0; i < topOrgNode.length; i++) {
                
                Node node = topOrgNode[i];
                
                if ( customerNode.getOrgNodeId().intValue() == 
                        node.getOrgNodeId().intValue() ) {
                    
                    hierarchyDetail.add (node);
                    cHierarchyMap.put (node.getOrgNodeId(),hierarchyDetail) ;  
                    continue;  
                    
                } else {
                    
                    generateOrgHiarchyFromCustomerToLoginUser (
                            commonPath, node.getOrgNodeId()
                            , cHierarchyMap, customerNode); 
                    
                }
            }
            
        } catch (SQLException se){
            se.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        

    }
    
    /*
    * Generate Hierarchy from customer Top node to login User's topNode(s)
    * generateOrgHiarchyFromCustomerToLoginUser()
    */
    
    private void generateOrgHiarchyFromCustomerToLoginUser (
                                Node []commonPath, Integer userNodeId,
                                HashMap cHierarchyMap, Node customerNode ) {
        
        ArrayList hierarchyDetail = new ArrayList();
        Node [] custometTopNode = new Node[1] ;
        custometTopNode[0] = customerNode;
        
        for (int i = commonPath.length - 1; i >= 0; i--) {
            
            Node node = commonPath[i]; 
            //Search the login user node
            if ( node.getOrgNodeId().intValue() == userNodeId.intValue() ) {
                // traverse loginUserNode to customer top Node
                for ( int j = i; j >= 0; j-- ) {
                    
                    Node tempNode = commonPath[j]; 
                    hierarchyDetail.add(tempNode);
                    
                    if ( isTopOrganization(tempNode, custometTopNode) ) {
                    
                        break;
                    
                    } else {
                    
                       j = getParentPosition (j, tempNode, commonPath,true);
                    
                        if ( j != -1 ) {
                            
                            j++;// we need to process current value of k
                        }
                    }
                    
                    
                }
                
                cHierarchyMap.put (node.getOrgNodeId(),hierarchyDetail);
                
            }
            
        }
    }
    
    /*
    * check wheather childNode is loginUser topNode
    * isTopOrganization()
    */
    
    private boolean isTopOrganization (Node childNode, Node []topOrgNode) {
        
        for (int i = 0; i < topOrgNode.length; i++) {
           
            Node topNode = topOrgNode[i];
            if (topNode.getOrgNodeId().intValue() == childNode.getOrgNodeId().
                                                            intValue()) {
                return true;
            }
        }
        
        return false;
    }
    
      
    /*
    * Map Header Organization Category to selected Organization Category
    * and retrive header category cell position.
    * retriveCellPosition()
    */
    
    private int retriveCellPosition (HashMap cellMap, Node node) {
        
        if (cellMap.containsKey(node.getOrgNodeCategoryId())) {
            
            Integer position =  (Integer)cellMap.get(node.getOrgNodeCategoryId());
            return position.intValue();
        
        }
        
        return -1;
    }
    
    /*
    * Create Excel Header. It Contains Organization Category depends upon customer and
    * User details
    * createExcelHeader()
    */
   
    private void createExcelHeader(OrgNodeCategory []OrgNodeCategory, 
            UserHeader userHeader, HashMap cellMap) 
                                    throws Exception {
        
        HSSFWorkbook wb = new HSSFWorkbook ();
		HSSFSheet sheet = wb.createSheet("FirstSheet");
		HSSFRow row = sheet.createRow(0);
        int userHeaderPosition = 0;
        int cellPosition = 0;
        
        // create header for category
        for ( int i = 0; i < OrgNodeCategory.length; i++ ) {
            
            HSSFCell cell = row.createCell((short)cellPosition);
            HSSFCellStyle style = cell.getCellStyle();
            style.setWrapText(true);
            cell.setCellStyle(style);
            cell.setCellValue(OrgNodeCategory[i].getCategoryName()+ " Name");
            cellMap.put(OrgNodeCategory[i].getOrgNodeCategoryId(),
                    new Integer (cellPosition));
                    
            cellPosition++;
            cell = row.createCell((short)cellPosition);
            style = cell.getCellStyle();
            style.setWrapText(true);
            cell.setCellStyle(style);
            cell.setCellValue(OrgNodeCategory[i].getCategoryName()+ " Id");
            cellPosition ++;
            userHeaderPosition = cellPosition;
            
        }
        // create header for User
        
        ArrayList userHeaderList = userHeader.getUserHeaderList();
        Class userHeaderClass = userHeader.getClass();
        //Method []methods = userHeaderClass.getDeclaredMethods();
        
        for (int i = 0; i <userHeaderList.size(); i++) {
            
            HSSFCell cell = row.createCell((short)userHeaderPosition);
            HSSFCellStyle style = cell.getCellStyle();
            style.setWrapText(true);
            cell.setCellStyle(style);
            String methodName = (String)userHeaderList.get(i);
            Method method = userHeaderClass.getMethod(methodName,null);
            Object value = method.invoke(userHeader, null);
            cell.setCellValue((String)value);
            ++userHeaderPosition;
            
        }
        
        FileOutputStream mfileOut = new FileOutputStream("Template.xls");
		wb.write(mfileOut);
		mfileOut.close();
        
        
    }
    
    
    
    
    
    /*
    *This function retrive parent Organization from currentNode and search parent
    * position in Node[]
    * getParentPosition()
    */
    
    private int getParentPosition (int currentPosition,Node currentNode,
                                         Node[] detailNode, boolean isCommonPath) {
                            
        int endPosition = 1;   
                                         
        if (isCommonPath) {
            
            endPosition = 0;
            
        } 
        
        for ( int i = currentPosition; i >= endPosition; i-- ) {
            
            Node tempNode = detailNode[i]; 
            
            if (currentNode.getParentOrgNodeId().intValue() == tempNode.getOrgNodeId().
                                                                        intValue()) {
                return i;
                
            }
        }
        
        return -1;
    }  
    
  /*
  * retrive uniquPath from customer top organization to selected user's organization.
  * this method is used for download User.
  * retriveUniquepathForUser()
  */  
    
    private void retriveUniquepathForUser ( Customer customer, Node [] detailNode,
            Node currentNode,Node []topOrgNode, HashMap hierarchyMap, HashMap commonPathMap) {
                
          int position = 0; 
          ArrayList hierarchyDetail = new ArrayList();     
                  
          if ( isTopOrganization(currentNode,topOrgNode) ) {
            
            position = retriveNodePosition(currentNode, detailNode);
            hierarchyDetail.add(detailNode[position]);
            hierarchyMap.put(currentNode.getOrgNodeId(),hierarchyDetail);
            
          } else {
            
            position = retriveNodePosition(currentNode, detailNode);
            
            for (int k = position; k >= 0; k--) {
                //get childNode
                Node childNode = detailNode [k];
                
                if (isTopOrganization(childNode, topOrgNode)) {
                    
                    break;
                    
                } else {
                    
                    //add childNode 
                    hierarchyDetail.add(childNode);
                    k = getParentPosition (k, childNode, detailNode,false);
                    
                    if (k != -1) {
                        
                        k++;// we need to process current value of k
                    }
                }
                        
            }
            
          hierarchyMap.put(currentNode.getOrgNodeId(),hierarchyDetail);
            
          }
          
          
       generateFullUniquePath(hierarchyMap, commonPathMap);         
                
    }
   
   /*
    * this will return the uploaded file type 
    * and determine if the header are correct?
    * getUploadFileType()
   */
   
    private String getUploadFileType ( InputStream fileInputStream,
                                       int noOfUserColumn,
                                       Integer customerId,
                                       OrgNodeCategory[] orgNodeCategory,
                                       Customer customer,String userName) throws CTBBusinessException {
    
       isStudentIDConfigurableCustomer(userName);
       String fileType = "";                 
       try {                              
            POIFSFileSystem pfs  = new POIFSFileSystem( fileInputStream );
            HSSFWorkbook wb = new HSSFWorkbook(pfs);
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFCell cells = null;  
            String fullHeader = "";
            String fullDBHeader = ""; 
            ArrayList orgNodeListFromTemplate = new ArrayList(); 
            ArrayList headerList = new ArrayList();
            ArrayList orgNodeList = new ArrayList();
            ArrayList headerListFromTemplate = new ArrayList(); 
            ArrayList demographicsHeaderListTemplate = new ArrayList(); 
            int count = 0;
            
            
            UserFileRow [] userFileRow = new UserFileRow[1];
            StudentFileRow [] studentFileRow = new StudentFileRow[1];       
            
            if ( sheet != null) {
                
                HSSFRow row = sheet.getRow(0);
                
                if(row == null){
                    throw  new CTBBusinessException("FileHeader.Failed") ;
                }
                
                else { 
                
                    int totalColumns = row.getPhysicalNumberOfCells();    
               
                    for ( int i = noOfUserColumn ; i < totalColumns ; i++  ) {
                        
                        cells = row.getCell((short)i);
                        
                        if (cells.getStringCellValue().equals("Role")) {
                          fileType = "Upload_User_Data" ;
                          break;
                        }
                        
                        if (cells.getStringCellValue().equals("Gender")) {
                          fileType = "Upload_Student_Data" ;
                          break;
                        }
                    }
                   
                    
                    if ( fileType.equals("Upload_User_Data") ) {
                         
                        createTemplateHeader(customer,orgNodeCategory, userFileRow, true);
                            
                         for ( int i = 0 ; i < noOfUserColumn - 1 ; i++  ) {
                           
                            cells = row.getCell((short)i);
                            orgNodeListFromTemplate.add(cells.getStringCellValue());
                        
                         }
                         
                                        
                        /* check if the uploaded excel node category header 
                         * and template are same or not
                         */
                        
                        Node[] orgNodes =  userFileRow[0].getOrganizationNodes();
                        //put the orgNodes details into list to maintain order
                        for( int i = 0; i < orgNodes.length; i++ ) {
                            
                            orgNodeList.add(orgNodes[i].getOrgNodeCategoryName());
                            orgNodeList.add(orgNodes[i].getOrgNodeCode());                                             
                            
                         }
                         /* postion should be at the begining and should 
                          * end before user details       
                         */
                        for ( int i =  0 ; i < orgNodeListFromTemplate.size() - 1; i++ ) {
                         
                            
                            if( !orgNodeList.get(i).equals(
                                    orgNodeListFromTemplate.get(i))) {
                                
                                throw  new CTBBusinessException("FileHeader.Failed") ;
                                    
                            }
                            
                        }
                        
                        //check if the uploaded excel header and template are same or not
                        headerListFromTemplate = HeaderOrder.getUserHeaderOrderList();
                        for ( int i = noOfUserColumn - 1 ; i < totalColumns ; i++  ) {
                        
                            cells = row.getCell((short)i);
                            headerList.add(cells.getStringCellValue());
                        }
                        
                        
                        for ( int i = 0; i < headerListFromTemplate.size(); i++ ) {
                           
                            if( !headerListFromTemplate.get(i).
                                    equals(headerList.get(i)) ) {
                            
                               throw  new CTBBusinessException("FileHeader.Failed") ; 
                            
                            }
                        }
                        
                    } 
                    else if ( fileType.equals("Upload_Student_Data") ){    // end of user header validation   
                         
                         createTemplateHeader(customer,orgNodeCategory, 
                                studentFileRow, false); 
                         
                         for ( int i = 0 ; i < noOfUserColumn - 1 ; i++  ) {
                           
                            cells = row.getCell((short)i);
                            orgNodeListFromTemplate.add(cells.getStringCellValue());
                        
                         }
                         
                                        
                        /* check if the uploaded excel node category header 
                         * and template are same or not
                         */
                        
                        Node[] orgNodes =  studentFileRow[0].getOrganizationNodes();
                        //put the orgNodes details into list to maintain order
                        for( int i = 0; i < orgNodes.length; i++ ) {
                            
                            orgNodeList.add(orgNodes[i].getOrgNodeCategoryName());
                            orgNodeList.add(orgNodes[i].getOrgNodeCode());                                             
                            
                         }
                         /* postion should be at the begining and should 
                          * end before student details       
                         */
                        for ( int i =  0 ; i < orgNodeListFromTemplate.size() - 1; i++ ) {
                         
                            
                            if( !orgNodeList.get(i).
                                    equals(orgNodeListFromTemplate.get(i))) {
                                
                                throw  new CTBBusinessException("FileHeader.Failed") ;
                                    
                            }
                            
                        }
                         
                         if(isStudentIdConfigurable || isStudentId2Configurable){  
                         headerListFromTemplate = getStudentIDHeaderOrderList();    
                         }
                         else{
                        	 headerListFromTemplate = HeaderOrder.getStudentHeaderOrderList();  
                         }
                         for ( int i = noOfUserColumn - 1; i < totalColumns; i++) {
                         
                            cells = row.getCell((short)i);
                            headerList.add(cells.getStringCellValue());
                            
                         }
                         
                         /* check if the uploaded excel student info
                            * header and template are same or not*/
                         
                         for ( int i = 0; i < headerListFromTemplate.size(); i++ ) {
                           
                            if( !headerListFromTemplate.get(i).
                                    equals(headerList.get(i)) ) {
                            
                                throw  new CTBBusinessException("FileHeader.Failed") ;
                            
                            }
                         }
                         
                          /* check if the uploaded excel student demographic info
                            * header and template are same or not*/
                         
                         
                         StudentDemoGraphics[] studentDemoGraphics = 
                                            studentFileRow[0].getStudentDemoGraphics();
                         
                        //put the demographic details into list to maintain order
                        for( int i = 0; i < studentDemoGraphics.length; i++ ) {
                            
                            demographicsHeaderListTemplate.add(
                                            studentDemoGraphics[i].getLabelName()); 
                            
                         }
                         
                        //if actual demographic field does not match with given demographic field
                        if((headerList.size()-headerListFromTemplate.size())
                                !=demographicsHeaderListTemplate.size()){
                            throw new CTBBusinessException("FileHeader.Failed") ;
                        }

                         //postion should be after student personal details to end       
                        for ( int i =  headerListFromTemplate.size() ;
                                        i < headerList.size() - 1; i++ ) {
                         
                            
                            if( !headerList.get(i).equals(
                                    demographicsHeaderListTemplate.get(count))) {
                                
                                throw  new CTBBusinessException("FileHeader.Failed") ;
                                    
                            }
                            count++;
                        }
                           
                    } // end of student header validation    
                    else{
                         throw  new CTBBusinessException("FileHeader.Failed") ;
                    }
                }
            }
       } catch (IOException e) {
            FileNotUploadedException fileNotUploadedException = 
                                        new FileNotUploadedException
                                                ("Uploaded.Failed");
            fileNotUploadedException.setStackTrace(e.getStackTrace());
            throw fileNotUploadedException;    
       } catch (CTBBusinessException e ) {
            FileHeaderException fileHeaderException = 
                                        new FileHeaderException
                                                ("FileHeader.Failed");
            fileHeaderException.setStackTrace(e.getStackTrace());
            throw fileHeaderException;
       } 
        return fileType;
     }
    
    private void createExcelHeaderForStudent(OrgNodeCategory []OrgNodeCategory,
                                    StudentDemoGraphics []StudentDemoGraphics,
                                    StudentHeader studentHeader, HashMap cellMap) 
                                    throws Exception {
        
        HSSFWorkbook wb = new HSSFWorkbook ();
		HSSFSheet sheet = wb.createSheet("FirstSheet");
		HSSFRow row = sheet.createRow(0);
        int studentHeaderPosition = 0;
        int cellPosition = 0;

        // create header for category
        for (int i = 0; i < OrgNodeCategory.length; i++) {
            
            HSSFCell cell = row.createCell((short)cellPosition);
            HSSFCellStyle style = cell.getCellStyle();
            style.setWrapText(true);
            cell.setCellStyle(style);
            cell.setCellValue(OrgNodeCategory[i].getCategoryName()+ " Name");
            cellMap.put(OrgNodeCategory[i].getOrgNodeCategoryId(),
                    new Integer (cellPosition));
                    
            cellPosition ++;
            cell = row.createCell((short)cellPosition);
            style = cell.getCellStyle();
            style.setWrapText(true);
            cell.setCellStyle(style);
            cell.setCellValue(OrgNodeCategory[i].getCategoryName()+ " Id");
            cellPosition ++;
            studentHeaderPosition = cellPosition;
            
        }

        
        // create header for Student
        
        ArrayList studentHeaderList = studentHeader.getStudentHeaderList();
        Class userHeaderClass = studentHeader.getClass();
        
        for (int i = 0; i <studentHeaderList.size(); i++) {

            HSSFCell cell = row.createCell((short)studentHeaderPosition);
            HSSFCellStyle style = cell.getCellStyle();
            style.setWrapText(true);
            cell.setCellStyle(style);
            String methodName = (String)studentHeaderList.get(i);
            Method method = userHeaderClass.getMethod(methodName,null);
            Object value = method.invoke(studentHeader, null);
            cell.setCellValue((String)value);
            studentHeaderPosition ++;
        }
        
      //crate header for Student demographic data

        for (int i = 0; i < StudentDemoGraphics.length; i++) {
           
            HSSFCell cell = row.createCell((short)studentHeaderPosition);
            HSSFCellStyle style = cell.getCellStyle();
            style.setWrapText(true);
            cell.setCellStyle(style);
            cell.setCellValue(StudentDemoGraphics[i].getLabelName());
            cellMap.put(StudentDemoGraphics[i].getCustomerDemographicId(),new Integer (i));
            studentHeaderPosition ++;
        }

        
        FileOutputStream mfileOut = new FileOutputStream("Template.xls");
		wb.write(mfileOut);
		mfileOut.close();
        
        
    }

 
    
    
    /*
     * uploadStudentFile()
    */
    private void uploadStudentFile( String serverFilePath,InputStream inputStream, 
                                    String userName ,StudentFileRow[] studentFileRowHeader,
                                    int noOfStudentColumn,HashMap commonHierarchyMap,
                                    InputStream fileInputStream,
                                    DataFileAudit dataFileAudit, Node []userTopOrgNode){
    
              
      /*    BatchProcessor.addProcessToQueue(new UploadStudent(serverFilePath,userName,inputStream , 
            studentFileRowHeader,
            noOfStudentColumn , commonHierarchyMap,
            orgNode ,  orgNodeCate, 
            users,customerdb,
            uploadDataFile, organizationManagement,
            studentManagement,dataFileAudit,students));*/
    	    isStudentIDConfigurableCustomer(userName);
    	
      
         System.out.println("  ***** Upload Control: Processing Student file" + userName); 
         UploadStudent uploadStudent = new UploadStudent(
                                        serverFilePath,userName,inputStream , 
            studentFileRowHeader,
            noOfStudentColumn , commonHierarchyMap,
            orgNode ,  orgNodeCate, 
            users,customerdb,
            uploadDataFile, organizationManagement,
            studentManagement,userManagement, dataFileAudit,students,userTopOrgNode,valueForStudentId,valueForStudentId2);
                                    
                                    
        uploadStudent.run();
        
        System.out.println("  ***** Upload Control: Done processing");
       
    }
    
    /*
     * validateUploadHeader
     * It validates the upload excel data with the template
    */
    
    private boolean validateUploadHeader(InputStream fileInputStream,
                                         Object objects,
                                         int startPosition,
                                         boolean isUserHeader) {
           
        try{
            POIFSFileSystem pfs  = new POIFSFileSystem( fileInputStream );
            HSSFWorkbook wb = new HSSFWorkbook(pfs);
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFCell cells = null;  
            String fullHeader = "";
            String fullDBHeader = ""; 
            ArrayList headerList = new ArrayList();
            ArrayList headerListFromTemplate = new ArrayList(); 
            ArrayList demographicsHeaderListTemplate = new ArrayList();     
            
            if ( sheet != null) {
                HSSFRow row = sheet.getRow(0);
               
                int totalColumns = row.getPhysicalNumberOfCells();
                int count = 0;
                
                if ( row != null ) { 
                    
                    if ( isUserHeader ) {
                        
                        headerListFromTemplate = HeaderOrder.getUserHeaderOrderList();
                        for ( int i = startPosition ; i < totalColumns ; i++  ) {
                        
                            cells = row.getCell((short)i);
                            headerList.add(cells.getStringCellValue());
                        }
                        
                        //check if the uploaded excel header and template are same or not
                        for ( int i = 0; i < headerListFromTemplate.size(); i++ ) {
                           
                            if( !headerListFromTemplate.get(i).equals(headerList.get(i)) ) {
                            
                                return false;
                            
                            }
                        }
                        
                    } else {    // end of user header validation   
                    	 if(isStudentIdConfigurable || isStudentId2Configurable){  
                             headerListFromTemplate = getStudentIDHeaderOrderList();    
                             }
                             else{
                            	 headerListFromTemplate = HeaderOrder.getStudentHeaderOrderList();  
                             }
                        // headerListFromTemplate = HeaderOrder.getStudentHeaderOrderList();      
                         for ( int i = startPosition; i < totalColumns; i++) {
                         
                            cells = row.getCell((short)i);
                            headerList.add(cells.getStringCellValue());
                            
                         }
                         
                         /* check if the uploaded excel student info
                            * header and template are same or not*/
                         
                         for ( int i = 0; i < headerListFromTemplate.size(); i++ ) {
                           
                            if( !headerListFromTemplate.get(i).equals(headerList.get(i)) ) {
                            
                                return false;
                            
                            }
                         }
                         
                          /* check if the uploaded excel student demographic info
                            * header and template are same or not*/
                         
                         StudentFileRow studentFileRow = (StudentFileRow)objects;
                         StudentDemoGraphics[] studentDemoGraphics = 
                                            studentFileRow.getStudentDemoGraphics();
                         
                        //put the demographic details into list to maintain order
                        for( int i = 0; i < studentDemoGraphics.length; i++ ) {
                            
                            demographicsHeaderListTemplate.add(
                                            studentDemoGraphics[i].getLabelName()); 
                            
                         }
                         //postion should be after student personal details to end       
                        for ( int i = startPosition + headerListFromTemplate.size() ;
                                                            i < totalColumns; i++ ) {
                         
                            
                            if(headerList.get(i).equals(
                                            demographicsHeaderListTemplate.get(count))) {
                                
                                return false;
                                    
                            }
                            count++;
                        }
                           
                    } // end of student header validation  
                }
            }
            
       } catch (Exception e ) {
             e.printStackTrace();
       }
       
       return true;                                 
        
    }
    
    
   /*
    * get the exact file name that user has uploaded
    */ 
     
    private static String getExactFileName(String fileName) {
		
		int position = fileName.lastIndexOf("_");
		fileName = fileName.substring(0,position);
		position = fileName.lastIndexOf("_");
		fileName = fileName.substring(0,position);
		position = fileName.lastIndexOf("_");
		fileName = fileName.substring(0,position);
		position = fileName.indexOf("/");
		fileName = fileName.substring(position + 1,fileName.length());
		fileName = fileName +  ".xls";
		return fileName;
		
	}
    
    /**
     * This method determines the permission flag. 
     * Permission flag contains 3 characters. Each character 
     * can either be 'F' or 'T' for False and   True. The 
     * 3 characters signify persmissions for 'Delete', 'Export Error file', 
     *  and 'Refresh List' respectively.
     * @param loginUserName
     * @param createdBy
     * @param failedRecordCount
     * @param status
     * @return String
     * @throws CTBBusinessException
     */
    private String getPermisssion(String loginUserName,
                                 Integer createdBy,   
                                 Integer failedRecordCount,
                                 String status) 
                                 throws CTBBusinessException {
        
        String permToken = "FFT";
       
         try {
			// Get the user
            User user = users.getUserDetails(loginUserName); 
           if ( user.getUserId().intValue() == createdBy.intValue()) {
          
                if ( status.equals(CTBConstants.ACTIVATION_STATUS_IN_PROGRESS) ) {
                    
                    permToken = "FFT";
                    
                    if ( failedRecordCount != null
                            && failedRecordCount.intValue() > 0 ) {
                    
                        permToken = "TTT";
                        
                    }  
                
                } else {
                    
                    permToken = "TFT"; 
                    
                    if ( failedRecordCount != null
                            && failedRecordCount.intValue() > 0 ) {
                    
                        permToken = "TTT";
                        
                    }   
                    
                }
                  
               
            } else {
                
                if ( failedRecordCount != null
                    && failedRecordCount.intValue() > 0 ) {
                    
                    permToken = "FTT";
                        
                }
                  
            }
                   
                  
        } catch(SQLException se){
            FileNotUploadedException dataNotfoundException = 
                                        new FileNotUploadedException
                                                ("UploadDownloadManagement.Failed");
            dataNotfoundException.setStackTrace(se.getStackTrace());                                    
            throw dataNotfoundException;                                                
        } catch (Exception e) {
            FileNotUploadedException dataNotFoundException = 
                                        new FileNotUploadedException
                                                ("UploadDownloadManagement.Failed");
            dataNotFoundException.setStackTrace(e.getStackTrace());
            throw dataNotFoundException;
        }
                  
         return permToken;                                    
    
    }
    
    /*
     * This method is used to build the sort param and type
     * @param String primarySortName
     * @param String secondarySortName
     * @return SortParams
    */
    private SortParams buildFileSortParams(String primarySortName, 
                                           String secondarySortName) {
        
        SortParam[] sortParams = null;
        SortParams sort = new SortParams();
         
       
        if (secondarySortName != null) {
        
            sortParams = new SortParam[2];
            sortParams[1] = new SortParam(secondarySortName, SortType.ALPHAASC);
        
        } else {
            
            sortParams = new SortParam[1];
        
        }
        sortParams[0] = new SortParam(primarySortName, SortType.ALPHAASC);
        sort.setSortParams(sortParams);
        return sort;
    }
    
    /*
     * isOrgPresent
     * check if org present in excel hierchy
    */
    private Node[] getOrgPathForSort(OrgNodeCategory []orgNodeCategory, Node []orgNodes ) {
        
       Node [] newOrgArray = new Node[orgNodeCategory.length];
	
       for ( int count=0; count < newOrgArray.length ; count++) {
            
            Node tempNode = new Node();
            tempNode.setOrgNodeName("$");
            newOrgArray[count] = tempNode;
       
       }
	
       for( int i = 0, j = 0 ; i < orgNodeCategory.length && j < orgNodes.length ;) {
		
           if(orgNodeCategory[i].getOrgNodeCategoryId().intValue()
                    == orgNodes[j].getOrgNodeCategoryId().intValue()){
		   
               Node temp = new Node();
               temp.setOrgNodeName(orgNodes[j].getOrgNodeName());
               newOrgArray[i] = temp ; 
               i++;
               j++;
		   
           } else {
               
              
			   i++;
			
           }
		
       }
       
       return newOrgArray;
        
    }
    
    //Changes for GA2011CR001
    private void isStudentIDConfigurableCustomer(String userName) 
    {     
    	try{
			Customer customer = users.getCustomer(userName);
			this.loginUserName = userName;
			this.customerId = customer.getCustomerId();
			getCustomerConfigurations(this.loginUserName, this.customerId);
			for (int i=0; i < this.customerConfigurations.length; i++)
			{
				CustomerConfiguration cc = (CustomerConfiguration)this.customerConfigurations[i];
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Student_ID_2") && cc.getDefaultValue().equalsIgnoreCase("T"))
				{
					this.isStudentId2Configurable = true; 
					configId = cc.getId();
					//System.out.println("configId"+ configId);
					customerConfigurationValues(configId);
					this.valueForStudentId2 = new String[8];

					for(int j=0; j<this.customerConfigurationsValue.length; j++){

						int sortOrder = this.customerConfigurationsValue[j].getSortOrder();
						this.valueForStudentId2[sortOrder-1] = this.customerConfigurationsValue[j].getCustomerConfigurationValue();
					}
					
					this.valueForStudentId2 = getDefaultValue(valueForStudentId2, CTBConstants.STUDENT_ID2);
					
				}
				if (cc.getCustomerConfigurationName().equalsIgnoreCase("Configurable_Student_ID") && cc.getDefaultValue().equalsIgnoreCase("T"))
				{
					this.isStudentIdConfigurable = true; 
					configId = cc.getId();
					customerConfigurationValues(configId);
					//By default there should be 3 entries for customer configurations
					this.valueForStudentId = new String[8];
					for(int j=0; j<this.customerConfigurationsValue.length; j++){
						int sortOrder = this.customerConfigurationsValue[j].getSortOrder();
						this.valueForStudentId[sortOrder-1] = this.customerConfigurationsValue[j].getCustomerConfigurationValue();
					}	
					
					this.valueForStudentId = getDefaultValue(valueForStudentId, CTBConstants.STUDENT_ID);
					
				}


			}

		} catch(SQLException e){

			FileNotUploadedException dataNotfoundException = 
				new FileNotUploadedException
				("UploadDownloadManagement.Failed");
			dataNotfoundException.setStackTrace(e.getStackTrace());                                    

		} 
	
	
	
     }
   //Changes for GA2011CR001 
    private String[] getDefaultValue(String [] arrValue, String labelName)
	{
		arrValue[0] = arrValue[0] != null ? arrValue[0]   : labelName ;
		arrValue[1] = arrValue[1] != null ? arrValue[1]   : "32" ;
		
		
		if(labelName.equals("Student ID")){
			arrValue[2] = arrValue[2] != null ? arrValue[2]   : "F" ;
			if(!arrValue[2].equals("T") && !arrValue[2].equals("F"))
				{ 
					arrValue[2]  = "F";
				}
			//START- GACR005 
			arrValue[3] = (arrValue[3] != null && new Integer(arrValue[3]).intValue() > 0)? arrValue[3]   : "0" ;
			try {
				int minLength = Integer.valueOf(arrValue[3]);
			} catch (NumberFormatException nfe){
				arrValue[3] = "0" ;
			}
			this.studentIdMinLength = arrValue[3];
			arrValue[4] = arrValue[4] != null ? arrValue[4]   : "AN" ;
			if(!arrValue[4].equals("NU") && !arrValue[4].equals("AN"))
				{ 
					arrValue[4]  = "AN";
				}
			this.isStudentIdNumeric = arrValue[4];
			//END- GACR005 
			System.out.println("Student ID" + arrValue[0] +"...."+arrValue[1] +"..." +arrValue[2] +"..." +arrValue[3] +"..."+arrValue[4] );
			
		}
		
		if(labelName.equals("Student ID2")){
			//START- GACR005 
			arrValue[2] = (arrValue[2] != null && new Integer(arrValue[2]).intValue() > 0)? arrValue[2]   : "0" ;
			try {
				int minLength = Integer.valueOf(arrValue[2]);
			} catch (NumberFormatException nfe){
				arrValue[2] = "0" ;
			}
			this.studentId2MinLength = arrValue[2];
			arrValue[3] = arrValue[3] != null ? arrValue[3]   : "AN" ;
			if(!arrValue[3].equals("NU") && !arrValue[3].equals("AN"))
				{ 
					arrValue[3]  = "AN";
				}
			this.isStudentId2Numeric = arrValue[3];
			//END- GACR005 
			System.out.println("Student ID 2" + arrValue[0] +"...."+arrValue[1] +"..." +arrValue[2] +"..." +arrValue[3]  );
			
		}
		
		// check for numeric conversion of maxlength

		try {
			int maxLength = Integer.valueOf(arrValue[1]);
		} catch (NumberFormatException nfe){
			arrValue[1] = "32" ;
		}
		
		
		
		return arrValue;
	}
    
    
    
    
    //Changes for GA2011CR001
	  private ArrayList getStudentIDHeaderOrderList() {
	        
	        ArrayList headerArray = new ArrayList();
	        
	        headerArray.add(CTBConstants.REQUIREDFIELD_FIRST_NAME);
	        headerArray.add(CTBConstants.MIDDLE_NAME);
	        headerArray.add(CTBConstants.REQUIREDFIELD_LAST_NAME);
	        headerArray.add(CTBConstants.REQUIREDFIELD_DATE_OF_BIRTH);
	        headerArray.add(CTBConstants.REQUIREDFIELD_GRADE);
	        headerArray.add(CTBConstants.REQUIREDFIELD_GENDER);
	        
	       if(isStudentIdConfigurable){
	    	   headerArray.add(valueForStudentId[0]);
	    	   
	       }
	       else{
	    	   headerArray.add(CTBConstants.STUDENT_ID);
	       }
	        
	       
	       if(isStudentId2Configurable){
	    	   headerArray.add(valueForStudentId2[0]);
	    	   
	       }
	       else{
	    	   headerArray.add(CTBConstants.STUDENT_ID2);
	       }
	     
	        
	        headerArray.add(CTBConstants.SCREEN_READER);
	        headerArray.add(CTBConstants.CALCULATOR);
	        headerArray.add(CTBConstants.TEST_PAUSE);
	        headerArray.add(CTBConstants.UNTIMED_TEST);
	        headerArray.add(CTBConstants.HIGHLIGHTER);
	        headerArray.add(CTBConstants.QUESTION_BACKGROUND_COLOR);
	        headerArray.add(CTBConstants.QUESTION_FONT_COLOR);
	        headerArray.add(CTBConstants.ANSWER_BACKGROUND_COLOR);
	        headerArray.add(CTBConstants.ANSWER_FONT_COLOR);
	        headerArray.add(CTBConstants.FONT_SIZE);
	       
	        return headerArray;
	    }
	/***
	 *  //Changes for GA2011CR001
	 * @param configId
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
	/**
	 * getCustomerConfigurations  //Changes for GA2011CR001
	 */
	private void getCustomerConfigurations(String userName, Integer customerId)
	{
		try {
			//if (this.customerConfigurations == null) {   //Changes for Defect-60479
				this.customerConfigurations = this.studentManagement.getCustomerConfigurations(userName, customerId);
			//}
		}
		catch (CTBBusinessException be) {
			be.printStackTrace();
		}
	}




	/**
	 * @return the studentIdMinLength
	 */
	public String getStudentIdMinLength() {
		return studentIdMinLength;
	}




	/**
	 * @param studentIdMinLength the studentIdMinLength to set
	 */
	public void setStudentIdMinLength(String studentIdMinLength) {
		this.studentIdMinLength = studentIdMinLength;
	}




	/**
	 * @return the studentId2MinLength
	 */
	public String getStudentId2MinLength() {
		return studentId2MinLength;
	}




	/**
	 * @param studentId2MinLength the studentId2MinLength to set
	 */
	public void setStudentId2MinLength(String studentId2MinLength) {
		this.studentId2MinLength = studentId2MinLength;
	}




	/**
	 * @return the isStudentIdNumeric
	 */
	public String getIsStudentIdNumeric() {
		return isStudentIdNumeric;
	}




	/**
	 * @param isStudentIdNumeric the isStudentIdNumeric to set
	 */
	public void setIsStudentIdNumeric(String isStudentIdNumeric) {
		this.isStudentIdNumeric = isStudentIdNumeric;
	}




	/**
	 * @return the isStudentId2Numeric
	 */
	public String getIsStudentId2Numeric() {
		return isStudentId2Numeric;
	}




	/**
	 * @param isStudentId2Numeric the isStudentId2Numeric to set
	 */
	public void setIsStudentId2Numeric(String isStudentId2Numeric) {
		this.isStudentId2Numeric = isStudentId2Numeric;
	}
	
 
   
    
} 
