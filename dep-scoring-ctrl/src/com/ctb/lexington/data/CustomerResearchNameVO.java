package com.ctb.lexington.data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author  jbecker
 * @version
 */
public class CustomerResearchNameVO implements Serializable, Cloneable
{
	private Integer customerResearchNameId = null;
	private Integer customerId = null;
	private String dataName = null;
	private Date createdDatetime = null;
	
	public void setCustomerResearchNameId(Integer customerResearchNameId_){
		this.customerResearchNameId = customerResearchNameId_;
	}
	public void setCustomerId(Integer customerId_){
		this.customerId = customerId_;
	}
	public void setDataName(String dataName_){
		this.dataName = dataName_;
	}
	public void setCreatedDatetime(Date createdDatetime_){
		this.createdDatetime = createdDatetime_;
	}
	public Integer getCustomerResearchNameId(){
		return this.customerResearchNameId;
	}
	public Integer getCustomerId(){
		return this.customerId;
	}
	public String getDataName(){
		return this.dataName;
	}
	public Date getCreatedDatetime(){
		return this.createdDatetime;
	}
}
