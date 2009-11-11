package com.ctb.lexington.domain.score.event;

public class IncorrectResponseEvent extends ResponseEvent {
    private Integer itemResponseId;
    private Integer itemSortOrder;
    private Integer responseElapsedTime;
    private Integer responseSeqNum;
    private String extAnswerChoiceId;
    private String response;
    private String responseMethod;
    private boolean studentMarked;

    public IncorrectResponseEvent(final Long testRosterId, final String itemId,
            final Integer itemSetId, final Integer itemResponseId) {
        super(testRosterId, itemId, itemSetId);
    }

    public IncorrectResponseEvent(final Long testRosterId, final String itemId,
            final Integer itemSetId) {
        this(testRosterId, itemId, itemSetId, new Integer(0));
    }

    public IncorrectResponseEvent(final ResponseReceivedEvent event) {
        super(event.getTestRosterId(), event.getItemId(), event.getItemSetId());
        this.itemResponseId = event.itemResponseId;
        this.itemSortOrder = event.itemSortOrder;
        this.responseElapsedTime = event.responseElapsedTime;
        this.responseSeqNum = event.responseSeqNum;
        this.extAnswerChoiceId = event.extAnswerChoiceId;
        this.response = event.response;
        this.responseMethod = event.responseMethod;
        this.studentMarked = event.studentMarked;
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
}