package com.ctb.lexington.db.irsdata;

import com.ctb.lexington.db.record.Persistent;

/**
 * @author Rama_Rao
 *
 */
public class IrsRecActivityDimData implements Persistent{
	
	private Long recActivityid;
    private String name;

    public Long getRecActivityid() {
        return recActivityid;
    }

    public void setRecActivityid(Long recActivityid) {
        this.recActivityid = recActivityid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
