package com.ctb.lexington.domain.score.event;

import com.ctb.lexington.domain.teststructure.ProductType;

public class AssessmentStartedEvent extends AssessmentEvent {
    private Integer studentId;
    private String studentFirstName;
    private String studentMiddleName;
    private String studentLastName;
    private Integer testAdminId;
    private Integer parentItemSetId;
    private String testDisplayName;
    private Integer currentItemSetId;
    private final Integer productId;
    private final ProductType productType;
    private final String grade;
    private String productTypeValue;

    public AssessmentStartedEvent(final Long testRosterId, final Integer productId,
            final String productType, final String grade) {
        super(testRosterId);
        if (null == testRosterId)
            throw new NullPointerException("Cannot start an assessment for null testRosterId");
        if (null == productId)
            throw new NullPointerException("Cannot start an assessment for null productId");
        if (null == productType)
            throw new NullPointerException("Cannot start an assessment for null productType");
        // HACKTAG: Shouldnt have biz logic here, but i need to move on...
        if (null == grade && !ProductType.isTabe(productType) && !ProductType.isSofa(productType))
            throw new NullPointerException("Cannot start an assessment for null grade for non SOFA and non TABE test");

        this.productId = productId;
        this.productTypeValue = productType;
        //TODO: pass in ProductType enum
        this.productType = (productType == null ? null : ProductType.getByCode(productType));
        this.grade = grade;
    }

    /**
     * @return Returns the currentItemSetId.
     */
    public Integer getCurrentItemSetId() {
        return currentItemSetId;
    }

    /**
     * @param currentItemSetId The currentItemSetId to set.
     */
    public void setCurrentItemSetId(final Integer currentItemSetId) {
        this.currentItemSetId = currentItemSetId;
    }

    /**
     * @return Returns the parentItemSetId.
     */
    public Integer getParentItemSetId() {
        return parentItemSetId;
    }

    /**
     * @param parentItemSetId The parentItemSetId to set.
     */
    public void setParentItemSetId(final Integer parentItemSetId) {
        this.parentItemSetId = parentItemSetId;
    }

    /**
     * @return Returns the productId.
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * @return Returns the productType.
     */
    public ProductType getProductType() {
        return productType;
    }

    /**
     * @return Returns the studentFirstName.
     */
    public String getStudentFirstName() {
        return studentFirstName;
    }

    /**
     * @param studentFirstName The studentFirstName to set.
     */
    public void setStudentFirstName(final String studentFirstName) {
        this.studentFirstName = studentFirstName;
    }

    /**
     * @return Returns the studentId.
     */
    public Integer getStudentId() {
        return studentId;
    }

    /**
     * @param studentId The studentId to set.
     */
    public void setStudentId(final Integer studentId) {
        this.studentId = studentId;
    }

    /**
     * @return Returns the studentLastName.
     */
    public String getStudentLastName() {
        return studentLastName;
    }

    /**
     * @param studentLastName The studentLastName to set.
     */
    public void setStudentLastName(final String studentLastName) {
        this.studentLastName = studentLastName;
    }

    /**
     * @return Returns the studentMiddleName.
     */
    public String getStudentMiddleName() {
        return studentMiddleName;
    }

    /**
     * @param studentMiddleName The studentMiddleName to set.
     */
    public void setStudentMiddleName(final String studentMiddleName) {
        this.studentMiddleName = studentMiddleName;
    }

    /**
     * @return Returns the testAdminId.
     */
    public Integer getTestAdminId() {
        return testAdminId;
    }

    /**
     * @param testAdminId The testAdminId to set.
     */
    public void setTestAdminId(final Integer testAdminId) {
        this.testAdminId = testAdminId;
    }

    /**
     * @return Returns the testDisplayName.
     */
    public String getTestDisplayName() {
        return testDisplayName;
    }

    /**
     * @param testDisplayName The testDisplayName to set.
     */
    public void setTestDisplayName(final String testDisplayName) {
        this.testDisplayName = testDisplayName;
    }

    /**
     * @return Returns the grade.
     */
    public String getGrade() {
        return grade;
    }

    /**
     * Checks of the given <var>o </var> is equals to this <code>AssessmentStartedEvent</code>
     * based on equality of the test roster ids.
     * 
     * @param o the other object
     */
    public boolean equals(final Object o) {
        if (this == o)
            return true;

        if (null == o)
            return false;

        if (! (o instanceof AssessmentStartedEvent))
            return false;

        final AssessmentStartedEvent that = (AssessmentStartedEvent) o;

        if (null == this.getTestRosterId())
            return null == that.getTestRosterId();

        return (getTestRosterId().equals(that.getTestRosterId()));
    }

	public String getProductTypeValue() {
		return productTypeValue;
	}

	public void setProductTypeValue(String productTypeValue) {
		this.productTypeValue = productTypeValue;
	}
}