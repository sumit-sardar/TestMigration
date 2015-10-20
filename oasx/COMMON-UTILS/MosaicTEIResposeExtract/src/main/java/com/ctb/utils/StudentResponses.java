package com.ctb.utils;

import java.io.Serializable;

public class StudentResponses implements Serializable {

	private static final long serialVersionUID = 1L;
	private String rosterid;
	private String dasItemid;
	private String oasItemId;
	private String PEId;
	private String clobResponse;
	private String itemOrder;
	private String itemSerIdTD;
	private String answered;
	
	/**
	 * @return the rosterid
	 */
	public String getRosterid() {
		return rosterid;
	}
	/**
	 * @param rosterid the rosterid to set
	 */
	public void setRosterid(String rosterid) {
		this.rosterid = rosterid;
	}
	/**
	 * @return the dasItemid
	 */
	public String getDasItemid() {
		return dasItemid;
	}
	/**
	 * @param dasItemid the dasItemid to set
	 */
	public void setDasItemid(String dasItemid) {
		this.dasItemid = dasItemid;
	}
	/**
	 * @return the oasItemId
	 */
	public String getOasItemId() {
		return oasItemId;
	}
	/**
	 * @param oasItemId the oasItemId to set
	 */
	public void setOasItemId(String oasItemId) {
		this.oasItemId = oasItemId;
	}
	/**
	 * @return the pEId
	 */
	public String getPEId() {
		return PEId;
	}
	/**
	 * @param pEId the pEId to set
	 */
	public void setPEId(String pEId) {
		PEId = pEId;
	}
	/**
	 * @return the clobResponse
	 */
	public String getClobResponse() {
		return clobResponse;
	}
	/**
	 * @param clobResponse the clobResponse to set
	 */
	public void setClobResponse(String clobResponse) {
		this.clobResponse = clobResponse;
	}
	/**
	 * @return the itemOrder
	 */
	public String getItemOrder() {
		return itemOrder;
	}
	/**
	 * @param itemOrder the itemOrder to set
	 */
	public void setItemOrder(String itemOrder) {
		this.itemOrder = itemOrder;
	}
	/**
	 * @return the itemSerIdTD
	 */
	public String getItemSerIdTD() {
		return itemSerIdTD;
	}
	/**
	 * @param itemSerIdTD the itemSerIdTD to set
	 */
	public void setItemSerIdTD(String itemSerIdTD) {
		this.itemSerIdTD = itemSerIdTD;
	}
	/**
	 * @return the answered
	 */
	public String getAnswered() {
		return answered;
	}
	/**
	 * @param answered the answered to set
	 */
	public void setAnswered(String answered) {
		this.answered = answered;
	}
}
