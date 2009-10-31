package utils; 

import com.ctb.bean.testAdmin.DataFileAudit;
import com.ctb.bean.testAdmin.DataFileAuditData;
import com.ctb.widgets.bean.PagerSummary;
import dto.AuditFileHistory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class UploadHistoryUtils 
{ 
    
    /**
     * buildcustomerPagerSummary
     */    
    public static PagerSummary buildFilePagerSummary(DataFileAuditData dataFileAuditData, 
                                                Integer pageRequested) {
        PagerSummary pagerSummary = new PagerSummary();
        pagerSummary.setCurrentPage(pageRequested);                
        pagerSummary.setTotalObjects(dataFileAuditData.getTotalCount());
        pagerSummary.setTotalPages(dataFileAuditData.getFilteredPages());
        pagerSummary.setTotalFilteredObjects(null);  
                  
        return pagerSummary;
    }
    
    /**
     * buildAuditFileList
     */    
    public static List buildAuditFileList(DataFileAuditData dataFileAuditData) 
    {
        ArrayList auditFileList = new ArrayList();
        AuditFileHistory auditFileHistory = null;
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyLocalizedPattern("MM/dd/yyyy HH:mm:ss");
        
        if (dataFileAuditData != null) {
            DataFileAudit[] dataFileAudit = dataFileAuditData.getDataFileAudit();
            
            if(dataFileAudit != null){
                
                for (int i=0 ; i<dataFileAudit.length ; i++) {
                    DataFileAudit auditFile = dataFileAudit[i];
                    
                    if (auditFile != null && auditFile.getDataFileAuditId() != null) {
                        auditFileHistory = new AuditFileHistory();
                        auditFileHistory.setCreatedDateTime(df.format(auditFile.getCreatedDateTime()));
                        auditFileHistory.setCustomerId(auditFile.getCustomerId());
                        auditFileHistory.setDataFileAuditId(auditFile.getDataFileAuditId());
                        
                        auditFileHistory.setStatus(auditFile.getStatus());
                        auditFileHistory.setUploadFileName(auditFile.getUploadFileName());
                        
                        if ( auditFile.getStatus().equals("IN") )  {
                        
                            auditFileHistory.setUploadFileRecordCount("--"); 
                            auditFileHistory.setFailedRecordCount("--");
                        
                        } else {
                            
                            if ( auditFile.getUploadFileRecordCount() != null ) {
                            
                                auditFileHistory.setUploadFileRecordCount(
                                        String.valueOf(auditFile.getUploadFileRecordCount())); 
                            
                            } else {
                            
                                auditFileHistory.setUploadFileRecordCount("0");
                            
                            }       
                            
                            if ( auditFile.getFailedRecordCount() != null ) {
                            
                                auditFileHistory.setFailedRecordCount(
                                        String.valueOf(auditFile.getFailedRecordCount()));
                            
                            } else {
                            
                                auditFileHistory.setFailedRecordCount("0");
                            
                            }
                            
                        }
                        auditFileHistory.setActionPermission(auditFile.getEditable());
                        auditFileList.add(auditFileHistory);
                    }
                }
            }
        }
        return auditFileList;
    }
    
    /*
    * retrive filename by passing selectedDataFileAuditId
    */ 
    public static String getFileName(Integer selectedDataFileAuditId, List recordList) {
        
        String fileName = "";
        for (int i = 0; i < recordList.size(); i++ ) {
            
            AuditFileHistory auditFileHistory = (AuditFileHistory) recordList.get(i);
            
            if (auditFileHistory.getDataFileAuditId().intValue() 
                    == selectedDataFileAuditId.intValue()) {
                        
                  fileName = auditFileHistory.getUploadFileName();
                  break;      
                
            }
            
        }
        
        return fileName;
        
    }
    
    /**
     * findOrgNode
     */    
    public static AuditFileHistory findFile(List fileList, Integer auditFileId) {
        if (fileList != null && auditFileId != null) {
            
            for ( int i = 0 ; i < fileList.size() ; i++ ) {
                
                AuditFileHistory file = (AuditFileHistory)fileList.get(i);
                if (file.getDataFileAuditId().intValue() == auditFileId.intValue()) {
                
                    return file;
                
                }
            }
        }
        return null;
    }
    
} 
