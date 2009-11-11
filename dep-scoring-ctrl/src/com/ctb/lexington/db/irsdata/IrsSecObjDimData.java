package com.ctb.lexington.db.irsdata;
import com.ctb.lexington.db.record.Persistent;

/**
 * @author Rama_Rao
 *
 */
public class IrsSecObjDimData implements Persistent{

    private Long secObjid;
    private Long primObjid;
    private String name;
    private Long secObjIndex;
    private String secObjType;
    private Long numItems;
    private Long pointsPossible;
    private Long nationalAverage;
    private Long assessmentid;
        
    public Long getAssessmentid() {
        return assessmentid;
    }
    
    public void setAssessmentid(Long assessmentid) {
        this.assessmentid = assessmentid;
    }

    public boolean equals(Object arg0) {
		return 
			name.equals(((IrsSecObjDimData)arg0).getName()) &&
			primObjid.equals(((IrsSecObjDimData)arg0).getPrimObjid()) &&
			(secObjIndex == null || secObjIndex.equals(((IrsSecObjDimData)arg0).getSecObjIndex())) &&
			secObjType.equals(((IrsSecObjDimData)arg0).getSecObjType()) &&
			numItems.equals(((IrsSecObjDimData)arg0).getNumItems()) &&
			pointsPossible.equals(((IrsSecObjDimData)arg0).getPointsPossible()) &&
			assessmentid.equals(((IrsSecObjDimData)arg0).getAssessmentid()) &&
            (nationalAverage == null || nationalAverage.equals(((IrsSecObjDimData)arg0).getNationalAverage()));
	}

	public int hashCode() {
		return (int) secObjid.longValue();
	}
    
    public Long getSecObjid() {
        return secObjid;
    }

    public void setSecObjid(Long secObjid) {
        this.secObjid = secObjid;
    }

    public Long getPrimObjid() {
        return primObjid;
    }

    public void setPrimObjid(Long primObjid) {
        this.primObjid = primObjid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSecObjIndex() {
        return secObjIndex;
    }

    public void setSecObjIndex(Long secObjIndex) {
        this.secObjIndex = secObjIndex;
    }

    public String getSecObjType() {
        return secObjType;
    }

    public void setSecObjType(String secObjType) {
        this.secObjType = secObjType;
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

    public Long getNationalAverage() {
        return nationalAverage;
    }

    public void setNationalAverage(Long nationalAverage) {
        this.nationalAverage = nationalAverage;
    }
}

