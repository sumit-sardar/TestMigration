package com.ctb.lexington.db.irsdata.irslldata;

import com.ctb.lexington.db.record.Persistent;
import java.util.Date;

/**
 * @author TCS
 *
 */
public class IrsLLSecObjFactData implements Persistent{

    private Long factid;
    private Long secObjid;
    private Long orgNodeid;
    private Long studentid;
    private Long formid;
    private Long sessionid;
    private Long levelid;
    private Long attr2id;
    private Long attr11id;
    private Long attr12id;
    private Long attr13id;
    private Long attr14id;
    private Long attr15id;
    private Long attr16id;
    private Long gradeid;
    private Long attr9id;
    private Long pointsAttempted;
    private Float percentObtained;
    private Long masteryLevelid;
    private Date testStartTimestamp;
    private Date testCompletionTimestamp;
    private Long assessmentid;
    private Long pointsObtained;
    private Long pointsPossible;
    private Long programid;
    private Long currentResultid;
    private String subtestName;
 // Added for LasLink - START 
	private Long attr17id; // HOME_LANGUAGE
	private Long attr18id; // MOBILITY
	private Long attr19id; // USA_SCHOOL_ENROLLMENT
	private String attr20id; // PROGRAM_PARTICIPATION
	private String attr21id; // SPECIAL_EDUCATION
	private Long attr22id; // DISABILITY
	private String attr23id; // ACCOMMODATIONS
	private Long attr25id; // SPECIAL_CODES-K
	private Long attr26id; // SPECIAL_CODES-L
	private Long attr27id; // SPECIAL_CODES-M
	private Long attr28id; // SPECIAL_CODES-N
	private Long attr29id; // SPECIAL_CODES-O
	private Long attr30id; // SPECIAL_CODES-P
	private Long attr31id; // SPECIAL_CODES-Q
	private Long attr32id; // SPECIAL_CODES-R
	private Long attr33id; // SPECIAL_CODES-S
	private Long attr34id; // SPECIAL_CODES-T
	private Long attr35id; // MUSIC_FILE_ID
	private Long attr36id; // MASKING_RULER
	private Long attr37id; // MAGNIFYING_GLASS
	// Added for LasLink - END


    public boolean equals(Object arg0) {
			return 
				secObjid.equals(((IrsLLSecObjFactData)arg0).getSecObjid()) &&
                orgNodeid.equals(((IrsLLSecObjFactData)arg0).getOrgNodeid()) &&
                studentid.equals(((IrsLLSecObjFactData)arg0).getStudentid()) &&
                formid.equals(((IrsLLSecObjFactData)arg0).getFormid()) &&
                sessionid.equals(((IrsLLSecObjFactData)arg0).getSessionid()) &&
                attr2id.equals(((IrsLLSecObjFactData)arg0).getAttr2id()) &&
                attr11id.equals(((IrsLLSecObjFactData)arg0).getAttr11id()) &&
                attr12id.equals(((IrsLLSecObjFactData)arg0).getAttr12id()) &&
                attr13id.equals(((IrsLLSecObjFactData)arg0).getAttr13id()) &&
                attr14id.equals(((IrsLLSecObjFactData)arg0).getAttr14id()) &&
                attr15id.equals(((IrsLLSecObjFactData)arg0).getAttr15id()) &&
                attr16id.equals(((IrsLLSecObjFactData)arg0).getAttr16id()) &&
                gradeid.equals(((IrsLLSecObjFactData)arg0).getGradeid()) &&
                attr9id.equals(((IrsLLSecObjFactData)arg0).getAttr9id()) &&
                testStartTimestamp.equals(((IrsLLSecObjFactData)arg0).getTestStartTimestamp()) &&
                testCompletionTimestamp.equals(((IrsLLSecObjFactData)arg0).getTestCompletionTimestamp()) &&
                assessmentid.equals(((IrsLLSecObjFactData)arg0).getAssessmentid()) &&
                pointsPossible.equals(((IrsLLSecObjFactData)arg0).getPointsPossible()) &&
                programid.equals(((IrsLLSecObjFactData)arg0).getProgramid()) &&
                currentResultid.equals(((IrsLLSecObjFactData)arg0).getCurrentResultid()) &&
                pointsAttempted.equals(((IrsLLSecObjFactData)arg0).getPointsAttempted()) &&
                pointsObtained.equals(((IrsLLSecObjFactData)arg0).getPointsObtained()) &&
                masteryLevelid.equals(((IrsLLSecObjFactData)arg0).getMasteryLevelid()) &&
                attr17id.equals(((IrsLLSecObjFactData)arg0).getAttr17id()) &&
                attr18id.equals(((IrsLLSecObjFactData)arg0).getAttr18id()) &&
                attr19id.equals(((IrsLLSecObjFactData)arg0).getAttr19id()) &&
                attr20id.equals(((IrsLLSecObjFactData)arg0).getAttr20id()) &&
                attr21id.equals(((IrsLLSecObjFactData)arg0).getAttr21id()) &&
                attr22id.equals(((IrsLLSecObjFactData)arg0).getAttr22id()) &&
                attr23id.equals(((IrsLLSecObjFactData)arg0).getAttr23id()) &&
                attr25id.equals(((IrsLLSecObjFactData)arg0).getAttr25id()) &&
                attr26id.equals(((IrsLLSecObjFactData)arg0).getAttr26id()) &&
                attr27id.equals(((IrsLLSecObjFactData)arg0).getAttr27id()) &&
                attr28id.equals(((IrsLLSecObjFactData)arg0).getAttr28id()) &&
                attr29id.equals(((IrsLLSecObjFactData)arg0).getAttr29id()) &&
                attr30id.equals(((IrsLLSecObjFactData)arg0).getAttr30id()) &&
                attr31id.equals(((IrsLLSecObjFactData)arg0).getAttr31id()) &&
                attr32id.equals(((IrsLLSecObjFactData)arg0).getAttr32id()) &&
                attr33id.equals(((IrsLLSecObjFactData)arg0).getAttr33id()) &&
                attr34id.equals(((IrsLLSecObjFactData)arg0).getAttr34id()) &&
                attr35id.equals(((IrsLLSecObjFactData)arg0).getAttr35id()) &&
                attr36id.equals(((IrsLLSecObjFactData)arg0).getAttr36id()) &&
                attr37id.equals(((IrsLLSecObjFactData)arg0).getAttr37id());
    }
    
