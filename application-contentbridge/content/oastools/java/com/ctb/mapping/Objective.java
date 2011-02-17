package com.ctb.mapping;


import java.util.*;

import org.jdom.*;


public class Objective {
    protected String grade;
    protected String internalProductDisplayName;
    protected String name;  
    protected String curriculumID; 
    protected String nodeKey;
    protected String parentKey;
    protected Integer level;
    protected long itemSetID;
    protected String activationStatus = "";
    private Map levelNameMap;

    public long getItemSetID() {
        return itemSetID;
    }

    public void setItemSetID(long itemSetID) {
        this.itemSetID = itemSetID;
    }

    public String getActivationStatus() {
        return activationStatus;
    }

    public void setActivationStatus(String activationStatus) {
        this.activationStatus = activationStatus;
    }

    public Objective(String name, String nodeKey) {
        this.nodeKey = nodeKey;
        this.name = name;
    }
    public Objective(String name, String curriculumID, String nodeKey, String parentKey, Integer level, Map levelNameMap) {
        if (levelNameMap.get(level) == null) {
            throw new IllegalArgumentException("Level " + level
                    + " is not defined in the framework definition.");
        }
        this.name = name;
        this.curriculumID = curriculumID;
        this.nodeKey = nodeKey;
        this.parentKey = parentKey;
        this.level = level;
        this.levelNameMap = levelNameMap;
    }

    public String getNodeKey() {
        return nodeKey;
    }

    public String getParentKey() {
        return parentKey;
    }

    public String getName() {
        return name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
	
    public String getInternalProductDisplayName() {
        return internalProductDisplayName;
    }
	
    void setInternalProductDisplayName(String displayName) {
        this.internalProductDisplayName = displayName;
    }

    public String getCurriculumID() {
        return curriculumID;
    }

    public Integer getLevel() {
        return level;
    }

    public String getLevelName() {
        String s = (String) levelNameMap.get(getLevel());

        if (s == null) {
            throw new RuntimeException("No type name found for " + getLevel());
        }
        return s;
    }

    public Element asElement() {
        return new Element("Hierarchy").setAttribute("Name", getName()).setAttribute("CurriculumID", getCurriculumID()).setAttribute("Number", getNodeKey()).setAttribute("Type",
                getLevelName());
    }

    public boolean hasParent() {
        return !getParentKey().equals("0");
    }

    public String toString() {
        return "Objective[Name=" + getName() + ",CurriculumID="
                + getCurriculumID() + ",Key=" + getNodeKey() + ",Parent="
                + getParentKey() + ",Type=" + getLevel() + "]";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Objective)) {
            return false;
        }

        final Objective objective = (Objective) o;

        if (!curriculumID.equals(objective.curriculumID)) {
            return false;
        }
        if (!name.equals(objective.name)) {
            return false;
        }
        if (!nodeKey.equals(objective.nodeKey)) {
            return false;
        }
        if (!parentKey.equals(objective.parentKey)) {
            return false;
        }
        if (!level.equals(objective.level)) {
            return false;
        }
        if (grade != null && !grade.equals(objective.grade)) {
            return false;
        }
        if (grade == null && objective.grade != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = name.hashCode() ^ curriculumID.hashCode()
                ^ nodeKey.hashCode() ^ parentKey.hashCode() ^ level.hashCode();

        if (grade != null) {
            result ^= grade.hashCode();
        }
        if (internalProductDisplayName != null) {
            result ^= internalProductDisplayName.hashCode();
        }
        return result;
    }
    
    public void accept(ObjectiveVisitor visitor) {
    	visitor.visitObjective(this);
    }
}
