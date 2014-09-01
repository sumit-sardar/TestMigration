package com.ctb.dao;

import com.ctb.bean.CustomerConfiguration;
import com.ctb.bean.CustomerConfigurationValue;
import com.ctb.bean.DataFileAudit;
import com.ctb.bean.DataFileTemp;
import com.ctb.bean.OrgNodeCategory;

/**
 * Interface related to UploadFile operations
 * @author TCS
 *
 */
public interface UploadFileDao {	
	public Integer getNextPKForTempFile() throws Exception;
	
	public void createDataFileTemp(DataFileTemp tempFile) throws Exception;
	
	public boolean checkCustomerConfiguration(Integer customerId , String configuration) throws Exception;
	
	public OrgNodeCategory[] getOrgNodeCategories(Integer customerId)  throws Exception;
	
	public CustomerConfiguration[] getCustomerConfigurations (Integer customerId)  throws Exception;
	
	public CustomerConfigurationValue[] getCustomerConfigurationsValue(Integer configId)  throws Exception;
	
	public void createDataFileAudit(DataFileAudit dataFileAudit) throws Exception;
	
	public DataFileAudit getUploadFile(Integer uploadDataFileId) throws Exception;
	
	public void upDateAuditTable(DataFileAudit dataFileAudit) throws Exception;

}
