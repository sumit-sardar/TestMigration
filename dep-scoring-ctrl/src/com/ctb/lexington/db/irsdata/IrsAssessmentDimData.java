package com.ctb.lexington.db.irsdata; 


import com.ctb.lexington.db.record.Persistent;

/**
 * @author Rama_Rao
 *
 */
public class IrsAssessmentDimData implements Persistent{

    private String name;
    private Long productid;
    private Long assessmentid;
    private String type;
    private Long formid;
    private Long levelid;
    private Long productTypeid;
    
    public Long getProductTypeid() {
        return this.productTypeid;
    }

    public void setProductTypeid(Long productTypeid) {
        this.productTypeid = productTypeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProductid() {
        return productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }
    
    public Long getFormid() {
        return formid;
    }

    public void setFormid(Long formid) {
        this.formid = formid;
    }
    
    public Long getLevelid() {
        return levelid;
    }

    public void setLevelid(Long levelid) {
        this.levelid = levelid;
    }

    public Long getAssessmentid() {
        return assessmentid;
    }

    public void setAssessmentid(Long assessmentid) {
        this.assessmentid = assessmentid;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}   
	public boolean equals(Object other) {
        return
            this.getName().equals(((IrsAssessmentDimData) other).getName()) &&
            this.getProductid().equals(((IrsAssessmentDimData) other).getProductid()) &&
            this.getType().equals(((IrsAssessmentDimData) other).getType()) &&
            this.getFormid().equals(((IrsAssessmentDimData) other).getFormid()) &&
            this.getLevelid().equals(((IrsAssessmentDimData) other).getLevelid());
    }

    public int hashCode() {
        return (int) assessmentid.longValue();
    }
}