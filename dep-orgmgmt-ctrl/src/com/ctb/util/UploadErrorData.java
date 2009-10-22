package com.ctb.util; 

import com.ctb.bean.testAdmin.DataFileRowError;
import com.ctb.bean.testAdmin.UserFileRow;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


/*
 * Userd for UploadErrorData 
 * @author  Tata Consultancy Services
 * 
 */

public class UploadErrorData 
{ 
    
    public HashMap errorDataRows;
    public ArrayList orgCommonPathErrors;
    public ArrayList errorRow;
    public UploadErrorData() { };
    public HSSFRow row;
    
    
       
    public UploadErrorData(HashMap errorDataRows,ArrayList orgCommonPathErrors,ArrayList errorRow,HSSFRow row ){
       
       this.errorDataRows =  errorDataRows;
       this.orgCommonPathErrors = orgCommonPathErrors;
       this.errorRow = errorRow;
       this.row = row ;
        
    }
    
    public byte[] writeToPOIExcel() throws Exception {
        
        HSSFWorkbook wb = new HSSFWorkbook ();
        HSSFSheet sheet = wb.createSheet("FirstSheet");
        HSSFRow rows = sheet.createRow(0);
        Integer[] errorColumns = null;
        HSSFCellStyle requiredStyle = wb.createCellStyle();
        //style.setFillPattern(HSSFCellStyle.NO_FILL);
        requiredStyle.setFillForegroundColor(HSSFColor.RED.index);
        requiredStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        HSSFCellStyle validateCharStyle = wb.createCellStyle();
        //validateCharStyle.setFillBackgroundColor(HSSFColor.ORANGE.index);
        // Create a new font and alter it.
        HSSFFont font = wb.createFont();
        /*font.setFontHeightInPoints((short)24);
        font.setFontName("Courier New");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setStrikeout(true);
        font.setColor(HSSFColor.BLUE.index);
        validateCharStyle.setFont(font);*/
        validateCharStyle.setFillForegroundColor(HSSFColor.ORANGE.index);
        validateCharStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        HSSFCellStyle maxLengthStyle = wb.createCellStyle();
        maxLengthStyle.setFillForegroundColor(HSSFColor.BLUE.index);
        maxLengthStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
       
        
        // create  excel header for error data
        for ( int i = 0; i < row.getPhysicalNumberOfCells(); i++ ) {
        
            HSSFCell cell = rows.createCell((short)i);
            HSSFCellStyle style = cell.getCellStyle();
            style.setWrapText(true);
            cell.setCellStyle(style);
            
            if ( cell.getCellType()==0 ) {
                
                cell.setCellValue(row.getCell(i).getNumericCellValue());
                                
            }
    
           if ( cell.getCellType()==1 ) {
            
                cell.setCellValue(row.getCell(i).getStringCellValue()); 
                 
            } 
            
            cell.setCellValue(row.getCell(i).getStringCellValue());   
                 
        }
        // get the error cell index
        
    
        // create the body parts of error data
        
        for (int i = 0; i < errorRow.size(); i++) {
            
            HSSFRow errorrow = (HSSFRow) errorRow.get(i);
            HashMap indexposition = null;
            
           
                if ( errorDataRows.containsKey(new Integer(i+1) ) ) {
                    
                    UserFileRow userFileRow = (UserFileRow) errorDataRows.get(new Integer(i+1));
                    DataFileRowError[] DataFileRowError = (DataFileRowError[]) userFileRow.getDataFilerowError();
                    //errorColumns =  new Integer[DataFileRowError.length];
                    indexposition = new HashMap();
                    
                    for ( int index=0; index < DataFileRowError.length ; index++) {
                        
                        //errorColumns[0] = DataFileRowError[index].getColumnIndex();
                        indexposition.put(DataFileRowError[index].getColumnIndex() , 
                                DataFileRowError[index].getErrorType());
                                
                    }
                    
                    rows = sheet.createRow(i+1);
                    //  write to server path this xml
                    for (int k = 0; k < row.getPhysicalNumberOfCells(); k++) {
                        HSSFCell cell = rows.createCell((short)k);
                        
    
                        HSSFCellStyle style = cell.getCellStyle();
                        style.setWrapText(true);
                        
                        
                        
                        if ( indexposition.containsKey(new Integer(k)) ) {
                            
                             if ( indexposition.get(new Integer(k)) .equals("requiredError") )
                                cell.setCellStyle(requiredStyle);
                             if ( indexposition.get(new Integer(k)) .equals("invalidChar") )
                                cell.setCellStyle(validateCharStyle);
                             if ( indexposition.get(new Integer(k)) .equals("maxLength") )
                                cell.setCellStyle(maxLengthStyle);
                               
                        } else {
                            style.setFillPattern(HSSFCellStyle.NO_FILL);
                            cell.setCellStyle(style);
                        }
                        
                        
                        
                        if (  errorrow.getCell(k) != null ) {
                             
                            //if(cell.getCellType()==0){
                                //cell.setCellValue(errorrow.getCell(k).getNumericCellValue());
                                
                             // }
    
                             // if(cell.getCellType()==1){
                                 cell.setCellValue(errorrow.getCell(k).getStringCellValue());  
                              //} 
                            
                        } else {
                            cell.setCellValue("");
                        }
                    }
                }
              
                    
                    
            }
            
            FileOutputStream mfileOut = new FileOutputStream("UploadError.xls");
            wb.write(mfileOut);
            
            mfileOut.close();
            
            byte[] errordata = wb.getBytes();
            
            //  convert to byte[] to save in blob
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();             
        
            InputStream in = new FileInputStream("UploadError.xls");
    
            boolean moreData = true;
            
           
		
    
            try {
                //baos = new FileOutputStream(filePath);
                while(moreData) {
    
                   byte [] buffer = new byte[128];
                   int read = in.read(buffer);
                  moreData = read > 0;
    
                   if(moreData) {
                       baos.write(buffer, 0, read);
                   }
               }
               baos.flush();
               baos.close();
           
            
        } catch (Exception e) {
            e.printStackTrace();
        } //end test         
            mfileOut.close();
            
            // Delete the file from the serverPath
           /* File errorFile = new File("c:\\UploadError.xls");
            
            if ( errorFile.exists()) {
                
                errorFile.delete();
            }*/
            return baos.toByteArray(); 
            
    }
    
        
       
        
        
        
        
        
        
    

   
} 
