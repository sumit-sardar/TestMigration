package com.ctb.util.jmsutils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExportDataVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer CustomerId ;
	private Integer userId;
	private String userName;
	private Integer jobId;
	private List<Integer>  testroster= new ArrayList<Integer>();
	

	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return CustomerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		CustomerId = customerId;
	}

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the testroster
	 */
	public List<Integer> getTestroster() {
		return testroster;
	}

	/**
	 * @param testroster the testroster to set
	 */
	public void setTestroster(List<Integer> testroster) {
		this.testroster = testroster;
	}

	/**
	 * @return the jobId
	 */
	public Integer getJobId() {
		return jobId;
	}

	/**
	 * @param jobId the jobId to set
	 */
	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	

}
