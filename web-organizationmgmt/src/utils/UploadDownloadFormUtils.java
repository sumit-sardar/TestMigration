package utils; 

import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.StudentDemoGraphics;
import com.ctb.bean.testAdmin.StudentDemoGraphicsData;
import com.ctb.bean.testAdmin.StudentFile;
import com.ctb.bean.testAdmin.StudentFileRow;
import com.ctb.bean.testAdmin.UserFile;
import com.ctb.bean.testAdmin.UserFileRow;
import dto.AuditFileHistory;
import dto.UploadFileInformation;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import com.ctb.bean.testAdmin.CustomerConfig;
import com.ctb.control.userManagement.UserManagement;
import com.ctb.util.CTBConstants;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;



public class UploadDownloadFormUtils 
{ 
	// Creating a demographic map of Ethnicity: Hispanic or Latino demograhic
	public static Map<String,String> demoEthnicityMap = new HashMap<String, String>();  
	static {
		demoEthnicityMap.put("centroamericano","centroamericano");
		demoEthnicityMap.put("cubano","cubano");
		demoEthnicityMap.put("cubano-americano","cubano-americano");
		demoEthnicityMap.put("dominicano","dominicano");
		demoEthnicityMap.put("mexicano","mexicano");
		demoEthnicityMap.put("mexicano-americano","mexicano-americano");
		demoEthnicityMap.put("otro","otro");
		demoEthnicityMap.put("puertorriqueño","puertorriqueño");
		demoEthnicityMap.put("sudamericano","sudamericano");
	}
	
	
    public static String createFileContent(String excelFile){
        
        InputStream inputStream = null;
		POIFSFileSystem pfs = null;
		HSSFSheet sheet = null;
        StringBuffer buff = new StringBuffer();
		try {
			
            inputStream = new FileInputStream(new File(excelFile));
        
        } catch (FileNotFoundException e) {
		
        	e.printStackTrace();
		
        }

		try {
		
        	pfs = new POIFSFileSystem( inputStream );
			HSSFWorkbook wb = new HSSFWorkbook(pfs);
	        sheet = wb.getSheetAt(0);
		
        } catch (IOException e) {
		
        	e.printStackTrace();
		
        }
		
        Iterator rows = sheet.rowIterator(); 
        
        while( rows.hasNext() ) {           
        
            HSSFRow row = (HSSFRow) rows.next();
            Iterator cells = row.cellIterator();
        
             while( cells.hasNext() ) {
        
                 HSSFCell cell = (HSSFCell) cells.next();
                 String cellValue = cell.toString();
                 buff.append(cellValue);
                 buff.append("\t");
             
             }
             
             buff.append("\n");
         }
      
      return buff.toString();  
        
    }
    /*
    * Create Template in Server path and return byte array 
    */ 
    //For MQC 67720: MDR columns needs to be removed for nonLaslinks
    public static byte[] createTemplateFile (UserFile userFile, String userName, 
            UserManagement userManagement, boolean islaslinkCustomer) {
        byte []errorData = null;
        try {
            
            UserFileRow []userFileRow = userFile.getUserFileRows();
            
            HSSFWorkbook rwb = new HSSFWorkbook ();
            HSSFSheet sheet = rwb.createSheet("FirstSheet");
            Node []headerNode = null;
            HSSFCellStyle style = null;
            HSSFRow row = null;
            HSSFCell cell = null;
            int userPos = 0;
            // START: For MQC 67720: MDR columns needs to be removed for nonLaslinks
            int headerPosFactor = 2;
        	if(islaslinkCustomer){
        		headerPosFactor = 3;
        	}
        	// END: For MQC 67720: MDR columns needs to be removed for nonLaslinks
            
            for ( int i = 0; i < userFileRow.length; i++ ) {
                
                row = sheet.createRow(i);
                
                if (i == 0) {
                   
                    headerNode = userFileRow[i].getOrganizationNodes();
                    //Insert organization header
                    // For MQC 67720: MDR columns needs to be removed for nonLaslinks
                    for (int k = 0,j = 0; k < headerNode.length; k++,j=j+headerPosFactor) { 	// For MQC 66840 : Upload/Download user/student with MDR
                        
                        cell = row.createCell((short)j);
                        style = cell.getCellStyle();
                        style.setWrapText(true);
                        cell.setCellStyle(style);
                        cell.setCellValue(headerNode[k].getOrgNodeCategoryName());
                        
                        cell = row.createCell((short)(j + 1));
                        style = cell.getCellStyle();
                        style.setWrapText(true);
                        cell.setCellStyle(style);
                        cell.setCellValue(headerNode[k].getOrgNodeCode());
                        
                        //Start For MQC 66840 : Upload/Download user/student with MDR
                        // START: For MQC 67720: MDR columns needs to be removed for nonLaslinks
                        if(islaslinkCustomer) {
                        	cell = row.createCell((short)(j + 2));
                            style = cell.getCellStyle();
                            style.setWrapText(true);
                            cell.setCellStyle(style);
                            cell.setCellValue(headerNode[k].getMdrNumber());
                        }
                        // END: For MQC 67720: MDR columns needs to be removed for nonLaslinks
                        // End For MQC 66840 : Upload/Download user/student with MDR
               
                    }
                    
                    //Insert User Header
                    // For MQC 67720: MDR columns needs to be removed for nonLaslinks
                    userPos = headerNode.length * headerPosFactor; 	// For MQC 66840 : Upload/Download user/student with MDR
                    
                    //FirstName Header
                    cell = row.createCell((short)userPos);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    
                    style.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
                    sheet.setDefaultColumnStyle((short)userPos++,style);  
                                      
                    cell.setCellValue (userFileRow[i].getFirstName());
                    
                    //MiddleName Header
                    
                    cell = row.createCell((short)userPos++);
                    cell.setCellStyle(style);
                    cell.setCellValue (userFileRow[i].getMiddleName());
                    
                    //LastName Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (userFileRow[i].getLastName());
                    
                    //Email Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (userFileRow[i].getEmail());
                    
                    //TimeZone Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (userFileRow[i].getTimeZone());
                    
                    //Role Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (userFileRow[i].getRoleName());
                    
                    //ExtPin1 Header CR
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (userFileRow[i].getExtPin1());
                    
                    // Address1 Header
                    
                     cell = row.createCell((short)userPos++);
                     style = cell.getCellStyle();
                     style.setWrapText(true);
                     cell.setCellStyle(style);
                     cell.setCellValue (userFileRow[i].getAddress1());
                     
                     // Address2 Header
                     
                     cell = row.createCell((short)userPos++);
                     style = cell.getCellStyle();
                     style.setWrapText(true);
                     cell.setCellStyle(style);
                     cell.setCellValue (userFileRow[i].getAddress2());
                     
                     //City Header
                     
                     cell = row.createCell((short)userPos++);
                     style = cell.getCellStyle();
                     style.setWrapText(true);
                     cell.setCellStyle(style);
                     cell.setCellValue (userFileRow[i].getCity());
                     
                     //State Header
                     
                     cell = row.createCell((short)userPos++);
                     style = cell.getCellStyle();
                     style.setWrapText(true);
                     cell.setCellStyle(style);
                     cell.setCellValue (userFileRow[i].getState());
                     
                     //Zip Header
                     
                     cell = row.createCell((short)userPos++);
                     style = cell.getCellStyle();
                     style.setWrapText(true);
                     cell.setCellStyle(style);
                     cell.setCellValue (userFileRow[i].getZip());
                     
                    //Primary Phone Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (userFileRow[i].getPrimaryPhone());
                    
                    //Secondary Phone Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (userFileRow[i].getSecondaryPhone());
                    
                    
                    //Fax Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (userFileRow[i].getFaxNumber());
                    
                    
                } else {
                   
                    Node []nodeData = userFileRow[i].getOrganizationNodes();
                    int cellPosition = 0;
                    for (int j = 0; j < nodeData.length; j++) {
                        //For MQC 67720: MDR columns needs to be removed for nonLaslinks
                        cellPosition = getCellPosition (headerNode, nodeData[j], islaslinkCustomer);
                        
                        if (cellPosition != -1) {
                            
                            //Create cell for nodeName
                            cell = row.createCell((short)cellPosition);
                            style = cell.getCellStyle();
                            style.setWrapText(true);
                            cell.setCellStyle(style);
                            cell.setCellValue(nodeData[j].getOrgNodeName());
                            
                            //create cell for nodeCode
                            cell = row.createCell((short)(cellPosition + 1));
                            style = cell.getCellStyle();
                            style.setWrapText(true);
                            cell.setCellStyle(style);
                            cell.setCellValue(nodeData[j].getOrgNodeCode());
                            
                           // For MQC 66840 : Upload/Download user/student with MDR
                           // START: For MQC 67720: MDR columns needs to be removed for nonLaslinks
                            if(islaslinkCustomer) {
                            	cell = row.createCell((short)(cellPosition + 2));
                                style = cell.getCellStyle();
                                style.setWrapText(true);
                                cell.setCellStyle(style);
                                cell.setCellValue(nodeData[j].getMdrNumber());
                            }
                            // END: For MQC 67720: MDR columns needs to be removed for nonLaslinks
                            // For MQC 66840 : Upload/Download user/student with MDR
                        }
                        
                        
                    }
                    
                }
                
            }
         String fileName = PathFinderUtils.getSaveFileName(userName,
                                           "User_Template.xls", userManagement); 
                                           
         fileName = CTBConstants.SERVER_FOLDER_NAME+"/"+fileName;
         FileOutputStream mfileOut = new FileOutputStream(fileName);
		 rwb.write(mfileOut);
         
         ByteArrayOutputStream baos = new ByteArrayOutputStream();             
        
        InputStream in = new FileInputStream(fileName);

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
         
		 mfileOut.close();   
         baos.flush();
         baos.close();   
         
            
        } catch (Exception e) {
            
            e.printStackTrace();
            
        }
        
       return errorData; 
        
    } 
    
    
    
