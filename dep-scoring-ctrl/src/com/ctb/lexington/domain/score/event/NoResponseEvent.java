package com.ctb.lexington.domain.score.event;

public class NoResponseEvent extends ResponseEvent {
    private final Integer itemSortOrder;
    private final Integer responseElapsedTime;
    private final Integer responseSeqNum;

    public NoResponseEvent(final ResponseReceivedEvent event) {
        super(event.getTestRosterId(), event.getItemId(), event.getItemSetId());
        this.itemSortOrder = event.itemSortOrder;
        this.responseElapsedTime = event.responseElapsedTime;
        this.responseSeqNum = event.responseSeqNum;
    }

    public Integer getItemSortOrder() {
        return itemSortOrder;
    }

    public Integer getResponseElapsedTime() {
        return responseElapsedTime;
    }

    public Integer getResponseSeqNum() {
        return responseSeqNum;
    }
}