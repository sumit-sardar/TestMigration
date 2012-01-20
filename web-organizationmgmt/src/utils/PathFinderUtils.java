package utils; 

import com.ctb.bean.testAdmin.DataFileTemp;
import com.ctb.bean.testAdmin.User;
import com.ctb.control.db.UploadDataFile;
import com.ctb.control.uploadDownloadManagement.UploadDownloadManagement;
import com.ctb.control.userManagement.UserManagement;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.CTBConstants;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import manageUpload.ManageUploadController.ManageUploadForm;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.RED;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.upload.FormFile;

public class PathFinderUtils 
{ 
    public static String getSaveFileName(String userName,
                                           String FileName, 
                                           UserManagement userManagement){
        
        Date date = new Date();
        String timeStamp = String.valueOf(date.getTime());
        User user = new User();
        try{
            user = userManagement.getUser(userName, userName);
        } catch (CTBBusinessException e) {
            e.printStackTrace();
        }
        StringBuffer fullFileName = new StringBuffer();
            //fullServerPath.append("/");
            fullFileName.append(FileName.substring(0,
                                      (FileName.length()-4))+"_");
            fullFileName.append(user.getCustomer().getCustomerId()+"_");
            fullFileName.append(user.getUserId()+"_");
            fullFileName.append(timeStamp);
            fullFileName.append(".xls");
                  
       return fullFileName.toString();
      }
      
    public static void saveFileToDBTemp(String saveFileName, ManageUploadForm form, UploadDownloadManagement uploadDownloadManagement) throws Exception {
        try {
            DataFileTemp temp = new DataFileTemp();
           // temp.setDataFileAuditId(form.getAuditFileId());
            temp.setDataFile(form.getTheFile().getFileData());
            Integer uploadDataFileId = uploadDownloadManagement.createDataFileTemp(temp);
            form.setAuditFileId(uploadDataFileId);
		} catch (Exception e) {
			e.printStackTrace();
            throw e;
		}
    }

    public static Integer saveFileToDBTemp(String saveFileName, FormFile theFile, UploadDownloadManagement uploadDownloadManagement) throws Exception {
    	Integer uploadDataFileId = new Integer(0);
        try {
            DataFileTemp temp = new DataFileTemp();
           // temp.setDataFileAuditId(form.getAuditFileId());
            temp.setDataFile(theFile.getFileData());
            uploadDataFileId = uploadDownloadManagement.createDataFileTemp(temp);
		} catch (Exception e) {
			e.printStackTrace();
            throw e;
		}
		return uploadDataFileId;
    }
    
   /**
	 * Read the file extension (characters after last period) from the filename.
	 * Protect against files with no extension.
	 * @param filename
	 * @return
	 */
    public static String getFileExtension(String filename) {
        String ext = (filename.lastIndexOf(".") > -1)
				? filename.substring(filename.lastIndexOf("."), filename.length())
				: "";
        return ext;
    } 
    
    public static void saveFileToDB(String fullFilePath,
                                    UploadDownloadManagement uploadDownloadManagement, 
                                    String userName, Integer uploadDataFileId)
                                    throws IOException,CTBBusinessException {
                
             uploadDownloadManagement.uploadFile(userName, fullFilePath, uploadDataFileId);  
    
    }
    
    public static boolean createTemplate (ManageUploadForm form, 
                                                boolean isUserTemplete) throws IOException{
        
        File userFile = null;
        OutputStream os = null;
        boolean moreData = true;
        InputStream is = null;
        String fileName = form.getFileName();
        
        if (isUserTemplete) {
            
             
            try {
                
                userFile = new File("Template.xls");
                is = new FileInputStream(userFile);
                os = new FileOutputStream(fileName);
                
                while(moreData) {
                   
                   byte [] buffer = new byte[1024];
                   int read = is.read(buffer);
                   moreData = read > 0;
                   
                   if(moreData) {
                    
                       os.write(buffer, 0, read);
                       
                   }
                }
                
                return true; 
                
                
            } catch (Exception e) {
                
                e.printStackTrace();
                
            } finally {
                
                os.flush();
                os.close();
                
            }
        return false;
            
        } else {
            //student template
            return true;   
        }
        
    }

    public static String createFileContent(InputStream errorFile) {
        
       POIFSFileSystem pfs = null;
       HSSFSheet sheet = null;
       StringBuffer buff = new StringBuffer(); 
       try{
       
			pfs = new POIFSFileSystem( errorFile );
			HSSFWorkbook wb = new HSSFWorkbook(pfs);
	        sheet = wb.getSheetAt(0);
	      } catch(IOException e) {
            e.printStackTrace();
          }
       
           Iterator rows = sheet.rowIterator(); 
             while( rows.hasNext() ) {           
                 HSSFRow row = (HSSFRow) rows.next();
                 Iterator cells = row.cellIterator();
                 while( cells.hasNext() ) {
                     HSSFCell cell = (HSSFCell) cells.next();
                     String cellValue = cell.toString();
                     //cell.getCellStyle().g
                     //cell.getCellStyle().getFillBackgroundColor()
                     buff.append("<div style=\"background-color:#ff0000;width:100\">"+cell+"</div>");
                     buff.append("\t");
                 }
                 buff.append("\n");
             }
         
         return buff.toString();
    }
      
      
} 
