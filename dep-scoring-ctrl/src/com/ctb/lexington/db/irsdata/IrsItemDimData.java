package com.ctb.lexington.db.irsdata;
import com.ctb.lexington.db.record.Persistent;
 
/**
 * @author Rama_Rao
 *
 */
public class IrsItemDimData implements Persistent {

    private Long itemid;
    private Long secObjid;
    private String oasItemid;
    private String itemText;
    private Long itemIndex;
    private String itemType;
    private String correctResponse;
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
			secObjid.equals(((IrsItemDimData)arg0).getSecObjid()) &&
			oasItemid.equals(((IrsItemDimData)arg0).getOasItemid()) &&
			itemText.equals(((IrsItemDimData)arg0).getItemText()) &&
			itemIndex.equals(((IrsItemDimData)arg0).getItemIndex()) &&
			itemType.equals(((IrsItemDimData)arg0).getItemType()) &&
			(correctResponse == null || correctResponse.equals(((IrsItemDimData)arg0).getCorrectResponse())) &&
			pointsPossible.equals(((IrsItemDimData)arg0).getPointsPossible()) &&
			assessmentid.equals(((IrsItemDimData)arg0).getAssessmentid()) &&
            (nationalAverage == null || nationalAverage.equals(((IrsItemDimData)arg0).getNationalAverage()));
	}

	public int hashCode() {
        if(itemid != null)
            return (int) itemid.longValue();
        else return -1;
	}
    
    public Long getItemid() {
        return itemid;
    }

    public void setItemid(Long itemid) {
        this.itemid = itemid;
    }

    public Long getSecObjid() {
        return secObjid;
    }

    public void setSecObjid(Long secObjid) {
        this.secObjid = secObjid;
    }

    public String getOasItemid() {
        return oasItemid;
    }

    public void setOasItemid(String oasItemid) {
        this.oasItemid = oasItemid;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public Long getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(Long itemIndex) {
        this.itemIndex = itemIndex;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getCorrectResponse() {
        return correctResponse;
    }

    public void setCorrectResponse(String correctResponse) {
        this.correctResponse = correctResponse;
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
