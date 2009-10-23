package com.ctb.testSessionInfo.dto; 

public class PathNode implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;

    private Integer id = null;
    private String name = null;
    private Integer childrenNodeCount = null;
    private Integer sessionCount = null;
    private String categoryName = null;
    private String orgCode = null;
    private Integer categoryId = null;
    
    public PathNode() {}
    
    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getSessionCount() {
        return this.sessionCount;
    }
    public void setSessionCount(Integer sessionCount) {
        this.sessionCount = sessionCount;
    }
    public Integer getChildrenNodeCount() {
        return this.childrenNodeCount;
    }
    public void setChildrenNodeCount(Integer childrenNodeCount) {
        this.childrenNodeCount = childrenNodeCount;
    }
    public String getHasChildren() {
        
        if ((this.childrenNodeCount != null) && (this.childrenNodeCount.intValue() > 0)) 
            return "true";
        else
            return "false";
    }
    public String getCategoryName() {
        return this.categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public String getOrgCode() {
        return this.orgCode;
    }
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }
    public Integer getCategoryId() {
        return this.categoryId;
    }
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
} 
