package com.ctb.lexington.data;

import java.io.Serializable;

/**
 * @author mnkamiya
 * @version $Id$
 */
public class ItemSetSectionVO implements Serializable {
    private int sequence;
    private String name;
    private String firstItemId;
    private int firstItemOrderNum;
    private String lastItemId;
    private int lastItemOrderNum;
    private int timeLimitForSection;
    private int themePageCount;

    public ItemSetSectionVO() {
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstItemId() {
        return firstItemId;
    }

    public void setFirstItemId(String firstItemId) {
        this.firstItemId = firstItemId;
    }

    public int getFirstItemOrderNum() {
        return firstItemOrderNum;
    }

    public void setFirstItemOrderNum(int firstItemOrderNum) {
        this.firstItemOrderNum = firstItemOrderNum;
    }

    public String getLastItemId() {
        return lastItemId;
    }

    public void setLastItemId(String lastItemId) {
        this.lastItemId = lastItemId;
    }

    public int getLastItemOrderNum() {
        return lastItemOrderNum;
    }

    public void setLastItemOrderNum(int lastItemOrderNum) {
        this.lastItemOrderNum = lastItemOrderNum;
    }

    public int getTimeLimitForSection() {
        return timeLimitForSection;
    }

    public void setTimeLimitForSection(int timeLimitForSection) {
        this.timeLimitForSection = timeLimitForSection;
    }

    public int getCountOfItemsInSection() {
        // item order starts at 1 and is inclusive, so the +1 is necessary
        return lastItemOrderNum - firstItemOrderNum + 1;
    }

    public int getThemePageCount() {
        return themePageCount;
    }

    public void setThemePageCount(int themePageCount) {
        this.themePageCount = themePageCount;
    }

    public int getClientClientObjectCount() {
        return getCountOfItemsInSection() + themePageCount;
    }
}
