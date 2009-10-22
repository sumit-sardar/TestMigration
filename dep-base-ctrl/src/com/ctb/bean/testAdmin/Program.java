package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;
import java.util.Date;

/**
 * Data bean representing the contents of the OAS.Program table
 * 
 * @author Mallik_Korivi
 */
public class Program extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    private Integer customerId;
    private Integer productId;
    private Integer programId;
    private String programName;
    private Date programStartDate;
    private Date programEndDate;
    private String normsGroup;
    private String normsYear;
    private Date createdDateTime;
    private Date updatedDateTime;
    
     /**
	 * @return Returns the customerId.
	 */
	public Integer getCustomerId() {
		return customerId;
	}
    /**
	 * @param customerId. The customerId to set.
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
    /**
	 * @return Returns the normsGroup.
	 */
	public String getNormsGroup() {
		return normsGroup;
	}
    /**
	 * @param normsGroup. The normsGroup to set.
	 */
	public void setNormsGroup(String normsGroup) {
		this.normsGroup = normsGroup;
	}
    /**
	 * @return Returns the normsYear.
	 */
	public String getNormsYear() {
		return normsYear;
	}
    /**
	 * @param normsYear. The normsYear to set.
	 */
	public void setNormsYear(String normsYear) {
		this.normsYear = normsYear;
	}
    /**
	 * @return Returns the productId.
	 */
	public Integer getProductId() {
		return productId;
	}
    /**
	 * @param  productId. The productId to set.
	 */
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
    /**
	 * @return Returns the programEndDate.
	 */
	public Date getProgramEndDate() {
		return programEndDate;
	}
    /**
	 * @param  programEndDate. The programEndDate to set.
	 */
	public void setProgramEndDate(Date programEndDate) {
		this.programEndDate = programEndDate;
	}
    /**
	 * @return Returns the 
	 */
	public Integer getProgramId() {
		return programId;
	}
    /**
	 * @param programId. The programId to set.
	 */
	public void setProgramId(Integer programId) {
		this.programId = programId;
	}
    /**
	 * @return Returns the programName.
	 */
	public String getProgramName() {
		return programName;
	}
    /**
	 * @param   programName. The programName to set.
	 */
	public void setProgramName(String programName) {
		this.programName = programName;
	}
    /**
	 * @return Returns the programStartDate.
	 */
	public Date getProgramStartDate() {
		return programStartDate;
	}
    /**
	 * @param programStartDate. The programStartDate to set.
	 */
	public void setProgramStartDate(Date programStartDate) {
		this.programStartDate = programStartDate;
	}
	/**
	 * @return Returns the createdDateTime.
	 */
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	/**
	 * @param createdDateTime. The createdDateTime to set.
	 */
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	/**
	 * @return Returns the updatedDateTime.
	 */
	public Date getUpdatedDateTime() {
		return updatedDateTime;
	}
	/**
	 * @param updatedDateTime. The updatedDateTime to set.
	 */
	public void setUpdatedDateTime(Date updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}  
    
} 
