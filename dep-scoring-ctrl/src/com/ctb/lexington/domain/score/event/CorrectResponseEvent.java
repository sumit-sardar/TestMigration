package com.ctb.lexington.domain.score.event;

public class CorrectResponseEvent extends ResponseEvent {
    private Integer itemResponseId;
    private Integer itemSortOrder;
    private Integer responseElapsedTime;
    private Integer responseSeqNum;
    private String extAnswerChoiceId;
    private String response;
    private String responseMethod;
    private boolean studentMarked;

    public CorrectResponseEvent(final Long testRosterId, final String itemId,
            final Integer itemSetId) {
        super(testRosterId, itemId, itemSetId);
    }

    public CorrectResponseEvent(final ResponseReceivedEvent event) {
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

    public Integer getItemResponseId() {
        return itemResponseId;
    }

    public Integer getItemSortOrder() {
        return itemSortOrder;
    }

    public String getResponse() {
        return response;
    }

    public Integer getResponseElapsedTime() {
        return responseElapsedTime;
    }

    public String getResponseMethod() {
        return responseMethod;
    }

    public Integer getResponseSeqNum() {
        return responseSeqNum;
    }

    public boolean isStudentMarked() {
        return studentMarked;
    }
}