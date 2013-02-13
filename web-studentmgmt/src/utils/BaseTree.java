package utils; 

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ctb.bean.studentManagement.CustomerConfiguration;
import com.ctb.bean.studentManagement.CustomerDemographic;

public class BaseTree {
	
	private List<TreeData> data = new ArrayList<TreeData> ();
	private Integer leafNodeCategoryId;
	public String[] gradeOptions = null;
	private List<CustomerDemographic> customerDemographicList;
	public CustomerConfiguration [] customerConfiguration = null;
	public Boolean isOOSConfigured = false;
	public List<String> demoCategory;
	/**
	 * @return the demoCategory
	 */
	public List<String> getDemoCategory() {
		return demoCategory;
	}

	/**
	 * @param demoCategory the demoCategory to set
	 */
	public void setDemoCategory(List<String> demoCategory) {
		this.demoCategory = demoCategory;
	}

	/**
	 * @return the gradeOptions
	 */
	public String[] getGradeOptions() {
		return gradeOptions;
	}

	/**
	 * @param gradeOptions the gradeOptions to set
	 */
	public void setGradeOptions(String[] gradeOptions) {
		this.gradeOptions = gradeOptions;
	}

	/**
	 * @return the leafNodeCategoryId
	 */
	public Integer getLeafNodeCategoryId() {
		return leafNodeCategoryId;
	}

	/**
	 * @param leafNodeCategoryId the leafNodeCategoryId to set
	 */
	public void setLeafNodeCategoryId(Integer leafNodeCategoryId) {
		this.leafNodeCategoryId = leafNodeCategoryId;
	}

	public List<TreeData> getData() {
		return data;
	}

	public void setData(List<TreeData> data) {
		this.data = data;
	}

	/**
	 * @return the customerDemographicList
	 */
	public List<CustomerDemographic> getCustomerDemographicList() {
		return customerDemographicList;
	}

	/**
	 * @param customerDemographicList the customerDemographicList to set
	 */
	public void setCustomerDemographicList(
			List<CustomerDemographic> customerDemographicList) {
		this.customerDemographicList = customerDemographicList;
	}

	public CustomerConfiguration[] getCustomerConfiguration() {
		return customerConfiguration;
	}
	public void setCustomerConfiguration(CustomerConfiguration[] customerConfiguration) {
		this.customerConfiguration = customerConfiguration;
	}

	public Boolean getIsOOSConfigured() {
		return isOOSConfigured;
	}

	public void setIsOOSConfigured(Boolean isOOSConfigured) {
		this.isOOSConfigured = isOOSConfigured;
	}
	
	

}
