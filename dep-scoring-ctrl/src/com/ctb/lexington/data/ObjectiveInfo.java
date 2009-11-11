package com.ctb.lexington.data;

/**
 * @author Tai Truong
 */
public class ObjectiveInfo implements java.io.Serializable 
{
    public static final String PRODUCT_TYPE = "product";
    public static final String OBJECTIVE_TYPE = "objective";
    
    private Integer id = new Integer(0);
    private String name = "";
    private String type = "";
    private String levelName = "";
    private Integer parentId = new Integer(0);
    private String parentName = "";
    private int itemCount = -1;    
    private int rollupItemCount = -1;    
    private boolean hasChildren = false;
    private Integer itemSetLevel = new Integer(0);
    private Integer reportingLevel = new Integer(0);
    
    public Integer getItemSetLevel() {
        return itemSetLevel;
    }
    public void setItemSetLevel(Integer itemSetLevel) {
        this.itemSetLevel = itemSetLevel;
    }
    public Integer getReportingLevel() {
        return reportingLevel;
    }
    public void setReportingLevel(Integer reportingLevel) {
        this.reportingLevel = reportingLevel;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public int getItemCount() {
        return itemCount;
    }
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
    public int getRollupItemCount() {
        return rollupItemCount;
    }
    public void setRollupItemCount(int rollupItemCount) {
        this.rollupItemCount = rollupItemCount;
    }
    public String getLevelName() {
        return levelName;
    }
    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getParentId() {
        return parentId;
    }
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
    public String getParentName() {
        return parentName;
    }
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
    public boolean isReportable() {
        return this.itemSetLevel.intValue() >= this.reportingLevel.intValue();
    }
    public boolean getHasChildren() {
        return hasChildren;
    }
    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
 }
