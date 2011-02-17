package com.ctb.hibernate.persist;

/**
 * @hibernate.class table="AUX_ITEM_RESPONSE"
 * persister="com.ctb.hibernate.CTBEntityPersister"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class AuxItemResponseRecord {
    private Long auxItemResponseId;
    private Long createdBy;
    private java.util.Date createdDateTime;
    private String itemId;
    private Long itemSetId;
    private String response;
    private Long responseElapsedTime;
    private String responseMethod;
    private Long responseSeqNum;
    private Long testRosterId;
    private Long updatedBy;
    private java.util.Date updatedDateTime;

    /**
     * @hibernate.id
     * generator-class="sequence"
     * column="AUX_ITEM_RESPONSE_ID" 
     * 
     * @hibernate.generator-param
     * name="sequence"
     * value="SEQ_AUX_ITEM_RESPONSE_ID"
     */
    public Long getAuxItemResponseId() {
        return auxItemResponseId;
    }

    /**
     * @hibernate.property
     * column="CREATED_BY"
     * not-null="true"
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * @hibernate.property
     * update="false"
     * column="CREATED_DATE_TIME"
     * not-null="true"
     */
    public java.util.Date getCreatedDateTime() {
        return createdDateTime;
    }

    /**
     * @hibernate.property
     * column="ITEM_ID"
     * not-null="true"
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * @hibernate.property
     * column="ITEM_SET_ID"
     * not-null="true"
     */
    public Long getItemSetId() {
        return itemSetId;
    }

    /**
     * @hibernate.property
     * column="RESPONSE"
     * not-null="false"
     */
    public String getResponse() {
        return response;
    }

    /**
     * @hibernate.property
     * column="RESPONSE_ELAPSED_TIME"
     * not-null="false"
     */
    public Long getResponseElapsedTime() {
        return responseElapsedTime;
    }

    /**
     * @hibernate.property
     * column="RESPONSE_METHOD"
     * not-null="false"
     */
    public String getResponseMethod() {
        return responseMethod;
    }

    /**
     * @hibernate.property
     * column="RESPONSE_SEQ_NUM"
     * not-null="false"
     */
    public Long getResponseSeqNum() {
        return responseSeqNum;
    }

    /**
     * @hibernate.property
     * column="TEST_ROSTER_ID"
     * not-null="true"
     */
    public Long getTestRosterId() {
        return testRosterId;
    }

    /**
     * @hibernate.property
     * column="UPDATED_BY"
     * not-null="false"
     */
    public Long getUpdatedBy() {
        return updatedBy;
    }

    /**
     * @hibernate.property
     * column="UPDATED_DATE_TIME"
     * not-null="false"
     */
    public java.util.Date getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setAuxItemResponseId(Long auxItemResponseId) {
        this.auxItemResponseId = auxItemResponseId;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedDateTime(java.util.Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setItemSetId(Long itemSetId) {
        this.itemSetId = itemSetId;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setResponseElapsedTime(Long responseElapsedTime) {
        this.responseElapsedTime = responseElapsedTime;
    }

    public void setResponseMethod(String responseMethod) {
        this.responseMethod = responseMethod;
    }

    public void setResponseSeqNum(Long responseSeqNum) {
        this.responseSeqNum = responseSeqNum;
    }

    public void setTestRosterId(Long testRosterId) {
        this.testRosterId = testRosterId;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUpdatedDateTime(java.util.Date updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

}
