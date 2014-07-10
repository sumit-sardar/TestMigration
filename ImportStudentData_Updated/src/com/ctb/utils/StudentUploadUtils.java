package com.ctb.utils;

import com.ctb.bean.CustomerConfig;
import com.ctb.bean.CustomerConfigurationValue;
import com.ctb.bean.CustomerDemographicValue;
import com.ctb.bean.StudentDemoGraphics;
import com.ctb.dao.StudentFileDao;
import com.ctb.dao.StudentFileDaoImpl;

public class StudentUploadUtils {
	
  private StudentFileDao studentDao = null;
	
  public String[] getGradesForCustomer(Integer customerId) throws Exception {
		String [] grades = null;
		this.studentDao = new StudentFileDaoImpl();
		CustomerConfigurationValue [] customerConfigurationValues = studentDao.getCustomerConfigurationValuesForGrades(customerId.intValue());
		if (customerConfigurationValues != null && customerConfigurationValues.length > 0){
			grades = new String[customerConfigurationValues.length];
			for (int i = 0; i < customerConfigurationValues.length; i++) {
				grades[i] = customerConfigurationValues[i].getCustomerConfigurationValue();
			}
		}
		return grades;	
  }
  
  
  public StudentDemoGraphics[] getStudentDemoGraphics(Integer customerId) throws Exception{
	  this.studentDao = new StudentFileDaoImpl();
	  StudentDemoGraphics[] studentDemographics = studentDao.getStudentDemoGraphics(customerId);
	  return studentDemographics;
  }
  
  public CustomerDemographicValue[] getCustomerDemographicValue(Integer customerDemographicId) throws Exception{
	  this.studentDao = new StudentFileDaoImpl();
	  CustomerDemographicValue[] customerDemographicValue = studentDao.getCustomerDemographicValue(customerDemographicId);
	  return customerDemographicValue;
  }
  
  public CustomerConfig[] getCustomerConfigurationForAccommodation(Integer customerId) throws Exception{
	  this.studentDao = new StudentFileDaoImpl();
	  CustomerConfig[] customerConfigs = studentDao.getCustomerConfigurationForAccommodation(customerId);
	  return customerConfigs;
  }
  
  
}