    public Date getTestStartTimestamp() {
        return testStartTimestamp;
    }

    public void setTestStartTimestamp(Date testStartTimestamp) {
        this.testStartTimestamp = testStartTimestamp;
    }

    public int hashCode() {
        return (int) factid.longValue();
    }

    public Long getFactid() {
        return factid;
    }

    public void setFactid(Long factid) {
        this.factid = factid;
    }

    public Long getSecObjid() {
        return secObjid;
    }

    public void setSecObjid(Long secObjid) {
        this.secObjid = secObjid;
    }

    public Long getOrgNodeid() {
        return orgNodeid;
    }

    public void setOrgNodeid(Long orgNodeid) {
        this.orgNodeid = orgNodeid;
    }

    public Long getStudentid() {
        return studentid;
    }

    public void setStudentid(Long studentid) {
        this.studentid = studentid;
    }

    public Long getFormid() {
        return formid;
    }

    public void setFormid(Long formid) {
        this.formid = formid;
    }

    public Long getSessionid() {
        return sessionid;
    }

    public void setSessionid(Long sessionid) {
        this.sessionid = sessionid;
    }

    public Long getLevelid() {
        return levelid;
    }

    public void setLevelid(Long levelid) {
        this.levelid = levelid;
    }

    public Long getAttr2id() {
        return attr2id;
    }

    public void setAttr2id(Long attr2id) {
        this.attr2id = attr2id;
    }

    public Long getGradeid() {
        return gradeid;
    }

    public void setGradeid(Long gradeid) {
        this.gradeid = gradeid;
    }

    public Long getAttr9id() {
        return attr9id;
    }

    public void setAttr9id(Long attr9id) {
        this.attr9id = attr9id;
    }
    
    public Long getPointsAttempted() {
        return pointsAttempted;
    }

    public void setPointsAttempted(Long pointsAttempted) {
        this.pointsAttempted = pointsAttempted;
    }

    /**
	 * @return the percentObtained
	 */
	public Float getPercentObtained() {
		return percentObtained;
	}

	/**
	 * @param percentObtained the percentObtained to set
	 */
	public void setPercentObtained(Float percentObtained) {
		this.percentObtained = percentObtained;
	}

	public Long getMasteryLevelid() {
        return masteryLevelid;
    }

    public void setMasteryLevelid(Long masteryLevelid) {
        this.masteryLevelid = masteryLevelid;
    }

    public Date getTestCompletionTimestamp() {
        return testCompletionTimestamp;
    }

    public void setTestCompletionTimestamp(Date testCompletionTimestamp) {
        this.testCompletionTimestamp = testCompletionTimestamp;
    }

    public Long getAssessmentid() {
        return assessmentid;
    }

    public void setAssessmentid(Long assessmentid) {
        this.assessmentid = assessmentid;
    }

    public Long getPointsObtained() {
        return pointsObtained;
    }

