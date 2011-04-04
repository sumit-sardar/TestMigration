package com.ctb.bean.testAdmin;

import java.util.Date;

import com.ctb.bean.CTBBean;

public class ResponsePoints extends CTBBean {

	private static final long serialVersionUID = 1L;
	
	private Integer itemId;
	private Integer responseId;
	private Integer datapointId;
	private Integer point;
	private Integer createdBy;
	private Date creattionDate;
	/**
	 * @return the itemId
	 */
	public Integer getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return the responseId
	 */
	public Integer getResponseId() {
		return responseId;
	}
	/**
	 * @param responseId the responseId to set
	 */
	public void setResponseId(Integer responseId) {
		this.responseId = responseId;
	}
	/**
	 * @return the datapointId
	 */
	public Integer getDatapointId() {
		return datapointId;
	}
	/**
	 * @param datapointId the datapointId to set
	 */
	public void setDatapointId(Integer datapointId) {
		this.datapointId = datapointId;
	}
	/**
	 * @return the point
	 */
	public Integer getPoint() {
		return point;
	}
	/**
	 * @param point the point to set
	 */
	public void setPoint(Integer point) {
		this.point = point;
	}
	/**
	 * @return the createdBy
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the creattionDate
	 */
	public Date getCreattionDate() {
		return creattionDate;
	}
	/**
	 * @param creattionDate the creattionDate to set
	 */
	public void setCreattionDate(Date creattionDate) {
		this.creattionDate = creattionDate;
	}
	
	
	
	
	

}
