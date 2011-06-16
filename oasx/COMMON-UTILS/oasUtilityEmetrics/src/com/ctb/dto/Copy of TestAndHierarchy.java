package com.ctb.dto;

import java.util.Date;

import org.coury.jfilehelpers.annotations.FieldFixedLength;
import org.coury.jfilehelpers.annotations.FixedLengthRecord;

@FixedLengthRecord()
public class Copy<T extends String, V extends Number, S extends Date> {
	@FieldFixedLength(1)
	private T modelLevel;
	@FieldFixedLength(10)
	private T organizationId;
	@FieldFixedLength(30)
	private T elementNameA;
	@FieldFixedLength(2)
	private T elementStructureLevelA;
	@FieldFixedLength(7)
	private T elementNumberA;
	@FieldFixedLength(10)
	private T elementSpecialCodesA;
	@FieldFixedLength(8)
	private V customerId;
	@FieldFixedLength(8)
	private T remainderOfElementSpecialCodesA;
	@FieldFixedLength(2)
	private T grade;
	@FieldFixedLength(30)
	private T city;
	@FieldFixedLength(2)
	private T state;
	@FieldFixedLength(30)
	private T elementNameB;
	@FieldFixedLength(2)
	private T elementStructureLevelB;
	@FieldFixedLength(7)
	private T elementNumberB;
	@FieldFixedLength(10)
	private T elementSpecialCodesB;
	@FieldFixedLength(8)
	private T schoolId;
	@FieldFixedLength(8)
	private T remainderOfElementSpecialCodesB;
	@FieldFixedLength(30)
	private T elementNameC;
	@FieldFixedLength(2)
	private T elementStructureLevelC;
	@FieldFixedLength(7)
	private T elementNumberC;
	@FieldFixedLength(26)
	private T elementSpecialCodesC;
	@FieldFixedLength(30)
	private T testName;
	@FieldFixedLength(2)
	private T testForm;
	@FieldFixedLength(2)
	private T testLevel;
	@FieldFixedLength(6)
	private S testDate;
	@FieldFixedLength(1)
	private T scoringType;
	@FieldFixedLength(16)
	private T unused;

	private T itemId;
	private S createdDateTime;

	@Override
    public String toString(){
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append(getModelLevel()).append(organizationId).append(elementNameA).append(elementStructureLevelA).append(elementNumberA).append(elementSpecialCodesA);
    	
    	
    	
    	return sb.toString();
    }

	/**
	 * @return the modelLevel
	 */
	public T getModelLevel() {
		return modelLevel;
	}

	/**
	 * @param modelLevel
	 *            the modelLevel to set
	 */
	public void setModelLevel(T modelLevel) {
		this.modelLevel = modelLevel;
	}

	/**
	 * @return the organizationId
	 */
	public T getOrganizationId() {
		return organizationId;
	}

