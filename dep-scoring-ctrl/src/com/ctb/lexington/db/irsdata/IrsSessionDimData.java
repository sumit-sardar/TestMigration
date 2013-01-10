package com.ctb.lexington.db.irsdata;

import com.ctb.lexington.db.record.Persistent;
import java.util.Date;

/**
 * @author Rama_Rao
 *
 */
public class IrsSessionDimData implements Persistent {
	private Long sessionid;
    private Long numberOfStudents;
    private Long programid;
    private Date windowStartDate;
    private Date windowEndDate;
    private Long schedulerid;
    private Long showGE;

    

    public Long getSessionid() {
        return sessionid;
    }

    public void setSessionid(Long sessionid) {
        this.sessionid = sessionid;
    }

    public Long getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(Long numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public Long getProgramid() {
        return programid;
    }

    public void setProgramid(Long programid) {
        this.programid = programid;
    }

    public Date getWindowStartDate() {
        return windowStartDate;
    }

    public void setWindowStartDate(Date windowStartDate) {
        this.windowStartDate = windowStartDate;
    }

    public Date getWindowEndDate() {
        return windowEndDate;
    }

    public void setWindowEndDate(Date windowEndDate) {
        this.windowEndDate = windowEndDate;
    }

    public Long getSchedulerid() {
        return schedulerid;
    }

    public void setSchedulerid(Long schedulerid) {
        this.schedulerid = schedulerid;
    }
       
    public Long getShowGE() {
		return showGE;
	}

	public void setShowGE(Long showGE) {
		this.showGE = showGE;
	}

	public boolean equals(Object other) {
        return
            this.getSchedulerid().equals(((IrsSessionDimData) other).getSchedulerid()) &&
            this.getProgramid().equals(((IrsSessionDimData) other).getProgramid()) &&
            this.getNumberOfStudents().equals(((IrsSessionDimData) other).getNumberOfStudents()) &&
            this.getWindowStartDate().equals(((IrsSessionDimData) other).getWindowStartDate()) &&
            this.getWindowEndDate().equals(((IrsSessionDimData) other).getWindowEndDate());
    }

    public int hashCode() {
        return (int) sessionid.longValue();
    }
}