    /*
    * retrive actual cell position by passing header node array and organization detail
    */ 
    //For MQC 67720: MDR columns needs to be removed for nonLaslinks
    private static int getCellPosition (Node []headerNode, Node node, boolean islaslinkCustomer) {
        
        int cellPosition = 0;
        boolean flag = false;
        for (int i = 0; i < headerNode.length; i++) {
            
            Node tempHeaderNode = headerNode[i];
            
            if (node.getOrgNodeName() != null && !node.getOrgNodeName().trim().equals("")) {
                
                if (tempHeaderNode.getOrgNodeCategoryId().intValue() 
                    == node.getOrgNodeCategoryId().intValue()) {
                    flag = true;    
                    break;
                
                }
                
            }
            
           // START: For MQC 67720: MDR columns needs to be removed for nonLaslinks 
            if(islaslinkCustomer){
            	 cellPosition = cellPosition + 3; 	// For MQC 66840 : Upload/Download user/student with MDR
            } else {
            	 cellPosition = cellPosition + 2; 	// For MQC 66840 : Upload/Download user/student with MDR
            }
           // END: For MQC 67720: MDR columns needs to be removed for nonLaslinks
            
        }
        if (flag) {
            
            return cellPosition;
        
        } else {
            
            return -1;
            
        }
        
        
    }
    
    public static String createTemplate(UserFile userFile) {
        
        UserFileRow []userFileRow = userFile.getUserFileRows();
        StringBuffer buff = new StringBuffer();
        
        for ( int i = 0; i < userFileRow.length; i++ ) {
            
            //Header Process
            if ( i == 0 ) {
                
                processHeaderTemplateOrg (userFileRow[i].getOrganizationNodes(),buff);
                
                buff.append(userFileRow[i].getFirstName());
                buff.append("\t");
                buff.append(userFileRow[i].getMiddleName());
                buff.append("\t");
                buff.append(userFileRow[i].getLastName());
                buff.append("\t");
                buff.append(userFileRow[i].getEmail());
                buff.append("\t");
                buff.append(userFileRow[i].getTimeZone());
                buff.append("\t");
                buff.append(userFileRow[i].getRole());
                buff.append("\t");
                buff.append(userFileRow[i].getAddress1());
                buff.append("\t");
                buff.append(userFileRow[i].getAddress2());
                buff.append("\t");
                buff.append(userFileRow[i].getCity());
                buff.append("\t");
                buff.append(userFileRow[i].getState());
                buff.append("\t");
                buff.append(userFileRow[i].getZip());
                buff.append("\t");
                buff.append(userFileRow[i].getPrimaryPhone());
                buff.append("\t");
                buff.append(userFileRow[i].getSecondaryPhone());
                buff.append("\t");
                buff.append(userFileRow[i].getFaxNumber());
                buff.append("\t");
                
            } else {
                
                processBodyTemplateOrg (userFileRow[i].getOrganizationNodes(), 
                        userFileRow[0].getOrganizationNodes(),buff);
                
            }
            
            buff.append("\n");
            
        }
        
        return buff.toString();  
        
    }
    // For MQC 67720: MDR columns needs to be removed for nonLaslinks
    public static byte[] downLoadUserDataFile (UserFile userFile, String userName, 
            UserManagement userManagement, boolean islaslinkCustomer) {
        
        
        byte []errorData = null;
        // START: For MQC 67720: MDR columns needs to be removed for nonLaslinks
        int headerPosFactor = 2;
        if(islaslinkCustomer){
        	headerPosFactor = 3;
        }
        // END: For MQC 67720: MDR columns needs to be removed for nonLaslinks
        try {
            
            UserFileRow []userFileRow = userFile.getUserFileRows();
            
            HSSFWorkbook rwb = new HSSFWorkbook ();
            HSSFSheet sheet = rwb.createSheet("FirstSheet");
            Node []headerNode = null;
            HSSFCellStyle style = null;
            HSSFRow row = null;
            HSSFCell cell = null;
            int userPos = 0;
            
            for ( int i = 0; i < userFileRow.length; i++ ) {
                
                row = sheet.createRow(i);
                
                if (i == 0) {
                   
                    headerNode = userFileRow[i].getOrganizationNodes();
                    //Insert organization header
                    // For MQC 66840 : Upload/Download user/student with MDR
                    // For MQC 67720: MDR columns needs to be removed for nonLaslinks
                    for (int k = 0,j = 0; k < headerNode.length; k++,j=j+headerPosFactor) { 
                        
                        cell = row.createCell((short)j);
                        style = cell.getCellStyle();
                        style.setWrapText(true);
                        cell.setCellStyle(style);
                        cell.setCellValue(headerNode[k].getOrgNodeCategoryName());
                        
                        cell = row.createCell((short)(j + 1));
                        style = cell.getCellStyle();
                        style.setWrapText(true);
                        cell.setCellStyle(style);
                        cell.setCellValue(headerNode[k].getOrgNodeCode());
                        
                        // Start For MQC 66840 : Upload/Download user/student with MDR
                        // START: For MQC 67720: MDR columns needs to be removed for nonLaslinks
                        if(islaslinkCustomer){
                            cell = row.createCell((short)(j + 2));
                            style = cell.getCellStyle();
                            style.setWrapText(true);
                            cell.setCellStyle(style);
                            cell.setCellValue(headerNode[k].getMdrNumber());
                        }
                        // END: For MQC 67720: MDR columns needs to be removed for nonLaslinks
                        // End For MQC 66840 : Upload/Download user/student with MDR
               
                    }
                    
                    //Insert User Header
                    // For MQC 67720: MDR columns needs to be removed for nonLaslinks
                    userPos = headerNode.length * headerPosFactor; 	//Start For MQC 66840 : Upload/Download user/student with MDR
                    
                    //FirstName Header
                    cell = row.createCell((short)userPos);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    
                    style.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
                    sheet.setDefaultColumnStyle((short)userPos++,style);                      
                    cell.setCellValue (userFileRow[i].getFirstName());
                    
                    //MiddleName Header
                    
                    cell = row.createCell((short)userPos++);
                    cell.setCellStyle(style);
                    cell.setCellValue (userFileRow[i].getMiddleName());
                    
                    //LastName Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (userFileRow[i].getLastName());
                    
                    //Email Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (userFileRow[i].getEmail());
                    
                    //TimeZone Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (userFileRow[i].getTimeZone());
                    
                    //Role Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (userFileRow[i].getRoleName());
                    
                    //CR External Id
                                                     
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (userFileRow[i].getExtPin1());
                    
                    // Address1 Header
                    
                     cell = row.createCell((short)userPos++);
                     style = cell.getCellStyle();
                     style.setWrapText(true);
                     cell.setCellStyle(style);
                     cell.setCellValue (userFileRow[i].getAddress1());
                     
                     // Address2 Header
                     
                     cell = row.createCell((short)userPos++);
                     style = cell.getCellStyle();
                     style.setWrapText(true);
                     cell.setCellStyle(style);
                     cell.setCellValue (userFileRow[i].getAddress2());
                     
                     //City Header
                     
                     cell = row.createCell((short)userPos++);
                     style = cell.getCellStyle();
                     style.setWrapText(true);
                     cell.setCellStyle(style);
                     cell.setCellValue (userFileRow[i].getCity());
                     
                     //State Header
                     
                     cell = row.createCell((short)userPos++);
                     style = cell.getCellStyle();
                     style.setWrapText(true);
                     cell.setCellStyle(style);
                     cell.setCellValue (userFileRow[i].getState());
                     
                     //Zip Header
                     
                     cell = row.createCell((short)userPos++);
                     style = cell.getCellStyle();
                     style.setWrapText(true);
                     cell.setCellStyle(style);
                     cell.setCellValue (userFileRow[i].getZip());
                     
                    //Primary Phone Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (userFileRow[i].getPrimaryPhone());
                    
                    //Secondary Phone Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (userFileRow[i].getSecondaryPhone());
                    
                    
                    //Fax Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (userFileRow[i].getFaxNumber());
                    
                    
                } else {
                   
                    Node []nodeData = userFileRow[i].getOrganizationNodes();
                    int cellPosition = 0;
                    for (int j = 0; j < nodeData.length; j++) {
                        // For MQC 67720: MDR columns needs to be removed for nonLaslinks
                        cellPosition = getCellPosition (headerNode, nodeData[j], islaslinkCustomer);
                        
                        if (cellPosition != -1) {
                            
                            //Create cell for nodeName
                            cell = row.createCell((short)cellPosition);
                            style = cell.getCellStyle();
                            style.setWrapText(true);
                            cell.setCellStyle(style);
                            cell.setCellValue(nodeData[j].getOrgNodeName());
                            
                            //create cell for nodeCode
                            cell = row.createCell((short)(cellPosition + 1));
                            style = cell.getCellStyle();
                            style.setWrapText(true);
                            cell.setCellStyle(style);
                            cell.setCellValue(nodeData[j].getOrgNodeCode());
                            
                          	// Start For MQC 66840 : Upload/Download user/student with MDR
                          	// START: For MQC 67720: MDR columns needs to be removed for nonLaslinks
                            if(islaslinkCustomer){
                                cell = row.createCell((short)(cellPosition + 2));
                                style = cell.getCellStyle();
                                style.setWrapText(true);
                                cell.setCellStyle(style);
                                cell.setCellValue(nodeData[j].getMdrNumber());
                            }
                            // END: For MQC 67720: MDR columns needs to be removed for nonLaslinks
                            // End For MQC 66840 : Upload/Download user/student with MDR
                        }
                   
                    } //End for loop
                    // For MQC 67720: MDR columns needs to be removed for nonLaslinks
                    cellPosition = headerNode.length * headerPosFactor;    	// Start For MQC 66840 : Upload/Download user/student with MDR
                    
                    //FirstName
                 
                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if (userFileRow[i].getFirstName() != null) {
                        
                        cell.setCellValue(userFileRow[i].getFirstName());
                        
                    } else {
                        
                        cell.setCellValue("");
                        
                    }
                    
                    // MiddleName
                    
                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if (userFileRow[i].getMiddleName() != null) {
                        
                        cell.setCellValue(userFileRow[i].getMiddleName());
                        
                    } else {
                        
                        cell.setCellValue("");
                        
                    }
                    
                    // LastName
                    
                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if (userFileRow[i].getLastName() != null) {
                        
                        cell.setCellValue(userFileRow[i].getLastName());
                        
                    } else {
                        
                        cell.setCellValue("");
                        
                    }
                    
                    // Email
                    
                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if (userFileRow[i].getEmail() != null) {
                        
                        cell.setCellValue(userFileRow[i].getEmail());
                        
                    } else {
                        
                        cell.setCellValue("");
                        
                    }
                    
                    //TimeZone
                    
                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if (userFileRow[i].getTimeZoneDese() != null) {
                        
                        cell.setCellValue(ColorUtil.initCap(userFileRow[i].getTimeZoneDese()));
                        
                    } else {
                        
                        cell.setCellValue("");
                        
                    }
                    
                    // Role
                    
                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if (userFileRow[i].getRoleName() != null) {
                        
                        cell.setCellValue(ColorUtil.initCap(ColorUtil.initCap(userFileRow[i].getRoleName())));
                        
                    } else {
                        
                        cell.setCellValue("");
                        
                    }
                    
                     // CR External Id
                    
                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if (userFileRow[i].getExtPin1() != null) {
                        
                        cell.setCellValue(userFileRow[i].getExtPin1());
                        
                    } else {
                        
                        cell.setCellValue("");
                        
                    }
                    
                    //Address Line1
                    
                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if (userFileRow[i].getAddress1() != null) {
                        
                        cell.setCellValue(userFileRow[i].getAddress1());
                        
                    } else {
                        
                        cell.setCellValue("");
                        
                    }
                    
                    //Address Line2
                    
                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if (userFileRow[i].getAddress2() != null) {
                        
                        cell.setCellValue(userFileRow[i].getAddress2());
                        
                    } else {
                        
                        cell.setCellValue("");
                        
                    }
                    
                    //City
                    
                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if (userFileRow[i].getCity() != null) {
                        
                        cell.setCellValue(userFileRow[i].getCity());
                        
                    } else {
                        
                        cell.setCellValue("");
                        
                    }
                    
                    // State
                    
                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if (userFileRow[i].getState() != null) {
                        
                        cell.setCellValue(userFileRow[i].getState());
                        
                    } else {
                        
                        cell.setCellValue("");
                        
                    }
                    
                    //ZIP
                    
                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if (userFileRow[i].getZip() != null) {
                        
                        cell.setCellValue(userFileRow[i].getZip());
                        
                    } else {
                        
                        cell.setCellValue("");
                        
                    }
                    
                    // Primary Phone
                    
                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if (userFileRow[i].getPrimaryPhone() != null) {
                        
                        cell.setCellValue(userFileRow[i].getPrimaryPhone());
                        
                    } else {
                        
                        cell.setCellValue("");
                        
                    }
                    
                    //Secondary Phone
                    
                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if (userFileRow[i].getSecondaryPhone() != null) {
                        
                        cell.setCellValue(userFileRow[i].getSecondaryPhone());
                        
                    } else {
                        
                        cell.setCellValue("");
                        
                    }
                    
                    //Fax
                    
                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if (userFileRow[i].getFaxNumber() != null) {
                        
                        cell.setCellValue(userFileRow[i].getFaxNumber());
                        
                    } else {
                        
                        cell.setCellValue("");
                        
                    }
                    
                }
                
            }
         String fileName = PathFinderUtils.getSaveFileName(userName,
                                           "User_Data_Download.xls", userManagement); 
                                           
         fileName = CTBConstants.SERVER_FOLDER_NAME+"/"+fileName;
         FileOutputStream mfileOut = new FileOutputStream(fileName);
		 rwb.write(mfileOut);
         
         ByteArrayOutputStream baos = new ByteArrayOutputStream();             
        
        InputStream in = new FileInputStream(fileName);

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
         
		 mfileOut.close();   
         baos.flush();
         baos.close();   
         
            
        } catch (Exception e) {
            
            e.printStackTrace();
            
        }
        
       return errorData; 
        
        
    }
    