	/**
	 * @param organizationId
	 *            the organizationId to set
	 */
	public void setOrganizationId(T organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * @return the elementNameA
	 */
	public T getElementNameA() {
		return elementNameA;
	}

	/**
	 * @param elementNameA
	 *            the elementNameA to set
	 */
	public void setElementNameA(T elementNameA) {
		this.elementNameA = elementNameA;
	}

	/**
	 * @return the elementStructureLevelA
	 */
	public T getElementStructureLevelA() {
		return elementStructureLevelA;
	}

	/**
	 * @param elementStructureLevelA
	 *            the elementStructureLevelA to set
	 */
	public void setElementStructureLevelA(T elementStructureLevelA) {
		this.elementStructureLevelA = elementStructureLevelA;
	}

	/**
	 * @return the elementNumberA
	 */
	public T getElementNumberA() {
		return elementNumberA;
	}

	/**
	 * @param elementNumberA
	 *            the elementNumberA to set
	 */
	public void setElementNumberA(T elementNumberA) {
		this.elementNumberA = elementNumberA;
	}

	/**
	 * @return the elementSpecialCodesA
	 */
	public T getElementSpecialCodesA() {
		return elementSpecialCodesA;
	}

	/**
	 * @param elementSpecialCodesA
	 *            the elementSpecialCodesA to set
	 */
	public void setElementSpecialCodesA(T elementSpecialCodesA) {
		this.elementSpecialCodesA = elementSpecialCodesA;
	}

	/**
	 * @return the customerId
	 */
	public V getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(V customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the remainderOfElementSpecialCodesA
	 */
	public T getRemainderOfElementSpecialCodesA() {
		return remainderOfElementSpecialCodesA;
	}

	/**
	 * @param remainderOfElementSpecialCodesA
	 *            the remainderOfElementSpecialCodesA to set
	 */
	public void setRemainderOfElementSpecialCodesA(
			T remainderOfElementSpecialCodesA) {
		this.remainderOfElementSpecialCodesA = remainderOfElementSpecialCodesA;
	}

	/**
	 * @return the grade
	 */
	public T getGrade() {
		return grade;
	}

	/**
	 * @param grade
	 *            the grade to set
	 */
	public void setGrade(T grade) {
		this.grade = grade;
	}

	/**
	 * @return the city
	 */
	public T getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(T city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public T getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(T state) {
		this.state = state;
	}

	/**
	 * @return the elementNameB
	 */
	public T getElementNameB() {
		return elementNameB;
	}

	/**
	 * @param elementNameB
	 *            the elementNameB to set
	 */
	public void setElementNameB(T elementNameB) {
		this.elementNameB = elementNameB;
	}

	/**
	 * @return the elementStructureLevelB
	 */
	public T getElementStructureLevelB() {
		return elementStructureLevelB;
	}

	/**
	 * @param elementStructureLevelB
	 *            the elementStructureLevelB to set
	 */
	public void setElementStructureLevelB(T elementStructureLevelB) {
		this.elementStructureLevelB = elementStructureLevelB;
	}

	/**
	 * @return the elementNumberB
	 */
	public T getElementNumberB() {
		return elementNumberB;
	}

	/**
	 * @param elementNumberB
	 *            the elementNumberB to set
	 */
	public void setElementNumberB(T elementNumberB) {
		this.elementNumberB = elementNumberB;
	}

	/**
	 * @return the elementSpecialCodesB
	 */
	public T getElementSpecialCodesB() {
		return elementSpecialCodesB;
	}

	/**
	 * @param elementSpecialCodesB
	 *            the elementSpecialCodesB to set
	 */
	public void setElementSpecialCodesB(T elementSpecialCodesB) {
		this.elementSpecialCodesB = elementSpecialCodesB;
	}

	/**
	 * @return the schoolId
	 */
	public T getSchoolId() {
		return schoolId;
	}

	/**
	 * @param schoolId
	 *            the schoolId to set
	 */
	public void setSchoolId(T schoolId) {
		this.schoolId = schoolId;
	}

	/**
	 * @return the remainderOfElementSpecialCodesB
	 */
	public T getRemainderOfElementSpecialCodesB() {
		return remainderOfElementSpecialCodesB;
	}

	/**
	 * @param remainderOfElementSpecialCodesB
	 *            the remainderOfElementSpecialCodesB to set
	 */
	public void setRemainderOfElementSpecialCodesB(
			T remainderOfElementSpecialCodesB) {
		this.remainderOfElementSpecialCodesB = remainderOfElementSpecialCodesB;
	}

	/**
	 * @return the elementNameC
	 */
	public T getElementNameC() {
		return elementNameC;
	}

	/**
	 * @param elementNameC
	 *            the elementNameC to set
	 */
	public void setElementNameC(T elementNameC) {
		this.elementNameC = elementNameC;
	}

	/**
	 * @return the elementStructureLevelC
	 */
	public T getElementStructureLevelC() {
		return elementStructureLevelC;
	}

	/**
	 * @param elementStructureLevelC
	 *            the elementStructureLevelC to set
	 */
	public void setElementStructureLevelC(T elementStructureLevelC) {
		this.elementStructureLevelC = elementStructureLevelC;
	}

	/**
	 * @return the elementNumberC
	 */
	public T getElementNumberC() {
		return elementNumberC;
	}

	/**
	 * @param elementNumberC
	 *            the elementNumberC to set
	 */
	public void setElementNumberC(T elementNumberC) {
		this.elementNumberC = elementNumberC;
	}

	/**
	 * @return the elementSpecialCodesC
	 */
	public T getElementSpecialCodesC() {
		return elementSpecialCodesC;
	}

	/**
	 * @param elementSpecialCodesC
	 *            the elementSpecialCodesC to set
	 */
	public void setElementSpecialCodesC(T elementSpecialCodesC) {
		this.elementSpecialCodesC = elementSpecialCodesC;
	}

	/**
	 * @return the testName
	 */
	public T getTestName() {
		return testName;
	}

	/**
	 * @param testName
	 *            the testName to set
	 */
	public void setTestName(T testName) {
		this.testName = testName;
	}

	/**
	 * @return the testForm
	 */
	public T getTestForm() {
		return testForm;
	}

	/**
	 * @param testForm
	 *            the testForm to set
	 */
	public void setTestForm(T testForm) {
		this.testForm = testForm;
	}

	/**
	 * @return the testLevel
	 */
	public T getTestLevel() {
		return testLevel;
	}

	/**
	 * @param testLevel
	 *            the testLevel to set
	 */
	public void setTestLevel(T testLevel) {
		this.testLevel = testLevel;
	}

	/**
	 * @return the testDate
	 */
	public S getTestDate() {
		return testDate;
	}

	/**
	 * @param testDate
	 *            the testDate to set
	 */
	public void setTestDate(S testDate) {
		this.testDate = testDate;
	}

	/**
	 * @return the scoringType
	 */
	public T getScoringType() {
		return scoringType;
	}

	/**
	 * @param scoringType
	 *            the scoringType to set
	 */
	public void setScoringType(T scoringType) {
		this.scoringType = scoringType;
	}

	/**
	 * @return the unused
	 */
	public T getUnused() {
		return unused;
	}

	/**
	 * @param unused
	 *            the unused to set
	 */
	public void setUnused(T unused) {
		this.unused = unused;
	}

	/**
	 * @return the itemId
	 */
	public T getItemId() {
		return itemId;
	}

	/**
	 * @param itemId
	 *            the itemId to set
	 */
	public void setItemId(T itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the createdDateTime
	 */
	public S getCreatedDateTime() {
		return createdDateTime;
	}

	/**
	 * @param createdDateTime
	 *            the createdDateTime to set
	 */
	public void setCreatedDateTime(S createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

}
