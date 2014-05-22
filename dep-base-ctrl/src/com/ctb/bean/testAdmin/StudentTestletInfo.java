package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;

/**
 * Data bean representing the contents of query to filter students for assigning testlets
 * 
 * @author Nate_Cohen
 */
public class StudentTestletInfo extends CTBBean
{
    static final long serialVersionUID = 1L;
    private Integer studentId;
    private Integer itemSetId;
    private String subject;
    private String completionStatus;
    private String testCompletionStatus;
    private String itemSetLevel;
    private String itemSetForm;
	private Integer productId;	
	private Date completionDateTime;
	private Integer ordr;
	
    /**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return Returns the completionStatus.
	 */
	public String getCompletionStatus() {
		return completionStatus;
	}
	/**
	 * @param completionStatus The completionStatus to set.
	 */
	public void setCompletionStatus(String completionStatus) {
		this.completionStatus = completionStatus;
	}
	/**
	 * @param testCompletionStatus The testCompletionStatus to set.
	 */
	public void setTestCompletionStatus(String testCompletionStatus) {
		this.testCompletionStatus = testCompletionStatus;
	}
	/**
	 * @return Returns the testCompletionStatus.
	 */
	public String getTestCompletionStatus() {
		return testCompletionStatus;
	}

	/**
	 * @return Returns the itemSetId.
	 */
	public Integer getItemSetId() {
		return itemSetId;
	}
	/**
	 * @param itemSetId The itemSetId to set.
	 */
	public void setItemSetId(Integer itemSetId) {
		this.itemSetId = itemSetId;
	}
	/**
	 * @return the studentId
	 */
	public Integer getStudentId() {
		return studentId;
	}
	/**
	 * @param studentId the studentId to set
	 */
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	/**
	 * @return the itemSetLevel
	 */
	public String getItemSetLevel() {
		return itemSetLevel;
	}
	/**
	 * @param itemSetLevel the itemSetLevel to set
	 */
	public void setItemSetLevel(String itemSetLevel) {
		this.itemSetLevel = itemSetLevel;
	}
	/**
	 * @return the itemSetForm
	 */
	public String getItemSetForm() {
		return itemSetForm;
	}
	/**
	 * @param itemSetForm the itemSetForm to set
	 */
	public void setItemSetForm(String itemSetForm) {
		this.itemSetForm = itemSetForm;
	}
	/**
	 * @return the productId
	 */
	public Integer getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	/**
	 * @return the ordr
	 */
	public Integer getOrdr() {
		return ordr;
	}
	/**
	 * @param ordr the ordr to set
	 */
	public void setOrdr(Integer ordr) {
		this.ordr = ordr;
	}
	/**
	 * @return Returns the completionDateTime.
	 */
	public Date getCompletionDateTime() {
		return completionDateTime;
	}
	/**
	 * @param completionDateTime the completionDateTime to set
	 */
	public void setCompletionDateTime(Date completionDateTime) {
		this.completionDateTime = completionDateTime;
	}
} 
