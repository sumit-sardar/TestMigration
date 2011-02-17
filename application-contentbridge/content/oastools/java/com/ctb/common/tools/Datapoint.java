package com.ctb.common.tools;


public class Datapoint {

    private String itemId;
    private long itemSetId;
    private int minPoints;
    private int maxPoints;

    public Datapoint(String itemId, long itemSetId, int minPoints, int maxPoints) {
        this.minPoints = minPoints;
        this.maxPoints = maxPoints;
        this.itemId = itemId;
        this.itemSetId = itemSetId;
    }

    public int getMinPoints() {
        return minPoints;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public String getItemId() {
        return itemId;
    }

    public long getItemSetId() {
        return itemSetId;
    }
}