    public static String downLoadUserData (UserFile userFile) {
        
        UserFileRow []userFileRow = userFile.getUserFileRows();
        StringBuffer buff = new StringBuffer();
       
        for ( int i = 0; i < userFileRow.length; i++ ) {
             //Header Process
            if ( i == 0 ) {
                
                processHeaderTemplateOrg (userFileRow[i].getOrganizationNodes(),buff);
                
                buff.append(userFileRow[i].getFirstName());
                buff.append("\t");
                buff.append(userFileRow[i].getMiddleName());
                buff.append("\t");
                buff.append(userFileRow[i].getLastName());
                buff.append("\t");
                buff.append(userFileRow[i].getEmail());
                buff.append("\t");
                buff.append(userFileRow[i].getTimeZone());
                buff.append("\t");
                buff.append(userFileRow[i].getRoleName());
                buff.append("\t");
                buff.append(userFileRow[i].getAddress1());
                buff.append("\t");
                buff.append(userFileRow[i].getAddress2());
                buff.append("\t");
                buff.append(userFileRow[i].getCity());
                buff.append("\t");
                buff.append(userFileRow[i].getState());
                buff.append("\t");
                buff.append(userFileRow[i].getZip());
                buff.append("\t");
                buff.append(userFileRow[i].getPrimaryPhone());
                buff.append("\t");
                buff.append(userFileRow[i].getSecondaryPhone());
                buff.append("\t");
                buff.append(userFileRow[i].getFaxNumber());
                buff.append("\t");
                
            } else {
                
                int OrgHeaderSize =  userFileRow[0].getOrganizationNodes().length;
                processDownLoadUserNodeData (userFileRow[i] ,userFileRow[0] ,OrgHeaderSize,buff);
                
            }
            
             buff.append("\n");
             
        }
        
         return buff.toString();  
        
    }
    
    
    public static String downLoadStudentData (StudentFile studentFile) {
        
        StudentFileRow []studentFileRow = studentFile.getStudentFileRows();
        
        StringBuffer buff = new StringBuffer();
       
        for ( int i = 0; i < studentFileRow.length; i++ ) {
             //Header Process
            if ( i == 0 ) {
                
                processHeaderTemplateOrg (studentFileRow[i].getOrganizationNodes(),buff);
                
                buff.append(studentFileRow[i].getFirstName());
                buff.append("\t");
                buff.append(studentFileRow[i].getMiddleName());
                buff.append("\t");
                buff.append(studentFileRow[i].getLastName());
                buff.append("\t");
                buff.append(studentFileRow[i].getHeaderDateOfBirth());
                buff.append("\t");
                buff.append(studentFileRow[i].getGrade());
                buff.append("\t");
                buff.append(studentFileRow[i].getGender());
                buff.append("\t");
                buff.append(studentFileRow[i].getExtPin1());
                buff.append("\t");
                buff.append(studentFileRow[i].getExtPin2());
                buff.append("\t");
                buff.append(studentFileRow[i].getScreenReader());
                buff.append("\t");
                buff.append(studentFileRow[i].getCalculator());
                buff.append("\t");
                buff.append(studentFileRow[i].getTestPause());
                buff.append("\t");
                buff.append(studentFileRow[i].getUntimedTest());
                buff.append("\t");
                buff.append(studentFileRow[i].getHighlighter());
                buff.append("\t");
                buff.append(studentFileRow[i].getQuestionBackgroundColor());
                buff.append("\t");
                buff.append((studentFileRow[i].getQuestionFontColor()));
                buff.append("\t");
                buff.append(studentFileRow[i].getAnswerBackgroundColor());
                buff.append("\t");
                buff.append(studentFileRow[i].getAnswerFontColor());
                buff.append("\t");
                buff.append(studentFileRow[i].getAnswerFontSize());
                buff.append("\t");
                
                StudentDemoGraphics []studentDemoGraphics=
                                    studentFileRow[i].getStudentDemoGraphics();
                
                processHeaderTemplateDemographic(studentDemoGraphics,buff);
                
            } else {
                
                int OrgHeaderSize =  studentFileRow[0].
                                            getOrganizationNodes().length;
                                            
                processDownLoadStudentData (studentFileRow[0],
                                            studentFileRow[i],
                                            OrgHeaderSize,buff);
            }
            
             buff.append("\n");
             
        }
        
         return buff.toString();  
        
    }
    
