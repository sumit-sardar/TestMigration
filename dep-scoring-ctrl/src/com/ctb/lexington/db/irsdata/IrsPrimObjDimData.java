package com.ctb.lexington.db.irsdata;
import com.ctb.lexington.db.record.Persistent;

/**
 * @author Rama_Rao
 *
 */
public class IrsPrimObjDimData implements Persistent{

    private Long primObjid;
    private Long contentAreaid;
    private String name;
    private Long primObjIndex;
    private String primObjType;
    private Long numItems;
    private Long pointsPossible;
    private Long assessmentid;
        
    public Long getAssessmentid() {
        return assessmentid;
    }
    
    public void setAssessmentid(Long assessmentid) {
        this.assessmentid = assessmentid;
    }

    public boolean equals(Object arg0) {
		return 
			name.equals(((IrsPrimObjDimData)arg0).getName()) &&
			contentAreaid.equals(((IrsPrimObjDimData)arg0).getContentAreaid()) &&
			(primObjIndex == null || primObjIndex.equals(((IrsPrimObjDimData)arg0).getPrimObjIndex())) &&
			primObjType.equals(((IrsPrimObjDimData)arg0).getPrimObjType()) &&
			numItems.equals(((IrsPrimObjDimData)arg0).getNumItems()) &&
			pointsPossible.equals(((IrsPrimObjDimData)arg0).getPointsPossible()) &&
			assessmentid.equals(((IrsPrimObjDimData)arg0).getAssessmentid());
	}

	public int hashCode() {
		return (int) primObjid.longValue();
	}
    
    public Long getPrimObjid() {
        return primObjid;
    }

    public void setPrimObjid(Long primObjid) {
        this.primObjid = primObjid;
    }

    public Long getContentAreaid() {
        return contentAreaid;
    }

    public void setContentAreaid(Long contentAreaid) {
        this.contentAreaid = contentAreaid;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Long getPrimObjIndex() {
        return primObjIndex;
    }

    public void setPrimObjIndex(Long primObjIndex) {
        this.primObjIndex = primObjIndex;
    }

    public String getPrimObjType() {
        return primObjType;
    }

    public void setPrimObjType(String primObjType) {
        this.primObjType = primObjType;
    }

    public Long getNumItems() {
        return numItems;
    }

    public void setNumItems(Long numItems) {
        this.numItems = numItems;
    }

    public Long getPointsPossible() {
        return pointsPossible;
    }

    public void setPointsPossible(Long pointsPossible) {
        this.pointsPossible = pointsPossible;
    }
}