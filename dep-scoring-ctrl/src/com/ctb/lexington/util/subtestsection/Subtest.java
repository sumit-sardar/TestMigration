package com.ctb.lexington.util.subtestsection;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * @author mnkamiya
 * @version $Id$
 */
public class Subtest {
    private String name;
    private String level;
    private int totalItems;
    private int timeLimit;
    private ArrayList sections;

    private Long itemSetId;

    public Subtest() {
        sections = new ArrayList();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public List getSections() {
        return Collections.unmodifiableList(sections);
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public Long getItemSetId() {
        return itemSetId;
    }

    public void setItemSetId(Long itemSetId) {
        this.itemSetId = itemSetId;
    }
}
