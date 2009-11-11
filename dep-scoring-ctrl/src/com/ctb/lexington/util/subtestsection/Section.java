package com.ctb.lexington.util.subtestsection;

/**
 * @author mnkamiya
 * @version $Id$
 */
public class Section {
    private String name;
    private int order;
    private String firstItemId;
    private int firstItemNumber;
    private String lastItemId;
    private int lastItemNumber;
    private int timeLimit;
    private int themePageCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getFirstItemNumber() {
        return firstItemNumber;
    }

    public void setFirstItemNumber(int firstItemNumber) {
        this.firstItemNumber = firstItemNumber;
    }

    public int getLastItemNumber() {
        return lastItemNumber;
    }

    public void setLastItemNumber(int lastItemNumber) {
        this.lastItemNumber = lastItemNumber;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getFirstItemId() {
        return firstItemId;
    }

    public void setFirstItemId(String firstItemId) {
        this.firstItemId = firstItemId;
    }

    public String getLastItemId() {
        return lastItemId;
    }

    public void setLastItemId(String lastItemId) {
        this.lastItemId = lastItemId;
    }

    public int getThemePageCount() {
        return themePageCount;
    }

    public void setThemePageCount(int themePageCount) {
        this.themePageCount = themePageCount;
    }
}
