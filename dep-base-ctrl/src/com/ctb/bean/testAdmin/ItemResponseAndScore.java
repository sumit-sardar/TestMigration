package com.ctb.bean.testAdmin;

import java.sql.Clob;
import java.util.Comparator;

import oracle.sql.CLOB;

import com.ctb.bean.CTBBean;

public class ItemResponseAndScore extends CTBBean {
	static final long serialVersionUID = 1L;

	private String itemId;
	private Integer itemOrder;
	private String rawScore;
	private String possibleScore;
	private String response;
	private String itemType;
	private String itemAnswerArea;
	private Integer itemSetIdTD;
	private String itemSetNameTD;
	private String completionStatusTD;
	private Integer itemSetIdTS;
	private String contentDomain;
	private Clob crResponse;
	private String pdfResponse;
	
	
	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return the itemOrder
	 */
	public Integer getItemOrder() {
		return itemOrder;
	}
	/**
	 * @param itemOrder the itemOrder to set
	 */
	public void setItemOrder(Integer itemOrder) {
		this.itemOrder = itemOrder;
	}
	/**
	 * @return the rawScore
	 */
	public String getRawScore() {
		return rawScore;
	}
	/**
	 * @param rawScore the rawScore to set
	 */
	public void setRawScore(String rawScore) {
		this.rawScore = rawScore;
	}
	/**
	 * @return the possibleScore
	 */
	public String getPossibleScore() {
		return possibleScore;
	}
	/**
	 * @param possibleScore the possibleScore to set
	 */
	public void setPossibleScore(String possibleScore) {
		this.possibleScore = possibleScore;
	}
	/**
	 * @return the response
	 */
	public String getResponse() {
		return response;
	}
	/**
	 * @param response the response to set
	 */
	public void setResponse(String response) {
		this.response = response;
	}
	/**
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
	}
	/**
	 * @param itemType the itemType to set
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	/**
	 * @return the itemAnswerArea
	 */
	public String getItemAnswerArea() {
		return itemAnswerArea;
	}
	/**
	 * @param itemAnswerArea the itemAnswerArea to set
	 */
	public void setItemAnswerArea(String itemAnswerArea) {
		this.itemAnswerArea = itemAnswerArea;
	}
	/**
	 * @return the itemSetIdTD
	 */
	public Integer getItemSetIdTD() {
		return itemSetIdTD;
	}
	/**
	 * @param itemSetIdTD the itemSetIdTD to set
	 */
	public void setItemSetIdTD(Integer itemSetIdTD) {
		this.itemSetIdTD = itemSetIdTD;
	}
	/**
	 * @return the itemSetNameTD
	 */
	public String getItemSetNameTD() {
		return itemSetNameTD;
	}
	/**
	 * @param itemSetNameTD the itemSetNameTD to set
	 */
	public void setItemSetNameTD(String itemSetNameTD) {
		this.itemSetNameTD = itemSetNameTD;
	}
	/**
	 * @return the completionStatusTD
	 */
	public String getCompletionStatusTD() {
		return completionStatusTD;
	}
	/**
	 * @param completionStatusTD the completionStatusTD to set
	 */
	public void setCompletionStatusTD(String completionStatusTD) {
		this.completionStatusTD = completionStatusTD;
	}
	/**
	 * @return the itemSetIdTS
	 */
	public Integer getItemSetIdTS() {
		return itemSetIdTS;
	}
	/**
	 * @param itemSetIdTS the itemSetIdTS to set
	 */
	public void setItemSetIdTS(Integer itemSetIdTS) {
		this.itemSetIdTS = itemSetIdTS;
	}
	/**
	 * @return the contentDomain
	 */
	public String getContentDomain() {
		return contentDomain;
	}
	/**
	 * @param contentDomain the contentDomain to set
	 */
	public void setContentDomain(String contentDomain) {
		this.contentDomain = contentDomain;
	}
	/**
	 * @return the crResponse
	 */
	public Clob getCrResponse() {
		return crResponse;
	}
	/**
	 * @param crResponse the crResponse to set
	 */
	public void setCrResponse(Clob crResponse) {
		this.crResponse = crResponse;
	}
	/**
	 * @return the pdfResponse
	 */
	public String getPdfResponse() {
		return pdfResponse;
	}
	/**
	 * @param pdfResponse the pdfResponse to set
	 */
	public void setPdfResponse(String pdfResponse) {
		this.pdfResponse = pdfResponse;
	}
}