    //Creating student data file with POI
    //For MQC 67720: MDR columns needs to be removed for nonLaslinks
    public static byte[] downLoadStudentDataFile (StudentFile studentFile, String userName,
            UserManagement userManagement, boolean islaslinkCustomer ) {
            
            byte []errorData = null;
            // START: For MQC 67720: MDR columns needs to be removed for nonLaslinks
        	int headerPosFactor = 2;
        	if(islaslinkCustomer){
        		headerPosFactor = 3;
        	}
        	// END: For MQC 67720: MDR columns needs to be removed for nonLaslinks
        try{
           
            StudentFileRow []studentFileRow = studentFile.getStudentFileRows();
            
            HSSFWorkbook rwb = new HSSFWorkbook ();
            HSSFSheet sheet = rwb.createSheet("FirstSheet");
            Node []headerNode = null;
            HSSFCellStyle style = null;
            HSSFRow row = null;
            HSSFCell cell = null;
            int studentPos = 0;
            int colPos = 0;
            
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            dateFormat.applyLocalizedPattern("MM/dd/yyyy");
            HSSFDataFormat format = rwb.createDataFormat();
            
            StudentDemoGraphics []demoGraphicHeader = studentFileRow[0].getStudentDemoGraphics();
            for( int i = 0; i< studentFileRow.length; i++) {
                row = sheet.createRow(i);
                
                if (i == 0) {
                   
                    headerNode = studentFileRow[i].getOrganizationNodes();
                    //Insert organization header
                    // For MQC 67720: MDR columns needs to be removed for nonLaslinks
                    for (int k = 0,j = 0; k < headerNode.length; k++,j=j+headerPosFactor) { 	// For MQC 66840 : Upload/Download user/student with MDR
                        
                        cell = row.createCell((short)j);
                        style = cell.getCellStyle();
                        style.setWrapText(true);
                        cell.setCellStyle(style);
                        cell.setCellValue(headerNode[k].getOrgNodeCategoryName());
                        
                        cell = row.createCell((short)(j + 1));
                        style = cell.getCellStyle();
                        style.setWrapText(true);
                        cell.setCellStyle(style);
                        cell.setCellValue(headerNode[k].getOrgNodeCode());
                        
                        // Start For MQC 66840 : Upload/Download user/student with MDR
                        // For MQC 67720: MDR columns needs to be removed for nonLaslinks
                        if(islaslinkCustomer){
                            cell = row.createCell((short)(j + 2));
                            style = cell.getCellStyle();
                            style.setWrapText(true);
                            cell.setCellStyle(style);
                            cell.setCellValue(headerNode[k].getMdrNumber());
                        }
                        // End For MQC 66840 : Upload/Download user/student with MDR
               
                    }
                    
                    //Insert Student Header
                    // For MQC 67720: MDR columns needs to be removed for nonLaslinks
                    studentPos = headerNode.length * headerPosFactor; 	// For MQC 66840 : Upload/Download user/student with MDR
                    
                    //FirstName Header
                    cell = row.createCell((short)studentPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getFirstName());
                    
                    //MiddleName Header
                    
                    cell = row.createCell((short)studentPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getMiddleName());
                    
                    //LastName Header
                    
                    cell = row.createCell((short)studentPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getLastName());
                    
                    //DOB Header
                    
                    cell = row.createCell((short)studentPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getHeaderDateOfBirth());
                    
                    //Grade Header
                    colPos = studentPos++;
                    cell = row.createCell((short)colPos);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
					style.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
                    sheet.setDefaultColumnStyle((short)colPos,style);

                    cell.setCellValue (studentFileRow[i].getGrade());
                    
                    //Gender Header
                    
                    cell = row.createCell((short)studentPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getGender());
                    
                    // StudentId1 Header
                    colPos = studentPos++;
                    cell = row.createCell((short)colPos );
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    
                    style.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
                    sheet.setDefaultColumnStyle((short)colPos,style);

                    cell.setCellValue (studentFileRow[i].getExtPin1());
                     
                     // StudentId2 Header
                    
                    colPos = studentPos++;
                    cell = row.createCell((short)colPos);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);

                    style.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
                    sheet.setDefaultColumnStyle((short)colPos,style);

                    cell.setCellValue (studentFileRow[i].getExtPin2());
                     
                     //Screen Reader Header
                     
                    cell = row.createCell((short)studentPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getScreenReader());
                     
                     //Calculator Header
                     
                    cell = row.createCell((short)studentPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getCalculator());
                     
                     //TestPause Header
                     
                    cell = row.createCell((short)studentPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getTestPause());
                     
                    //Untimed Test Header
                    
                    cell = row.createCell((short)studentPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getUntimedTest());
                    
                    //Highlighter Header
                    
                    cell = row.createCell((short)studentPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getHighlighter());
                    
                    //QuestionBackgroundColor Header
                    
                    cell = row.createCell((short)studentPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getQuestionBackgroundColor());
                    
                    //QuestionFontColor Header
                    
                    cell = row.createCell((short)studentPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getQuestionFontColor());
                    
                    //AnswerBackgroundColor Header
                    
                    cell = row.createCell((short)studentPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getAnswerBackgroundColor());
                    
                    //AnswerFontColor Header
                    
                    cell = row.createCell((short)studentPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getAnswerFontColor());
                    
                    //AnswerFontSize Header
                    
                    cell = row.createCell((short)studentPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getAnswerFontSize());
                    
                    // DemoGraphics Header
                    
                    StudentDemoGraphics[] studentDemoGraphics = studentFileRow[i].
                                                        getStudentDemoGraphics();
                                                        
                    int size = studentDemoGraphics.length;
                    
                    for (int ii = 0; ii < size; ii++) {
                        
                        cell = row.createCell((short)studentPos++);
                        style = cell.getCellStyle();
                        style.setWrapText(true);
                        cell.setCellStyle(style);
                        cell.setCellValue (studentDemoGraphics[ii].getLabelName());
                        
                    }
                    
                    
                }  else {
                    
                    Node []nodeData = studentFileRow[i].getOrganizationNodes();
                    int cellPosition = 0;
                    for (int j = 0; j < nodeData.length; j++) {
                        // For MQC 67720: MDR columns needs to be removed for nonLaslinks
                        cellPosition = getCellPosition (headerNode, nodeData[j], islaslinkCustomer);
                        
                        if (cellPosition != -1) {
                            
                            //Create cell for nodeName
                            cell = row.createCell((short)cellPosition);
                            style = cell.getCellStyle();
                            style.setWrapText(true);
                            cell.setCellStyle(style);
                            cell.setCellValue(nodeData[j].getOrgNodeName());
                            
                            //create cell for nodeCode
                            cell = row.createCell((short)(cellPosition + 1));
                            style = cell.getCellStyle();
                            style.setWrapText(true);
                            cell.setCellStyle(style);
                            cell.setCellValue(nodeData[j].getOrgNodeCode());
                            
                          	// Start For MQC 66840 : Upload/Download user/student with MDR
                          	// For MQC 67720: MDR columns needs to be removed for nonLaslinks
                            if(islaslinkCustomer){
                            	 cell = row.createCell((short)(cellPosition + 2));
                                 style = cell.getCellStyle();
                                 style.setWrapText(true);
                                 cell.setCellStyle(style);
                                 cell.setCellValue(nodeData[j].getMdrNumber());
                            }
                           // Start For MQC 66840 : Upload/Download user/student with MDR
                        }
                   
                    } //End for loop
                    // For MQC 67720: MDR columns needs to be removed for nonLaslinks
                    cellPosition = headerNode.length * headerPosFactor;  	// For MQC 66840 : Upload/Download user/student with MDR  
                    
                    //FirstName
                 
                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if (studentFileRow[i].getFirstName() != null) {
                        
                        cell.setCellValue(studentFileRow[i].getFirstName());
                        
                    } else {
                        
                        cell.setCellValue("");
                        
                    }
                    
                    // MiddleName
                    
                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if (studentFileRow[i].getMiddleName() != null) {
                        
                        cell.setCellValue(studentFileRow[i].getMiddleName());
                        
                    } else {
                        
                        cell.setCellValue("");
                        
                    }
                    
                    // LastName
                    
                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if (studentFileRow[i].getLastName() != null) {
                        
                        cell.setCellValue(studentFileRow[i].getLastName());
                        
                    } else {
                        
                        cell.setCellValue("");
                        
                    }
                    
                    //Date of birth
                    
                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if ( studentFileRow[i].getBirthdate() != null ) {
                        
                        cell.setCellValue(dateFormat.format(studentFileRow[i].getBirthdate()));
                        
            
                    } else {
            
                         cell.setCellValue("");
                    }
                    
                    //Grade

                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if ( studentFileRow[i].getGrade() != null ) {
                        
                        cell.setCellValue(studentFileRow[i].getGrade());
                        
            
                    } else {
            
                         cell.setCellValue("");
                    }
                    
                    //Gender

                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if ( studentFileRow[i].getGender() != null ) {
                        
                        cell.setCellValue(ColorUtil.getGender(studentFileRow[i].getGender()));
                        
            
                    } else {
            
                         cell.setCellValue("");
                    }
                    
                    //StudentID

                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if ( studentFileRow[i].getExtPin1() != null ) {
                        
                        cell.setCellValue(studentFileRow[i].getExtPin1());
                        
            
                    } else {
            
                         cell.setCellValue("");
                    }
                    
                    //StudentID2

                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if ( studentFileRow[i].getExtPin2() != null ) {
                        
                        cell.setCellValue(studentFileRow[i].getExtPin2());
                        
            
                    } else {
            
                         cell.setCellValue("");
                    }
                    
                    //Screen Reader

                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if ( studentFileRow[i].getScreenReader() != null ) {
                        
                        cell.setCellValue(studentFileRow[i].getScreenReader());
                        
            
                    } else {
            
                         cell.setCellValue("");
                    }
                    
                    // Calculator

                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if ( studentFileRow[i].getCalculator() != null ) {
                        
                        cell.setCellValue(studentFileRow[i].getCalculator());
                        
            
                    } else {
            
                         cell.setCellValue("");
                    }


                    // Test Pause

                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if ( studentFileRow[i].getTestPause() != null ) {
                        
                        cell.setCellValue(studentFileRow[i].getTestPause());
                        
            
                    } else {
            
                         cell.setCellValue("");
                    }

                    // Untimed Test

                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if ( studentFileRow[i].getUntimedTest() != null ) {
                        
                        cell.setCellValue(studentFileRow[i].getUntimedTest());
                        
            
                    } else {
            
                         cell.setCellValue("");
                    }

                    // Highlighter

                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if ( studentFileRow[i].getHighlighter() != null ) {
                        
                        cell.setCellValue(studentFileRow[i].getHighlighter());
                        
            
                    } else {
            
                         cell.setCellValue("");
                    }
                    

                    // Question Background Color

                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if ( studentFileRow[i].getQuestionBackgroundColor() != null ) {
                        
                        cell.setCellValue(ColorUtil.getColor(
                            studentFileRow[i].getQuestionBackgroundColor()));
                        
            
                    } else {
            
                         cell.setCellValue(CTBConstants.WHITE_INIT );
                    }

                    // Question Font Color

                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if ( studentFileRow[i].getQuestionFontColor() != null ) {
                        
                        cell.setCellValue(ColorUtil.getColor(
                            studentFileRow[i].getQuestionFontColor()));
                        
            
                    } else {
            
                         cell.setCellValue( CTBConstants.BLACK_INIT );
                    }
                    
                    //Answer Background Color

                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if ( studentFileRow[i].getAnswerBackgroundColor() != null ) {
                        
                        cell.setCellValue(ColorUtil.getColor(
                            studentFileRow[i].getAnswerBackgroundColor())) ;
                        
            
                    } else {
            
                         cell.setCellValue( CTBConstants.LIGHT_YELLOW_INIT );
                    }
                    //Answer Font Color

                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if ( studentFileRow[i].getAnswerFontColor() != null ) {
                        
                        cell.setCellValue(ColorUtil.getColor(
                            studentFileRow[i].getAnswerFontColor()));
                        
            
                    } else {
            
                         cell.setCellValue( CTBConstants.BLACK_INIT );
                    }

                    //Font Size

                    cell = row.createCell((short)cellPosition++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    if ( studentFileRow[i].getAnswerFontSize() != null ) {
                        
                        cell.setCellValue(ColorUtil.getFontValue(
                            studentFileRow[i].getAnswerFontSize())) ;
                        
            
                    } else {
            
                         cell.setCellValue( CTBConstants.STANDARD_FONT );
                    }

                 
               ///For Demographic Data
                    //StudentDemoGraphics []demoGraphicHeader = studentFileRow[0].getStudentDemoGraphics();
                    
                    StudentDemoGraphicsData []demoGraphicData = studentFileRow[i].
                                                                getStudentDemoGraphicsData();
                    
                    
                    
                    Integer ethnicityDemoId = new Integer(0);
                    String ethnicityCardinality = null;
                    String demoValue = null;
                    
                    for ( int k = 0; k < demoGraphicHeader.length; k++ ) {
                        
                        Integer demoGraphicId = demoGraphicHeader[k].getCustomerDemographicId();
                        String msValueCardinality = demoGraphicHeader[k].getValueCardinality();
                        
                        cell = row.createCell((short)cellPosition++);
                        style = cell.getCellStyle();
                        style.setWrapText(true);
                        
                        // Added on 3 Sep ,2013.. #75217,#75292
                        // If current header is Sub-Ethnicity,then we will check if ethnicityId is present i.e. !0 and assign those ethnicty demographic Id 
                        // and Cardinality to demoGraphicId and msValueCardinality respectively in order to fetch the ethnicity demo value again. This value will
                        // be shown in place of Sub-ethnicity Cell.                        
                        if (ethnicityDemoId != 0 && ethnicityCardinality != null && demoGraphicHeader[k].getLabelName().equalsIgnoreCase("SUB_ETHNICITY"))
                        {
                        	demoGraphicId = ethnicityDemoId;
                        	msValueCardinality = ethnicityCardinality;
                        }                        
                                                
                        demoValue = getDemoGraphicsData(demoGraphicId,demoGraphicData,msValueCardinality,demoGraphicHeader[k].getLabelName(),islaslinkCustomer);
                        
                        // If current header is Ethnicity,then we will keep a copy of these demographic Id and cardinality.
                        if (islaslinkCustomer && demoValue.equalsIgnoreCase("Hispanic or Latino"))
                        {
                        	ethnicityDemoId = demoGraphicId;
                        	ethnicityCardinality = msValueCardinality;
                        }
                        //End : Added on 3 Sep ,2013.. #75217,#75292
                        
                        // Added on 3 Sep ,2013.. #75217,#75292
                        // If the current header is Sub-Ethnicity and demo-value is "Hispanic or Latino" , then it means that the Ethnicity selected was "Hispanic or Latino"
                        // and Sub-ethnicity was left blank in case of upload or in UI. Hence to export in the same manner we are 
                        // populating the Sub-ethnicity cell as Blank.
                        if (demoGraphicHeader[k].getLabelName().equalsIgnoreCase("SUB_ETHNICITY") && islaslinkCustomer && demoValue.equalsIgnoreCase("Hispanic or Latino")){
                        	demoValue = "";
                        }
                      //End : Added on 3 Sep ,2013.. #75217,#75292
                        	
                        cell.setCellValue(demoValue);                       
                        
                    }
               
               } //end of else for body part
               
            } //end of for loop
            
            String fileName = PathFinderUtils.getSaveFileName(userName,
                            "Student_Data_Download.xls",userManagement);
            fileName = CTBConstants.SERVER_FOLDER_NAME+"/"+fileName;
            FileOutputStream mfileOut = new FileOutputStream(fileName);
            rwb.write(mfileOut);
         
            ByteArrayOutputStream baos = new ByteArrayOutputStream();             
        
            InputStream in = new FileInputStream(fileName);

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
         
            mfileOut.close();   
            baos.flush();
            baos.close();   
         
            
        } catch (Exception e) {
            
            e.printStackTrace();
            
        }
        
        return errorData; 
        
    }

