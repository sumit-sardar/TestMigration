package com.ctb.lexington.domain.score.event;

// TODO: should this be renamed to make it clear that it is an external event? (wait until key
// entry)
public class ResponseReceivedEvent extends ResponseEvent {
    protected Integer itemResponseId;
    /** The sort order for this item response within the item set. */
    protected Integer itemSortOrder;
    protected Integer responseElapsedTime;
    protected Integer responseSeqNum;
    /** The answer choice id of the bean. */
    protected String extAnswerChoiceId;//
    protected String response;
    protected String responseMethod;
    protected boolean studentMarked;
    // CR items have points if they are scored via key entry or scan and score
    protected Integer pointsObtained;
    protected Integer conditionCodeId;
    protected String comments;

    public ResponseReceivedEvent(final Long testRosterId, final String itemId,
            final Integer itemSetId) {
        super(testRosterId, itemId, itemSetId);
    }

    /**
     * @return Returns the comment.
     */
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getExtAnswerChoiceId() {
        return extAnswerChoiceId;
    }

    public void setExtAnswerChoiceId(String extAnswerChoiceId) {
        this.extAnswerChoiceId = extAnswerChoiceId;
    }

    public Integer getItemResponseId() {
        return itemResponseId;
    }

    public void setItemResponseId(Integer itemResponseId) {
        this.itemResponseId = itemResponseId;
    }

    public Integer getItemSortOrder() {
        return itemSortOrder;
    }

    public void setItemSortOrder(Integer itemSortOrder) {
        this.itemSortOrder = itemSortOrder;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Integer getResponseElapsedTime() {
        return responseElapsedTime;
    }

    public void setResponseElapsedTime(Integer responseElapsedTime) {
        this.responseElapsedTime = responseElapsedTime;
    }

    public String getResponseMethod() {
        return responseMethod;
    }

    public void setResponseMethod(String responseMethod) {
        this.responseMethod = responseMethod;
    }

    public Integer getResponseSeqNum() {
        return responseSeqNum;
    }

    public void setResponseSeqNum(Integer responseSeqNum) {
        this.responseSeqNum = responseSeqNum;
    }

    public boolean isStudentMarked() {
        return studentMarked;
    }

    public void setStudentMarked(boolean studentMarked) {
        this.studentMarked = studentMarked;
    }

    public Integer getPointsObtained() {
        return pointsObtained;
    }

    public void setPointsObtained(Integer pointsObtained) {
        this.pointsObtained = pointsObtained;
    }

    /**
     * @return Returns the conditionCodeId.
     */
    public Integer getConditionCodeId() {
        return conditionCodeId;
    }

    /**
     * @param conditionCodeId The conditionCodeId to set.
     */
    public void setConditionCodeId(Integer conditionCodeId) {
        this.conditionCodeId = conditionCodeId;
    }

    /**
     * Checks of the given <var>o </var> is equals to this <code>ResponseReceivedEvent</code>
     * based on equality of the three primary keys, test roster id, item set id and item id.
     * 
     * @param o the other object
     */
    public boolean equals(final Object o) {
        if (null == o)
            return false;

        if (! (o instanceof ResponseReceivedEvent))
            return false;

        final ResponseReceivedEvent that = (ResponseReceivedEvent) o;

        if (null == this.getTestRosterId())
            return null == that.getTestRosterId();
        if (!getTestRosterId().equals(that.getTestRosterId()))
            return false;

        if (null == this.getItemSetId())
            return null == that.getItemSetId();
        if (!getItemSetId().equals(that.getItemSetId()))
            return false;

        if (null == this.getItemId())
            return null == that.getItemId();
        return getItemId().equals(that.getItemId());
    }
}