package com.ctb.lexington.db.irsdata;

import com.ctb.lexington.db.record.Persistent;

/**
 * @author Rama_Rao
 *
 */
public class IrsSchedulerDimData implements Persistent {
	private Long schedulerid;
    private String name;

    public Long getSchedulerid() {
        return schedulerid;
    }
    public void setSchedulerid(Long schedulerid) {
        this.schedulerid = schedulerid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean equals(Object other) {
        return
            this.getSchedulerid().equals(((IrsSchedulerDimData) other).getSchedulerid()) &&
            this.getName().equals(((IrsSchedulerDimData) other).getName());
    }

    public int hashCode() {
        return (int) schedulerid.longValue();
    }
}