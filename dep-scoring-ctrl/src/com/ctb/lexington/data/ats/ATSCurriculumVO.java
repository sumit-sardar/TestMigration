package com.ctb.lexington.data.ats;

/**
 * <p>Title: ATSCurriculumVO</p>
 * <p>Description: value object for ATS Curriculum data</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: CTB/McGraw-Hill</p>
 * @author Nate Cohen
 * @version 1.0
 */

public class ATSCurriculumVO extends Object implements java.io.Serializable, java.lang.Cloneable {
    public static final String VO_LABEL       = "com.ctb.lexington.data.ats.ATSCurriculumVO";
    public static final String VO_ARRAY_LABEL = "com.ctb.lexington.data.ats.ATSCurriculumVO.array";

    private Integer curriculumDimId;
    private String  customer;
	private String  curriculumName;
	private String  curriculumVersion;
    private String  curriculumIndex;
    private String 	hierarchyLevel;
    private String  type;
    private String  name;
    private String  description;

    public ATSCurriculumVO() {
    }
    public Integer getCurriculumDimId() {
        return curriculumDimId;
    }
    public String getCurriculumName() {
        return curriculumName;
    }
    public String getCurriculumVersion() {
        return curriculumVersion;
    }
    public String getCurriculumIndex() {
        return curriculumIndex;
    }
    public String getCustomer() {
        return customer;
    }
    public String getDescription() {
        return description;
    }
    public String getHierarchyLevel() {
        return hierarchyLevel;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    public void setCurriculumDimId(Integer curriculumDimId) {
        this.curriculumDimId = curriculumDimId;
    }
    public void setCurriculumName(String curriculumName) {
        this.curriculumName = curriculumName;
    }
    public void setCurriculumVersion(String curriculumVersion) {
        this.curriculumVersion = curriculumVersion;
    }
    public void setCurriculumIndex(String curriculumIndex) {
        this.curriculumIndex = curriculumIndex;
    }
    public void setCustomer(String customer) {
        this.customer = customer;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setHierarchyLevel(String hierarchyLevel) {
        this.hierarchyLevel = hierarchyLevel;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setType(String type) {
        this.type = type;
    }
}