        /*
    * Create Template in Server path and return byte array 
    */
    // For MQC 67720: MDR columns needs to be removed for nonLaslinks 
    public static byte[] createStudentTemplateFile (StudentFile studentFile, String userName, 
            UserManagement userManagement, boolean islaslinkCustomer) {
        byte []errorData = null;
        try {
            
            StudentFileRow []studentFileRow = studentFile.getStudentFileRows();
            
            HSSFWorkbook rwb = new HSSFWorkbook ();
            HSSFSheet sheet = rwb.createSheet("FirstSheet");
            Node []headerNode = null;
            HSSFCellStyle style = null;
            HSSFRow row = null;
            HSSFCell cell = null;
            int userPos = 0;
            int colPos = 0;
            // START: For MQC 67720: MDR columns needs to be removed for nonLaslinks
            int headerPosFactor = 2;
        	if(islaslinkCustomer){
        		headerPosFactor = 3;
        	}
        	// END: For MQC 67720: MDR columns needs to be removed for nonLaslinks
            
            for ( int i = 0; i < studentFileRow.length; i++ ) {
                
                row = sheet.createRow(i);
                
                if (i == 0) {
                   
                    headerNode = studentFileRow[i].getOrganizationNodes();
                    //Insert organization header
                    // For MQC 66840 : Upload/Download user/student with MDR
                    // For MQC 67720: MDR columns needs to be removed for nonLaslinks
                    for (int k = 0,j = 0; k < headerNode.length; k++,j=j+headerPosFactor) {
                        
                        cell = row.createCell((short)j);
                        style = cell.getCellStyle();
                        style.setWrapText(true);
                        cell.setCellStyle(style);
                        cell.setCellValue(headerNode[k].getOrgNodeCategoryName());
                        
                        cell = row.createCell((short)(j + 1));
                        style = cell.getCellStyle();
                        style.setWrapText(true);
                        cell.setCellStyle(style);
                        cell.setCellValue(headerNode[k].getOrgNodeCode());
                        
                        // Start For MQC 66840 : Upload/Download user/student with MDR
                        if(islaslinkCustomer) {
                        	cell = row.createCell((short)(j + 2));
                            style = cell.getCellStyle();
                            style.setWrapText(true);
                            cell.setCellStyle(style);
                            cell.setCellValue(headerNode[k].getMdrNumber());
                        }
                        // End For MQC 66840 : Upload/Download user/student with MDR
               
                    }
                    
                    //Insert Student Header
                    // For MQC 67720: MDR columns needs to be removed for nonLaslinks
                    userPos = headerNode.length * headerPosFactor; 	// For MQC 66840 : Upload/Download user/student with MDR
                    
                    //FirstName Header
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getFirstName());
                    
                    //MiddleName Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getMiddleName());
                    
                    //LastName Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getLastName());
                    
                    //DOB Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getHeaderDateOfBirth());
                    
                    //Grade Header
                    
                    colPos = userPos++ ;
                    cell = row.createCell((short)colPos);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    
                    style.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
                    sheet.setDefaultColumnStyle((short)colPos,style);
                    
                    cell.setCellValue (studentFileRow[i].getGrade());
                    
                    //Gender Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getGender());
                    
                    // StudentId1 Header
                     colPos = userPos++ ;
                     cell = row.createCell((short)colPos);
                     style = cell.getCellStyle();
                     style.setWrapText(true);
                     cell.setCellStyle(style);
                     
                     style.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
                     sheet.setDefaultColumnStyle((short)colPos,style);
                     
                     cell.setCellValue (studentFileRow[i].getExtPin1());
                     
                     // StudentId2 Header
                     
                     colPos = userPos++ ;
                     cell = row.createCell((short)colPos);
                     style = cell.getCellStyle();
                     style.setWrapText(true);
                     cell.setCellStyle(style);
                     
                     style.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
                     sheet.setDefaultColumnStyle((short)colPos,style);
                     
                     cell.setCellValue (studentFileRow[i].getExtPin2());
                     
                     //Screen Reader Header
                     
                     cell = row.createCell((short)userPos++);
                     style = cell.getCellStyle();
                     style.setWrapText(true);
                     cell.setCellStyle(style);
                     cell.setCellValue (studentFileRow[i].getScreenReader());
                     
                     //Calculator Header
                     
                     cell = row.createCell((short)userPos++);
                     style = cell.getCellStyle();
                     style.setWrapText(true);
                     cell.setCellStyle(style);
                     cell.setCellValue (studentFileRow[i].getCalculator());
                     
                     //TestPause Header
                     
                     cell = row.createCell((short)userPos++);
                     style = cell.getCellStyle();
                     style.setWrapText(true);
                     cell.setCellStyle(style);
                     cell.setCellValue (studentFileRow[i].getTestPause());
                     
                    //Untimed Test Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getUntimedTest());
                    
                    //Highlighter Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getHighlighter());
                    
                    //QuestionBackgroundColor Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getQuestionBackgroundColor());
                    
                    //QuestionFontColor Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getQuestionFontColor());
                    
                    //AnswerBackgroundColor Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getAnswerBackgroundColor());
                    
                    //AnswerFontColor Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getAnswerFontColor());
                    
                    //AnswerFontSize Header
                    
                    cell = row.createCell((short)userPos++);
                    style = cell.getCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue (studentFileRow[i].getAnswerFontSize());
                    
                    // DemoGraphics Header
                    
                    StudentDemoGraphics[] studentDemoGraphics = studentFileRow[i].
                                                        getStudentDemoGraphics();
                                                        
                    int size = studentDemoGraphics.length;
                    
                    for (int ii = 0; ii < size; ii++) {
                        
                        cell = row.createCell((short)userPos++);
                        style = cell.getCellStyle();
                        style.setWrapText(true);
                        cell.setCellStyle(style);
                        cell.setCellValue (studentDemoGraphics[ii].getLabelName());
                        
                    }
                    
                    
                } else {
                   
                    Node []nodeData = studentFileRow[i].getOrganizationNodes();
                    int cellPosition = 0;
                    for (int j = 0; j < nodeData.length; j++) {
                       // For MQC 67720: MDR columns needs to be removed for nonLaslinks 
                        cellPosition = getCellPosition (headerNode, nodeData[j], islaslinkCustomer);
                        
                        if (cellPosition != -1) {
                            
                            //Create cell for nodeName
                            cell = row.createCell((short)cellPosition);
                            style = cell.getCellStyle();
                            style.setWrapText(true);
                            cell.setCellStyle(style);
                            cell.setCellValue(nodeData[j].getOrgNodeName());
                            
                            //create cell for nodeCode
                            cell = row.createCell((short)(cellPosition + 1));
                            style = cell.getCellStyle();
                            style.setWrapText(true);
                            cell.setCellStyle(style);
                            cell.setCellValue(nodeData[j].getOrgNodeCode());
                            
                          	// Start For MQC 66840 : Upload/Download user/student with MDR
                          	// For MQC 67720: MDR columns needs to be removed for nonLaslinks
                            if(islaslinkCustomer) {
                            	 cell = row.createCell((short)(cellPosition + 2));
                                 style = cell.getCellStyle();
                                 style.setWrapText(true);
                                 cell.setCellStyle(style);
                                 cell.setCellValue(nodeData[j].getMdrNumber());
                            }
                            // Start For MQC 66840 : Upload/Download user/student with MDR
                        }
                        
                        
                    }
                    
                }
                
            }
         String fileName = PathFinderUtils.getSaveFileName(userName,
                                           "Student_Template.xls", userManagement); 
                                           
         fileName = CTBConstants.SERVER_FOLDER_NAME+"/"+fileName;
         FileOutputStream mfileOut = new FileOutputStream(fileName);
		 rwb.write(mfileOut);
         
         ByteArrayOutputStream baos = new ByteArrayOutputStream();             
        
        InputStream in = new FileInputStream(fileName);

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
         
		 mfileOut.close();   
         baos.flush();
         baos.close();   
         
            
        } catch (Exception e) {
            
            e.printStackTrace();
            
        }
        
