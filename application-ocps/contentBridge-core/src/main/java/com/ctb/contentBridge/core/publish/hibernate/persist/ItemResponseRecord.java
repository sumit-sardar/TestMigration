package com.ctb.contentBridge.core.publish.hibernate.persist;

/**
 * @hibernate.class table="ITEM_RESPONSE"
 * persister="com.ctb.hibernate.CTBEntityPersister"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class ItemResponseRecord {
    private Long createdBy;
    private java.util.Date createdDateTime;
    private String extAnswerChoiceId;
    private String itemId;
    private Long itemResponseId;
    private Long itemSetId;
    private String response;
    private Long responseElapsedTime;
    private String responseMethod;
    private Long responseSeqNum;
    private String studentMarked;
    private Long testRosterId;

    /**
     * @hibernate.property
     * column="CREATED_BY"
     * not-null="false"
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
     * column="EXT_ANSWER_CHOICE_ID"
     * not-null="false"
     */
    public String getExtAnswerChoiceId() {
        return extAnswerChoiceId;
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
     * @hibernate.id
     * generator-class="sequence"
     * column="ITEM_RESPONSE_ID" 
     * 
     * @hibernate.generator-param
     * name="sequence"
     * value="SEQ_ITEM_RESPONSE_ID"
     */
    public Long getItemResponseId() {
        return itemResponseId;
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
     * not-null="true"
     */
    public String getResponseMethod() {
        return responseMethod;
    }

    /**
     * @hibernate.property
     * column="RESPONSE_SEQ_NUM"
     * not-null="true"
     */
    public Long getResponseSeqNum() {
        return responseSeqNum;
    }

    /**
     * @hibernate.property
     * column="STUDENT_MARKED"
     * not-null="false"
     */
    public String getStudentMarked() {
        return studentMarked;
    }

    /**
     * @hibernate.property
     * column="TEST_ROSTER_ID"
     * not-null="true"
     */
    public Long getTestRosterId() {
        return testRosterId;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedDateTime(java.util.Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public void setExtAnswerChoiceId(String extAnswerChoiceId) {
        this.extAnswerChoiceId = extAnswerChoiceId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setItemResponseId(Long itemResponseId) {
        this.itemResponseId = itemResponseId;
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

    public void setStudentMarked(String studentMarked) {
        this.studentMarked = studentMarked;
    }

    public void setTestRosterId(Long testRosterId) {
        this.testRosterId = testRosterId;
    }

}
