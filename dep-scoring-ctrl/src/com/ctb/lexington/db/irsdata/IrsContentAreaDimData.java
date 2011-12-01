package com.ctb.lexington.db.irsdata;
import com.ctb.lexington.db.record.Persistent;
/**
 * @author Rama_Rao
 *
 */
public class IrsContentAreaDimData implements Persistent {
	 	private Long contentAreaid;
	    private String name;
	    private Long contentAreaIndex;
	    private String contentAreaType;
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
				name.equals(((IrsContentAreaDimData)arg0).getName()) &&
				contentAreaIndex.equals(((IrsContentAreaDimData)arg0).getContentAreaIndex()) &&
				contentAreaType.equals(((IrsContentAreaDimData)arg0).getContentAreaType()) &&
				(numItems == null || numItems.equals(((IrsContentAreaDimData)arg0).getNumItems())) &&
				(pointsPossible == null || pointsPossible.equals(((IrsContentAreaDimData)arg0).getPointsPossible())) &&
				assessmentid.equals(((IrsContentAreaDimData)arg0).getAssessmentid()) &&
                subjectid.equals(((IrsContentAreaDimData)arg0).getSubjectid());
		}

		public int hashCode() {
			return (int) contentAreaid.longValue();
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

	    public Long getContentAreaIndex() {
	        return contentAreaIndex;
	    }

	    public void setContentAreaIndex(Long contentAreaIndex) {
	        this.contentAreaIndex = contentAreaIndex;
	    }

	    public String getContentAreaType() {
	        return contentAreaType;
	    }

	    public void setContentAreaType(String contentAreaType) {
	        this.contentAreaType = contentAreaType;
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
