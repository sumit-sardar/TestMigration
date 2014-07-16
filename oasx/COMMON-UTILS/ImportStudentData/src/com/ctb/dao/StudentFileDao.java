package com.ctb.dao;

import com.ctb.bean.CustomerConfig;
import com.ctb.bean.CustomerConfigurationValue;
import com.ctb.bean.CustomerDemographicValue;
import com.ctb.bean.Node;
import com.ctb.bean.StudentDemoGraphics;
import com.ctb.utils.cache.StudentDBCacheImpl;

/**
 * 
 * @author TCS
 *
 */
public interface StudentFileDao {
	
	public CustomerConfigurationValue [] getCustomerConfigurationValuesForGrades(int customerId) throws Exception;
	
	public StudentDemoGraphics[] getStudentDemoGraphics(Integer customerId) throws Exception;
	
	public CustomerDemographicValue[] getCustomerDemographicValue(Integer customerDemographicId) throws Exception;
	
	public CustomerConfig[] getCustomerConfigurationForAccommodation(Integer customerId) throws Exception;
	
	public boolean checkCustomerConfigurationEntries(Integer customerId, String customerConfigurationName) throws Exception;
	
	public String checkCustomerConfiguration(Integer customerId,String customerConfigurationName) throws Exception;
	
	public Node[] getUserDataTemplate(Integer customerId) throws Exception;
	
	public void getExistStudentData(Integer customerId,StudentDBCacheImpl dbCache) throws Exception;
	
	public Node[] getTopNodeDetails(Integer customerId) throws Exception;
	
	public String checkUniqueMdrNumberForOrgNodes(String selectedMdrNumber) throws Exception;

	public String checkUniqueStudentId(String studentId, Integer customerId) throws Exception;

}
