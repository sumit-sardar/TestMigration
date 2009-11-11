package com.ctb.lexington.db.record;

/*
 * Generated class: do NOT format or modify in any way!
 */
public class StudentResearchDataRecord implements Persistent {
    public static final String TABLE_NAME = "STS_STUDENT_RESEARCH_DATA";

    public static final String STUDENT_DIM_ID = "STUDENT_DIM_ID";
    public static final String STUDENT_DIM_VERSION_ID = "STUDENT_DIM_VERSION_ID";
    public static final String DEMO_CATEGORY = "DEMO_CATEGORY";
    public static final String DEMO_VALUE = "DEMO_VALUE";
    public static final String ADMIN_DIM_ID = "ADMIN_DIM_ID";

    private Long studentDimId;
    private Long studentDimVersionId;
    private String demoCategory;
    private String demoValue;
    private Long adminDimId;

    public Long getStudentDimId() {
        return studentDimId;
    }

    public void setStudentDimId(Long studentDimId) {
        this.studentDimId = studentDimId;
    }

    public Long getStudentDimVersionId() {
        return studentDimVersionId;
    }

    public void setStudentDimVersionId(Long studentDimVersionId) {
        this.studentDimVersionId = studentDimVersionId;
    }

    public String getDemoCategory() {
        return demoCategory;
    }

    public void setDemoCategory(String demoCategory) {
        this.demoCategory = demoCategory;
    }

    public String getDemoValue() {
        return demoValue;
    }

    public void setDemoValue(String demoValue) {
        this.demoValue = demoValue;
    }

    public Long getAdminDimId() {
        return adminDimId;
    }

    public void setAdminDimId(Long adminDimId) {
        this.adminDimId = adminDimId;
    }
}