       return errorData; 
        
    } 
    

    
    
    
    ///
    public static String createStudentTemplate(StudentFile studentFile) {
        
        StudentFileRow[] studentFileRow = studentFile.getStudentFileRows();
        StringBuffer buff = new StringBuffer();
        
        for ( int i = 0; i < studentFileRow.length; i++ ) {
            
            //Header Process
            if ( i == 0 ) {
                
                processHeaderTemplateOrg (studentFileRow[i].getOrganizationNodes(),buff);
                
                buff.append(studentFileRow[i].getFirstName());
                buff.append("\t");
                buff.append(studentFileRow[i].getMiddleName());
                buff.append("\t");
                buff.append(studentFileRow[i].getLastName());
                buff.append("\t");
                buff.append(studentFileRow[i].getHeaderDateOfBirth());
                buff.append("\t");
                buff.append(studentFileRow[i].getGrade());
                buff.append("\t");
                buff.append(studentFileRow[i].getGender());
                buff.append("\t");
                buff.append(studentFileRow[i].getExtPin1());
                buff.append("\t");
                buff.append(studentFileRow[i].getExtPin2());
                buff.append("\t");
                buff.append(studentFileRow[i].getScreenReader());
                buff.append("\t");
                buff.append(studentFileRow[i].getCalculator());
                buff.append("\t");
                buff.append(studentFileRow[i].getTestPause());
                buff.append("\t");
                buff.append(studentFileRow[i].getUntimedTest());
                buff.append("\t");
                buff.append(studentFileRow[i].getHighlighter());
                buff.append("\t");
                buff.append(studentFileRow[i].getQuestionBackgroundColor());
                buff.append("\t");
                buff.append(studentFileRow[i].getQuestionFontColor());
                buff.append("\t");
                buff.append(studentFileRow[i].getAnswerBackgroundColor());
                buff.append("\t");
                buff.append(studentFileRow[i].getAnswerFontColor());
                buff.append("\t");
                buff.append(studentFileRow[i].getAnswerFontSize());
                buff.append("\t");
                
                StudentDemoGraphics[] studentDemoGraphics=studentFileRow[i].
                                                        getStudentDemoGraphics();
                                                        
                processHeaderTemplateDemographic(studentDemoGraphics,buff);
                
            } else {
                
                processBodyTemplateOrg (studentFileRow[i].getOrganizationNodes(),
                        studentFileRow[0].getOrganizationNodes(),buff);
                
            }
            
            buff.append("\n");
            
        }
        
        return buff.toString();  
        
    }

    
    private static void processHeaderTemplateOrg (Node []node, StringBuffer buff) {
        
        for ( int i = 0; i < node.length; i++ ) {
            
            buff.append(node[i].getOrgNodeCategoryName());
            buff.append("\t");
            buff.append(node[i].getOrgNodeCode());
            buff.append("\t");
            
        }
        
    }
   /* 
    private static String getHeaderCategoryName (String orgNodeName) {
        
        String []headerName = orgNodeName.split(" ");
        return headerName[0].trim();    
    }
    
    private static String getHeaderCategoryCode (String orgNodeCode) {
        
        String []headerCode = orgNodeCode.split(" ");
        return headerCode[0].trim();
        
    }
    */
    private static void processHeaderTemplateDemographic (
                                        StudentDemoGraphics []studentDemoGraphics, 
                                        StringBuffer buff) {
        
        for ( int i = 0; i < studentDemoGraphics.length; i++ ) {
            
            buff.append(studentDemoGraphics[i].getLabelName());
            buff.append("\t");
            
            
        }
        
    }
    
    private static void processHeaderTemplateAccomodation (
                                        CustomerConfig []customerConfig, 
                                        StringBuffer buff) {
        
        for ( int i = 0; i < customerConfig.length; i++ ) {
            
            buff.append(customerConfig[i].getCustomerConfigurationName());
            buff.append("\t");
            
        }
        
    }

    
    private static void processBodyTemplateOrg (Node []bodyNode,  Node []headerNode, StringBuffer buff) {
        
         //Node bodyNode[] = reverseOrder(node);
         int headerSize = headerNode.length;
         for (int i = 0, j = 0; i < headerSize; i++,j++ ) {
            
             if (j == bodyNode.length) {
                
                break;
             }   
             if (!getCategoryName(headerNode[i].getOrgNodeCategoryName()).equals(bodyNode[j].getOrgNodeCategoryName())) {
                
                buff.append("");
                buff.append("\t");
                buff.append("");
                buff.append("\t");
                j--;
                
            } else {
                
                buff.append(bodyNode[j].getOrgNodeName());
                buff.append("\t");
                
                if ( bodyNode[j].getOrgNodeCode() != null ) {
                    
                    buff.append(bodyNode[j].getOrgNodeCode());
                    
                } else {
                    
                    buff.append("");
                    
                }
                
                buff.append("\t");
                
            }
            
        }
        
    }
    
    private static String getCategoryName (String value) {
        
        String []catName = value.split(" ");
        return catName[0].trim();
        
    }
    /*
    * Reverse Order
    */ 
    private static Node[] reverseOrder (Node []node) {
        
        Node [] reverseNode = new Node[node.length];
        
        for (int i = node.length -1,j = 0; i >= 0; i--,j++ ) {
            
            reverseNode[j] = node[i];
            
        }
        
        return reverseNode;
        
    }
    
    private static void processDownLoadStudentData (StudentFileRow header,
                                                    StudentFileRow studentFileRow,
                                                    int userPosition, 
                                                    StringBuffer buff) {
        
       
        Node []bodyNode = studentFileRow.getOrganizationNodes();
        Node []headerNode = header.getOrganizationNodes();
        
        int headerSize = headerNode.length; 
        int count = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyLocalizedPattern("MM/dd/yyyy");
        
        for (int i = 0, j = 0; i < headerSize; i++,j++ ) {
            
             if (j == bodyNode.length) {
                
                break;
             }   
             if (!getCategoryName(headerNode[i].getOrgNodeCategoryName()).equals(bodyNode[j].getOrgNodeCategoryName())) {
                
                buff.append("");
                buff.append("\t");
                buff.append("");
                buff.append("\t");
                j--;
                count++;
                
            } else {
                
                buff.append(bodyNode[j].getOrgNodeName());
                buff.append("\t");
                
                if ( bodyNode[j].getOrgNodeCode() != null ) {
                    
                    buff.append(bodyNode[j].getOrgNodeCode());
                    
                } else {
                    
                    buff.append("");
                    
                }
                
                buff.append("\t");
                
            }
            
        }
        //inset blank in the rest of the organization
        for ( int k = bodyNode.length + count; k < userPosition; k++ ) {
            
            buff.append("");
            buff.append("\t");
            buff.append("");
            buff.append("\t");
            
        }
        
        //Insert student Details
        
        buff.append(studentFileRow.getFirstName());
        buff.append("\t");
        if ( studentFileRow.getMiddleName() != null ) {
                    
            buff.append(studentFileRow.getMiddleName());
            buff.append("\t");
                    
        } else {
            
            buff.append("");
            buff.append("\t");
            
        }
        buff.append(studentFileRow.getLastName());
        buff.append("\t");
        if( studentFileRow.getBirthdate() != null ) {
            
            buff.append(dateFormat.format(studentFileRow.getBirthdate()));
            buff.append("\t");

        }else {

             buff.append("");
             buff.append("\t");
        }
        if( studentFileRow.getGrade() != null ) {
            
            buff.append(studentFileRow.getGrade());
            buff.append("\t");

        }else {

             buff.append("");
             buff.append("\t");
        }
        if(studentFileRow.getGender() != null){
             buff.append(ColorUtil.getGender(studentFileRow.getGender()));
             buff.append("\t");

        } else {

             buff.append("");
             buff.append("\t");
        }

        if ( studentFileRow.getExtPin1() != null ){
            
            buff.append(studentFileRow.getExtPin1());
            buff.append("\t");
        
        } else {
            
             buff.append("");
             buff.append("\t");
            
        }
        if ( studentFileRow.getExtPin2() != null ) {
        
            buff.append(studentFileRow.getExtPin2());
            buff.append("\t");
        
        } else {
            
             buff.append("");
             buff.append("\t");
            
        }

        if ( studentFileRow.getScreenReader() != null ) {
            
            buff.append(studentFileRow.getScreenReader());
            buff.append("\t");
            
        } else {
            
            buff.append("");
            buff.append("\t");
            
        }

        if ( studentFileRow.getCalculator() != null ) {
            
            buff.append(studentFileRow.getCalculator());
            buff.append("\t");
            
        } else {
            
             buff.append("");
             buff.append("\t");
            
        }
        if ( studentFileRow.getTestPause() != null ) {
            
            buff.append(studentFileRow.getTestPause());
            buff.append("\t");
            
        } else {
            
            buff.append("");
            buff.append("\t");
            
        }
        if ( studentFileRow.getUntimedTest() != null ) {
            
            buff.append(studentFileRow.getUntimedTest());
            buff.append("\t");
            
        } else {
            
             buff.append("");
             buff.append("\t");
            
        }
        if ( studentFileRow.getHighlighter() != null ) {
            
            buff.append(studentFileRow.getHighlighter());
            buff.append("\t");
            
        } else {
            
            buff.append("");
            buff.append("\t");
            
        }
        if ( studentFileRow.getQuestionBackgroundColor() != null ) {
            
            buff.append(ColorUtil.getColor(studentFileRow.getQuestionBackgroundColor()));
            buff.append("\t");
            
        } else {
            
            buff.append(CTBConstants.WHITE_INIT);
            buff.append("\t");
            
        }
 
        if ( studentFileRow.getQuestionFontColor() != null ) {
            
            buff.append(ColorUtil.getColor(studentFileRow.getQuestionFontColor()));
            buff.append("\t");
            
        } else {
            
            buff.append(CTBConstants.BLACK_INIT);
            buff.append("\t");
            
        }
        if ( studentFileRow.getAnswerBackgroundColor() != null ) {
            
            buff.append(ColorUtil.getColor(studentFileRow.getAnswerBackgroundColor()));
            buff.append("\t");
            
        } else {
            
             buff.append(CTBConstants.LIGHT_YELLOW_INIT);
             buff.append("\t");
            
        }
        if ( studentFileRow.getAnswerFontColor() != null ) {
            
            buff.append(ColorUtil.getColor(studentFileRow.getAnswerFontColor()));
            buff.append("\t");
            
        } else {
            
            buff.append(CTBConstants.BLACK_INIT);
            buff.append("\t");
            
        }
        if ( studentFileRow.getAnswerFontSize() != null ) {
            
            buff.append(ColorUtil.getFontValue(studentFileRow.getAnswerFontSize()));
            buff.append("\t");
            
        } else {
            
            buff.append(CTBConstants.STANDARD_FONT);
            buff.append("\t");
            
        }
        
        StudentDemoGraphics []demoGraphicHeader = header.getStudentDemoGraphics();
        
        StudentDemoGraphicsData []demoGraphicData = studentFileRow.
                                                    getStudentDemoGraphicsData();
        
        for ( int k = 0; k < demoGraphicHeader.length; k++ ) {
            
            Integer demoGraphicId = demoGraphicHeader[k].getCustomerDemographicId();
            String msValueCardinality = demoGraphicHeader[k].getValueCardinality();
            
            buff.append(getDemoGraphicsData(demoGraphicId,demoGraphicData,msValueCardinality,"DEMO",false));// This function is not used anymore. Hence adding dummy entries to match the function signature. Aug 31,2013
            buff.append("\t");
            
        }
        
      
    }
    
    private static String getDemoGraphicsData (Integer demoGraphicId, 
                                        StudentDemoGraphicsData []demoGraphicData,
                                        String asCardinality , String demoHeader , Boolean isLaslink) {
    	
        // we have to tackle the Ethnicity demographic for Laslink customer 
        StringBuffer buffMultipleDemoValue =  new StringBuffer();;
        //retrive demographicValue        
        for ( int i = 0; i < demoGraphicData.length; i++ ) {
            
            //Getting Single cardinality values
            if ( (demoGraphicId.intValue() 
                    == demoGraphicData[i].getCustomerDemographicId().intValue())
                    && (!asCardinality.equals(CTBConstants.MULTIPLE_DEMOGRAPHIC))) {
            	
            	// If it is Laslink and the demo Header is "Ethnicity" and value is among one of the values of sub-ethnicity, then we 
            	// will show "Hispanic or Latino" as value under "Ethnicity" demo-header . The original demo value 
            	// will be shown under "Sub-ethnicity" header.
            	// Added on 3 Sep ,2013.. #75217,#75292
            	if (demoGraphicData[i].getValueName().equalsIgnoreCase(demoEthnicityMap.get(demoGraphicData[i].getValueName().toLowerCase())) && demoHeader.equalsIgnoreCase("ETHNICITY") && isLaslink){
            		
            		return "Hispanic or Latino";
            	}
                
                return demoGraphicData[i].getValueName();
            
            //Getting Multiple cardinality values
            } else if ((demoGraphicId.intValue() 
                    == demoGraphicData[i].getCustomerDemographicId().intValue())
                    && asCardinality.equals(CTBConstants.MULTIPLE_DEMOGRAPHIC) ) {
                    
                    buffMultipleDemoValue.append(demoGraphicData[i].getValueName());   
                    buffMultipleDemoValue.append(CTBConstants.DEMOGRAPHIC_VALUSE_SEPARATOR);
            }
            
        }
        if ( buffMultipleDemoValue.length()>0 ) {
            
            String msMultipleDemoValue = buffMultipleDemoValue.toString();
            
            if(msMultipleDemoValue.indexOf(CTBConstants.DEMOGRAPHIC_VALUSE_SEPARATOR)>0){
                
                msMultipleDemoValue = msMultipleDemoValue.substring
                        (0,msMultipleDemoValue.lastIndexOf(CTBConstants.DEMOGRAPHIC_VALUSE_SEPARATOR));

            }
            
            return msMultipleDemoValue;
        }            
        
        return "";
        
    }
    
    private static void processDownLoadUserNodeData (UserFileRow userFileRow, 
                                                     UserFileRow userFileRowHeader,                                                    
                                                     int userPosition, 
                                                     StringBuffer buff) {
        
        Node[] bodyNode = userFileRow.getOrganizationNodes();
        Node[] headerNode = userFileRowHeader.getOrganizationNodes();
        //Node[] bodyNode = reverseOrder(node);
        int headerSize = headerNode.length; 
        int count = 0;
        for (int i = 0, j = 0; i < headerSize; i++,j++ ) {
            
             if (j == bodyNode.length) {
                
                break;
             }   
             if (!getCategoryName(headerNode[i].getOrgNodeCategoryName()).equals
                    (bodyNode[j].getOrgNodeCategoryName())) {
                
                buff.append("");
                buff.append("\t");
                buff.append("");
                buff.append("\t");
                j--;
                count++;
                
            } else {
                
                buff.append(bodyNode[j].getOrgNodeName());
                buff.append("\t");
                
                if ( bodyNode[j].getOrgNodeCode() != null ) {
                    
                    buff.append(bodyNode[j].getOrgNodeCode());
                    
                } else {
                    
                    buff.append("");
                    
                }
                
                buff.append("\t");
                
            }
            
        }
        //inset blank in the rest of the organization
        for ( int k = bodyNode.length + count; k < userPosition; k++ ) {
            
            buff.append("");
            buff.append("\t");
            buff.append("");
            buff.append("\t");
            
        }
        
        buff.append(userFileRow.getFirstName());
        buff.append("\t");
        
        if ( userFileRow.getMiddleName() != null ) {
                    
            buff.append(userFileRow.getMiddleName());
            buff.append("\t");
                    
        } else {
            
            buff.append("");
            buff.append("\t");
            
        }
        buff.append(userFileRow.getLastName());
        buff.append("\t");
        if ( userFileRow.getEmail() != null ) {
            
            buff.append(userFileRow.getEmail());
            buff.append("\t");
            
        } else {
            
             buff.append("");
             buff.append("\t");
            
        }
        
        buff.append(ColorUtil.initCap(userFileRow.getTimeZoneDese()));
        buff.append("\t");
        buff.append(userFileRow.getRoleName());
        buff.append("\t");
        
        if ( userFileRow.getAddress1() == null ) {
            
            buff.append("");
            buff.append("\t");
            
        } else {
            
            buff.append(userFileRow.getAddress1());
            buff.append("\t");
            
        }
        
        if ( userFileRow.getAddress2() == null ) {
            
            buff.append("");
            buff.append("\t");
            
        } else {
            
            buff.append(userFileRow.getAddress2());
            buff.append("\t");
            
        }
        
        if ( userFileRow.getCity() == null ) {
            
            buff.append("");
            buff.append("\t");
            
        } else {
            
            buff.append(userFileRow.getCity());
            buff.append("\t");
            
        }
        
        if ( userFileRow.getState() == null ) {
            
            buff.append("");
            buff.append("\t");
            
        } else {
            
            buff.append(userFileRow.getState());
            buff.append("\t");
            
        }
        
        if ( userFileRow.getZip() == null ) {
            
            buff.append("");
            buff.append("\t");
            
        } else {
            
            buff.append(userFileRow.getZip());
            buff.append("\t");
            
        }
        
        if ( userFileRow.getPrimaryPhone() == null ) {
            
            buff.append("");
            buff.append("\t");
            
        } else {
            
            buff.append(userFileRow.getPrimaryPhone());
            buff.append("\t");
            
        }
        
        if ( userFileRow.getSecondaryPhone() == null ) {
            
            buff.append("");
            buff.append("\t");
            
        } else {
            
            buff.append(userFileRow.getSecondaryPhone());
            buff.append("\t");
            
        }
        
        if ( userFileRow.getFaxNumber() == null ) {
            
            buff.append("");
            buff.append("\t");
            
        } else {
            
            buff.append(userFileRow.getFaxNumber());
            buff.append("\t");
            
        }
        
    
    }
    
     public static boolean verifyFileExtension (String fileName) {
    
        boolean isValidFileExt = false;
        
        if ( fileName != null && !fileName.equals("") && fileName.length() >= 5 ) {
            
            
            String fileExt = fileName.substring(fileName.length()-4 , 
                                                fileName.length());
           
            if ( fileExt.equalsIgnoreCase(".xls") ) {
            
                isValidFileExt = true;
            
            }
            
        } else {
            
            isValidFileExt = false;
        }
        
        return isValidFileExt;                                
    }
    
      /**
     * fetch customer from list
     * @param customerId Integer
     * @param customerList List
     * @return CustomerProfileInformation
     */
    
     public static AuditFileHistory getFileFromList
                                    (Integer auditFileId, List fileList) {
        
        AuditFileHistory file = new AuditFileHistory();
        
        for ( int i = 0; i < fileList.size(); i++ ) {
         
            file = (AuditFileHistory) fileList.get(i);
         
            if ( file.getDataFileAuditId().equals(auditFileId) ) {
               
                return file;
            
            } 
        }
        return file;
    }
    
    

} 

    