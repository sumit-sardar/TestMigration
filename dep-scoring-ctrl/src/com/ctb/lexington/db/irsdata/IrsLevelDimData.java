package com.ctb.lexington.db.irsdata;

import com.ctb.lexington.db.record.Persistent;
/**
 * @author Rama_Rao
 *
 */
public class IrsLevelDimData implements Persistent {

    private Long levelid;
    private String name;

    

    public Long getLevelid() {
        return levelid;
    }

    public void setLevelid(Long levelid) {
        this.levelid = levelid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
