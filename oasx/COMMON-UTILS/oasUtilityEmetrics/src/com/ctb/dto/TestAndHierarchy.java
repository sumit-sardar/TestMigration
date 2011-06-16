package com.ctb.dto;

import org.ffpojo.metadata.positional.annotation.PositionalField;
import org.ffpojo.metadata.positional.annotation.PositionalRecord;




@PositionalRecord
public class TestAndHierarchy {
	
	private String modelLevel;
	
	private String organizationId;
	
	private String elementNameA;
	
	private String elementStructureLevelA;
	
	private String elementNumberA;
	
	private String elementSpecialCodesA;

	private Integer customerId;

	private String remainderOfElementSpecialCodesA;
	

	
/*	@Override
    public String toString(){
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append(getModelLevel()).append(organizationId).append(elementNameA).append(elementStructureLevelA).append(elementNumberA).append(elementSpecialCodesA);
    	
    	
    	
    	return sb.toString();
    }*/

	/**
	 * @return the modelLevel
	 */
	@PositionalField(initialPosition = 1, finalPosition =1)
	public String getModelLevel() {
		return modelLevel;
	}

	/**
	 * @param modelLevel
	 *            the modelLevel to set
	 */
	public void setModelLevel(String modelLevel) {
		this.modelLevel = modelLevel;
	}

	/**
	 * @return the organizationId
	 */
	@PositionalField(initialPosition = 2, finalPosition =11)
	public String getOrganizationId() {
		return organizationId;
	}

	/**
	 * @param organizationId
	 *            the organizationId to set
	 */
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * @return the elementNameA
	 */
	@PositionalField(initialPosition = 12, finalPosition =41)
	public String getElementNameA() {
		return elementNameA;
	}

	/**
	 * @param elementNameA
	 *            the elementNameA to set
	 */
	public void setElementNameA(String elementNameA) {
		this.elementNameA = elementNameA;
	}

	/**
	 * @return the elementStructureLevelA
	 */
	@PositionalField(initialPosition = 42, finalPosition =43)
	public String getElementStructureLevelA() {
		return elementStructureLevelA;
	}

	/**
	 * @param elementStructureLevelA
	 *            the elementStructureLevelA to set
	 */
	public void setElementStructureLevelA(String elementStructureLevelA) {
		this.elementStructureLevelA = elementStructureLevelA;
	}

	/**
	 * @return the elementNumberA
	 */
	@PositionalField(initialPosition = 44, finalPosition =50)
	public String getElementNumberA() {
		return elementNumberA;
	}

	/**
	 * @param elementNumberA
	 *            the elementNumberA to set
	 */
	public void setElementNumberA(String elementNumberA) {
	
		this.elementNumberA = elementNumberA;
	}

	/**
	 * @return the elementSpecialCodesA
	 */
	@PositionalField(initialPosition = 51, finalPosition =60)
	public String getElementSpecialCodesA() {
		return elementSpecialCodesA;
	}

	/**
	 * @param elementSpecialCodesA
	 *            the elementSpecialCodesA to set
	 */
	public void setElementSpecialCodesA(String elementSpecialCodesA) {
		this.elementSpecialCodesA = elementSpecialCodesA;
	}

	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		
		this.customerId = customerId;
	}

	/**
	 * @return the remainderOfElementSpecialCodesA
	 */
	public String getRemainderOfElementSpecialCodesA() {
		return remainderOfElementSpecialCodesA;
	}

	/**
	 * @param remainderOfElementSpecialCodesA the remainderOfElementSpecialCodesA to set
	 */
	public void setRemainderOfElementSpecialCodesA(String remainderOfElementSpecialCodesA) {
		this.remainderOfElementSpecialCodesA = remainderOfElementSpecialCodesA;
	}

}
