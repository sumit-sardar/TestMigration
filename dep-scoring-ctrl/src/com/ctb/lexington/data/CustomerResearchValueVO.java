package com.ctb.lexington.data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author  jbecker
 * @version
 */
public class CustomerResearchValueVO implements Serializable, Cloneable
{
	private Integer customerResearchNameId = null;
	private String dataValue = null;
	private Date createdDatetime = null;
	
	public void setCustomerResearchNameId(Integer customerResearchNameId_){
		this.customerResearchNameId = customerResearchNameId_;
	}
	public void setDataValue(String dataValue_){
		this.dataValue = dataValue_;
	}
	public void setCreatedDatetime(Date createdDatetime_){
		this.createdDatetime = createdDatetime_;
	}
	public Integer getCustomerResearchNameId(){
		return this.customerResearchNameId;
	}
	public String getDataValue(){
		return this.dataValue;
	}
	public Date getCreatedDatetime(){
		return this.createdDatetime;
	}
}