    public void setPointsObtained(Long pointsObtained) {
        this.pointsObtained = pointsObtained;
    }

    public Long getPointsPossible() {
        return pointsPossible;
    }

    public void setPointsPossible(Long pointsPossible) {
        this.pointsPossible = pointsPossible;
    }

    public Long getProgramid() {
        return programid;
    }

    public void setProgramid(Long programid) {
        this.programid = programid;
    }

    public Long getCurrentResultid() {
        return currentResultid;
    }

    public void setCurrentResultid(Long currentResultid) {
        this.currentResultid = currentResultid;
    }

	public Long getAttr11id() {
		return attr11id;
	}

	public void setAttr11id(Long attr11id) {
		this.attr11id = attr11id;
	}

	public Long getAttr12id() {
		return attr12id;
	}

	public void setAttr12id(Long attr12id) {
		this.attr12id = attr12id;
	}

	public Long getAttr13id() {
		return attr13id;
	}

	public void setAttr13id(Long attr13id) {
		this.attr13id = attr13id;
	}

	public Long getAttr14id() {
		return attr14id;
	}

	public void setAttr14id(Long attr14id) {
		this.attr14id = attr14id;
	}

	public Long getAttr15id() {
		return attr15id;
	}

	public void setAttr15id(Long attr15id) {
		this.attr15id = attr15id;
	}

	public Long getAttr16id() {
		return attr16id;
	}

	public void setAttr16id(Long attr16id) {
		this.attr16id = attr16id;
	}
    public String getSubtestName() {
        return this.subtestName;
    }
    public void setSubtestName(String subtestName) {
        this.subtestName = subtestName;
    }

	public Long getAttr17id() {
		return attr17id;
	}

	public void setAttr17id(Long attr17id) {
		this.attr17id = attr17id;
	}

	public Long getAttr18id() {
		return attr18id;
	}

	public void setAttr18id(Long attr18id) {
		this.attr18id = attr18id;
	}

	public Long getAttr19id() {
		return attr19id;
	}

	public void setAttr19id(Long attr19id) {
		this.attr19id = attr19id;
	}

	public String getAttr20id() {
		return attr20id;
	}

	public void setAttr20id(String attr20id) {
		this.attr20id = attr20id;
	}

	public String getAttr21id() {
		return attr21id;
	}

	public void setAttr21id(String attr21id) {
		this.attr21id = attr21id;
	}

	public Long getAttr22id() {
		return attr22id;
	}

	public void setAttr22id(Long attr22id) {
		this.attr22id = attr22id;
	}

	public String getAttr23id() {
		return attr23id;
	}

	public void setAttr23id(String attr23id) {
		this.attr23id = attr23id;
	}

	public Long getAttr25id() {
		return attr25id;
	}

	public void setAttr25id(Long attr25id) {
		this.attr25id = attr25id;
	}

	public Long getAttr26id() {
		return attr26id;
	}

	public void setAttr26id(Long attr26id) {
		this.attr26id = attr26id;
	}

	public Long getAttr27id() {
		return attr27id;
	}

	public void setAttr27id(Long attr27id) {
		this.attr27id = attr27id;
	}

	public Long getAttr28id() {
		return attr28id;
	}

	public void setAttr28id(Long attr28id) {
		this.attr28id = attr28id;
	}

	public Long getAttr29id() {
		return attr29id;
	}

	public void setAttr29id(Long attr29id) {
		this.attr29id = attr29id;
	}

	public Long getAttr30id() {
		return attr30id;
	}

	public void setAttr30id(Long attr30id) {
		this.attr30id = attr30id;
	}

	public Long getAttr31id() {
		return attr31id;
	}

	public void setAttr31id(Long attr31id) {
		this.attr31id = attr31id;
	}

	public Long getAttr32id() {
		return attr32id;
	}

	public void setAttr32id(Long attr32id) {
		this.attr32id = attr32id;
	}

	public Long getAttr33id() {
		return attr33id;
	}

	public void setAttr33id(Long attr33id) {
		this.attr33id = attr33id;
	}

	public Long getAttr34id() {
		return attr34id;
	}

	public void setAttr34id(Long attr34id) {
		this.attr34id = attr34id;
	}

	public Long getAttr35id() {
		return attr35id;
	}

	public void setAttr35id(Long attr35id) {
		this.attr35id = attr35id;
	}

	public Long getAttr36id() {
		return attr36id;
	}

	public void setAttr36id(Long attr36id) {
		this.attr36id = attr36id;
	}

	public Long getAttr37id() {
		return attr37id;
	}

	public void setAttr37id(Long attr37id) {
		this.attr37id = attr37id;
	}
}
