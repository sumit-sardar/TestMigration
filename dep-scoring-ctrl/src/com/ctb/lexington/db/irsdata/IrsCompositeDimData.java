package com.ctb.lexington.db.irsdata;
import com.ctb.lexington.db.record.Persistent;

/**
 * @author Rama_Rao
 *
 */
public class IrsCompositeDimData implements Persistent{

    private Long compositeid;
    private String name;
    private Long compositeIndex;
    private String compositeType;
    private Long numItems;
    private Long pointsPossible;
    private Long subjectid;
    private Long assessmentid;
        
        public Long getAssessmentid() {
            return assessmentid;
        }
        
        public void setAssessmentid(Long assessmentid) {
            this.assessmentid = assessmentid;
        }
    
    public boolean equals(Object arg0) {
		return 
			name.equals(((IrsCompositeDimData)arg0).getName()) &&
			(compositeIndex == null || compositeIndex.equals(((IrsCompositeDimData)arg0).getCompositeIndex())) &&
			compositeType.equals(((IrsCompositeDimData)arg0).getCompositeType()) &&
            assessmentid.equals(((IrsCompositeDimData)arg0).getAssessmentid()) &&
			(numItems == null || numItems.equals(((IrsCompositeDimData)arg0).getNumItems())) &&
			(pointsPossible == null || pointsPossible.equals(((IrsCompositeDimData)arg0).getPointsPossible())) &&
			subjectid.equals(((IrsCompositeDimData)arg0).getSubjectid());
	}

	public int hashCode() {
		return (int) compositeid.longValue();
	}

	public Long getCompositeid() {
        return compositeid;
    }

    public void setCompositeid(Long compositeid) {
        this.compositeid = compositeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCompositeIndex() {
        return compositeIndex;
    }

    public void setCompositeIndex(Long compositeIndex) {
        this.compositeIndex = compositeIndex;
    }

    public String getCompositeType() {
        return compositeType;
    }

    public void setCompositeType(String compositeType) {
        this.compositeType = compositeType;
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

    public Long getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(Long subjectid) {
        this.subjectid = subjectid;
    }
}
