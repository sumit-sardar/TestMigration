package com.ctb.control.uploadDownloadManagement; 

import com.bea.control.annotations.TransactionAttribute;
import com.bea.control.annotations.TransactionAttributeType;
import com.ctb.bean.testAdmin.StudentFile;
import com.ctb.bean.testAdmin.UserFile;
import org.apache.beehive.controls.api.bean.ControlInterface;

@ControlInterface()
public interface UploadDownloadManagement 
{ 

    
    UserFile getUserFileTemplate(java.lang.String userName) throws com.ctb.exception.CTBBusinessException;
    

    
    com.ctb.bean.testAdmin.StudentFile getStudentFileTemplate(java.lang.String userName) throws com.ctb.exception.CTBBusinessException;

    
    com.ctb.bean.testAdmin.UserFile getUserFile(java.lang.String userName) throws com.ctb.exception.CTBBusinessException;

    
    com.ctb.bean.testAdmin.StudentFile getStudentFile(java.lang.String userName) throws com.ctb.exception.CTBBusinessException;
    
    com.ctb.bean.testAdmin.DataFileAuditData getUploadHistory(java.lang.String userName, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * delete error file
     * @param auditFileId
     * @throws CTBBusinessException
     */
    
    void deleteErrorDataFile(java.lang.Integer auditFileId) throws com.ctb.exception.CTBBusinessException;

    
    byte[] getErrorDataFile(java.lang.String userName, java.lang.Integer auditFileId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Check whether user has the
     * @param userName - identifies the user
     * @param customerId - identifies the customer
     * @return Boolean
     * @throws CTBBusinessException
     */
    
    java.lang.Boolean hasUploadDownloadConfig(java.lang.String userName) throws com.ctb.exception.CTBBusinessException;

    
    void uploadFile(java.lang.String userName, java.lang.String serverFilePath, java.lang.Integer uploadDataFileId) throws com.ctb.exception.CTBBusinessException;

    
    void updateAuditFileStatus(java.lang.Integer uploadFileId) throws com.ctb.exception.CTBBusinessException;

    
    java.lang.Integer createDataFileTemp(com.ctb.bean.testAdmin.DataFileTemp dataFileTemp) throws com.ctb.exception.CTBBusinessException;

    
    java.lang.Integer addErrorDataFile(java.lang.String userName, java.lang.String serverFilePath, java.lang.Integer uploadDataFileId) throws com.ctb.exception.CTBBusinessException;

    /**
     * it will get the common path from customer to the login user node
     * @param userName
     * @param customer
     * @param userTopOrgNode
     * @param commonPathMap
     */
    
    void getcommonPathFromCustomerToLoginUser(java.lang.String userName, com.ctb.bean.testAdmin.Customer customer, com.ctb.bean.testAdmin.Node[] userTopOrgNode, java.util.HashMap commonPathMap) throws com.ctb.exception.CTBBusinessException;
} 
